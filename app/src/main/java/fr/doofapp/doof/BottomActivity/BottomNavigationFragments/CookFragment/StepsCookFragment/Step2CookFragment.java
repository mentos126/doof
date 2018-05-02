package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fr.doofapp.doof.R;


public class Step2CookFragment extends Fragment {

    View rootView;
    Button next;
    String date;
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step2, container, false);

        Bundle bundle = getArguments();

        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();

        Log.e("======DATE======",date);
        Log.e("======TIME======",time);

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String d = date.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("Date", date);
                bundle.putString("Time", time);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Step3CookFragment step3CookFragment = new Step3CookFragment();
                step3CookFragment.setArguments(bundle);

                Log.e("====COMMIT====","=======COMIT====");

                fragmentTransaction.replace(R.id.frame_cook_container, step3CookFragment);
                fragmentTransaction.commit();

            }
        });
        

        return rootView;
    }

}
