package fr.doofapp.doof.LoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.R;

public class RegisterActivity extends AppCompatActivity {

    EditText family_name;
    EditText first_name;
    EditText email;
    EditText pass1;
    EditText pass2;
    EditText adress;
    EditText phone;
    Button validate;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(RegisterActivity.this, new HttpClientStack(mHttpClient));

        Button buttonIgnore = (Button) findViewById(R.id.go_to_login);
        buttonIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        family_name = (EditText) findViewById(R.id.family_name);
        first_name = (EditText) findViewById(R.id.first_name);
        email = (EditText) findViewById(R.id.email);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        adress = (EditText) findViewById(R.id.adress);
        phone = (EditText) findViewById(R.id.phone);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActionValidate();
            }
        });

    }

    private void doActionValidate() {
        if(!family_name.getText().toString().equals("")){
            if(!first_name.getText().toString().equals("")){
                if(!email.getText().toString().equals("") && email.getText().toString().contains("@")){
                    if(pass1.getText().toString().length() >= 8 && pass1.getText().toString().equals(pass2.getText().toString())){
                        if(!adress.getText().toString().equals("")){
                                //TODO request register account
                            JSONObject jsonBodyObj = new JSONObject();
                            try{
                                jsonBodyObj.put("id", email.getText().toString());
                                jsonBodyObj.put("pwd", pass1.getText().toString());
                                jsonBodyObj.put("prenom", first_name.getText().toString());
                                jsonBodyObj.put("nom", family_name.getText().toString());
                                jsonBodyObj.put("adresse", adress.getText().toString());
                                jsonBodyObj.put("telephone", phone.getText().toString());
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            String URL = URLProject.getInstance().getREGISTER();
                            mQueue.add(createRequest(URL, jsonBodyObj, 0));

                        }else {
                            Toast.makeText(this, R.string.prompt_error_adress,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.prompt_error_password,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,R.string.prompt_error_email,Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, R.string.prompt_error_firstname,Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, R.string.prompt_error_familyname,Toast.LENGTH_LONG).show();
        }

    }


    private JsonObjectRequest createRequest(String URL, JSONObject jsonObject, final int who)  {
        JSONObject jsonBodyObj =  jsonObject;
        final String requestBody = jsonBodyObj.toString();
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                URL, jsonBodyObj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                CookieStore cs = mHttpClient.getCookieStore();
                BasicClientCookie c = (BasicClientCookie) getCookie(cs, "cookie");
                if (c != null) {
                    setTvCookieText(c.getValue());
                }
                cs.addCookie(c);
                Log.d("SUCCESS LISTENER", response.toString());
                try {
                    VolleyLog.v("Response:%n %s", response.toString(4));

                    /*MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] hash = digest.digest(mPassword.getBytes("UTF-8"));
                    String resPass = convertByteArrayToHexString(hash);*/

                    if(response.isNull("error")){
                        switch (who){
                            case 0: {
                                JSONObject jsonBodyObj = new JSONObject();
                                try{
                                    jsonBodyObj.put("id", email.getText().toString());
                                    jsonBodyObj.put("pwd", pass1.getText().toString());
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                String URL = URLProject.getInstance().getLOGIN();
                                mQueue.add(createRequest(URL, jsonBodyObj, 1));
                                break;
                            }
                            case 1:{
                                User u = null;
                                u = new User(email.getText().toString(),
                                        pass1.getText().toString(),
                                        response.get("token").toString(),
                                        1,
                                        1);
                                db.open();
                                db.addUser(u);
                                db.close();

                                Intent intent = new Intent(RegisterActivity.this, BottomActivity.class);
                                startActivity(intent);

                                break;
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        return JOPR;
    }

    private void setTvCookieText(String str) {}

    public Cookie getCookie(CookieStore cs, String cookieName) {
        Cookie ret = null;
        List<Cookie> l = cs.getCookies();
        for (Cookie c : l) {
            if (c.getName().equals(cookieName)) {
                ret = c;
                break;
            }
        }
        return ret;
    }


}
