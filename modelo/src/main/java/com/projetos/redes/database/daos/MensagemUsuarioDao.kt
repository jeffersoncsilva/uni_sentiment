package com.projetos.redes.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.projetos.redes.database.modelos.MensagemUsuario

@Dao
interface MensagemUsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insereMensagem(mensagem: MensagemUsuario) : Long
}