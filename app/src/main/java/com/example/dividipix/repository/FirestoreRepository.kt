package com.example.dividipix.repository

import com.example.dividipix.data.model.Conta
import com.example.dividipix.data.model.Pagamento
import com.example.dividipix.data.model.Participante
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // =========================================================
    // CONTAS
    // =========================================================

    fun criarConta(
        conta: Conta,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "nome" to conta.nome,
            "descricao" to conta.descricao,
            "valorTotal" to conta.valorTotal,
            "data" to conta.data
        )

        db.collection("contas")
            .add(data)
            .addOnSuccessListener { documento ->
                onSuccess(documento.id)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun listarContas(
        onSuccess: (List<Conta>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("contas")
            .get()
            .addOnSuccessListener { resultado ->

                val contas = resultado.documents.map { documento ->
                    Conta(
                        id = documento.id,
                        nome = documento.getString("nome") ?: "",
                        descricao = documento.getString("descricao") ?: "",
                        valorTotal = documento.getDouble("valorTotal") ?: 0.0,
                        data = documento.getString("data") ?: ""
                    )
                }

                onSuccess(contas)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun atualizarConta(
        conta: Conta,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "nome" to conta.nome,
            "descricao" to conta.descricao,
            "valorTotal" to conta.valorTotal,
            "data" to conta.data
        )

        db.collection("contas")
            .document(conta.id)
            .update(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun excluirConta(
        contaId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("contas")
            .document(contaId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    // =========================================================
    // PARTICIPANTES
    // =========================================================

    fun criarParticipante(
        participante: Participante,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "contaId" to participante.contaId,
            "nome" to participante.nome,
            "email" to participante.email
        )

        db.collection("participantes")
            .add(data)
            .addOnSuccessListener { documento ->
                onSuccess(documento.id)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun listarParticipantes(
        contaId: String,
        onSuccess: (List<Participante>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("participantes")
            .whereEqualTo("contaId", contaId)
            .get()
            .addOnSuccessListener { resultado ->

                val participantes = resultado.documents.map { documento ->
                    Participante(
                        id = documento.id,
                        contaId = documento.getString("contaId") ?: "",
                        nome = documento.getString("nome") ?: "",
                        email = documento.getString("email") ?: ""
                    )
                }

                onSuccess(participantes)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun atualizarParticipante(
        participante: Participante,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "contaId" to participante.contaId,
            "nome" to participante.nome,
            "email" to participante.email
        )

        db.collection("participantes")
            .document(participante.id)
            .update(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun excluirParticipante(
        participanteId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("participantes")
            .document(participanteId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    // =========================================================
    // PAGAMENTOS
    // =========================================================

    fun criarPagamento(
        pagamento: Pagamento,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "contaId" to pagamento.contaId,
            "participanteId" to pagamento.participanteId,
            "participanteNome" to pagamento.participanteNome,
            "valor" to pagamento.valor,
            "descricao" to pagamento.descricao,
            "comprovante" to pagamento.comprovante,
            "pago" to pagamento.pago
        )

        db.collection("pagamentos")
            .add(data)
            .addOnSuccessListener { documento ->
                onSuccess(documento.id)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun listarPagamentos(
        contaId: String,
        onSuccess: (List<Pagamento>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("pagamentos")
            .whereEqualTo("contaId", contaId)
            .get()
            .addOnSuccessListener { resultado ->

                val pagamentos = resultado.documents.map { documento ->
                    Pagamento(
                        id = documento.id,
                        contaId = documento.getString("contaId") ?: "",
                        participanteId = documento.getString("participanteId") ?: "",
                        participanteNome = documento.getString("participanteNome") ?: "",
                        valor = documento.getDouble("valor") ?: 0.0,
                        descricao = documento.getString("descricao") ?: "",
                        comprovante = documento.getString("comprovante") ?: "",
                        pago = documento.getBoolean("pago") ?: false
                    )
                }

                onSuccess(pagamentos)
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun atualizarPagamento(
        pagamento: Pagamento,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val data: Map<String, Any> = mapOf(
            "contaId" to pagamento.contaId,
            "participanteId" to pagamento.participanteId,
            "participanteNome" to pagamento.participanteNome,
            "valor" to pagamento.valor,
            "descricao" to pagamento.descricao,
            "comprovante" to pagamento.comprovante,
            "pago" to pagamento.pago
        )

        db.collection("pagamentos")
            .document(pagamento.id)
            .update(data)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }

    fun excluirPagamento(
        pagamentoId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        db.collection("pagamentos")
            .document(pagamentoId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { erro ->
                onError(erro)
            }
    }
}