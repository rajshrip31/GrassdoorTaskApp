package com.example.grassdoorapptask.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grassdoorapptask.R
import com.example.grassdoorapptask.model.Result
import com.example.grassdoorapptask.view.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_layout_movie_rv.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovieListAdapter(private val context: Context, private val movieList: List<Result>)
    : RecyclerView.Adapter<MovieListAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Result,cnxt: Context) {
            Log.d("Bind movie data", "Movie : $movie")
            itemView.apply {
                tv_id.text = movie.id.toString()
                tv_title.text = "Title: "+movie.title
                tv_release_date.text = "Release Date: "+formatDate(movie.release_date) //movie.release_date
                Glide.with(iv_movie_pic.context)
                    .load("https://image.tmdb.org/t/p/original"+movie.poster_path)
                    .into(iv_movie_pic)
            }

            itemView.setOnClickListener {
                val intent = Intent(cnxt, MovieDetailsActivity::class.java)
                intent.putExtra("ID",itemView.tv_id.text.toString())
                cnxt.startActivity(intent)
            }
        }

        private fun formatDate(date: String): CharSequence {
            val inputDate = date.substring(0,10)
            val inputFormat = SimpleDateFormat("yyyy-dd-mm")//, Locale.getDefault())//2021-12-18T05:06:00Z
            val outputFormat = SimpleDateFormat("MMM yyyy")
            val parsedDate: Date = inputFormat.parse(inputDate)
            Log.d("DATA", "inputDate date : $inputDate")
            Log.d("DATA", "parsedDate date : $outputFormat.format(parsedDate)")
            return outputFormat.format(parsedDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_layout_movie_rv, parent, false))

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(movieList[position],context)
    }
}