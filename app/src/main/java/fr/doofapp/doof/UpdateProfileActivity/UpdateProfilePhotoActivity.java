package fr.doofapp.doof.UpdateProfileActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.R;

public class UpdateProfilePhotoActivity extends AppCompatActivity {

    private ImageView iv;
    private Button validate;
    private Button searchPhoto;
    private Button takePhoto;
    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");

        iv = (ImageView) findViewById(R.id.prompt_photo);
        Log.e("=====PHOTO=====",mProfile.getPhoto());
        new DownLoadImageTask(iv).execute(mProfile.getPhoto());

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonValidate();
            }
        });

        validate = (Button) findViewById(R.id.search_file);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonSearchFile();
            }
        });

        validate = (Button) findViewById(R.id.take_photo);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonTakePhoto();
            }
        });

    }

    protected void actionButtonValidate(){}
    protected void actionButtonSearchFile(){}
    protected void actionButtonTakePhoto(){}


}
