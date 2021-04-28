package com.example.practice.Digit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable

interface DigitApi : Serializable {
    @GET("/json/convert/num2str?dec=0")
    fun getCurrentDigit(@Query("num") number: String?): Call<Digit?>?
}