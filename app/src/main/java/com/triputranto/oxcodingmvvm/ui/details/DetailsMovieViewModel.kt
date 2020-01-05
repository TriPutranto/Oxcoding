package com.triputranto.oxcodingmvvm.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.triputranto.oxcodingmvvm.data.entitiy.Movie
import com.triputranto.oxcodingmvvm.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailsMovieViewModel(movieRepository: DetailsMovieRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<Movie> by lazy {
        movieRepository.getMovieDetails(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}