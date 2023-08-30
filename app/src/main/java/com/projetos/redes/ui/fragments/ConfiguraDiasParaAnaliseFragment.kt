package com.projetos.redes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.projetos.redes.R
import com.projetos.redes.databinding.FragmentTempoMinimoExecucaoBinding
import com.projetos.redes.ui.viewmodels.ConfiguraDiasParaAnaliseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfiguraDiasParaAnaliseFragment : Fragment() {
        private lateinit var binding: FragmentTempoMinimoExecucaoBinding
        private val viewModel: ConfiguraDiasParaAnaliseViewModel by viewModels()

        override fun onCreateView(inflater: LayoutInflater,  container: ViewGroup?, savedInstanceState: Bundle?): View?{
                binding = FragmentTempoMinimoExecucaoBinding.inflate(layoutInflater, container, false)
                adicionaOpcoesDeDias()
                return binding.root
        }

        override fun onResume() {
                super.onResume()

                setaChangeListenerOpcoesDias()
        }

        private fun adicionaOpcoesDeDias(){
                val dias = resources.getStringArray(R.array.diasMinimosParaAnalisar)
                var idd = 0
                dias.map {
                        val rb = RadioButton(context)
                        rb.setText(it)
                        rb.id = idd
                        val params = RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
                        binding.diasExecucao.addView(rb, params)
                        idd+= 1
                }
        }

        private fun setaChangeListenerOpcoesDias(){
                binding.diasExecucao.setOnCheckedChangeListener{ rg, id ->
                        viewModel.salvaOpcaoEscolhida(context, id)
                }
        }
}