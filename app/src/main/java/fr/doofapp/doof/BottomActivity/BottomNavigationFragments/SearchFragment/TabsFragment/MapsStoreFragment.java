package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.doofapp.doof.R;

public class MapsStoreFragment extends Fragment /*implements GoogleMap.OnMapLongClickListener*/
{

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

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

    //@Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity().getApplicationContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247,-74.44502)).title("statues of liberty").snippet("I hope"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247,-74.44502)).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

    }

    /*@Override
    public void onMapLongClick(LatLng latLng) {

    }*/
}

