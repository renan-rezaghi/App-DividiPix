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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividipix.data.model.Conta
import com.example.dividipix.repository.FirestoreRepository
import com.example.dividipix.ui.components.CardConta
import com.example.dividipix.ui.theme.Branco
import com.example.dividipix.ui.theme.VerdePix

@Composable
fun InicioScreen(
    repository: FirestoreRepository,
    onNovaConta: () -> Unit,
    onAbrirConta: (Conta) -> Unit
) {
    var contas by remember {
        mutableStateOf<List<Conta>>(emptyList())
    }

    var carregando by remember {
        mutableStateOf(true)
    }

    var erro by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {

        repository.listarContas(

            onSuccess = {
                contas = it
                carregando = false
            },

            onError = {
                erro = it.message
                carregando = false
            }
        )
    }

    val valorTotal = contas.sumOf {
        it.valorTotal
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "DividiPix",
                        color = Branco,
                        style = MaterialTheme.typography.titleLarge
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
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =====================================================
            // CABEÇALHO
            // =====================================================

            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "Olá! 👋",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Organize suas despesas compartilhadas.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // =====================================================
            // RESUMO
            // =====================================================

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    horizontalArrangement = Arrangement.SpaceBetween,

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "Contas cadastradas",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = contas.size.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "Valor total",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "R$ %.2f".format(valorTotal),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // =====================================================
            // BOTÃO NOVA CONTA
            // =====================================================

            Button(
                onClick = onNovaConta,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("＋  Nova conta")
            }

            // =====================================================
            // LISTA
            // =====================================================

            Text(
                text = "Minhas contas",
                style = MaterialTheme.typography.titleLarge
            )

            when {

                carregando -> {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator()
                    }
                }

                erro != null -> {

                    Text(
                        text = "Erro ao carregar contas: $erro",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                contas.isEmpty() -> {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "Nenhuma conta ainda",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Crie sua primeira conta compartilhada para começar a dividir suas despesas.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),

                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = contas,
                            key = { it.id }
                        ) { conta ->

                            CardConta(
                                conta = conta,

                                onClick = {
                                    onAbrirConta(conta)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}