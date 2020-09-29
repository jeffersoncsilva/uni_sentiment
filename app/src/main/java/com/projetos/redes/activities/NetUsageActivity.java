package com.projetos.redes.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.adapters.NetUsageAdapter;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.NetworkUsage;
import java.util.ArrayList;
import java.util.List;



public class NetUsageActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rc_netusage;
    private static String tag = "NetUsageActivity";
    private NetUsageAdapter adapter;
    private Button bt_reloadData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netusage);

        rc_netusage = findViewById(R.id.rc_data);
        adapter = new NetUsageAdapter(new ArrayList<NetworkUsage>(), this);
        rc_netusage.setLayoutManager( new LinearLayoutManager(this)) ;
        rc_netusage.setAdapter(adapter);
        bt_reloadData = findViewById(R.id.bt_usodados);
        bt_reloadData.setOnClickListener(this);
        new GetUsage(getApplicationContext()).execute();
    }

    @Override
    public void onClick(View view) {
        Log.d(tag, "clique na vier " + view.getId());
        if(view.getId() == R.id.bt_usodados){
            new GetUsage(getApplicationContext()).execute();
        }
    }

    protected class GetUsage extends AsyncTask<Void, Void, Void>{
        private Context context;
        List<NetworkUsage> c;

        public GetUsage(Context con){
            context = con;
            c = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reloadData.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LexicoDb db = new LexicoDb(context);
            c = db.getNetworkUsage();
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
