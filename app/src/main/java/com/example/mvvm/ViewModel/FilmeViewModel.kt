package com.example.mvvm2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.entity.Filme
import com.example.mvvm2.model.dao.FilmeDao
import kotlinx.coroutines.launch

class FilmeViewModel(private val FilmeDao: FilmeDao) : ViewModel() {

    var listaFilmes = mutableStateOf(listOf<Filme>())
        private set

    init {
        carregarFilmes()
    }

    private fun carregarFilmes() {
        viewModelScope.launch {
            listaFilmes.value = FilmeDao.buscarTodos()
        }
    }

    fun salvarFilme(titulo: String, diretor: String): String {
        if (titulo.isBlank() || diretor.isBlank()) {
            return "Preencha todos os campos!"
        }

        val filme = Filme(id = 0, titulo = titulo, diretor = diretor)

        viewModelScope.launch {
            FilmeDao.inserir(filme)
            carregarFilmes()
        }

        return "Filme salvo com sucesso!"
    }

    fun excluirFilme(filme: Filme) {
        viewModelScope.launch {
            FilmeDao.deletar(filme)
            carregarFilmes()
        }
    }

    fun atualizarFilme(id: Int, titulo: String, diretor: String): String {
        if (titulo.isBlank() || diretor.isBlank()) {
            return "Preencha todos os campos!"
        }

        val filme = listaFilmes.value.find { it.id == id } ?: return "Erro ao atualizar filme"
        val filmeAtualizado = filme.copy(titulo = titulo, diretor = diretor)

        viewModelScope.launch {
            FilmeDao.atualizar(filmeAtualizado)
            carregarFilmes()
        }

        return "Filme atualizado!"
    }
}
