package com.example.kashish.movies;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kashish.movies.Cast.Cast;
import com.example.kashish.movies.popularPOJO.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastViewHolder> {

    Context context;
    List<Cast> lists;
    MovieClickListener movieClickListener;

    CastAdapter (Context context, List<Cast> lists, MovieClickListener movieClickListener){
        this.lists = lists;
        this.context = context;
        this.movieClickListener = movieClickListener;
    }
    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowLayout = inflater.inflate(R.layout.cast_layout,viewGroup,false);
        return new CastViewHolder(rowLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastViewHolder castViewHolder, int i) {
        Cast cast= lists.get(i);
        //  Picasso.get().load(Contract.IMAGE_URL+result.getPosterPath()).into(movieViewHolder.imageView);

       // Log.d("lalalal", Contract.IMAGE_URL + cast.getProfilePath());
        String url = Contract.IMAGE_URL + cast.getProfilePath();

        new Picasso.Builder(context).listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                Log.d(("lalalal"),exception.toString());
            }
        }).build().load(url.trim()).error(R.drawable.ic_launcher_foreground).into(castViewHolder.imageView);

        castViewHolder.textViewRealName.setText(cast.getName());
        castViewHolder.textViewMovieName.setText(cast.getCharacter());
        castViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                movieClickListener.onMovieClick(view,castViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
