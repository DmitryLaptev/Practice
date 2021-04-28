package com.example.practice

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.practice.MainPackage.ParsingHtmlService
import com.example.practice.Movies.MovieToString
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.jvm.Throws

object AI {
    var map =
        HashMap<String, String>()
    var answers = ArrayList<String>()
    var isIn = false

    //MovieAPI
    var PAGE = 1
    var API_KEY = "00fcd4f05d22b709aa4800d59bb5c8f0"
    var LANGUAGE = "en-US"
    var CATEGORY = "popular"

    @SuppressLint("NewApi", "WeekBasedYear")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Throws(ParseException::class)
    fun getAnswer(
        question: String,
        callback: Consumer<String?>
    ) {
        var question = question
        answers.clear()
        val c = Calendar.getInstance()
        question = question.toLowerCase()
        var key = -1
        val s = arrayOf(
            "привет",
            "как дела",
            "чем занимаешься",
            "какой сегодня день",
            "который час",
            "какой день недели",
            "погода в городе"
        )
        val cityPattern = Pattern.compile(
            "погода в городе (\\p{L}+)",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = cityPattern.matcher(question)
        /*if (matcher.find()) {
            val cityName = matcher.group(1)
            //answers.add("Не знаю я, какая там погода у вас в городе "+ cityName);
            ForecastToString.getForecast(
                cityName,
                object : Consumer<String?> {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    override fun accept(t: String?) {
                        //Forecast result = null;
                        //String answer = "сейчас где-то " + result.current.temperature + "градуса " + " и " + result.current.weather_descriptions.get(0);
                        callback.accept(t)
                        isIn = true
                        answers.size
                    }
                })
        }*/
        /*Pattern digitPattern = Pattern.compile("(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher digitMatcher = digitPattern.matcher(question);
        if (digitMatcher.find()){
            String digitName = digitMatcher.group(1);
            //answers.add("Не знаю я, какая там погода у вас в городе "+ cityName);
            DigitToString.getDigit(digitName, new Consumer<String>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void accept(String digit) {
                    callback.accept(digit);
                    isIn=true;
                    answers.size();
                }
            });
        }*/if (question.contains(s[0])) {
            //map.put(s[0], "Привет");
            answers.add("Привет")
            Observable.fromCallable<Any> { "Привет" }
            key = 0
        }
        if (question.contains(s[1])) {
            //map.put(s[1], "Неплохо");
            answers.add("Неплохо")
            Observable.fromCallable<Any> { "Неплохо" }
            key = 1
        }
        if (question.contains(s[2])) {
            //map.put(s[2], "Отвечаю на вопросы");
            Observable.fromCallable<Any> { "Отвечаю на вопросы" }
            key = 2
        }
        if (question.contains(s[3])) {
            @SuppressLint("SimpleDateFormat") val dateFormat =
                SimpleDateFormat("dd:MM:YYYY")
            //map.put(s[3], dateFormat.format(new Date()));
            answers.add(dateFormat.format(Date()))
            key = 3
        }
        if (question.contains(s[4])) {
            @SuppressLint("SimpleDateFormat") val dateFormat =
                SimpleDateFormat("hh:mm:ss")
            //map.put(s[4], dateFormat.format(new Date()));
            answers.add(dateFormat.format(Date()))
            key = 4
        }
        if (question.contains(s[5])) {
            val calendar = Calendar.getInstance()
            c.time = Date()
            //map.put(s[5], String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
            answers.add(calendar[Calendar.DAY_OF_WEEK].toString())
            key = 5
        }
        /*if (key!=-1) return map.get(s[key]);
        else return "Я вас не понимаю";*/
        /*if (answers.size() == 0 && !isIn) {
            answers.add("Я вас не понимаю");
        }*/
        //callback.accept(map.get(key));
        val result = answers.toString()
        //return result;
        //callback.accept(answers.toString())
        if (question.contains("праздник")) {
            //String[] findDate = getDate(question);
            //ArrayList<String> dates=new ArrayList<>();
            /*new AsyncTask<String, Integer, String>() {
                @Override
                protected void onPostExecute(String s) {
                    callback.accept(s);
                }

                @Override
                protected String doInBackground(String... strings) {
                    String result = "";
                    for (String string : strings) {
                        try {
                            result = ParsingHtmlService.getHoliday(string);
                            answers.add(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return result;
                }
            }.execute(findDate.split(","));*/
            val finalQuestion = question
            /*Observable.fromCallable {
                val dates = getDate(finalQuestion.substring("праздник".length).trim { it <= ' ' })
                val result = ParsingHtmlService.getHoliday(dates)
                java.lang.String.join("\n", result)
            }
                    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: String? ->
                    callback.accept(
                        t
                    )

                }*/
        }
        if (question.contains("фильм")) {
            MovieToString.getMovie("фильм", object : Consumer<String?> {
                override fun accept(t: String?) {
                    callback.accept(t)
                    isIn = true
                    answers.size
                }
            })
        }
        if (question.contains("валюта")) {
            val finalQuestion = question
            Observable.fromCallable {
                val result = ParsingHtmlService.getWallet(finalQuestion)
                java.lang.String.join("\n", result)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t: String? ->
                    callback.accept(
                        t
                    )
                }
        }
    }

    @SuppressLint("DefaultLocale", "SimpleDateFormat")
    @Throws(ParseException::class)
    fun getDate(question: String): Array<String> {
        @SuppressLint("SimpleDateFormat") val dateFormat1 =
            SimpleDateFormat("dd MMMM yyyy")
        @SuppressLint("SimpleDateFormat") val dateFormat2 =
            SimpleDateFormat("dd.MM.yyyy")
        val dates = question.split(", ".toRegex()).toTypedArray()
        val result = ArrayList<String>()
        for (date in dates) {
            if (date.contains("сегодня")) {
                result.add(String.format("%1\$td %1\$tB %1\$tY", Date()))
            }
            if (date.contains("вчера")) {
                result.add(
                    String.format(
                        "%1\$td %1\$tB %1\$tY",
                        Date(Date().time - 1000 * 60 * 60 * 24)
                    )
                )
            }
            if (date.contains("завтра")) {
                result.add(
                    String.format(
                        "%1\$td %1\$tB %1\$tY",
                        Date(Date().time + 1000 * 60 * 60 * 24)
                    )
                )
            }
            try {
                result.add(String.format("%1\$td %1\$tB %1\$tY", dateFormat1.parse(date)))
            } catch (e: Exception) {
                try {
                    result.add(
                        String.format(
                            "%1\$td %1\$tB %1\$tY",
                            dateFormat2.parse(date)
                        )
                    )
                } catch (e2: Exception) {
                }
            }
            if (result.size > 0 && result[result.size - 1].startsWith("0")) {
                result[result.size - 1] = result[result.size - 1].substring(1)
            }
        }
        return result.toTypedArray()
    }
}