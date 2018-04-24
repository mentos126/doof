package fr.doofapp.doof.UpdateProfileActivity;

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

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.IsConnectedActivity;
import fr.doofapp.doof.R;

public class UpdateProfileActivity extends AppCompatActivity {

    private UserDAO db;
    private Profile mProfile;

    private EditText familyName;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText adress;

    private ImageView iv;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!isConnected()){
            Intent myIntent = new Intent(UpdateProfileActivity.this, IsConnectedActivity.class);
            startActivity(myIntent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");
        Log.e("=====UPDATE=====",mProfile.getName());

        familyName = (EditText) findViewById(R.id.edit_family_name);
        familyName.setText(mProfile.getFamilyName());

        name = (EditText) findViewById(R.id.edit_name);
        name.setText(mProfile.getName());


        db = new UserDAO(this);
        db.open();
        User u = null;
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
        }
    }

}
