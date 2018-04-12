package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.doofapp.doof.R;

public class ProfileMealsListFragment extends Fragment {
    public ProfileMealsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_meals_list, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
