package com.mob.developer.hw1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_row, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
        Coin coin = items.get(position);
        holder.setCoinPrice(coin.getPrice());
        holder.setCoinName(coin.getName());
        holder.setChange1h(coin.getChange1h());
        holder.setChange24h(coin.getChange24h());
        holder.setChange7d(coin.getChange7d());
        holder.setCoinNameAbbr(coin.getAbbrName());
        holder.setImageAddress(coin.getImgAddress());
    }

    @Override public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoinName;
        private TextView txtCoinPrice;
        private TextView coinNameAbbr;
        private TextView change1h;
        private TextView change24h;
        private TextView change7d;
        private ImageView coinImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCoinPrice = itemView.findViewById(R.id.coin_price_lbl);
            txtCoinName = itemView.findViewById(R.id.coin_name_lbl);
            coinNameAbbr = itemView.findViewById(R.id.coin_abbr_name_lbl);
            change1h = itemView.findViewById(R.id.coin_change_1h);
            change24h = itemView.findViewById(R.id.coin_change_24h);
            change7d = itemView.findViewById(R.id.coin_change_7d);
            coinImage = itemView.findViewById(R.id.coin_image);
        }

        public void setCoinName(String coinName) {
            this.txtCoinName.setText(coinName);
        }

        public void setCoinPrice(String coinPrice) {
            this.txtCoinPrice.setText(coinPrice);
        }

        public void setCoinNameAbbr(String CoinNameAbbr) {
            this.coinNameAbbr.setText(CoinNameAbbr);
        }
        public void setImageAddress(String ImageAddress) {
//            this.ImageAddress.setText(ImageAddress);
            //TODO
        }
        public void setChange1h(String Change1h) {
            this.change1h.setText(Change1h);
        }
        public void setChange24h(String Change24h) {
            this.change24h.setText(Change24h);
        }
        public void setChange7d(String Change7d) {
            this.change7d.setText(Change7d);
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