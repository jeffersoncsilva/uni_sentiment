package com.projetos.redes.ui.viewmodels.fragments

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.projetos.redes.extension.PREF_TEMPO
import com.projetos.redes.extension.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SalvaTempoCapturaDadosViewModel: ViewModel() {
       suspend  fun carregaOpcaoEscolhida(context: Context?)  : Int{
                val result = context?.dataStore?.data?.map{ prefs ->
                        prefs[PREF_TEMPO] ?: 0
                }
                result.apply {

                }
               return result?.first() ?: 0
       }

        suspend fun salvaOpcaoEscolhida(context: Context?, opcao: Int){
                context?.dataStore?.edit { settings ->
                        settings[PREF_TEMPO] = opcao
                }
        }
}