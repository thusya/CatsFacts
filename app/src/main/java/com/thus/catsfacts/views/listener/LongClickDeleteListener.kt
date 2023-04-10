package com.thus.catsfacts.views.listener

import android.view.View
import com.thus.catsfacts.views.RandomFactViewModel

class LongClickDeleteListener(private val viewModel: RandomFactViewModel) :
    View.OnLongClickListener {
    override fun onLongClick(view: View): Boolean {
        viewModel.delete(view.tag as String)
        return true
    }
}