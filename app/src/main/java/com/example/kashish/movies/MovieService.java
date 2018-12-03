package com.example.kashish.movies;

import com.example.kashish.movies.AiringToday.AiringTodayResponse;
import com.example.kashish.movies.Cast.Cast;
import com.example.kashish.movies.Cast.CastResponse;
import com.example.kashish.movies.Details.MovieDetails;
import com.example.kashish.movies.People.PeopleResponse;
import com.example.kashish.movies.PeopleMovieCreditd.PeopleCreditMivieResponse;
import com.example.kashish.movies.PeopleTvCredits.PeopleTvCreditResponse;
import com.example.kashish.movies.SimilarMovies.SimilarMovieResponse;
import com.example.kashish.movies.SimilarTvShows.SimilarTvShowResponse;
import com.example.kashish.movies.TV.TVOnTheAir.OnTheAirResponse;
import com.example.kashish.movies.TVPopular.TVPopularResponse;
import com.example.kashish.movies.Trailers.MovieTrailers;
import com.example.kashish.movies.TvCastDetils.TvCastResponse;
import com.example.kashish.movies.TvDetails.TvDetailsResponse;
import com.example.kashish.movies.TvTopRated.TvTopRatedResponse;
import com.example.kashish.movies.TvTrailers.TvTrailersResponse;
import com.example.kashish.movies.UpcomingPOJO.UpcomingResponse;
import com.example.kashish.movies.nowShowingPOJO.NowShowingResponse;
import com.example.kashish.movies.popularPOJO.PopularsResponse;
import com.example.kashish.movies.topRatedPOJO.TopRatedResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("popular")
    Call<PopularsResponse> getPopularMovies(@Query("api_key") String str,@Query("page")int page);

    @GET("top_rated")
    Call<TopRatedResponse> getTopRatedMovies(@Query("api_key") String str, @Query("page")int page);

    @GET("now_playing")
    Call<NowShowingResponse> getNowShawingMovies(@Query("api_key") String str, @Query("page")int page);

    @GET("upcoming")
    Call<UpcomingResponse> getUpcomingMovies(@Query("api_key") String str, @Query("page")int page);

    @GET("{id}")
    Call<MovieDetails> getDetails(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/videos")
    Call<MovieTrailers> getTrailers(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/similar")
    Call<SimilarMovieResponse> getSimilarMovies(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/credits")
    Call<CastResponse> getCasts(@Path("id") long i, @Query("api_key") String str);


    @GET("airing_today")
    Call<AiringTodayResponse> getAiringToday(@Query("api_key") String str, @Query("page")int page);

    @GET("top_rated")
    Call<TvTopRatedResponse> gettop_rated(@Query("api_key") String str, @Query("page")int page);

    @GET("on_the_air")
    Call<OnTheAirResponse> geton_the_air(@Query("api_key") String str, @Query("page")int page);

    @GET("airing_today")
    Call<TVPopularResponse> getPopular(@Query("api_key") String str, @Query("page")int page);

    @GET("{id}")
    Call<TvDetailsResponse> getTVDetails(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/credits")
    Call<TvCastResponse> getTvCasts(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/videos")
    Call<TvTrailersResponse> getTVTrailers(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/similar")
    Call<SimilarTvShowResponse> getTvSimilar(@Path("id") long i, @Query("api_key") String str);


    @GET("{id}")
    Call<PeopleResponse> getpeople(@Path("id") long i, @Query("api_key") String str);

    @GET("{id}/movie_credits")
    Call<PeopleCreditMivieResponse> getPeopleMovie(@Path("id") long i, @Query("api_key") String str);


    @GET("{id}/tv_credits")
    Call<PeopleTvCreditResponse> getPeopleTv(@Path("id") long i, @Query("api_key") String str);


}
