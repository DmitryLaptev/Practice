package com.example.practice.Digit

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.practice.Digit.DigitService.api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer

object DigitToString {
    fun getDigit(city: String?, callback: Consumer<String?>) {
        val api = api
        val call = api.getCurrentDigit(city)
        call!!.enqueue(object : Callback<Digit?> {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onResponse(call: Call<Digit?>, response: Response<Digit?>) {
                val result = response.body()
                if (result != null) {
                    val answer = result.str
                    callback.accept(answer)
                } else callback.accept("Не могу перевести число")
            }

            override fun onFailure(call: Call<Digit?>, t: Throwable) {
                Log.w("DIGIT", t.message!!)
            }
        })
    }
}