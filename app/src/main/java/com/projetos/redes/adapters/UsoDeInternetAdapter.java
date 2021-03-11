package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projetos.redes.R;
import com.projetos.redes.modelos.ConsumoInternet;

import java.util.List;

public class UsoDeInternetAdapter extends RecyclerView.Adapter<UsoDeInternetAdapter.HolderNet> {
    private List<ConsumoInternet> data;
    private final Context c;

    public UsoDeInternetAdapter(List<ConsumoInternet> data, Context con){
        this.data = data;
        this.c = con;
    }

    public void setLst(List<ConsumoInternet> lsr){
        this.data = lsr;
    }

    @NonNull
    @Override
    public HolderNet onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.rc_element_netusage, parent, false);
        return new HolderNet(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNet h, int position) {
        ConsumoInternet nu = data.get(position);
        h.dt_inicio.setText(nu.toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String convertMb(long size){
        long n = 1000;
        String s = "";
        double kb = size / n;
        double mb = kb / n;
        double gb = mb / n;
        double tb = gb / n;
        if(size < n) {
            s = size + " Bytes";
        } else if(size >= n && size < (n * n)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (n * n) && size < (n * n * n)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (n * n * n) && size < (n * n * n * n)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (n * n * n * n)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

    protected class HolderNet extends RecyclerView.ViewHolder{
        public TextView dt_inicio;
        public HolderNet(@NonNull View v){
            super(v);
            dt_inicio = v.findViewById(R.id.tx_dt_inicio);
        }
    }
}