package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.Utils;
import com.projetos.redes.models.ResultadoFinal;
import java.util.ArrayList;
import java.util.List;


public class ResultadoFinalAdapter extends RecyclerView.Adapter<ResultadoFinalAdapter.FimAdapter>{
    private List<ResultadoFinal> data;
    private Context context;

    public ResultadoFinalAdapter(Context con){
        this.context = con;
        data = new ArrayList<>();
    }

    public void setData(List<ResultadoFinal> lst){
        this.data = lst;
    }

    public List<ResultadoFinal> getItems(){ return this.data;}

    @NonNull
    @Override
    public FimAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.rc_element_resultado_final, parent, false);
        return new FimAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FimAdapter h, int position) {
        ResultadoFinal rf = data.get(position);
        h.tx_sentimento.setText(rf.final_res);
        /*h.tx_inicio.setText(String.format(context.getString(R.string.dt_inicio),rf.getDtInicio()));
        h.tx_fim.setText(String.format(context.getString(R.string.dt_fim),rf.getDtFim()));
        h.tx_sentimento.setText(String.format(context.getString(R.string.tx_sentimento), rf.getSentimento().toString()));
        h.tx_dados.setText((context.getString(R.string.tx_total) + Utils.convertMb(rf.getTotalBytes())));*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class FimAdapter extends RecyclerView.ViewHolder{
        protected TextView tx_inicio, tx_fim, tx_sentimento, tx_dados;
        public FimAdapter(View v){
            super(v);
            tx_inicio = v.findViewById(R.id.tx_data_inicio);
            tx_fim = v.findViewById(R.id.tx_data_fim);
            tx_sentimento = v.findViewById(R.id.tx_sentimento);
            tx_dados = v.findViewById(R.id.tx_total_consumo);
        }
    }
}
