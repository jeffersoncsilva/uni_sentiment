package com.projetos.redes.database.modelos

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "mensagem_usuario")
data class MensagemUsuario(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val mensagem: String,
    val data: LocalDateTime)
