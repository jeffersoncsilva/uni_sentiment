package com.projetos.redes.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.projetos.redes.fragments.tutoriais.FinalisaEConfiguraIdentificacaoFragment;
import com.projetos.redes.fragments.tutoriais.TempoCapturaDadosRedeFragment;
import com.projetos.redes.fragments.tutoriais.ExplicacaoAutorizacoesFragment;
import com.projetos.redes.ui.fragments.ConfiguraDiasParaAnaliseFragment;
import com.projetos.redes.ui.fragments.IntroducaoFragment;
import com.projetos.redes.ui.fragments.TutorialComoUsarFragment;

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
                return new ExplicacaoAutorizacoesFragment();
            case 3:
                return new TempoCapturaDadosRedeFragment();
            case 4:
                return new ConfiguraDiasParaAnaliseFragment();
            case 5:
                return new FinalisaEConfiguraIdentificacaoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }



}
