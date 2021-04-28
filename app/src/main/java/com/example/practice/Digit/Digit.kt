package com.example.practice.Digit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Digit : Serializable {
    @SerializedName("str")
    @Expose
    val str: String? = null
}