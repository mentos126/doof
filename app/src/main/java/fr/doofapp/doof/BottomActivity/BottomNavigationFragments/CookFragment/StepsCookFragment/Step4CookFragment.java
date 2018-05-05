package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.R;

public class Step4CookFragment extends Fragment {

    View rootView;

    String date;
    String time;
    String adress;
    String mainTitle;
    String mainDescription;
    byte[] mainPhoto;
    int mainNbPortion;
    int mainPrice;
    Boolean isContain;

    ListMealCache listMealCache;

    List<String> titles;
    List<String> descriptions;
    List<byte[]> photos;
    List<Integer> nbPortions;
    List<Integer> prices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step3, container, false);

        /*titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        photos = new ArrayList<>();
        nbPortions = new ArrayList<>();
        prices = new ArrayList<>();*/

        Bundle bundle = getArguments();
        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();
        adress = bundle.get("Adress").toString();
        mainTitle = bundle.get("MainTitle").toString();
        mainDescription = bundle.get("MainDescription").toString();
        //mainPhoto = bundle.getByteArray("MainPhoto");
        mainNbPortion = bundle.getInt("MainNbPortion");
        mainPrice= bundle.getInt("MainPrice");
        isContain= bundle.getBoolean("IsContain");

        //listMealCache = new ListMealCache();
        String listMealCache = (String) bundle.get("ListMeal");
        Gson gson = new Gson();
        ListMealCache l = gson.fromJson(listMealCache,ListMealCache.class);

        Log.e("======TITLE=====",mainDescription);
        Log.e("======LIST=====",listMealCache);
        Log.e("======LIST=====",l.getDescriptions().toString());




        return rootView;
    }

}
