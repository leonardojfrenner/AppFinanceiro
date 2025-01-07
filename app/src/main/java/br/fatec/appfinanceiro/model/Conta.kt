package br.fatec.appfinanceiro.model

import java.util.Date

data class Conta(
    val id: Long = 0,
    val nome: String,
    val valor: Double,
    val dataVencimento: Date,
    val dataPagamento: Date? = null,
    val categoria: String,
    val observacoes: String? = null,
    val comprovante: String? = null,
    val status: StatusConta = StatusConta.PENDENTE
)