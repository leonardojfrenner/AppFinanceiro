package br.fatec.appfinanceiro.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.fatec.appfinanceiro.R
import br.fatec.appfinanceiro.databinding.ItemContaBinding
import br.fatec.appfinanceiro.model.Conta
import br.fatec.appfinanceiro.model.StatusConta
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContasAdapter(
    private val onItemClick: (Conta) -> Unit
) : RecyclerView.Adapter<ContasAdapter.ContaViewHolder>() {

    private var contas = listOf<Conta>()

    fun updateContas(novasContas: List<Conta>) {
        contas = novasContas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContaViewHolder {
        val binding = ItemContaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContaViewHolder, position: Int) {
        holder.bind(contas[position])
    }

    override fun getItemCount() = contas.size

    inner class ContaViewHolder(
        private val binding: ItemContaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(contas[position])
                }
            }
        }

        fun bind(conta: Conta) {
            binding.apply {
                tvNomeConta.text = conta.nome
                tvValor.text = formatarMoeda(conta.valor)
                tvDataVencimento.text = formatarData(conta.dataVencimento)

                // Configurar o chip de status
                chipStatus.apply {
                    text = conta.status.descricao
                    chipBackgroundColor = ColorStateList.valueOf(getStatusColor(conta.status))
                }

                // Configurar cor do valor
                tvValor.setTextColor(ContextCompat.getColor(
                    root.context,
                    R.color.negative_red
                ))
            }
        }

        private fun formatarMoeda(valor: Double): String {
            return "R$ ${String.format("%.2f", valor)}"
        }

        private fun formatarData(data: Date): String {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
            return formato.format(data)
        }

        private fun getStatusColor(status: StatusConta): Int {
            return when (status) {
                StatusConta.PAGO -> R.color.positive_green
                StatusConta.PENDENTE -> R.color.warning_yellow
                StatusConta.ATRASADO -> R.color.negative_red
            }
        }
    }
}