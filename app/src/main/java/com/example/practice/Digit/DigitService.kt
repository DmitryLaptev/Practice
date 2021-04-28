package com.example.practice.Digit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DigitService {
    val api: DigitApi
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://htmlweb.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(DigitApi::class.java)
        }
}