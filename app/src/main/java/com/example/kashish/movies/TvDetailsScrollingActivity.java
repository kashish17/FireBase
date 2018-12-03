package com.example.kashish.movies;

import android.arch.persistence.room.Room;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kashish.movies.Cast.Cast;
import com.example.kashish.movies.Cast.CastResponse;
import com.example.kashish.movies.RoomDatabase.AppDatabase;
import com.example.kashish.movies.RoomDatabase.MovieTable;
import com.example.kashish.movies.RoomDatabase.TvTable;
import com.example.kashish.movies.SimilarMovies.SimilarMovieResponse;
import com.example.kashish.movies.SimilarTvShows.SimilarTvShowResponse;
import com.example.kashish.movies.Trailers.MovieTrailers;
import com.example.kashish.movies.TvCastDetils.TvCastResponse;
import com.example.kashish.movies.TvDetails.TvDetailsResponse;
import com.example.kashish.movies.TvTrailers.Result;
import com.example.kashish.movies.TvTrailers.TvTrailersResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvDetailsScrollingActivity extends AppCompatActivity {

    Long id;
    Retrofit retrofit;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;
    MovieService service;
    TextView castTv,similarTv,trailersTv;
    String titleT,genreT,homepageT;

    List<com.example.kashish.movies.TvCastDetils.Cast> tvcastList = new ArrayList<>();
    List<Result> tvtrailersList = new ArrayList<>();
    List<com.example.kashish.movies.SimilarTvShows.Result> tvSimilarList = new ArrayList<>();



    TvCastAdapter tvCastAdapter;
    TvTrailersAdapter tvTrailersAdapter;
    SimilarShowsAdapter similarShowsAdapter;
    LinearLayoutManager linearLayoutManager;
    ImageView backD,poster;
    boolean isShow = true;
    int scrollRange = -1;
    TextView Name,detail,date,time,rate,genre,seasons,episodes;

    AppDatabase appDatabase;
    ImageButton heartTv, shareTv;
    boolean isLiked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_details_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        appDatabase= Room.databaseBuilder(this,AppDatabase.class,"movie_db").allowMainThreadQueries().build();

        heartTv=findViewById(R.id.addTv);
        shareTv=findViewById(R.id.shareTv);



        backD=findViewById(R.id.backDropTv);
        poster=findViewById(R.id.posterTv);

        castTv=findViewById(R.id.castTextView);
        similarTv=findViewById(R.id.similarTextView);
        trailersTv=findViewById(R.id.trailersTextView);
        Name=findViewById(R.id.tvName);
        detail=findViewById(R.id.overViewTv);
        rate=findViewById(R.id.rateingtv);
        date=findViewById(R.id.dateTv);
        time=findViewById(R.id.timeTv);
        genre=findViewById(R.id.genreTv);
        seasons=findViewById(R.id.noOfSeasons);
        episodes=findViewById(R.id.noOfEpisodes);

        appBarLayout=findViewById(R.id.app_barTv);
        collapsingToolbarLayout=findViewById(R.id.toolbar_layoutTv);

        Intent intent=getIntent();
        id =intent.getLongExtra("id",0);

        boolean check=appDatabase.getdao().getIsMarkedTv(id);
        isLiked=check;
        if(check){
            heartTv.setImageDrawable(TvDetailsScrollingActivity.this.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/tv/")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        service = retrofit.create(MovieService.class);

        Call<TvDetailsResponse> call = service.getTVDetails(id,Contract.API_KEY);

        call.enqueue(new Callback<TvDetailsResponse>() {
            @Override
            public void onResponse(Call<TvDetailsResponse> call, Response<TvDetailsResponse> response) {
                if(response.body()!=null){


                    final TvDetailsResponse tvDetailsResponse=response.body();
                    String url = Contract.IMAGE_URL + tvDetailsResponse.getPosterPath();
                    new Picasso.Builder(TvDetailsScrollingActivity.this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d(("lalalal"),exception.toString());
                        }
                    }).build().load(url.trim()).error(R.drawable.ic_launcher_foreground).into(poster);

                    String url2 = Contract.IMAGE_URL + tvDetailsResponse.getBackdropPath();
                    new Picasso.Builder(TvDetailsScrollingActivity.this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            Log.d(("lalalal"),exception.toString());
                        }
                    }).build().load(url2.trim()).error(R.drawable.ic_launcher_foreground).into(backD);

                    if(tvDetailsResponse.getHomepage()!=null)
                    homepageT=tvDetailsResponse.getHomepage().toString();

                    Name.setText(tvDetailsResponse.getName());
                    titleT=Name.getText().toString();
                    rate.setText(tvDetailsResponse.getVoteAverage().toString()+rate.getText());
                    detail.setText(tvDetailsResponse.getOverview()+"");
                    if(tvDetailsResponse.getGenres().size()!=0){
                        genre.setVisibility(View.VISIBLE);
                        String str="";

                        for(int i=0;i<tvDetailsResponse.getGenres().size();i++){
                            str+=tvDetailsResponse.getGenres().get(i).getName()+" ";
                        }
                        genre.setText(str);
                        genreT=str;
                    }

                    if(tvDetailsResponse.getFirstAirDate()!=null){
                        date.setVisibility(View.VISIBLE);
                        date.setText(date.getText()+"        "+tvDetailsResponse.getFirstAirDate()+"");
                    }

                    if(tvDetailsResponse.getEpisodeRunTime().size()!=0){
                        time.setVisibility(View.VISIBLE);
                        time.setText(time.getText()+"         "+tvDetailsResponse.getEpisodeRunTime().get(0).toString()+" mins");
                    }
                    if(tvDetailsResponse.getNumberOfEpisodes()!=null){
                        episodes.setVisibility(View.VISIBLE);
                        episodes.setText(episodes.getText()+":          "+tvDetailsResponse.getNumberOfEpisodes()+"");
                        }
                    if(tvDetailsResponse.getNumberOfSeasons()!=null){
                        seasons.setVisibility(View.VISIBLE);
                        seasons.setText(seasons.getText()+":          "+tvDetailsResponse.getNumberOfSeasons()+"");
                    }

                    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                            if (scrollRange == -1) {
                                scrollRange = appBarLayout.getTotalScrollRange();
                            }
                            if (scrollRange + i == 0) {
                                collapsingToolbarLayout.setTitle(tvDetailsResponse.getName());
                                isShow = true;
                            } else if (isShow) {
                                collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                                isShow = false;
                            }

                        }

                    });

                    shareTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ArrayList<String> data= new ArrayList<>();
                            data.add(titleT);
                            data.add(genreT);
                            data.add(homepageT);
                            String data1= TextUtils.join("\n",data);
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(intent.EXTRA_TEXT,data1);
                            startActivity(intent);
                        }
                    });

                    heartTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(isLiked==false) {
                                TvTable tvTable=new TvTable();
                                tvTable.setTvId(tvDetailsResponse.getId());
                                tvTable.setTvTitle(tvDetailsResponse.getName());
                                tvTable.setTvPosterPath(tvDetailsResponse.getPosterPath());

                                isLiked=true;
                                heartTv.setImageDrawable(TvDetailsScrollingActivity.this.getResources().getDrawable(R.drawable.ic_favorite_red_24dp));
                                tvTable.setLiked(true);
                                appDatabase.getdao().addTv(tvTable);
                                Toast.makeText(TvDetailsScrollingActivity.this,"added to favorites",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                // heart.setImageDrawable(DetailsScrollingActivity.this.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));

                                TvTable tvTable=new TvTable();
                                tvTable.setTvId(tvDetailsResponse.getId());
                                appDatabase.getdao().deleteTv(tvTable);
                            }
                        }
                    });


                    getdataCast();
                    getdataTvTrailers();
                    getdataSimilarShows();
                }

            }

            @Override
            public void onFailure(Call<TvDetailsResponse> call, Throwable t) {

            }
        });


    }


    public void getdataTvTrailers() {


        recyclerView=findViewById(R.id.TrailersTv);

        tvTrailersAdapter = new TvTrailersAdapter(this, tvtrailersList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                Result result=tvtrailersList.get(position);
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + result.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + result.getKey()));
                try {
                    TvDetailsScrollingActivity.this.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    TvDetailsScrollingActivity.this.startActivity(webIntent);
                }
            }
        });
        recyclerView.setAdapter(tvTrailersAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<TvTrailersResponse> call= service.getTVTrailers(id,Contract.API_KEY);
        call.enqueue(new Callback<TvTrailersResponse>() {
            @Override
            public void onResponse(Call<TvTrailersResponse> call, Response<TvTrailersResponse> response) {
                if(response!=null&& response.body().getResults().size()!=0) {

                  //  recyclerView.setVisibility(View.VISIBLE);
                    trailersTv.setVisibility(View.VISIBLE);
                    tvtrailersList.addAll(response.body().getResults());
                    tvTrailersAdapter.notifyDataSetChanged();
                }else{
                    // Toast.makeText(DetailsScrollingActivity.this,"no similar Movies",Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<TvTrailersResponse> call, Throwable t) {

            }
        });


    }

    public void getdataSimilarShows() {

        recyclerView=findViewById(R.id.SimilarTVShows);
        similarShowsAdapter=new SimilarShowsAdapter(this, tvSimilarList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.SimilarTvShows.Result object=tvSimilarList.get(position);
                Intent intent=new Intent(TvDetailsScrollingActivity.this,TvDetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);

            }
        });


        recyclerView.setAdapter(similarShowsAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<SimilarTvShowResponse> call= service.getTvSimilar(id,Contract.API_KEY);
       call.enqueue(new Callback<SimilarTvShowResponse>() {
           @Override
           public void onResponse(Call<SimilarTvShowResponse> call, Response<SimilarTvShowResponse> response) {
               if(response.body()!=null&&response.body().getResults().size()!=0) {

                   recyclerView.setVisibility(View.VISIBLE);
                   similarTv.setVisibility(View.VISIBLE);
                   tvSimilarList.addAll(response.body().getResults());

                   similarShowsAdapter.notifyDataSetChanged();
               }
           }



           @Override
           public void onFailure(Call<SimilarTvShowResponse> call, Throwable t) {

           }
       });
    }



    public void getdataCast() {

        recyclerView = findViewById(R.id.CastTv);

        tvCastAdapter = new TvCastAdapter(this, tvcastList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.TvCastDetils.Cast object = tvcastList.get(position);
                Intent intent = new Intent(TvDetailsScrollingActivity.this, CastDetailActivity.class);
                intent.putExtra("id", object.getId());
                intent.putExtra("mode", 0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(tvCastAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));


        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        service = retrofit.create(MovieService.class);
        Call<TvCastResponse> call = service.getTvCasts(id, Contract.API_KEY);
        call.enqueue(new Callback<TvCastResponse>() {
            @Override
            public void onResponse(Call<TvCastResponse> call, Response<TvCastResponse> response) {
                if(response.body()!=null&&response.body().getCast().size()!=0){
                    castTv.setVisibility(View.VISIBLE);
                   // recyclerView.setVisibility(View.VISIBLE);


                    tvcastList.addAll(response.body().getCast());
                    tvCastAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TvCastResponse> call, Throwable t) {

            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_tv_details_scrolling, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
