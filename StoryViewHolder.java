package com.example.storyapp.stories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.R;
import com.example.storyapp.ViewStoryActivity;


public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView storyID, storyName, authorID, authorName, description;

    public StoryViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        storyID=itemView.findViewById(R.id.storyID);
        storyName=itemView.findViewById(R.id.storyName);
        authorID=itemView.findViewById(R.id.authorID);
        authorName=itemView.findViewById(R.id.authorName);
        description=itemView.findViewById(R.id.desc);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ViewStoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("storyID", storyID.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
