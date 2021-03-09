package com.mob.developer.hw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<Coin> coinList;

    public Adapter(Context context, ArrayList<Coin> coinList) {
        this.context = context;
        this.coinList = coinList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coin coin = coinList.get(position);
        holder.setCoinPrice(coin.getPrice());
        holder.setCoinName(coin.getName());
        holder.setYear(coin.getYear());
    }

    @Override
    public int getItemCount() {
        return coinList == null ? 0 : coinList.size();
    }
}