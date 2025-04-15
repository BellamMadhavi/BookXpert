package com.example.bookxpert.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objects")
data class ObjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val data: String
)