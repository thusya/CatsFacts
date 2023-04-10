package com.thus.catsfacts.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDetailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCatDetails(catDetails: CatDetails)

    @Query("select * from cat_details")
    suspend fun fetchCatDetails(): List<CatDetails>

    @Query("delete from cat_details where cat_id=:catId")
    fun deleteCatDetails(catId: String)

}

