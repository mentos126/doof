package fr.doofapp.doof.BottomActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CalendarFragment.CalendarFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.CookFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.NotAuthorizedFragment.NotConnectedFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.NotAuthorizedFragment.NotCookerConnectedFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.ProfileFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.SearchFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.ListStoreFragment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.LoginActivity;
import fr.doofapp.doof.R;

//TODO PB sur les tabview lors de changement de fragment !!!
public class BottomActivity extends AppCompatActivity /*implements SearchFragment.SendListMeal*/ {

    private UserDAO db;
    private User u;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_agenda:
                    if(userIsConnected()){
                        fragment = new CalendarFragment();
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_rechercher:
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_publier:
                    if(userIsConnected()){
                        if(u != null && u.getRole() == 2){
                            fragment = new CookFragment();
                        }else{
                            fragment = new NotCookerConnectedFragment();
                        }
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profil:
                    if(userIsConnected()){
                        fragment = new ProfileFragment();
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "BOTTOM_FRAGMENT");
        transaction.addToBackStack(fragment.toString());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected Boolean userIsConnected() {
        db.open();
        u = null;
        u = db.getUserConnected();
        db.close();
        if(u == null){
            Log.e("=======USER=ID=========","NULL");
        }else{
            Log.e("=======USER=ID=========",u.getUserId());
        }
        if (u != null && u.getConnected() == 1){
            Log.e("=======USER=CO=========","YES");
            return true;
        }else{
            Log.e("=======USER=CO=========","NO");
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        db = new UserDAO(getApplicationContext());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment;
        int tab;
        try {
            tab = (int) getIntent().getSerializableExtra("Tab");
            switch (tab) {
                case R.id.navigation_agenda:
                    if(userIsConnected()){
                        fragment = new CalendarFragment();
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    break;
                case R.id.navigation_rechercher:
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    break;
                case R.id.navigation_publier:
                    if(userIsConnected()){
                        if(u != null && u.getRole() == 2){
                            fragment = new CookFragment();
                        }else{
                            fragment = new NotCookerConnectedFragment();
                        }
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    break;
                case R.id.navigation_profil:
                    if(userIsConnected()){
                        fragment = new ProfileFragment();
                    }else{
                        fragment = new NotConnectedFragment();
                    }
                    loadFragment(fragment);
                    break;
            }
        }catch (Exception e){
            tab = R.id.navigation_agenda;
            fragment = new CalendarFragment();
            loadFragment(fragment);
        }
        navigation.setSelectedItemId(tab);

    }

    /*@Override
    public void sendListMEal(List<Meal> mealList) {
        String tag = "BOTTOM_FRAGMENT";
        String tag2 = "" + R.id.viewpager;
        ListStoreFragment fl = (ListStoreFragment) getSupportFragmentManager()
                .findFragmentByTag(tag)
                .getFragmentManager()
                .findFragmentById(R.id.viewPagerSearch);
        fl.receiveData(mealList);
    }*/
}
