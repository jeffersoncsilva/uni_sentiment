package com.projetos.redes.ui.fragments.tutoriais

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.projetos.redes.R
import com.projetos.redes.databinding.FragmentTempoCapturaDadosRedeBinding
import com.projetos.redes.ui.viewmodels.fragments.SalvaTempoCapturaDadosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SalvaTempoCapturaDadosRedeFragment : Fragment() {
        private lateinit var binding : FragmentTempoCapturaDadosRedeBinding
        private val viewModel: SalvaTempoCapturaDadosViewModel by viewModels()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = FragmentTempoCapturaDadosRedeBinding.inflate(layoutInflater, container, false)
                return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                carregaOpcoesTempoExecucao()
                configuraListenerRadioGroup()
        }

        private fun configuraListenerRadioGroup() {
                binding.tempoExecucao.setOnCheckedChangeListener { group, checkedId ->
                        lifecycleScope.launch {
                                viewModel.salvaOpcaoEscolhida(context, checkedId)
                        }
                }
        }

        private fun carregaOpcoesTempoExecucao() {
                val opcoes = resources.getStringArray(R.array.intervalos_captura_dados_rede)
                var id = 0
                opcoes.forEach { op ->
                        val rb = RadioButton(context)
                        rb.text = op
                        rb.id = id++
                        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                        binding.tempoExecucao.addView(rb, layoutParams)
                }
        }

        override fun onResume() {
                super.onResume()
                lifecycleScope.launch {
                        val opcao = viewModel.carregaOpcaoEscolhida(context)
                        binding.tempoExecucao.check(opcao)
                }
        }
}
