package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment;

import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.ListStoreFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.MapsStoreFragment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;


public class SearchFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Meal> mealList = new ArrayList<>();
    //private SendListMeal SM;

    private int[] tabIcons = {
            R.drawable.ic_music_box,
            R.drawable.ic_excel_box
    };

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) getView().findViewById(R.id.viewPagerSearch);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) getView().findViewById(R.id.tabsSearch);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon();
        // prepareMealData();

    }

    /*public interface SendListMeal{
        void sendListMEal(List<Meal> mealList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            SM = (SendListMeal) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }*/


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
        adapter.addFragment(new MapsStoreFragment(), getResources().getString(R.string.prompt_tab_map));
        adapter.addFragment(new ListStoreFragment(), getResources().getString(R.string.prompt_tab_list));
        viewPager.setAdapter(adapter);
    }

   /* protected void prepareMealData(){
        String URL = URLProject.getInstance().getMEALS();

        JsonArrayRequest jsonObjectReq = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("=========MEALS========", response.toString());
                        try {
                            Meal meal;
                            int countObject = response.length();
                            for(int i=0 ; i<countObject; i++){
                                JSONObject jsonObject;
                                jsonObject = response.getJSONObject(i);
                                meal = new Meal(
                                        jsonObject.get("photo_meal").toString(),
                                        parseInt(jsonObject.get("price").toString()),
                                        jsonObject.get("title").toString(),
                                        jsonObject.get("link_meal").toString(),
                                        jsonObject.get("date_heure").toString()
                                );
                                mealList.add(meal);
                            }
                            //SM.sendListMEal(mealList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("=========MEALS========", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);
    }*/

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
