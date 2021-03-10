package com.mob.developer.hw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Coin item);
    }

    private final List<Coin> items;
    private final OnItemClickListener listener;

    public Adapter(List<Coin> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
        Coin coin = items.get(position);
        holder.setCoinPrice(coin.getPrice());
        holder.setCoinName(coin.getName());
        holder.setYear(coin.getYear());
    }

    @Override public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoinName;
        private TextView txtCoinPrice;
        private TextView txtYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCoinPrice = itemView.findViewById(R.id.txt_coin_price);
            txtCoinName = itemView.findViewById(R.id.txt_coin_name);
            txtYear = itemView.findViewById(R.id.txt_year);
        }

        public void setCoinName(String coinName) {
            this.txtCoinName.setText(coinName);
        }

        public void setCoinPrice(String coinPrice) {
            this.txtCoinPrice.setText(coinPrice);
        }

        public void setYear(String year) {
            this.txtYear.setText(year);
        }


        public void bind(final Coin item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}