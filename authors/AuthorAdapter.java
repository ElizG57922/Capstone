package com.example.storyapp.authors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storyapp.R;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorViewHolder> {
    private List<Author> authorList;
    private Context context;

    public AuthorAdapter(List<Author> authorList, Context context){
        this.authorList=authorList;
        this.context=context;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_item, null, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(params);
        AuthorViewHolder row = new AuthorViewHolder(layoutView);
        return row;
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        holder.authorName.setText(authorList.get(position).getName());
        holder.authorBio.setText(authorList.get(position).getBio());
        holder.authorID.setText(authorList.get(position).getUserID());
        if(!authorList.get(position).getProfilePicURL().equals("defaultImage")) {
            Glide.with(context).load(authorList.get(position).getProfilePicURL()).into(holder.authorImage);
        }
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }
}