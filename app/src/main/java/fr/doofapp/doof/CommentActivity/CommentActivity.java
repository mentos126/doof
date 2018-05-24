package fr.doofapp.doof.CommentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

public class CommentActivity extends AppCompatActivity {

    private ImageView photo_meal;
    private ImageView photo_comment;

    private TextView prompt_meal_title;
    private EditText leave_description;

    private Button validate;
    private Button searchPhoto;
    private Button takePhoto;

    private Meal mMeal;
    private Bitmap newImg;
    private Comment newComment;

    private Dialog dialog;

    /*********/

    ImageView home_star1, home_star2, home_star3, home_star4, home_star5;
    ImageView cook_star1, cook_star2, cook_star3, cook_star4, cook_star5;
    ImageView cleanless_star1, cleanless_star2, cleanless_star3, cleanless_star4, cleanless_star5;

    TextView tNoteHome, tNoteCook, tNoteCleanless;

    /*********/


    private int note_home = 5;
    private int note_cook = 5;
    private int note_cleanless = 5;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        db = new UserDAO(this);
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(CommentActivity.this, new HttpClientStack(mHttpClient));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1001);
            }
        }
        home_star1 = (ImageView) findViewById(R.id.home_star1);
        home_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_home = 1;
                actionOnHomeStars(note_home);
            }
        });

        home_star2 = (ImageView) findViewById(R.id.home_star2);
        home_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_home = 2;
                actionOnHomeStars(note_home);

            }
        });

        home_star3 = (ImageView) findViewById(R.id.home_star3);
        home_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_home = 3;
                actionOnHomeStars(note_home);

            }
        });

        home_star4 = (ImageView) findViewById(R.id.home_star4);
        home_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_home = 4;
                actionOnHomeStars(note_home);

            }
        });

        home_star5 = (ImageView) findViewById(R.id.home_star5);
        home_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_home = 5;
                actionOnHomeStars(note_home);

            }
        });

        cook_star1 = (ImageView) findViewById(R.id.cook_star1);
        cook_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cook = 1;
                actionOnCookStars(note_cook);
            }
        });

        cook_star2 = (ImageView) findViewById(R.id.cook_star2);
        cook_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cook = 2;
                actionOnCookStars(note_cook);
            }
        });

        cook_star3 = (ImageView) findViewById(R.id.cook_star3);
        cook_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cook = 3;
                actionOnCookStars(note_cook);
            }
        });

        cook_star4 = (ImageView) findViewById(R.id.cook_star4);
        cook_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cook = 4;
                actionOnCookStars(note_cook);
            }
        });

        cook_star5 = (ImageView) findViewById(R.id.cook_star5);
        cook_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cook = 5;
                actionOnCookStars(note_cook);
            }
        });

        cleanless_star1 = (ImageView) findViewById(R.id.cleanless_star1);
        cleanless_star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cleanless = 1;
                actionOnCleanlessStars(note_cleanless);
            }
        });

        cleanless_star2 = (ImageView) findViewById(R.id.cleanless_star2);
        cleanless_star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cleanless = 2;
                actionOnCleanlessStars(note_cleanless);
            }
        });

        cleanless_star3 = (ImageView) findViewById(R.id.cleanless_star3);
        cleanless_star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cleanless = 3;
                actionOnCleanlessStars(note_cleanless);
            }
        });

        cleanless_star4 = (ImageView) findViewById(R.id.cleanless_star4);
        cleanless_star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cleanless = 4;
                actionOnCleanlessStars(note_cleanless);
            }
        });

        cleanless_star5 = (ImageView) findViewById(R.id.cleanless_star5);
        cleanless_star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_cleanless = 5;
                actionOnCleanlessStars(note_cleanless);

            }
        });

        String s;
        tNoteHome = (TextView) findViewById(R.id.note_home);
        s = note_home + "/5 ";
        tNoteHome.setText(s);
        tNoteCook = (TextView) findViewById(R.id.note_cook);
        s = note_cook + "/5 ";
        tNoteCook.setText(s);
        tNoteCleanless = (TextView) findViewById(R.id.note_cleanless);
        s = note_cleanless + "/5 ";
        tNoteCleanless.setText(s);

        newImg = null;
        photo_comment = (ImageView) findViewById(R.id.photo_comment);
        photo_meal = (ImageView) findViewById(R.id.photo_meal);



        //mMeal = (Meal) getIntent().getSerializableExtra("Meal");
        mMeal = CommandCache.getMeal();
        prompt_meal_title = (TextView) findViewById(R.id.prompt_meal_title);
        prompt_meal_title.setText(mMeal.getName());

        new DownLoadImageTask(photo_meal).execute(mMeal.getPhoto());

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

        leave_description = (EditText) findViewById(R.id.leave_description);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonValidate();
            }
        });

    }


    protected void actionOnHomeStars(int res){
        home_star5.setImageResource(R.drawable.star);
        home_star4.setImageResource(R.drawable.star);
        home_star3.setImageResource(R.drawable.star);
        home_star2.setImageResource(R.drawable.star);
        home_star1.setImageResource(R.drawable.star);
        if(res < 5) {
            home_star5.setImageResource(R.drawable.star_blanc);
            if(res < 4) {
                home_star4.setImageResource(R.drawable.star_blanc);
                if(res < 3) {
                    home_star3.setImageResource(R.drawable.star_blanc);
                    if(res < 2) {
                        home_star2.setImageResource(R.drawable.star_blanc);
                        if(res < 1) {
                            home_star1.setImageResource(R.drawable.star_blanc);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteHome.setText(s);
    }

    protected void actionOnCookStars(int res){
        cook_star5.setImageResource(R.drawable.star);
        cook_star4.setImageResource(R.drawable.star);
        cook_star3.setImageResource(R.drawable.star);
        cook_star2.setImageResource(R.drawable.star);
        cook_star1.setImageResource(R.drawable.star);
        if(res < 5) {
            cook_star5.setImageResource(R.drawable.star_blanc);
            if(res < 4) {
                cook_star4.setImageResource(R.drawable.star_blanc);
                if(res < 3) {
                    cook_star3.setImageResource(R.drawable.star_blanc);
                    if(res < 2) {
                        cook_star2.setImageResource(R.drawable.star_blanc);
                        if(res < 1) {
                            cook_star1.setImageResource(R.drawable.star_blanc);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteCook.setText(s);
    }

    protected void actionOnCleanlessStars(int res){
        cleanless_star5.setImageResource(R.drawable.star);
        cleanless_star4.setImageResource(R.drawable.star);
        cleanless_star3.setImageResource(R.drawable.star);
        cleanless_star2.setImageResource(R.drawable.star);
        cleanless_star1.setImageResource(R.drawable.star);
        if(res < 5) {
            cleanless_star5.setImageResource(R.drawable.star_blanc);
            if(res < 4) {
                cleanless_star4.setImageResource(R.drawable.star_blanc);
                    if(res < 3) {
                    cleanless_star3.setImageResource(R.drawable.star_blanc);
                    if(res < 2) {
                        cleanless_star2.setImageResource(R.drawable.star_blanc);
                        if(res < 1) {
                            cleanless_star1.setImageResource(R.drawable.star_blanc);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteCleanless.setText(s);
    }

    protected void actionButtonValidate(){

        if(!leave_description.getText().toString().equals("")){
            if(photo_comment != null){

                JSONObject jsonNote = new JSONObject();
                JSONObject json = new JSONObject();
                JSONObject jsonRepas = new JSONObject();
                try {
                    jsonNote.put("accueil", note_home);
                    jsonNote.put("proprete", note_cleanless);
                    jsonNote.put("cuisine", note_cook);

                    jsonRepas.put("_id",mMeal.getLinkMeal());
                    jsonRepas.put("titre",mMeal.getName());

                    json.put("commentaire",leave_description.getText().toString());
                    json.put("photo", toBase64(newImg));
                    json.put("note", jsonNote);
                    json.put("repas", jsonRepas);

                    json.put("cuisinier", "");
                    json.put("acheteur", "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                User u = null;
                db.open();
                u = db.getUserConnected();
                db.close();
                String URL = URLProject.getInstance().getSEND_COMMENT()+"/"+u.getToken();

                dialog = ProgressDialog.show(this, "", "", true);
                mQueue.add(createRequest(URL, json));

            }else{
                Toast.makeText(this, R.string.toast_prompt_take_photo,Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, R.string.prompt_write_com,Toast.LENGTH_LONG).show();
        }

    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    protected void actionButtonSearchFile(){
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                //.withFilter(Pattern.compile(".*\\.jpg$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(false) // Show hidden files and folders
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
            photo_comment.setImageBitmap(newImg);
        }
        //search file
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            newImg = BitmapFactory.decodeFile(filePath ,bmOptions);
            photo_comment.setImageBitmap(newImg);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private JsonObjectRequest createRequest(String URL, JSONObject jsonObject)  {
        JSONObject jsonBodyObj =  jsonObject;
        final String requestBody = jsonBodyObj.toString();
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST, URL, jsonBodyObj, new Response.Listener<JSONObject>(){
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
                        Intent myIntent = new Intent(CommentActivity.this, BottomActivity.class);
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
