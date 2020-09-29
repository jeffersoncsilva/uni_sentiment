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
import com.projetos.redes.adapters.UserMessangesAdapter;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.UsrMsg;
import java.util.List;

public class ListUsrMsgs extends AppCompatActivity {
    private Button bt_reload;
    private RecyclerView rview;
    UserMessangesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lstusrmsg);
        bt_reload = findViewById(R.id.bt_reload_msgs);
        bt_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetUserMessages(getApplicationContext()).execute();
            }
        });
        new GetUserMessages(getApplicationContext()).execute();
        rview = findViewById(R.id.rc_lst_msgs);
        adapter = new UserMessangesAdapter(this);
        rview.setAdapter(adapter);
        rview.setLayoutManager(new LinearLayoutManager(this));
    }

    protected class GetUserMessages extends AsyncTask<Void, Void, Void>{
        List<UsrMsg> lst = null;
        Context context;

        public GetUserMessages(Context con){
            context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bt_reload.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            LexicoDb db = new LexicoDb(context);
            lst = db.getUserMsgs();
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
