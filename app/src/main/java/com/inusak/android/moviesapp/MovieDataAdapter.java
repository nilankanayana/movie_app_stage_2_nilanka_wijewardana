package com.inusak.android.moviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.MovieContract;
import com.inusak.android.moviesapp.data.MovieContract.FavouriteEntry;
import com.inusak.android.moviesapp.data.MovieData;
import com.inusak.android.moviesapp.task.FetchFavouriteMovieDataLoader;
import com.inusak.android.moviesapp.utils.MovieDataCursorUtils;
import com.inusak.android.moviesapp.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading movie data from models, inflate ui components and binding them to view holders.
 * <p>
 * Created by Nilanka on 10/14/2017.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.MovieDataViewHolder> {

    private final List<MovieData> movieDataList;

    private OnMovieItemClickListener onMovieItemClickListener;

    private Cursor cursor;

    private int mode;

    public static final int MODE_DATA_LIST = 0;
    public static final int MODE_CURSOR = 1;

    /**
     * Parameterized constructor with item click listner argument
     *
     * @param onMovieItemClickListener {@link OnMovieItemClickListener}
     */
    public MovieDataAdapter(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;

        movieDataList = new ArrayList<>();
    }

    @Override
    public MovieDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get context
        final Context context = parent.getContext();
        // id for the list item layout
        final int layoutId = R.layout.movie_list_item;
        // inflate view from the layout id
        final View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // create new view holder
        return new MovieDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieDataViewHolder holder, int position) {
        // get context
        final Context context = holder.posterImageView.getContext();

        String title = null;
        switch (getMode()) {
            case MODE_DATA_LIST:
                // retrieve poster path
                final String imagePath = movieDataList.get(position).getPosterPath();
                title = movieDataList.get(position).getTitle();
                // load image using Picasso
                Picasso.with(context).load(NetworkUtils.DEFAULT_ROOT_URL_POSTERS + File.separator + context.getResources().getString(R.string.default_poster_size) + imagePath).into(holder.posterImageView);
                holder.posterTitleTextView.setText(title);
                break;
            case MODE_CURSOR:
                cursor.moveToPosition(position);
                // holder.posterImageView.setImageBitmap(MovieDataCursorUtils.loadImageForCurrentPosition(cursor, FavouriteEntry.INDEX_COLUMN_POSTER));
                Picasso.with(context).load(NetworkUtils.DEFAULT_ROOT_URL_POSTERS + File.separator + context.getResources().getString(R.string.default_poster_size) + cursor.getString(FavouriteEntry.INDEX_COLUMN_POSTER_PATH)).into(holder.posterImageView);
                title = cursor.getString(FavouriteEntry.INDEX_COLUMN_TITLE);
                holder.posterTitleTextView.setText(title);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (getMode()) {
            case MODE_DATA_LIST:
                return movieDataList.size();
            case MODE_CURSOR:
                if (null == cursor) {
                    return 0;
                }

                return cursor.getCount();
            default:
                return 0;
        }

    }

    /**
     * This method is used to set data to model once loaded from async task
     *
     * @param data
     * @param mode
     */
    public void setMovieData(Object data, int mode) {
        setMode(mode);

        switch (getMode()) {
            case MODE_DATA_LIST:
                this.movieDataList.clear();
                final List<MovieData> movieDataList = (List<MovieData>) data;
                if (movieDataList != null && !movieDataList.isEmpty()) {
                    // add all objects if the list is not null and not empty
                    this.movieDataList.addAll(movieDataList);
                }
                break;
            case MODE_CURSOR:
                this.cursor = (Cursor) data;
                break;
            default:
                break;
        }

        // notify about the change of data
        notifyDataSetChanged();
    }

    private int getMode() {
        return mode;
    }

    private void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * This class acts as the view holder for MovieData objects for MovieDataAdapter.
     */
    class MovieDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;

        private TextView posterTitleTextView;

        /**
         * @param itemView
         */
        MovieDataViewHolder(View itemView) {
            super(itemView);

            // find poster image view
            posterImageView = (ImageView) itemView.findViewById(R.id.iv_poster_thumbnail);
            posterTitleTextView = (TextView) itemView.findViewById(R.id.tv_poster_title);

            // set click listener
            posterImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get position
            final int position = getAdapterPosition();
            switch (getMode()) {
                case MODE_DATA_LIST:
                    // propagate events to listener
                    onMovieItemClickListener.onClick(movieDataList.get(position));
                    break;
                case MODE_CURSOR:
                    cursor.moveToPosition(position);
                    // propagate events to listener
                    onMovieItemClickListener.onClick(MovieDataCursorUtils.getDataForCurrentPosition(cursor));
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * This interface defines the behaviour for movie item click listener.
     */
    interface OnMovieItemClickListener {

        /**
         * This method should be implemented by all parties who is interested in item click events
         *
         * @param movieData {@link MovieData}
         */
        void onClick(MovieData movieData);
    }
}
