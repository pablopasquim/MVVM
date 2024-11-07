package com.example.mvvm.Model.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvm.Model.entity.Filme
import com.example.mvvm2.model.dao.FilmeDao

@Database(entities = [Filme::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun FilmeDao(): FilmeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}