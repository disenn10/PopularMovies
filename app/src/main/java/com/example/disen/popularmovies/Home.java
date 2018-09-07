package com.example.disen.popularmovies;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.*;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disen.popularmovies.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.zip.Inflater;

import static android.content.ContentValues.TAG;

public class Home extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movies>>, MoviesCustomAdapter.onClickItemListener {
    String FINITE_URL = null;
    MoviesCustomAdapter customAdapter;
    String detailed_url;
    String base_url;
    ArrayList<Movies> movies_data;
    int loaderID;
    LoaderManager loaderManager;
    FragmentHomeBinding homeBinding;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment by initializing the datbind
        homeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        setHasOptionsMenu(true);
        //set up recycler view
        homeBinding.homeRecycl.setAdapter(null);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        homeBinding.homeRecycl.setLayoutManager(layoutManager);
        homeBinding.homeRecycl.setHasFixedSize(true);
        //base url and detail url
        base_url = getString(R.string.base_url);
        detailed_url = getString(R.string.detailed_url);
        loaderID = 0;

        FINITE_URL = constructUrl(detailed_url);
        //refresh loader once swiped top to bottom
        homeBinding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(checkCo()){
                    loaderManager.restartLoader(loaderID,null,Home.this);
                    homeBinding.homeRecycl.setVisibility(View.VISIBLE);
                }else{
                    setNoInternet();
                }
            }
        });
        //fetch request if there is an internet connection
        if(checkCo()){
            loaderManager = getLoaderManager();
            //on device rotation reload manager with correct url
            if(savedInstanceState != null){
                detailed_url = savedInstanceState.getString("url");
                reloadLoader(detailed_url);
            }
            else{
                loaderManager.initLoader(1,null,this);
            }

        }
        //otherwise let the user know
        else{
            setNoInternet();
        }
        return homeBinding.getRoot();
    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {
                return new MoviesLoader(getContext(),FINITE_URL);
    }
    //fill out recycler views with fetched data
    public void UpdateUI(ArrayList<Movies>data){
        homeBinding.refresh.setRefreshing(false);
        customAdapter = new MoviesCustomAdapter(getContext(),data, (MoviesCustomAdapter.onClickItemListener)this);
        homeBinding.progressHome.setVisibility(View.GONE);
        homeBinding.homeRecycl.setAdapter(customAdapter);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        if(data != null){
            movies_data = data;
            UpdateUI(data);
        }
    }
    //Display the need of an internet connection
    public void setNoInternet(){
        homeBinding.homeRecycl.setVisibility(View.GONE);
        homeBinding.noInternet.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), getString(R.string.internet), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        switch (item.getItemId()){
            //if there is internet reload the manager depending on the sorting args chosen by the user
            case R.id.sort_pop:
                if (checkCo()){
                    String popularity = getString(R.string.popularity);
                    reloadLoader(popularity);
                }
                else{
                    setNoInternet();
                }
                return true;
            case R.id.sort_vote:
                if(checkCo()){
                    String highest_rated = getString(R.string.highest);
                    reloadLoader(highest_rated);
                }
                else{
                    setNoInternet();
                }
                return true;
            case R.id.favs:
                Intent intent = new Intent(getContext(), FavoriteActivity.class);
                startActivity(intent,bundle);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //save the url on device rotation
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("url",detailed_url);
        super.onSaveInstanceState(outState);
    }

    public String constructUrl(String url){
        String finite_Url = null;
        StringBuilder combined_url = new StringBuilder();
        String sort_desc = null;
        sort_desc = url;
        finite_Url = String.valueOf(combined_url.append(base_url).append(sort_desc).append(Utils.api_key));
        return finite_Url;
    }
    public void reloadLoader(String url){
        detailed_url = url;
        FINITE_URL = constructUrl(detailed_url);
        Log.e(TAG, " "+FINITE_URL );
        loaderManager.restartLoader(loaderID,null,this);
    }
    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
        loader.reset();
    }

    public Boolean checkCo(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void itemClick(int itemclicked, View view) {
        //Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
        Bundle bundle1 = ActivityOptions.makeSceneTransitionAnimation(getActivity()
        ,view,view.getTransitionName()).toBundle();
        Intent intent = new Intent(getActivity(), MovieDetails.class);
        String title = movies_data.get(itemclicked).getTitle();
        String id = movies_data.get(itemclicked).getId();
        String image = movies_data.get(itemclicked).getImage();
        String overview = movies_data.get(itemclicked).getOverview();
        String rate = movies_data.get(itemclicked).getRate();
        String release_date = movies_data.get(itemclicked).getReleaseDate();

        String from_class = "home";
        intent.putExtra("title",title);
        intent.putExtra("from_class",from_class);
        intent.putExtra("image",image);
        intent.putExtra("overview",overview);
        intent.putExtra("rate",rate);
        intent.putExtra("id",id);
        intent.putExtra("releaseDate",release_date);
        startActivity(intent,bundle1);
    }
}
