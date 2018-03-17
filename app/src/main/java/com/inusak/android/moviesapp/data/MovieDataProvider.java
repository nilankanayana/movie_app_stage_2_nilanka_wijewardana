package com.inusak.android.moviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.inusak.android.moviesapp.data.MovieContract.*;

/**
 * This class is the data provider for all Movie Data saved locally in each users environment.
 * Created by Nilanka on 3/4/2018.
 */

public class MovieDataProvider extends ContentProvider {

    private MovieDbHelper dbHelper;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static final int CODE_FAVOURITES = 100;

    public static final int CODE_FAVOURITE_WITH_ID = 200;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            // when uri is of type: content://com.inusak.android.moviesapp.movie/favourite/112233
            case CODE_FAVOURITE_WITH_ID: {
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        FavouriteEntry.TABLE_NAME,
                        projection,
                        FavouriteEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            // when uri is exactly: content://com.inusak.android.moviesapp.movie/favourite/
            case CODE_FAVOURITES: {
                cursor = dbHelper.getReadableDatabase().query(
                        FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Types are not supported by " + MovieDataProvider.class.getSimpleName());
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null; // URI to be returned

        switch (uriMatcher.match(uri)) {
            case CODE_FAVOURITES:
                long id = dbHelper.getWritableDatabase().insert(FavouriteEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavouriteEntry.CONTENT_URI, id);
                    // notify the resolver if the uri has been changed
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case CODE_FAVOURITES:
                selection = "1"; // we make it 1 so that all rows get deleted
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        FavouriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case CODE_FAVOURITE_WITH_ID:
                selection = FavouriteEntry.COLUMN_MOVIE_ID + " = ? "; // any other scenarios, we make it delete by movie_id
                selectionArgs = new String[]{uri.getLastPathSegment()};
                numRowsDeleted = dbHelper.getWritableDatabase().delete(
                        FavouriteEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // if we actually deleted any rows, notify that a change has occurred to this URI
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update is not supported by " + MovieDataProvider.class.getSimpleName());
    }

    /**
     * This method creates a uri matcher to handle all supported uris by this content provider
     *
     * @return UriMatcher
     */
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;
        matcher.addURI(authority, PATH_FAVOURITE, CODE_FAVOURITES);
        matcher.addURI(authority, PATH_FAVOURITE + "/#", CODE_FAVOURITE_WITH_ID);

        return matcher;
    }

}
