package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.R;
import com.projetos.redes.bd.LexicoDb;
import com.projetos.redes.models.LexicoResult;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.Holder> {
    private List<LexicoResult> data;
    private Context context;

    public MessagesAdapter(Context context){
        this.context = context;
        LexicoDb ldb = new LexicoDb(context);
        data = ldb.getLexicoResult();
    }

    @NonNull
    @Override
    public MessagesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.element_message, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.Holder holder, int p) {
        LexicoResult lr = data.get(p);
        holder.txDate.setText(lr.getData());
        holder.txMsg.setText(lr.getFrase());
        holder.txLexico.setText(lr.getSentimento().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class Holder extends RecyclerView.ViewHolder{
        public TextView txMsg, txDate, txLexico;
        public Holder(@NonNull View v) {
            super(v);
            txMsg = v.findViewById(R.id.txMsg);
            txDate = v.findViewById(R.id.txDate);
            txLexico = v.findViewById(R.id.txRes);
        }
    }
}
