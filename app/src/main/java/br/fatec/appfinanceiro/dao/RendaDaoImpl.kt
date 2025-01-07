package br.fatec.appfinanceiro.dao

import br.fatec.appfinanceiro.model.Renda
import br.fatec.appfinanceiro.repository.RendaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RendaDaoImpl : RendaRepository {
    private var rendaAtual: Renda? = null
    private var nextId = 1L

    override suspend fun salvarRenda(renda: Renda): Unit = withContext(Dispatchers.IO) {
        rendaAtual = renda.copy(id = nextId++)
    }

    override suspend fun obterRenda(id: Long): Renda? = withContext(Dispatchers.IO) {
        if (rendaAtual?.id == id) rendaAtual else null
    }

    override suspend fun obterRendaAtual(): Renda? = withContext(Dispatchers.IO) {
        rendaAtual
    }

    override suspend fun atualizarRenda(renda: Renda): Unit = withContext(Dispatchers.IO) {
        if (rendaAtual?.id == renda.id) {
            rendaAtual = renda
        }
    }

    override suspend fun deletarRenda(id: Long): Unit = withContext(Dispatchers.IO) {
        if (rendaAtual?.id == id) {
            rendaAtual = null
        }
    }
}