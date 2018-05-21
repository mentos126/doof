package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CalendarFragment;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.util.Pair;
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
import android.widget.Button;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.ListStoreFragment;
import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment.ListStoreMealAdapterFragment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.LoginActivity;
import fr.doofapp.doof.Notify.NotifyCalendarService;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class CalendarFragment extends Fragment {

    private List<Pair<Meal,Boolean>> mealNewList = new ArrayList<Pair<Meal,Boolean>>();
    private RecyclerView newMeals;
    private CalendarAdapterFragment mNewAdapter;

    private List<Pair<Meal,Boolean>> mealOldList = new ArrayList<Pair<Meal,Boolean>>();
    private RecyclerView oldMeals;
    private CalendarAdapterFragment mOldAdapter;

    private UserDAO db;

    View rootView;
    Dialog dialog;

    Button notif;

    public CalendarFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new UserDAO(getActivity());

        /*newMeals = rootView.findViewById(R.id.calendarNewMeals);
        newMeals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mNewAdapter = new CalendarAdapterFragment(getContext(),mealNewList);
        newMeals.setAdapter(mNewAdapter);
        RecyclerView.LayoutManager mOnlineLayoutManager = new LinearLayoutManager(getContext());
        newMeals.setLayoutManager(mOnlineLayoutManager);
        newMeals.setItemAnimator(new DefaultItemAnimator());
        newMeals.setAdapter(mNewAdapter);*/

        newMeals = rootView.findViewById(R.id.calendarNewMeals);
        mNewAdapter = new CalendarAdapterFragment(getContext(),mealNewList);

        RecyclerView.LayoutManager mNewLayoutManager = new GridLayoutManager(getActivity(), 2);
        newMeals.setLayoutManager(mNewLayoutManager);
        newMeals.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        newMeals.setItemAnimator(new DefaultItemAnimator());
        newMeals.setAdapter(mNewAdapter);


        /************************************/

       /* oldMeals = rootView.findViewById(R.id.calendarOldMeals);
        oldMeals.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mOldAdapter = new CalendarAdapterFragment(getContext(),mealOldList);
        oldMeals.setAdapter(mOldAdapter);
        RecyclerView.LayoutManager mLastLayoutManager = new LinearLayoutManager(getContext());
        oldMeals.setLayoutManager(mLastLayoutManager);
        oldMeals.setItemAnimator(new DefaultItemAnimator());
        oldMeals.setAdapter(mOldAdapter);*/

        oldMeals = rootView.findViewById(R.id.calendarOldMeals);
        mOldAdapter = new CalendarAdapterFragment(getContext(),mealOldList);

        RecyclerView.LayoutManager mOldLayoutManager = new GridLayoutManager(getActivity(), 2);
        oldMeals.setLayoutManager(mOldLayoutManager);
        oldMeals.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        oldMeals.setItemAnimator(new DefaultItemAnimator());
        oldMeals.setAdapter(mOldAdapter);



        // test
        /*notif = (Button) rootView.findViewById(R.id.notif);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                builder.setSmallIcon(R.drawable.ic_cake_white_24dp);
                builder.setContentTitle("My title");
                builder.setContentText("My Text");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
                stackBuilder.addParentStack(LoginActivity.class);
                stackBuilder.addNextIntent(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setAutoCancel(true);
                NotificationManager NM = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                NM.notify(0, builder.build());

            }
        });*/

        prepareMealData();

    }

    protected void prepareMealData(){
        User u = null;
        db.open();
        u = db.getUserConnected();
        db.close();

        String URL = URLProject.getInstance().getCALENDAR()+"/"+u.getToken();
        dialog = ProgressDialog.show(getActivity(), "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=========MEALS========", response.toString());

                        if (response.isNull("error")){
                            try {
                                Meal meal;
                                JSONArray newsList = response.getJSONObject("result").getJSONArray("new");
                                int countNews = newsList.length();
                                for(int i = 0; i<countNews; i++){
                                    JSONObject news = newsList.getJSONObject(i);
                                    Log.e("======NEW"+i+"=====",news.toString());

                                    JSONObject LatLng;
                                    LatLng = news.getJSONObject("adresse");
                                    final double lat = parseDouble(LatLng.get("lat").toString());
                                    final double lng = parseDouble(LatLng.get("lng").toString());

                                    JSONArray orders = news.getJSONArray("orders");
                                    JSONObject order = orders.getJSONObject(0);

                                    meal = new Meal(
                                            order.get("photo").toString(),
                                            parseInt(order.get("prix").toString()),
                                            order.get("titre").toString(),
                                            news.get("_id").toString(),
                                            news.get("date").toString()+"  "+news.get("creneau").toString(),
                                            order.get("description").toString(),
                                            LatLng.get("rue").toString(),
                                            new LatLng(lat,lng)
                                    );
                                    meal.setNbPart(parseInt(news.get("total").toString()));
                                    //TODO mettre false car c'est des news
                                    meal.setComment(false/*news.getBoolean("commenter")*/);
                                    meal.setSold(parseInt(news.get("vendu").toString()));
                                    Boolean b = news.getBoolean("vendre");

                                    Pair<Meal,Boolean> p = new Pair<>(meal,b);
                                    mealNewList.add(p);

                                }
                                mNewAdapter.notifyDataSetChanged();

                               /* JSONArray oldsList = response.getJSONObject("result").getJSONArray("old");
                                int countOlds = oldsList.length();
                                for(int i = 0; i<countOlds; i++){
                                    JSONObject olds = oldsList.getJSONObject(i);
                                    Log.e("======NEW"+i+"=====",olds.toString());

                                    JSONObject LatLng;
                                    LatLng = olds.getJSONObject("adresse");
                                    final double lat = parseDouble(LatLng.get("lat").toString());
                                    final double lng = parseDouble(LatLng.get("lng").toString());

                                    JSONArray orders = olds.getJSONArray("orders");
                                    JSONObject order = orders.getJSONObject(0);

                                    meal = new Meal(
                                            order.get("photo").toString(),
                                            parseInt(order.get("prix").toString()),
                                            order.get("titre").toString(),
                                            olds.get("_id").toString(),
                                            olds.get("date").toString()+"  "+olds.get("creneau").toString(),
                                            order.get("description").toString(),
                                            LatLng.get("rue").toString(),
                                            new LatLng(lat,lng)
                                    );
                                    meal.setNbPart(parseInt(olds.get("total").toString()));
                                    meal.setComment(olds.getBoolean("commenter"));
                                    meal.setSold(parseInt(olds.get("vendu").toString()));
                                    Boolean b = olds.getBoolean("vendre");

                                    Pair<Meal,Boolean> p = new Pair<>(meal,b);
                                    mealOldList.add(p);

                                }
                                mOldAdapter.notifyDataSetChanged();*/



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getActivity(), "Une erreur est survennue", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

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

