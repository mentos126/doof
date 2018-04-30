package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;


public class ProfileMealsListsFragment extends Fragment {

    private List<Meal> onlineMealList = new ArrayList<>();
    private List<Meal> lastMealList = new ArrayList<>();
    private RecyclerView OnlineMeals;
    private RecyclerView LastMeals;
    private MealAdapterFragment mOnlineAdapter;
    private MealAdapterFragment mLastAdapter;
    //TODO change X with this numbers
    private int nbOnlineMeals;
    private int nbLastMeals;


    public ProfileMealsListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_meals_lists, container, false);

        OnlineMeals = rootView.findViewById(R.id.online_meals);
        OnlineMeals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mOnlineAdapter = new MealAdapterFragment(getContext(), onlineMealList);
        OnlineMeals.setAdapter(mOnlineAdapter);
        RecyclerView.LayoutManager mOnlineLayoutManager = new LinearLayoutManager(getContext());
        OnlineMeals.setLayoutManager(mOnlineLayoutManager);
        OnlineMeals.setItemAnimator(new DefaultItemAnimator());
        OnlineMeals.setAdapter(mOnlineAdapter);

        LastMeals = rootView.findViewById(R.id.last_meals);
        LastMeals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mLastAdapter = new MealAdapterFragment(getContext(), lastMealList);
        LastMeals.setAdapter(mLastAdapter);
        RecyclerView.LayoutManager mLastLayoutManager = new LinearLayoutManager(getContext());
        LastMeals.setLayoutManager(mLastLayoutManager);
        LastMeals.setItemAnimator(new DefaultItemAnimator());
        LastMeals.setAdapter(mLastAdapter);

        nbOnlineMeals = -1;
        nbLastMeals   = -1;

        prepareMealData();

        return rootView;
    }

    public void prepareMealData(){

        String URL = URLProject.getInstance().getPROFILE_MEALS();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=========MEALS========", response.toString());
                        try {
                            nbOnlineMeals = parseInt(response.get("nb_online_meals").toString());
                            nbLastMeals = parseInt(response.get("nb_lastmeals").toString());
                            Meal meal;

                            //online meals
                            JSONArray jsonOnlineMeals = (JSONArray) response.get("online_meals");
                            int countObject = jsonOnlineMeals.length();
                            for(int i=0 ; i<countObject; i++){
                                JSONObject jsonObject;
                                jsonObject = jsonOnlineMeals.getJSONObject(i);
                                meal = new Meal(
                                        jsonObject.get("photo_meal").toString(),
                                        parseInt(jsonObject.get("note_totale").toString()),
                                        jsonObject.get("name_user").toString(),
                                        "link",
                                        "date"
                                );
                                onlineMealList.add(meal);
                            }
                            mOnlineAdapter.notifyDataSetChanged();

                            //last meals
                            JSONArray jsonLeastMeals = (JSONArray) response.get("last_meals");
                            countObject = jsonLeastMeals.length();
                            for(int i=0 ; i<countObject; i++){
                                JSONObject jsonObject;
                                jsonObject = jsonLeastMeals.getJSONObject(i);
                                meal = new Meal(
                                        jsonObject.get("photo_meal").toString(),
                                        parseInt(jsonObject.get("note_totale").toString()),
                                        jsonObject.get("name_user").toString(),
                                        "link",
                                        "date"
                                );
                                lastMealList.add(meal);
                            }

                            mLastAdapter.notifyDataSetChanged();
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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
