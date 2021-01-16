package com.projetos.redes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.adapters.UsoDeInternetAdapter;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.UsoDeInternet;
import java.util.ArrayList;
import java.util.List;

public class UsoDeInternetActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rc_netusage;
    private static final String tag = "NetUsageActivity";
    private UsoDeInternetAdapter adapter;
    private Button bt_reloadData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netusage);

        rc_netusage = findViewById(R.id.rc_data);
        adapter = new UsoDeInternetAdapter(new ArrayList<UsoDeInternet>(), this);
        rc_netusage.setLayoutManager( new LinearLayoutManager(this)) ;
        rc_netusage.setAdapter(adapter);
        bt_reloadData = findViewById(R.id.bt_usodados);
        bt_reloadData.setOnClickListener(this);
        new CarregaDadosDoBanco(getApplicationContext()).execute();
        setTitle("UniSentiment - Consumo Internet");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.ic_help){
            Intent in = new Intent(getApplicationContext(), AjudaActivity.class);
            Bundle extra = new Bundle();
            extra.putString(AjudaActivity.AJUDA_KEY, getString(R.string.btAjudaUsoDeInternet));
            in.putExtras(extra);
            startActivity(in);
        }else if(item.getItemId() == R.id.ic_contato){
            Intent contato = new Intent(getApplicationContext(), ContatoActivity.class);
            startActivity(contato);
        }else if(item.getItemId() == R.id.tutorial){
            Intent in = new Intent(getApplicationContext(), TutorialActivity.class);
            Utils.DesativarPularTutorial(getApplicationContext());
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_usodados){
            new CarregaDadosDoBanco(getApplicationContext()).execute();
        }
    }

    protected class CarregaDadosDoBanco extends AsyncTask<Void, Void, Void>{
        private final Context context;
        List<UsoDeInternet> c = new ArrayList<>();

        public CarregaDadosDoBanco(Context con){
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reloadData.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BancoDeDados banco = new BancoDeDados(context);
            c = banco.pegarDadosUsoInternet();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setLst(c);
            adapter.notifyDataSetChanged();
            bt_reloadData.setEnabled(true);
        }
    }
}
