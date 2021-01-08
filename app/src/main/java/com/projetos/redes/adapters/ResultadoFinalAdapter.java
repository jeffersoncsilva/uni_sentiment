package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.modelos.ResultadoFinalLexico;
import java.util.ArrayList;
import java.util.List;


public class ResultadoFinalAdapter extends RecyclerView.Adapter<ResultadoFinalAdapter.FimAdapter>{
    private List<ResultadoFinalLexico> data;
    private final Context context;

    public ResultadoFinalAdapter(Context con){
        this.context = con;
        data = new ArrayList<>();
    }

    public void setData(List<ResultadoFinalLexico> lst){
        this.data = lst;
    }

    public List<ResultadoFinalLexico> getItems(){ return this.data;}

    @NonNull
    @Override
    public FimAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.rc_element_resultado_final, parent, false);
        return new FimAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FimAdapter h, int position) {
        ResultadoFinalLexico rf = data.get(position);
        h.tx_sentimento.setText(rf.resultadoParaEnviar());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class FimAdapter extends RecyclerView.ViewHolder{
        protected TextView tx_sentimento;
        public FimAdapter(View v){
            super(v);
            tx_sentimento = v.findViewById(R.id.tx_sentimento);
        }
    }
}
