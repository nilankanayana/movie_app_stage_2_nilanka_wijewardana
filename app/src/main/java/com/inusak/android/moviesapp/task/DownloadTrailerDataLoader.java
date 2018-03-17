package com.inusak.android.moviesapp.task;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.inusak.android.moviesapp.MovieInfoChildActivity;
import com.inusak.android.moviesapp.data.TrailerData;
import com.inusak.android.moviesapp.utils.MovieDataJsonUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * This class handles background data loading for trailer data from movie db api.
 * Created by Nilanka on 3/8/2018.
 */

public class DownloadTrailerDataLoader implements LoaderManager.LoaderCallbacks<List<TrailerData>> {

    private MovieInfoChildActivity movieInfoChildActivity;

    private long movieId;

    private static final String TAG = DownloadTrailerDataLoader.class.getSimpleName();

    public static final int ID_TRAILER_DATA_LOADER = 3000;

    public DownloadTrailerDataLoader(MovieInfoChildActivity movieInfoChildActivity, long movieId) {
        this.movieInfoChildActivity = movieInfoChildActivity;
        this.movieId = movieId;
    }


    @Override
    public Loader<List<TrailerData>> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_TRAILER_DATA_LOADER:
                return new AsyncTaskLoader<List<TrailerData>>(movieInfoChildActivity) {

                    List<TrailerData> cacheData;

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();

                        if (cacheData == null) {
                            // movieInfoChildActivity.getLoadingProgress().setVisibility(View.VISIBLE);
                            forceLoad();
                        } else {
                            deliverResult(cacheData);
                        }
                    }

                    @Override
                    public List<TrailerData> loadInBackground() {
                        // generate url
                        final URL trailerDataUrl = NetworkUtils.buildUrl(movieId, NetworkUtils.VIDEO_SEARCH);

                        try {
                            // load json data string
                            final String trailerDataJsonString = NetworkUtils.getResponseFromHttpUrl(trailerDataUrl);
                            // convert to list of trailer data
                            return MovieDataJsonUtils.getTrailerDataFromJson(movieInfoChildActivity, trailerDataJsonString);
                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.v(TAG, "Exception while loading trailer data", e);
                        }

                        return null;
                    }

                    @Override
                    public void deliverResult(List<TrailerData> data) {
                        cacheData = data;
                        super.deliverResult(data);
                    }

                };

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<TrailerData>> loader, List<TrailerData> data) {
        // movieInfoChildActivity.getLoadingProgress().setVisibility(View.INVISIBLE);

        if (data != null && !data.isEmpty()) {
            // if data is available from background task set data to adapter
            // mainActivity.setMovieDataViewsVisible();
            movieInfoChildActivity.getTrailerDataAdapter().setData(data);
        } else {
            // display error message to user
            // movieInfoChildActivity.setErrorViewsVisible();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<TrailerData>> loader) {
        movieInfoChildActivity.getTrailerDataAdapter().setData(null);
    }
}
