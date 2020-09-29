package com.projetos.redes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projetos.redes.R;
import com.projetos.redes.models.UsrMsg;
import java.util.ArrayList;
import java.util.List;

public class UserMessangesAdapter extends RecyclerView.Adapter<UserMessangesAdapter.HolderMsg>{
    private Context context;
    private List<UsrMsg> data;

    public UserMessangesAdapter(Context con){
        data  = new ArrayList<>();
        this.context = con;
    }

    public void setData(List<UsrMsg> d){
        this.data = d;
    }

    @NonNull
    @Override
    public HolderMsg onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.rc_element_usr_msg, parent, false);
        return new HolderMsg(v);
    }

    @Override
    public void onBindViewHolder(HolderMsg h, int p) {
        UsrMsg m = data.get(p);
        h.tx_data.setText(m.getDate());
        h.tx_msg.setText(m.getMsg());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    protected class HolderMsg extends RecyclerView.ViewHolder{
        private TextView tx_data, tx_msg;

        public HolderMsg(View v){
            super(v);
            tx_data = v.findViewById(R.id.tx_msg_date);
            tx_msg = v.findViewById(R.id.tx_msg);
        }
    }
}
