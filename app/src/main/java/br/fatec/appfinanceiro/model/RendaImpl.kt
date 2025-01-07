package br.fatec.appfinanceiro.model

class RendaImpl {
    private val _rendas = mutableListOf<Renda>()

    fun adicionarRenda(renda: Renda) {
        _rendas.add(renda)
    }

    fun atualizarRenda(renda: Renda) {
        val index = _rendas.indexOfFirst { it.id == renda.id }
        if (index != -1) {
            _rendas[index] = renda
        }
    }

    fun obterRenda(id: Long): Renda? {
        return _rendas.find { it.id == id }
    }

    fun listarRendas(): List<Renda> {
        return _rendas.toList()
    }

    fun deletarRenda(id: Long) {
        _rendas.removeIf { it.id == id }
    }
}