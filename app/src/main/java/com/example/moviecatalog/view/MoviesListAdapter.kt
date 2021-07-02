package com.example.moviecatalog.view

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviecatalog.R
import com.example.moviecatalog.data.models.MinimalMovie
import com.example.moviecatalog.inflate
import com.squareup.picasso.Picasso

/**
 * Created by Iftekhar Ahmed on 02/07/2021.
 */
class MoviesListAdapter(
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onItemClick(movie: MinimalMovie)
    }

    private val items = ArrayList<MinimalMovie>()

    fun addItems(items: Array<MinimalMovie>): Boolean {
        val positionStart = this.items.size
        val modified = this.items.addAll(items)
        if (modified)
            notifyItemRangeInserted(positionStart, items.size)
        return modified
    }

    fun getItem(position: Int): MinimalMovie {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class MovieViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.grid_item_movies_list)
    ) {

        private val title: TextView by lazy { itemView.findViewById(R.id.title) }
        private val posterImage: ImageView by lazy { itemView.findViewById(R.id.posterImage) }

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) onClickListener.onItemClick(items[adapterPosition])
            }
        }

        fun bind(movie: MinimalMovie) {
            title.text = movie.title
            Picasso.get().load(movie.fullPosterPath).placeholder(R.drawable.ic_movie)
                .error(R.drawable.ic_movie).into(posterImage)
        }
    }
}