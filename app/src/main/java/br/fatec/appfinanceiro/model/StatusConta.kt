package br.fatec.appfinanceiro.model

enum class StatusConta(val descricao: String) {
    PAGO("Pago"),
    PENDENTE("Pendente"),
    ATRASADO("Atrasado")
}