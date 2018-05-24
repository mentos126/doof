package fr.doofapp.doof.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.doofapp.doof.R;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText pass1, pass2, email;
    Button validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = (EditText) findViewById(R.id.email);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doActionValidate();
            }
        });

    }

    private void doActionValidate() {

        Toast.makeText(this,"PAS ENCORE IMPLEMENTER",Toast.LENGTH_SHORT).show();
        /*if(!email.getText().toString().equals("") &&
                email.getText().toString().contains("@")){
            if(pass1.getText().toString().length() >= 8 &&
                    pass1.getText().toString().equals(pass2.getText().toString())){

                //TODO request reset password
                Toast.makeText(this, "PAS ENCORE IMPLEMENTER",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);*/

            /*}else{
                Toast.makeText(this, R.string.prompt_error_password,Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, R.string.prompt_error_email,Toast.LENGTH_LONG).show();
        }*/
    }

}
