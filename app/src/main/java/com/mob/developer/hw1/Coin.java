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
    private String year;
    private String price;
    private static ArrayList<Coin> allCoins = new ArrayList<>();
    private static final String DATA_ADDRESS = "";

    public Coin(String name, String price, String year) {
        this.name = name;
        this.price = price;
        this.year = year;
        allCoins.add(this);
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getYear() {
        return year;
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
