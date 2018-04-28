package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment.ProfileCommentsListFragment;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment.ProfileMealsListsFragment;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.LoginActivity.IsConnectedActivity;
import fr.doofapp.doof.R;
import fr.doofapp.doof.UpdateProfileActivity.UpdateProfileActivity;
import fr.doofapp.doof.UpdateProfileActivity.UpdateProfilePhotoActivity;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Profile mProfile;
    private TextView name;
    private ImageView iv ;
    private TextView note_totale;
    private TextView note_accueil;
    private TextView note_cuisine;
    private TextView note_proprete;

    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;

    private Button editPhoto;
    private Button editProfile;

    private int[] tabIcons = {
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_home_black_24dp
    };

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void getProfileWeb() {

        //String URL = "http://mon-site-en-ligne.fr/doofprod/profile.json";
        String URL = URLProject.getInstance().getPROFILE();

          JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        try {

                           mProfile = new Profile(
                                    response.get("nom").toString(),
                                    response.get("prenom").toString(),
                                    response.get("birth").toString(),
                                    parseInt(response.get("age").toString()),
                                    response.get("photo").toString(),
                                   parseDouble(response.get("note").toString()),
                                   parseDouble(response.get("noteaccueil").toString()),
                                   parseDouble(response.get("noteproprete").toString()),
                                   parseDouble(response.get("notecuisine").toString()),
                                   "adress",
                                   "phone"
                            );

                            new DownLoadImageTask(iv).execute(mProfile.getPhoto());

                            String s = mProfile.getFamilyName()+" "+mProfile.getName();
                            name.setText(s);
                            s = mProfile.getNoteTotal()+"/5  "+"X"+ getResources().getString(R.string.opinions);
                            note_totale.setText(s);
                            s = getResources().getString(R.string.home) + " " + mProfile.getNotaHome() + "/5";
                            note_accueil.setText(s);
                            s = getResources().getString(R.string.cooked)  + " " + mProfile.getNoteCooked() + "/5";
                            note_cuisine.setText(s);
                            s= getResources().getString(R.string.cleanliness)  + " " + mProfile.getNoteCleanless() + "/5";
                            note_proprete.setText(s);

                            double noteTotale = parseDouble(response.get("note").toString());
                            if(noteTotale < 5.0) {
                                star5.setImageResource(R.drawable.ic_home_black_24dp);
                                if(noteTotale < 4.0) {
                                    star4.setImageResource(R.drawable.ic_home_black_24dp);
                                    if(noteTotale < 3.0) {
                                        star3.setImageResource(R.drawable.ic_home_black_24dp);
                                        if(noteTotale < 2.0) {
                                            star2.setImageResource(R.drawable.ic_home_black_24dp);
                                            if(noteTotale < 1.0) {
                                                star1.setImageResource(R.drawable.ic_home_black_24dp);
                                            }
                                        }
                                    }
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editPhoto = (Button) getView().findViewById(R.id.EditPhotoButton);
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), UpdateProfilePhotoActivity.class);
                myIntent.putExtra("Profile", (Serializable) mProfile);
                startActivity(myIntent);
            }
        });

        editProfile = (Button) getView().findViewById(R.id.EditProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mProfile.getName() != null){
                    Intent myIntent = new Intent(getActivity(), UpdateProfileActivity.class);
                    myIntent.putExtra("Profile", (Serializable) mProfile);
                    startActivity(myIntent);
                }
            }
        });

        mProfile = new Profile("ERREUR","ERREUR","ERREUR",-1, "ERREUR",
                -1,-1,-1,-1, "ERREUR", "ERREUR");

        iv = (ImageView) getView().findViewById(R.id.photo);
        name = (TextView) getView().findViewById(R.id.name);
        note_totale = (TextView) getView().findViewById(R.id.note_totale);
        note_accueil = (TextView) getView().findViewById(R.id.note_accueil);
        note_cuisine = (TextView) getView().findViewById(R.id.note_cuisine);
        note_proprete = (TextView) getView().findViewById(R.id.note_proprete);

        star1 = (ImageView) getView().findViewById(R.id.star1);
        star2 = (ImageView) getView().findViewById(R.id.star2);
        star3 = (ImageView) getView().findViewById(R.id.star3);
        star4 = (ImageView) getView().findViewById(R.id.star4);
        star5 = (ImageView) getView().findViewById(R.id.star5);

        if(!isConnected()){
            Intent intent = new Intent(getActivity(), IsConnectedActivity.class);
            startActivity(intent);
        }

        getProfileWeb();

        String s = mProfile.getFamilyName() + " " + mProfile.getName();
        name.setText(s);
        // TODO changer "X" par le nombre d'avis
        s = mProfile.getNoteTotal() + "/5  " + "X" + " " + getResources().getString(R.string.opinions);
        note_totale.setText(s);
        s = getResources().getString(R.string.home) + " " + mProfile.getNotaHome() + "/5";
        note_accueil.setText(s);
        s = getResources().getString(R.string.cooked)  + " " + mProfile.getNoteCooked() + "/5";
        note_cuisine.setText(s);
        s= getResources().getString(R.string.cleanliness)  + " " + mProfile.getNoteCleanless() + "/5";
        note_proprete.setText(s);

        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout = (TabLayout) getView().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupTabIcon(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new ProfileMealsListsFragment(), getResources().getString(R.string.profile_meals));
        adapter.addFragment(new ProfileCommentsListFragment(), getResources().getString(R.string.profile_coms));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

