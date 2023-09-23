package com.projetos.redes.database.converters

import org.junit.Assert.*
import org.junit.Test
import java.lang.NullPointerException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class DateConvertersTest{
    val converter: DateConverters = DateConverters()
    @Test
    fun `quando passado um valor em long, deve ser retornado um LocalDateTime com a data correspondente`(){
        val dataEmLong = LocalDateTime.now().minusDays(44).toEpochSecond(ZoneOffset.UTC)

        val localDate = converter.toLocalDateTime(dataEmLong)

        assertEquals(LocalDateTime.now().minusDays(44).truncatedTo(ChronoUnit.SECONDS), localDate)
    }

    @Test
    fun `quando passado um LocalDateTime, deve ser retornado um valor em long com a data correspondente`(){
        val data = LocalDateTime.now()

        val dataEmLong = converter.fromLocalDateTime(data)

        assertEquals(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), dataEmLong)

    }

    @Test(expected = NullPointerException::class)
    fun `quando passar uma data em null para converter para LocalDateTime, deve retornar um nullPointerException`(){
        converter.toLocalDateTime(null)
    }

    @Test(expected = NullPointerException::class)
    fun `quando passar uma data em null para converter para Long, deve retornar um nullPointerException`(){
        converter.fromLocalDateTime(null)
    }
}