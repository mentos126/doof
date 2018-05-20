package fr.doofapp.doof.CreditActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.R;

public class CreditActivity extends AppCompatActivity {


    private Button minus, plus, validate;
    private TextView how_many;

    private int nbTikets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        nbTikets = 0;

        how_many = (TextView) findViewById(R.id.prompt_how_many);
        String s = ""+nbTikets;
        how_many.setText(s);

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

    }

    private void doValidateAction() {
        //TODO request addTickets

        Intent intent = new Intent(CreditActivity.this, BottomActivity.class);
        intent.putExtra("Tab",4);
        startActivity(intent);
    }


    public void doMinusAction(){
        if (nbTikets <= 0){
            nbTikets = 0;
        }else{
            nbTikets--;
        }
        String s = nbTikets+"";
        how_many.setText(s);
    }

    public void doPlusAction(){
        if (nbTikets <= 0){
            nbTikets = 1;
        }else {
            nbTikets++;
        }
        String s = nbTikets+"";
        how_many.setText(s);

    }
}
