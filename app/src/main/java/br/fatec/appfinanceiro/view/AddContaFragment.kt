package br.fatec.appfinanceiro.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import br.fatec.appfinanceiro.R
import br.fatec.appfinanceiro.databinding.FragmentAddContaBinding
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.ContaImpl
import br.fatec.appfinanceiro.model.StatusConta
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import br.fatec.appfinanceiro.viewmodel.ContasViewModel

class AddContaFragment : Fragment() {
    private var _binding: FragmentAddContaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContasViewModel by activityViewModels()
    private val contaImpl = ContaImpl()
    private var dataVencimentoSelecionada: Date? = null
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoriaSpinner()
        setupDatePicker()
        setupSalvarButton()
    }

    private fun setupCategoriaSpinner() {
        val categorias = resources.getStringArray(R.array.categorias)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categorias
        )
        binding.spinnerCategoria.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        binding.edtDataVencimento.setOnClickListener {
            val calendar = Calendar.getInstance()

            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    dataVencimentoSelecionada = calendar.time
                    binding.edtDataVencimento.setText(dateFormatter.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupSalvarButton() {
        binding.btnSalvar.setOnClickListener {
            if (validarCampos()) {
                salvarConta()
            }
        }
    }

    private fun validarCampos(): Boolean {
        var isValid = true
        binding.apply {
            if (edtNomeConta.text.isNullOrBlank()) {
                tilNomeConta.error = "Campo obrigatório"
                isValid = false
            } else {
                tilNomeConta.error = null
            }

            if (edtValor.text.isNullOrBlank()) {
                tilValor.error = "Campo obrigatório"
                isValid = false
            } else {
                tilValor.error = null
            }

            if (dataVencimentoSelecionada == null) {
                tilDataVencimento.error = "Campo obrigatório"
                isValid = false
            } else {
                tilDataVencimento.error = null
            }

            if (spinnerCategoria.text.isNullOrBlank()) {
                tilCategoria.error = "Campo obrigatório"
                isValid = false
            } else {
                tilCategoria.error = null
            }
        }
        return isValid
    }

    private fun salvarConta() {
        try {
            val novaConta = Conta(
                nome = binding.edtNomeConta.text.toString(),
                valor = binding.edtValor.text.toString().toDouble(),
                dataVencimento = dataVencimentoSelecionada!!,
                categoria = binding.spinnerCategoria.text.toString(),
                observacoes = binding.edtObservacoes.text?.toString(),
                status = StatusConta.PENDENTE
            )

            viewModel.adicionarConta(novaConta)
            Toast.makeText(context, "Conta salva com sucesso!", Toast.LENGTH_SHORT).show()
            limparCampos()

        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao salvar conta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limparCampos() {
        binding.apply {
            edtNomeConta.text?.clear()
            edtValor.text?.clear()
            edtDataVencimento.text?.clear()
            spinnerCategoria.text?.clear()
            edtObservacoes.text?.clear()
            dataVencimentoSelecionada = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}