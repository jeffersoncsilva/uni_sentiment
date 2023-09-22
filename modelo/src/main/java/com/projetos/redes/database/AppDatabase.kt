package com.projetos.redes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projetos.redes.database.converters.DateConverters
import com.projetos.redes.database.daos.ConsumoInternetDao
import com.projetos.redes.database.daos.MensagemUsuarioDao
import com.projetos.redes.database.daos.ResultadoAposProcessamentoDao
import com.projetos.redes.database.daos.ResultadoFinalDao
import com.projetos.redes.database.modelos.ConsumoInternet
import com.projetos.redes.database.modelos.MensagemUsuario
import com.projetos.redes.database.modelos.ResultadoAnaliseDeUmaMensagem
import com.projetos.redes.database.modelos.ResultadoFinal

@Database(entities = [
    ConsumoInternet::class,
    MensagemUsuario::class,
    ResultadoAnaliseDeUmaMensagem::class,
    ResultadoFinal::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun consumoInternetDao(): ConsumoInternetDao
    abstract fun mensagemUsuarioDao(): MensagemUsuarioDao
    abstract fun resultadoAposProcessamentoDao(): ResultadoAposProcessamentoDao
    abstract fun resultadoFinalLexicoDao(): ResultadoFinalDao

}