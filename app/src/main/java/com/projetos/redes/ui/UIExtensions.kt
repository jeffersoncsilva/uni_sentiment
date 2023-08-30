package com.projetos.redes.ui

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(message: String, duration: Int  = Toast.LENGTH_LONG){
        Toast.makeText(requireContext(), message, duration).show()
}