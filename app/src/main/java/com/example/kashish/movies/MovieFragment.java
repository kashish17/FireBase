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
import android.widget.ImageView;
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

    RecyclerView recyclerViewUp,recyclerViewTop,recyclerViewPop,recyclerViewNow;


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
     LinearLayoutManager linearLayoutManagerUp,linearLayoutManagerTop,linearLayoutManagerPop,linearLayoutManagerNow;


    boolean isScrollingup=false, isScrollingpop=false,isScrollingtop=false,isScrollingnow=false;

    int pageUp = 1;
    int currentItemsUp = 0,totalItemsUp = 0,scollOutItemsUp = 0;

    int pagePop = 1;
    int currentItemsPop = 0,totalItemsPop = 0,scollOutItemsPop = 0;

    int pagetop = 1;
    int currentItemstop = 0,totalItemstop = 0,scollOutItemstop = 0;

    int pagenow = 1;
    int currentItemsnow = 0,totalItemsnow = 0,scollOutItemsnow = 0;

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

        recyclerViewUp= view.findViewById(R.id.Upcoming);
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
        recyclerViewUp.setAdapter(Upadapter);
        recyclerViewUp.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManagerUp = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewUp.setLayoutManager(linearLayoutManagerUp);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataUp();

        recyclerViewUp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollingup = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItemsUp=linearLayoutManagerUp.getChildCount();
                totalItemsUp=linearLayoutManagerUp.getItemCount();
                scollOutItemsUp=linearLayoutManagerUp.findFirstVisibleItemPosition();

                if(isScrollingup&& (currentItemsUp+scollOutItemsUp==totalItemsUp)){
                    isScrollingup=false;
                    pageUp++;
                    getDataUp();
                }

            }
        });
    }

    public void getDataUp( ) {
        Call<UpcomingResponse> call = service.getUpcomingMovies(Contract.API_KEY, pageUp);

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

        recyclerViewNow= view.findViewById(R.id.NowShaowing);
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
        recyclerViewNow.setAdapter(bigMovieAdapter);
        recyclerViewNow.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManagerNow = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewNow.setLayoutManager(linearLayoutManagerNow);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataNow();

        recyclerViewNow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollingnow = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItemsnow=linearLayoutManagerNow.getChildCount();
                totalItemsnow=linearLayoutManagerNow.getItemCount();
                scollOutItemsnow=linearLayoutManagerNow.findFirstVisibleItemPosition();

                if(isScrollingnow&& (currentItemsnow+scollOutItemsnow==totalItemsnow)){
                    isScrollingnow=false;
                    pagenow++;
                    getDataNow();
                }

            }
        });



    }
    public void getDataNow( ){
        Call<NowShowingResponse> call=service.getNowShawingMovies(Contract.API_KEY,pagenow);

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

        recyclerViewTop = view.findViewById(R.id.TopRated);
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
        recyclerViewTop.setAdapter(Tadapter);
        recyclerViewTop.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManagerTop = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewTop.setLayoutManager(linearLayoutManagerTop);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



        // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getDataTop(TOP_RATED);

        recyclerViewTop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollingtop = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItemstop=linearLayoutManagerTop.getChildCount();
                totalItemstop=linearLayoutManagerTop.getItemCount();
                scollOutItemstop=linearLayoutManagerTop.findFirstVisibleItemPosition();

                if(isScrollingtop&& (currentItemstop+scollOutItemstop==totalItemstop)){
                    isScrollingtop=false;
                    pagetop++;
                    getDataTop(TOP_RATED);
                }

            }
        });


    }


    public void getDataTop( String str){
        Call<TopRatedResponse> call = service.getTopRatedMovies(Contract.API_KEY,pagetop);

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


        recyclerViewPop= view.findViewById(R.id.Popular);
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
        recyclerViewPop.setAdapter(adapter);
        recyclerViewPop.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



         linearLayoutManagerPop = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPop.setLayoutManager(linearLayoutManagerPop);
     //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();



       // Toast.makeText(getContext(), "hit done", Toast.LENGTH_SHORT).show();
        getData(POPULAR);

        recyclerViewPop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrollingpop = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItemsPop=linearLayoutManagerPop.getChildCount();
                totalItemsPop=linearLayoutManagerPop.getItemCount();
                scollOutItemsPop=linearLayoutManagerPop.findFirstVisibleItemPosition();

                if(isScrollingpop&& (currentItemsPop+scollOutItemsPop==totalItemsPop)){
                    isScrollingpop=false;
                    pagePop++;
                    getData(POPULAR);
                }

            }
        });


    }



    public void getData( String str){
        Call<PopularsResponse> call=null;
        if(str.equals("popular"))
            call = service.getPopularMovies(Contract.API_KEY,pagePop);

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
