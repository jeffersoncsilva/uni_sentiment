package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.R;
import com.projetos.redes.modelos.MensagemUsuario;

import java.util.ArrayList;
import java.util.List;

public class MostraMensagensAdapter extends RecyclerView.Adapter<MostraMensagensAdapter.MensagensUsuarioHolder> {
    private List<MensagemUsuario> mensagens;

    public List<MensagemUsuario> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<MensagemUsuario> mensagens) {
        this.mensagens = mensagens;
    }

    private final Context mContext;

    public MostraMensagensAdapter(Context con) {
        this.mensagens = new ArrayList<>();
        this.mContext = con;
    }

    @Override
    public MensagensUsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.rc_mensagem_usuario, parent, false);
        return new MostraMensagensAdapter.MensagensUsuarioHolder(v);
    }

    @Override
    public void onBindViewHolder( MensagensUsuarioHolder holder, int position) {
        MensagemUsuario msg = mensagens.get(position);
        holder.data.setText(msg.getData().toString());
        holder.autor.setText(msg.getAutor());
        holder.mensagem.setText(msg.getMensagem());
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    protected class MensagensUsuarioHolder extends RecyclerView.ViewHolder{
        public TextView autor, data, mensagem;
        public MensagensUsuarioHolder( View v) {
            super(v);
            data = v.findViewById(R.id.txData);
            autor = v.findViewById(R.id.txAutor);
            mensagem = v.findViewById(R.id.txMensagem);
        }
    }

}
