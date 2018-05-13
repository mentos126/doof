package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

public class Step1CookFragment extends Fragment {
    View rootView;
    int thisDay, thisYear, thisMonth;
    TimePicker begin;
    NumberPicker end;
    DatePicker date;
    Button next;
    int bHour, bMin, eMin;
    private UserDAO db;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView =  inflater.inflate(R.layout.fragment_cook_step1, container, false);

        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(getActivity(), new HttpClientStack(mHttpClient));
        db = new UserDAO(getActivity());

        Calendar calendar = Calendar.getInstance();
        thisYear = calendar.get(Calendar.YEAR);
        thisMonth = calendar.get(Calendar.MONTH);
        thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        date = (DatePicker) rootView.findViewById(R.id.date_picker);
        date.init(thisYear,thisMonth,thisDay,null);

        begin = (TimePicker) rootView.findViewById(R.id.time_picker_begin);
        begin.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            begin.setHour(0);
            begin.setMinute(0);
        }
        begin.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                bHour = timePicker.getCurrentHour();
                bMin = timePicker.getCurrentMinute();
            }
        });

        end = (NumberPicker) rootView.findViewById(R.id.time_picker_end);
        end.setMinValue(0);
        end.setMaxValue(2000);
        end.setWrapSelectorWheel(true);
        end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                eMin = newVal;
            }
        });

        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d = getDate();
                String t = "";
                int tempHour = (bHour +( (bMin + eMin) / 60)) % 24;
                int tempMin = (bMin + eMin) % 60;
                t = bHour+":"+bMin+"-"+tempHour+":"+tempMin;

                getDate();

                String adress = "ERREUR";

                if (!d.equals("") && !t.equals("")){

                    List<String> strs = new ArrayList<>();
                    List<Bitmap> photos = new ArrayList<>();
                    List<Integer> ints = new ArrayList<>();
                    ListMealCache.setTitles(strs);
                    ListMealCache.setDescriptions(strs);
                    ListMealCache.setPhotos(photos);
                    ListMealCache.setNbPortions(ints);
                    ListMealCache.setPrices(ints);
                    ListMealCache.setAdress(adress);
                    ListMealCache.setDate(d);
                    ListMealCache.setTime(t);
                    ListMealCache.setIsContain(false);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Step2CookFragment step2CookFragment = new Step2CookFragment();
                    fragmentTransaction.replace(R.id.frame_cook_container, step2CookFragment);
                    fragmentTransaction.commit();

                }else{
                    Toast.makeText(getActivity(), R.string.prompt_error_fild,Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rootView;
    }

    public String getDate() {
        StringBuilder builder=new StringBuilder();
        if(date.getYear() >= thisYear && date.getMonth() >= thisMonth && date.getDayOfMonth() >= thisDay){
            builder.append(date.getDayOfMonth()+"-");
            builder.append((date.getMonth() + 1)+"-");
            builder.append(date.getYear());
        }
        return builder.toString();
    }


}
