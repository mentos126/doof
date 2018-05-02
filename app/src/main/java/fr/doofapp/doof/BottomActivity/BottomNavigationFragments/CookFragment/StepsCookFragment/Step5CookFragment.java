package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.doofapp.doof.R;

public class Step5CookFragment extends Fragment {

    View rootView;



    String date;
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step5, container, false);

        Bundle bundle = getArguments();

        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();

        Log.e("======DATE=====", date);
        Log.e("======TIME=====", time);

        return rootView;
    }

}
