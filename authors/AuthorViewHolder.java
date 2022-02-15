package com.example.storyapp.authors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.storyapp.R;
import com.example.storyapp.ViewProfileActivity;


public class AuthorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView authorID, authorName, authorBio;
    public ImageView authorImage;

    public AuthorViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        authorID=itemView.findViewById(R.id.authorID);
        authorName=itemView.findViewById(R.id.authorName);
        authorBio=itemView.findViewById(R.id.authorBio);
        authorImage=itemView.findViewById(R.id.authorImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ViewProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("authorID", authorID.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }
}
