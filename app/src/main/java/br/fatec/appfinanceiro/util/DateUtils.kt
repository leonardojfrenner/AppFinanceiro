package br.fatec.appfinanceiro.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    fun formatarData(data: Date): String {
        return dateFormatter.format(data)
    }

    fun formatarDataHora(data: Date): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR")).format(data)
    }

    fun formatarDataCompleta(data: Date): String {
        return SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")).format(data)
    }
}