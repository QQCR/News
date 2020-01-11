package com.example.news.api;

import com.example.news.models.News;
import com.example.news.models.SourceWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country ,
            @Query("apiKey") String apiKey

    );

    @GET("everything")
    Call<News> getNewsSearch(
            @Query("q") String keyword,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey

    );

    @GET("sources")
    Call<SourceWrapper> getSources(
            @Query("country") String country,
            @Query("language") String language,
            @Query("apiKey") String apiKey
    );
}
