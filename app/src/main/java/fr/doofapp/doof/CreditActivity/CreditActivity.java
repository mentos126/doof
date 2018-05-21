package fr.doofapp.doof.CreditActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;

public class CreditActivity extends AppCompatActivity {


    private Button minus, plus, validate;
    private TextView how_many, sold;

    private int nbTikets;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(CreditActivity.this, new HttpClientStack(mHttpClient));

        getUserData();

        sold = (TextView) findViewById(R.id.prompt_sold);

        nbTikets = 0;

        how_many = (TextView) findViewById(R.id.prompt_how_many);
        String s = ""+nbTikets;
        how_many.setText(s);

        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMinusAction();
            }
        });

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPlusAction();
            }
        });

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidateAction();
            }
        });

    }

    private void doValidateAction() {
        //TODO url request addTickets

        User u = null;
        db.open();
        u = db.getUserConnected();
        db.close();
        String URL = URLProject.getInstance().getCREDIT()+"/"+u.getToken();

        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("value", nbTikets);
        }catch (JSONException e){
            e.printStackTrace();
        }

        dialog = ProgressDialog.show(this, "", "", true);
        mQueue.add(createRequest(URL, jsonBodyObj));


    }


    protected void getUserData(){
        db.open();
        User u = db.getUserConnected();
        db.close();
        String URL = URLProject.getInstance().getGET_NB_TIKET()+"/"+u.getToken();
        dialog = ProgressDialog.show(this, "", "", true);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("FINALISEACTIVITY", response.toString());
                        dialog.dismiss();
                        try {

                            if(!response.isNull("error")){
                                Toast.makeText(CreditActivity.this, "ERREUR SERVEUR",Toast.LENGTH_LONG).show();
                            }else {
                                int t = parseInt(response.get("result").toString());
                                String s= getResources().getString(R.string.prompt_sold) +t;
                                sold.setText(s);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "ERREUR IMPOSSIBLE", Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

    }

    public void doMinusAction(){
        if (nbTikets <= 0){
            nbTikets = 0;
        }else{
            nbTikets--;
        }
        String s = nbTikets+"";
        how_many.setText(s);
    }

    public void doPlusAction(){
        if (nbTikets <= 0){
            nbTikets = 1;
        }else {
            nbTikets++;
        }
        String s = nbTikets+"";
        how_many.setText(s);

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

                    if(response.isNull("error")){
                        dialog.dismiss();
                        Intent intent = new Intent(CreditActivity.this, BottomActivity.class);
                        intent.putExtra("Tab",4);
                        startActivity(intent);
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
