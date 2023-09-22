package com.projetos.redes.database.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateConverters {
    @TypeConverter
    fun toLocalDateTime(value: Long?) : LocalDateTime{
        return LocalDateTime.ofEpochSecond(value!!, 0, java.time.ZoneOffset.UTC)
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?) : Long{
        return value!!.toEpochSecond(java.time.ZoneOffset.UTC)
    }
}