package com.example.kashish.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.kashish.movies.AiringToday.AiringTodayResponse;
import com.example.kashish.movies.TV.TVOnTheAir.OnTheAirResponse;
import com.example.kashish.movies.TVPopular.TVPopularResponse;
import com.example.kashish.movies.TvTopRated.TvTopRatedResponse;
import com.example.kashish.movies.popularPOJO.PopularsResponse;
import com.example.kashish.movies.popularPOJO.Result;

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
public class TvFragment extends Fragment {

    RecyclerView recyclerView;

    AiringTodayApdapter airingTodayApdapter;
    OnTheAirAdapter onTheAirAdapter;
    TvPopularAdapter tvPopularAdapter;
    TvTopRatedAdapter tvTopRatedAdapter;

    int page=1;
    int currentItems=0,scollOutItems=0,totalItems=0;
    boolean isScrolling;

    List<com.example.kashish.movies.TV.TVOnTheAir.Result> onTheAirList = new ArrayList<>();
    List<com.example.kashish.movies.TVPopular.Result> tvPopularList=new ArrayList<>();
    List<com.example.kashish.movies.AiringToday.Result> tvAiringTodayist = new ArrayList<>();
    List<com.example.kashish.movies.TvTopRated.Result> tvTopRatedList=new ArrayList<>();


    Retrofit retrofit;

    MovieService service;
    LinearLayoutManager linearLayoutManager;

    public TvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // Toast.makeText(getContext(),"tv",Toast.LENGTH_SHORT).show();

        View view= inflater.inflate(R.layout.fragment_tv, container, false);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/tv/")
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.build();

        service = retrofit.create(MovieService.class);


        fetchAiringToday(view);
        fetchTvTopRated(view);
        fetchOnTheAir(view);
        fetchTvPopular(view);

      return  view;


    }

    public void fetchAiringToday(View view){
         page=1;
         currentItems=0;scollOutItems=0;totalItems=0;
         recyclerView =view.findViewById(R.id.airToday);
        airingTodayApdapter =new AiringTodayApdapter(getContext(), tvAiringTodayist, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.AiringToday.Result object=tvAiringTodayist.get(position);
                Intent intent=new Intent(getContext(),TvDetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(airingTodayApdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));



        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();
        getDataAiringToday();
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
                   getDataAiringToday();
                }

            }
        });

    }


    public void getDataAiringToday(){
        Call<AiringTodayResponse> call=null;

            call = service.getAiringToday(Contract.API_KEY,page);

            call.enqueue(new Callback<AiringTodayResponse>() {
                @Override
                public void onResponse(Call<AiringTodayResponse> call, Response<AiringTodayResponse> response) {
                    if(response.body()!=null){
                        tvAiringTodayist.addAll(response.body().getResults());
                        airingTodayApdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<AiringTodayResponse> call, Throwable t) {

                }
            });


    }


    public void fetchTvTopRated(View view) {

        page = 1;
        currentItems = 0;
        scollOutItems = 0;
        totalItems = 0;
        recyclerView = view.findViewById(R.id.TopRatedTV);

        tvTopRatedAdapter = new TvTopRatedAdapter(getContext(), tvTopRatedList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.TvTopRated.Result object=tvTopRatedList.get(position);
                Intent intent=new Intent(getContext(),TvDetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());

                startActivity(intent);

            }
        });
        recyclerView.setAdapter(tvTopRatedAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();
        getdataTvTopRated();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scollOutItems == totalItems)) {
                    isScrolling = false;
                    page++;
                    getdataTvTopRated();
                }

            }
        });
    }
        public void getdataTvTopRated(){
            Call<TvTopRatedResponse> call=null;

            call = service.gettop_rated(Contract.API_KEY,page);

            call.enqueue(new Callback<TvTopRatedResponse>() {
                @Override
                public void onResponse(Call<TvTopRatedResponse> call, Response<TvTopRatedResponse> response) {
                    if(response.body()!=null){
                        tvTopRatedList.addAll(response.body().getResults());
                        tvTopRatedAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<TvTopRatedResponse> call, Throwable t) {

                }
            });



        }




    public void fetchOnTheAir(View view){

        page = 1;
        currentItems = 0;
        scollOutItems = 0;
        totalItems = 0;
        recyclerView = view.findViewById(R.id.onTheair);

       onTheAirAdapter=new OnTheAirAdapter(getContext(), onTheAirList, new MovieClickListener() {
           @Override
           public void onMovieClick(View view, int position) {
               com.example.kashish.movies.TV.TVOnTheAir.Result object=onTheAirList.get(position);
               Intent intent=new Intent(getContext(),TvDetailsScrollingActivity.class);
               intent.putExtra("id",object.getId());
               intent.putExtra("mode",0);
               startActivity(intent);
           }
       });


        recyclerView.setAdapter(onTheAirAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();
        getdataOnTheAir();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scollOutItems == totalItems)) {
                    isScrolling = false;
                    page++;
                    getdataOnTheAir();
                }

            }
        });
    }

    public void getdataOnTheAir(){
        Call<OnTheAirResponse> call=null;

        call = service.geton_the_air(Contract.API_KEY,page);

       call.enqueue(new Callback<OnTheAirResponse>() {
           @Override
           public void onResponse(Call<OnTheAirResponse> call, Response<OnTheAirResponse> response) {
               if(response.body()!=null){
                   onTheAirList.addAll(response.body().getResults());
                   onTheAirAdapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onFailure(Call<OnTheAirResponse> call, Throwable t) {

           }
       });


    }


    public void fetchTvPopular(View view){

        page = 1;
        currentItems = 0;
        scollOutItems = 0;
        totalItems = 0;
        recyclerView = view.findViewById(R.id.PopularTV);

        tvPopularAdapter =new TvPopularAdapter(getContext(), tvPopularList, new MovieClickListener() {
            @Override
            public void onMovieClick(View view, int position) {
                com.example.kashish.movies.TVPopular.Result object=tvPopularList.get(position);
                Intent intent=new Intent(getContext(),TvDetailsScrollingActivity.class);
                intent.putExtra("id",object.getId());
                intent.putExtra("mode",0);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(tvPopularAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //   Toast.makeText(getContext(), "Home Fragment Loaded", Toast.LENGTH_SHORT).show();
        getdataPopular();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scollOutItems == totalItems)) {
                    isScrolling = false;
                    page++;
                    getdataPopular();
                }

            }
        });

    }

    public void getdataPopular(){
        Call<TVPopularResponse> call=null;

        call = service.getPopular(Contract.API_KEY,page);

       call.enqueue(new Callback<TVPopularResponse>() {
           @Override
           public void onResponse(Call<TVPopularResponse> call, Response<TVPopularResponse> response) {
               if(response.body()!=null){
                   tvPopularList.addAll(response.body().getResults());
                   tvPopularAdapter.notifyDataSetChanged();
               }
           }

           @Override
           public void onFailure(Call<TVPopularResponse> call, Throwable t) {

           }
       });

    }


}
