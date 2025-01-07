package br.fatec.appfinanceiro.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.fatec.appfinanceiro.R
import br.fatec.appfinanceiro.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isValueVisible = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVisibilityButton()
    }

    private fun setupVisibilityButton() {
        binding.btnVisibility.setOnClickListener {
            isValueVisible = !isValueVisible
            updateValueVisibility()
        }
    }

    private fun updateValueVisibility() {
        binding.apply {
            if (isValueVisible) {
                tvValorRenda.text = "R$ 1.234,56" // Seu valor real aqui
                btnVisibility.setImageResource(R.drawable.ic_visibility)
            } else {
                tvValorRenda.text = "R$ ●●●●●"
                btnVisibility.setImageResource(R.drawable.ic_visibility_off)
            }
        }
    }
}