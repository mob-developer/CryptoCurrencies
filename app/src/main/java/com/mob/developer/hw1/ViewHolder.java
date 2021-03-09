package com.mob.developer.hw1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
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
}
