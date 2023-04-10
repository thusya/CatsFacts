package com.thus.catsfacts.model

import com.thus.catsfacts.db.CatDetailDao
import com.thus.catsfacts.db.CatDetails
import com.thus.catsfacts.util.DateUtil.parseDate
import com.thus.catsfacts.views.adapter.RandomCatFacts

class DataMapper(private val dao: CatDetailDao) {
    fun toMap(data: CatRandomFact): RandomCatFacts {
        val displayDate = parseDate(data.createdAt)
        val catDetails = CatDetails(catId = data.id, fact = data.text, createdDate = displayDate)
        dao.insertCatDetails(catDetails)
        return RandomCatFacts(data.id, data.text, displayDate)
    }

    suspend fun fetchCatDetails(): List<RandomCatFacts> {
        val randomCatFacts = dao.fetchCatDetails()
        return randomCatFacts.map {
            RandomCatFacts(it.catId, it.fact, it.createdDate)
        }
    }

    fun delete(catId: String) {
        dao.deleteCatDetails(catId)
    }
}