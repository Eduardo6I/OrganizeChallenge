package com.example.organizechallenge.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.organizechallenge.R;
import com.example.organizechallenge.model.Torneio;

import java.util.List;

public class AdapterTorneio extends RecyclerView.Adapter<AdapterTorneio.MyViewHolder> {

    List<Torneio> torneios;
    Context context;

    public AdapterTorneio(List<Torneio> torneios, Context context) {
        this.torneios = torneios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_torneio, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Torneio torneio = torneios.get(position);

        holder.titulo.setText(torneio.getNome());
        holder.data.setText(torneio.getData());
        holder.estilo.setText(torneio.getEstilo());
        holder.data.setTextColor(context.getResources().getColor(R.color.colorAccent));

    }


    @Override
    public int getItemCount() {
        return torneios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, data, estilo;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            data = itemView.findViewById(R.id.textAdapterData);
            estilo = itemView.findViewById(R.id.textAdapterEstilo);
        }

    }

}

