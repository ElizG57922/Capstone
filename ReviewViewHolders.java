package com.example.storyapp.reviews;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.R;

public class ReviewViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView review;
    public LinearLayout container;

    public ReviewViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        review=itemView.findViewById(R.id.review);
        container=itemView.findViewById(R.id.container);
    }

    @Override
    public void onClick(View view) {
    }
}
