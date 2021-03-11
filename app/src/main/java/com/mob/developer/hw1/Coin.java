package com.mob.developer.hw1;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Coin {
    private String name;
    private String imgAddress;
    private String price;
    private String abbrName;
    private String change7d;
    private String change1h;
    private String change24h;
    private static ArrayList<Coin> allCoins = new ArrayList<>();
    private static final String DATA_ADDRESS = "";

    public Coin(String name, String imgAddress, String price, String abbrName, String change7d, String change1h, String change24h) {
        this.name = name;
        this.imgAddress = imgAddress;
        this.price = price;
        this.abbrName = abbrName;
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

    public String getAbbrName() {
        return abbrName;
    }

    public void setAbbrName(String abbrName) {
        this.abbrName = abbrName;
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
            FileOutputStream fOut = context.openFileOutput(DATA_ADDRESS, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            osw.write("");
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fileToLog(Context context) {
        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                String input = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
}
