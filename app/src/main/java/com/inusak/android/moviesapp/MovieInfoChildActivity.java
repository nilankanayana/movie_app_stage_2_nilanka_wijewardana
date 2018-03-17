package com.inusak.android.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.data.TrailerData;
import com.inusak.android.moviesapp.task.DownloadReviewDataLoader;
import com.inusak.android.moviesapp.task.DownloadTrailerDataLoader;
import com.inusak.android.moviesapp.task.MultiPurposeFavouriteMovieDataTask;
import com.inusak.android.moviesapp.utils.MovieDataCursorUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.inusak.android.moviesapp.utils.NetworkUtils.YOUTUBE_APP_URI_PREFIX;
import static com.inusak.android.moviesapp.utils.NetworkUtils.YOUTUBE_WEB_URI_PREFIX;

/**
 * This class handles displaying movie information once a poster item is clocked from the main activity.
 */
public class MovieInfoChildActivity extends AppCompatActivity implements TrailerDataAdapter.OnTrailerItemClickListener {

    private ImageView posterImageView;

    private ImageView backdropImageView;

    private TextView titleTextView;

    private TextView summaryTextView;

    private TextView voteCountTextView;

    private TextView overviewTextView;

    private RatingBar ratingBar;

    private MenuItem menuItemLike;

    private RecyclerView recyclerViewTrailers;

    private RecyclerView recyclerViewReviews;

    private TrailerDataAdapter trailerDataAdapter;

    private ReviewDataAdapter reviewDataAdapter;

    private DownloadTrailerDataLoader downloadTrailerDataLoader;

    private DownloadReviewDataLoader downloadReviewDataLoader;

    private MovieData movieData;

    private static final String FORCE_FULL_SCREEN = "force_fullscreen";

    private static final String TEXT_PLAIN = "text/plain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info_child);

        // find views
        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        backdropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        titleTextView = (TextView) findViewById(R.id.tv_title);
        summaryTextView = (TextView) findViewById(R.id.tv_summary);
        voteCountTextView = (TextView) findViewById(R.id.tv_vote_count);
        overviewTextView = (TextView) findViewById(R.id.tv_overview);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        // create recycler views
        recyclerViewTrailers = (RecyclerView) findViewById(R.id.rv_trailers);
        recyclerViewReviews = (RecyclerView) findViewById(R.id.rv_reviews);

        // set layout managers
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        // performance updates
        recyclerViewTrailers.setHasFixedSize(true);
        recyclerViewReviews.setHasFixedSize(true);

        // set data adapters
        trailerDataAdapter = new TrailerDataAdapter(this);
        recyclerViewTrailers.setAdapter(trailerDataAdapter);

        reviewDataAdapter = new ReviewDataAdapter();
        recyclerViewReviews.setAdapter(reviewDataAdapter);

        // set data to ui received from intent
        setDataToUI();

        // init loader tasks
        createLoaders();
    }

    /**
     * This method is used to create and init loaders
     */
    private void createLoaders() {
        // load trailer and review tasks
        downloadTrailerDataLoader = new DownloadTrailerDataLoader(this, movieData.getId());
        downloadReviewDataLoader = new DownloadReviewDataLoader(this, movieData.getId());

        getSupportLoaderManager().initLoader(DownloadTrailerDataLoader.ID_TRAILER_DATA_LOADER, null, downloadTrailerDataLoader);
        getSupportLoaderManager().initLoader(DownloadReviewDataLoader.ID_REVIIEW_DATA_LOADER, null, downloadReviewDataLoader);
    }

    /**
     * This method is used to set data to ui elements
     */
    private void setDataToUI() {
        // get intent
        final Intent intentStartedThisActivity = getIntent();

        // if intent is valid and has extra params, then start processing
        if (intentStartedThisActivity != null && intentStartedThisActivity.hasExtra(MovieData.MOVIE_DATA_KEY)) {
            // retrieve movie data from parcel
            setMovieData((MovieData) intentStartedThisActivity.getParcelableExtra(MovieData.MOVIE_DATA_KEY));

            // set backdrop
            Picasso.with(this).load(NetworkUtils.DEFAULT_URL_BACKDROPS_WITH_SIZE + getMovieData().getBackdropPath()).into(backdropImageView);

            // set movie title to tile of action bar
            getSupportActionBar().setTitle(getMovieData().getTitle());

            // load image using Picasso and set to image view
            Picasso.with(this).load(NetworkUtils.DEFAULT_URL_POSTERS_WITH_SIZE + getMovieData().getPosterPath()).into(posterImageView);

            // set all other data ui elements
            titleTextView.setText(getMovieData().getTitle());
            summaryTextView.setText((!getMovieData().getGenres().isEmpty() ? getMovieData().getGenres() + " | " : "") + getMovieData().getLanguages() + " | " + getMovieData().getReleaseDate());
            voteCountTextView.setText("(" + getMovieData().getVoteCount() + ")");
            overviewTextView.setText(getMovieData().getOverview());

            // rating bar behaviour
            ratingBar.setRating((float) (getMovieData().getAverageVotes() / 2));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create menu item
        getMenuInflater().inflate(R.menu.menu_movie_info, menu);

        // set favourites icon based on isLiked value
        menuItemLike = menu.findItem(R.id.action_like);

        // initiate favourite task check
        final Map<String, Object> map = new HashMap<>(2);
        map.put(MultiPurposeFavouriteMovieDataTask.KEY_MODE, MultiPurposeFavouriteMovieDataTask.QUERY);
        map.put(MultiPurposeFavouriteMovieDataTask.KEY_ID, getMovieData().getId());

        new MultiPurposeFavouriteMovieDataTask(this).execute(map);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_like) { // when like button is clicked
            if (!movieData.isLiked()) { // if not already liked, then like
                final Map<String, Object> map = new HashMap<>(3);
                map.put(MultiPurposeFavouriteMovieDataTask.KEY_MODE, MultiPurposeFavouriteMovieDataTask.INSERT);
                map.put(MultiPurposeFavouriteMovieDataTask.KEY_ID, getMovieData().getId());
                map.put(MultiPurposeFavouriteMovieDataTask.KEY_CONTENT_VALUES, MovieDataCursorUtils.getContentValuesFromMovieData(getMovieData()));

                // create image bytes[]
                // try {
                //    ImageUtils.getBytes(getContentResolver().openInputStream(Uri.parse(NetworkUtils.DEFAULT_URL_POSTERS_WITH_SIZE).
                //            buildUpon().appendPath(getMovieData().getPosterPath()).build()));
                //} catch (IOException ex) {
                //    ex.printStackTrace();
                //}

                // insert
                new MultiPurposeFavouriteMovieDataTask(this).execute(map);
            } else { // unlike
                final Map<String, Object> map = new HashMap<>(2);
                map.put(MultiPurposeFavouriteMovieDataTask.KEY_MODE, MultiPurposeFavouriteMovieDataTask.DELETE);
                map.put(MultiPurposeFavouriteMovieDataTask.KEY_ID, getMovieData().getId());

                // delete
                new MultiPurposeFavouriteMovieDataTask(this).execute(map);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(TrailerData trailerData, View view) {
        if (view.getId() == R.id.iv_trailer) {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_URI_PREFIX + trailerData.getKey()));
            if (youtubeIntent.resolveActivity(getPackageManager()) == null) {
                youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_WEB_URI_PREFIX + trailerData.getKey()));
            }
            youtubeIntent.putExtra(FORCE_FULL_SCREEN, true);
            startActivity(youtubeIntent);
        } else if (view.getId() == R.id.iv_share) {
            final Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType(TEXT_PLAIN)
                    .setText(YOUTUBE_WEB_URI_PREFIX + trailerData.getKey())
                    .setChooserTitle(getResources().getString(R.string.share_trailer))
                    .getIntent();

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(shareIntent);
            }
        }
    }

    /**
     * This method can be used to retrieve trailer data adapter
     *
     * @return TrailerDataAdapter
     */
    public TrailerDataAdapter getTrailerDataAdapter() {
        return trailerDataAdapter;
    }

    /**
     * This method can be used to retrieve review data adapter
     *
     * @return ReviewDataAdapter
     */
    public ReviewDataAdapter getReviewDataAdapter() {
        return reviewDataAdapter;
    }

    /**
     * This method is used to handle favourite behaviour
     *
     * @param liked {@link Boolean}
     */
    public void handleFavouriteBehaviour(Boolean liked) {
        movieData.setLiked(liked);
        menuItemLike.setIcon(movieData.isLiked() ? R.drawable.ic_favour : R.drawable.ic_unfavour);
        menuItemLike.setVisible(true);
    }

    /**
     * This method is used to notify user of the favourite/unfavourite action status
     *
     * @param mode {@link String}
     */
    public void notifyUser(final String mode) {
        int resourceId = -1;
        switch (mode) {
            case MultiPurposeFavouriteMovieDataTask.INSERT:
                resourceId = R.string.notify_user_make_favourite;
                break;
            case MultiPurposeFavouriteMovieDataTask.DELETE:
                resourceId = R.string.notify_user_make_unfavourite;
                break;
            default:
                break;
        }

        if (resourceId > -1) {
            final String message = getResources().getString(resourceId, movieData.getTitle());
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is used to retrieve movie data
     *
     * @return MovieData
     */
    public MovieData getMovieData() {
        return movieData;
    }

    /**
     * This method is used to set movie data
     *
     * @param movieData {@link MovieData}
     */
    private void setMovieData(MovieData movieData) {
        this.movieData = movieData;
    }

}
