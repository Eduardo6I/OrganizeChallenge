package com.example.organizechallenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.organizechallenge.R;
import com.example.organizechallenge.model.Equipe;

import java.util.List;

public class AdapterEquipe extends RecyclerView.Adapter<AdapterEquipe.MyViewHolder> {

    List<Equipe> equipes;
    Context context;

    public AdapterEquipe(List<Equipe> equipes, Context context) {
        this.equipes = equipes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_torneio, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Equipe equipe = equipes.get(position);

        holder.nome.setText(equipe.getNome());
        holder.nome.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

    }


    @Override
    public int getItemCount() {
        return equipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textAdapterNomeEquipe);
        }

    }

}