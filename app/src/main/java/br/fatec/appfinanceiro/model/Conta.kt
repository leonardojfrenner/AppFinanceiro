package br.fatec.appfinanceiro.model

data class Conta(
    val id: Long = 0,
    val nome: String,
    val valor: Double,
    val dataVencimento: Long,
    val dataPagamento: Long? = null,
    val categoria: String,
    val observacoes: String? = null,
    val comprovante: String? = null, // Aqui vamos guardar o URI do arquivo posteriormente
    val status: StatusConta = StatusConta.PENDENTE
)

enum class StatusConta {
    PAGO,
    PENDENTE,
    ATRASADO
}