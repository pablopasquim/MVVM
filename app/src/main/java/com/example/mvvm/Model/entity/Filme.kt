package com.example.mvvm.Model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filme")
data class Filme(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var titulo: String,
    var diretor: String
)
{

}