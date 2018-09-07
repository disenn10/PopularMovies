package com.example.disen.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.disen.popularmovies.data.MovieContract;
import com.example.disen.popularmovies.databinding.FragmentFavoritesBinding;

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    CursorAdapter cursorAdapter;
    FragmentFavoritesBinding favoritesBinding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoritesBinding = DataBindingUtil.setContentView(this,R.layout.fragment_favorites);
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setDuration(3000);
        slide.addTarget(R.id.favorite_view);
        getWindow().setEnterTransition(slide);
        cursorAdapter = new CursorAdapter(this,null);
        favoritesBinding.favoriteView.setEmptyView(favoritesBinding.emptyview);
        getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = new String[] {MovieContract.MovieEntry.ColumnImage, MovieContract.MovieEntry.ColumnID,
                MovieContract.MovieEntry.ColumnTitle, MovieContract.MovieEntry.ColumnRate,
                MovieContract.MovieEntry.ColumnReleaseDate, MovieContract.MovieEntry.ColumnOverview};
        return new CursorLoader(this,MovieContract.Content_Uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!= null){
            updateUI(data);
        }
    }
    private void updateUI(final Cursor data) {
        cursorAdapter.swapCursor(data);
        favoritesBinding.favoriteView.setAdapter(cursorAdapter);
        favoritesBinding.favoriteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                Cursor c_adapter = (Cursor) cursorAdapter.getItem(i);
                String title = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnTitle));
                String id = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnID));
                String image = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnImage));
                String overview = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnOverview));
                String rate = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnRate));
                String release_date = c_adapter.getString(c_adapter.getColumnIndex(MovieContract.MovieEntry.ColumnReleaseDate));
                String from_class = "favorite";
                intent.putExtra("title",title);
                intent.putExtra("from_class",from_class);
                intent.putExtra("image",image);
                intent.putExtra("overview",overview);
                intent.putExtra("rate",rate);
                intent.putExtra("id",id);
                intent.putExtra("releaseDate",release_date);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
