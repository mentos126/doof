package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.R;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION_CODES.O;


public class Step3CookFragment extends Fragment {
    View rootView;
    NumberPicker np;
    NumberPicker npPrice;
    Button next;
    EditText name;
    EditText description;
    ImageView photo;
    Button takePhoto;
    Button searchPhoto;
    CheckBox contain;
    Button previous;

    int price;
    int nbPortion;
    Bitmap newPhoto;

    String date;
    String time;
    String adress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step3, container, false);

        Bundle bundle = getArguments();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1001);
            }
        }

        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();
        adress = bundle.get("Adress").toString();

        name = (EditText) rootView.findViewById(R.id.name);
        description = (EditText) rootView.findViewById(R.id.description);
        photo = (ImageView) rootView.findViewById(R.id.prompt_photo);
        contain  = (CheckBox) rootView.findViewById(R.id.is_container);

        takePhoto = (Button) rootView.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonTakePhoto();
            }
        });

        searchPhoto = (Button) rootView.findViewById(R.id.search_photo);
        searchPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonSearchFile();
            }
        });

        np = (NumberPicker) rootView.findViewById(R.id.nbPortion);
        np.setMinValue(0);
        np.setMaxValue(99);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                nbPortion = newVal;
            }
        });

        npPrice = (NumberPicker) rootView.findViewById(R.id.price);
        npPrice.setMinValue(0);
        npPrice.setMaxValue(50);
        npPrice.setWrapSelectorWheel(true);
        npPrice.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                price = newVal;
            }
        });

        previous = (Button) rootView.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Date", date);
                bundle.putString("Time", time);
                bundle.putString("Adress", adress);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Step2CookFragment step2CookFragment = new Step2CookFragment();
                step2CookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_cook_container, step2CookFragment);
                fragmentTransaction.commit();
            }
        });

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = name.getText().toString();
                String desc = description.getText().toString();

                if(nbPortion > 0 && !desc.equals("") && !title.equals("")){
                    if(newPhoto != null){
                        Boolean ct = contain.isChecked();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        newPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Bundle bundle = new Bundle();

                        ListMealCache meals = ListMealCache.getInstance();

                        bundle.putString("Date", date);
                        bundle.putString("Time", time);
                        bundle.putString("Adress", adress);
                        bundle.putString("MainTitle", title);
                        bundle.putString("MainDescription", desc);
                        bundle.putInt("MainNbPoriotn", nbPortion);
                        bundle.putInt("MAinPrice", price);
                        bundle.putBoolean("IsContainer",ct);

                        meals.setMainPhoto(newPhoto);
                        List<String> titles = new ArrayList<>();
                        meals.setTitles(titles);
                        List<String> descriptions = new ArrayList<>();
                        meals.setDescriptions(descriptions);
                        List<byte []> photos = new ArrayList<>();
                        meals.setPhotos(photos);
                        List<Integer> nbPortions = new ArrayList<>();
                        meals.setNbPortions(nbPortions);
                        List<Integer> prices = new ArrayList<>();
                        meals.setPrices(prices);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        //TODO change step 4
                        Step5CookFragment step5CookFragment = new Step5CookFragment();
                        step5CookFragment.setArguments(bundle);
                        Log.e("====COMMIT====","=======COMIT====");
                        fragmentTransaction.replace(R.id.frame_cook_container, step5CookFragment);
                        fragmentTransaction.commit();

                    }else{
                        Toast.makeText(getActivity(), R.string.prompt_add_photo,Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(!desc.equals("") && !title.equals("")){
                        Toast.makeText(getActivity(),getResources().getString(R.string.prompt_edit_all),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), R.string.prompt_portion,Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        return rootView;
    }

    protected void actionButtonSearchFile(){
        new MaterialFilePicker()
                .withSupportFragment(this)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // take photo
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            newPhoto = bitmap;
            photo.setImageBitmap(bitmap);
        }
        //search file
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap b = BitmapFactory.decodeFile(filePath ,bmOptions);
            newPhoto = b;
            photo.setImageBitmap(b);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getActivity(), "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
