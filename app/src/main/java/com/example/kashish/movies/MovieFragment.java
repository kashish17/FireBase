package com.example.kashish.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.kashish.movies.UpcomingPOJO.UpcomingResponse;
import com.example.kashish.movies.nowShowingPOJO.NowShowingResponse;
import com.example.kashish.movies.popularPOJO.PopularsResponse;
import com.example.kashish.movies.popularPOJO.Result;
import com.example.kashish.movies.topRatedPOJO.TopRatedResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    public  static String POPULAR="popular";
    public  static String TOP_RATED="toprated";
    public  static String UPCOMING="upcoming";
    public  static String ON_SHOWING="onshowing";

    RecyclerView recyclerView;


    MovieAdapter adapter;
    MovieTopRatedAdapter Tadapter;
    BigMovieAdapter bigMovieAdapter;
    UpcomingBigMovieAdapter Upadapter;
    List<Result> popularList = new ArrayList<>();
    List<com.example.kashish.movies.topRatedPOJO.Result> topRatedList = new ArrayList<>();
    List<com.example.kashish.movies.nowShowingPOJO.Result> nowShaowingList=new ArrayList<>();
    List<com.example.kashish.movies.UpcomingPOJO.Result> upcomingList = new ArrayList<>();
    Retrofit retrofit;

    MovieService service;
     LinearLayoutManager linearLayoutManager;
    int page = 1;

    boolean isScrolling=false;
    int currentItems,totalItems,scollOutItems;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_movie, container, false);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        service = retrofit.create(MovieService.class);

        fetchPopularPost(view);
        fetchTopRated(view);
        fetchNowShowing(view);
        fetchUpcoming(view);
        return view;
    }



    public void fetchUpcoming(View view){
        page=1;
        currentItems=0;totalItems=0;scollOutItems=0;
        recyclerView= view.findViewById(R.id.Upcoming);
        Upadapter = new UpcomingBigMovieAdapter(getContext(), upcomingList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {

                com.example.kashish.movies.UpcomingPOJO.Result object=upcomingList.get(position);
                Intent intent=new Intent(getContext(),DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(Upadapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataUp();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling&& (currentItems+scollOutItems==totalItems)){
                    isScrolling=false;
                    page++;
                    getDataUp();
                }

            }
        });
    }

    public void getDataUp( ) {
        Call<UpcomingResponse> call = null;

        call = service.getUpcomingMovies(Contract.API_KEY, page);
        call.enqueue(new Callback<UpcomingResponse>() {
            @Override
            public void onResponse(Call<UpcomingResponse> call, Response<UpcomingResponse> response) {
                upcomingList.addAll(response.body().getResultsUp());
                bigMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UpcomingResponse> call, Throwable t) {

            }
        });

    }

    public void fetchNowShowing(View view){
        page=1;
        currentItems=0;totalItems=0;scollOutItems=0;
        recyclerView= view.findViewById(R.id.NowShaowing);
        bigMovieAdapter = new BigMovieAdapter(getContext(), nowShaowingList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
               com.example.kashish.movies.nowShowingPOJO.Result object=nowShaowingList.get(position);
                Intent intent=new Intent(getContext(),DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(bigMovieAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataNow();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling&& (currentItems+scollOutItems==totalItems)){
                    isScrolling=false;
                    page++;
                    getDataNow();
                }

            }
        });



    }
    public void getDataNow( ){
        Call<NowShowingResponse> call=null;

        call=service.getNowShawingMovies(Contract.API_KEY,page);

       call.enqueue(new Callback<NowShowingResponse>() {
           @Override
           public void onResponse(Call<NowShowingResponse> call, Response<NowShowingResponse> response) {
               nowShaowingList.addAll(response.body().getResultsNow());
               bigMovieAdapter.notifyDataSetChanged();
           }


           @Override
           public void onFailure(Call<NowShowingResponse> call, Throwable t) {

           }
       });
    }



    public void fetchTopRated(View view){
        //page=1;
        currentItems=0;totalItems=0;scollOutItems=0;
        recyclerView= view.findViewById(R.id.TopRated);
        Tadapter = new MovieTopRatedAdapter(getContext(), topRatedList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {

                com.example.kashish.movies.topRatedPOJO.Result object=topRatedList.get(position);
                Intent intent=new Intent(getContext(),DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(Tadapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataTop(TOP_RATED);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling&& (currentItems+scollOutItems==totalItems)){
                    isScrolling=false;
                    page++;
                    getDataTop(TOP_RATED);
                }

            }
        });


    }


    public void getDataTop( String str){
        Call<TopRatedResponse> call=null;

        call=service.getTopRatedMovies(Contract.API_KEY,page);

        call.enqueue(new Callback<TopRatedResponse>() {
            @Override
            public void onResponse(Call<TopRatedResponse> call, Response<TopRatedResponse> response) {
                topRatedList.addAll(response.body().getResultsTop());
                Tadapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<TopRatedResponse> call, Throwable t) {

            }
        });
    }

    public void fetchPopularPost(View view){

        page=1;
        recyclerView= view.findViewById(R.id.Popular);
        adapter = new MovieAdapter(getContext(), popularList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {

                Result object=popularList.get(position);
                Intent intent=new Intent(getContext(),DetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



         linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
     //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



       // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getData(POPULAR);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems=linearLayoutManager.getChildCount();
                totalItems=linearLayoutManager.getItemCount();
                scollOutItems=linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling&& (currentItems+scollOutItems==totalItems)){
                    isScrolling=false;
                    page++;
                    getData(POPULAR);
                }

            }
        });


    }



    public void getData( String str){
        Call<PopularsResponse> call=null;
        if(str.equals("popular"))
            call = service.getPopularMovies(Contract.API_KEY,page);

        call.enqueue(new Callback<PopularsResponse>() {
            @Override
            public void onResponse(Call<PopularsResponse> call, Response<PopularsResponse> response) {

                if(response.body()!=null) {
                    popularList.addAll(response.body().getResults());
                    //  Toast.makeText(getContext(), "List changed", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getContext(), "Response null", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<PopularsResponse> call, Throwable t) {

            }
        });
    }
}
