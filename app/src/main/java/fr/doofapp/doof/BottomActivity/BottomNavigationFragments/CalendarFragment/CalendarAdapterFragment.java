package fr.doofapp.doof.BottomActivity.BottomNavigationFragments.CalendarFragment;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import fr.doofapp.doof.ClassMetier.CommandCache;
import fr.doofapp.doof.ClassMetier.Meal;
import fr.doofapp.doof.CommentActivity.CommentActivity;
import fr.doofapp.doof.CommandActivity.CommandMealActivity;
import fr.doofapp.doof.MealActivity.MealActivity;
import fr.doofapp.doof.R;

public class CalendarAdapterFragment extends RecyclerView.Adapter<CalendarAdapterFragment.MyViewHolder> {

    private Context context;
    private List<Pair<Meal,Integer>> mealList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, date, how_many, inter1, inter2, inter3, nothing;
        public ImageView photo, ic;
        public CardView cardView;
        public LinearLayout rel;
        public Button button;

        public MyViewHolder(View view) {
            super(view);
            ic = (ImageView) view.findViewById(R.id.ic_sold);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            photo = (ImageView) view.findViewById(R.id.photo);
            date = (TextView) view.findViewById(R.id.date);
            cardView  = (CardView) view.findViewById(R.id.card_view);
            rel  = (LinearLayout) view.findViewById(R.id.rel);
            button = (Button) view.findViewById(R.id.button);
            how_many  = (TextView) view.findViewById(R.id.how_many);
            inter1 = (TextView) view.findViewById(R.id.inter1);
            inter2 = (TextView) view.findViewById(R.id.inter2);
            inter3 = (TextView) view.findViewById(R.id.inter3);
            nothing = (TextView) view.findViewById(R.id.nothing);
        }
    }

    public CalendarAdapterFragment(Context context, List<Pair<Meal,Integer>> mealList) {
        this.mealList = mealList;
        this.context =context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calendar_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Pair<Meal,Integer> p = mealList.get(position);
        final Meal mTemp = p.first;
        Integer b = p.second;
        Meal m = p.first;

        holder.inter1.setVisibility(View.GONE);
        holder.inter2.setVisibility(View.GONE);
        holder.inter3.setVisibility(View.GONE);
        holder.rel.setVisibility(View.GONE);
        holder.nothing.setVisibility(View.GONE);
        Log.e("==========NAME========",m.getName());
        if(b == 4){
            holder.nothing.setVisibility(View.VISIBLE);
            holder.cardView.setElevation((float) 5.0);
        }else if(b == 1){
            holder.inter1.setVisibility(View.VISIBLE);
            holder.cardView.setElevation((float) 0.0);
            holder.cardView.setCardBackgroundColor(Color.argb(255, 250, 250, 250));
        }else if(b == 2){
            holder.inter2.setVisibility(View.VISIBLE);
            holder.cardView.setElevation((float) 0.0);
            holder.cardView.setCardBackgroundColor(Color.argb(255, 250, 250, 250));
        }else if(b == 3){
            holder.inter3.setVisibility(View.VISIBLE);
            holder.cardView.setElevation((float) 0.0);
            holder.cardView.setCardBackgroundColor(Color.argb(255, 250, 250, 250));
        }else if(b == 0){
            holder.rel.setVisibility(View.VISIBLE);
            holder.cardView.setElevation((float) 3.0);
            holder.cardView.setCardBackgroundColor(Color.argb(255, 255, 255, 255));
            holder.name.setText(m.getName());
            String s = "prix: "+m.getPrice()+" ticket";
            holder.price.setText(s);
            s = "date: "+m.getDate();
            holder.date.setText(s);
            s = "restant: " + (m.getNbPart() - m.getSold());
            holder.how_many.setText(s);
            Glide.with(context)
                    .load(m.getPhoto())
                    .into(holder.photo);
            holder.button.setVisibility(View.GONE);

            if(m.getSell()){
                holder.ic.setImageResource(R.drawable.ic_excel_box);
            }else{
                holder.ic.setImageResource(R.drawable.ic_drawing_box);
                if(!mTemp.getComment()){
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent myIntent = new Intent(view.getContext(), CommentActivity.class);
                            CommandCache.setMeal(mTemp);
                            context.startActivity(myIntent);
                        }
                    });
                }
            }
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("==========CLICK========","CLICK");
                    final Intent myIntent = new Intent(view.getContext(), MealActivity.class);
                    CommandCache.setMeal(mTemp);
                    context.startActivity(myIntent);
                }
            });
            holder.rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("==========CLICK========","CLICK");
                    final Intent myIntent = new Intent(view.getContext(), MealActivity.class);
                    CommandCache.setMeal(mTemp);
                    context.startActivity(myIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }
}
