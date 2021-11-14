package com.thesohelshaikh.songify.ui.feed.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thesohelshaikh.songify.data.Song
import com.thesohelshaikh.songify.network.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val songRepository: SongRepository) : ViewModel() {

    val songsResponse = MutableLiveData<ArrayList<Song>>()

    fun getSongs() {
        songRepository.getSongs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it?.let {
                    songsResponse.value = it.songs
                }
            }
    }
}