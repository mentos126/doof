package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

import android.content.Context;
import android.content.Intent;
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

import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.R;

public class ListStoreMealAdapterFragment extends RecyclerView.Adapter<ListStoreMealAdapterFragment.MyViewHolder> {
    private Context context;
    private List<Meal> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, date;
        public ImageView photo;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            photo = (ImageView) view.findViewById(R.id.photo);
            date = (TextView) view.findViewById(R.id.date);
        }
    }


    public ListStoreMealAdapterFragment(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.context =context;
    }

    @Override
    public ListStoreMealAdapterFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_store_meal_adapter, parent, false);

        return new ListStoreMealAdapterFragment.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListStoreMealAdapterFragment.MyViewHolder holder, int position) {
        final Meal p = mealList.get(position);
        Meal m = mealList.get(position);
        holder.name.setText(m.getName());
        holder.price.setText(m.getPrice()+" ticket");
        holder.date.setText(m.getDate());
        Glide.with(context)
                .load(m.getPhoto())
                .into(holder.photo);

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
