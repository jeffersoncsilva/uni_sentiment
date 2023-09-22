package com.projetos.redes.ui.fragments.tutoriais

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.projetos.redes.activities.MainActivity
import com.projetos.redes.databinding.FragmentFinalisaEConfiguraIdentificacaoBinding
import com.projetos.redes.ui.viewmodels.fragments.FinalizaTutorialFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FinalizaTutorialFragment : Fragment() {
        private lateinit var binding : FragmentFinalisaEConfiguraIdentificacaoBinding
        private val viewModel : FinalizaTutorialFragmentViewModel by viewModels()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = FragmentFinalisaEConfiguraIdentificacaoBinding.inflate(layoutInflater)
                return binding.root
        }

        override fun onResume() {
                super.onResume()
                binding.btFinaliza.setOnClickListener {
                        lifecycleScope.launch {
                                viewModel.salvaTutorialJaVisto(context)
                                viewModel.salvaIdentificadorUsuario(context, binding.edtIdUsuario.text.toString())
                        }
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                }
        }
}