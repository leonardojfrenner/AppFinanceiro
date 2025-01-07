package br.fatec.appfinanceiro.dao

import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.StatusConta
import br.fatec.appfinanceiro.repository.ContaRepository
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContaDaoImpl : ContaRepository {
    private val contas = mutableListOf<Conta>()
    private var nextId = 1L

    override suspend fun adicionarConta(conta: Conta): Unit = withContext(Dispatchers.IO) {
        val novaConta = conta.copy(id = nextId++)
        contas.add(novaConta)
    }

    override suspend fun atualizarConta(conta: Conta): Unit = withContext(Dispatchers.IO) {
        val index = contas.indexOfFirst { it.id == conta.id }
        if (index != -1) {
            contas[index] = conta
        }
    }

    override suspend fun obterConta(id: Long): Conta? = withContext(Dispatchers.IO) {
        contas.find { it.id == id }
    }

    override suspend fun listarContas(): List<Conta> = withContext(Dispatchers.IO) {
        contas.toList()
    }

    override suspend fun listarContasPorStatus(status: StatusConta): List<Conta> = withContext(Dispatchers.IO) {
        contas.filter { it.status == status }
    }

    override suspend fun listarContasVencidas(): List<Conta> = withContext(Dispatchers.IO) {
        val hoje = Date()
        contas.filter {
            it.status == StatusConta.PENDENTE && it.dataVencimento.before(hoje)
        }
    }

    override suspend fun deletarConta(id: Long): Unit = withContext(Dispatchers.IO) {
        contas.removeIf { it.id == id }
    }

    override suspend fun marcarComoPaga(id: Long): Unit = withContext(Dispatchers.IO) {
        val conta = obterConta(id)
        conta?.let {
            val contaAtualizada = it.copy(
                status = StatusConta.PAGO,
                dataPagamento = Date()
            )
            atualizarConta(contaAtualizada)
        }
    }
}