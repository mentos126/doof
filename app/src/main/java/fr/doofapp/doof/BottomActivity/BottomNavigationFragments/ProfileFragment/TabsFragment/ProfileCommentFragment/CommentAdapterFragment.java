package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.MealActivity.MealActivity;
import fr.doofapp.doof.ProfileActivity.ProfileActivity;
import fr.doofapp.doof.R;


public class CommentAdapterFragment extends RecyclerView.Adapter<CommentAdapterFragment.MyViewHolder> {

    private Context context;
    private List<Comment> commentList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public ImageView imgProfile, imgMeal;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description = (TextView) view.findViewById(R.id.description);
            imgMeal = (ImageView) view.findViewById(R.id.img_meal);
            imgProfile = (ImageView) view.findViewById(R.id.img_profile);
        }
    }


    public CommentAdapterFragment(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        this.context =context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_comment_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment coment = commentList.get(position);
        Comment com = commentList.get(position);
        holder.name.setText(com.getNameUser());
        holder.description.setText(com.getDescriptif());
        Glide.with(context)
                .load(coment.getPhoto())
                .into(holder.imgMeal);
        holder.imgMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MealActivity.class);
                Meal m = new Meal("",0,"",coment.getLinkMeal(),"","","",new LatLng(0,0));
                CommandCache.setMeal(m);
                context.startActivity(intent);
            }
        });
        Glide.with(context)
                .load(coment.getPhotoUser())
                .into(holder.imgProfile);
        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileActivity.class);
                Meal m = new Meal("",0,"",coment.getLinkUser(),"","","",new LatLng(0,0));
                CommandCache.setMeal(m);
                intent.putExtra("Link",coment.getLinkUser());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}
