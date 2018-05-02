package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.ByteArrayOutputStream;

import fr.doofapp.doof.R;

import static android.app.Activity.RESULT_OK;

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

        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();
        adress = bundle.get("Adress").toString();

        Log.e("======DATE=====",date);
        Log.e("======TIME=====",time);
        Log.e("======Adress=====",adress);

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
                        bundle.putString("Date", date);
                        bundle.putString("Time", time);
                        bundle.putString("Adress", adress);
                        bundle.putString("Title", title);
                        bundle.putString("Description", desc);
                        bundle.putByteArray("Photo",byteArray);
                        bundle.putInt("NbPoriotn", nbPortion);
                        bundle.putInt("Price", price);
                        bundle.putBoolean("IsContainer",ct);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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
            Log.e("=====SEARCH=====","======SEACHR===");
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap b = BitmapFactory.decodeFile(filePath ,bmOptions);
            newPhoto = b;
            photo.setImageBitmap(b);
        }
    }


}
