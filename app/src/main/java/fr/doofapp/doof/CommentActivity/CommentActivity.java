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


    /*********/

    ImageView home_star1, home_star2, home_star3, home_star4, home_star5;
    ImageView cook_star1, cook_star2, cook_star3, cook_star4, cook_star5;
    ImageView cleanless_star1, cleanless_star2, cleanless_star3, cleanless_star4, cleanless_star5;

    TextView tNoteHome, tNoteCook, tNoteCleanless;

    /*********/


    private int note_home = 5;
    private int note_cook = 5;
    private int note_cleanless = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
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


    protected void actionOnHomeStars(int res){
        home_star5.setImageResource(R.drawable.ic_excel_box);
        home_star4.setImageResource(R.drawable.ic_excel_box);
        home_star3.setImageResource(R.drawable.ic_excel_box);
        home_star2.setImageResource(R.drawable.ic_excel_box);
        home_star1.setImageResource(R.drawable.ic_excel_box);
        if(res < 5) {
            home_star5.setImageResource(R.drawable.ic_home_black_24dp);
            if(res < 4) {
                home_star4.setImageResource(R.drawable.ic_home_black_24dp);
                if(res < 3) {
                    home_star3.setImageResource(R.drawable.ic_home_black_24dp);
                    if(res < 2) {
                        home_star2.setImageResource(R.drawable.ic_home_black_24dp);
                        if(res < 1) {
                            home_star1.setImageResource(R.drawable.ic_home_black_24dp);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteHome.setText(s);
    }

    protected void actionOnCookStars(int res){
        cook_star5.setImageResource(R.drawable.ic_excel_box);
        cook_star4.setImageResource(R.drawable.ic_excel_box);
        cook_star3.setImageResource(R.drawable.ic_excel_box);
        cook_star2.setImageResource(R.drawable.ic_excel_box);
        cook_star1.setImageResource(R.drawable.ic_excel_box);
        if(res < 5) {
            cook_star5.setImageResource(R.drawable.ic_home_black_24dp);
            if(res < 4) {
                cook_star4.setImageResource(R.drawable.ic_home_black_24dp);
                if(res < 3) {
                    cook_star3.setImageResource(R.drawable.ic_home_black_24dp);
                    if(res < 2) {
                        cook_star2.setImageResource(R.drawable.ic_home_black_24dp);
                        if(res < 1) {
                            cook_star1.setImageResource(R.drawable.ic_home_black_24dp);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteCook.setText(s);
    }

    protected void actionOnCleanlessStars(int res){
        cleanless_star5.setImageResource(R.drawable.ic_excel_box);
        cleanless_star4.setImageResource(R.drawable.ic_excel_box);
        cleanless_star3.setImageResource(R.drawable.ic_excel_box);
        cleanless_star2.setImageResource(R.drawable.ic_excel_box);
        cleanless_star1.setImageResource(R.drawable.ic_excel_box);
        if(res < 5) {
            cleanless_star5.setImageResource(R.drawable.ic_home_black_24dp);
            if(res < 4) {
                cleanless_star4.setImageResource(R.drawable.ic_home_black_24dp);
                if(res < 3) {
                    cleanless_star3.setImageResource(R.drawable.ic_home_black_24dp);
                    if(res < 2) {
                        cleanless_star2.setImageResource(R.drawable.ic_home_black_24dp);
                        if(res < 1) {
                            cleanless_star1.setImageResource(R.drawable.ic_home_black_24dp);
                        }
                    }
                }
            }
        }
        String s = res + "/5 ";
        tNoteCleanless.setText(s);
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
                /*double noteAccueil*/note_home,
                /*double noteProprete*/note_cleanless,
                /*double noteCuisine*/note_cook,
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
