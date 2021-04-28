package com.example.practice.Movies

import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.function.Consumer

object MovieToString {
    var PAGE = 1
    var API_KEY = "00fcd4f05d22b709aa4800d59bb5c8f0"
    var LANGUAGE = "en-US"
    var CATEGORY = "popular"
    fun getMovie(movie: String?, callback: Consumer<String?>) {
        val myInterface: TMDBApi = MovieService.movies
        val call = myInterface.listOfMovies(CATEGORY, API_KEY, LANGUAGE, PAGE)
        call!!.enqueue(object : Callback<MovieResults?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<MovieResults?>, response: Response<MovieResults?>) {
                val results = response.body()
                if (results != null) {
                    val listOfMovies: List<ResultsBean>? = results.results
                    val random = Random()
                    val k = listOfMovies?.let { random.nextInt(it.size) }
                    val firstMovie = k?.let { listOfMovies[it] }
                    if (firstMovie != null) {
                        var age = ""
                        age = if (firstMovie.isAdult) "18+" else "0+"
                        val result = "Фильм: " + firstMovie.title.toString() +"\n"+ "Дата релиза: " + firstMovie.release_date.toString() + "\n" + "Язык: " + firstMovie.original_language.toString()  + "\n" + "Возраст: " + age
                        callback.accept(result)
                    }
                } else callback.accept("Не могу перевести число")
            }

            override fun onFailure(call: Call<MovieResults?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}