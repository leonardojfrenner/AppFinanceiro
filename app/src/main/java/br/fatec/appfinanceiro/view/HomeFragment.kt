package br.fatec.appfinanceiro.view

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
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.util.DateUtils
import br.fatec.appfinanceiro.util.DateUtils.formatarData
import br.fatec.appfinanceiro.viewmodel.ContasViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(conta.nome)
            .setMessage(
                """
                Valor: R$ ${String.format("%.2f", conta.valor)}
                Vencimento: ${DateUtils.formatarData(conta.dataVencimento)}
                Categoria: ${conta.categoria}
                Status: ${conta.status.descricao}
                ${conta.observacoes?.let { "\nObservações: $it" } ?: ""}
                """.trimIndent()
            )
            .setPositiveButton("Marcar como Paga") { _, _ ->
                viewModel.marcarComoPaga(conta)
            }
            .setNegativeButton("Fechar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}