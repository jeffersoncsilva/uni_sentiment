package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.modelos.ResultadoLexicoProcessado;
import java.util.ArrayList;
import java.util.List;

public class LexicoProcessadoAdapter extends RecyclerView.Adapter<LexicoProcessadoAdapter.HolderLexico> {

    private final Context con;
    private List<ResultadoLexicoProcessado> data;

    public LexicoProcessadoAdapter(Context con){
        this.con = con;
        data = new ArrayList<>();
    }

    public void setData(List<ResultadoLexicoProcessado> d){
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
        ResultadoLexicoProcessado r = data.get(p);
        h.tx_msg.setText("MENSAGEM: " + r.getFrase() + "\nSENTIMENTO: " + r.getSentimento() + "\nHORA: " + r.getHora()+":"+r.getMinuto());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class HolderLexico extends RecyclerView.ViewHolder{
        public TextView tx_msg;
        public HolderLexico(View v){
            super(v);
            tx_msg = v.findViewById(R.id.txtMsg);
        }

    }
}
