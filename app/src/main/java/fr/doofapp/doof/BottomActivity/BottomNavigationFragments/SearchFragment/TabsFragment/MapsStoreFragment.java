package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.CommandActivity.CommandMealActivity;
import fr.doofapp.doof.R;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class MapsStoreFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    List<Meal> mealList = new ArrayList<>();
    Bitmap b;

    Dialog dialog;

    LocationManager locationManager;
    LocationListener locationListener;

    public MapsStoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps_store, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync((OnMapReadyCallback) this);
        }


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                CameraPosition cam = CameraPosition.builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(14).build();
                mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };





       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
                return;
            } else {
                configureSettingsLocation();
            }
        } else {
            configureSettingsLocation();
        }*/


    }

    @SuppressLint("MissingPermission")
    private void configureSettingsLocation() {
        locationManager.requestLocationUpdates("gps", 2000, 2, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureSettingsLocation();
                }
                return;
            case 15:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String locationProvider = LocationManager.NETWORK_PROVIDER;
                    @SuppressLint("MissingPermission")
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                    //TODO error last location
                    //CameraPosition cam = CameraPosition.builder().target(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())).zoom(14).build();
                    CameraPosition cam = CameraPosition.builder().target(new LatLng(3, 4)).zoom(14).build();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam));
                }
                return;
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final Intent myIntent = new Intent(getView().getContext(), CommandMealActivity.class);
        Meal mTemp = (Meal) marker.getTag();
        CommandCache.setMeal(mTemp);
        startActivity(myIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(43.632344,3.844843)).zoom(14).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

        mGoogleMap.setOnInfoWindowClickListener(this);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 15);
            return;
        }else{
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            //TODO errror last loation
            /*Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            CameraPosition cam = CameraPosition.builder().target(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())).zoom(14).build();*/
            CameraPosition cam = CameraPosition.builder().target(new LatLng(3, 4)).zoom(14).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cam));
        }

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
                                    meal.setContain(jsonOrders.getBoolean("contenant"));

                                    final Meal finalMeal = meal;
                                    Volley.newRequestQueue(getActivity().getApplicationContext())
                                            .add(new ImageRequest(meal.getPhoto(), new Response.Listener<Bitmap>() {
                                                @Override
                                                public void onResponse(Bitmap bitmap) {
                                                    b = bitmap;

                                                    Marker marker = mGoogleMap.addMarker(
                                                            new MarkerOptions()
                                                                    .position(new LatLng(lat,lng))
                                                                    .title(finalMeal.getName())
                                                                    .snippet(finalMeal.getDate()+" "+finalMeal.getDescription())
                                                                    .snippet(getString(R.string.prompt_commend))
                                                                    .icon(BitmapDescriptorFactory.fromBitmap(b))
                                                                    .anchor(0.5f, 1)

                                                    );
                                                    marker.setTag(finalMeal);

                                                }
                                            }, 100, 100, null, null));
                                    mealList.add(meal);
                                }
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

