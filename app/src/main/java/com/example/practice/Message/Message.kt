package com.example.practice.Message

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Message : Serializable {
    var text: String
    var date: Date? = null
    var isSend: Boolean

    constructor(text: String, isSend: Boolean) {
        this.text = text
        this.isSend = isSend
        date = Date()
    }

    @SuppressLint("SimpleDateFormat")
    constructor(entity: MessageEntity) {
        text = entity.text
        try {
            date = SimpleDateFormat().parse(entity.date)
        } catch (e: ParseException) {
            date = Date()
        }
        isSend = entity.isSend === 1
    }

    constructor(text: String, date: Date?, isSend: Boolean) {
        this.text = text
        this.date = date
        this.isSend = isSend
    }
}