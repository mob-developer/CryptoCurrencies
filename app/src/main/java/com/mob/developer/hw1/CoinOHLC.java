package com.mob.developer.hw1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CoinOHLC extends AppCompatActivity {
    private String coinAbbrName;
    private TextView textView;
    private Handler handlerThread;
    private static final int LOAD_FROM_API = 1;

    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        coinAbbrName = intent.getStringExtra("abbr");
        Toast.makeText(getApplicationContext(), intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_coin_ohlc);
        textView = findViewById(R.id.ohcl);
        generateData(coinAbbrName,Range.weekly);


        handlerThread = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == LOAD_FROM_API) {
                    if (msg.arg1 == 1) {
                        textView.setText((String)msg.obj);
                    } else {
                        Log.e("mylog", "error in handle msg");
                    }
                }
            }
        };
    }

    public enum Range {
        weekly,
        oneMonth,
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getDate(int days) {
        LocalDate localDate = LocalDate.now().minusDays(days);
        return String.valueOf(localDate) + "T" + "00:00:00";
    }

    private void generateData(String symbol,Range range) {
        showLoading();

        Thread threadGetData = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message message = new Message();
                message.what = LOAD_FROM_API;
                try {
                    String temp = getCandles(symbol, range);
                    if (temp==null) {
                        message.arg1 = 0;
                    } else {
                        message.arg1 = 1;
                        message.obj = temp;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handlerThread.sendMessage(message);
            }
        });
        threadGetData.start();
    }

    private void showLoading(){

    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCandles(String symbol, Range range) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        String miniUrl;
        final String description;
        switch (range) {
            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_start=" + getDate(7));
                //+"&time_end=".concat(String.valueOf(getCurrentDate())).concat("&limit=7")
                description = "Daily candles from now";
                break;
            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_start=" + getDate(30));
                description = "Daily candles from now";
                break;
            default:
                miniUrl = "";
                description = "";
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl))).newBuilder();
        String url = urlBuilder.build().toString();
        final Request request = new Request.Builder().url(url).addHeader("X-CoinAPI-Key", "917174EC-0BF3-4365-8C9E-C79741576C25").build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return null;
        }

    }
}