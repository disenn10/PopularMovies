package com.example.disen.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by disen on 10/19/2017.
 */

public class MoviesLoader extends AsyncTaskLoader {
    String base_url;
    String detailed_url;
    public MoviesLoader(Context context, String baseUrl) {
        super(context);
        this.base_url = baseUrl;
    }

    @Override
    public Object loadInBackground() {
        ArrayList<Movies> moviesArrayList = Utils.FetchData(base_url);
        return moviesArrayList;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
