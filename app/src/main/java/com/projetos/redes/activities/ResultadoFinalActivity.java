package com.projetos.redes.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.projetos.redes.adapters.ResultadoFinalAdapter;
import com.projetos.redes.bd.BancoDeDados;
import com.projetos.redes.enums.Sentimento;
import com.projetos.redes.modelos.ConsumoInternet;
import com.projetos.redes.modelos.ResultadoFinalLexico;
import com.projetos.redes.task.SendMailTask;

import java.util.ArrayList;
import java.util.List;

public class ResultadoFinalActivity extends AppCompatActivity {
    private RecyclerView rc;
    private ResultadoFinalAdapter adapter;
    private Button bt_reload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_final);
        rc = findViewById(R.id.rc_resultado_final);
        rc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResultadoFinalAdapter(this);
        rc.setAdapter(adapter);
        bt_reload = findViewById(R.id.bt_reload_);
        bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PegarDadosBanco(getBaseContext()).execute();
            }
        });

        findViewById(R.id.btSendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarActivityEnviarEmail();
            }
        });
        setTitle("UniSentiment - Resultado final");
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
            extra.putString("ajuda_nome", getString(R.string.btAjudaResultadoFinal));
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
    protected void onStart() {
        super.onStart();
        new PegarDadosBanco(this).execute();
    }

    private void iniciarActivityEnviarEmail(){
        StringBuilder sb = new StringBuilder();
        sb.append("<html> <body>");
        for(ResultadoFinalLexico rf : adapter.getItems()){
            sb.append(rf.resultadoParaEnviar());
            sb.append("<br>");
        }
        sb.append("</body></html>");
        Log.d("HTMLEMAIL", sb.toString());
        SendMailTask task = new SendMailTask(this, sb.toString());
        task.execute();
    }

    protected class PegarDadosBanco extends AsyncTask<Void, Void, Void>{
        private List<ResultadoFinalLexico> lst = new ArrayList<>();
        private final Context context;

        public PegarDadosBanco(Context con){
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reload.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            BancoDeDados db = new BancoDeDados(context);
            List<ConsumoInternet> consumo = db.pegarDadosUsoInternet();
            for(ConsumoInternet ci : consumo){
                /*
                select count(sentimento) from tb_lexico_result where 	hora = 10 AND minuto >= 15 AND minuto <= 20 GROUP BY sentimento;
                */
                String sql = "select count(sentimento) from tb_lexico_result where hora ="+ ci.getHora() +" AND minuto >= "+ci.getMinuto_inicial()+
                                "+ AND minuto <="+ci.getMinuto_final()+" GROUP BY sentimento;";
                int[] res = db.pegaResultadoSentimento(sql);
                ResultadoFinalLexico rf = new ResultadoFinalLexico(ci.getDia(), ci.getHora(), ci.getMobile()+ci.getWifi(), ci.getMinuto_final()-ci.getMinuto_inicial());
                if(res[0] > res[1])
                    rf.setSentimento(Sentimento.POSITIVO);
                else
                    rf.setSentimento(Sentimento.NEGATIVO);
                lst.add(rf);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setData(lst);
            adapter.notifyDataSetChanged();
            bt_reload.setEnabled(true);
        }
    }
}
