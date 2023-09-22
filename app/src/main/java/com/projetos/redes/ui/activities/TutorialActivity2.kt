package com.projetos.redes.ui.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.projetos.redes.databinding.ActivityTutorialBinding

class TutorialActivity2 : AppCompatActivity() {

    private lateinit  var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setuUpTutorialFragments()
    }

    fun setuUpTutorialFragments(){
        val navController = binding.navContainer.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}