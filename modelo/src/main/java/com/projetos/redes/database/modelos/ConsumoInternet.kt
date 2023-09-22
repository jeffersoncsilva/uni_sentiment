package com.projetos.redes.database.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "consumo_internet")
data class ConsumoInternet(
            @PrimaryKey(autoGenerate = true) val id: Int,
            val wifi: Long ,
            val mobile: Long,
            val dataInicio: LocalDateTime? = LocalDateTime.now(),
            val dataFim: LocalDateTime? = LocalDateTime.now())