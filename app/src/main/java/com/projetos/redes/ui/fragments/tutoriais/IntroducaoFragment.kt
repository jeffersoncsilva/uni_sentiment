package com.projetos.redes.ui.fragments.tutoriais

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projetos.redes.R
import com.projetos.redes.databinding.FragmentIntroducaoBinding
import com.projetos.redes.ui.toast

class IntroducaoFragment : Fragment() {
        private lateinit var binding: FragmentIntroducaoBinding

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = FragmentIntroducaoBinding.inflate(layoutInflater, container, false)
                return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                configuraBotaoEnviarEmail()
        }

        private fun configuraBotaoEnviarEmail(){
                binding.btEntrarEmContato.setOnClickListener {
                        enviarEmail()
                }
        }

        private fun enviarEmail(){
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto")
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_padrao))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contato_participar))
                val pm = requireContext().packageManager
                if(pm != null){
                        if(intent.resolveActivity(pm!!) != null)
                                startActivity(intent)
                } else {
                        toast(getString(R.string.aplicativoEmailNaoInstalado))
                }
        }
}