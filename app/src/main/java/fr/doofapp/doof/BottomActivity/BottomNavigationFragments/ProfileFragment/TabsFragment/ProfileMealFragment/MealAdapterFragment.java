package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileCommentFragment.CommentAdapterFragment;
import fr.doofapp.doof.ClassMetier.Comment;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;


public class MealAdapterFragment extends RecyclerView.Adapter<MealAdapterFragment.MyViewHolder> {
    private Context context;
    private List<Meal> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.description);
            photo = (ImageView) view.findViewById(R.id.img_meal);
        }
    }


    public MealAdapterFragment(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.context =context;
    }

    @Override
    public MealAdapterFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_comment_adapter, parent, false);

        return new MealAdapterFragment.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealAdapterFragment.MyViewHolder holder, int position) {
        final Meal p = mealList.get(position);
        Meal m = mealList.get(position);
        holder.name.setText(m.getName());
        holder.price.setText(m.getPrice()+"");
        Glide.with(context)
                .load(m.getPhoto())
                .into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
