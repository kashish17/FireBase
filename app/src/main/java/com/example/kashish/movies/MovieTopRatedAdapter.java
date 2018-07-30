package com.example.kashish.movies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kashish.movies.popularPOJO.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieTopRatedAdapter extends RecyclerView.Adapter<MovieViewHolder> {

    Context context;
    List<com.example.kashish.movies.topRatedPOJO.Result> lists;
    MovieClickListener movieClickListener;

    MovieTopRatedAdapter(Context context, List<com.example.kashish.movies.topRatedPOJO.Result> lists, MovieClickListener movieClickListener){
        this.lists = lists;
        this.context = context;
        this.movieClickListener = movieClickListener;
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout = inflater.inflate(R.layout.row_layout,viewGroup,false);
        return new MovieViewHolder(rowLayout);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, int i) {
        com.example.kashish.movies.topRatedPOJO.Result result = lists.get(i);
        //  Picasso.get().load(Contract.IMAGE_URL+result.getPosterPath()).into(movieViewHolder.imageView);

        Log.d("lalalal", Contract.IMAGE_URL + result.getPosterPath());
        String url = Contract.IMAGE_URL + result.getPosterPath();

        new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d(("lalalal"),exception.toString());
            }
        }).build().load(url.trim()).error(R.drawable.ic_launcher_foreground).into(movieViewHolder.imageView);

        movieViewHolder.textView.setText(result.getTitle());
        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                movieClickListener.onMovieClick(view,movieViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

}
