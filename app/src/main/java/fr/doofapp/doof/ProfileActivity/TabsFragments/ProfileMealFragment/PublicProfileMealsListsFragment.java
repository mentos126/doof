package fr.doofapp.doof.ProfileActivity.TabsFragments.ProfileMealFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment.MealAdapterFragment;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.R;

import static java.lang.Integer.parseInt;

public class PublicProfileMealsListsFragment extends Fragment {

    private List<Meal> onlineMealList = new ArrayList<>();
    private List<Meal> lastMealList = new ArrayList<>();
    private RecyclerView OnlineMeals;
    private RecyclerView LastMeals;
    private MealAdapterFragment mOnlineAdapter;
    private MealAdapterFragment mLastAdapter;
    private int nbOnlineMeals;
    private int nbLastMeals;
    private TextView tv_old;
    private TextView tv_new;
    private Dialog dialog;
    String link;



    public PublicProfileMealsListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_meals_lists, container, false);

        link = CommandCache.getMeal().getLinkMeal();
        tv_new = (TextView) rootView.findViewById(R.id.tv_news);
        tv_old = (TextView) rootView.findViewById(R.id.tv_olds);

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
        String URL = URLProject.getInstance().getPROFILE_Meal()+"/"+link;
        dialog = ProgressDialog.show(getActivity(), "", "", true);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        Log.e("=========MEALS========", response.toString());
                        try {

                            if(response.isNull("error")){

                                JSONArray news = response.getJSONObject("result").getJSONArray("new");
                                JSONArray olds = response.getJSONObject("result").getJSONArray("old");
                                nbOnlineMeals = parseInt(response.getJSONObject("result").get("nb_online_meals").toString());
                                nbLastMeals = parseInt(response.getJSONObject("result").get("nb_last_meals").toString());
                                String s = nbOnlineMeals+" "+getString(R.string.prompt_meal_online);
                                tv_new.setText(s);
                                s = nbLastMeals+" "+getString(R.string.prompt_meal_last);
                                tv_old.setText(s);
                                Meal meal;

                                //online meals
                                int countObject = news.length();
                                for(int i=0 ; i<countObject; i++){
                                    JSONObject jsonObject;
                                    jsonObject = news.getJSONObject(i);
                                    meal = new Meal(
                                            jsonObject.get("photo").toString(),
                                            parseInt(jsonObject.get("stars").toString()),
                                            jsonObject.get("title").toString(),
                                            jsonObject.get("_id").toString(),
                                            jsonObject.get("date").toString()+" "+jsonObject.get("creneau").toString(),
                                            "description",
                                            "adresse",
                                            new LatLng(4,34)
                                    );
                                    onlineMealList.add(meal);
                                }
                                mOnlineAdapter.notifyDataSetChanged();

                                //last meals
                                countObject = olds.length();
                                for(int i=0 ; i<countObject; i++){
                                    JSONObject jsonObject;
                                    jsonObject = olds.getJSONObject(i);
                                    meal = new Meal(
                                            jsonObject.get("photo").toString(),
                                            parseInt(jsonObject.get("stars").toString()),
                                            jsonObject.get("title").toString(),
                                            jsonObject.get("_id").toString(),
                                            jsonObject.get("date").toString()+" "+jsonObject.get("creneau").toString(),
                                            "description",
                                            "adresse",
                                            new LatLng(4,34)
                                    );
                                    lastMealList.add(meal);
                                }

                                mLastAdapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
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
