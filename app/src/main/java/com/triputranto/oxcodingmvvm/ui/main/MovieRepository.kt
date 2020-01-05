package com.triputranto.oxcodingmvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.triputranto.oxcodingmvvm.data.entitiy.Movie
import com.triputranto.oxcodingmvvm.data.network.ApiService
import com.triputranto.oxcodingmvvm.data.repository.MovieDataSource
import com.triputranto.oxcodingmvvm.data.repository.MovieDataSourceFactory
import com.triputranto.oxcodingmvvm.data.repository.NetworkState
import com.triputranto.oxcodingmvvm.utils.Utils.POST_PER_PAGE
import io.reactivex.disposables.CompositeDisposable

class MovieRepository(private val apiService: ApiService) {

    private lateinit var moviePagedList: LiveData<PagedList<Movie>>
    private lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun getMovies(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState
        )
}