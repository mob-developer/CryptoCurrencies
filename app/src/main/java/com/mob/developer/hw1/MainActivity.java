package com.mob.developer.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    private Adapter.OnItemClickListener listener;

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


        // test: refresh list
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                coinArrayList.add(new Coin("ByteCoin!", "20,000 $", "2020"));
                setData();

            }
        };
        handler.postDelayed(runnable,2000);


        /*
        * set onClickListener for 'load more' button
        * and get data from api in new thread and refresh the list
        *
        * */


        /*
        * set onClick listener for recyclerView
        *
        * */



        

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
        listener = new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coin item) {
//                Toast.makeText(getApplicationContext(),item.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,CoinOHLC.class);
                intent.putExtra("name",item.getName());
                startActivity(intent);
            }
        };
        rvCoins.setAdapter(new Adapter(coinArrayList,listener));
//        rvCoins.setAdapter(new Adapter(this, coinArrayList));
    }

    public enum Range {
        weekly,
        oneMonth,
    }

    public void getCandles(String symbol,Range range) {

        OkHttpClient okHttpClient = new OkHttpClient();

        String miniUrl;
        final String description;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=7"));
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=30"));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl)))
                .newBuilder();

        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", YOUR_COIN_IO_API_KEY)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    //extractCandlesFromResponse(response.body().string(), description);

                }
            }
        });

    }

    // push test!!
}