@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.dividipix.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import com.example.dividipix.data.model.Pagamento
import com.example.dividipix.data.model.Participante
import com.example.dividipix.repository.FirestoreRepository
import com.example.dividipix.ui.components.CardPagamento
import com.example.dividipix.ui.theme.Branco
import com.example.dividipix.ui.theme.VerdePix

// =============================================================
// LISTA DE PAGAMENTOS
// =============================================================

@Composable
fun TelaPagamentos(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onNovoPagamento: () -> Unit,
    onEditarPagamento: (Pagamento) -> Unit
) {
    var pagamentos by remember {
        mutableStateOf<List<Pagamento>>(emptyList())
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

        repository.listarPagamentos(

            contaId = conta.id,

            onSuccess = {
                pagamentos = it
                carregando = false
            },

            onError = {
                erro = it.message
                carregando = false
            }
        )
    }

    val totalPago = pagamentos
        .filter { it.pago }
        .sumOf { it.valor }

    val totalPendente = pagamentos
        .filter { !it.pago }
        .sumOf { it.valor }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Pagamentos",
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
                text = "Acompanhe os valores pagos e as pendências desta conta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // =================================================
            // RESUMO
            // =================================================

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Card(
                    modifier = Modifier.weight(1f),

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp),

                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "Pago",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "R$ %.2f".format(totalPago),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp),

                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "Pendente",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "R$ %.2f".format(totalPendente),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            Button(

                onClick = onNovoPagamento,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("＋  Novo pagamento")
            }

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // =================================================
            // LISTA
            // =================================================

            when {

                carregando -> {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator()
                    }
                }

                pagamentos.isEmpty() -> {

                    Text(
                        text = "Nenhum pagamento cadastrado.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Adicione um pagamento para começar a acompanhar as despesas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),

                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = pagamentos,
                            key = { it.id }
                        ) { pagamento ->

                            CardPagamento(

                                pagamento = pagamento,

                                onEditar = {
                                    onEditarPagamento(pagamento)
                                },

                                onExcluir = {

                                    repository.excluirPagamento(

                                        pagamentoId = pagamento.id,

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
// NOVO PAGAMENTO
// =============================================================

@Composable
fun TelaNovoPagamento(
    repository: FirestoreRepository,
    conta: Conta,
    onVoltar: () -> Unit,
    onPagamentoCriado: () -> Unit
) {
    var participantes by remember {
        mutableStateOf<List<Participante>>(emptyList())
    }

    var carregandoParticipantes by remember {
        mutableStateOf(true)
    }

    var participanteSelecionado by remember {
        mutableStateOf<Participante?>(null)
    }

    var menuAberto by remember {
        mutableStateOf(false)
    }

    var valor by remember {
        mutableStateOf("")
    }

    var descricao by remember {
        mutableStateOf("")
    }

    var comprovante by remember {
        mutableStateOf("")
    }

    var pago by remember {
        mutableStateOf(false)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(conta.id) {

        repository.listarParticipantes(

            contaId = conta.id,

            onSuccess = {
                participantes = it
                carregandoParticipantes = false
            },

            onError = {
                erro = it.message
                carregandoParticipantes = false
            }
        )
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Novo pagamento",
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
                text = "Registrar pagamento",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = conta.nome,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Participante",
                style = MaterialTheme.typography.titleMedium
            )

            if (carregandoParticipantes) {

                CircularProgressIndicator()

            } else if (participantes.isEmpty()) {

                Text(
                    text = "Cadastre pelo menos um participante antes de criar um pagamento.",
                    color = MaterialTheme.colorScheme.error
                )

            } else {

                Column {

                    OutlinedButton(

                        onClick = {
                            menuAberto = true
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            participanteSelecionado?.nome
                                ?: "Selecionar participante"
                        )
                    }

                    DropdownMenu(

                        expanded = menuAberto,

                        onDismissRequest = {
                            menuAberto = false
                        }
                    ) {

                        participantes.forEach { participante ->

                            DropdownMenuItem(

                                text = {
                                    Text(participante.nome)
                                },

                                onClick = {

                                    participanteSelecionado = participante
                                    menuAberto = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(

                value = valor,

                onValueChange = {
                    valor = it
                    erro = null
                },

                label = {
                    Text("Valor")
                },

                placeholder = {
                    Text("Ex.: 50,00")
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

                placeholder = {
                    Text("Ex.: Mercado, gasolina, jantar...")
                },

                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(

                value = comprovante,

                onValueChange = {
                    comprovante = it
                },

                label = {
                    Text("Comprovante")
                },

                placeholder = {
                    Text("Link ou referência do comprovante")
                },

                modifier = Modifier.fillMaxWidth()
            )

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Status do pagamento",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (pago) {
                            "Pagamento realizado"
                        } else {
                            "Pagamento pendente"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = pago,

                    onCheckedChange = {
                        pago = it
                    }
                )
            }

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(

                onClick = {

                    val valorConvertido = valor
                        .replace(",", ".")
                        .toDoubleOrNull()

                    when {

                        participanteSelecionado == null -> {

                            erro = "Selecione um participante."
                        }

                        valorConvertido == null -> {

                            erro = "Informe um valor válido."
                        }

                        valorConvertido <= 0 -> {

                            erro = "O valor deve ser maior que zero."
                        }

                        else -> {

                            repository.criarPagamento(

                                pagamento = Pagamento(
                                    contaId = conta.id,
                                    participanteId = participanteSelecionado!!.id,
                                    participanteNome = participanteSelecionado!!.nome,
                                    valor = valorConvertido,
                                    descricao = descricao,
                                    comprovante = comprovante,
                                    pago = pago
                                ),

                                onSuccess = {
                                    onPagamentoCriado()
                                },

                                onError = {

                                    erro = it.message
                                        ?: "Não foi possível salvar o pagamento."
                                }
                            )
                        }
                    }
                },

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Salvar pagamento")
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
// EDITAR PAGAMENTO
// =============================================================

@Composable
fun TelaEditarPagamento(
    repository: FirestoreRepository,
    pagamento: Pagamento,
    conta: Conta,
    onVoltar: () -> Unit,
    onPagamentoAtualizado: () -> Unit
) {
    var participantes by remember {
        mutableStateOf<List<Participante>>(emptyList())
    }

    var carregandoParticipantes by remember {
        mutableStateOf(true)
    }

    var participanteSelecionado by remember {
        mutableStateOf<Participante?>(null)
    }

    var menuAberto by remember {
        mutableStateOf(false)
    }

    var valor by remember {
        mutableStateOf(pagamento.valor.toString())
    }

    var descricao by remember {
        mutableStateOf(pagamento.descricao)
    }

    var comprovante by remember {
        mutableStateOf(pagamento.comprovante)
    }

    var pago by remember {
        mutableStateOf(pagamento.pago)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(conta.id) {

        repository.listarParticipantes(

            contaId = conta.id,

            onSuccess = { lista ->

                participantes = lista

                participanteSelecionado = lista.find {
                    it.id == pagamento.participanteId
                }

                carregandoParticipantes = false
            },

            onError = {

                erro = it.message
                carregandoParticipantes = false
            }
        )
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Editar pagamento",
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
                text = "Editar pagamento",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = conta.nome,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Participante",
                style = MaterialTheme.typography.titleMedium
            )

            if (carregandoParticipantes) {

                CircularProgressIndicator()

            } else {

                Column {

                    OutlinedButton(

                        onClick = {
                            menuAberto = true
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            participanteSelecionado?.nome
                                ?: pagamento.participanteNome
                        )
                    }

                    DropdownMenu(

                        expanded = menuAberto,

                        onDismissRequest = {
                            menuAberto = false
                        }
                    ) {

                        participantes.forEach { participante ->

                            DropdownMenuItem(

                                text = {
                                    Text(participante.nome)
                                },

                                onClick = {

                                    participanteSelecionado = participante
                                    menuAberto = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(

                value = valor,

                onValueChange = {
                    valor = it
                    erro = null
                },

                label = {
                    Text("Valor")
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

                value = comprovante,

                onValueChange = {
                    comprovante = it
                },

                label = {
                    Text("Comprovante")
                },

                modifier = Modifier.fillMaxWidth()
            )

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Status do pagamento",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (pago) {
                            "Pagamento realizado"
                        } else {
                            "Pagamento pendente"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = pago,

                    onCheckedChange = {
                        pago = it
                    }
                )
            }

            erro?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(

                onClick = {

                    val valorConvertido = valor
                        .replace(",", ".")
                        .toDoubleOrNull()

                    when {

                        participanteSelecionado == null -> {

                            erro = "Selecione um participante."
                        }

                        valorConvertido == null -> {

                            erro = "Informe um valor válido."
                        }

                        valorConvertido <= 0 -> {

                            erro = "O valor deve ser maior que zero."
                        }

                        else -> {

                            repository.atualizarPagamento(

                                pagamento = pagamento.copy(
                                    participanteId = participanteSelecionado!!.id,
                                    participanteNome = participanteSelecionado!!.nome,
                                    valor = valorConvertido,
                                    descricao = descricao,
                                    comprovante = comprovante,
                                    pago = pago
                                ),

                                onSuccess = {
                                    onPagamentoAtualizado()
                                },

                                onError = {

                                    erro = it.message
                                        ?: "Não foi possível atualizar o pagamento."
                                }
                            )
                        }
                    }
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