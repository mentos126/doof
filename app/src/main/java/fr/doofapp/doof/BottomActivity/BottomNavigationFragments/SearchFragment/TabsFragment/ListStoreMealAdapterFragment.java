package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.SearchFragment.TabsFragment;

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
import fr.doofapp.doof.R;

public class ListStoreMealAdapterFragment extends RecyclerView.Adapter<ListStoreMealAdapterFragment.MyViewHolder> {
    private Context context;
    private List<Meal> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, date, nbPart;
        public ImageView photo;
        public CardView cardView;
        public RelativeLayout rel;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            photo = (ImageView) view.findViewById(R.id.photo);
            date = (TextView) view.findViewById(R.id.date);
            cardView  = (CardView) view.findViewById(R.id.card_view);
            rel  = (RelativeLayout) view.findViewById(R.id.rel);
            nbPart = (TextView) view.findViewById(R.id.nb_part);
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
        final Meal mTemp = mealList.get(position);
        Meal m = mealList.get(position);
        holder.name.setText(m.getName());
        String s = m.getPrice()+" ticket";
        holder.price.setText(s);
        holder.date.setText(m.getDate());
        s = "Nombre de part restante:"  + m.getNbPart();
        holder.nbPart.setText(s);
        Glide.with(context)
                .load(m.getPhoto())
                .into(holder.photo);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent myIntent = new Intent(view.getContext(), CommandMealActivity.class);
                CommandCache.setMeal(mTemp);
                context.startActivity(myIntent);
            }
        });
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent myIntent = new Intent(view.getContext(), CommandMealActivity.class);
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
