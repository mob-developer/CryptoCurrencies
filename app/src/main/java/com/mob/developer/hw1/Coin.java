package com.mob.developer.hw1;

import android.content.Context;
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

import static android.content.Context.MODE_PRIVATE;

public class Coin {
    private String name;
    private String imgAddress;
    private String price;
    private String symbol;
    private String change7d;
    private String change1h;
    private String change24h;
    public static ArrayList<Coin> allCoins = new ArrayList<>();
    private static final String DATA_ADDRESS = "";

    public Coin(String name, String imgAddress, String price, String symbol, String change7d, String change1h, String change24h) {
        this.name = name;
        this.imgAddress = imgAddress;
        this.price = price;
        this.symbol = symbol;
        this.change7d = change7d;
        this.change1h = change1h;
        this.change24h = change24h;
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

    public String getChange7d() {
        return change7d;
    }

    public void setChange7d(String change7d) {
        this.change7d = change7d;
    }

    public String getChange1h() {
        return change1h;
    }

    public void setChange1h(String change1h) {
        this.change1h = change1h;
    }

    public String getChange24h() {
        return change24h;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public static void logToFile(Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(DATA_ADDRESS, MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(convertCoinsToJson());
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fileToLog(Context context) {
        try {
            InputStream inputStream = context.openFileInput(DATA_ADDRESS);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                convertJsonToCoins(stringBuilder.toString());
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }

    private static String convertCoinsToJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray arrayOfCoins = new JSONArray();
        for (Coin coin : allCoins) {
            JSONObject objectiveCoin = new JSONObject();
            try {
                objectiveCoin.put("name",coin.name);
                objectiveCoin.put("imgAddress",coin.imgAddress);
                objectiveCoin.put("price",coin.price);
                objectiveCoin.put("abbrName",coin.symbol);
                objectiveCoin.put("change7d",coin.change7d);
                objectiveCoin.put("change1h",coin.change1h);
                objectiveCoin.put("change24h",coin.change24h);
                arrayOfCoins.put(objectiveCoin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put("array", arrayOfCoins);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static void convertJsonToCoins(String input) {
        try {
             JSONArray jsonObject = new JSONArray(input);
            for (int i = 0; i < jsonObject.length() ; i++) {
                JSONObject objectiveCoin = jsonObject.getJSONObject(i);
                new Coin(objectiveCoin.getString("name"),"a", "10203",
                        objectiveCoin.getString("symbol"), objectiveCoin.getString("cmc_rank"), "change1ddd",
                        "aaaaa");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
