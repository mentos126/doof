package fr.doofapp.doof.UpdateProfileActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.util.regex.Pattern;

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.R;

public class UpdateProfilePhotoActivity extends AppCompatActivity {

    private ImageView iv;
    private Button validate;
    private Button searchPhoto;
    private Button takePhoto;
    private Profile mProfile;
    private Bitmap newImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_photo);
        newImg = null;
        mProfile = (Profile) getIntent().getSerializableExtra("Profile");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

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

    protected void actionButtonValidate(){

        // TODO send bitmap to server to update profile "newImg"

        Intent myIntent = new Intent(UpdateProfilePhotoActivity.this, BottomActivity.class);

        startActivity(myIntent);



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
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bitmap);
        }
        //search file
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap b = BitmapFactory.decodeFile(filePath ,bmOptions);
            newImg = b;
            iv.setImageBitmap(b);
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
            }
        }
    }
}
