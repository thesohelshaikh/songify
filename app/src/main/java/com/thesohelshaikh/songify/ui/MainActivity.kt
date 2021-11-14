package com.thesohelshaikh.songify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thesohelshaikh.songify.databinding.ItemSongPlayerBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ItemSongPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemSongPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}