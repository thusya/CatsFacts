package com.thus.catsfacts.views.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thus.catsfacts.R
import com.thus.catsfacts.util.Constants
import com.thus.catsfacts.views.listener.LongClickDeleteListener

class FactsViewHolder(
    private val view: View,
    private val longClickDeleteListener: LongClickDeleteListener
) :
    RecyclerView.ViewHolder(view) {

    fun render(data: RandomCatFacts) {
        val catImage = view.findViewById<ImageView>(R.id.catImage)
        val randomFact = view.findViewById<TextView>(R.id.randomFact)
        val dateAdded = view.findViewById<TextView>(R.id.factDateAdded)

        randomFact.text = data.fact
        dateAdded.text = data.dateAdded
        view.tag = data.id
        view.setOnLongClickListener(longClickDeleteListener)

        Glide.with(view.context)
            .load(Constants.CAT_IMAGE_URL)
            .into(catImage)

        /* We can add below methods for showing random cats pics but currently it showing same image
           by Glide for the same url
         .diskCacheStrategy(DiskCacheStrategy.NONE)
         .skipMemoryCache(true)
         */
    }
}