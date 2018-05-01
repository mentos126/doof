package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CalendarFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.ListStoreMealAdapterFragment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;


public class CalendarFragment extends Fragment {

    private List<Pair<Meal,Boolean>> mealList = new ArrayList<Pair<Meal,Boolean>>();
    private RecyclerView meals;
    private CalendarAdapterFragment mAdapter;
    View rootView;

    public CalendarFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        meals = rootView.findViewById(R.id.calendarMeals);
        meals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CalendarAdapterFragment(getContext(),mealList);
        meals.setAdapter(mAdapter);
        RecyclerView.LayoutManager mOnlineLayoutManager = new LinearLayoutManager(getContext());
        meals.setLayoutManager(mOnlineLayoutManager);
        meals.setItemAnimator(new DefaultItemAnimator());
        meals.setAdapter(mAdapter);

        prepareMealData();

    }

    protected void prepareMealData(){
        String URL = URLProject.getInstance().getCALENDARMEALS();

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
                                        jsonObject.get("date_heure").toString(),
                                        "description"
                                );
                                Boolean b = jsonObject.getBoolean("cooked");
                                Pair<Meal,Boolean> p = new Pair<>(meal,b);
                                mealList.add(p);
                            }
                            mAdapter.notifyDataSetChanged();
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

