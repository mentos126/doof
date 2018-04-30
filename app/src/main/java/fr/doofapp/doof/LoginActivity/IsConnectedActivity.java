package fr.doofapp.doof.LoginActivity;

import android.app.Activity;
import android.os.Bundle;

import fr.doofapp.doof.R;

public class IsConnectedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_connected);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
