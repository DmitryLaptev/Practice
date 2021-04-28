package com.example.practice.Movies

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieService {
    val movies: TMDBApi
        get() {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(TMDBApi::class.java)
        }
}