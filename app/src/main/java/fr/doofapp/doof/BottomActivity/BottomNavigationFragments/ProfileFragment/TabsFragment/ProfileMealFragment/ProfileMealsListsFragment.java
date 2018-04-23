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
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;


public class ProfileMealsListsFragment extends Fragment {

    private List<Meal> onlineMealList = new ArrayList<>();
    private List<Meal> lastMealList = new ArrayList<>();
    private RecyclerView OnlineMeals;
    private RecyclerView LastMeals;
    private MealAdapterFragment mAdapter;

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
        OnlineMeals = (RecyclerView) rootView.findViewById(R.id.online_meals);
        OnlineMeals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new MealAdapterFragment(getContext(), onlineMealList);
        OnlineMeals.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        OnlineMeals.setLayoutManager(mLayoutManager);
        OnlineMeals.setItemAnimator(new DefaultItemAnimator());
        OnlineMeals.setAdapter(mAdapter);

        prepareCommentData();

        return rootView;
    }

    public void prepareCommentData(){

        String URL = URLProject.getInstance().getPROFILE_MEALS();

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("LoginActivity", response.toString());
                        int countObject = response.length();
                        Meal meal = null;
                        for(int i=0 ; i<countObject; i++){
                            try {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);
                                meal = new Meal(
                                        jsonObject.get("photo_meal").toString(),
                                        parseInt(jsonObject.get("note_totale").toString()),
                                        jsonObject.get("name_user").toString()
                                );
                                onlineMealList.add(meal);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("LoginActivity", "Error: " + error.getMessage());
            }
        });
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayReq, URL);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
