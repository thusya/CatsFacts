package com.thus.catsfacts.views

import android.content.Context
import androidx.lifecycle.*
import com.thus.catsfacts.R
import com.thus.catsfacts.util.Connectivity
import com.thus.catsfacts.views.repo.RandomFactRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RandomFactViewModel(
    private val randomFactRepo: RandomFactRepo,
    private val dispatchersIO: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val liveData = MutableLiveData<State>()

    fun loadFromDb() {
        viewModelScope.launch {
            liveData.postValue(State.RenderData(ArrayList(randomFactRepo.fetchDetails())))
        }
    }

    private fun getRandomFact() {
        viewModelScope.launch(dispatchersIO) {
            randomFactRepo.fetchRandomFact().catch { exception ->
                liveData.postValue(State.Error(exception))
            }
                .collect {
                    liveData.postValue(State.Add(it))
                }
        }
    }

    fun setObserver(owner: LifecycleOwner, observer: Observer<State>) {
        liveData.observe(owner, observer)
    }

    fun delete(catId: String) {
        viewModelScope.launch(dispatchersIO) {
            randomFactRepo.delete(catId).catch { e ->
                liveData.postValue(State.Error(e))
            }.collect {
                liveData.postValue(State.Delete(it))
            }
        }
    }

    fun loadFacts(context: Context) {
        if (Connectivity.isConnected(context)) {
            getRandomFact()
        } else {
            loadFromDb()
            liveData.value = State.ShowToast(context.getString(R.string.no_internet_connection))
        }
    }
}