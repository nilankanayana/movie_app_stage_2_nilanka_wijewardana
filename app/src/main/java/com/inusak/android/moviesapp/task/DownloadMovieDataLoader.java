package com.inusak.android.moviesapp.task;

/**
 * Created by Nilanka on 10/15/2017.
 */

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;

import com.inusak.android.moviesapp.MainActivity;
import com.inusak.android.moviesapp.MovieDataAdapter;
import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.utils.MovieDataJsonUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * This class handles background data loading for movie data for most popular & highest rated
 * scenarios from movie db api.
 */
public class DownloadMovieDataLoader implements LoaderManager.LoaderCallbacks<List<MovieData>> {

    private MainActivity mainActivity;

    public static final int ID_MOVIE_DATA_LOADER = 1000;

    public static final String SORTING_OPTION_INDEX = "sorting_option_index";

    private static final String TAG = DownloadMovieDataLoader.class.getSimpleName();

    public DownloadMovieDataLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public Loader<List<MovieData>> onCreateLoader(int loaderId, final Bundle args) {
        switch (loaderId) {
            case ID_MOVIE_DATA_LOADER:
                return new AsyncTaskLoader<List<MovieData>>(mainActivity) {

                    List<MovieData> cacheData;

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();

                        if (cacheData == null) {
                            mainActivity.getLoadingProgress().setVisibility(View.VISIBLE);
                            forceLoad();
                        } else {
                            deliverResult(cacheData);
                        }
                    }

                    @Override
                    public List<MovieData> loadInBackground() {
                        // generate url
                        final URL movieDataUrl = NetworkUtils.buildUrl(args.getInt(SORTING_OPTION_INDEX));

                        try {
                            // load json data string
                            final String movieDataJsonString = NetworkUtils.getResponseFromHttpUrl(movieDataUrl);
                            // convert to list of movie data
                            return MovieDataJsonUtils.getMovieDataFromJson(mainActivity, movieDataJsonString);
                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.v(TAG, "Exception while loading movie data", e);
                        }

                        return null;
                    }

                    @Override
                    public void deliverResult(List<MovieData> data) {
                        cacheData = data;
                        super.deliverResult(data);
                    }
                };

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> data) {
        mainActivity.getLoadingProgress().setVisibility(View.INVISIBLE);
        // mainActivity.setRefreshing(false);

        if (data != null && !data.isEmpty()) {
            // if data is available from background task set data to adapter
            mainActivity.setMovieDataViewsVisible();
            mainActivity.setMovieData(data);
        } else {
            // display error message to user
            mainActivity.setErrorViewsVisible();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieData>> loader) {
        mainActivity.setMovieData(null);
    }

}
