package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;


public class ListStoreFragment extends Fragment {

    private List<Meal> mealList = new ArrayList<>();
    private RecyclerView meals;
    private ListStoreMealAdapterFragment mAdapter;

    public ListStoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_list_store, container, false);

        meals = rootView.findViewById(R.id.online_meals);
        meals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new ListStoreMealAdapterFragment(getContext(), mealList);
        meals.setAdapter(mAdapter);
        RecyclerView.LayoutManager mOnlineLayoutManager = new LinearLayoutManager(getContext());
        meals.setLayoutManager(mOnlineLayoutManager);
        meals.setItemAnimator(new DefaultItemAnimator());
        meals.setAdapter(mAdapter);

        prepareMealData();

        return rootView;

    }

    protected void prepareMealData(){
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


}
