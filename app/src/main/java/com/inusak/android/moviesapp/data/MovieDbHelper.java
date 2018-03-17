package com.inusak.android.moviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.inusak.android.moviesapp.data.MovieContract.*;

/**
 * This class provides the support for handling all db queries with the underlying sqlite db.
 * Created by Nilanka on 3/3/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE =
                "CREATE TABLE " + FavouriteEntry.TABLE_NAME + " (" +
                        FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavouriteEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        FavouriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        FavouriteEntry.COLUMN_OVERVIEW + " TEXT, " +
                        FavouriteEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        FavouriteEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        FavouriteEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                        FavouriteEntry.COLUMN_LANGUAGES + " TEXT, " +
                        FavouriteEntry.COLUMN_GENRES + " TEXT, " +
                        FavouriteEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                        FavouriteEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                        FavouriteEntry.COLUMN_POSTER + " BLOB, " +
                        FavouriteEntry.COLUMN_BACKDROP + " BLOB, " +
                        "UNIQUE (" + FavouriteEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
