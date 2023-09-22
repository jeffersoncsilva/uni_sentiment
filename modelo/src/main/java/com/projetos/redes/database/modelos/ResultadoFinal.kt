package com.projetos.redes.database.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projetos.redes.database.modelos.enums.MinutosConsideradoDaAnalise
import com.projetos.redes.database.modelos.enums.Sentimento
import java.time.LocalDateTime

@Entity(tableName = "resultado_final")
data class ResultadoFinal(
    @PrimaryKey(autoGenerate = true)  val id: Int,
    val data: LocalDateTime,
   // @Embedded val consumo: ConsumoInternet,
    val sentimento: Sentimento,
    val minutosCapiturado: MinutosConsideradoDaAnalise)
