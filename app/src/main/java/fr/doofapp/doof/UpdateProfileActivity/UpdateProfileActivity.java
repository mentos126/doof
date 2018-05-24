package fr.doofapp.doof.UpdateProfileActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.ClassMetier.ProfileCache;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.IsConnectedActivity;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;

public class UpdateProfileActivity extends AppCompatActivity {

    private UserDAO db;
    private User u;
    private Profile mProfile;

    private EditText familyName;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText adress;

    private ImageView iv;
    private Button button;
    private Button photo;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        if (!isConnected()){
            Intent myIntent = new Intent(UpdateProfileActivity.this, IsConnectedActivity.class);
            startActivity(myIntent);
        }

        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(UpdateProfileActivity.this, new HttpClientStack(mHttpClient));

        mProfile = ProfileCache.getInstance().getProfile();
        Log.e("=====UPDATE=====",mProfile.getName());

        familyName = (EditText) findViewById(R.id.edit_family_name);
        familyName.setText(mProfile.getFamilyName());

        name = (EditText) findViewById(R.id.edit_name);
        name.setText(mProfile.getName());


        db.open();
        u = null;
        u = db.getUserConnected();
        db.close();
        email = (EditText) findViewById(R.id.edit_email);
        email.setText(u.getUserId());

        familyName = (EditText) findViewById(R.id.edit_phone);
        familyName.setText(mProfile.getPhone());

        familyName = (EditText) findViewById(R.id.edit_adress);
        familyName.setText(mProfile.getAdress());

        iv = (ImageView) findViewById(R.id.prompt_photo);
        new DownLoadImageTask(iv).execute(mProfile.getPhoto());

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButton();
            }
        });

        photo = (Button) findViewById(R.id.changePhoto);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(UpdateProfileActivity.this, UpdateProfilePhotoActivity.class);
                myIntent.putExtra("Profile", (Serializable) mProfile);
                startActivity(myIntent);
            }
        });

        getUserProfile();

    }

    private void getUserProfile() {

        db.open();
        User u = db.getUserConnected();
        db.close();
        String URL = URLProject.getInstance().getMY_PROFILE_Meal()+"/"+u.getToken();
        dialog = ProgressDialog.show(UpdateProfileActivity.this, "", "", true);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.e("=========MEALS========", response.toString());
                        try {

                            if(response.isNull("error")){

                                JSONObject res = response.getJSONObject("result");



                            }else{
                                Toast.makeText(UpdateProfileActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        VolleyLog.e("=========MEALS========", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(UpdateProfileActivity.this).addToRequestQueue(jsonObjectReq, URL);


    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected void actionButton(){
        Log.e("====VALIDER===","VALIDER");
        if (!isConnected()){
            Intent myIntent = new Intent(UpdateProfileActivity.this, IsConnectedActivity.class);
            startActivity(myIntent);
        }else{
            //TODO SEND REQUEST FOR UPDATE PROFILE

            JSONObject jsonBodyObj = new JSONObject();
            try{
                jsonBodyObj.put("id", email.getText().toString());
            }catch (JSONException e){
                e.printStackTrace();
            }

            //u is instancied
            String URL = "/"+u.getToken();

            dialog = ProgressDialog.show(this, "", "", true);
            mQueue.add(createRequest(URL, jsonBodyObj));

        }
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

                        Intent myIntent = new Intent(UpdateProfileActivity.this, BottomActivity.class);
                        myIntent.putExtra("Tab", R.id.navigation_profil);
                        startActivity(myIntent);

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
