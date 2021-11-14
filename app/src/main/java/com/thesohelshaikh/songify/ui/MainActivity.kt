package com.thesohelshaikh.songify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.thesohelshaikh.songify.R

import androidx.core.content.res.ResourcesCompat
import com.google.android.exoplayer2.*
import com.thesohelshaikh.songify.databinding.ItemSongPlayerBinding

class MainActivity : AppCompatActivity(), Player.EventListener {
    private lateinit var binding: ItemSongPlayerBinding

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    lateinit var  durationRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ItemSongPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPlayPauseButton()
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun setupPlayPauseButton() {
        binding.imageViewPlayPause.setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
            } else {
                player?.play()
            }
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                val mediaItem =
                    MediaItem.fromUri("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3")
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(0, playbackPosition)
                exoPlayer.prepare()
            }
        updatePlayButtonUi(playWhenReady)

        player?.addListener(object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                super.onPlayWhenReadyChanged(playWhenReady, reason)
                updatePlayButtonUi(playWhenReady)
            }
        })

        setupProgressIndicator()
    }

    private fun setupProgressIndicator() {
        val handler = Handler(Looper.getMainLooper())
        durationRunnable = Runnable {
            binding.progressBar.progress =
                ((player!!.currentPosition * 100) / player!!.duration).toInt()
            handler.postDelayed(durationRunnable, 1)
        }
        handler.postDelayed(durationRunnable, 0)
    }

    private fun updatePlayButtonUi(playWhenReady: Boolean) {
        if (playWhenReady) {
            binding.imageViewPlayPause.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_pause_button,
                    null
                )
            )
        } else {
            binding.imageViewPlayPause.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_play_button,
                    null
                )
            )
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }
}