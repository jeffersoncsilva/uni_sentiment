package com.projetos.redes.ui.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projetos.redes.enums.DiasAnterioresParaAnalise
import com.projetos.redes.extension.dataStore
import com.projetos.redes.ui.fragments.DIAS_PARA_ANALISAR
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfiguraDiasParaAnaliseViewModel @Inject constructor() : ViewModel() {

        fun salvaOpcaoEscolhida(context: Context?, id: Int)  {
                viewModelScope.launch {
                        context?.dataStore?.edit { settings ->
                                val dias = DiasAnterioresParaAnalise.factory(id)
                                settings[DIAS_PARA_ANALISAR] = "${dias.id}"
                        }
                }
        }
}