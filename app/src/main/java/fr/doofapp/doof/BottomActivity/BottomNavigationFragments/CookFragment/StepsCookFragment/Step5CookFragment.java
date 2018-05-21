package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CookFragment.StepsCookFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.MultipartRequest;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.ListMealCache;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.LoginActivity.RegisterActivity;
import fr.doofapp.doof.R;

public class Step5CookFragment extends Fragment {

    View rootView;
    ImageView prompt_meal_photo;
    TextView prompt_price, prompt_name_meal, prompt_date, prompt_time,
            prompt_adress, prompt_contenant;
    Button validate, modify;

    String date, time, adress, mainTitle, mainDescription;
    int mainNbPortion, mainPrice;
    Boolean contain;
    Bitmap mainPhoto;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;

    ProgressDialog dialog ;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cook_step5, container, false);

        db = new UserDAO(getActivity());
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(getActivity(), new HttpClientStack(mHttpClient));

        date = ListMealCache.getDate();
        time = ListMealCache.getTime();
        adress = ListMealCache.getAdress();
        contain = ListMealCache.getIsContain();

        mainTitle = ListMealCache.getTitles().get(0);
        mainDescription = ListMealCache.getDescriptions().get(0);
        mainPhoto = ListMealCache.getPhotos().get(0);
        mainNbPortion =  ListMealCache.getNbPortions().get(0);
        mainPrice = ListMealCache.getPrices().get(0);

        Log.e("========PRICE=======", mainPrice+"");
        Log.e("========NBPORT=======", mainNbPortion+"");

        prompt_adress = (TextView) rootView.findViewById(R.id.prompt_adress);
        prompt_adress.setText(adress);
        prompt_date = (TextView) rootView.findViewById(R.id.prompt_date);
        prompt_date.setText(date);
        prompt_price = (TextView) rootView.findViewById(R.id.prompt_price);
        String s=""+mainPrice;
        prompt_price.setText(s);
        prompt_name_meal = (TextView) rootView.findViewById(R.id.prompt_name_meal);
        prompt_name_meal.setText(mainTitle);
        prompt_time = (TextView) rootView.findViewById(R.id.prompt_time);
        prompt_time.setText(time);
        prompt_contenant = (TextView) rootView.findViewById(R.id.prompt_contenant);
        s="";
        if(contain){
            s= "OUI";
        }else{
            s= "NON";
        }
        prompt_contenant.setText(s);

        prompt_meal_photo =(ImageView) rootView.findViewById(R.id.prompt_meal_photo);
        prompt_meal_photo.setImageBitmap(mainPhoto);

        modify = (Button) rootView.findViewById(R.id.modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Step3CookFragment step3CookFragment = new Step3CookFragment();
                fragmentTransaction.replace(R.id.frame_cook_container, step3CookFragment);
                fragmentTransaction.commit();
            }
        });

        validate = (Button) rootView.findViewById(R.id.validate);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              dialog = ProgressDialog.show(getActivity(), "", "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Log.e("==========BEGIN========","uploading started.....");
                                JSONObject jsonBodyObj = new JSONObject();
                                try{

                                    jsonBodyObj.put("adresse", adress);
                                    jsonBodyObj.put("date", date);
                                    jsonBodyObj.put("photo", toBase64(mainPhoto));
                                    jsonBodyObj.put("creneau", time);
                                    jsonBodyObj.put("titre",mainTitle);
                                    jsonBodyObj.put("description",mainDescription);
                                    jsonBodyObj.put("prix",mainPrice);
                                    jsonBodyObj.put("nbPart",mainNbPortion);
                                    jsonBodyObj.put("contenant", contain);
                                    jsonBodyObj.put("allergenes", null);

                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                Log.e("========JSONOBJECT=====",jsonBodyObj.toString());
                                String URL = URLProject.getInstance().getCREATE_MEAL();
                                db.open();
                                User u = null;
                                u = db.getUserConnected();
                                db.close();
                                URL = URL + "/" + u.getToken();
                                mQueue.add(createRequest(URL, jsonBodyObj));

                            }
                        });

                    }
                }).start();


            }
        });

        return rootView;
    }

    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
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

                        Intent myIntent = new Intent(getActivity(), BottomActivity.class);
                        getActivity().startActivity(myIntent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
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


}