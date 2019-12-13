package com.example.project_news.api;

import com.example.project_news.model.news;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apiinterface {

    @GET("top-headlines")
    Call<news> getNews(
            @Query("country") String country ,
            @Query("category") String category ,
            @Query("apiKey") String apiKey

    );
    @GET("everything")
    Call<news> getnewsSearch(
            @Query("q") String keyword,
//            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apikey
    );

}
