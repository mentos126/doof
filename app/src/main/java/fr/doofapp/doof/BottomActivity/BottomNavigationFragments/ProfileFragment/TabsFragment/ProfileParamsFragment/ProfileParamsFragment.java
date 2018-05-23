package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileParamsFragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.BottomActivity.BottomActivity;
import fr.doofapp.doof.ClassMetier.Profile;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.CreditActivity.CreditActivity;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.R;
import fr.doofapp.doof.UpdateProfileActivity.UpdateProfileActivity;
import fr.doofapp.doof.UpdateProfileActivity.UpdateProfilePhotoActivity;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ProfileParamsFragment extends Fragment {

    private Button editPhoto;
    private Button editProfile;
    private Button deconexion;
    private Button credit_tikets;

    private UserDAO db;
    private Profile mProfile;

    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_params, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new UserDAO(getActivity());

        credit_tikets  = (Button) getView().findViewById(R.id.credit_tiket);
        credit_tikets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreditActivity.class);
                startActivity(intent);
            }
        });

        deconexion = (Button) getView().findViewById(R.id.deconexion);
        deconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();
                User u = null;
                u = db.getUserConnected();
                db.removeUser(u.getUserId());
                db.close();
                Intent intent  = new Intent(getActivity(), BottomActivity.class);
                startActivity(intent);
            }
        });

        editPhoto = (Button) getView().findViewById(R.id.EditPhotoButton);
        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), UpdateProfilePhotoActivity.class);
                myIntent.putExtra("Profile", (Serializable) mProfile);
                startActivity(myIntent);
            }
        });

        editProfile = (Button) getView().findViewById(R.id.EditProfileButton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mProfile.getName() != null){
                    Intent myIntent = new Intent(getActivity(), UpdateProfileActivity.class);
                    myIntent.putExtra("Profile", (Serializable) mProfile);
                    startActivity(myIntent);
                }
            }
        });

        //getProfileWeb();

    }


    public void getProfileWeb() {

        String URL = URLProject.getInstance().getMYp();
        dialog = ProgressDialog.show(getActivity(), "", "", true);
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity", response.toString());
                        dialog.dismiss();
                        try {

                            mProfile = new Profile(
                                    response.get("nom").toString(),
                                    response.get("prenom").toString(),
                                    response.get("birth").toString(),
                                    parseInt(response.get("age").toString()),
                                    response.get("photo").toString(),
                                    parseDouble(response.get("note").toString()),
                                    parseDouble(response.get("noteaccueil").toString()),
                                    parseDouble(response.get("noteproprete").toString()),
                                    parseDouble(response.get("notecuisine").toString()),
                                    "adress",
                                    "phone"
                            );


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
                        Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        VolleyLog.d("LoginEEROREActivity", "Error: " + error.getMessage());
                        editPhoto.setVisibility(View.GONE);
                        editProfile.setVisibility(View.GONE);
                    }
                });

        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
