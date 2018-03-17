package com.inusak.android.moviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * This class defines the contract for the movie data content provider.
 * Created by Nilanka on 3/3/2018.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.inusak.android.moviesapp.movie";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITE = "favourite";

    /* Inner class that defines the table contents of the favourite table */
    public static final class FavouriteEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITE)
                .build();

        /* Used internally as the name of our movie table. */
        public static final String TABLE_NAME = "favourite";

        /* all column names */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static final String COLUMN_RELEASE_DATE = "releass_date";

        public static final String COLUMN_LANGUAGES = "languages";

        public static final String COLUMN_GENRES = "genres";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_BACKDROP = "backdrop";

        /* all column indices */
        public static final int INDEX_COLUMN_MOVIE_ID = 0;

        public static final int INDEX_COLUMN_TITLE = 1;

        public static final int INDEX_COLUMN_VOTE_AVERAGE = 2;

        public static final int INDEX_COLUMN_VOTE_COUNT = 3;

        public static final int INDEX_COLUMN_RELEASE_DATE = 4;

        public static final int INDEX_COLUMN_LANGUAGES = 5;

        public static final int INDEX_COLUMN_GENRES = 6;

        public static final int INDEX_COLUMN_OVERVIEW = 7;

        public static final int INDEX_COLUMN_POSTER_PATH = 8;

        public static final int INDEX_COLUMN_BACKDROP_PATH = 9;

        public static final int INDEX_COLUMN_POSTER = 10;

        public static final int INDEX_COLUMN_BACKDROP = 11;

        public static final String[] DEFAULT_MOVIE_PROJECTION = {
                FavouriteEntry.COLUMN_MOVIE_ID,
                FavouriteEntry.COLUMN_TITLE,
                FavouriteEntry.COLUMN_VOTE_AVERAGE,
                FavouriteEntry.COLUMN_VOTE_COUNT,
                FavouriteEntry.COLUMN_RELEASE_DATE,
                FavouriteEntry.COLUMN_LANGUAGES,
                FavouriteEntry.COLUMN_GENRES,
                FavouriteEntry.COLUMN_OVERVIEW,
                FavouriteEntry.COLUMN_POSTER_PATH,
                FavouriteEntry.COLUMN_BACKDROP_PATH
        };

    }

}
