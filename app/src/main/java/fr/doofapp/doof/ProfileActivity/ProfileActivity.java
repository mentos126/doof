package fr.doofapp.doof.ProfileActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment.ProfileCommentsListFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment.ProfileMealsListsFragment;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.LoginActivity.IsConnectedActivity;
import fr.doofapp.doof.ProfileActivity.TabsFragments.ProfileCommentFragment.PublicProfileCommentsListFragment;
import fr.doofapp.doof.ProfileActivity.TabsFragments.ProfileMealFragment.PublicProfileMealsListsFragment;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ProfileActivity extends AppCompatActivity {

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

    private String link;

    private Dialog dialog;

    private int[] tabIcons = {
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_home_black_24dp
    };

    public void getProfileWeb() {

        String URL = URLProject.getInstance().getPROFILE()+"/"+link;
        dialog = ProgressDialog.show(this, "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ProfileActivity", response.toString());
                        dialog.dismiss();
                        try{
                            if(response.isNull("error")){
                                JSONObject res = response.getJSONObject("result");
                                String birth;
                                int age;
                                try{
                                    age = parseInt(res.get("age").toString());
                                }catch(Exception e){
                                    age = 0;
                                }
                                try{
                                    birth = res.get("birth").toString();
                                }catch(Exception e){
                                    birth = "";
                                }
                                JSONObject notes = res.getJSONObject("note");
                                mProfile = new Profile(
                                        res.get("nom").toString(),
                                        res.get("prenom").toString(),
                                        birth,
                                        age,
                                        res.get("photo").toString(),
                                        parseDouble(res.get("note_global").toString()),
                                        parseDouble(notes.get("accueil").toString()),
                                        parseDouble(notes.get("proprete").toString()),
                                        parseDouble(notes.get("cuisine").toString()),
                                        "",
                                        ""
                                );

                                new DownLoadImageTask(iv).execute(mProfile.getPhoto());

                                String s = mProfile.getFamilyName()+" "+mProfile.getName();
                                name.setText(s);
                                s = new DecimalFormat("#.#").format(mProfile.getNoteTotal())+"/5 ";//+"X"+ getResources().getString(R.string.opinions);
                                note_totale.setText(s);
                                s = getResources().getString(R.string.home) + " " + new DecimalFormat("#.#").format(mProfile.getNotaHome())+ "/5";
                                note_accueil.setText(s);
                                s = getResources().getString(R.string.cooked)  + " " + new DecimalFormat("#.#").format(mProfile.getNoteCooked()) + "/5";
                                note_cuisine.setText(s);
                                s= getResources().getString(R.string.cleanliness)  + " " + new DecimalFormat("#.#").format(mProfile.getNoteCleanless()) + "/5";
                                note_proprete.setText(s);

                                double noteTotale = mProfile.getNoteTotal();
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


                            }else{
                                Toast.makeText(ProfileActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
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
                        dialog.dismiss();
                        Toast.makeText(ProfileActivity.this, getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        link = CommandCache.getMeal().getLinkMeal();

        mProfile = new Profile("ERREUR","ERREUR","ERREUR",-1, "ERREUR",
                -1,-1,-1,-1, "ERREUR", "ERREUR");

        iv = (ImageView) findViewById(R.id.photo);
        name = (TextView) findViewById(R.id.name);
        note_totale = (TextView) findViewById(R.id.note_totale);
        note_accueil = (TextView) findViewById(R.id.note_accueil);
        note_cuisine = (TextView) findViewById(R.id.note_cuisine);
        note_proprete = (TextView) findViewById(R.id.note_proprete);

        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        getProfileWeb();

        String s = mProfile.getFamilyName() + " " + mProfile.getName();
        name.setText(s);
        s = mProfile.getNoteTotal() + "/5";// + "X" + " " + getResources().getString(R.string.opinions);
        note_totale.setText(s);
        s = getResources().getString(R.string.home) + " " + mProfile.getNotaHome() + "/5";
        note_accueil.setText(s);
        s = getResources().getString(R.string.cooked)  + " " + mProfile.getNoteCooked() + "/5";
        note_cuisine.setText(s);
        s= getResources().getString(R.string.cleanliness)  + " " + mProfile.getNoteCleanless() + "/5";
        note_proprete.setText(s);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon();

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setupTabIcon(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PublicProfileMealsListsFragment(), getResources().getString(R.string.profile_meals));
        adapter.addFragment(new PublicProfileCommentsListFragment(), getResources().getString(R.string.profile_coms));
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
