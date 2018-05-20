package fr.doofapp.doof.CommandActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.CreditActivity.CreditActivity;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;
import fr.doofapp.doof.TutorialChangeActivity.TutorialChangeActivity;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class FinaliseActivity extends AppCompatActivity {

    private TextView prompt_title_payment;
    private Button payment_modify;
    private CheckBox checkBox_not_tickets;
    private ImageView img_payment;
    private TextView prompt_payment;
    /**********************/
    private TextView prompt_title_not_payment;
    private Button button_payment;
    /**********************/
    private TextView prompt_system_payment;
    private TextView prompt_limit_payment;
    private CheckBox checkBox_tickets;
    private TextView prompt_sold;
    /**********************/
    private TextView prompt_integrate_system;
    private Button button_register_caution;
    private Button button_tutorial;
    /**********************/
    private TextView prompt_nbtikets;
    private Button validate;
    private Button credit_tiket;
    /*************************/
    private LinearLayout rel_use_payment;
    private LinearLayout rel_add_payment;
    /**********OR*************/
    private LinearLayout rel_use_tickets;
    private LinearLayout rel_add_system_payment;

    int nbTikets =0;
    int price = -1;
    String moyenPaiement="";
    Boolean isMoyenPayment;
    Boolean isSystemChange;
    List<Meal> meals = new ArrayList<>();
    List<Integer> prices = new ArrayList<>();
    List<String> allergens = new ArrayList<>();

    UserDAO db;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalise);

        db = new UserDAO(getApplicationContext());
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(FinaliseActivity.this, new HttpClientStack(mHttpClient));
        meals = CommandCache.getMeals();
        prices = CommandCache.getPrices();
        allergens = CommandCache.getAllergens();

        /***************************************/

        /*prompt_title_payment = (TextView) findViewById(R.id.prompt_title_payment);

        payment_modify = (Button) findViewById(R.id.payment_modify);
        payment_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionModifyPayment();
            }
        });

        checkBox_not_tickets = (CheckBox) findViewById(R.id.checkBox_not_tickets);
        img_payment = (ImageView) findViewById(R.id.img_payment);
        prompt_payment = (TextView) findViewById(R.id.prompt_payment);*/

        /***************************************/

        /*prompt_title_not_payment = (TextView) findViewById(R.id.prompt_title_not_payment);

        button_payment = (Button) findViewById(R.id.button_payment);
        button_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionPayment();
            }
        });*/

        /**************************************/

        prompt_system_payment = (TextView) findViewById(R.id.prompt_system_payment);
        prompt_limit_payment = (TextView) findViewById(R.id.prompt_limit_payment);
        checkBox_tickets = (CheckBox) findViewById(R.id.checkBox_tickets);
        prompt_sold = (TextView) findViewById(R.id.prompt_sold);

        /*****************************************/

        /*prompt_integrate_system = (TextView) findViewById(R.id.prompt_integrate_system);

        button_register_caution = (Button) findViewById(R.id.button_register_caution);
        button_register_caution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionRegisterCaution();
            }
        });*/

        button_tutorial = (Button) findViewById(R.id.button_tutorial);
        button_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionTutorial();
            }
        });

        /*****************************************/

        credit_tiket = (Button) findViewById(R.id.credit_tiket);
        credit_tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinaliseActivity.this, CreditActivity.class);
                startActivity(intent);
            }
        });

        prompt_nbtikets = (TextView) findViewById(R.id.prompt_nbtikets);
        price = 0;

        int countMeals = meals.size();
        for(int i = 0; i < countMeals; i++){
            price += (meals.get(i).getPrice() * prices.get(i));
        }
        String s = price+" tickets";
        prompt_nbtikets.setText(s);

        validate = (Button) findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionValidate();
            }
        });

        /******************************************/
        /*rel_use_payment = (LinearLayout) findViewById(R.id.rel_use_payment);
        rel_use_payment.setVisibility(View.INVISIBLE);
        rel_add_payment = (LinearLayout) findViewById(R.id.rel_add_payment);
        rel_add_payment.setVisibility(View.INVISIBLE);*/
        /********************************************/

        rel_use_tickets = (LinearLayout) findViewById(R.id.rel_use_tickets);
        rel_use_tickets.setVisibility(View.INVISIBLE);
        /*rel_add_system_payment = (LinearLayout) findViewById(R.id.rel_add_system_payment);
        rel_add_system_payment.setVisibility(View.INVISIBLE);*/
        /********************************************/

        getUserData();

    }

    protected void getUserData(){
        moyenPaiement = "";
        isMoyenPayment = true;
        isSystemChange = true;

        db.open();
        User u = null;
        u = db.getUserConnected();
        db.close();
        String URL = URLProject.getInstance().getGET_NB_TIKET()+"/"+u.getToken();
        dialog = ProgressDialog.show(this, "", "", true);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("FINALISEACTIVITY", response.toString());
                        dialog.dismiss();
                        try {

                            if(!response.isNull("error")){
                                Toast.makeText(FinaliseActivity.this, "ERREUR SERVEUR",Toast.LENGTH_LONG).show();
                            }else {
                                //nbTikets = 48;
                                nbTikets = parseInt(response.get("result").toString());
                                moyenPaiement = "CB";
                                isMoyenPayment = true;
                                isSystemChange = true;

                                String s;
                                /*if(isMoyenPayment){
                                    rel_use_payment.setVisibility(View.VISIBLE);
                                    s = moyenPaiement + "1 ticket = 1 €";
                                    prompt_payment.setText(s);
                                }else{
                                    rel_add_payment.setVisibility(View.VISIBLE);
                                }*/

                                if(isSystemChange){
                                    rel_use_tickets.setVisibility(View.VISIBLE);
                                    s = getResources().getString(R.string.prompt_sold) +nbTikets;
                                    prompt_sold.setText(s);
                                }/*else{
                                    rel_add_system_payment.setVisibility(View.VISIBLE);
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "ERREUR IMPOSSIBLE", Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

    }

    protected void onActionValidate(){
       /* if(checkBox_not_tickets.isChecked() && checkBox_tickets.isChecked()){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.one_choice),Toast.LENGTH_LONG).show();
        }else if(!checkBox_not_tickets.isChecked() && !checkBox_tickets.isChecked()){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_choice),Toast.LENGTH_LONG).show();
        }else */if(checkBox_tickets.isChecked()){
            if(nbTikets >= price){
                db.open();
                User u = null;
                u = db.getUserConnected();
                db.close();
                String URL = URLProject.getInstance().getCOMMAND_ORDER()+"/"+u.getToken();
                dialog = ProgressDialog.show(this, "", "", true);

                JSONObject jsonBodyObj = new JSONObject();
                JSONArray ordersJson = new JSONArray();
                JSONObject rootJson = new JSONObject();
                try{
                    jsonBodyObj.put("_id", CommandCache.getIdOrder());
                    jsonBodyObj.put("nbPart", CommandCache.getNbPart());

                    ordersJson.put(0, jsonBodyObj);

                    rootJson.put("orders", (Object) ordersJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("=========JSON=======",rootJson.toString());

                mQueue.add(createRequest(URL , rootJson));

            }else{
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_enouth_tiket),Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, R.string.prompt_error_impossible,Toast.LENGTH_SHORT).show();

        }

    }

    protected void onActionModifyPayment(){
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTER",Toast.LENGTH_LONG).show();
    }

    protected void onActionRegisterCaution(){
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTER",Toast.LENGTH_LONG).show();
    }

    protected void onActionTutorial(){
        Intent myIntent = new Intent(FinaliseActivity.this, TutorialChangeActivity.class);
        startActivity(myIntent);
    }

    protected void onActionPayment(){
        Toast.makeText(getApplicationContext(), "PAS ENCORE IMPLEMENTER",Toast.LENGTH_LONG).show();
    }


    private JsonObjectRequest createRequest(String URL, JSONObject jsonObject)  {
        JSONObject jsonBodyObj =  jsonObject;
        final String requestBody = jsonBodyObj.toString();
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                URL, jsonBodyObj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                CookieStore cs = mHttpClient.getCookieStore();
                BasicClientCookie c = (BasicClientCookie) getCookie(cs, "cookie");
                if (c != null) {
                    setTvCookieText(c.getValue());
                }
                cs.addCookie(c);
                Log.d("SUCCESS LISTENER", response.toString());
                dialog.dismiss();
                try {
                    VolleyLog.v("Response:%n %s", response.toString(4));

                    if(response.isNull("error")){
                        Intent intent = new Intent(FinaliseActivity.this, FinalisedActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.prompt_error_impossible, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"ERREUR IMPOSSIBLE",Toast.LENGTH_LONG).show();
                VolleyLog.e("Error: ", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
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
