package com.example.disen.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by disen on 11/13/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyreviewAdapter> {
    Context context;
    ArrayList<Movies> movies;
    @Override
    public ReviewsAdapter.MyreviewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reviews, parent,false);
        return new MyreviewAdapter(v);
    }
    public ReviewsAdapter(Context context, ArrayList<Movies> moviesArrayList){
        this.context = context;
        movies = moviesArrayList;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.MyreviewAdapter holder, int position) {
        holder.author_view.setText("Author: "+movies.get(position).getAuthor());
        holder.content_view.setText(movies.get(position).getTheContent());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyreviewAdapter extends RecyclerView.ViewHolder {
        TextView author_view;
        TextView content_view;
        public MyreviewAdapter(View itemView) {
            super(itemView);
            author_view =  itemView.findViewById(R.id.author);
            content_view = itemView.findViewById(R.id.content);
        }
    }
}
