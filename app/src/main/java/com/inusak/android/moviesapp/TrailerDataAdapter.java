package com.inusak.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.TrailerData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading trailer data from models, inflate ui components and binding them to view holders.
 * Created by Nilanka on 3/8/2018.
 */

public class TrailerDataAdapter extends RecyclerView.Adapter<TrailerDataAdapter.TrailerDataViewHolder> {

    private final List<TrailerData> trailerDataList;

    private OnTrailerItemClickListener onTrailerItemClickListener;

    public TrailerDataAdapter(OnTrailerItemClickListener onTrailerItemClickListener) {
        this.onTrailerItemClickListener = onTrailerItemClickListener;

        trailerDataList = new ArrayList<>();
    }

    @Override
    public TrailerDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get context
        final Context context = parent.getContext();
        // get layout id
        final int layoutId = R.layout.trailer_list_item;
        // inflate view from layout
        final View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // create and return new view holder instance
        return new TrailerDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerDataViewHolder holder, int position) {
        // get context
        final Context context = holder.textViewName.getContext();
        // set data
        holder.textViewName.setText(trailerDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerDataList.size();
    }

    /**
     * @param trailerDataList
     */
    public void setData(List<TrailerData> trailerDataList) {
        this.trailerDataList.clear();
        if (trailerDataList != null && !trailerDataList.isEmpty()) {
            // add all objects if the list is not null and not empty
            this.trailerDataList.addAll(trailerDataList);
        }

        // notify about the change of data
        notifyDataSetChanged();
    }

    /**
     * This class acts as the view holder for TrailerData objects for TrailerDataAdapter.
     */
    class TrailerDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageViewTrailer;

        private ImageView imageViewShare;

        private TextView textViewName;

        public TrailerDataViewHolder(View itemView) {
            super(itemView);

            // find view items
            imageViewTrailer = (ImageView) itemView.findViewById(R.id.iv_trailer);
            imageViewShare = (ImageView) itemView.findViewById(R.id.iv_share);
            textViewName = (TextView) itemView.findViewById(R.id.tv_trailer_name);

            // set click listener
            imageViewTrailer.setOnClickListener(this);
            imageViewShare.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            // get position
            final int position = getAdapterPosition();
            // trigger listener
            onTrailerItemClickListener.onClick(trailerDataList.get(position), v);
        }
    }

    /**
     *
     */
    interface OnTrailerItemClickListener {

        /**
         * This method should be implemented by all parties who is interested in item click events
         *
         * @param trailerData {@link TrailerData}
         */
        void onClick(TrailerData trailerData, View view);
    }

}
