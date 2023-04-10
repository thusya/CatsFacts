package com.thus.catsfacts.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.thus.catsfacts.R
import org.koin.android.ext.android.inject
import com.thus.catsfacts.views.listener.LongClickDeleteListener
import com.thus.catsfacts.views.observer.RandomStateObserver

class FactsActivity : AppCompatActivity() {

    private val viewModel: RandomFactViewModel by inject()
    private val randomFactView: RandomFactView by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(randomFactView.inflate(LayoutInflater.from(this), null))

        setObserver()
        setLongClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFromDb()
    }

    private fun setObserver(){
        viewModel.setObserver(this, RandomStateObserver(randomFactView))
    }

    private fun setLongClickListener(){
        randomFactView.setLongClickListener(LongClickDeleteListener(viewModel))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.loadFact -> {
                viewModel.loadFacts(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}