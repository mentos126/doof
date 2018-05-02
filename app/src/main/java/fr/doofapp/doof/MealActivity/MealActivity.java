package fr.doofapp.doof.MealActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

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

public class MealActivity extends AppCompatActivity {

    ImageView photo, star1, star2, star3, star4, star5, meal_photo;
    TextView name, note_totale, price, meal_title, meal_description,
             date, heure, adress, if_contenant;

    private Meal meal;
    private List<String> allergens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        allergens = new ArrayList<>();

        meal = (Meal) getIntent().getSerializableExtra("Meal");
        Log.e("=============",meal.getLinkMeal());

        photo = (ImageView) findViewById(R.id.prompt_photo);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        meal_photo = (ImageView) findViewById(R.id.prompt_meal);

        name = (TextView) findViewById(R.id.prompt_name);
        note_totale = (TextView) findViewById(R.id.note_totale);
        price = (TextView) findViewById(R.id.prompt_price);
        meal_title = (TextView) findViewById(R.id.prompt_meal_title );
        meal_description = (TextView) findViewById(R.id.prompt_meal_description);
        date = (TextView) findViewById(R.id.prompt_date);
        heure = (TextView) findViewById(R.id.prompt_heure);
        adress = (TextView) findViewById(R.id.prompt_adress);
        if_contenant = (TextView) findViewById(R.id.prompt_if_contenant);

        getMealWeb();

    }

    public void getMealWeb(){
        String link = meal.getLinkMeal();
        //TODO use link for url
        String URL = URLProject.getInstance().getONEMEAL();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        try {

                            new DownLoadImageTask(photo).execute(response.get("cook_photo").toString());
                            new DownLoadImageTask(meal_photo).execute(response.get("meal_photo").toString());

                            String s = response.get("cook_name").toString();
                            name.setText(s);

                            s = response.get("cook_note").toString() + "/5  "+
                                    "X"+ getResources().getString(R.string.opinions);
                            note_totale.setText(s);
                            double noteTotale = parseDouble(response.get("cook_note").toString());
                            if(noteTotale < 5.0) {
                                star5.setImageResource(R.drawable.ic_home_black_24dp);
                                if(noteTotale < 4.0) {
                                    star4.setImageResource(R.drawable.ic_home_black_24dp);
                                    if(noteTotale < 3.0) {
                                        star3.setImageResource(R.drawable.ic_home_black_24dp);
                                        if(noteTotale < 2.0) {
                                            star2.setImageResource(R.drawable.ic_home_black_24dp);
                                            if(noteTotale < 1.0) {
                                                star1.setImageResource(R.drawable.ic_home_black_24dp);
                                            }
                                        }
                                    }
                                }
                            }

                            s = response.get("meal_price").toString() + " tickets";
                            price.setText(s);

                            s = response.get("meal_title").toString();
                            meal_title.setText(s);

                            s = response.get("meal_description").toString();
                            meal_description.setText(s);

                            s = response.get("meal_day").toString();
                            date.setText(s);

                            s = response.get("meal_hour").toString();
                            heure.setText(s);

                            s = response.get("meal_adress").toString();
                            adress.setText(s);

                            s = response.get("meal_is_conteant").toString();
                            if(response.getBoolean("meal_is_conteant")){
                                if_contenant.setText(getResources().getString(R.string.is_true_contenant));
                            }else{
                                if_contenant.setText(getResources().getString(R.string.is_false_contenant));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

    }

}
