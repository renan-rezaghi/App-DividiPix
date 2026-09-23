@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.dividipix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividipix.data.model.Conta
import com.example.dividipix.data.model.Participante
import com.example.dividipix.repository.FirestoreRepository
import com.example.dividipix.ui.components.CardParticipante
import com.example.dividipix.ui.theme.Branco
import com.example.dividipix.ui.theme.VerdePix

// =============================================================
// LISTA DE PARTICIPANTES
// =============================================================

@Composable
fun TelaParticipantes(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onNovoParticipante: () -> Unit,
    onEditarParticipante: (Participante) -> Unit
) {
    var participantes by remember {
        mutableStateOf<List<Participante>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    var atualizar by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(conta.id, atualizar) {

        carregando = true
        erro = null

        repository.listarParticipantes(

            contaId = conta.id,

            onSuccess = {
                participantes = it
                carregando = false
            },

            onError = {
                erro = it.message
                carregando = false
            }
        )
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Participantes",
                        color = Branco
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdePix
                )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = conta.nome,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Pessoas que participam desta conta compartilhada.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(

                onClick = onNovoParticipante,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("＋  Novo participante")
            }

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (carregando) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()
                }

            } else if (participantes.isEmpty()) {

                Text(
                    text = "Nenhum participante cadastrado.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Adicione as pessoas que irão dividir esta despesa.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),

                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = participantes,
                        key = { it.id }
                    ) { participante ->

                        CardParticipante(

                            participante = participante,

                            onEditar = {
                                onEditarParticipante(participante)
                            },

                            onExcluir = {

                                repository.excluirParticipante(

                                    participanteId = participante.id,

                                    onSuccess = {
                                        atualizar++
                                    },

                                    onError = {
                                        erro = it.message
                                    }
                                )
                            }
                        )
                    }
                }
            }

            OutlinedButton(

                onClick = onVoltar,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Voltar")
            }
        }
    }
}

// =============================================================
// NOVO PARTICIPANTE
// =============================================================

@Composable
fun TelaNovoParticipante(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onParticipanteCriado: () -> Unit
) {
    var nome by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Novo participante",
                        color = Branco
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdePix
                )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Adicionar participante",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Informe os dados da pessoa que participará desta conta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = nome,

                onValueChange = {
                    nome = it
                    erro = null
                },

                label = {
                    Text("Nome")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,

                onValueChange = {
                    email = it
                },

                label = {
                    Text("E-mail")
                },

                modifier = Modifier.fillMaxWidth()
            )

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(

                onClick = {

                    if (nome.isBlank()) {

                        erro = "Informe o nome do participante."

                        return@Button
                    }

                    repository.criarParticipante(

                        participante = Participante(
                            contaId = conta.id,
                            nome = nome,
                            email = email
                        ),

                        onSuccess = {
                            onParticipanteCriado()
                        },

                        onError = {
                            erro = it.message
                                ?: "Não foi possível salvar o participante."
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Salvar participante")
            }

            OutlinedButton(

                onClick = onVoltar,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Cancelar")
            }
        }
    }
}

// =============================================================
// EDITAR PARTICIPANTE
// =============================================================

@Composable
fun TelaEditarParticipante(
    repository: FirestoreRepository,
    participante: Participante,
    onVoltar: () -> Unit,
    onParticipanteAtualizado: () -> Unit
) {
    var nome by remember {
        mutableStateOf(participante.nome)
    }

    var email by remember {
        mutableStateOf(participante.email)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Editar participante",
                        color = Branco
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdePix
                )
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Editar participante",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = nome,

                onValueChange = {
                    nome = it
                    erro = null
                },

                label = {
                    Text("Nome")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,

                onValueChange = {
                    email = it
                },

                label = {
                    Text("E-mail")
                },

                modifier = Modifier.fillMaxWidth()
            )

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(

                onClick = {

                    if (nome.isBlank()) {

                        erro = "Informe o nome do participante."

                        return@Button
                    }

                    repository.atualizarParticipante(

                        participante = participante.copy(
                            nome = nome,
                            email = email
                        ),

                        onSuccess = {
                            onParticipanteAtualizado()
                        },

                        onError = {
                            erro = it.message
                                ?: "Não foi possível atualizar o participante."
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Salvar alterações")
            }

            OutlinedButton(

                onClick = onVoltar,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Cancelar")
            }
        }
    }
}