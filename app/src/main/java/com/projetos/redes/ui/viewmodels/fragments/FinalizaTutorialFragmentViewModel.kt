package com.projetos.redes.ui.viewmodels.fragments

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.projetos.redes.extension.dataStore
import com.projetos.redes.ui.fragments.ID_USUARIO
import com.projetos.redes.ui.fragments.TUTORIAL_JA_VISTO

class FinalizaTutorialFragmentViewModel : ViewModel() {

        suspend fun salvaTutorialJaVisto(context: Context?){
                context?.dataStore?.edit { settings ->
                        settings[TUTORIAL_JA_VISTO] = true
                }
        }

        suspend fun salvaIdentificadorUsuario(context: Context?, idUsuario: String) {
                context?.dataStore?.edit { settings ->
                        settings[ID_USUARIO] = idUsuario
                }
        }
}