package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.List;

import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.R;


public class Step2CookFragment extends Fragment {

    View rootView;
    Button next;
    Button previous;
    String adress;
    CheckBox checkAdress;
    CheckBox checkNewAdress;
    Button modify;
    LinearLayout rel;
    EditText new_adress;

    private UserDAO db;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step2, container, false);

        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(getActivity(), new HttpClientStack(mHttpClient));
        db = new UserDAO(getActivity());

        String URL = URLProject.getInstance().getMY_ADRESS();
        mQueue.add(createRequest(URL));

        rel = (LinearLayout) rootView.findViewById(R.id.rel);

        checkNewAdress = (CheckBox) rootView.findViewById(R.id.check_new_dress);

        new_adress = (EditText) rootView.findViewById(R.id.new_adress);
        new_adress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkNewAdress.setText(editable);
            }
        });

        modify = (Button) rootView.findViewById(R.id.add_new_adress);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rel.getVisibility() == View.VISIBLE){
                    checkNewAdress.setChecked(false);
                    rel.setVisibility(View.INVISIBLE);
                }else{
                    rel.setVisibility(View.VISIBLE);
                    modify.setVisibility(View.INVISIBLE);
                }
            }
        });

        previous = (Button) rootView.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Step1CookFragment step1CookFragment = new Step1CookFragment();
                fragmentTransaction.replace(R.id.frame_cook_container, step1CookFragment);
                fragmentTransaction.commit();
            }
        });


        next = (Button) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAdress.isChecked() && checkNewAdress.isChecked()){
                    Toast.makeText(getActivity(), R.string.prompt_select_adress,Toast.LENGTH_LONG).show();
                }else if(!checkAdress.isChecked() && !checkNewAdress.isChecked()) {
                    Toast.makeText(getActivity(), R.string.prompt_select_prompt_only_one,Toast.LENGTH_LONG).show();
                }else if(checkAdress.isChecked()){
                    adress = checkAdress.getText().toString();
                    ListMealCache.setAdress(adress);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Step3CookFragment step3CookFragment = new Step3CookFragment();
                    fragmentTransaction.replace(R.id.frame_cook_container, step3CookFragment);
                    fragmentTransaction.commit();
                }else{
                    adress = checkNewAdress.getText().toString();
                    ListMealCache.setAdress(adress);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    Step3CookFragment step3CookFragment = new Step3CookFragment();
                    fragmentTransaction.replace(R.id.frame_cook_container, step3CookFragment);
                    fragmentTransaction.commit();
                }


            }
        });

        adress = ListMealCache.getAdress();
//        Log.e("======RESADR==========",adress);

        checkAdress  = (CheckBox) rootView.findViewById(R.id.check_adress);
        checkAdress.setText(adress);

        return rootView;
    }

    private JsonObjectRequest createRequest(String url) {

        db.open();
        User u = null;
        u = db.getUserConnected();
        db.close();
        url = url + "/" + u.getToken();
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        CookieStore cs = mHttpClient.getCookieStore();
                        BasicClientCookie c = (BasicClientCookie) getCookie(cs, "cookie");
                        if (c != null) {
                            setTvCookieText(c.getValue());
                        }
                        Log.d("SUCCESS LISTENER", response.toString());
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.e("========ADRESSE=======",response.toString());
                            ListMealCache.setAdress("ERREUR");
                            if(response.isNull("error")){
                                ListMealCache.setAdress(response.get("result").toString());
                                Log.e("========ADRESSE2======",ListMealCache.getAdress());
                                checkAdress.setText(ListMealCache.getAdress());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        return JOPR;

    }

    private void setTvCookieText(String str) {}

    public Cookie getCookie(CookieStore cs, String cookieName) {
        Cookie ret = null;
        List<Cookie> l = cs.getCookies();
        for (Cookie c : l) {
            if (c.getName().equals(cookieName)) {
                ret = c;
                break;
            }
        }
        return ret;
    }



}
