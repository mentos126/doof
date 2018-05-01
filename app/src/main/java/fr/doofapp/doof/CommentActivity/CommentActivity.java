package fr.doofapp.doof.CommentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.Meal;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        newImg = null;
        mMeal = (Meal) getIntent().getSerializableExtra("Meal");

        photo_meal = (ImageView) findViewById(R.id.photo_meal);
        new DownLoadImageTask(photo_meal).execute(mMeal.getPhoto());

        prompt_meal_title = (TextView) findViewById(R.id.prompt_meal_title);
        prompt_meal_title.setText(mMeal.getName());

        photo_comment = (ImageView) findViewById(R.id.photo_comment);


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


    protected void actionButtonValidate(){

        // TODO send bitmap to server to update profile "newImg"
        //TODO send request new comment

        newComment = new Comment(
                /*String descriptif*/ leave_description.getText().toString(),
                /*String photo*/"send in other request",
                /*String link*/"",
                /*String nameUser*/"",
                /*String photoUser*/"",
                /*double noteAccueil*/0,
                /*double noteProprete*/0,
                /*double noteCuisine*/0,
                /*double noteTotale*/0);

        Intent myIntent = new Intent(CommentActivity.this, BottomActivity.class);
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
            photo_comment.setImageBitmap(bitmap);
        }
        //search file
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap b = BitmapFactory.decodeFile(filePath ,bmOptions);
            newImg = b;
            photo_comment.setImageBitmap(b);
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
