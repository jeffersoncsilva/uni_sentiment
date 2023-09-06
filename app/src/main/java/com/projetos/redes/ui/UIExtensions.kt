package com.projetos.redes.ui

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.projetos.redes.R

fun Fragment.toast(message: String, duration: Int  = Toast.LENGTH_LONG){
        Toast.makeText(requireContext(), message, duration).show()
}

fun mostraDialogReadPhoneState(context: Context?, solicitaPermissao: () -> Unit) {
        if(context == null)
                return
        val alert = AlertDialog.Builder(context)
        alert.setTitle(context.getString(R.string.permisao_acesso_telefone))
        alert.setMessage(context.getString(R.string.permisao_acesso_telefone_descricao))
        alert.setPositiveButton(context.getString(R.string.permission_authorize)) { dialog, which ->
                solicitaPermissao()
        }
        alert.show()
}