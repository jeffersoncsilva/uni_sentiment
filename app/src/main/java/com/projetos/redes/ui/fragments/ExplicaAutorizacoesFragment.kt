package com.projetos.redes.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.projetos.redes.R
import com.projetos.redes.databinding.FragmentAutorizacoesBinding
import com.projetos.redes.permissions.REQUEST_CODE_PERMISSION
import com.projetos.redes.permissions.solicitaPermissaoAcessoRedeMovel
import com.projetos.redes.permissions.temPermissaoAcessoDadosRedeMovel
import com.projetos.redes.permissions.temPermissaoAcessoDadosTelefone
import com.projetos.redes.ui.mostraDialogReadPhoneState
import com.projetos.redes.ui.toast

class ExplicaAutorizacoesFragment : Fragment(){
        private lateinit var binding: FragmentAutorizacoesBinding

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = FragmentAutorizacoesBinding.inflate(layoutInflater, container, false)
                return binding.root
        }

        override fun onStart() {
                super.onStart()
                if(temPermissaoAcessoDadosTelefone(context)){
                        desabilitaBotao(binding.autorizarWiFi)
                }
                if(temPermissaoAcessoDadosRedeMovel(context)){
                        desabilitaBotao(binding.autorizarDadosMoveis)
                }
                configuraListenersBotoesDePermisao()
        }

        private fun desabilitaBotao(botao: View){
                botao.isEnabled = false
                botao.background = context?.getDrawable(R.drawable.botao_padrao_desabilitado)
        }

        private fun configuraListenersBotoesDePermisao(){
                binding.autorizarWiFi.setOnClickListener {
                        solicitaPermissaoAcessoRedeMovel(context)
                }
                binding.autorizarDadosMoveis.setOnClickListener {
                        solicitaPermicaoDeAcessoADadosDeTelefone()
                }
        }

        private fun solicitaPermicaoDeAcessoADadosDeTelefone() {
                when{
                        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ->{
                                toast("Permissão concedida")
                                desabilitaBotao(binding.autorizarDadosMoveis)
                        }
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) -> {
                                toast("Permissão necessária para correto funcionamento do app.")
                                mostraDialogReadPhoneState(context, ::solicitaPermissao)
                        }
                        else -> {
                                solicitaPermissao()
                                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_CODE_PERMISSION)
                        }
                }

        }

        fun solicitaPermissao(){
                activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_CODE_PERMISSION) }
        }
}