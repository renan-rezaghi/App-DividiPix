package com.example.dividipix.data.model

data class Pagamento(
    val id: String = "",
    val contaId: String = "",
    val participanteId: String = "",
    val participanteNome: String = "",
    val valor: Double = 0.0,
    val descricao: String = "",
    val comprovante: String = "",
    val pago: Boolean = false
)