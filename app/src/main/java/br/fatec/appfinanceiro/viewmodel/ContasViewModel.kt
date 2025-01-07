package br.fatec.appfinanceiro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.ContaImpl
import java.util.Date

class ContasViewModel : ViewModel() {
    private val contaImpl = ContaImpl()
    private val _contas = MutableLiveData<List<Conta>>()
    val contas: LiveData<List<Conta>> = _contas

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        atualizarContas()
    }

    fun atualizarContas() {
        _loading.value = true
        val contasAtualizadas = contaImpl.listarContas()
        _contas.value = contasAtualizadas.filter {
            // Filtrar contas dos próximos 7 dias
            val hoje = Date()
            val diasEmMillis = 7 * 24 * 60 * 60 * 1000L
            it.dataVencimento.time <= hoje.time + diasEmMillis
        }.sortedBy { it.dataVencimento }
        _loading.value = false
    }

    fun adicionarConta(conta: Conta) {
        contaImpl.adicionarConta(conta)
        atualizarContas()
    }

    fun marcarComoPaga(conta: Conta) {
        contaImpl.marcarComoPaga(conta.id)
        atualizarContas()
    }
}