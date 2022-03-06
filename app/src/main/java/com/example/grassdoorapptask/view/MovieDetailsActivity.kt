package com.example.grassdoorapptask.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.grassdoorapptask.R
import com.example.grassdoorapptask.core.BaseActivity
import com.example.grassdoorapptask.databinding.ActivityMovieDetailsBinding
import com.example.grassdoorapptask.viewmodel.MovieDetailsViewModel
import kotlinx.android.synthetic.main.item_layout_movie_rv.*

class MovieDetailsActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityMovieDetailsBinding
    private lateinit var detailsViewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_movie_details)
        dataBinding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        registerObserver()
        setUI()
    }

    private fun registerObserver() {
        detailsViewModel =
            ViewModelProviders.of(this)[MovieDetailsViewModel::class.java]

        detailsViewModel.movieLiveData.observe(this){
            val movie = it
            dataBinding.tvTitle.text = "Title: "+movie.title
            dataBinding.tvDesc.text = "Description: "+movie.overview

            Glide.with(iv_movie_pic.context)
                .load("https://image.tmdb.org/t/p/original"+movie.poster_path)
                .into(iv_movie_pic)
        }
    }

    private fun setUI(){
        (this as AppCompatActivity).supportActionBar!!.title = "Movie Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val id = intent.getStringExtra("ID")
       detailsViewModel.getMovieDetails(id.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}