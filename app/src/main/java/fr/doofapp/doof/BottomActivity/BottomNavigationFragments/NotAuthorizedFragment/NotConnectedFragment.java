package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.NotAuthorizedFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.doofapp.doof.LoginActivity.LoginActivity;
import fr.doofapp.doof.R;

public class NotConnectedFragment extends Fragment {

    private Button bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_not_connected, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt = (Button) getView().findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
