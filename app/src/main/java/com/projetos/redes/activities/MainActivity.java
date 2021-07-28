package com.projetos.redes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.bd.InicializaBancoDeDados;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PERMISSION_READ_STATE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.identificadorUsuarioSalvo(this)) {
            setContentView(R.layout.activity_main);
            findViewById(R.id.btn_VerDadosRedes).setOnClickListener(this);
            findViewById(R.id.btResultadoProcessamentoLexico).setOnClickListener(this);
            findViewById(R.id.btResultadoFinalClassificado).setOnClickListener(this);
            findViewById(R.id.btHelpUsoInternet).setOnClickListener(this);
            findViewById(R.id.btHelpResultadoLexico).setOnClickListener(this);
            findViewById(R.id.btHelpResultadoFinal).setOnClickListener(this);
        } else {
            Toast.makeText(this, "Identificador não foi salvo. Por favor, insira-o ao final do tutorial!", Toast.LENGTH_SHORT).show();
            //Intent in = new Intent(this, TutorialActivity.class);
            //in.putExtra(Utils.MOSTRA_TUTO, true);
            //startActivity(in);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        inicializarBancoDeDados();
    }

    private void inicializarBancoDeDados() {
        InicializaBancoDeDados banco = new InicializaBancoDeDados(this);
        Log.d("MainActivity", "" + banco.existeBancoDeDadosMemoria());
        if (!banco.existeBancoDeDadosMemoria())
            banco.copiaBanco();
    }

    @Override
    public void onClick(View view) {
        Intent in = null;
        Bundle extra = null;
        switch (view.getId()) {
            case R.id.btn_VerDadosRedes:
                in = new Intent(MainActivity.this, UsoDeInternetActivity.class);
                break;
            case R.id.btResultadoProcessamentoLexico:
                in = new Intent(MainActivity.this, LexicoProcessadoActivity.class);
                break;
            case R.id.btResultadoFinalClassificado:
                in = new Intent(MainActivity.this, ResultadoFinalActivity.class);
                break;
            case R.id.btHelpUsoInternet:
                extra = new Bundle();
                extra.putString("ajuda_nome", getString(R.string.btAjudaUsoDeInternet));
                in = new Intent(MainActivity.this, AjudaActivity.class);
                break;
            case R.id.btHelpResultadoLexico:
                extra = new Bundle();
                extra.putString("ajuda_nome", getString(R.string.btAjudaResultadoLexico));
                in = new Intent(MainActivity.this, AjudaActivity.class);
                break;
            case R.id.btHelpResultadoFinal:
                extra = new Bundle();
                extra.putString("ajuda_nome", getString(R.string.btAjudaResultadoFinal));
                in = new Intent(MainActivity.this, AjudaActivity.class);
                break;
        }

        if (extra != null)
            in.putExtras(extra);
        if (in != null)
            startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ic_help) {
            Intent in = new Intent(getApplicationContext(), AjudaActivity.class);
            Bundle extra = new Bundle();
            extra.putString(AjudaActivity.AJUDA_KEY, getString(R.string.btAjudaAplicativo));
            in.putExtras(extra);
            startActivity(in);
        } else if (item.getItemId() == R.id.ic_contato) {
            Intent contact = new Intent(getApplicationContext(), ContatoActivity.class);
            startActivity(contact);
        } else if (item.getItemId() == R.id.tutorial) {
            Intent in = new Intent(getApplicationContext(), TutorialActivity.class);
            Utils.DesativarPularTutorial(getApplicationContext());
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}