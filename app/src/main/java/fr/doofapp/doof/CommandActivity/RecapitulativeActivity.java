package fr.doofapp.doof.CommandActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

public class RecapitulativeActivity extends AppCompatActivity {

    //TODO FAIRE LE LIEN meal activity et finalised activity
    //recoie une liste de plats et execute la commande
    //envoie une requete pour sauvegarder
    //connaitre le nombre de ticket actuel
    //envoyer les tickets au cuisto

    private TextView date_hoour;
    private TextView prompt_allergen;
    private TextView prompt_adress;
    private Button go_to_maps;
    private TextView prompt_all_items;
    private TextView prompt_total;
    private Button validate;

    List<Meal> meals = new ArrayList<>();
    List<Integer> prices = new ArrayList<>();
    List<String> allergens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulative);
        meals = (List<Meal>) getIntent().getSerializableExtra("Meals");
        prices = (List<Integer>) getIntent().getSerializableExtra("Prices");
        allergens = (List<String>) getIntent().getSerializableExtra("Allergens");

        Meal m = meals.get(0);

        date_hoour = (TextView) findViewById(R.id.date_hoour);
        String s = "Heure: à " + m.getDate();
        date_hoour.setText(s);

        prompt_allergen = (TextView) findViewById(R.id.prompt_allergen);
        s = "";
        for(String i : allergens){
            s += i + " ";
        }
        prompt_allergen.setText(s);

        prompt_adress = (TextView) findViewById(R.id.prompt_adress);
        //todo Meal add adress
        s = m.getDescription();
        prompt_adress.setText(s);

        prompt_all_items = (TextView) findViewById(R.id.prompt_all_items);
        s="";
        int countMeals = meals.size();
        for(int i = 0; i < countMeals; i++){
            s += prices.get(i) +"X     " + meals.get(i).getName() + "      " + meals.get(i).getPrice() + " tickets";
        }
        prompt_all_items.setText(s);

        prompt_total = (TextView) findViewById(R.id.prompt_total);
        int nbTickets = 0;
        countMeals = meals.size();
        for(int i = 0; i < countMeals; i++){
            nbTickets += (meals.get(i).getPrice() * prices.get(i));
        }
        s= "Au total: " + nbTickets + " tickets";
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

    public void doRequestValidate(){
        //TODO send request for update data
        //TODO update gere les ticket !!!


        Intent myIntent = new Intent(RecapitulativeActivity.this, FinaliseActivity.class);
        startActivity(myIntent);
    }

    public void doactionGoToMaps(){
        Toast.makeText(RecapitulativeActivity.this,"Impossible pour l'instant",Toast.LENGTH_LONG).show();
    }


}
