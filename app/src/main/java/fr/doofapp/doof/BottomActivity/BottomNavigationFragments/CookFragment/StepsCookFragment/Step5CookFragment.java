package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.R;

public class Step5CookFragment extends Fragment {

    View rootView;
    ImageView prompt_meal_photo;
    TextView prompt_price, prompt_name_meal, prompt_date, prompt_time,
            prompt_adress, prompt_contenant;
    Button validate, previous, modify;

    String date, time, adress, mainTitle, mainDescription;
    int mainNbPortion, mainPrice;
    Boolean contain;
    byte[] mainPhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step5, container, false);

        Bundle bundle = getArguments();
        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();
        adress = bundle.get("Adress").toString();
        mainTitle = bundle.get("MainTitle").toString();
        mainDescription = bundle.get("MainDescription").toString();
        mainPhoto = bundle.getByteArray("MainPhoto");
        mainNbPortion =  bundle.getInt("MainNbPoriotn");
        mainPrice = bundle.getInt("MAinPrice");
        contain = bundle.getBoolean("IsContainer");

        prompt_adress = (TextView) rootView.findViewById(R.id.prompt_adress);
        prompt_adress.setText(adress);
        prompt_date = (TextView) rootView.findViewById(R.id.prompt_date);
        prompt_date.setText(date);
        prompt_price = (TextView) rootView.findViewById(R.id.prompt_price);
        String s=""+mainPrice;
        prompt_price.setText(s);
        prompt_name_meal = (TextView) rootView.findViewById(R.id.prompt_name_meal);
        prompt_name_meal.setText(mainTitle);
        prompt_time = (TextView) rootView.findViewById(R.id.prompt_time);
        prompt_time.setText(time);
        prompt_contenant = (TextView) rootView.findViewById(R.id.prompt_contenant);
        s="";
        if(contain){
            s= "OUI";
        }else{
            s= "NON";
        }
        prompt_contenant.setText(s);

        Bitmap b = ListMealCache.getInstance().getMainPhoto();
        prompt_meal_photo =(ImageView) rootView.findViewById(R.id.prompt_meal_photo);
        prompt_meal_photo.setImageBitmap(b);

        modify = (Button) rootView.findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Date", date);
                bundle.putString("Time", time);
                bundle.putString("Adress", adress);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Step3CookFragment step3CookFragment = new Step3CookFragment();
                step3CookFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_cook_container, step3CookFragment);
                fragmentTransaction.commit();
            }
        });

        validate = (Button) rootView.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO send request to create meal
                Intent myIntent = new Intent(view.getContext(), BottomActivity.class);
                getActivity().startActivity(myIntent);
            }
        });






        return rootView;
    }

}
