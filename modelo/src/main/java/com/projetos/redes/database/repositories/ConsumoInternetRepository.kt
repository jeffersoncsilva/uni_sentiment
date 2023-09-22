package com.projetos.redes.database.repositories

import com.projetos.redes.database.daos.ConsumoInternetDao
import com.projetos.redes.database.modelos.ConsumoInternet
import java.time.LocalDateTime
import javax.inject.Inject

class ConsumoInternetRepository @Inject constructor(private val dao: ConsumoInternetDao){

    suspend fun insereConsumo(consumo: ConsumoInternet) = dao.insereUmConsumo(consumo)

    suspend fun recuperaTodosOsConsumos() = dao.recuperaTodosOsConsumos()

    suspend fun recuperaConsumoEntre(dataInicial: LocalDateTime, dataFinal: LocalDateTime) : List<ConsumoInternet>{
        val inicio = dataInicial.toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = dataFinal.toEpochSecond(java.time.ZoneOffset.UTC)
        return dao.recuperaConsumoEntre(inicio, fim)
    }

    suspend fun recuperaConsumoTotalDeWifiEntre(dataInicial: LocalDateTime, dataFinal: LocalDateTime) : Long{
        val inicio = dataInicial.toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = dataFinal.toEpochSecond(java.time.ZoneOffset.UTC)
        return dao.recuperaConsumoTotalDeWifiEntre(inicio, fim)
    }

    suspend fun recuperaConsumoTotalDeMobileEntre(dataInicial: LocalDateTime, dataFinal: LocalDateTime) : Long{
        val inicio = dataInicial.toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = dataFinal.toEpochSecond(java.time.ZoneOffset.UTC)
        return dao.recuperaConsumoTotalDeMobileEntre(inicio, fim)
    }
}