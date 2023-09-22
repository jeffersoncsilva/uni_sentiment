package com.projetos.redes.database.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projetos.redes.database.modelos.enums.Sentimento
import java.time.LocalDateTime

@Entity("resultado_analise_uma_mensagem")
data class ResultadoAnaliseDeUmaMensagem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val data: LocalDateTime,
    val mensagem: String,
    val sentiment: Sentimento)
