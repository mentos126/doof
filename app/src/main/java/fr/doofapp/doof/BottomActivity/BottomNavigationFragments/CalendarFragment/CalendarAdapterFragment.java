package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CalendarFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.CommentActivity.CommentActivity;
import fr.doofapp.doof.CommandActivity.CommandMealActivity;
import fr.doofapp.doof.R;

public class CalendarAdapterFragment extends RecyclerView.Adapter<CalendarAdapterFragment.MyViewHolder> {

    private Context context;
    private List<Pair<Meal,Boolean>> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, date;
        public ImageView photo;
        public CardView cardView;
        public RelativeLayout rel;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            photo = (ImageView) view.findViewById(R.id.photo);
            date = (TextView) view.findViewById(R.id.date);
            cardView  = (CardView) view.findViewById(R.id.card_view);
            rel  = (RelativeLayout) view.findViewById(R.id.rel);
            button = (Button) view.findViewById(R.id.button);
        }
    }

    public CalendarAdapterFragment(Context context, List<Pair<Meal,Boolean>> mealList) {
        this.mealList = mealList;
        this.context =context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calendar_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Pair<Meal,Boolean> p = mealList.get(position);
        final Meal mTemp = p.first;
        final Boolean bTemp = p.second;
        Boolean b = p.second;
        Meal m = p.first;
        holder.name.setText(m.getName());
        String s = m.getPrice()+" ticket";
        holder.price.setText(s);
        holder.date.setText(m.getDate());
        Glide.with(context)
                .load(m.getPhoto())
                .into(holder.photo);
        if(b){
            holder.rel.setBackgroundColor(Color.BLUE);
            holder.button.setVisibility(View.INVISIBLE);
        }else{
            holder.button.setVisibility(View.VISIBLE);
            holder.rel.setBackgroundColor(Color.RED);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), CommentActivity.class);
                    myIntent.putExtra("Meal", (Serializable) mTemp);
                    context.startActivity(myIntent);
                }
            });
        }
        holder.rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("==========CLICK========","CLICK");
                final Intent myIntent = new Intent(view.getContext(), CommandMealActivity.class);
                myIntent.putExtra("Meal", (Serializable) mTemp);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
