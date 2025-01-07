package br.fatec.appfinanceiro.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.fatec.appfinanceiro.R
import br.fatec.appfinanceiro.adapter.ContasAdapter
import br.fatec.appfinanceiro.databinding.FragmentHomeBinding
import br.fatec.appfinanceiro.databinding.DialogDetalhesContaBinding
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.util.DateUtils
import br.fatec.appfinanceiro.viewmodel.ContasViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContasViewModel by activityViewModels()
    private lateinit var contasAdapter: ContasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupVisibilityButton()
        setupSwipeRefresh()
        setupObservers()
    }

    private fun setupRecyclerView() {
        contasAdapter = ContasAdapter { conta ->
            mostrarDialogDetalhes(conta)
        }

        binding.rvContasProximas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contasAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.atualizarContas()
        }
    }

    private fun setupObservers() {
        viewModel.contas.observe(viewLifecycleOwner) { contas ->
            contasAdapter.updateContas(contas)
            binding.rvContasProximas.visibility = if (contas.isEmpty()) View.GONE else View.VISIBLE
            binding.tvEmpty.visibility = if (contas.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }

    private fun setupVisibilityButton() {
        var isValueVisible = true
        binding.btnVisibility.setOnClickListener {
            isValueVisible = !isValueVisible
            if (isValueVisible) {
                binding.tvValorRenda.text = "R$ 1.234,56"
                binding.btnVisibility.setImageResource(R.drawable.ic_visibility)
            } else {
                binding.tvValorRenda.text = "R$ ●●●●●"
                binding.btnVisibility.setImageResource(R.drawable.ic_visibility_off)
            }
        }
    }

    private fun mostrarDialogDetalhes(conta: Conta) {
        val dialogBinding = DialogDetalhesContaBinding.inflate(layoutInflater)
        var dataPagamentoSelecionada: Date? = null

        dialogBinding.apply {
            tvNomeConta.text = conta.nome
            tvValor.text = "R$ ${String.format("%.2f", conta.valor)}"
            tvDataVencimento.text = "Vencimento: ${DateUtils.formatarData(conta.dataVencimento)}"
            tvCategoria.text = "Categoria: ${conta.categoria}"
            tvStatus.text = "Status: ${conta.status.descricao}"
            conta.observacoes?.let {
                tvObservacoes.text = "Observações: $it"
                tvObservacoes.visibility = View.VISIBLE
            }

            // Configurar campo de data
            edtDataPagamento.apply {
                setText(DateUtils.formatarData(Date()))
                setOnClickListener {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        requireContext(),
                        R.style.MaterialCalendarTheme,
                        { _, year, month, day ->
                            calendar.set(year, month, day)
                            dataPagamentoSelecionada = calendar.time
                            setText(DateUtils.formatarData(calendar.time))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }

        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setTitle("Detalhes da Conta")
            .setView(dialogBinding.root)
            .setPositiveButton("Marcar como Paga") { _, _ ->
                viewModel.marcarComoPaga(conta, dataPagamentoSelecionada ?: Date())
            }
            .setNegativeButton("Fechar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}