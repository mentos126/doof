package fr.doofapp.doof.LoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.doofapp.doof.R;

public class RegisterActivity extends AppCompatActivity {

    EditText family_name;
    EditText first_name;
    EditText email;
    EditText pass1;
    EditText pass2;
    EditText adress;
    EditText phone;
    Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button buttonIgnore = (Button) findViewById(R.id.go_to_login);
        buttonIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });

        family_name = (EditText) findViewById(R.id.family_name);
        first_name = (EditText) findViewById(R.id.first_name);
        email = (EditText) findViewById(R.id.email);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        adress = (EditText) findViewById(R.id.adress);
        phone = (EditText) findViewById(R.id.phone);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActionValidate();
            }
        });

    }

    private void doActionValidate() {
        if(!family_name.getText().toString().equals("")){
            if(!first_name.getText().toString().equals("")){
                if(!email.getText().toString().equals("") && email.getText().toString().contains("@")){
                    if(pass1.getText().toString().length() >= 8 && pass1.getText().toString().equals(pass2.getText().toString())){
                        if(!adress.getText().toString().equals("")){
                                //TODO request register account
                        }else {
                            Toast.makeText(this, R.string.prompt_error_adress,Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this,R.string.prompt_error_password,Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(this,R.string.prompt_error_email,Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(this, R.string.prompt_error_firstname,Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, R.string.prompt_error_familyname,Toast.LENGTH_LONG).show();
        }

    }
}
