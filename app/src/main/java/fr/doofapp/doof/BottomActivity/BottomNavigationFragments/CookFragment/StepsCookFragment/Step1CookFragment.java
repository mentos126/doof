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
import android.widget.EditText;
import android.widget.Toast;

import fr.doofapp.doof.R;

public class Step1CookFragment extends Fragment {
    View rootView;
    EditText time;
    EditText date;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView =  inflater.inflate(R.layout.fragment_cook_step1, container, false);

        //TODO get information of user "adresses"

        time = (EditText) rootView.findViewById(R.id.time);
        date = (EditText) rootView.findViewById(R.id.date);
        next = (Button) rootView.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d = date.getText().toString();
                String t = time.getText().toString();

                if (!d.equals("") && !t.equals("")){

                    Bundle bundle = new Bundle();
                    bundle.putString("Date", d);
                    bundle.putString("Time", t);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Step2CookFragment step2CookFragment = new Step2CookFragment();
                    step2CookFragment.setArguments(bundle);

                    Log.e("====COMMIT====","=======COMIT====");

                    fragmentTransaction.replace(R.id.frame_cook_container, step2CookFragment);
                    fragmentTransaction.commit();

                }else{
                    Toast.makeText(getActivity(), R.string.prompt_error_fild,Toast.LENGTH_LONG).show();
                }


            }
        });

        return rootView;
    }
}
