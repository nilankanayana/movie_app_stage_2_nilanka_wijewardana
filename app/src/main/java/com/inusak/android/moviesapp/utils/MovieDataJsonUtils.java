package com.inusak.android.moviesapp.utils;

import android.content.Context;

import com.inusak.android.moviesapp.R;
import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.data.ReviewData;
import com.inusak.android.moviesapp.data.TrailerData;
import com.inusak.android.moviesapp.exception.InvalidStatusException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * This class provides fascilities to interpret json data in terms of movie data.
 * <p>
 * Created by Nilanka on 10/12/2017.
 */

public class MovieDataJsonUtils {

    // common
    private static final String TAG_STATUS_CODE = "status_code";

    private static final String TAG_STATUS_MESSAGE = "status_message";

    private static final String TAG_STATUS_SUCCESS = "success";

    private static final String TAG_RESULTS = "results";

    // movies
    private static final String TAG_MOVIE_ID = "id";

    private static final String TAG_MOVIE_TITLE = "title";

    private static final String TAG_MOVIE_RELEASE_DATE = "release_date";

    private static final String TAG_MOVIE_POSTER_PATH = "poster_path";

    private static final String TAG_MOVIE_BACKDROP_PATH = "backdrop_path";

    private static final String TAG_MOVIE_AVERAGE_VOTES = "vote_average";

    private static final String TAG_MOVIE_VOTE_COUNT = "vote_count";

    private static final String TAG_MOVIE_GENRES = "genres";

    private static final String TAG_MOVIE_GENRE_IDS = "genre_ids";

    private static final String TAG_MOVIE_ORIGINAL_LANGUAGE = "original_language";

    private static final String TAG_MOVIE_OVERVIEW = "overview";

    private static final String TAG_MOVIE_ATTRIBUTE_NAME = "name";

    // trailers
    private static final String TAG_TRAILER_ID = "id";

    private static final String TAG_TRAILER_KEY = "key";

    private static final String TAG_TRAILER_NAME = "name";

    private static final String TAG_TRAILER_SITE = "site";

    // reviews
    private static final String TAG_REVIEW_ID = "id";

    private static final String TAG_REVIEW_AUTHOR = "author";

    private static final String TAG_REVIEW_CONTENT = "content";

    /**
     * This method processes the provided json string and produces a list of movie data objects
     *
     * @param context             {@link Context}
     * @param movieDataJsonString {@link String}
     * @return
     * @throws JSONException
     * @throws InvalidStatusException
     */
    public static List<MovieData> getMovieDataFromJson(Context context, String movieDataJsonString) throws JSONException, InvalidStatusException {
        final List<MovieData> list = new ArrayList<>();

        final JSONObject movieDataJson = new JSONObject(movieDataJsonString);
        if (movieDataJson.has(TAG_STATUS_CODE) && !movieDataJson.getBoolean(TAG_STATUS_SUCCESS)) {
            throw new InvalidStatusException(movieDataJson.getInt(TAG_STATUS_CODE), movieDataJson.getString(TAG_STATUS_CODE));
        }

        final JSONArray moviesJsonArray = movieDataJson.getJSONArray(TAG_RESULTS);
        for (int i = 0; i < moviesJsonArray.length(); i++) {
            final JSONObject movie = moviesJsonArray.getJSONObject(i);
            final long id = movie.getLong(TAG_MOVIE_ID);
            final String title = movie.getString(TAG_MOVIE_TITLE);
            final String releaseDate = movie.getString(TAG_MOVIE_RELEASE_DATE);
            final String posterPath = movie.getString(TAG_MOVIE_POSTER_PATH);
            final String backdropPath = movie.getString(TAG_MOVIE_BACKDROP_PATH);
            final double averageVotes = Double.parseDouble(movie.getString(TAG_MOVIE_AVERAGE_VOTES));
            final String voteCount = movie.getString(TAG_MOVIE_VOTE_COUNT);
            final String overview = movie.getString(TAG_MOVIE_OVERVIEW);

            StringBuffer buffer = new StringBuffer();
            String genres;
            if (movieDataJson.has(TAG_MOVIE_GENRES)) {
                final JSONArray genresJsonArray = movieDataJson.getJSONArray(TAG_MOVIE_GENRES);
                for (int j = 0; j < genresJsonArray.length(); j++) {
                    buffer.append(genresJsonArray.getJSONObject(j).getString(TAG_MOVIE_ATTRIBUTE_NAME));
                    if (j < genresJsonArray.length() - 1) {
                        buffer.append(" | ");
                    }
                }
            } else if (movieDataJson.has(TAG_MOVIE_GENRE_IDS)) {
                final JSONArray genresJsonArray = movieDataJson.getJSONArray(TAG_MOVIE_GENRE_IDS);
                for (int j = 0; j < genresJsonArray.length(); j++) {
                    final int genreId = genresJsonArray.getInt(j);
                    buffer.append(MovieDataUtils.getStringForGenre(context, genreId));
                    if (j < genresJsonArray.length() - 1) {
                        buffer.append(" | ");
                    }
                }
            }
            genres = buffer.toString();

            final String languages = new Locale(movie.getString(TAG_MOVIE_ORIGINAL_LANGUAGE)).getDisplayLanguage();

            list.add(new MovieData(id, title, releaseDate, posterPath, backdropPath, averageVotes, voteCount, overview, genres, languages));
        }

        return list;
    }

    /**
     * This method processes the provided json string and produces a list of trailer data objects
     *
     * @param context               {@link Context}
     * @param trailerDataJsonString {@link String}
     * @return List<TrailerData>
     * @throws JSONException
     * @throws InvalidStatusException
     */
    public static List<TrailerData> getTrailerDataFromJson(Context context, String trailerDataJsonString) throws JSONException, InvalidStatusException {
        final List<TrailerData> list = new ArrayList<>();

        final JSONObject movieDataJson = new JSONObject(trailerDataJsonString);
        if (movieDataJson.has(TAG_STATUS_CODE) && !movieDataJson.getBoolean(TAG_STATUS_SUCCESS)) {
            throw new InvalidStatusException(movieDataJson.getInt(TAG_STATUS_CODE), movieDataJson.getString(TAG_STATUS_CODE));
        }

        final JSONArray trailersJsonArray = movieDataJson.getJSONArray(TAG_RESULTS);
        for (int i = 0; i < trailersJsonArray.length(); i++) {
            final JSONObject trailer = trailersJsonArray.getJSONObject(i);
            final String id = trailer.getString(TAG_TRAILER_ID);
            final String key = trailer.getString(TAG_TRAILER_KEY);
            final String name = trailer.getString(TAG_TRAILER_NAME);
            final String site = trailer.getString(TAG_TRAILER_SITE);

            list.add(new TrailerData(id, key, name, site));
        }

        return list;
    }

    /**
     * This method processes the provided json string and produces a list of review data objects
     *
     * @param context              {@link Context}
     * @param reviewDataJsonString {@link String}
     * @return List<ReviewData>
     * @throws JSONException
     * @throws InvalidStatusException
     */
    public static List<ReviewData> getReviewDataFromJson(Context context, String reviewDataJsonString) throws JSONException, InvalidStatusException {
        final List<ReviewData> list = new ArrayList<>();

        final JSONObject reviewDataJson = new JSONObject(reviewDataJsonString);
        if (reviewDataJson.has(TAG_STATUS_CODE) && !reviewDataJson.getBoolean(TAG_STATUS_SUCCESS)) {
            throw new InvalidStatusException(reviewDataJson.getInt(TAG_STATUS_CODE), reviewDataJson.getString(TAG_STATUS_CODE));
        }

        final JSONArray reviewsJsonArray = reviewDataJson.getJSONArray(TAG_RESULTS);
        for (int i = 0; i < reviewsJsonArray.length(); i++) {
            final JSONObject review = reviewsJsonArray.getJSONObject(i);
            final String id = review.getString(TAG_REVIEW_ID);
            final String author = review.getString(TAG_REVIEW_AUTHOR);
            final String content = review.getString(TAG_REVIEW_CONTENT);

            list.add(new ReviewData(id, author, content));
        }

        return list;
    }

}
