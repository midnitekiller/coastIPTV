package com.direct2guests.d2g_tv.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.R;

import java.util.Locale;
import java.util.Random;


public class MainLangActivity extends LangSelectActivity implements View.OnClickListener {

    public final static String LANG_SELECT_FROM = "com.direct2guests.d2g_tv.LangSelectActivity.LANG_SELECT_FROM";

//    public static final String LANG = "en";
//    SharedPreferences myLang;


    Variable vdata = new Variable();


    private int HKFocus = 0;
    private FrameLayout frenchFrame;
    private FrameLayout englishFrame;
    private FrameLayout koreanFrame;
    private FrameLayout chinaFrame;
    private FrameLayout japFrame;
    private ImageView japImage;
    private ImageView frenchImage;
    private ImageView englishImage;
    private ImageView koreanImage;
    private ImageView chinaImage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_select);

        findViewById(R.id.LangEnglish).setOnClickListener(this);
        findViewById(R.id.LangKorean).setOnClickListener(this);
        findViewById(R.id.LangFrench).setOnClickListener(this);
        findViewById(R.id.LangChina).setOnClickListener(this);
        findViewById(R.id.LangJap).setOnClickListener(this);
        findViewById(R.id.LangChina).setOnClickListener(this);

        frenchFrame = findViewById(R.id.LangFrench);
        englishFrame = findViewById(R.id.LangEnglish);
        koreanFrame = findViewById(R.id.LangKorean);
        chinaFrame = findViewById(R.id.LangChina);
        japFrame = findViewById(R.id.LangJap);

        japImage = findViewById(R.id.JapIcon);
        frenchImage = findViewById(R.id.FrenchIcon);
        englishImage = findViewById(R.id.EnglishIcon);
        koreanImage = findViewById(R.id.KoreaIcon);
        chinaImage = findViewById(R.id.ChinaIcon);

//        myLang = getSharedPreferences(LANG, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = myLang.edit();


        videoViewPlay();
        onFocusFrames();






    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.LangEnglish) {
            setLanguage("en");
            Intent i = new Intent(MainLangActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.LangFrench) {
            setLanguage("fr");
            Intent i = new Intent(MainLangActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.LangKorean) {
            setLanguage("ko");
            Intent i = new Intent(MainLangActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.LangChina) {
            setLanguage("zh");
            Intent i = new Intent(MainLangActivity.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.LangJap) {
            setLanguage("ja");
            Intent i = new Intent(MainLangActivity.this, MainActivity.class);
            startActivity(i);
        }
    }


    public void onFocusFrames() {
        frenchFrame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view, hasFocus, getApplicationContext(), MainLangActivity.this);
                if(frenchFrame.hasFocus()){
                    frenchImage.setImageResource(0);
                    frenchImage.setBackground(getDrawable(R.drawable.frfocus));
                    HKFocus = 0;
                } else{
                    frenchImage.setImageResource(0);
                    frenchImage.setBackground(getDrawable(R.drawable.fricon));
                }

            }
        });

        englishFrame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view, hasFocus, getApplicationContext(), MainLangActivity.this);
                if(englishFrame.hasFocus()){
                    englishImage.setImageResource(0);
                    englishImage.setBackground(getDrawable(R.drawable.enfocus));
                    HKFocus = 0;
                } else{
                    englishImage.setImageResource(0);
                    englishImage.setBackground(getDrawable(R.drawable.enicon));
                }
            }
        });
        koreanFrame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view, hasFocus, getApplicationContext(), MainLangActivity.this);
                if(koreanFrame.hasFocus()){
                    koreanImage.setImageResource(0);
                    koreanImage.setBackground(getDrawable(R.drawable.krfocus));
                    HKFocus = 0;
                } else{
                    koreanImage.setImageResource(0);
                    koreanImage.setBackground(getDrawable(R.drawable.kricon));
                }

            }
        });
        chinaFrame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view, hasFocus, getApplicationContext(), MainLangActivity.this);
                if(chinaFrame.hasFocus()){
                    chinaImage.setImageResource(0);
                    chinaImage.setBackground(getDrawable(R.drawable.cnfocus));
                    HKFocus = 0;
                } else{
                    chinaImage.setImageResource(0);
                    chinaImage.setBackground(getDrawable(R.drawable.cnicon));
                }

            }
        });
        japFrame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view, hasFocus, getApplicationContext(), MainLangActivity.this);
                if(japFrame.hasFocus()){
                    //view.setBackground(getDrawable(R.drawable.jafocus));
                    japImage.setImageResource(0);
                    japImage.setBackground(getDrawable(R.drawable.jafocus));
                    HKFocus = 0;
                } else{
                    japImage.setImageResource(0);
                    japImage.setBackground(getDrawable(R.drawable.jaicon));
                }

            }
        });
    }




    public void videoViewPlay(){

        Resources res = getResources();
        String[] ADS = res.getStringArray(R.array.myADS);
        String randomStr = ADS[new Random().nextInt(ADS.length)];

        VideoView videoView = findViewById(R.id.videoViewMainLangActivity);

        videoView.setVideoPath(String.valueOf(randomStr));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });


    }

    // End of Dynamic Background

}
