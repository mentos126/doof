package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.doofapp.doof.App.AppSingleton;
import fr.doofapp.doof.App.URLProject;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.User;
import fr.doofapp.doof.R;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class ProfileCommentsListFragment extends Fragment {

    private List<Comment> comList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapterFragment mAdapter;

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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new CommentAdapterFragment(getContext(), comList);
        recyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareCommentData();


        return rootView;
    }

    public void prepareCommentData(){

        String URL = URLProject.getInstance().getPROFILE_COMMENTS();

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("LoginActivity", response.toString());
                        int countObject = response.length();
                        Comment com = null;
                        for(int i=0 ; i<countObject; i++){
                            try {
                                JSONObject jsonObject = null;
                                jsonObject = response.getJSONObject(i);
                                com = new Comment(
                                        jsonObject.get("descriptif").toString(),
                                        jsonObject.get("photo_meal").toString(),
                                        jsonObject.get("link_user").toString(),
                                        jsonObject.get("name_user").toString(),
                                        jsonObject.get("photo_user").toString(),
                                        parseDouble(jsonObject.get("note_accueil").toString()),
                                        parseDouble(jsonObject.get("note_proprete").toString()),
                                        parseDouble(jsonObject.get("note_cuisine").toString()),
                                        parseDouble(jsonObject.get("note_totale").toString())
                                );
                                comList.add(com);

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
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayReq, URL);

        mAdapter.notifyDataSetChanged();

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
