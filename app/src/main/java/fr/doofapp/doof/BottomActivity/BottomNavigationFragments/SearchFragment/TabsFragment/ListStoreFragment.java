package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class ListStoreFragment extends Fragment {

    private List<Meal> mealList = new ArrayList<>();
    private RecyclerView meals;
    private ListStoreMealAdapterFragment mAdapter;
    View rootView;


    public ListStoreFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_store, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*meals = rootView.findViewById(R.id.online_meals);
        meals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new ListStoreMealAdapterFragment(getContext(), mealList);
        meals.setAdapter(mAdapter);
        RecyclerView.LayoutManager mOnlineLayoutManager = new LinearLayoutManager(getContext());
        meals.setLayoutManager(mOnlineLayoutManager);
        meals.setItemAnimator(new DefaultItemAnimator());
        meals.setAdapter(mAdapter);*/

        meals = rootView.findViewById(R.id.online_meals);
        mAdapter = new ListStoreMealAdapterFragment(getContext(), mealList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        meals.setLayoutManager(mLayoutManager);
        meals.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        meals.setItemAnimator(new DefaultItemAnimator());
        meals.setAdapter(mAdapter);

        prepareMealData();

    }

    protected void prepareMealData(){

        String URL = URLProject.getInstance().getSEARCH_MEAL();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=========MEALS========", response.toString());
                        try {
                            if(!response.isNull("error")){
                                Toast.makeText(getActivity(), "ERREUR SERVEUR",Toast.LENGTH_LONG).show();
                            }else{
                                JSONArray res = response.getJSONArray("result");
                                Meal meal;
                                int countObject = res.length();
                                for(int i=0 ; i<countObject; i++){
                                    JSONObject jsonObject;
                                    jsonObject = res.getJSONObject(i);
                                    JSONArray orders = jsonObject.getJSONArray("orders");
                                    JSONObject jsonOrders = orders.getJSONObject(0);

                                    JSONObject LatLng;
                                    LatLng = jsonObject.getJSONObject("adresse");
                                    final double lat = parseDouble(LatLng.get("lat").toString());
                                    final double lng = parseDouble(LatLng.get("lng").toString());

                                    meal = new Meal(
                                            jsonOrders.get("photo").toString(),
                                            parseInt(jsonOrders.get("prix").toString()),
                                            jsonOrders.get("titre").toString(),
                                            jsonObject.get("_id").toString(),
                                            jsonObject.get("date").toString()+"  "+jsonObject.get("creneau").toString(),
                                            jsonOrders.get("description").toString(),
                                            LatLng.get("rue").toString(),
                                            new LatLng(lat,lng)
                                    );

                                    meal.setNbPart(parseInt(jsonOrders.get("nbPart").toString()));
                                    meal.setContain(jsonOrders.getBoolean("contenant"));
                                    mealList.add(meal);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
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

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
