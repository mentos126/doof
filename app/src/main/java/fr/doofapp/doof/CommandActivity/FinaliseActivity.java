package fr.doofapp.doof.CommandActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;
import fr.doofapp.doof.TutorialChangeActivity.TutorialChangeActivity;

public class FinaliseActivity extends AppCompatActivity {

    private TextView prompt_title_payment;
    private Button payment_modify;
    private CheckBox checkBox_not_tickets;
    private ImageView img_payment;
    private TextView prompt_payment;
    /**********************/
    private TextView prompt_title_not_payment;
    private Button button_payment;
    /**********************/
    private TextView prompt_system_payment;
    private TextView prompt_limit_payment;
    private CheckBox checkBox_tickets;
    private TextView prompt_sold;
    /**********************/
    private TextView prompt_integrate_system;
    private Button button_register_caution;
    private Button button_tutorial;
    /**********************/
    private TextView prompt_nbtikets;
    private Button validate;
    /*************************/
    private LinearLayout rel_use_payment;
    private LinearLayout rel_add_payment;
    /**********OR*************/
    private LinearLayout rel_use_tickets;
    private LinearLayout rel_add_system_payment;

    int nbTikets =0;
    int price = -1;
    String moyenPaiement="";
    Boolean isMoyenPayment;
    Boolean isSystemChange;
    List<Meal> meals = new ArrayList<>();
    List<Integer> prices = new ArrayList<>();
    List<String> allergens = new ArrayList<>();

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("Ms", true);
        savedInstanceState.putSerializable("Meals",meals);
        savedInstanceState.putIntArray("Prices", prices);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise);

        meals = (List<Meal>) getIntent().getSerializableExtra("Meals");
        prices = (List<Integer>) getIntent().getSerializableExtra("Prices");
        allergens = (List<String>) getIntent().getSerializableExtra("Allergens");

        /***************************************/

        prompt_title_payment = (TextView) findViewById(R.id.prompt_title_payment);

        payment_modify = (Button) findViewById(R.id.payment_modify);
        payment_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionModifyPayment();
            }
        });

        checkBox_not_tickets = (CheckBox) findViewById(R.id.checkBox_not_tickets);
        img_payment = (ImageView) findViewById(R.id.img_payment);
        prompt_payment = (TextView) findViewById(R.id.prompt_payment);

        /***************************************/

        prompt_title_not_payment = (TextView) findViewById(R.id.prompt_title_not_payment);

        button_payment = (Button) findViewById(R.id.button_payment);
        button_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionPayment();
            }
        });

        /**************************************/

        prompt_system_payment = (TextView) findViewById(R.id.prompt_system_payment);
        prompt_limit_payment = (TextView) findViewById(R.id.prompt_limit_payment);
        checkBox_tickets = (CheckBox) findViewById(R.id.checkBox_tickets);
        prompt_sold = (TextView) findViewById(R.id.prompt_sold);

        /*****************************************/

        prompt_integrate_system = (TextView) findViewById(R.id.prompt_integrate_system);

        button_register_caution = (Button) findViewById(R.id.button_register_caution);
        button_register_caution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionRegisterCaution();
            }
        });

        button_tutorial = (Button) findViewById(R.id.button_tutorial);
        button_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionTutorial();
            }
        });

        /*****************************************/

        prompt_nbtikets = (TextView) findViewById(R.id.prompt_nbtikets);
        price = 0;
        for (int i : prices){
            price += i;
        }
        String s = price+" tickets";
        prompt_nbtikets.setText(s);

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionValidate();
            }
        });

        /******************************************/
        rel_use_payment = (LinearLayout) findViewById(R.id.rel_use_payment);
        rel_use_payment.setVisibility(View.INVISIBLE);
        rel_add_payment = (LinearLayout) findViewById(R.id.rel_add_payment);
        rel_add_payment.setVisibility(View.INVISIBLE);
        /********************************************/
        rel_use_tickets = (LinearLayout) findViewById(R.id.rel_use_tickets);
        rel_use_tickets.setVisibility(View.INVISIBLE);
        rel_add_system_payment = (LinearLayout) findViewById(R.id.rel_add_system_payment);
        rel_add_system_payment.setVisibility(View.INVISIBLE);
        /********************************************/

        getUserData();

    }

    protected void getUserData(){
        //TODO request: getNbTikets, getMoyenPaiement,
        nbTikets = 48;
        moyenPaiement = "CB";
        isMoyenPayment = true;
        isSystemChange = false;

        String s;
        if(isMoyenPayment){
            rel_use_payment.setVisibility(View.VISIBLE);
            s = moyenPaiement + "1 ticket = 1 €";
            prompt_payment.setText(s);
        }else{
            rel_add_payment.setVisibility(View.VISIBLE);
        }

        if(isSystemChange){
            rel_use_tickets.setVisibility(View.VISIBLE);
            s = getResources().getString(R.string.prompt_sold) +nbTikets;
            prompt_sold.setText(s);
        }else{
            rel_add_system_payment.setVisibility(View.VISIBLE);
        }

    }

    protected void onActionValidate(){
        if(checkBox_not_tickets.isChecked() && checkBox_tickets.isChecked()){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.one_choice),Toast.LENGTH_LONG).show();
        }else if(!checkBox_not_tickets.isChecked() && !checkBox_tickets.isChecked()){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_choice),Toast.LENGTH_LONG).show();
        }else if(checkBox_tickets.isChecked()){
            if(nbTikets >= price){
                //TODO request Tiket
                Intent myIntent = new Intent(FinaliseActivity.this, FinalisedActivity.class);
                startActivity(myIntent);
            }else{
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_enouth_tiket),Toast.LENGTH_LONG).show();
            }
        }else{
            //TODO request CB
            Intent myIntent = new Intent(FinaliseActivity.this, FinalisedActivity.class);
            startActivity(myIntent);
        }

    }

    protected void onActionModifyPayment(){
        //TODO
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTE",Toast.LENGTH_LONG).show();
    }

    protected void onActionRegisterCaution(){
        //TODO
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTE",Toast.LENGTH_LONG).show();
    }

    protected void onActionTutorial(){
        //TODO
        Intent myIntent = new Intent(FinaliseActivity.this, TutorialChangeActivity.class);
        startActivity(myIntent);
    }

    protected void onActionPayment(){
        //TODO
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTE",Toast.LENGTH_LONG).show();
    }

}
