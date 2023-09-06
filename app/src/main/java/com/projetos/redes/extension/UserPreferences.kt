package com.projetos.redes.extension

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
val PREF_TEMPO = intPreferencesKey("tempo_captura_dados")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="user_settings")