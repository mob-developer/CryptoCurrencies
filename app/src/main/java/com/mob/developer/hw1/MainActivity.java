package com.mob.developer.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;

/*
* coin market cap api key:
* 1dfc3423-a3cb-4aea-802e-5a7ee6b24d2d
*
* */

public class MainActivity extends AppCompatActivity {
    private ArrayList<Coin> coinArrayList;
    private RecyclerView rvCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        /*
        * replace generateData() with:
        * load data from cache and show loading
        * get data from api in new thread and fetch it in UI
        * */
        generateData();

        setData();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                coinArrayList.add(new Coin("ByteCoin!", "20,000 $", "2020"));
                setData();

            }
        };
        handler.postDelayed(runnable,2000);

    }

    private void init() {
        coinArrayList = new ArrayList<>();
        rvCoins = findViewById(R.id.rv_coins);
    }

    private void generateData() {
        coinArrayList.add(new Coin("bitcoin", "200 $", "2021"));
    }

    private void setData() {
        rvCoins.setLayoutManager(new LinearLayoutManager(this));
        rvCoins.setAdapter(new Adapter(this, coinArrayList));
    }
}