package com.thesohelshaikh.songify.ui.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.thesohelshaikh.songify.R
import com.thesohelshaikh.songify.data.Song
import com.thesohelshaikh.songify.databinding.ActivityMainBinding
import com.thesohelshaikh.songify.ui.feed.adapter.SongAdapter
import com.thesohelshaikh.songify.ui.feed.viewmodel.FeedViewModel
import com.thesohelshaikh.songify.util.gone
import com.thesohelshaikh.songify.util.show
import com.thesohelshaikh.songify.vo.Status
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
        viewModel.songsResponse.observe(this) { resource ->
            when (resource.status) {
                Status.ERROR -> {
                    Log.e(TAG, "observeSongsResponse: ${resource.message}")
                }
                Status.SUCCESS -> {
                    resource.data?.let { songs ->
                        if (!songs.isNullOrEmpty()) {
                            binding.progressBar.gone()
                            Log.d(TAG, "Fetched songs (${songs.size}) = $songs")
                            songAdapter.data = songs
                        }
                    }
                }
                Status.LOADING -> {
                    binding.progressBar.show()
                }
            }
        }
    }

    private fun fetchSongs() {
        viewModel.getSongs()
    }

    private fun setupFeed() {
        songAdapter = SongAdapter(this) { view: View?, song: Song ->
            handleEvent(view, song)

        }
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

    private fun handleEvent(view: View?, song: Song) {
        when (view?.id) {
            R.id.buttonShare -> shareSong(song)
        }
    }

    private fun shareSong(song: Song) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hey, I am listening to ${song.title} " +
                    "by ${song.creator.email} " +
                    "on ${getString(R.string.app_name)}"
        )
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, "send to"))
    }

    override fun onStop() {
        super.onStop()
        songAdapter.releasePlayer()
    }
}