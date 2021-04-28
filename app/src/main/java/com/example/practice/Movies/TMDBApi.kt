package com.example.practice.Movies

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.Serializable

interface TMDBApi : Serializable {
    @GET("/3/movie/{category}")
    fun listOfMovies(
            @Path("category") category: String?,
            @Query("api_key") apiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int
    ): Call<MovieResults?>?
}