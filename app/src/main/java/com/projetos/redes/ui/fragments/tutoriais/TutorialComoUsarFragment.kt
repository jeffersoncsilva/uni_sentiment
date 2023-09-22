package com.projetos.redes.ui.fragments.tutoriais

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projetos.redes.databinding.FragmentComoUsarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialComoUsarFragment : Fragment(){
        private lateinit var binding: FragmentComoUsarBinding

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = FragmentComoUsarBinding.inflate(layoutInflater, container, false)
                return binding.root
        }



}