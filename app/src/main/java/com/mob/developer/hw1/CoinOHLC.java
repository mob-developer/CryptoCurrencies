package com.mob.developer.hw1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CoinOHLC extends AppCompatActivity {
    private String symbol;
    private TextView information;
    private Handler handlerThread;
    private static final int LOAD_FROM_API = 1;
    private Button day7;
    private Button day40;
    private ProgressBar progressBar;
    private CandleStickChart chart;
    private ArrayList<CandleEntry> values;

    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        symbol = intent.getStringExtra("abbr");
        Toast.makeText(getApplicationContext(), intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_coin_ohlc);
        values = new ArrayList<>();
        day7 = findViewById(R.id.day7);
        day40 = findViewById(R.id.day40);
        information = findViewById(R.id.ohcl);
        progressBar = findViewById(R.id.progressBar2);
        chart = findViewById(R.id.candle);
        chart.setBackgroundColor(Color.rgb(50, 50, 50));
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(7, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.resetTracking();

        generateData(symbol, Range.weekly);

        //TODO load from cache


        handlerThread = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == LOAD_FROM_API) {
                    if (msg.arg1 == 1) {
                        showData((String) msg.obj);
                        hideLoading();
                    } else {
                        Log.e("mylog", "error in handle msg");
                    }
                }
            }
        };

        day7.setOnClickListener(view -> {
            generateData(symbol, Range.weekly);
        });
        day40.setOnClickListener(view -> {
            generateData(symbol, Range.oneMonth);
        });


    }

    private void showData(String data) {
        //information.setText(data);
        try {
            JSONArray jsonObject = new JSONArray(data);
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                values.add(new CandleEntry(i,Float.parseFloat( jsonObject1.getString("price_open")),Float.parseFloat( jsonObject1.getString("price_open")),
                        Float.parseFloat(jsonObject1.getString("price_open")), Float.parseFloat(jsonObject1.getString("price_open")),getResources().getDrawable(R.drawable.bit)));
            }
            CandleDataSet set1 = new CandleDataSet(values, "Data Set");

            set1.setDrawIcons(false);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
            set1.setShadowWidth(0.7f);
            set1.setDecreasingColor(Color.RED);
            set1.setDecreasingPaintStyle(Paint.Style.FILL);
            set1.setIncreasingColor(Color.rgb(122, 242, 84));
            set1.setIncreasingPaintStyle(Paint.Style.STROKE);
            set1.setNeutralColor(Color.BLUE);
            //set1.setHighlightLineWidth(1f);

            CandleData data1 = new CandleData(set1);

            chart.setData(data1);
            chart.invalidate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void generateData(String symbol, Range range) {
        showLoading();

        Thread threadGetData = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message message = new Message();
                message.what = LOAD_FROM_API;
                try {
                    String temp = getCandles(symbol, range);
                    if (temp == null) {
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

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    ;

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    ;

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
            String s = response.body().string();
            Log.v("mylog", s);
            return s;
        } else {
            return null;
        }

    }
}