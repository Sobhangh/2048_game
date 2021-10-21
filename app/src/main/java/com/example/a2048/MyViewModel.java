package com.example.a2048;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyViewModel extends ViewModel {

    private int[] bord = new int[16];
    private boolean bordUpdated =false;
    private int highScore;
    private final String bordData = "bordData";
    private final String scoreData = "scoreData";
    private final String highscoreData = "highscoreData";
    private int score = 0;

    public int[] getBord(Context context){
        if(bordUpdated){
            return bord;
        }
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = sharedPref.getString(bordData, null);
        if(jsonText == null){
            return null;
        }
        int[] data = gson.fromJson(jsonText, int[].class);
        this.bord = data.clone();
        return data;
    }

    public void setBord(int[] bord, Context context){
        bordUpdated = true;
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        this.bord = bord.clone();
        Gson gson = new Gson();
        List<Integer> list2 = new ArrayList<Integer>();
        for(Integer i:this.bord) {
            list2.add(i);
        }
        String jsonText = gson.toJson(list2);
        sharedPref.edit().putString(bordData, jsonText).apply();
    }


    public int getHighScore(Context context){
        if(highScore != 0){
            return highScore;
        }
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        this.highScore= sharedPref.getInt(highscoreData,0);
        return highScore;
    }

    public void setHighScore( Context context){
        if(score> highScore){
            this.highScore = score;
            // save this as highscore
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            sharedPref.edit().putInt(highscoreData, this.highScore).apply();
        }
    }

    public void setHighScore(){
        if(score> highScore) {
            this.highScore = score;
        }
    }

    public int getScore(Context context){
        if(score != 0){
            return score;
        }
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
        this.score= sharedPref.getInt(scoreData,0);
        return score;
    }

    public void setScore(Context context){
            // save this as highscore
            SharedPreferences sharedPref = context.getSharedPreferences("myPref", MODE_PRIVATE);
            sharedPref.edit().putInt(scoreData, this.score).apply();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int val){
        this.score += val;
    }

}
