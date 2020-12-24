package com.example.githubproject.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubproject.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}