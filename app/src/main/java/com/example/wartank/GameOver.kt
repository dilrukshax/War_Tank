package com.example.wartank

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    private lateinit var tvPoints: TextView
    private lateinit var tvHighest: TextView
    private lateinit var ivNewHighest: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        // Initialize views and SharedPreferences
        tvPoints = findViewById(R.id.tvPoints)
        tvHighest = findViewById(R.id.tvHighest)
        ivNewHighest = findViewById(R.id.ivNewHighest)
        sharedPreferences = getSharedPreferences("my_pref", MODE_PRIVATE)

        // Get points from the intent
        val points = intent.getIntExtra("points", 0)

        // Update points TextView
        tvPoints.text = points.toString()

        // Get highest score from SharedPreferences
        var highest = sharedPreferences.getInt("highest", 0)

        // Check if current points beat the highest score
        if (points > highest) {
            // Show the new highest score indicator
            ivNewHighest.visibility = View.VISIBLE

            // Update the highest score
            highest = points

            // Save the new highest score to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.apply()
        }

        // Update highest score TextView
        tvHighest.text = highest.toString()
    }

    fun restart(view: View) {
        // Restart the game by starting MainActivity
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(view: View) {
        // Exit the game by finishing the activity
        finish()
    }
}
