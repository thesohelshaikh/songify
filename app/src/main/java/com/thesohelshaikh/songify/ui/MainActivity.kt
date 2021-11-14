package com.thesohelshaikh.songify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thesohelshaikh.songify.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_song_player)
    }
}