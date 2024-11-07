package com.example.mvvm2.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.Model.dataBase.AppDataBase
import com.example.mvvm.Model.entity.Filme
import com.example.mvvm.ViewModel.FilmeViewModelFactory
import com.example.mvvm2.viewmodel.FilmeViewModel


class MainActivity : ComponentActivity() {
    private val filmeViewModel: FilmeViewModel by viewModels {
        val dao = AppDataBase.getDatabase(applicationContext).FilmeDao()
        FilmeViewModelFactory(dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(filmeViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(filmeViewModel: FilmeViewModel) {
    var titulo by remember { mutableStateOf("") }
    var diretor by remember { mutableStateOf("") }
    var filmeTemp by remember { mutableStateOf<Filme?>(null) }
    var textoBotao by remember { mutableStateOf("Salvar") }
    var modoEditar by remember { mutableStateOf(false) }

    var listaFilmes by filmeViewModel.listaFilmes
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Variável de estado para exibir ou ocultar a caixa de diálogo
    var mostrarCaixaDialogo by remember { mutableStateOf(false) }

    // Caixa de diálogo para confirmação de exclusão
    if (mostrarCaixaDialogo) {
        ExcluirFilme(onConfirm = {
            filmeTemp?.let { filmeViewModel.excluirFilme(it) }
            mostrarCaixaDialogo = false
        }, onDismiss = { mostrarCaixaDialogo = false })
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Lista de Filmes",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Título do filme") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = diretor,
            onValueChange = { diretor = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Diretor do filme") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val retorno: String? = if (modoEditar) {
                    // Atualização do filme existente
                    filmeTemp?.let {
                        filmeViewModel.atualizarFilme(it.id, titulo, diretor).also {
                            modoEditar = false
                            textoBotao = "Salvar"
                        }
                    }
                } else {
                    // Salvar novo filme
                    filmeViewModel.salvarFilme(titulo, diretor)
                }

                Toast.makeText(context, retorno, Toast.LENGTH_LONG).show()

                // Limpar os campos de entrada e foco
                titulo = ""
                diretor = ""
                focusManager.clearFocus()
            }
        ) {
            Text(text = textoBotao)
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Lista de filmes
        LazyColumn {
            items(listaFilmes) { filme ->
                Text(
                    text = "${filme.titulo} (${filme.diretor})",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Button(onClick = {
                        filmeTemp = filme
                        mostrarCaixaDialogo = true
                    }) {
                        Text(text = "Excluir")
                    }

                    Button(onClick = {
                        modoEditar = true
                        filmeTemp = filme
                        titulo = filme.titulo
                        diretor = filme.diretor
                        textoBotao = "Atualizar"
                    }) {
                        Text(text = "Atualizar")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun ExcluirFilme(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirmar exclusão") },
        text = { Text(text = "Tem certeza que deseja excluir este filme?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Sim, excluir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Não, cancelar")
            }
        }
    )
}
