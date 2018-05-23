package fr.doofapp.doof.MealActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;

public class MealActivity extends AppCompatActivity {

    ImageView photo, star1, star2, star3, star4, star5, meal_photo;
    TextView name, note_totale, price, meal_title, meal_description,
             date, heure, adress, if_contenant;

    private Meal meal;
    private List<String> allergens;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        allergens = new ArrayList<>();

        meal = CommandCache.getMeal();

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

        String URL = URLProject.getInstance().getONEMEAL()+"/"+meal.getLinkMeal();
        Log.e("========MEALURL========",URL);
        dialog = ProgressDialog.show(this, "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        try {
                            if(response.isNull("error")){
                                JSONObject res = response.getJSONObject("result");
                                JSONObject user = res.getJSONObject("user");
                                JSONObject meal = res.getJSONObject("meal");

                                final String link =  user.get("_id").toString();
                                new DownLoadImageTask(photo).execute(user.get("photo").toString());
                                photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MealActivity.this, ProfileActivity.class);
                                        intent.putExtra("Link", link);
                                        startActivity(intent);
                                    }
                                });

                                JSONObject order = meal.getJSONArray("orders").getJSONObject(0);

                                new DownLoadImageTask(meal_photo).execute(order.get("photo").toString());

                                String s = user.get("nom").toString() + " " + user.get("prenom").toString();
                                name.setText(s);

                                JSONArray com = user.getJSONArray("commentaires");
                                double noteTotale = parseDouble(user.get("note").toString());
                                s = noteTotale + "/5  "+ com.length()+ " " + getResources().getString(R.string.opinions);
                                note_totale.setText(s);

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

                                s = order.get("prix").toString() + " tickets";
                                price.setText(s);

                                s = order.get("titre").toString();
                                meal_title.setText(s);

                                s = order.get("description").toString();
                                meal_description.setText(s);

                                s = meal.get("date").toString();
                                date.setText(s);

                                s = meal.get("creneau").toString();
                                heure.setText(s);

                                s = meal.getJSONObject("adresse").get("rue").toString();
                                adress.setText(s);

                                if(order.getBoolean("contenant")){
                                    if_contenant.setText(getResources().getString(R.string.is_true_contenant));
                                }else{
                                    if_contenant.setText(getResources().getString(R.string.is_false_contenant));
                                }

                            }else{
                                Toast.makeText(getApplicationContext(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.prompt_error_impossible),Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

    }

}
