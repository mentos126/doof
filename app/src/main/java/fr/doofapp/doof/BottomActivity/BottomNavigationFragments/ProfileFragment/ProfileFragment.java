package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment.ProfileCommentsListFragment;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment.ProfileMealsListsFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileParamsFragment.ProfileParamsFragment;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.IsConnectedActivity;
import fr.doofapp.doof.R;

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

    private UserDAO db;

    private Dialog dialog;

    private int[] tabIcons = {
            R.drawable.ic_dashboard_black_24dp,
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_apk_box
    };

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void getProfileWeb() {
        db.open();
        User u = db.getUserConnected();
        db.close();
        String URL = URLProject.getInstance().getMY_PROFILE() +"/"+u.getToken();
        dialog = ProgressDialog.show(getActivity(), "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        dialog.dismiss();
                        try {

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
                                    star5.setImageResource(R.drawable.star_blanc);
                                    if(noteTotale < 4.0) {
                                        star4.setImageResource(R.drawable.star_blanc);
                                        if(noteTotale < 3.0) {
                                            star3.setImageResource(R.drawable.star_blanc);
                                            if(noteTotale < 2.0) {
                                                star2.setImageResource(R.drawable.star_blanc);
                                                if(noteTotale < 1.0) {
                                                    star1.setImageResource(R.drawable.star_blanc);
                                                }
                                            }
                                        }
                                    }
                                }


                            }else{
                                Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
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

        db = new UserDAO(getActivity());

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
        s = mProfile.getNoteTotal() + "/5";//  " + "X" + " " + getResources().getString(R.string.opinions);
        note_totale.setText(s);
        s = getResources().getString(R.string.home) + " " + mProfile.getNotaHome() + "/5";
        note_accueil.setText(s);
        s = getResources().getString(R.string.cooked)  + " " + mProfile.getNoteCooked() + "/5";
        note_cuisine.setText(s);
        s= getResources().getString(R.string.cleanliness)  + " " + mProfile.getNoteCleanless() + "/5";
        note_proprete.setText(s);

        viewPager = (ViewPager) getView().findViewById(R.id.viewpager);
        setupViewPager(viewPager);

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
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProfileMealsListsFragment(), getResources().getString(R.string.profile_meals));
        adapter.addFragment(new ProfileCommentsListFragment(), getResources().getString(R.string.profile_coms));
        adapter.addFragment(new ProfileParamsFragment(), getString(R.string.prompt_profile_tab_params));
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

