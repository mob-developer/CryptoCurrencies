package com.mob.developer.hw1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        coinAbbrName = intent.getStringExtra("abbr");
        Toast.makeText(getApplicationContext(),intent.getStringExtra("name"),Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_coin_ohlc);
        getCandles(coinAbbrName,Range.weekly);
    }

    public enum Range {
        weekly,
        oneMonth,
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getDate(int days) {
        LocalDate localDate = LocalDate.now().minusDays(days);
        Log.v("mylog", String.valueOf(localDate));
        return String.valueOf(localDate) + "T" + "00:00:00";
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCandles(String symbol, Range range) {

        OkHttpClient okHttpClient = new OkHttpClient();

        String miniUrl;
        final String description;
        switch (range) {

            case weekly:

                miniUrl = "period_id=1DAY".concat("&time_start=" + getDate(7 ));
                //+"&time_end=".concat(String.valueOf(getCurrentDate())).concat("&limit=7")
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_start=" + getDate(30 ));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/".concat(symbol).concat("/USD/history?".concat(miniUrl)))
                .newBuilder();

        String url = urlBuilder.build().toString();
        Log.v("mylog","url:"+url);


        final Request request = new Request.Builder().url(url)
                .addHeader("X-CoinAPI-Key", "917174EC-0BF3-4365-8C9E-C79741576C25")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("mylog", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    //extractCandlesFromResponse(response.body().string(), description);
                    Log.v("mylog", response.toString());
                    Log.v("mylog", response.body().string());
                }
            }
        });

    }
}