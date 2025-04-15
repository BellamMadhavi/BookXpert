package com.example.bookxpert.roomdb

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {
    @Query("SELECT * FROM objects")
    fun getAllObjects(): Flow<List<ObjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObjects(objects: List<ObjectEntity>)

    @Update
    suspend fun updateObject(obj: ObjectEntity)

    @Delete
    suspend fun deleteObject(obj: ObjectEntity)
}