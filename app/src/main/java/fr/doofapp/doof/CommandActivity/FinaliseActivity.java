package fr.doofapp.doof.CommandActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.doofapp.doof.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise);

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

    protected void getUserData(){}
    protected void onActionModifyPayment(){}
    protected void onActionRegisterCaution(){}
    protected void onActionTutorial(){}
    protected void onActionValidate(){}
    protected void onActionPayment(){}

}
