package com.thus.catsfacts.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_details")
data class CatDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "cat_id")
    val catId: String = "",
    @ColumnInfo(name = "fact")
    val fact: String = "",
    @ColumnInfo(name = "crated_date")
    val createdDate: String = "",
)