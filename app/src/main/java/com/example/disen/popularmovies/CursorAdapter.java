package com.example.disen.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.disen.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by disen on 11/12/2017.
 */

public class CursorAdapter extends android.support.v4.widget.CursorAdapter {
    public CursorAdapter(Context context, Cursor c) {
        super(context, c);
    }
    public static class viewHolder{
        ImageView imageView;
        public viewHolder(View view){
            imageView = (ImageView)view.findViewById(R.id.cover_favorite);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_favorites, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder favoriteViewH = new viewHolder(view);

        int columnImage = cursor.getColumnIndex(MovieContract.MovieEntry.ColumnImage);
        favoriteViewH.imageView.setImageResource(columnImage);
            Picasso.with(context).load(cursor.getString(columnImage)).into(favoriteViewH.imageView);
    }
}
