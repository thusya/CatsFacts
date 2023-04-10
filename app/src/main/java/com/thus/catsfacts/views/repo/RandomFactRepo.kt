package com.thus.catsfacts.views.repo

import kotlinx.coroutines.flow.Flow
import com.thus.catsfacts.views.adapter.RandomCatFacts

interface RandomFactRepo {
    fun fetchRandomFact(): Flow<RandomCatFacts>
    fun delete(catId: String): Flow<String>
    suspend fun fetchDetails(): List<RandomCatFacts>
}