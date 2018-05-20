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
    Button validate, previous, modify;

    String date, time, adress, mainTitle, mainDescription;
    int mainNbPortion, mainPrice;
    Boolean contain;
    Bitmap mainPhoto;

    private AbstractHttpClient mHttpClient;
    private RequestQueue mQueue;
    private UserDAO db;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    ProgressDialog loading = null;

    String upLoadServerUri = URLProject.getInstance().getUPLOAD();

    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    final String uploadFileName = "temp.jpg";

    /*******c  NEW TEST IMG  c*****/
    byte [] multipartBody;
    String mimeType = "image/jpeg";

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


    /*********************TEST NE MARCHE PAS****************************/

    private void testBitmap() {
        String url = URLProject.getInstance().getUPLOAD();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        try {
                            Log.e("=========RESULT=====",s);
                            JSONObject jsonObject = new JSONObject(s);
                            /*int responseCode = Integer.parseInt(jsonObject.getString("responseCode"));
                            String response = jsonObject.getString("response");
                            if (responseCode == 1) {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Error: " + response, Toast.LENGTH_LONG).show();
                            }*/
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "Failed to upload.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        VolleyLog.e("Error: ", volleyError.getMessage());
                        Toast.makeText(getActivity(), "ERREUR DE CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = toBase64(mainPhoto);
                Map<String,String> params = new Hashtable<>();
                params.put("photo", image);
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }


    public int uploadFile() {

        File f = null;
        try {
            f = new File(getContext().getCacheDir(), "temp.jpg");
            f.createNewFile();

            //Convert bitmap to byte array
            Bitmap bitmap = mainPhoto;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream("temp.jpg");
            mainPhoto.compress(Bitmap.CompressFormat.PNG, 0, fos);

            fos.write(bitmapdata);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        //write the bytes in file

        String fileName = "temp.jpg";

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = f;

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    +uploadFilePath + "" + uploadFileName);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.e("======RUN======","Source File not exist :"
                            +uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
                //conn.setRequestProperty("bill", sourceFile.getAbsolutePath());

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            Log.e("=======RUN2=====","File Upload Complete.");
                            Toast.makeText(getActivity(), "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.e("=====RUN3=====",  "MalformedURLException Exception : check script url.");
                        Toast.makeText(getActivity(), "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.e("=======RUN4=====", "Got Exception : see logcat ");
                        Toast.makeText(getActivity(), "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("UpfiletoserverException", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

    private void uploadImage(Bitmap thumbnailBitmap) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (thumbnailBitmap != null) {
                thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            }

            byte[] fileByteArray = byteArrayOutputStream.toByteArray();

            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream2);
            try {
                // the first file
                buildPart(dataOutputStream, fileByteArray, "picture.jpg");
                // send multipart form data necesssary after file data
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // pass to multipart body
                multipartBody = byteArrayOutputStream2.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }


            String url = URLProject.getInstance().getUPLOAD() ;
            MultipartRequest multipartRequest = new MultipartRequest(url, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    Log.e("========RESPONSE======",response.toString());
                    Toast.makeText(getActivity(), "REFRESH IMG", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(getActivity(), "ERROR TRY AGAIN", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });

            AppSingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileName + "\"" + lineEnd);

        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    /*******************FIN TEST NE MARCHE PAS***************************/

}