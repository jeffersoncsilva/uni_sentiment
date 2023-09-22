package com.projetos.redes.database.di

import android.content.Context
import androidx.room.ProvidedTypeConverter
import androidx.room.Room
import com.projetos.redes.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

const val DATABASE_NAME = "unisentiment.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideConsumoInternetDao(appDatabase: AppDatabase) = appDatabase.consumoInternetDao()

    @Provides
    fun provideMensagemUsuarioDao(appDatabase: AppDatabase) = appDatabase.mensagemUsuarioDao()

    @Provides
    fun provideResultadoAposProcessamentoDao(appDatabase: AppDatabase) = appDatabase.resultadoAposProcessamentoDao()

    @Provides
    fun provideResultadoFinalDao(appDatabase: AppDatabase) = appDatabase.resultadoFinalLexicoDao()
}