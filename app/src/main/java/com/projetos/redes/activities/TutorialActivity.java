package com.projetos.redes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.adapters.TutorialPagerAdapter;

import static com.projetos.redes.Utils.CONFIG;
import static com.projetos.redes.Utils.JA_VIU_TUTORIAL;

public class TutorialActivity extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tab;
    private TutorialPagerAdapter adapter = new TutorialPagerAdapter(getSupportFragmentManager());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        SharedPreferences sp = getSharedPreferences(CONFIG, MODE_PRIVATE);
        if(sp.getBoolean(JA_VIU_TUTORIAL, false)){
            Intent mainAct = new Intent(this, MainActivity.class);
            startActivity(mainAct);
        }
        pager = findViewById(R.id.page_fragments);
        pager.setAdapter(adapter);
        tab = findViewById(R.id.menu_pager);
        tab.setupWithViewPager(pager);
        setTitle("UniSentiment - Introdução");
        for(int i = 0; i < tab.getTabCount(); i++)
            tab.getTabAt(i).setIcon(getDrawable(R.drawable.circle_desable));
        tab.getTabAt(0).setIcon(getDrawable(R.drawable.circle_enabled));
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        setTitle("UniSentiment - Introdução");
                        break;
                    case 1:
                        setTitle("UniSentiment - Como Funciona?");
                        break;
                    case 2:
                        setTitle("UniSentiment - Autorizações");
                        break;
                    case 3:
                        setTitle("UniSentiment - Tempo Execução");
                        break;
                    case 4:
                        setTitle("UniSentiment - Fim");
                        break;
                }
                tab.setIcon(getDrawable(R.drawable.circle_enabled));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(getDrawable(R.drawable.circle_desable));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}