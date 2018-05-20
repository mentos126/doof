package fr.doofapp.doof.CommandActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.DownLoadImageTask;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.LoginActivity;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class CommandMealActivity extends AppCompatActivity {

    ImageView photo, star1, star2, star3, star4, star5, meal_photo;
    TextView name, note_totale, price, meal_title, meal_description,
            how_many, date, heure, adress, if_contenant;
    Button minus, plus, validate;

    private Meal meal;
    private int nbMeals;

    private List<String> allergens;
    LinearLayout rel_primary, rel_secondary;
    Button bt;

    int maxNbPart;
    Dialog dialog;

    private UserDAO db;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_meal);

        maxNbPart = 0;

        allergens = new ArrayList<>();
        //db = new UserDAO(getApplicationContext());
        db = new UserDAO(this);

        meal = CommandCache.getMeal();
        Log.e("=============",meal.getLinkMeal());
        nbMeals = 0;

        CommandCache.setNbPart(0);

        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommandMealActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        rel_primary = (LinearLayout) findViewById(R.id.rel_primary);
        rel_secondary = (LinearLayout) findViewById(R.id.rel_secondary);

        photo = (ImageView) findViewById(R.id.prompt_photo);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);

        meal_photo = (ImageView) findViewById(R.id.prompt_meal);
        Meal m = CommandCache.getMeal();
        new DownLoadImageTask(meal_photo).execute(m.getPhoto());

        name = (TextView) findViewById(R.id.prompt_name);
        note_totale = (TextView) findViewById(R.id.note_totale);

        price = (TextView) findViewById(R.id.prompt_price);
        price.setText(String.valueOf(m.getPrice()+" tickets"));

        meal_title = (TextView) findViewById(R.id.prompt_meal_title );
        meal_title.setText(m.getName());

        meal_description = (TextView) findViewById(R.id.prompt_meal_description);
        meal_description.setText(m.getDescription());

        how_many = (TextView) findViewById(R.id.prompt_how_many);

        date = (TextView) findViewById(R.id.prompt_date);
        date.setText(m.getDate());
        heure = (TextView) findViewById(R.id.prompt_heure);
        Log.e("======DATE=====",m.getDate());
        heure.setText(m.getDate().trim().split("\\\\s+")[0]);

        adress = (TextView) findViewById(R.id.prompt_adress);
        adress.setText(m.getAdress());

        if_contenant = (TextView) findViewById(R.id.prompt_if_contenant);
        String s ="";
        if(m.getContain()){
            s = "POSSEDE CONTENANT: OUI";
        }else{
            s = "POSSEDE CONTENANT: NON";
        }
        if_contenant.setText(s);

        minus = (Button) findViewById(R.id.minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doMinusAction();
            }
        });

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPlusAction();
            }
        });

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doValidateAction();
            }
        });

        s = nbMeals+"";
        how_many.setText(s);
        getMealWeb();
    }

    public void getMealWeb(){
        String link = meal.getLinkMeal();
        String URL = URLProject.getInstance().getONEMEAL()+"/"+meal.getLinkMeal();

        dialog = ProgressDialog.show(this, "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        dialog.dismiss();
                        try {

                            if(!response.isNull("error")){
                                Toast.makeText(CommandMealActivity.this, "ERREUR SERVEUR",Toast.LENGTH_LONG).show();
                            }else {

                                JSONObject user = response.getJSONObject("result").getJSONObject("user");
                                JSONObject meal = response.getJSONObject("result").getJSONObject("meal");
                                CommandCache.setIdOrder(meal.getJSONArray("orders").getJSONObject(0).get("_id").toString());

                                final String link =  user.get("_id").toString();
                                maxNbPart = parseInt(meal.getJSONArray("orders").getJSONObject(0).get("nbPart").toString());

                                new DownLoadImageTask(photo).execute(user.get("photo").toString());
                                photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CommandMealActivity.this, ProfileActivity.class);
                                        intent.putExtra("Link", link);
                                        startActivity(intent);
                                    }
                                });

                                //new DownLoadImageTask(meal_photo).execute(response.get("meal_photo").toString());

                                String s = user.get("nom").toString() + " " + user.get("prenom").toString();
                                name.setText(s);

                                JSONArray com = user.getJSONArray("commentaires");
                                /*JSONObject jnote = user.getJSONObject("note");

                                double noteTotale = 0;
                                if(!jnote.isNull("accueil") && !jnote.isNull("proprete") && !jnote.isNull("cuisine")){
                                    double home = parseDouble(jnote.get("accueil").toString());
                                    double cleanless = parseDouble(jnote.get("proprete").toString());
                                    double cook = parseDouble(jnote.get("cuisine").toString());
                                    noteTotale = (home+cleanless+cook)/3;
                                }*/

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

                                /*s = response.get("meal_price").toString() + " tickets";
                                price.setText(s);

                                s = response.get("meal_title").toString();
                                meal_title.setText(s);

                                s = response.get("meal_description").toString();
                                meal_description.setText(s);*/

                                s = meal.get("date").toString();
                                date.setText(s);

                                s = meal.get("creneau").toString();
                                heure.setText(s);

                                /*s = response.get("meal_adress").toString();
                                adress.setText(s);

                                s = response.get("meal_is_conteant").toString();
                                if(response.getBoolean("meal_is_conteant")){
                                    if_contenant.setText(getResources().getString(R.string.is_true_contenant));
                                }else{
                                    if_contenant.setText(getResources().getString(R.string.is_false_contenant));
                                }*/
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
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);



    }

    public void doMinusAction(){
        if (nbMeals <= 0){
            nbMeals = 0;
        }else{
            nbMeals--;
        }
        String s = nbMeals+"";
        how_many.setText(s);
        CommandCache.setNbPart(nbMeals);
    }

    public void doPlusAction(){
        if (nbMeals >= maxNbPart){
            nbMeals = maxNbPart;
        }else if (nbMeals <= 0){
            nbMeals = 1;
        }else {
            nbMeals++;
        }
        String s = nbMeals+"";
        how_many.setText(s);
        CommandCache.setNbPart(nbMeals);

    }

    public void doValidateAction(){

        if(userIsConnected()){

            if(nbMeals > 0){
                //TODO Comment géré les allergenes ??

                List<Meal> meals = new ArrayList<>();
                meals.add(meal);
                CommandCache.setMeals(meals);

                List<Integer> prices = new ArrayList<>();
                prices.add(nbMeals);
                CommandCache.setPrices(prices);

                //TODO add allergenes dans Meal
                for (Meal i : meals) {
                    if (!allergens.contains(i.getDescription())){
                        //allergens.add(i.getDescription());
                    }
                }
                CommandCache.setAllergens(allergens);

                Intent myIntent = new Intent(CommandMealActivity.this, RecapitulativeActivity.class);
                startActivity(myIntent);
            }else{
                Toast.makeText(this, R.string.prompt_error_nbportion,Toast.LENGTH_LONG).show();
            }
        }else{
            rel_primary.setVisibility(View.GONE);
            rel_secondary.setVisibility(View.VISIBLE);
        }

    }

    private boolean userIsConnected() {

        db.open();
        u = null;
        u = db.getUserConnected();
        db.close();
        if(u == null){
            Log.e("=======USER=ID=========","NULL");
        }else{
            Log.e("=======USER=ID=========",u.getUserId());
        }
        if (u != null && u.getConnected() == 1){
            Log.e("=======USER=CO=========","YES");
            return true;
        }else{
            Log.e("=======USER=CO=========","NO");
            return false;
        }

    }

}
