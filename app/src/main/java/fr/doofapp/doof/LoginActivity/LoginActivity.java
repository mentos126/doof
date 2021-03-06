package fr.doofapp.doof.LoginActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;
import static java.lang.Integer.parseInt;

//////////////////////////////////////////////////////////
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>
{
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private UserDAO db;

    // Test version cookie
    private RequestQueue mQueue;
    private AbstractHttpClient mHttpClient;
    ///////////////////////////////////////



    public LoginActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = new UserDAO(this);
        db.open();
        User u = null;
        u = db.getUserConnected();
        db.close();
        if(u != null && u.getConnected() == 1){
            Intent myIntent = new Intent(LoginActivity.this, BottomActivity.class);
            startActivity(myIntent);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ///////////////////////////////////////
        mHttpClient = new DefaultHttpClient();
        mQueue = Volley.newRequestQueue(LoginActivity.this, new HttpClientStack(mHttpClient));
        ///////////////////////////////////////

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button buttonResetPassword = (Button) findViewById(R.id.lost_password);
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(myIntent);
            }
        });

        Button buttonRegister = (Button) findViewById(R.id.register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        Button buttonIgnore = (Button) findViewById(R.id.ignore);
        buttonIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, BottomActivity.class);
                startActivity(myIntent);
            }
        });

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }



    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;



        ///////////////////////////////////

        private JsonObjectRequest createRequest(String URL)  {

            MessageDigest digest = null;
            byte[] hash = new byte[0];
            String resPass = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
                hash = digest.digest(mPassword.getBytes("UTF-8"));
                resPass = convertByteArrayToHexString(hash);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //Log.d("LoginActivity", resPass);

            JSONObject jsonBodyObj = new JSONObject();
            try{
                jsonBodyObj.put("id", mEmail);
                jsonBodyObj.put("pwd", resPass);
            }catch (JSONException e){
                e.printStackTrace();
            }
            final String requestBody = jsonBodyObj.toString();

            JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST,
                    URL, jsonBodyObj, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {

                    CookieStore cs = mHttpClient.getCookieStore();
                    BasicClientCookie c = (BasicClientCookie) getCookie(cs, "cookie");
                    cs.addCookie(c);

                    Log.d("SUCCESS LISTENER", response.toString());
                    try {
                        VolleyLog.v("Response:%n %s", response.toString(4));

                        User u = null;
                        u = new User(mEmail,
                                mPassword,
                                response.get("token").toString(),
                                1,
                                1);

                        if(response.isNull("error")){
                            db.open();
                            db.addUser(u);
                            db.close();
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
        // end
        //////////////////////////////////


        private boolean isConnected() {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        private String convertByteArrayToHexString(byte[] arrayBytes) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < arrayBytes.length; i++) {
                stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return stringBuffer.toString();
        }

        public void volleyJsonArrayRequest() {

            String URL = URLProject.getInstance().getLOGIN();
            mQueue.add(createRequest(URL));


        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(!isConnected()){
                return false;
            }

            volleyJsonArrayRequest();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            db.open();
            User u = null;
            u = db.getUserConnected();
            db.close();
            System.out.println(mEmail);
            return (u != null && u.getConnected() ==1);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, BottomActivity.class);
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
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

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

   /*private Response.Listener<String> createMyReqSuccessListener() {
            return new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    CookieStore cs = mHttpClient.getCookieStore();
                    BasicClientCookie c = (BasicClientCookie) getCookie(cs, "cookie");
                    if (c != null) {
                        setTvCookieText(c.getValue());
                    }
                    Log.d("SUCCESS LISTENER",response);
                }
            };
        }*/

/*JsonArrayRequest jsonArrayReq = new JsonArrayRequest(REQUEST_TAG,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("LoginActivity", response.toString());
                            int countObject = response.length();
                            System.out.println("444444444444444"+countObject);
                            for(int i=0 ; i<countObject; i++){
                                try {
                                    JSONObject jsonObject = null;
                                    jsonObject = response.getJSONObject(i);
                                    System.out.println("jsonObject " + i + ": " + jsonObject.get("email"));
                                    db.open();
                                    User u = new User(jsonObject.get("email").toString(),
                                            jsonObject.get("password").toString(),
                                            parseInt(jsonObject.get("role").toString()),
                                            1);
                                    db.addUser(u);
                                    db.close();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("LoginActivity", "Error: " + error.getMessage());
                        }
                    });
            // Adding JsonObject request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);*/

//            try {
//                MessageDigest digest = MessageDigest.getInstance("SHA-256");
//                byte[] hash = digest.digest(mPassword.getBytes("UTF-8"));
//                String resPassword = convertByteArrayToHexString(hash);
//                JSONObject jsonBody = new JSONObject();
//                jsonBody.put("email", mEmail);
//                jsonBody.put("password", resPassword);
//                final String requestBody = jsonBody.toString();

//{
                    /*@Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }*/

//}

//            }catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }catch (JSONException e) {
//                e.printStackTrace();
//            }

/*okieStore cs = mHttpClient.getCookieStore();
            BasicClientCookie c = (BasicClientCookie) getCookie(cs, "my_cookie");
            cs.addCookie(c);
            mQueue.add(createRequest(URL));*/

            /*final JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e("====LoginActivity===", response.toString());
                            try {
                                //(response.isNull("error"))
                                //
                                //sponse = (JSONObject) response.get("result");
                                User u = null;
                                u = new User(response.get("email").toString(),
                                        response.get("password").toString(),
                                        parseInt(response.get("role").toString()),
                                        1);
                                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                                byte[] hash = digest.digest(mPassword.getBytes("UTF-8"));
                                String resPass = convertByteArrayToHexString(hash);
                                Log.d("LoginActivity", resPass);
                                if (u != null && u.getPassword().equals(resPass) && u.getUserId().equals(mEmail)) {
                                    db.open();
                                    db.addUser(u);
                                    db.close();
                                }
                                //ast.makeText(LoginActivity.this, "ERREUR EST NULL", Toast.LENGTH_LONG).show();
                                //else {
                                //  Toast.makeText(LoginActivity.this, "ERREUR EST NULL", Toast.LENGTH_LONG).show();
                                //
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                        }
                    });

            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);
            */