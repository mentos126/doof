package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.ProfileFragment.TabsFragment.ProfileMealFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.CommandActivity.CommandMealActivity;
import fr.doofapp.doof.MealActivity.MealActivity;
import fr.doofapp.doof.R;

public class MealAdapterFragment extends RecyclerView.Adapter<MealAdapterFragment.MyViewHolder> {
    private Context context;
    private List<Meal> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;
        public ImageView photo;
        public CardView cardView;
        public RelativeLayout rel;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            photo = (ImageView) view.findViewById(R.id.photo);
            cardView  = (CardView) view.findViewById(R.id.card_view);
            rel = (RelativeLayout) view.findViewById(R.id.rel);
        }
    }


    public MealAdapterFragment(Context context, List<Meal> mealList) {
        this.mealList = mealList;
        this.context =context;
    }

    @Override
    public MealAdapterFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile_meal_adapter, parent, false);

        return new MealAdapterFragment.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MealAdapterFragment.MyViewHolder holder, int position) {
        final Meal mTemp = mealList.get(position);
        Meal m = mealList.get(position);
        holder.name.setText(m.getName());
        String s = m.getPrice()+" ticket";
        holder.price.setText(s);
        Glide.with(context)
                .load(m.getPhoto())
                .into(holder.photo);
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("==========CLICK========","CLICK");
                final Intent myIntent = new Intent(view.getContext(), MealActivity.class);
                //myIntent.putExtra("Meal", (Serializable) mTemp);
                CommandCache.setMeal(mTemp);
                context.startActivity(myIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
