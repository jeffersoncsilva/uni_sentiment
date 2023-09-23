package com.projetos.redes.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.projetos.redes.database.daos.ConsumoInternetDao
import com.projetos.redes.database.modelos.ConsumoInternet
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest{
    private lateinit var database: AppDatabase
    private lateinit var consumoInternetDao: ConsumoInternetDao

    @Before
    fun setupDatabase(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java).build()
        consumoInternetDao = database.consumoInternetDao()
    }

    @After
    fun closeDatabase(){
        database.close()
    }

    @Test
    fun quando_insere_um_consumo_de_internet_deve_retornar_a_quantidade_de_linhas_inseridas() {
        val consumo = ConsumoInternet(1, 1L, 1L, LocalDateTime.now(), LocalDateTime.now())
        val retorno = consumoInternetDao.insereUmConsumo(consumo)
        assertEquals(1L, retorno)
    }


    @Test
    fun quando_inserido_3_registros_no_banco_de_dados_deve_ser_recuperado_os_tres_registros_inseridos(){
        val reg1 = getConsumoInternet(1, 1)
        val reg2 = getConsumoInternet(2, 2)
        val reg3 = getConsumoInternet(3, 3)

        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val consumos = consumoInternetDao.recuperaTodosOsConsumos()

        assertEquals(3, consumos.size)
        assertEquals(reg1, consumos[0])
        assertEquals(reg2, consumos[1])
        assertEquals(reg3, consumos[2])
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_deve_ser_retornado_apenas_os_que_estao_no_intervalo_especificado(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(2, 2)
        val reg3 = getConsumoInternet(1,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val inseridos = consumoInternetDao.recuperaTodosOsConsumos()

        assertEquals(3, inseridos.size)

        val consumos = consumoInternetDao.recuperaConsumoEntre(inicio, fim)

        assertEquals(2, consumos.size)
        assertEquals(reg2, consumos[0])
        assertEquals(reg3, consumos[1])
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_nenhum_deve_ser_retornado_se_nao_estiverem_no_intervalo_especificado(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(5, 2)
        val reg3 = getConsumoInternet(7,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val consumos = consumoInternetDao.recuperaConsumoEntre(inicio, fim)

        assertEquals(0, consumos.size)
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_deve_ser_retornado_o_total_de_wifi_consumido_no_intervalo_especificado_que_nesse_caso_e_zero(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(5, 2)
        val reg3 = getConsumoInternet(7,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val total = consumoInternetDao.recuperaConsumoTotalDeWifiEntre(inicio, fim)

        assertEquals(0L, total)
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_deve_ser_retornado_o_total_de_wifi_consumido_no_intervalo_especificado_que_nesse_caso_e_dois(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(2, 2)
        val reg3 = getConsumoInternet(1,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val total = consumoInternetDao.recuperaConsumoTotalDeWifiEntre(inicio, fim)

        assertEquals(2L, total)
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_deve_ser_retornado_o_total_de_mobile_consumido_no_intervalo_especificado_que_nesse_caso_e_zero(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(5, 2)
        val reg3 = getConsumoInternet(7,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val total = consumoInternetDao.recuperaConsumoTotalDeMobileEntre(inicio, fim)

        assertEquals(0L, total)
    }

    @Test
    fun quando_inserido_3_registro_no_banco_de_dados_deve_ser_retornado_o_total_de_mobile_consumido_no_intervalo_especificado_que_nesse_caso_e_dois(){
        val reg1 = getConsumoInternet(8, 1)
        val reg2 = getConsumoInternet(2, 2)
        val reg3 = getConsumoInternet(1,3 )
        consumoInternetDao.insereUmConsumo(reg1)
        consumoInternetDao.insereUmConsumo(reg2)
        consumoInternetDao.insereUmConsumo(reg3)

        val inicio = LocalDateTime.now().minusHours(3).toEpochSecond(java.time.ZoneOffset.UTC)
        val fim = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC)

        val total = consumoInternetDao.recuperaConsumoTotalDeMobileEntre(inicio, fim)

        assertEquals(2L, total)
    }


    fun getConsumoInternet(horas: Int, id : Int? = null): ConsumoInternet {
        return ConsumoInternet( id,1L, 1L, LocalDateTime.now().minusHours(horas.toLong()).truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
    }
}