package br.fatec.appfinanceiro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.fatec.appfinanceiro.dao.ContaDaoImpl
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.ContaImpl
import br.fatec.appfinanceiro.model.StatusConta
import java.util.Date
import kotlinx.coroutines.launch

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

    fun marcarComoPaga(conta: Conta, dataPagamento: Date) {
        val contaDao = ContaDaoImpl()
        viewModelScope.launch {
            val contaAtualizada = conta.copy(
                status = StatusConta.PAGO,
                dataPagamento = dataPagamento
            )
            contaDao.atualizarConta(contaAtualizada)
            atualizarContas()
        }
    }
}