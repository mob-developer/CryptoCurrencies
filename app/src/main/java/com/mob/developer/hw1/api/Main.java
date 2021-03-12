package com.mob.developer.hw1.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
    public enum Range {
        weekly,
        oneMonth,
    }

    public void getCandles(String symbol,Range range) {

        OkHttpClient okHttpClient = new OkHttpClient();

        String miniUrl = null;
        final String description;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&limit=7");
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&limit=30");
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
                .addHeader("X-CoinAPI-Key", "1dfc3423-a3cb-4aea-802e-5a7ee6b24d2d")
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

                    System.out.println(response);

                }
            }
        });

    }
}
