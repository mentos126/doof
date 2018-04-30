package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class MapsStoreFragment extends Fragment implements OnMapReadyCallback
{

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    List<Meal> mealList = new ArrayList<>();
    double latitude = 0;
    double longitude = 0;
    Bitmap b;

    public MapsStoreFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_maps_store, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync((OnMapReadyCallback) this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //TODO get POSITION
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247,-74.44502)).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

        prepareMealData();

    }

    protected void prepareMealData(){

        mGoogleMap.getCameraPosition();
        Log.e("======MAP====",mGoogleMap.getCameraPosition().target.toString());


        //TODO use location
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

                                JSONObject LatLng;
                                LatLng = jsonObject.getJSONObject("latlng");
                                final double lat = parseDouble(LatLng.get("lat").toString());
                                final double lng = parseDouble(LatLng.get("lng").toString());

                                final Meal finalMeal = meal;
                                Volley.newRequestQueue(getActivity().getApplicationContext())
                                        .add(new ImageRequest(meal.getPhoto(), new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        b = bitmap;



                                        mGoogleMap.addMarker(
                                                new MarkerOptions()
                                                        .position(new LatLng(lat,lng))
                                                        .title(finalMeal.getName())
                                                        .snippet(finalMeal.getPhoto())
                                                        .icon(BitmapDescriptorFactory.fromBitmap(b))
                                                        .anchor(0.5f, 1)

                                        );

                                        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {
                                                //int position = (int)(marker.getTag());

                                                Toast.makeText(getActivity().getApplicationContext(),
                                                        marker.getTitle(),
                                                        Toast.LENGTH_LONG).show();
                                                return false;
                                            }
                                        });


                                    }
                                    //TODO change size of bitmap
                                }, 100, 100, null, null));
                                mealList.add(meal);
                            }
                            //mAdapter.notifyDataSetChanged();
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

        Log.e("========mLIST=======",mealList.toString());

        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);
    }


}

