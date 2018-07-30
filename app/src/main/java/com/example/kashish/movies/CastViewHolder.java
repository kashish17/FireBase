package com.example.kashish.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CastViewHolder extends RecyclerView.ViewHolder {


    ImageView imageView;
    TextView  textViewRealName;
    TextView textViewMovieName;
    View itemView;


public CastViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView=itemView;
        imageView=itemView.findViewById(R.id.castImage);
        textViewMovieName=itemView.findViewById(R.id.moviename);
        textViewRealName=itemView.findViewById(R.id.realName);

        }

}
