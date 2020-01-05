package com.triputranto.oxcodingmvvm.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.triputranto.oxcodingmvvm.R
import com.triputranto.oxcodingmvvm.base.BaseActivity
import com.triputranto.oxcodingmvvm.data.entitiy.Movie
import com.triputranto.oxcodingmvvm.data.repository.NetworkState
import com.triputranto.oxcodingmvvm.utils.Utils.IMAGE_URL
import kotlinx.android.synthetic.main.activity_details_movie.*
import java.text.NumberFormat
import java.util.*
import kotlin.properties.Delegates

class DetailsMovieActivity : BaseActivity() {

    private lateinit var detailsMovieViewModel: DetailsMovieViewModel
    private lateinit var movieRepository: DetailsMovieRepository
    private var movieId by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_movie)

        setupData()
        setupViewModel()
    }

    private fun setupData() {
        movieId = intent.getIntExtra("id", 1)
        movieRepository = DetailsMovieRepository(apiService)
    }

    private fun setupViewModel() {
        detailsMovieViewModel = getDetailsMovieViewModel(movieId)
        detailsMovieViewModel.movieDetails.observe(this, Observer {
            bindUi(it)
        })

        detailsMovieViewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun bindUi(it: Movie?) {
        movie_title.text = it?.title
        movie_tagline.text = it?.tagline
        movie_release_date.text = it?.releaseDate
        movie_rating.text = it?.rating.toString()
        movie_runtime.text = it?.runtime.toString() + "minutes"
        movie_overview.text = it?.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it?.budget)
        movie_revenue.text = formatCurrency.format(it?.revenue)

        val moviePosterURL = IMAGE_URL + it?.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)

    }

    private fun getDetailsMovieViewModel(movieId: Int): DetailsMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailsMovieViewModel(movieRepository, movieId) as T
            }
        })[DetailsMovieViewModel::class.java]
    }
}
