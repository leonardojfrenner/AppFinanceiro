package br.fatec.appfinanceiro.repository

import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.StatusConta
import java.util.Date

interface ContaRepository {
    suspend fun adicionarConta(conta: Conta)
    suspend fun atualizarConta(conta: Conta)
    suspend fun obterConta(id: Long): Conta?
    suspend fun listarContas(): List<Conta>
    suspend fun listarContasPorStatus(status: StatusConta): List<Conta>
    suspend fun listarContasVencidas(): List<Conta>
    suspend fun deletarConta(id: Long)
    suspend fun marcarComoPaga(id: Long)
}