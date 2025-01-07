package br.fatec.appfinanceiro.repository

import br.fatec.appfinanceiro.model.Renda

interface RendaRepository {
    suspend fun salvarRenda(renda: Renda)
    suspend fun obterRenda(id: Long): Renda?
    suspend fun obterRendaAtual(): Renda?
    suspend fun atualizarRenda(renda: Renda)
    suspend fun deletarRenda(id: Long)
}