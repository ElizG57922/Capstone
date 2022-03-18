package com.example.storyapp.reviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolders> {
    private List<Review> reviewList;
    private Context context;

    public ReviewAdapter(List<Review> reviewList, Context context){
        this.reviewList=reviewList;
        this.context=context;
    }

    @NonNull
    @Override
    public ReviewViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, null, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(params);
        ReviewViewHolders row = new ReviewViewHolders(layoutView);
        return row;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolders holder, int position) {
        holder.review.setText(reviewList.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return this.reviewList.size();
    }
}
