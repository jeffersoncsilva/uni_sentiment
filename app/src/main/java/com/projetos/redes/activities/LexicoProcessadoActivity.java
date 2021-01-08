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
import com.projetos.redes.adapters.LexicoProcessadoAdapter;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;
import java.util.ArrayList;
import java.util.List;

public class LexicoProcessadoActivity extends AppCompatActivity {
    private LexicoProcessadoAdapter adapter;
    private RecyclerView rc_lexico;
    private Button bt_reload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lexico_result);
        adapter = new LexicoProcessadoAdapter(this);
        rc_lexico = findViewById(R.id.rc_lexico);
        rc_lexico.setAdapter(adapter);
        rc_lexico.setLayoutManager(new LinearLayoutManager(this));
        bt_reload = findViewById(R.id.bt_reload_lexico);
        bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LexicoResultTask(getApplicationContext()).execute();
            }
        });
        new LexicoResultTask(getApplicationContext()).execute();
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
            extra.putString("ajuda_nome", getString(R.string.btAjudaResultadoLexico));
            in.putExtras(extra);
            startActivity(in);
        }else if(item.getItemId() == R.id.ic_contato){
            Intent contato = new Intent(getApplicationContext(), ContatoActivity.class);
            startActivity(contato);
        }
        return super.onOptionsItemSelected(item);
    }

    protected class LexicoResultTask extends AsyncTask<Void, Void, Void>{
        Context c;
        List<ResultadoLexicoProcessado>  results;

        public LexicoResultTask(Context con){
            c = con;
            results = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reload.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BancoDeDados db = new BancoDeDados(c);
            results = db.pegarResultadoLexico();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setData(results);
            adapter.notifyDataSetChanged();
            bt_reload.setEnabled(true);
        }
    }
}
