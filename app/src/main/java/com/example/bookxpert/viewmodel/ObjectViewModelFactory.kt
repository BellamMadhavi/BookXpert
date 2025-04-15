package com.example.bookxpert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookxpert.repository.ObjectRepository

class ObjectViewModelFactory(private val repository: ObjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ObjectViewModel::class.java)) {
            return ObjectViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}