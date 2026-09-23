@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.dividipix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividipix.data.model.Conta
import com.example.dividipix.repository.FirestoreRepository
import com.example.dividipix.ui.components.CardGerenciamento
import com.example.dividipix.ui.theme.Branco
import com.example.dividipix.ui.theme.VerdePix

// =============================================================
// NOVA CONTA
// =============================================================

@Composable
fun TelaNovaConta(
    repository: FirestoreRepository,
    onVoltar: () -> Unit,
    onContaCriada: () -> Unit
) {
    var nome by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    var valorTotal by remember {
        mutableStateOf("")
    }

    var data by remember {
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
                        text = "Nova conta",
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
                text = "Cadastrar conta",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Crie uma conta para organizar uma despesa compartilhada.",
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
                    Text("Nome da conta")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,

                onValueChange = {
                    descricao = it
                },

                label = {
                    Text("Descrição")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valorTotal,

                onValueChange = {
                    valorTotal = it
                    erro = null
                },

                label = {
                    Text("Valor total")
                },

                placeholder = {
                    Text("Ex.: 250,00")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = data,

                onValueChange = {
                    data = it
                },

                label = {
                    Text("Data")
                },

                placeholder = {
                    Text("Ex.: 22/09/2026")
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

                    val valor = valorTotal
                        .replace(",", ".")
                        .toDoubleOrNull()

                    if (nome.isBlank()) {

                        erro = "Informe o nome da conta."

                        return@Button
                    }

                    if (valor == null) {

                        erro = "Informe um valor válido."

                        return@Button
                    }

                    if (valor <= 0) {

                        erro = "O valor deve ser maior que zero."

                        return@Button
                    }

                    repository.criarConta(

                        conta = Conta(
                            nome = nome,
                            descricao = descricao,
                            valorTotal = valor,
                            data = data
                        ),

                        onSuccess = {
                            onContaCriada()
                        },

                        onError = {
                            erro = it.message
                                ?: "Não foi possível salvar a conta."
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Salvar conta")
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
// EDITAR CONTA
// =============================================================

@Composable
fun TelaEditarConta(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onContaAtualizada: () -> Unit
) {
    var nome by remember {
        mutableStateOf(conta.nome)
    }

    var descricao by remember {
        mutableStateOf(conta.descricao)
    }

    var valorTotal by remember {
        mutableStateOf(conta.valorTotal.toString())
    }

    var data by remember {
        mutableStateOf(conta.data)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Editar conta",
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
                text = "Editar informações",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = nome,

                onValueChange = {
                    nome = it
                    erro = null
                },

                label = {
                    Text("Nome da conta")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,

                onValueChange = {
                    descricao = it
                },

                label = {
                    Text("Descrição")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valorTotal,

                onValueChange = {
                    valorTotal = it
                    erro = null
                },

                label = {
                    Text("Valor total")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = data,

                onValueChange = {
                    data = it
                },

                label = {
                    Text("Data")
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

                    val valor = valorTotal
                        .replace(",", ".")
                        .toDoubleOrNull()

                    if (nome.isBlank()) {

                        erro = "Informe o nome da conta."

                        return@Button
                    }

                    if (valor == null) {

                        erro = "Informe um valor válido."

                        return@Button
                    }

                    if (valor <= 0) {

                        erro = "O valor deve ser maior que zero."

                        return@Button
                    }

                    repository.atualizarConta(

                        conta = conta.copy(
                            nome = nome,
                            descricao = descricao,
                            valorTotal = valor,
                            data = data
                        ),

                        onSuccess = {
                            onContaAtualizada()
                        },

                        onError = {
                            erro = it.message
                                ?: "Não foi possível atualizar a conta."
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

// =============================================================
// DETALHES DA CONTA
// =============================================================

@Composable
fun TelaDetalhesConta(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onEditar: () -> Unit,
    onExcluir: () -> Unit,
    onParticipantes: () -> Unit,
    onPagamentos: () -> Unit
) {
    var mensagem by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Detalhes",
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

            // =================================================
            // RESUMO DA CONTA
            // =================================================

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),

                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Valor total",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "R$ %.2f".format(conta.valorTotal),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (conta.descricao.isNotBlank()) {

                        Text(
                            text = conta.descricao,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = "Data: ${conta.data.ifBlank { "Não informada" }}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // =================================================
            // GERENCIAMENTO
            // =================================================

            Text(
                text = "Gerenciamento",
                style = MaterialTheme.typography.titleLarge
            )

            CardGerenciamento(

                titulo = "👥 Participantes",

                descricao = "Adicione, edite ou remova as pessoas que participam desta conta.",

                onClick = onParticipantes
            )

            CardGerenciamento(

                titulo = "💰 Pagamentos",

                descricao = "Registre valores pagos, pendências e comprovantes.",

                onClick = onPagamentos
            )

            // =================================================
            // MENSAGEM
            // =================================================

            mensagem?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // =================================================
            // AÇÕES
            // =================================================

            Button(

                onClick = onEditar,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Editar conta")
            }

            OutlinedButton(

                onClick = {

                    repository.excluirConta(

                        contaId = conta.id,

                        onSuccess = {
                            onExcluir()
                        },

                        onError = {
                            mensagem = it.message
                                ?: "Não foi possível excluir a conta."
                        }
                    )
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Excluir conta")
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