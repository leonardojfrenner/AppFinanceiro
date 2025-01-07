package br.fatec.appfinanceiro.model

class ContaImpl {
    private val _contas = mutableListOf<Conta>()

    fun adicionarConta(conta: Conta) {
        _contas.add(conta)
    }

    fun atualizarConta(conta: Conta) {
        val index = _contas.indexOfFirst { it.id == conta.id }
        if (index != -1) {
            _contas[index] = conta
        }
    }

    fun obterConta(id: Long): Conta? {
        return _contas.find { it.id == id }
    }

    fun listarContas(): List<Conta> {
        return _contas.toList()
    }

    fun listarContasPorStatus(status: StatusConta): List<Conta> {
        return _contas.filter { it.status == status }
    }

    fun listarContasVencidas(): List<Conta> {
        val hoje = System.currentTimeMillis()
        return _contas.filter {
            it.status == StatusConta.PENDENTE && it.dataVencimento < hoje
        }
    }

    fun deletarConta(id: Long) {
        _contas.removeIf { it.id == id }
    }

    fun marcarComoPaga(id: Long) {
        val conta = obterConta(id)
        conta?.let {
            val contaAtualizada = it.copy(
                status = StatusConta.PAGO,
                dataPagamento = System.currentTimeMillis()
            )
            atualizarConta(contaAtualizada)
        }
    }
}