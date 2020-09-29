package com.projetos.redes.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.adapters.LexicoResultAdapter;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.LexicoResult;
import java.util.ArrayList;
import java.util.List;

public class LexicoResultActivity extends AppCompatActivity {
    private LexicoResultAdapter adapter;
    private RecyclerView rc_lexico;
    private Button bt_reload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lexico_result);
        adapter = new LexicoResultAdapter(this);
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

    protected class LexicoResultTask extends AsyncTask<Void, Void, Void>{
        Context c;
        List<LexicoResult>  results = null;

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
            LexicoDb db = new LexicoDb(c);
            results = db.getLexicoResult();
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
