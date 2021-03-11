package com.mob.developer.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CoinOHLC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Toast.makeText(getApplicationContext(),intent.getStringExtra("name"),Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_coin_ohlc);
    }
}