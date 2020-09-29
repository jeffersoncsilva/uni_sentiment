package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.models.LexicoResult;
import java.util.ArrayList;
import java.util.List;

public class LexicoResultAdapter extends RecyclerView.Adapter<LexicoResultAdapter.HolderLexico> {

    private Context con;
    private List<LexicoResult> data;

    public LexicoResultAdapter(Context con){
        this.con = con;
        data = new ArrayList<>();
    }

    public void setData(List<LexicoResult> d){
        this.data = d;
    }

    @NonNull
    @Override
    public HolderLexico onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.con).inflate(R.layout.element_message, parent, false);
        return new HolderLexico(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderLexico h, int p) {
        LexicoResult r = data.get(p);
        h.tx_msg.setText(r.getFrase());
        h.tx_saldo.setText(String.format(con.getString(R.string.tx_result_ls), r.getSentimento()));
        h.tx_data.setText(r.getData());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class HolderLexico extends RecyclerView.ViewHolder{
        public TextView tx_msg, tx_saldo, tx_data;
        public HolderLexico(View v){
            super(v);
            tx_msg = v.findViewById(R.id.txMsg);
            tx_saldo = v.findViewById(R.id.txRes);
            tx_data = v.findViewById(R.id.txDate);
        }

    }
}
