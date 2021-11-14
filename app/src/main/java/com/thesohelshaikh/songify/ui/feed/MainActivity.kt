package com.thesohelshaikh.songify.ui.feed

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.thesohelshaikh.songify.databinding.ActivityMainBinding
import com.thesohelshaikh.songify.ui.feed.adapter.SongAdapter
import com.thesohelshaikh.songify.ui.feed.viewmodel.FeedViewModel
import com.thesohelshaikh.songify.util.gone
import com.thesohelshaikh.songify.util.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    lateinit var songAdapter: SongAdapter

    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFeed()
        fetchSongs()
        observeSongsResponse()
    }

    private fun observeSongsResponse() {
        viewModel.songsResponse.observe(this) { songs ->
            if (!songs.isNullOrEmpty()) {
                binding.progressBar.gone()
                Log.d(TAG, "Fetched songs (${songs.size}) = $songs")
                songAdapter.data = songs
            }
        }
    }

    private fun fetchSongs() {
        binding.progressBar.show()
        viewModel.getSongs()
    }

    private fun setupFeed() {
        songAdapter = SongAdapter(this)
        binding.viewPagerFeed.apply {
            adapter = songAdapter
            // TODO: 15-11-2021 Messes UI
//            setPageTransformer(DepthPageTransformer())
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