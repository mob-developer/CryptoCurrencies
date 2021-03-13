package com.mob.developer.hw1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/*
 * coin market cap api key:
 * 1dfc3423-a3cb-4aea-802e-5a7ee6b24d2d
 *
 *
 * coinapi.io api key:
 * 917174EC-0BF3-4365-8C9E-C79741576C25
 * */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Coin> coinArrayList;
    private RecyclerView rvCoins;
    private Button loadMore;
    private Button refresh;
    private Adapter.OnItemClickListener listener;
    private Handler handlerThread;
    private int lastCoin = 1;
    private static final int LOAD_FROM_API = 1;
    private static final int FIRST_LOAD_CACHE = 2;
    private static final int REFRESH_DATA = 3;


    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        generateData(lastCoin, 10);

        loadMore.setOnClickListener(view -> {
            generateData(lastCoin, 5);
        });
        refresh.setOnClickListener(view -> {
            Coin.allCoins = new ArrayList<>();
            generateData(1, lastCoin - 1);
        });



        //TODO add load data from cache


        handlerThread = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == LOAD_FROM_API) {
                    if (msg.arg1 == 1) {
                        coinArrayList = Coin.allCoins;
                        setData();
                    } else {
                        Log.e("mylog", "error in handle msg");
                    }
                }
            }
        };


    }

    private void showLoading(int from, int limit) {
        ProgressBar progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        Button load = findViewById(R.id.load);
        Button refresh = findViewById(R.id.refresh);
        load.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
    }

    private void hideLoading() {
        ProgressBar progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        Button load = findViewById(R.id.load);
        Button refresh = findViewById(R.id.refresh);
        load.setVisibility(View.VISIBLE);
        refresh.setVisibility(View.VISIBLE);
    }

    private void init() {
        coinArrayList = new ArrayList<>();
        rvCoins = findViewById(R.id.rv_coins);
        loadMore = findViewById(R.id.load);
        refresh = findViewById(R.id.refresh);
    }

    private void generateData(int from, int limit) {
        //TODO show more beautiful loading(mirkamali)
        showLoading(from, limit);

        Thread threadGetData = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = LOAD_FROM_API;
                try {
                    boolean temp = getDataFromCoinMarketCap(from, limit);
                    lastCoin += limit;
                    if (temp) {
                        message.arg1 = 1;
                    } else {
                        message.arg1 = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handlerThread.sendMessage(message);
            }
        });
        threadGetData.start();
    }

    private void setData() {
        rvCoins.setLayoutManager(new LinearLayoutManager(this));
        listener = new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coin item) {
                Intent intent = new Intent(MainActivity.this, CoinOHLC.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("abbr", item.getSymbol());
                startActivity(intent);
            }
        };
        rvCoins.setAdapter(new Adapter(coinArrayList, listener));
        hideLoading();
    }


    private boolean getDataFromCoinMarketCap(int from, int to) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=" + from + "&limit=" + to;
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        url = builder.build().toString();
        final Request request = new Request.Builder().url(url).addHeader("X-CMC_PRO_API_KEY", "1dfc3423-a3cb-4aea-802e-5a7ee6b24d2d").build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseText = response.body().string();
            String first = "},\"data\":[";
            int location = responseText.indexOf(first);
            location += 9;
            responseText = responseText.substring(location, responseText.length() - 1);
            Coin.convertJsonToCoins(responseText);
            //TODO data
            Log.v("mylog",responseText);
            return true;
        } else {
            return false;
        }
    }


}