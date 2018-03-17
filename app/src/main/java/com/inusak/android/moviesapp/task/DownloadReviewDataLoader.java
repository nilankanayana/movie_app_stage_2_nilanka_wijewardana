package com.inusak.android.moviesapp.task;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.inusak.android.moviesapp.MovieInfoChildActivity;
import com.inusak.android.moviesapp.data.ReviewData;
import com.inusak.android.moviesapp.utils.MovieDataJsonUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * This class handles background data loading for review data from movie db api.
 * Created by Nilanka on 3/8/2018.
 */

public class DownloadReviewDataLoader implements LoaderManager.LoaderCallbacks<List<ReviewData>> {

    private long movieId;

    private MovieInfoChildActivity movieInfoChildActivity;

    private static final String TAG = DownloadReviewDataLoader.class.getSimpleName();

    public static final int ID_REVIIEW_DATA_LOADER = 4000;

    public DownloadReviewDataLoader(MovieInfoChildActivity movieInfoChildActivity, long movieId) {
        this.movieInfoChildActivity = movieInfoChildActivity;
        this.movieId = movieId;
    }


    @Override
    public Loader<List<ReviewData>> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_REVIIEW_DATA_LOADER:
                return new AsyncTaskLoader<List<ReviewData>>(movieInfoChildActivity) {

                    List<ReviewData> cacheData;

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
                    public List<ReviewData> loadInBackground() {
                        // generate url
                        final URL reviewDataUrl = NetworkUtils.buildUrl(movieId, NetworkUtils.REVIEW_SEARCH);

                        try {
                            // load json data string
                            final String reviewDataJsonString = NetworkUtils.getResponseFromHttpUrl(reviewDataUrl);
                            // convert to list of review data
                            return MovieDataJsonUtils.getReviewDataFromJson(movieInfoChildActivity, reviewDataJsonString);
                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.v(TAG, "Exception while loading review data", e);
                        }

                        return null;
                    }

                    @Override
                    public void deliverResult(List<ReviewData> data) {
                        cacheData = data;
                        super.deliverResult(data);
                    }

                };

            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<ReviewData>> loader, List<ReviewData> data) {
        // movieInfoChildActivity.getLoadingProgress().setVisibility(View.INVISIBLE);

        if (data != null && !data.isEmpty()) {
            // if data is available from background task set data to adapter
            // mainActivity.setMovieDataViewsVisible();
            movieInfoChildActivity.getReviewDataAdapter().setData(data);
        } else {
            // display error message to user
            // movieInfoChildActivity.setErrorViewsVisible();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ReviewData>> loader) {
        movieInfoChildActivity.getReviewDataAdapter().setData(null);
    }
}
