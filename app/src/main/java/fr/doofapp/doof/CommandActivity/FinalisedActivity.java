package fr.doofapp.doof.CommandActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.R;

public class FinalisedActivity extends Activity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalised_meal);

        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(FinalisedActivity.this, BottomActivity.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
