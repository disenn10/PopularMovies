package com.example.disen.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by disen on 10/19/2017.
 */

public class MoviesCustomAdapter extends RecyclerView.Adapter<MoviesCustomAdapter.MyViewHolder> {
    ArrayList<Movies> moviesArrayList;
    Context context;
    onClickItemListener clickItemListener;

    public MoviesCustomAdapter(Context context, ArrayList<Movies> arrayList, onClickItemListener listener){
        moviesArrayList = arrayList;
        this.context = context;
        clickItemListener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = View.inflate(parent.getContext(),R.layout.list_movies,parent);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_movies, parent,false);
        return new MyViewHolder(v);
    }
    public interface onClickItemListener{
        void itemClick(int itemclicked, View view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titleView.setText(moviesArrayList.get(position).getTitle());
        Picasso.with(context).load(moviesArrayList.get(position).getImage()).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView titleView;
        ImageView cover;
        public MyViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            cover = itemView.findViewById(R.id.cover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int posi = getAdapterPosition();
            view = cover;
            clickItemListener.itemClick(posi, view);
        }
    }
}
