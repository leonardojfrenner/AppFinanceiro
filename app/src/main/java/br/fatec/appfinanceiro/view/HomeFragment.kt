package br.fatec.appfinanceiro.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.fatec.appfinanceiro.R
import br.fatec.appfinanceiro.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        setupVisibilityButton()
        setupRecyclerView()
    }

    private fun setupVisibilityButton() {
        binding.btnVisibility.setOnClickListener {
            // Implementar lógica de visibilidade
        }
    }

    private fun setupRecyclerView() {
        binding.rvContasProximas.apply {
            layoutManager = LinearLayoutManager(context)
            // Adicionar adapter quando estiver pronto
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}