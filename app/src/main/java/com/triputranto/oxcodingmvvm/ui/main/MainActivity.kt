package com.triputranto.oxcodingmvvm.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.triputranto.oxcodingmvvm.R
import com.triputranto.oxcodingmvvm.base.BaseActivity
import com.triputranto.oxcodingmvvm.data.repository.NetworkState
import com.triputranto.oxcodingmvvm.ui.adapter.MovieAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupData()
        setupView()
        setupViewModel()
    }

    private fun setupData() {
        movieRepository = MovieRepository(apiService)
    }

    private fun setupView() {
        movieAdapter = MovieAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter
    }

    private fun setupViewModel() {
        movieViewModel = getViewModel()
        movieViewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        movieViewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility =
                if (movieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility =
                if (movieViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!movieViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MovieViewModel =
        ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                MovieViewModel(movieRepository) as T
        })[MovieViewModel::class.java]
}
