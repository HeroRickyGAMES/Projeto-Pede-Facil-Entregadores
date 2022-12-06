package com.herorickystudios.pedefacil_entregas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class entregasAdapter extends RecyclerView.Adapter<entregasAdapter.MyViewHolder> {

    Context context;
    ArrayList<cardsEntregas> list;


    public entregasAdapter(Context context, ArrayList<cardsEntregas> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_entregas,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        cardsEntregas users = list.get(position);

        holder.textTitulo.setText(users.getTitulo());
        holder.textnomeL.setText(users.getNome());
        holder.textlocalL.setText(users.getLocal());
        holder.textendereçoL.setText(users.getLocalentrega());
        holder.textDistanciadvc.setText(users.getTextDistanciadvc());
        holder.textPreco.setText(users.getPreco());
        holder.textestaAtivo.setText(users.getEstaAtivo());
        holder.textEntreguePor.setText(users.getEntreguePor());
        holder.textuidEntregaor.setText(users.getUidEntregaor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textTitulo, textnomeL, textlocalL, textendereçoL, textDistanciadvc, textPreco, textestaAtivo, textEntreguePor, textuidEntregaor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            textTitulo = itemView.findViewById(R.id.textTitulo);
            textnomeL = itemView.findViewById(R.id.textProduto);
            textlocalL = itemView.findViewById(R.id.textLocal);
            textendereçoL = itemView.findViewById(R.id.textLocalEntrega);
            textDistanciadvc = itemView.findViewById(R.id.textDistanciadvc);
            textPreco = itemView.findViewById(R.id.textPreco);
            textestaAtivo = itemView.findViewById(R.id.textestaAtivo);
            textEntreguePor = itemView.findViewById(R.id.textEntreguePor);
            textuidEntregaor = itemView.findViewById(R.id.textuidEntregaor);

            //st = mensage.getText().toString();

        }

        @Override
        public void onClick(View v) {

        }
    }
}
