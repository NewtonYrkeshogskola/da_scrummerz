package se.newton.scrummerz.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import se.newton.scrummerz.R;
import se.newton.scrummerz.model.Courses;


public class CoursesAdapter extends RecyclerView.Adapter<Item> {

    private Context context;
    private ArrayList<Courses> coursesList;
    String[] items = {"hej", "youo", "nisse", "hult"};


    public CoursesAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false);
        return new Item(card, parent.getContext());
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        Courses courses = coursesList.get(position);


    }


    @Override
    public int getItemCount() {
        return items.length;
    }
}

class Item extends RecyclerView.ViewHolder{
    TextView textView;
    Item(View itemView, Context context) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.myCoursesTextView);
    }
}