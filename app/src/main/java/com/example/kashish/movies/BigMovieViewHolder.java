package com.example.kashish.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BigMovieViewHolder extends RecyclerView.ViewHolder{
    ImageView imageView;
    TextView textView;
    View itemView;


    public BigMovieViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        imageView=itemView.findViewById(R.id.bigImage);
        textView=itemView.findViewById(R.id.BigText);
        }
}
