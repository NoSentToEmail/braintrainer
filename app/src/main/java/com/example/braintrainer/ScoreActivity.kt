package com.example.braintrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {

    private lateinit var textView_now: TextView
    private lateinit var button_start_game: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        textView_now = findViewById(R.id.textView_now)
        button_start_game = findViewById(R.id.button_start_game)

        val intent = intent
        if(intent.hasExtra("gameOver")){
            val result = intent.getIntExtra("gameOver", 0)

            val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val max = preferences.getInt("max", 0)
            val scare = String.format("Ваш результат: %s\nМаксимальный результат: %s", result, max)
            textView_now.text = scare
        }
        button_start_game.setOnClickListener(){
            val intent = Intent(this@ScoreActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}