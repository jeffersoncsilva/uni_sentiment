package com.projetos.redes.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.projetos.redes.ui.fragments.tutoriais.ConfiguraDiasParaAnaliseFragment;
import com.projetos.redes.ui.fragments.tutoriais.ExplicaAutorizacoesFragment;
import com.projetos.redes.ui.fragments.tutoriais.FinalizaTutorialFragment;
import com.projetos.redes.ui.fragments.tutoriais.IntroducaoFragment;
import com.projetos.redes.ui.fragments.tutoriais.SalvaTempoCapturaDadosRedeFragment;
import com.projetos.redes.ui.fragments.tutoriais.TutorialComoUsarFragment;

public class TutorialPagerAdapter extends FragmentPagerAdapter {

    public TutorialPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new IntroducaoFragment();
            case 1:
                return new TutorialComoUsarFragment();
            case 2:
                return new ExplicaAutorizacoesFragment();
            case 3:
                return new SalvaTempoCapturaDadosRedeFragment();
            case 4:
                return new ConfiguraDiasParaAnaliseFragment();
            case 5:
                return new FinalizaTutorialFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }



}
