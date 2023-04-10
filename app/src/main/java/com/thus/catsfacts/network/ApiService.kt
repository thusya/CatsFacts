package com.thus.catsfacts.network

import com.thus.catsfacts.model.CatRandomFact
import retrofit2.http.GET

interface ApiService {

    @GET("facts/random")
    suspend fun getRandomFact(): CatRandomFact
}