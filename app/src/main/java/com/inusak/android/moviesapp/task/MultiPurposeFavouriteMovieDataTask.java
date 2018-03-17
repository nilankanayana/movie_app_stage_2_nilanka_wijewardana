package com.inusak.android.moviesapp.task;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.inusak.android.moviesapp.MovieInfoChildActivity;
import com.inusak.android.moviesapp.data.MovieContract;

import java.util.Map;

/**
 * This class handles on-demand background data insert/query/delete functions for locally saved movie
 * data in sqlite db.
 * Created by Nilanka on 3/11/2018.
 */

public class MultiPurposeFavouriteMovieDataTask extends AsyncTask<Map<String, Object>, Void, Boolean> {

    private MovieInfoChildActivity movieInfoChildActivity;

    private String mode;

    public static final String QUERY = "query";

    public static final String INSERT = "insert";

    public static final String DELETE = "delete";

    public static final String KEY_MODE = "mode";

    public static final String KEY_ID = "id";

    public static final String KEY_CONTENT_VALUES = "content-values";

    public MultiPurposeFavouriteMovieDataTask(MovieInfoChildActivity movieInfoChildActivity) {
        this.movieInfoChildActivity = movieInfoChildActivity;
    }

    @Override
    protected Boolean doInBackground(Map<String, Object>... maps) {
        final Map<String, Object> map = maps[0];

        setMode((String) map.get(KEY_MODE));

        final String movieIdInString = String.valueOf(map.get(KEY_ID));
        final Uri uri = MovieContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(movieIdInString).build();

        switch (getMode()) {
            case QUERY:
                final Cursor cursor = movieInfoChildActivity.getContentResolver().query(uri, new String[]{
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_ID}, null, null, null);
                return cursor != null ? cursor.getCount() != 0 : false;
            case INSERT:
                final ContentValues contentValues = (ContentValues) map.get(KEY_CONTENT_VALUES);
                final Uri returnUri = movieInfoChildActivity.getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI, contentValues);
                return returnUri != null;
            case DELETE:
                final int numberOfRowsDeleted = movieInfoChildActivity.getContentResolver().delete(uri, null, null);
                return numberOfRowsDeleted > 0 ? false : true;
            default:
                throw new UnsupportedOperationException("Mode " + getMode() + " is not supported");
        }
    }

    @Override
    protected void onPostExecute(Boolean liked) {
        // handle favourites behaviour
        movieInfoChildActivity.handleFavouriteBehaviour(liked);
        // notify user
        movieInfoChildActivity.notifyUser(getMode());
    }

    /**
     * This method is used to retrieve currently operating database function mode
     *
     * @return String
     */
    public String getMode() {
        return mode;
    }

    /**
     * This method is used to set currently operating database function mode
     *
     * @param mode {@link String}
     */
    private void setMode(String mode) {
        this.mode = mode;
    }

}
