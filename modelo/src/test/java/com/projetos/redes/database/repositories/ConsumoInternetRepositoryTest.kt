package com.projetos.redes.database.repositories

import com.projetos.redes.database.daos.ConsumoInternetDao
import com.projetos.redes.database.modelos.ConsumoInternet
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDateTime

class ConsumoInternetRepositoryTest{
    val dao = mockk<ConsumoInternetDao>()
    val repository = ConsumoInternetRepository(dao)

    @Test
    fun `quando inserir um novo consumo, deve ser retornado a quantidade de linhas inseridas`()  {
        val consumo = ConsumoInternet(1, 1L, 1L, LocalDateTime.now(), LocalDateTime.now())
        coEvery {  dao.insereUmConsumo(any()) } returns 1L
        runBlocking {
            val retorno = repository.insereConsumo(mockk())
            assertEquals(1L, retorno)
        }
    }

    @Test
    fun `quando recuperar todos os consumos, uma lista contendo todos os consumos deve ser retornada`() {
        val lista = listOf<ConsumoInternet>(mockk(), mockk(), mockk())
        coEvery { dao.recuperaTodosOsConsumos() } returns lista
        runBlocking {
            val retorno = repository.recuperaTodosOsConsumos()
            assertEquals(lista.size, retorno.size)
        }
    }

    @Test
    fun `quando passar uma data de inicio e uma data de fim, deve retonar todos os consumos que estao nesse intervalo passado`(){
        val lista = listOf(ConsumoInternet(1, 1L, 1L, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1)),
            ConsumoInternet(2, 1L, 1L, LocalDateTime.now().minusHours(3), LocalDateTime.now().minusHours(2)),
            ConsumoInternet(3, 1L, 1L, LocalDateTime.now().minusHours(6), LocalDateTime.now().minusHours(2)))
        val dataInicial = LocalDateTime.now().minusHours(7)
        val dataFinal = LocalDateTime.now()
        coEvery { dao.recuperaConsumoEntre(any(), any()) } returns lista
        runBlocking {
            val retorno = repository.recuperaConsumoEntre(dataInicial, dataFinal)
            assertEquals(3, retorno.size)
        }

    }

    @Test
    fun `quando passar uma data de inicio e fim, deve retornar o total de consumo de internet wifi nesse intervalo`(){
        val dataInicial = LocalDateTime.now().minusHours(7)
        val dataFinal = LocalDateTime.now()
        coEvery { dao.recuperaConsumoTotalDeWifiEntre(any(), any()) } returns 100L
        runBlocking {
            val retorno = repository.recuperaConsumoTotalDeWifiEntre(dataInicial, dataFinal)
            assertEquals(100L, retorno)
        }
    }

    @Test
    fun `quando passar uma data de inicio e fim, deve retornar o total de consumo de internet mobile nesse intervalo`(){
        val dataInicial = LocalDateTime.now().minusHours(7)
        val dataFinal = LocalDateTime.now()
        coEvery { dao.recuperaConsumoTotalDeMobileEntre(any(), any()) } returns 100L
        runBlocking {
            val retorno = repository.recuperaConsumoTotalDeMobileEntre(dataInicial, dataFinal)
            assertEquals(100L, retorno)
        }
    }
}