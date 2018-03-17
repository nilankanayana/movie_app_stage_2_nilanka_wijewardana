package com.inusak.android.moviesapp;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.task.DownloadMovieDataLoader;
import com.inusak.android.moviesapp.task.FetchFavouriteMovieDataLoader;
import com.inusak.android.moviesapp.utils.NetworkUtils;

/**
 * Main Activity for displaying all movie posters and selecting the desired sort criteria.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, MovieDataAdapter.OnMovieItemClickListener {

    private Spinner searchSpinner;

    private RecyclerView recylerView;

    private TextView errorTextView;

    private ProgressBar loadingProgress;

    private MovieDataAdapter movieDataAdapter;

    private DownloadMovieDataLoader downloadMovieDataloader;

    private FetchFavouriteMovieDataLoader fetchFavouriteMovieDataloader;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Bundle bundle;

    private int spinnerIndex;

    private static final String SPINNER_INDEX_KEY = "spinner_index";

    private static final int DEFAULT_SPINNER_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load swipe refresh layout
        // initSwipeRefreshLayout();

        // find ui components
        recylerView = (RecyclerView) findViewById(R.id.rv_movies);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);
        loadingProgress = (ProgressBar) findViewById(R.id.pb_loading);

        // handle awkward 2 image grid for landscape modes
        int spanCount = 2;
        final int rotation = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            spanCount = 3;
        }

        // create grid layout with 2 columns
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recylerView.setLayoutManager(layoutManager);

        // performance improvements via fixed size attribute
        recylerView.setHasFixedSize(true);

        // create data adapter
        movieDataAdapter = new MovieDataAdapter(this);
        recylerView.setAdapter(movieDataAdapter);

        // init task for downloading movie data from moviedb
        downloadMovieDataloader = new DownloadMovieDataLoader(this);
        bundle = new Bundle();

        // init task for fetching favourite data from sqlite db
        fetchFavouriteMovieDataloader = new FetchFavouriteMovieDataLoader(this);

        // if spinner index was saved before
        if (savedInstanceState != null) {
            spinnerIndex = savedInstanceState.getInt(SPINNER_INDEX_KEY, DEFAULT_SPINNER_INDEX);
        }
    }

    /**
     * This method is used to initialize swipe refresh layout
     */
    private void initSwipeRefreshLayout() {
        // find layout
        // swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // add refresh listener
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reset data set when spinner selection is changed
                setMovieData(null);

                // load data from moviedb
                loadMovieData();
            }
        });

        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * This method is used to set refreshing state
     *
     * @param refresh boolean
     */
    public void setRefreshing(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create menu item
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // find sorting spinner
        final MenuItem sortingMenuItem = menu.findItem(R.id.sorting_spinner);
        searchSpinner = (Spinner) sortingMenuItem.getActionView();

        // add listeners
        searchSpinner.setOnItemSelectedListener(this);

        // load search criteria to spinner
        loadSearchTypes();

        // set first index
        searchSpinner.setSelection(spinnerIndex);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_refresh) {
            // reset data set when spinner selection is changed
            setMovieData(null);

            // load data from moviedb
            loadMovieData();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // keep track of the current spinner state
        if (searchSpinner != null) {
            outState.putInt(SPINNER_INDEX_KEY, searchSpinner.getSelectedItemPosition());
        }
    }

    /**
     * This method will load search criteria and set them to spinner
     */
    private void loadSearchTypes() {
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);
    }

    /**
     * This method will load movie data from MovieDb / SQLite db and display recycler view
     */
    private void loadMovieData() {
        setMovieDataViewsVisible();

        // favourites are always loaded with offline data
        if (isFavouriteMode()) {
            // async task creation and execution
            getSupportLoaderManager().restartLoader(FetchFavouriteMovieDataLoader.ID_MOVIE_DATA_LOADER, null, fetchFavouriteMovieDataloader);
        }
        // network available scenario for most popular and highest rated selections
        else if (NetworkUtils.isOnline(MainActivity.this)) {
            // async task creation and execution
            bundle.putInt(DownloadMovieDataLoader.SORTING_OPTION_INDEX, searchSpinner.getSelectedItemPosition());
            getSupportLoaderManager().restartLoader(DownloadMovieDataLoader.ID_MOVIE_DATA_LOADER, bundle, downloadMovieDataloader);
        } else {
            setErrorViewsVisible();
        }
    }

    /**
     * This method is used to set movie data to the underlying data adapter based on the load mode
     *
     * @param data {@link Object}
     */
    public void setMovieData(Object data) {
        movieDataAdapter.setMovieData(data, isFavouriteMode() ? MovieDataAdapter.MODE_CURSOR : MovieDataAdapter.MODE_DATA_LIST);
    }

    /**
     * This method sets movie data views visible
     */
    public void setMovieDataViewsVisible() {
        errorTextView.setVisibility(View.INVISIBLE);
        recylerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method sets error views visible
     */
    public void setErrorViewsVisible() {
        recylerView.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // reset data set when spinner selection is changed
        setMovieData(null);

        // load data from moviedb
        loadMovieData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    public void onClick(MovieData movieData) {
        // create an intent when a movie thumbnail is clicked
        final Intent intent = new Intent(this, MovieInfoChildActivity.class);
        // add movie data as extra params
        intent.putExtra(MovieData.MOVIE_DATA_KEY, movieData);
        // start activity
        startActivity(intent);
    }

    /**
     * This method is used to retrieve loading progress bar
     *
     * @return ProgressBar
     */
    public ProgressBar getLoadingProgress() {
        return loadingProgress;
    }

    /**
     * This method can be used to retrieve recycler view
     *
     * @return RecyclerView
     */
    public RecyclerView getRecylerView() {
        return recylerView;
    }

    /**
     * This method is used to identify favourites mode
     *
     * @return true if its favourites mode or else false
     */
    public boolean isFavouriteMode() {
        if (searchSpinner != null) {
            return searchSpinner.getSelectedItem().equals(getResources().getString(R.string.favourites));
        }

        return false;
    }
}
