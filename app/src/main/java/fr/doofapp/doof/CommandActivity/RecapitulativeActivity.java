package fr.doofapp.doof.CommandActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

public class RecapitulativeActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView date_hoour;
    TextView prompt_allergen;
    TextView prompt_adress;
    Button go_to_maps;
    TextView prompt_all_items;
    TextView prompt_total;
    Button validate;
    LinearLayout rel;

    List<Meal> meals = new ArrayList<>();
    List<Integer> prices = new ArrayList<>();
    List<String> allergens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulative);
        meals = CommandCache.getMeals();
        prices = CommandCache.getPrices();
        allergens = CommandCache.getAllergens();

        Meal m = meals.get(0);

        date_hoour = (TextView) findViewById(R.id.date_hoour);
        String s = "Heure: à " + m.getDate();
        date_hoour.setText(s);

        prompt_allergen = (TextView) findViewById(R.id.prompt_allergen);
        s = "";
        for (String i : allergens) {
            s += i + " ";
        }
        Log.e("=====SSSS====",s);
        prompt_allergen.setText(s);

        prompt_adress = (TextView) findViewById(R.id.prompt_adress);
        s = m.getAdress();
        prompt_adress.setText(s);

        prompt_all_items = (TextView) findViewById(R.id.prompt_all_items);
        s = "";
        int countMeals = meals.size();
        for (int i = 0; i < countMeals; i++) {
            s += prices.get(i) + "X     " + meals.get(i).getName() + "      " + meals.get(i).getPrice() + " tickets";
        }
        prompt_all_items.setText(s);

        prompt_total = (TextView) findViewById(R.id.prompt_total);
        int nbTickets = 0;
        countMeals = meals.size();
        for (int i = 0; i < countMeals; i++) {
            nbTickets += (meals.get(i).getPrice() * prices.get(i));
        }
        s = "Au total: " + nbTickets + " tickets";
        prompt_total.setText(s);

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRequestValidate();
            }
        });

        go_to_maps = (Button) findViewById(R.id.go_to_maps);
        go_to_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doactionGoToMaps();
            }
        });
    }

    public void doRequestValidate() {
        Intent myIntent = new Intent(RecapitulativeActivity.this, FinaliseActivity.class);
        CommandCache.setMeals(meals);
        CommandCache.setPrices(prices);
        CommandCache.setAllergens(allergens);
        startActivity(myIntent);
    }

    public void doactionGoToMaps() {
        rel = (LinearLayout) findViewById(R.id.rel);
        rel.setVisibility(View.VISIBLE);
        MapView mapFragment = (MapView) findViewById(R.id.map);
        if (mapFragment != null) {
            mapFragment.onCreate(null);
            mapFragment.onResume();
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 20);
            return;
        }else{
            LatLng cam = new LatLng(meals.get(0).getLatlng().latitude, meals.get(0).getLatlng().longitude);
            GoogleMap map = googleMap;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cam, 13));
            map.addMarker(new MarkerOptions()
                    .title(meals.get(0).getName())
                    .snippet(meals.get(0).getDescription())
                    .position(cam));
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 20:{
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                    final LatLng cam = new LatLng(meals.get(0).getLatlng().latitude, meals.get(0).getLatlng().latitude);
                    final GoogleMap map = null;
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(cam, 13));

                    Volley.newRequestQueue(getApplicationContext())
                            .add(new ImageRequest(meals.get(0).getPhoto(), new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {

                                     Marker marker = map.addMarker(
                                            new MarkerOptions()
                                                    .title(meals.get(0).getName())
                                                    .snippet(meals.get(0).getDescription()+" "+meals.get(0).getDate())
                                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                                    .anchor(0.5f, 1)
                                                    .position(cam)

                                    );
                                    marker.setTag(meals.get(0));

                                }
                            }, 100, 100, null, null));

                }else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
