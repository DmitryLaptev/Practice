package com.example.practice.MainPackage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.practice.AI.answers
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*

object ParsingHtmlService {
    private const val url = "http://mirkosmosa.ru/holiday/2021/"
    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getHoliday(dates: Array<String>): Array<String>? {
        return try {
            val body = Jsoup.connect(url).get().body()
            val elements = body.select(".month_row")
            for (element in elements) {
                val holidayDate = element.select(".month_cel_date span").first().text()
                for (date in dates) {
                    if (date == holidayDate) {
                        val holidays = element.select(".month_cel li a")
                        var result = StringBuilder()
                        for (holiday in holidays) {
                            result.append(holiday.text()).append(", ")
                        }
                        if (result.length > 0) {
                            result = StringBuilder(
                                result.substring(0, result.length - 2).trim { it <= ' ' })
                        }
                        answers.add(date + " - " + if (result.length == 0) "нет праздников;" else "$result;")
                    }
                }
            }
            answers.toArray(arrayOfNulls<String>(0))
        } catch (e: Exception) {
            null
        }
    }

    @Throws(IOException::class)
    fun getWallet(wallet: String): String? {
        return try {
            val body = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get().body()
            val elements = body.getElementsByTag("tbody")
            val table = elements[0]
            val elementsTable = table.children()
            val dollar = elementsTable[1]
            val dollarElem = dollar.children()
            for (i in 1 until elementsTable.size) {
                val wall = elementsTable[i]
                val wallElem = wall.children()
                if (wallet.contains(wallElem[wallElem.size - 2].text().toLowerCase(Locale.ROOT))) {
                    var result = StringBuilder()
                    result.append(wallElem[wallElem.size - 2].text()).append(" ")
                        .append(wallElem[wallElem.size - 1].text())
                    answers.add(result.toString())
                    if (result.length > 0) {
                        result =
                            StringBuilder(result.substring(0, result.length - 2).trim { it <= ' ' })
                    }
                    //answers.add(wallet + " - " + ((result.length() == 0) ? "нет валюты;" : result + ";"));
                }
            }
            Log.d(
                "MyLog",
                "Title:" + dollarElem[dollarElem.size - 2].text() + " " + dollarElem[dollarElem.size - 1].text()
            )
            answers.toString()
        } catch (e: Exception) {
            null
        }
    }
}