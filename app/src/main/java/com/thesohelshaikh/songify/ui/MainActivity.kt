package com.thesohelshaikh.songify.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.thesohelshaikh.songify.data.Song
import com.thesohelshaikh.songify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFeed()
    }

    private fun setupFeed() {
        val videoItems = ArrayList<Song>()

        val song1 = Song(
            songURL = "https://filesamples.com/samples/audio/m4a/sample4.m4a",
            songTitle = "Title 1",
            artistName = "Artist 1"
        )
        val song2 = Song(
            songURL = "https://filesamples.com/samples/audio/m4a/sample3.m4a",
            songTitle = "Title 2",
            artistName = "Artist 2"
        )
        val song3 = Song(
            songURL = "https://filesamples.com/samples/audio/m4a/sample1.m4a",
            songTitle = "Title 3",
            artistName = "Artist 3"
        )
        val song4 = Song(
            songURL = "https://filesamples.com/samples/audio/m4a/sample2.m4a",
            songTitle = "Title 4",
            artistName = "Artist 4"
        )
        val song5 = Song(
            songURL = "https://download.samplelib.com/mp3/sample-3s.mp3",
            songTitle = "Title 5",
            artistName = "Artist 5"
        )

        videoItems.add(song1)
        videoItems.add(song2)
        videoItems.add(song3)
        videoItems.add(song4)
        videoItems.add(song5)

        songAdapter = SongAdapter(this, videoItems)
        binding.viewPagerFeed.apply {
            adapter = songAdapter
            setPageTransformer(DepthPageTransformer())
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // TODO: Loading the item each time page is loaded, can be optimized
                    songAdapter.notifyItemChanged(position)
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        songAdapter.releasePlayer()
    }
}