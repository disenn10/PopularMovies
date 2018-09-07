package com.example.disen.popularmovies;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disen.popularmovies.data.MovieContract;
import com.example.disen.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movies>> {
    int rate_angle;
    String title ;
    String release_date ;
    String overview;
    String cover;
    String rate;
    String id;
    String youtubeKey;
    ActionBar actionBar;
    String request;
    String reviews_request;
    String from_class;
    ActivityMovieDetailsBinding detailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the binderview
        detailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_movie_details);
        //once the trailer button is clicked open implicit intent
        detailsBinding.trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String video_request =getString(R.string.base_youtube)+ youtubeKey;
                Uri uri = Uri.parse(video_request);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        Slide slide = null;
        Slide slidex = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            slidex = new Slide(Gravity.BOTTOM);
            slidex.addTarget(R.id.overview);
            slidex.setInterpolator(
                    AnimationUtils.loadInterpolator(this,android.R.interpolator.linear_out_slow_in)
            );
            slidex.setDuration(1000);
            getWindow().setEnterTransition(slidex);
        }
        //initialize and set up recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        detailsBinding.reviewRecycl.setLayoutManager(layoutManager);
        detailsBinding.reviewRecycl.setHasFixedSize(true);
        //get all the extra intents from origin class
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        release_date = getIntent().getStringExtra("releaseDate");
        overview = getIntent().getStringExtra("overview");
        cover = getIntent().getStringExtra("image");
        rate = getIntent().getStringExtra("rate");
        from_class = getIntent().getStringExtra("from_class");
        //direct us to onpreparemenu options to modify the menu
        invalidateOptionsMenu();
        //request for the trailer
        request = getString(R.string.request)+id+"/videos?api_key="+Utils.api_key;
        //request for the reviews
        reviews_request = getString(R.string.request)+id+"/reviews?api_key="+Utils.api_key;
        //initialize actionbar
        actionBar = getSupportActionBar();

        //if the origin class was home...fetch request for trailer and reviews
        if(from_class.equals("home")){
            getSupportLoaderManager().initLoader(0,null,this);
            getSupportLoaderManager().initLoader(1,null,this);
        }
        //if not hide the trailer and the reviews
        else {
            detailsBinding.trailer.setVisibility(View.GONE);
            detailsBinding.trailerTextview.setVisibility(View.GONE);
            detailsBinding.reviews.setVisibility(View.GONE);
            detailsBinding.reviewRecycl.setVisibility(View.GONE);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);

        //bind the views to their respective values
        Picasso.with(getApplicationContext()).load(cover).into(detailsBinding.cover);
        detailsBinding.title.setText(title);
        detailsBinding.releaseDate.append(release_date);
        detailsBinding.rate.setText(rate);
        detailsBinding.overview.setText(overview);
        setTitle(getString(R.string.details));


        //set up rating animation
        Double rate_double = Double.parseDouble(rate);
        int rate_int = rate_double.intValue();
        rate_angle = (rate_int*360)/10;
        change_circle_color(detailsBinding.circle, rate_int);
        CircleAngleAnimation animation = new CircleAngleAnimation(detailsBinding.circle, rate_angle);
        animation.setDuration(900);
        detailsBinding.circle.startAnimation(animation);
    }
    //Chance the color of the rating circle
    public void change_circle_color(Circle circle, int rate){
        if(rate<10) {
            circle.changeColor(Color.RED);
        }
        else {
            circle.changeColor(Color.GREEN);
        }

    }
    //Change to the appropriate menu icon depending of the origin activity
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem delete = menu.findItem(R.id.remove);
        MenuItem favorite = menu.findItem(R.id.favorite);
        if (from_class.equals("favorite")) {
            delete.setVisible(true);
            favorite.setVisible(false);
        } else {
            delete.setVisible(false);
            favorite.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.favorite, menu);
        return true;
    }

    //Display a toast and prevent the user to add any duplicates in the favorites/database
    public boolean Noduplicate(){
        String[] projection = new String[]{MovieContract.MovieEntry.ColumnTitle};
        int i = 0;
        String selection = MovieContract.MovieEntry.ColumnTitle + "=? ";
        String[] args = new String[]{title};
        Cursor cursor = getContentResolver().query(MovieContract.Content_Uri,projection,selection,args,null);
            while (cursor.moveToNext()){
                i+=1;
            }
            if(i <=0){
                return true;
            }
            else {
                return false;
            }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        final int startScroll = getResources().getDimensionPixelSize(R.dimen.scroll_up);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll);
        Animator animator = ObjectAnimator.ofInt(scrollView,"scrollY",startScroll).setDuration(500);
        animator.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                if(Noduplicate()){
                   insertData();
                }
                else{
                    Toast.makeText(getApplicationContext(), title +getString(R.string.duplicate), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.remove:
                unfavoriteConfirmation();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //put selected movie into database/favorites
    public void insertData(){
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.ColumnImage, cover);
        values.put(MovieContract.MovieEntry.ColumnRate, rate);
        values.put(MovieContract.MovieEntry.ColumnOverview, overview);
        values.put(MovieContract.MovieEntry.ColumnReleaseDate, release_date);
        values.put(MovieContract.MovieEntry.ColumnTitle, title);
        getContentResolver().insert(MovieContract.Content_Uri, values);
        Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 0:
                return new MoviesLoader(this, request);
            case 1:
                return new MoviesLoader(this, reviews_request);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        //get the youtube key
        if(data!= null && data.size() !=0 && loader.getId() == 0){
            youtubeKey = data.get(0).getKey();
        }
        else if (data != null && data.size() !=0 && loader.getId() == 1){
            updateUI(data);
        }
    }
    //fill out the recycler view with the reviews
    private void updateUI(ArrayList<Movies> data) {
        ReviewsAdapter review_adapter = new ReviewsAdapter(this,data);
        detailsBinding.reviewRecycl.setAdapter(review_adapter);
    }
    //remove a movie from database/favorite
    private void unfavoriteMovie(String id){
        String selection = MovieContract.MovieEntry.ColumnID + "=?";
        String[] args = new String[] {id};
        getContentResolver().delete(MovieContract.Content_Uri,selection,args);
        Toast.makeText(getApplicationContext(),getString(R.string.unfavorited),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }
    //display an alert message upon deletion
    private void unfavoriteConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove)+title+getString(R.string.from_favs));
        builder.setTitle(getString(R.string.confirmation));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                unfavoriteMovie(id);
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {

    }
}
