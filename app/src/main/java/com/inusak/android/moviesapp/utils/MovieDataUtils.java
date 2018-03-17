package com.inusak.android.moviesapp.utils;

import android.content.Context;

import com.inusak.android.moviesapp.R;

/**
 * This utility class provides all convenience methods for manipulation of movie data retrieved from moviedb api.
 * Created by Nilanka on 3/11/2018.
 */

public class MovieDataUtils {

    /**
     * This method can be used to get the corresponding text for the genre id
     *
     * @param context {@link Context}
     * @param id      int
     * @return String
     */
    public static String getStringForGenre(final Context context, final int id) {
        int resourceId;
        switch (id) {
            case 12:
                resourceId = R.string.genre_12;
                break;
            case 14:
                resourceId = R.string.genre_14;
                break;
            case 16:
                resourceId = R.string.genre_16;
                break;
            case 18:
                resourceId = R.string.genre_18;
                break;
            case 27:
                resourceId = R.string.genre_27;
                break;
            case 28:
                resourceId = R.string.genre_28;
                break;
            case 35:
                resourceId = R.string.genre_35;
                break;
            case 36:
                resourceId = R.string.genre_36;
                break;
            case 37:
                resourceId = R.string.genre_37;
                break;
            case 53:
                resourceId = R.string.genre_53;
                break;
            case 80:
                resourceId = R.string.genre_80;
                break;
            case 99:
                resourceId = R.string.genre_99;
                break;
            case 878:
                resourceId = R.string.genre_878;
                break;
            case 9648:
                resourceId = R.string.genre_9648;
                break;
            case 10402:
                resourceId = R.string.genre_10402;
                break;
            case 10749:
                resourceId = R.string.genre_10749;
                break;
            case 10751:
                resourceId = R.string.genre_10751;
                break;
            case 10752:
                resourceId = R.string.genre_10752;
                break;
            case 10770:
                resourceId = R.string.genre_10770;
                break;
            default:
                return context.getResources().getString(R.string.genre_unknown);
        }

        return context.getResources().getString(resourceId);
    }

}
