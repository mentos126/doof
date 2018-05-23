package fr.doofapp.doof.UpdateProfileActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.ClassMetier.ProfileCache;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

public class UpdateProfilePhotoActivity extends AppCompatActivity {

    private ImageView iv;
    private Button validate;
    private Button searchPhoto;
    private Button takePhoto;
    private Profile mProfile;
    private Bitmap newImg;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);

        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(UpdateProfilePhotoActivity.this, new HttpClientStack(mHttpClient));

        newImg = null;
        mProfile = ProfileCache.getInstance().getProfile();

        Log.e("========PPHOTO=====",mProfile.getPhoto());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1001);
            }
        }

        iv = (ImageView) findViewById(R.id.prompt_photo);
        new DownLoadImageTask(iv).execute(mProfile.getPhoto());

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonValidate();
            }
        });

        searchPhoto = (Button) findViewById(R.id.search_file);
        searchPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonSearchFile();
            }
        });

        takePhoto = (Button) findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonTakePhoto();
            }
        });

    }

    protected void actionButtonValidate(){

        // TODO send bitmap to server to update profile "newImg"
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("photo", toBase64(newImg));
        }catch (JSONException e){
            e.printStackTrace();
        }

        User u = null;
        db.open();
        u = db.getUserConnected();
        db.close();
        String URL = "/"+u.getToken();

        dialog = ProgressDialog.show(this, "", "", true);
        mQueue.add(createRequest(URL, jsonBodyObj));

    }

    protected void actionButtonSearchFile(){
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                //.withFilter(Pattern.compile(".*\\.jpg$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false)
                .withHiddenFiles(false)
                .start();
    }

    protected void actionButtonTakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // take photo
        if (requestCode == 0 && resultCode == RESULT_OK) {
            newImg = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(newImg);
        }
        //search file
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            newImg = BitmapFactory.decodeFile(filePath ,bmOptions);
            iv.setImageBitmap(newImg);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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

                        Intent myIntent = new Intent(UpdateProfilePhotoActivity.this, BottomActivity.class);
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
