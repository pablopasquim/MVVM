package com.example.mvvm.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm2.model.dao.FilmeDao
import com.example.mvvm2.viewmodel.FilmeViewModel

class FilmeViewModelFactory(private val filmeDao: FilmeDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom((FilmeViewModel::class.java))){
            return FilmeViewModel(filmeDao) as T
        }
        throw IllegalArgumentException("Classe ViewModel desconhecida")
    }
}