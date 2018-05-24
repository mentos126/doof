package fr.doofapp.doof.UpdateProfileActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText pass_actual, pass1, pass2;
    Button validata;

    String old, mdp1, mdp2;

    AbstractHttpClient mHttpClient;
    RequestQueue mQueue;
    Dialog dialog;

    UserDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        db = new UserDAO(this);
        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(UpdatePasswordActivity.this, new HttpClientStack(mHttpClient));

        pass_actual = (EditText) findViewById(R.id.pass_actual);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);

        validata = (Button) findViewById(R.id.validate);
        validata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("========mdp1=======",pass1.getText().toString());
                Log.e("========mdp2=======",pass2.getText().toString());
                Log.e("========mdp3=======",pass1.getText().toString().equals(pass2.getText().toString())+"");
                Log.e("========mdp4=======",pass1.getText().toString().length()+"");
                if(pass1.getText().toString().equals(pass2.getText().toString()) && pass1.getText().toString().length() > 7){

                    db.open();
                    User u  = db.getUserConnected();
                    db.close();

                    byte[] hash = new byte[0];
                    MessageDigest digest = null;
                    MessageDigest digest2 = null;
                    String resPass = null;
                    try {
                        digest = MessageDigest.getInstance("SHA-256");
                        hash = digest.digest(pass1.getText().toString().getBytes("UTF-8"));
                        resPass = convertByteArrayToHexString(hash);
                        digest2 = MessageDigest.getInstance("SHA-256");
                        hash = digest2.digest(pass_actual.getText().toString().getBytes("UTF-8"));
                        resPass = convertByteArrayToHexString(hash);
                    }catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    Log.e("========DIGEST========",resPass);

                    JSONObject jsonBodyObj = new JSONObject();
                    try{
                        jsonBodyObj.put("token",u.getToken());
                        jsonBodyObj.put("password_old", digest2);
                        jsonBodyObj.put("paswword_new", digest);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    String URL = URLProject.getInstance().getCHANGE_PASSWORD()+"/"+u.getToken();

                    dialog = ProgressDialog.show(UpdatePasswordActivity.this, "", "", true);
                    mQueue.add(createRequest(URL, jsonBodyObj));

                }else{
                    Toast.makeText(UpdatePasswordActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }


    private JsonObjectRequest createRequest(String URL, JSONObject jsonObject)  {
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

                    dialog.dismiss();
                    if(response.isNull("error")){
                        Intent intent = new Intent(UpdatePasswordActivity.this, BottomActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(UpdatePasswordActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"ERREUR IMPOSSIBLE",Toast.LENGTH_LONG).show();
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
