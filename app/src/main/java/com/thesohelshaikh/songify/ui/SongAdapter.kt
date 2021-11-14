package com.thesohelshaikh.songify.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.thesohelshaikh.songify.R
import com.thesohelshaikh.songify.data.Song
import com.thesohelshaikh.songify.databinding.ItemSongPlayerBinding


class SongAdapter(
    private val context: Context,
    private val videoItems: List<Song>
) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder?>() {

    var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    init {
        initializePlayer()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongPlayerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.setVideoData(videoItems[position])
    }

    override fun getItemCount(): Int {
        return videoItems.size
    }

    inner class SongViewHolder(val binding: ItemSongPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var durationRunnable: Runnable

        fun setVideoData(videoItem: Song) {
            binding.apply {
                textViewSongTitle.text = videoItem.songTitle
                textViewArtistName.text = videoItem.artistName
            }

            videoItem.songURL?.let { songUrl ->
                playSong(songUrl)
            }

            setupPlayPauseButton()

            updatePlayButtonUi(playWhenReady)

            player?.addListener(object : Player.Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, reason)
                    updatePlayButtonUi(playWhenReady)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    // play song again after it ends
                    if (playbackState == Player.STATE_ENDED) {
                        player?.seekTo(0)
                        player?.play()
                    }
                }
            })

            setupProgressIndicator()
        }

        private fun playSong(songUrl: String) {
            player?.clearMediaItems()
            val mediaItem =
                MediaItem.fromUri(songUrl)
            player?.setMediaItem(mediaItem)
            player?.play()
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

        private fun setupProgressIndicator() {
            val handler = Handler(Looper.getMainLooper())
            durationRunnable = Runnable {
                player?.let {
                    binding.progressBar.progress =
                        ((player!!.currentPosition * 100) / player!!.duration).toInt()
                    handler.postDelayed(durationRunnable, 1)
                }
            }
            handler.postDelayed(durationRunnable, 0)
        }

        private fun updatePlayButtonUi(playWhenReady: Boolean) {
            if (playWhenReady) {
                binding.imageViewPlayPause.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_pause_button,
                        null
                    )
                )
            } else {
                binding.imageViewPlayPause.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.ic_play_button,
                        null
                    )
                )
            }
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(0, playbackPosition)
                exoPlayer.prepare()
            }
    }

    fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

}