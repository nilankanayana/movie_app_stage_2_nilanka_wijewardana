package com.inusak.android.moviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inusak.android.moviesapp.data.ReviewData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles loading review data from models, inflate ui components and binding them to view holders.
 * Created by Nilanka on 3/8/2018.
 */

public class ReviewDataAdapter extends RecyclerView.Adapter<ReviewDataAdapter.ReviewDataViewHolder> {

    private final List<ReviewData> reviewDataList;

    public ReviewDataAdapter() {
        reviewDataList = new ArrayList<>();
    }

    @Override
    public ReviewDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // get context
        final Context context = parent.getContext();
        // get layout id
        final int layoutId = R.layout.review_list_item;
        // inflate view from layout
        final View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        // create and return new view holder instance
        return new ReviewDataAdapter.ReviewDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewDataViewHolder holder, int position) {
        // get context
        final Context context = holder.textViewReview.getContext();
        // set data
        holder.textViewReview.setText(reviewDataList.get(position).getContent());
        holder.textViewReviewer.setText(reviewDataList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewDataList.size();
    }

    /**
     * @param reviewDataList
     */
    public void setData(List<ReviewData> reviewDataList) {
        this.reviewDataList.clear();
        if (reviewDataList != null && !reviewDataList.isEmpty()) {
            // add all objects if the list is not null and not empty
            this.reviewDataList.addAll(reviewDataList);
        }

        // notify about the change of data
        notifyDataSetChanged();
    }

    /**
     * This class acts as the view holder for ReviewData objects for ReviewDataAdapter.
     */
    class ReviewDataViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewReview;

        private TextView textViewReviewer;

        public ReviewDataViewHolder(View itemView) {
            super(itemView);

            // find view items
            textViewReview = (TextView) itemView.findViewById(R.id.tv_review);
            textViewReviewer = (TextView) itemView.findViewById(R.id.tv_reviewer);
        }
    }

}
