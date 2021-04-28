package com.example.practice.Message

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class MessageEntity {
    var text: String
    var date: String
    var isSend: Int

    constructor(text: String, date: String, isSend: Int) {
        this.text = text
        this.date = date
        this.isSend = isSend
    }

    @SuppressLint("SimpleDateFormat")
    constructor(message: Message) {
        text = message.text
        date = SimpleDateFormat().format(message.date)
        isSend = if (message.isSend) 1 else 0
    }
}