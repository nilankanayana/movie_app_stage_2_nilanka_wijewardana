package com.inusak.android.moviesapp.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.inusak.android.moviesapp.data.MovieContract;
import com.inusak.android.moviesapp.data.MovieContract.FavouriteEntry;
import com.inusak.android.moviesapp.task.FetchFavouriteMovieDataLoader;
import com.inusak.android.moviesapp.data.MovieData;

import static android.net.Uri.parse;

/**
 * This class provides functionalities to handle cursor based movie data conversions.
 * Created by Nilanka on 3/4/2018.
 */

public class MovieDataCursorUtils {

    /**
     * This method can be used to load an {@link Bitmap} image from a cursor object
     *
     * @param cursor      {@link Cursor}
     * @param columnIndex int
     * @return Bitmap
     */
    public static Bitmap loadImageForCurrentPosition(Cursor cursor, int columnIndex) {
        final byte[] imageData = cursor.getBlob(columnIndex);
        final Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        return image;
    }

    /**
     * This method can be used to retrieve byte[] image data from image uri
     *
     * @param imagePath {@link String}
     * @return byte[]
     */
    public static byte[] getImageDataFromUri(String imagePath) {
        final Uri uri = Uri.parse(NetworkUtils.DEFAULT_URL_POSTERS_WITH_SIZE).buildUpon().appendPath(imagePath).build();
        return null;
    }

    /**
     * This method can be used to make a {@link MovieData} object from a db cursor object
     *
     * @param cursor {@link Cursor}
     * @return MovieData
     */
    public static MovieData getDataForCurrentPosition(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            final long id = cursor.getLong(FavouriteEntry.INDEX_COLUMN_MOVIE_ID);
            final String title = cursor.getString(FavouriteEntry.INDEX_COLUMN_TITLE);
            final String overview = cursor.getString(FavouriteEntry.INDEX_COLUMN_OVERVIEW);
            final String releaseData = cursor.getString(FavouriteEntry.INDEX_COLUMN_RELEASE_DATE);
            final String languages = cursor.getString(FavouriteEntry.INDEX_COLUMN_LANGUAGES);
            final String genres = cursor.getString(FavouriteEntry.INDEX_COLUMN_GENRES);
            final String voteCount = cursor.getString(FavouriteEntry.INDEX_COLUMN_VOTE_COUNT);

            final String posterPath = cursor.getString(FavouriteEntry.INDEX_COLUMN_POSTER_PATH);
            final String backdropPath = cursor.getString(FavouriteEntry.INDEX_COLUMN_BACKDROP_PATH);

            final double averageVotes = cursor.getDouble(FavouriteEntry.INDEX_COLUMN_VOTE_AVERAGE);

            // final byte[] posterImageData = cursor.getBlob(FavouriteEntry.INDEX_COLUMN_POSTER);
            // final byte[] backdropImageData = cursor.getBlob(FavouriteEntry.INDEX_COLUMN_BACKDROP);

            return new MovieData(id, title, releaseData, posterPath, backdropPath, averageVotes, voteCount, overview, genres, languages, null, null, true);
        }

        return null;
    }

    /**
     * This method can be used to make {@link ContentValues} object from {@link MovieData}
     *
     * @param movieData {@link MovieData}
     * @return ContentValues
     */
    public static ContentValues getContentValuesFromMovieData(MovieData movieData) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(FavouriteEntry.COLUMN_MOVIE_ID, movieData.getId());
        contentValues.put(FavouriteEntry.COLUMN_TITLE, movieData.getTitle());
        contentValues.put(FavouriteEntry.COLUMN_OVERVIEW, movieData.getOverview());
        contentValues.put(FavouriteEntry.COLUMN_GENRES, movieData.getGenres());
        contentValues.put(FavouriteEntry.COLUMN_LANGUAGES, movieData.getLanguages());
        contentValues.put(FavouriteEntry.COLUMN_RELEASE_DATE, movieData.getReleaseDate());
        contentValues.put(FavouriteEntry.COLUMN_VOTE_AVERAGE, movieData.getAverageVotes());
        contentValues.put(FavouriteEntry.COLUMN_VOTE_COUNT, movieData.getVoteCount());
        contentValues.put(FavouriteEntry.COLUMN_POSTER_PATH, movieData.getPosterPath());
        contentValues.put(FavouriteEntry.COLUMN_BACKDROP_PATH, movieData.getBackdropPath());
        contentValues.put(FavouriteEntry.COLUMN_POSTER, movieData.getPosterImageData());
        contentValues.put(FavouriteEntry.COLUMN_BACKDROP, movieData.getBackdropImageData());

        return contentValues;
    }
}
