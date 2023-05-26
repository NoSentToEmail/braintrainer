package com.example.braintrainer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.Preference
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.StrictMath.random
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var text_button1: TextView
    private lateinit var text_button2: TextView
    private lateinit var text_button3: TextView
    private lateinit var text_button4: TextView
    private lateinit var text_ball: TextView
    private lateinit var text_timer: TextView

    private lateinit var question: String
    private var rightAnswer: Int = 0
    private var rightAnswerPositioin: Int = 0
    private var isPositive:Boolean = false
    private var min = 5
    private var max = 30
    private var countOfQuestions = 0
    private var countOfRightAC = 0
    private var gameOver:Boolean = false


    private lateinit var option:ArrayList<TextView>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        text_ball= findViewById(R.id.text_ball)
        text_timer= findViewById(R.id.text_timer)
        text_button1= findViewById(R.id.textButton1)
        text_button2= findViewById(R.id.textButton2)
        text_button3= findViewById(R.id.textButton3)
        text_button4= findViewById(R.id.textButton4)

        option = ArrayList()

        option.add(text_button1)
        option.add(text_button2)
        option.add(text_button3)
        option.add(text_button4)
        playNext()
        val time = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                text_timer.text = (getTime(millisUntilFinished))
                if(millisUntilFinished < 10000){
                    text_timer.setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_dark))
                }
            }

            override fun onFinish() {
                gameOver = true
                val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                max = preferences.getInt("max", 0)
                if(countOfRightAC >= max){
                    preferences.edit().putInt("max", countOfRightAC).apply()
                }
                val intent = Intent(this@MainActivity, ScoreActivity::class.java)
                intent.putExtra("gameOver", countOfRightAC)
                startActivity(intent)
            }
        }

        time.start() // Запуск таймера

    }
    private fun playNext(){
        genereitQuestion()
        for(i in 0 until  option.size){
            if (i == rightAnswerPositioin){
                option[i].text = rightAnswer.toString()
            }else {
                option[i].text = generateWrongAnswer().toString()
            }
        }
        val score = String.format("%s / %s", countOfRightAC, countOfQuestions)
        text_ball.text = score
    }

    private fun getTime(millis: Long): String {
        var seconds = (millis / 1000)
        val minutes = seconds / 60
        seconds %= 60
        return String.format(Locale.getDefault(),"%02d: %02d", minutes, seconds)
    }

    private fun genereitQuestion(){
        val a = (Math.random() * (max - min + 1) + min).toInt()
        val b = (Math.random() * (max - min + 1) + min).toInt()
        val mark = (Math.random() * 2)
        isPositive = (mark.toInt() == 1)
        if(isPositive){
            rightAnswer = (a + b).toInt()
            question = String.format("%s + %s", a, b)
        }else {
            rightAnswer = (a - b).toInt()
            question = String.format("%s - %s", a, b)
        }
        textView.text = question
        rightAnswerPositioin = ((Math.random() * 4).toInt())
    }

    private fun generateWrongAnswer(): Int {
        var result: Int
        do{
            result = ((Math.random() * max * 2 + 1) - (max - min)).toInt()
        } while(result == rightAnswer)
        return result



    }

    fun onClickAnswer(view: View) {
        if(!gameOver) {
            val textView: TextView = view as TextView
            val answer = textView.text.toString()
            val chosenAnser = answer.toInt()
            if (chosenAnser == rightAnswer) {
                countOfRightAC++
                Toast.makeText(this, "Верно", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "не верно", Toast.LENGTH_SHORT).show()
            }
            countOfQuestions++
            playNext()
        }

    }
}