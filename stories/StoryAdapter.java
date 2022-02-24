package com.example.storyapp.stories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storyapp.R;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryViewHolder> {
    private List<Story> storyList;
    private Context context;

    public StoryAdapter(List<Story> storyList, Context context){
        this.storyList=storyList;
        this.context=context;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, null, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(params);
        StoryViewHolder row = new StoryViewHolder(layoutView);
        return row;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        holder.storyName.setText(storyList.get(position).getName());
        holder.storyID.setText(storyList.get(position).getStoryID());
        holder.authorID.setText(storyList.get(position).getAuthorID());
        holder.authorName.setText("By "+storyList.get(position).getAuthorName());
        holder.description.setText(storyList.get(position).getDescription());
      //  if(!storyList.get(position).getProfilePicURL().equals("defaultImage")) {
      //      Glide.with(context).load(storyList.get(position).getProfilePicURL()).into(holder.authorImage);
      //  }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }
}