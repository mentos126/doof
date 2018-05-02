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
import android.widget.NumberPicker;

import fr.doofapp.doof.R;

public class Step3CookFragment extends Fragment {
    View rootView;
    NumberPicker np;
    NumberPicker price;
    Button next;

    String date;
    String time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step3, container, false);

        Bundle bundle = getArguments();

        date = bundle.get("Date").toString();
        time = bundle.get("Time").toString();

        Log.e("======DATE=====",date);
        Log.e("======TIME=====",time);

        np = (NumberPicker) rootView.findViewById(R.id.nbPortion);
        np.setMinValue(0);
        np.setMaxValue(99);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                Log.e("======NP=======", newVal+"");
            }
        });

        price = (NumberPicker) rootView.findViewById(R.id.nbPortion);
        price.setMinValue(0);
        price.setMaxValue(50);
        price.setWrapSelectorWheel(true);
        price.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                Log.e("======NP=======", newVal+"");
            }
        });

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

                Step5CookFragment step5CookFragment = new Step5CookFragment();
                step5CookFragment.setArguments(bundle);

                Log.e("====COMMIT====","=======COMIT====");

                fragmentTransaction.replace(R.id.frame_cook_container, step5CookFragment);
                fragmentTransaction.commit();

            }
        });

        return rootView;
    }

}
