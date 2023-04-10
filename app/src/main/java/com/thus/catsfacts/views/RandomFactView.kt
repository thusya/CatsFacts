package com.thus.catsfacts.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thus.catsfacts.views.adapter.RandomCatFacts
import com.thus.catsfacts.views.listener.LongClickDeleteListener

interface RandomFactView {

    fun inflate(inflater: LayoutInflater, container: ViewGroup?): View?
    fun changeState(state: State)
    fun setLongClickListener(longClickDeleteListener: LongClickDeleteListener)

    sealed class State {
        class RenderData(val randomFacts: ArrayList<RandomCatFacts>) : State()
        class Add(val fact: RandomCatFacts) : State()
        class Delete(val catId: String) : State()
        class Error(val error: Throwable) : State()
        class ShowToast(val message: String) : State()
    }
}