package com.example.bookxpert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookxpert.repository.ObjectRepository
import com.example.bookxpert.roomdb.ObjectEntity
import kotlinx.coroutines.launch

class ObjectViewModel(private val repository: ObjectRepository) : ViewModel() {

    val objectList = repository.localObjects.asLiveData()
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchObjects() {
        viewModelScope.launch {
            val result = repository.fetchAndStoreObjects()
            result.exceptionOrNull()?.let { _error.value = it.message }
        }
    }

    fun update(obj: ObjectEntity) {
        viewModelScope.launch {
            if (obj.name.isBlank()) {
                _error.value = "Name cannot be empty"
                return@launch
            }
            repository.update(obj)
        }
    }

    fun delete(obj: ObjectEntity) {
        viewModelScope.launch {
            repository.delete(obj)
        }
    }

    fun clearError() { _error.value = null }
}