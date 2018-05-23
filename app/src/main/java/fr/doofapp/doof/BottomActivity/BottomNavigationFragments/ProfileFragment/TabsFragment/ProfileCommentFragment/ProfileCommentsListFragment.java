package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.DataBase.UserDAO;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class ProfileCommentsListFragment extends Fragment {

    private List<Comment> comList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapterFragment mAdapter;

    Dialog dialog;
    UserDAO db;

    public ProfileCommentsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_comments_list, container, false);

        db = new UserDAO(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CommentAdapterFragment(getContext(), comList);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //prepareCommentData();

        return rootView;
    }

    public void prepareCommentData(){

        db.open();
        User u = db.getUserConnected();
        db.close();

        String URL = URLProject.getInstance().getMY_PROFILE_COMMENT()+"/"+u.getToken();
        Log.e("=========URL=========",URL);

        dialog = ProgressDialog.show(getActivity(), "", "", true);

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=========com========", response.toString());
                        if(response.isNull("error")){
                            try {
                                JSONArray comments = response.getJSONArray("result");
                                int comCount = comments.length();
                                Comment com;
                                for(int i=0; i<comCount; i++){
                                    JSONObject jsonObject = comments.getJSONObject(i);
                                    JSONObject note = jsonObject.getJSONObject("note");
                                    JSONObject meal = jsonObject.getJSONObject("repas");
                                    JSONObject user = jsonObject.getJSONObject("acheteur_meta");
                                    com = new Comment(
                                            jsonObject.get("commentaire").toString(),
                                            meal.get("titre").toString(),
                                            jsonObject.get("photo").toString(),
                                            meal.get("_id").toString(),
                                            user.get("_id").toString(),
                                            user.get("prenom").toString(),
                                            user.get("photo").toString(),
                                            parseDouble(note.get("accueil").toString()),
                                            parseDouble(note.get("proprete").toString()),
                                            parseDouble(note.get("cuisine").toString()),
                                            parseDouble(jsonObject.get("total_note").toString())
                                    );

                                    comList.add(com);
                                }
                                mAdapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getActivity(),getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.prompt_error_impossible), Toast.LENGTH_SHORT).show();
                        VolleyLog.e("=========MEALS========", "Error: " + error.getMessage());
                    }
                });

        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectReq, URL);

}


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
