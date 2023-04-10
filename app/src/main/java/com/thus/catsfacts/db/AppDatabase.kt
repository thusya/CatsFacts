package com.thus.catsfacts.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thus.catsfacts.util.Constants.DATABASE_NAME

@Database(entities = [CatDetails::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catDetailsDao(): CatDetailDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null)
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                }
            return INSTANCE as AppDatabase
        }
    }
}