package com.mob.developer.hw1;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class Coin {
    private long id;
    private String name;
    private String symbol;
    private String imgAddress;
    private double price;
    private double percent_change_7d;
    private double percent_change_1h;
    private double percent_change_24h;
    public static ArrayList<Coin> allCoins = new ArrayList<>();
    private static final String DATA_ADDRESS = "coin.txt";

    public Coin(long id, String name, String imgAddress, double price, String symbol, double percent_change_7d, double percent_change_1h, double percent_change_24h) {
        this.id = id;
        this.name = name;
        this.imgAddress = imgAddress;
        this.price = price;
        this.symbol = symbol;
        this.percent_change_7d = percent_change_7d;
        this.percent_change_1h = percent_change_1h;
        this.percent_change_24h = percent_change_24h;
        allCoins.add(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public void setPrice(String price) {
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public static void coinToFile(Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(DATA_ADDRESS, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(convertCoinsToJsonArray());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fileToCoin(Context context) {
        try {
            InputStream inputStream = context.openFileInput(DATA_ADDRESS);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                convertJsonToCoins(new JSONArray(stringBuilder.toString()), 0);
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            Log.e("json", "file can not convert to json: " + e.toString());
        }

    }

    private static String convertCoinsToJsonArray() {
        JSONArray arrayOfCoins = new JSONArray();
        for (Coin coin : allCoins) {
            JSONObject objectiveCoin = new JSONObject();
            try {
                objectiveCoin.put("id", coin.id);
                objectiveCoin.put("name", coin.name);
                objectiveCoin.put("symbol", coin.symbol);
                objectiveCoin.put("imgAddress", coin.imgAddress);
                JSONObject USD = new JSONObject();
                USD.put("price", coin.price);
                USD.put("percent_change_7d", coin.percent_change_7d);
                USD.put("percent_change_1h", coin.percent_change_1h);
                USD.put("percent_change_24h", coin.percent_change_24h);
                JSONObject quote = new JSONObject();
                quote.put("USD", USD);
                objectiveCoin.put("quote", quote);
                arrayOfCoins.put(objectiveCoin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayOfCoins.toString();
    }

    public static void convertJsonToCoins(JSONArray jsonArray, int mode) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectiveCoin = jsonArray.getJSONObject(i);
                JSONObject USD = objectiveCoin.getJSONObject("quote").getJSONObject("USD");
                String imgAddress = "";
                if (mode == 1) {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?id=" + objectiveCoin.getString("id");
                    HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
                    url = builder.build().toString();
                    final Request request = new Request.Builder().url(url).addHeader("X-CMC_PRO_API_KEY", "1dfc3423-a3cb-4aea-802e-5a7ee6b24d2d").build();
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseText = response.body().string();
                        Log.v("mylog", responseText);
                        imgAddress = responseText.substring(responseText.indexOf("\",\"logo\":\"")+10);
                        imgAddress = imgAddress.substring(0,imgAddress.indexOf("\",\""));
                        Log.v("mylog",imgAddress);
                    }
                }else if(mode == 2 ){
                    imgAddress = "https://s2.coinmarketcap.com/static/img/coins/64x64/"+objectiveCoin.getString("id")+".png";
                }


                new Coin(objectiveCoin.getLong("id"), objectiveCoin.getString("name"), imgAddress, Math.round(USD.getDouble("price") * 1000.0) / 1000.0,
                        objectiveCoin.getString("symbol"), Math.round(USD.getDouble("percent_change_7d") * 100.0) / 100.0, Math.round(USD.getDouble("percent_change_1h") * 100.0) / 100.0,
                        Math.round(USD.getDouble("percent_change_24h") * 100.0) / 100.0);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
