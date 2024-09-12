package com.example.navegacao1.ui.telas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navegacao1.model.dados.Endereco
import com.example.navegacao1.model.dados.RetrofitClient
import com.example.navegacao1.model.dados.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPrincipal(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var endereco by remember { mutableStateOf(Endereco()) }
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var idParaBuscarOuRemover by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            usuarios = getUsuarios()
        }
    }

    val primaryColor = Color(0xFF424242)
    val secondaryColor = Color(0xFF757575)
    val backgroundColor = Color(0xFF303030)
    val surfaceColor = Color(0xFF424242)
    val onSurfaceColor = Color.White
    val textColor = Color(0xFFBDBDBD)

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = primaryColor,
            secondary = secondaryColor,
            background = backgroundColor,
            surface = surfaceColor,
            onSurface = onSurfaceColor
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Text(
                text = "Tela Principal (CRUD USUÁRIO)",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Adicionar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedTextColor = onSurfaceColor
                )
            )

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedTextColor = onSurfaceColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val novoUsuario = Usuario(
                            id = getNextId(usuarios),
                            nome = nome,
                            senha = senha
                        )
                        inserirUsuario(novoUsuario)
                        usuarios = getUsuarios()
                        nome = ""
                        senha = ""
                        focusManager.clearFocus()
                    }
                },
                enabled = nome.isNotEmpty() && senha.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)

            ) {
                Text("Adicionar", color = onSurfaceColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Buscar / Remover",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            OutlinedTextField(
                value = idParaBuscarOuRemover,
                onValueChange = { idParaBuscarOuRemover = it },
                label = { Text("ID do Usuário", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = secondaryColor,
                    focusedTextColor = onSurfaceColor
                )
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        scope.launch {
                            if (idParaBuscarOuRemover.isNotEmpty()) {
                                try {
                                    val usuario = buscarUsuarioPorId(idParaBuscarOuRemover)
                                    usuarios = listOf(usuario)
                                } catch (e: Exception) {
                                    mensagemErro = "Usuário não encontrado!"
                                }
                                idParaBuscarOuRemover = ""
                            }
                        }
                    },
                    enabled = idParaBuscarOuRemover.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(secondaryColor)
                ) {
                    Text("Buscar", color = onSurfaceColor)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            if (idParaBuscarOuRemover.isNotEmpty()) {
                                removerUsuario(idParaBuscarOuRemover)
                                usuarios = getUsuarios()
                                idParaBuscarOuRemover = ""
                            }
                        }
                    },
                    enabled = idParaBuscarOuRemover.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(secondaryColor)
                ) {
                    Text("Remover", color = onSurfaceColor)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Listagem",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    scope.launch {
                        usuarios = getUsuarios()
                    }
                }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Recarregar", tint = primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = surfaceColor,
                            contentColor = onSurfaceColor
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "ID: ${usuario.id}", color = onSurfaceColor)
                            Text(text = "Nome: ${usuario.nome}", color = onSurfaceColor)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            mensagemErro?.let { erro ->
                LaunchedEffect(erro) {
                    scope.launch {
                        delay(3000)
                        mensagemErro = null
                    }
                }
                Snackbar(modifier = Modifier.padding(8.dp), containerColor = primaryColor) {
                    Text(text = erro, color = onSurfaceColor)
                }
            }
        }
    }
}

suspend fun getUsuarios(): List<Usuario> {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.listar()
    }
}

suspend fun inserirUsuario(usuario: Usuario) {
    withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.inserir(usuario)
    }
}

suspend fun removerUsuario(id: String) {
    withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.remover(id)
    }
}

suspend fun buscarUsuarioPorId(id: String): Usuario {
    return withContext(Dispatchers.IO) {
        RetrofitClient.usuarioService.buscarPorId(id)
    }
}

fun getNextId(usuarios: List<Usuario>): String {
    return (usuarios.maxOfOrNull { it.id.toIntOrNull() ?: 0 }?.plus(1) ?: 1).toString()
}