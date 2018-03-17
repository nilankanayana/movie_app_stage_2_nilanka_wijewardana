package com.inusak.android.moviesapp.task;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;

import com.inusak.android.moviesapp.MainActivity;
import com.inusak.android.moviesapp.MovieDataAdapter;
import com.inusak.android.moviesapp.data.MovieContract;
import com.inusak.android.moviesapp.data.MovieDataProvider;

import static com.inusak.android.moviesapp.data.MovieContract.*;

/**
 * This class handles background data loading for movie data from local sqlite db.
 * Created by Nilanka on 3/4/2018.
 */

public class FetchFavouriteMovieDataLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private MainActivity mainActivity;

    private int position = RecyclerView.NO_POSITION;

    public static final int ID_MOVIE_DATA_LOADER = 2000;

    public FetchFavouriteMovieDataLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_MOVIE_DATA_LOADER:
                final Uri uri = FavouriteEntry.CONTENT_URI;
                final String sortOrder = FavouriteEntry._ID + " DESC";

                return new CursorLoader(mainActivity, uri, MovieContract.FavouriteEntry.DEFAULT_MOVIE_PROJECTION, null, null, sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // mainActivity.setRefreshing(false);
        mainActivity.setMovieData(data);

        if (position == RecyclerView.NO_POSITION) {
            position = 0;
        }

        mainActivity.getRecylerView().smoothScrollToPosition(position);

        if (data.getCount() != 0) {
            mainActivity.setMovieDataViewsVisible();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainActivity.setMovieData(null);
    }
}
