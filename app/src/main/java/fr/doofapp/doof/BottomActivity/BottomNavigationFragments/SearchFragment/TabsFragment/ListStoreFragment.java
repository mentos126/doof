package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.doofapp.doof.R;


public class ListStoreFragment extends Fragment {
    public ListStoreFragment() {
        // Required empty public constructor
    }

    public static ListStoreFragment newInstance(String param1, String param2) {
        ListStoreFragment fragment = new ListStoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_store, container, false);
    }
}
