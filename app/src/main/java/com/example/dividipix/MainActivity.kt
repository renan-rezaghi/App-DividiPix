package com.example.dividipix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dividipix.data.model.Conta
import com.example.dividipix.data.model.Pagamento
import com.example.dividipix.data.model.Participante
import com.example.dividipix.repository.FirestoreRepository
import com.example.dividipix.ui.screens.InicioScreen
import com.example.dividipix.ui.screens.TelaDetalhesConta
import com.example.dividipix.ui.screens.TelaEditarConta
import com.example.dividipix.ui.screens.TelaEditarPagamento
import com.example.dividipix.ui.screens.TelaEditarParticipante
import com.example.dividipix.ui.screens.TelaNovaConta
import com.example.dividipix.ui.screens.TelaNovoPagamento
import com.example.dividipix.ui.screens.TelaNovoParticipante
import com.example.dividipix.ui.screens.TelaPagamentos
import com.example.dividipix.ui.screens.TelaParticipantes
import com.example.dividipix.ui.theme.DividiPixTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DividiPixTheme {
                DividiPixApp()
            }
        }
    }
}

@Composable
fun DividiPixApp() {

    val repository = remember {
        FirestoreRepository()
    }

    var telaAtual by remember {
        mutableStateOf("inicio")
    }

    var contaSelecionada by remember {
        mutableStateOf<Conta?>(null)
    }

    var participanteSelecionado by remember {
        mutableStateOf<Participante?>(null)
    }

    var pagamentoSelecionado by remember {
        mutableStateOf<Pagamento?>(null)
    }

    when (telaAtual) {

        // =====================================================
        // INÍCIO
        // =====================================================

        "inicio" -> {

            InicioScreen(
                repository = repository,

                onNovaConta = {
                    telaAtual = "nova_conta"
                },

                onAbrirConta = { conta ->
                    contaSelecionada = conta
                    telaAtual = "detalhes"
                }
            )
        }

        // =====================================================
        // CONTAS
        // =====================================================

        "nova_conta" -> {

            TelaNovaConta(
                repository = repository,

                onVoltar = {
                    telaAtual = "inicio"
                },

                onContaCriada = {
                    telaAtual = "inicio"
                }
            )
        }

        "editar_conta" -> {

            contaSelecionada?.let { conta ->

                TelaEditarConta(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "detalhes"
                    },

                    onContaAtualizada = {
                        telaAtual = "inicio"
                    }
                )
            }
        }

        "detalhes" -> {

            contaSelecionada?.let { conta ->

                TelaDetalhesConta(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "inicio"
                    },

                    onEditar = {
                        telaAtual = "editar_conta"
                    },

                    onExcluir = {
                        contaSelecionada = null
                        telaAtual = "inicio"
                    },

                    onParticipantes = {
                        telaAtual = "participantes"
                    },

                    onPagamentos = {
                        telaAtual = "pagamentos"
                    }
                )
            }
        }

        // =====================================================
        // PARTICIPANTES
        // =====================================================

        "participantes" -> {

            contaSelecionada?.let { conta ->

                TelaParticipantes(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "detalhes"
                    },

                    onNovoParticipante = {
                        telaAtual = "novo_participante"
                    },

                    onEditarParticipante = { participante ->
                        participanteSelecionado = participante
                        telaAtual = "editar_participante"
                    }
                )
            }
        }

        "novo_participante" -> {

            contaSelecionada?.let { conta ->

                TelaNovoParticipante(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "participantes"
                    },

                    onParticipanteCriado = {
                        telaAtual = "participantes"
                    }
                )
            }
        }

        "editar_participante" -> {

            participanteSelecionado?.let { participante ->

                TelaEditarParticipante(
                    repository = repository,
                    participante = participante,

                    onVoltar = {
                        telaAtual = "participantes"
                    },

                    onParticipanteAtualizado = {
                        participanteSelecionado = null
                        telaAtual = "participantes"
                    }
                )
            }
        }

        // =====================================================
        // PAGAMENTOS
        // =====================================================

        "pagamentos" -> {

            contaSelecionada?.let { conta ->

                TelaPagamentos(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "detalhes"
                    },

                    onNovoPagamento = {
                        telaAtual = "novo_pagamento"
                    },

                    onEditarPagamento = { pagamento ->
                        pagamentoSelecionado = pagamento
                        telaAtual = "editar_pagamento"
                    }
                )
            }
        }

        "novo_pagamento" -> {

            contaSelecionada?.let { conta ->

                TelaNovoPagamento(
                    repository = repository,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "pagamentos"
                    },

                    onPagamentoCriado = {
                        telaAtual = "pagamentos"
                    }
                )
            }
        }

        "editar_pagamento" -> {

            val conta = contaSelecionada
            val pagamento = pagamentoSelecionado

            if (conta != null && pagamento != null) {

                TelaEditarPagamento(
                    repository = repository,
                    pagamento = pagamento,
                    conta = conta,

                    onVoltar = {
                        telaAtual = "pagamentos"
                    },

                    onPagamentoAtualizado = {
                        pagamentoSelecionado = null
                        telaAtual = "pagamentos"
                    }
                )
            }
        }
    }
}