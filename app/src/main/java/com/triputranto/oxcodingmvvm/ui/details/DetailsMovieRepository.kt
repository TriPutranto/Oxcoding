package com.triputranto.oxcodingmvvm.ui.details

import androidx.lifecycle.LiveData
import com.triputranto.oxcodingmvvm.data.entitiy.Movie
import com.triputranto.oxcodingmvvm.data.network.ApiService
import com.triputranto.oxcodingmvvm.data.repository.MovieDetailsNetworkDataSource
import com.triputranto.oxcodingmvvm.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailsMovieRepository(private val apiService: ApiService) {
    private lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun getMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<Movie> {

        movieDetailsNetworkDataSource =
            MovieDetailsNetworkDataSource(
                apiService,
                compositeDisposable
            )
        movieDetailsNetworkDataSource.getMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> =
        movieDetailsNetworkDataSource.networkState
}