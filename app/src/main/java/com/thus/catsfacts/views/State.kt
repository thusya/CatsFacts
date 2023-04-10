package com.thus.catsfacts.views

import com.thus.catsfacts.views.adapter.RandomCatFacts

sealed class State {
    class RenderData(val randomFacts: ArrayList<RandomCatFacts>) : State()
    class Add(val fact: RandomCatFacts) : State()
    class Delete(val catId: String) : State()
    class Error(val error: Throwable) : State()
    class ShowToast(val message: String) : State()
}
