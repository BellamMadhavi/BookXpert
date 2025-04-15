package com.example.bookxpert.repository

import com.example.bookxpert.API.ApiService
import com.example.bookxpert.retrofit.RetrofitClient
import com.example.bookxpert.roomdb.ObjectDao
import com.example.bookxpert.roomdb.ObjectEntity
import com.google.gson.Gson

class ObjectRepository(private val api: ApiService, private val dao: ObjectDao) {

    val localObjects = dao.getAllObjects()

    suspend fun fetchAndStoreObjects(): Result<Unit> {
        return try {
//            val response = api.getObjects()
            val response = RetrofitClient.api.getObjects()
            if (response.isSuccessful) {
                response.body()?.let { items ->
                    val entities = items.map {
                        ObjectEntity(
                            id = it.id.toInt(),
                            name = it.name,
                            data = Gson().toJson(it.data)
                        )
                    }
                    dao.insertObjects(entities)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to load: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(obj: ObjectEntity) = dao.updateObject(obj)
    suspend fun delete(obj: ObjectEntity) = dao.deleteObject(obj)
}