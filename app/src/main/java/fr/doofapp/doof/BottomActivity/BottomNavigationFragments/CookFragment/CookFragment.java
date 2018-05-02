package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment.Step1CookFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment.Step2CookFragment;
import fr.doofapp.doof.R;

public class CookFragment extends Fragment {

    View rootView;
    EditText time;
    EditText date;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView =  inflater.inflate(R.layout.fragment_cook, container, false);

       FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.frame_cook_container, new Step1CookFragment());
       fragmentTransaction.commit();

        return rootView;
    }

}
