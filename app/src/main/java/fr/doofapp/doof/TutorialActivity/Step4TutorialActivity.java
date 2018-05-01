package fr.doofapp.doof.TutorialActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.doofapp.doof.R;

public class Step4TutorialActivity extends AppCompatActivity {

    Button before;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4_tutorial);

        next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Step4TutorialActivity.this, Step5TutorialActivity.class);
                startActivity(myIntent);
            }
        });

        before = (Button) findViewById(R.id.button_before);
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Step4TutorialActivity.this, Step3TutorialActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
