package com.example.kashish.movies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kashish.movies.Cast.Cast;
import com.example.kashish.movies.Cast.CastResponse;
import com.example.kashish.movies.Details.MovieDetails;
import com.example.kashish.movies.SimilarMovies.SimilarMovieResponse;
import com.example.kashish.movies.Trailers.MovieTrailers;
import com.example.kashish.movies.popularPOJO.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsScrollingActivity extends AppCompatActivity {

    long id;
    int mode;
    TrailersAdapter trailersAdapter;
    SMovieAdapter sMovieAdapter;
    LinearLayoutManager linearLayoutManager;
    CastAdapter castAdapter;


    List<Cast> castList = new ArrayList<>();
    List<com.example.kashish.movies.Trailers.Result> trailerList = new ArrayList<>();
    List<com.example.kashish.movies.SimilarMovies.Result> similarMovieList = new ArrayList<>();
    Retrofit retrofit;
    MovieService service;
    ImageView backD,poster;
    TextView Name,detail,date,time,rate,genre,tagline;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    boolean isShow = true;
    int scrollRange = -1;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout=findViewById(R.id.app_bar);
        collapsingToolbarLayout=findViewById(R.id.Ctoolbar_layout);





        setSupportActionBar(toolbar);

        backD=findViewById(R.id.backDrop);
        poster=findViewById(R.id.poster);

        Name=findViewById(R.id.MovieName);
        detail=findViewById(R.id.overView);
        rate=findViewById(R.id.rateing);
        date=findViewById(R.id.dateM);
        time=findViewById(R.id.timeM);
        genre=findViewById(R.id.genre);
        tagline=findViewById(R.id.tagline);

        Intent intent=getIntent();
        id =intent.getLongExtra("id",0);


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        service = retrofit.create(MovieService.class);
        Call<MovieDetails> call= service.getDetails(id,Contract.API_KEY);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
            Log.d("DetailsActivity","hit");
                if(response.body()!=null){
                    final MovieDetails movieDetails=response.body();

                    String url = Contract.IMAGE_URL + movieDetails.getPosterPath();
                    new Picasso.Builder(DetailsScrollingActivity.this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d(("lalalal"),exception.toString());
                        }
                    }).build().load(url.trim()).error(R.drawable.ic_launcher_foreground).into(poster);

                    String url2 = Contract.IMAGE_URL + movieDetails.getBackdropPath();
                    new Picasso.Builder(DetailsScrollingActivity.this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d(("lalalal"),exception.toString());
                        }
                    }).build().load(url2.trim()).error(R.drawable.ic_launcher_foreground).into(backD);

                    Name.setText(movieDetails.getTitle());
                    rate.setText(movieDetails.getVoteAverage().toString()+rate.getText());
                    detail.setText(movieDetails.getOverview()+"");
                    date.setText(date.getText()+movieDetails.getReleaseDate().toString());
                    time.setText(time.getText()+movieDetails.getRuntime().toString());
                    String str="";

                    for(int i=0;i<movieDetails.getGenres().size();i++){
                        str+=movieDetails.getGenres().get(i).getName()+" ";
                    }
                    genre.setText(str);
                    tagline.setText(movieDetails.getTagline());

                    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                            if (scrollRange == -1) {
                                scrollRange = appBarLayout.getTotalScrollRange();
                            }
                            if (scrollRange + i == 0) {
                                collapsingToolbarLayout.setTitle(movieDetails.getTitle());
                                isShow = true;
                            } else if (isShow) {
                                collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                                isShow = false;
                            }

                        }

                    });

                    getTrailers();
                    getSimilarMovies();
                    getCast();
                }
        }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.i("Error",t.getMessage());
            }
        });


    }
    public  void getCast(){
        recyclerView=findViewById(R.id.Cast);

        castAdapter = new CastAdapter(this, castList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
              Cast object=castList.get(position);
                Intent intent=new Intent(DetailsScrollingActivity.this,DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(castAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<CastResponse> call= service.getCasts(id,Contract.API_KEY);
        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                if(response.body()!=null){
                    castList.addAll(response.body().getCast());
                    castAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {

            }
        });
    }
    public  void getSimilarMovies(){
        recyclerView=findViewById(R.id.SimilarMovies);

        sMovieAdapter = new SMovieAdapter(this, similarMovieList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.SimilarMovies.Result object=similarMovieList.get(position);
                Intent intent=new Intent(DetailsScrollingActivity.this,DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(sMovieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<SimilarMovieResponse> call= service.getSimilarMovies(id,Contract.API_KEY);
       call.enqueue(new Callback<SimilarMovieResponse>() {
           @Override
           public void onResponse(Call<SimilarMovieResponse> call, Response<SimilarMovieResponse> response) {
                if(response.body()!=null) {
                    similarMovieList.addAll(response.body().getResults());
                    sMovieAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(DetailsScrollingActivity.this,"no similar Movies",Toast.LENGTH_SHORT).show();
                }
           }

           @Override
           public void onFailure(Call<SimilarMovieResponse> call, Throwable t) {

           }
       });
    }



    public  void getTrailers(){

        recyclerView=findViewById(R.id.trailers);

        trailersAdapter = new TrailersAdapter(this, trailerList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {

                com.example.kashish.movies.Trailers.Result result=trailerList.get(position);
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + result.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + result.getKey()));
                try {
                   DetailsScrollingActivity.this.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    DetailsScrollingActivity.this.startActivity(webIntent);
                }

            }
        });
        recyclerView.setAdapter(trailersAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<MovieTrailers> call= service.getTrailers(id,Contract.API_KEY);
        call.enqueue(new Callback<MovieTrailers>() {
            @Override
            public void onResponse(Call<MovieTrailers> call, Response<MovieTrailers> response) {
               if(response!=null) {
                   trailerList.addAll(response.body().getResults());
                   trailersAdapter.notifyDataSetChanged();
               }else{
                   Toast.makeText(DetailsScrollingActivity.this,"no similar Movies",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<MovieTrailers> call, Throwable t) {

            }
        });
    }


}
