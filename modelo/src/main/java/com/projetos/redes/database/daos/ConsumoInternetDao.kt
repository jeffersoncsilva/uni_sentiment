package com.projetos.redes.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.projetos.redes.database.modelos.ConsumoInternet

@Dao
interface ConsumoInternetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insereUmConsumo(consumoInternet: ConsumoInternet) : Long

    @Query("SELECT * FROM consumo_internet")
     fun recuperaTodosOsConsumos() : List<ConsumoInternet>

    @Query("SELECT * FROM consumo_internet WHERE dataInicio > :dataInicial AND dataFim < :dataFinal")
     fun recuperaConsumoEntre(dataInicial: Long, dataFinal: Long): List<ConsumoInternet>

    @Query("SELECT SUM(wifi) FROM consumo_internet WHERE dataInicio > :dataInicial AND dataFim < :dataFinal")
     fun recuperaConsumoTotalDeWifiEntre(dataInicial: Long, dataFinal: Long): Long

    @Query("SELECT sum(mobile) FROM consumo_internet WHERE dataInicio > :dataInicial AND dataFim < :dataFinal")
     fun recuperaConsumoTotalDeMobileEntre(dataInicial: Long, dataFinal: Long): Long
}