package com.example.practice

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.MainPackage.DBHelper
import com.example.practice.Message.Message
import com.example.practice.Message.MessageEntity
import com.example.practice.Message.MessageListAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import kotlin.jvm.Throws
class MainActivity : AppCompatActivity() {
    protected var sendButton: Button? = null
    protected var questionText: EditText? = null
    protected var chatMessageList: RecyclerView? = null
    protected var textToSpeech: TextToSpeech? = null
    protected lateinit var messageArray: Array<String?>
    protected var callback: Consumer<String>? = null
    protected var messageListAdapter: MessageListAdapter? = null
    var sPref: SharedPreferences? = null
    var name = "undefined"
    private var isLight = true
    private val THEME = "THEME"
    var dbHelper: DBHelper? = null
    var database: SQLiteDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        sPref = getSharedPreferences(
            APP_PREFERENCES,
            Context.MODE_PRIVATE
        )
        isLight = sPref?.getBoolean(THEME, true)!!
        if (!isLight) {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        } else {
            delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendButton = findViewById(R.id.sendButton)
        questionText = findViewById(R.id.questionField)
        chatMessageList = findViewById(R.id.chatMessageList)
        messageListAdapter = MessageListAdapter()
        chatMessageList?.layoutManager = LinearLayoutManager(this)
        chatMessageList?.adapter = messageListAdapter
        //chatMessageList.scrollToPosition(messageListAdapter.messageList.size()-1);
        messageArray = arrayOfNulls(1000)
        textToSpeech = TextToSpeech(applicationContext,
            OnInitListener { status ->
                if (status != TextToSpeech.ERROR) textToSpeech!!.language = Locale("ru")
            })
        sendButton?.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onClick(v: View) {
                try {
                    onSend()
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Throws(ParseException::class)
            protected fun onSend() {
                val text = questionText?.text.toString()
                AI.getAnswer(text, Consumer<String?> { t -> //answers.add(answer);
                    //callback.accept(String.valueOf(answers));
                    messageListAdapter!!.messageList.add(Message(text, true))
                    t?.let { Message(it, false) }?.let { messageListAdapter!!.messageList.add(it) }
                    chatMessageList?.scrollToPosition(messageListAdapter!!.messageList.size - 1)
                    messageListAdapter!!.notifyDataSetChanged()
                    textToSpeech!!.speak(t, TextToSpeech.QUEUE_FLUSH, null, null)
                })
                //messageArray[0]=text;
                //messageArray[1]=answer;

                /*chatWindow.append(">>"+text);
                chatWindow.append("\n");
                chatWindow.append("<<"+answer);
                chatWindow.append("\n");*/
                //messageListAdapter.messageList.add(new Message(text, true));
                //messageListAdapter.messageList.add(new Message(answer, false));


                //textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        })
        dbHelper = DBHelper(this)
        database = dbHelper!!.writableDatabase
        try {
            val cursor =
                database?.query(DBHelper.TABLE_MESSAGES, null, null, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val messageIndex = cursor.getColumnIndex(DBHelper.FIELD_MESSAGE)
                    val dateIndex = cursor.getColumnIndex(DBHelper.FIELD_DATE)
                    val sendIndex = cursor.getColumnIndex(DBHelper.FIELD_SEND)
                    do {
                        val entity = messageIndex.let { cursor.getString(it) }?.let {
                            dateIndex.let { cursor.getString(it) }?.let { it1 ->
                                sendIndex.let { cursor.getInt(it) }.let { it2 ->
                                    MessageEntity(
                                            it,
                                            it1,
                                            it2
                                    )
                                }
                            }
                        }
                        try {
                            entity?.let { Message(it) }?.let {
                                messageListAdapter!!.messageList.add(
                                    it
                                )
                            }
                        } catch (e: Exception) {
                        }
                    } while (cursor.moveToNext())
                }
            }
            cursor?.close()
        } catch (e2: Exception) {
        }
        Log.i("LOG", "onCreate")
    }

    override fun onStart() {
        super.onStart()
        chatMessageList?.scrollToPosition(messageListAdapter!!.messageList.size - 1)
        Log.i("LOG", "onStart")
    }

    @SuppressLint("CommitPrefEdits")
    override fun onStop() {
        super.onStop()
        val editor = sPref!!.edit()
        editor.putBoolean(THEME, isLight)
        editor.apply()
        database!!.delete(DBHelper.TABLE_MESSAGES, null, null)
        for (message in messageListAdapter!!.messageList) {
            val entity = MessageEntity(message)
            val contentValues = ContentValues()
            contentValues.put(DBHelper.FIELD_MESSAGE, entity.text)
            contentValues.put(DBHelper.FIELD_DATE, entity.date)
            contentValues.put(DBHelper.FIELD_SEND, entity.isSend)
            database!!.insert(DBHelper.TABLE_MESSAGES, null, contentValues)
        }
        Log.i("LOG", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.i("LOG", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.i("LOG", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("LOG", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        database!!.close()
        dbHelper!!.close()
        Log.i("LOG", "onDestroy")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.day_settings -> {
                isLight = true
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            }
            R.id.night_settings -> {
                isLight = false
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val textList = ArrayList<String>()
        val dateList = ArrayList<String>()
        val isSendList = ArrayList<String>()
        for (message in messageListAdapter!!.messageList) {
            textList.add(message.text)
            dateList.add(SimpleDateFormat().format(message.date))
            isSendList.add(java.lang.Boolean.toString(message.isSend))
        }
        outState.putStringArrayList("textHistory", textList)
        outState.putStringArrayList("dateHistory", dateList)
        outState.putStringArrayList("isSendHistory", isSendList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //super.onRestoreInstanceState(savedInstanceState);
        val messages = ArrayList<Message>()
        val textList =
            savedInstanceState.getStringArrayList("textHistory")
        val dateList =
            savedInstanceState.getStringArrayList("dateHistory")
        val isSendList =
            savedInstanceState.getStringArrayList("isSendHistory")
        for (i in textList!!.indices) {
            try {
                @SuppressLint("SimpleDateFormat") val message = Message(
                    textList[i],
                    SimpleDateFormat().parse(dateList!![i]),
                    isSendList!![i] == "true"
                )
                messages.add(message)
            } catch (e: ParseException) {
            }
        }
        messageListAdapter!!.messageList = messages
    }

    companion object {
        const val nameVariableKey = "NAME_VARIABLE"
        const val textViewTexKey = "TEXTVIEW_TEXT"
        const val APP_PREFERENCES = "mysettings"
        private const val URL = "http://mirkosmosa.ru/holiday/2020"
    }
}