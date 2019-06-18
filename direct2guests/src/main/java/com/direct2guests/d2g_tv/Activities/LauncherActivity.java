package com.direct2guests.d2g_tv.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

import static android.view.View.VISIBLE;


public class LauncherActivity extends LangSelectActivity {
    private NetworkConnection nc = new NetworkConnection();
    private Variable vdata;
    public final static String WATCHTV_FROM = "com.direct2guests.d2g_tv.WATCHTV_FROM";


    /* ========== Housekeeping Modal ========== */
    private Button exitButton, extendButton, checkoutButton;
    private TextView HKDate, HKStatus, HKCancelHouseKeeping, HKTitle, HKKeeping;
    private String urlCancelToday, urlCancelWhole, urlRequestHK, urlGetStatus, chatFrom;
    private int HKFocus = 0;


    // Font path
    String fontPathRegRale = "raleway/Raleway-Regular.ttf";
    String fontPathBoldRale = "raleway/Raleway_Bold.ttf";
    String fontPathRegCav = "fonts/CaviarDreams.ttf";
    String fontPathBoldCav = "fonts/CaviarDreams_Bold.ttf";
    String message;

    private String currentDateString, lastClick;
    /* ========== End ========== */




    private int seconds, minute, hour;
    private int timer1, timer2;

    private FrameLayout watchtv_frame;
    private FrameLayout hotelservices_frame;
    private FrameLayout checkout_frame;

    private FrameLayout tm_chanFrame;
    private FrameLayout tm_vodFrame;


    private RelativeLayout weatherLayout, selectBack;

    private TextView currentTime, consumeTime, testTime;
    private TextView guestname_txtview;
    private TextView date_txtview;
    private TextView watctv_txt_launcher, weather_description, weather_temp;
    private TextView hotelservices_txt_launcher;

    private ImageView hotellogo_imgview, weather_icon;
    private TextClock textClock;

    private Button channelButton, vodclickButton;
    private String qlcheckURL11, qlcheckURL12, qlcheckURL13, qlcheckURL14, qlcheckURL15, qlcheckURL16;



    private Tracker mTracker;


    private SharedPreferences preferenceSettings, preferenceSettingsTime;
    private SharedPreferences.Editor preferenceEditor, preferenceEditorTime;
    private static final int PREFERENCE_MODE_PRIVATE = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.d("Error", "UncaughtException!!!");
                System.exit(2);
            }
        });
        Bundle configBundle = new Bundle();
        try {
            setContentView(R.layout.activity_launcher2nd);
        }catch (RuntimeException e){
            onCreate(configBundle);
        }

        AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        currentDateString = dateFormat.format(date);
        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);

        //init widgets
        date_txtview = findViewById(R.id.dateTxtmain);
        watchtv_frame = findViewById(R.id.watchtvframe);
        hotelservices_frame = findViewById(R.id.hotelservicesframe);
        checkout_frame = findViewById(R.id.checkoutFrame);

        exitButton = findViewById(R.id.exitBttn);
//        extendButton = findViewById(R.id.extendBttn);
//        checkoutButton = findViewById(R.id.checkoutBttn);



        guestname_txtview = findViewById(R.id.tm_welcomeTxt);
        hotellogo_imgview = findViewById(R.id.hotelLogo);
        watctv_txt_launcher = findViewById(R.id.watchtv_launcher_text);
        hotelservices_txt_launcher = findViewById(R.id.hotelservices_launcher_text);
        watctv_txt_launcher = findViewById(R.id.watchtv_launcher_text);
        hotelservices_txt_launcher = findViewById(R.id.hotelservices_launcher_text);
        weather_description = findViewById(R.id.LweatherDesc);
        weather_icon = findViewById(R.id.LweatherIcon1);
        weather_temp = findViewById(R.id.LweatherTemp1);
        weatherLayout = findViewById(R.id.weatherL);
        // Font path
        String fontPathReg = "raleway/Raleway-Regular.ttf";
        String fontPathRegBold = "raleway/Raleway_Bold.ttf";
        String fontPathBold = "fonts/CaviarDreams_Bold.ttf";

        // text view label
        date_txtview = findViewById(R.id.dateTxt);
        textClock = findViewById(R.id.textClock);
        currentTime = findViewById(R.id.textView7);
        consumeTime = findViewById(R.id.shadowTime);
        testTime = findViewById(R.id.textView8);




//        currentTime.setText((CharSequence) textClock);




        // Loading Font Face
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontRegBold = Typeface.createFromAsset(getAssets(), fontPathRegBold);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        //Applying font
        watctv_txt_launcher.setTypeface(fontReg);
        hotelservices_txt_launcher.setTypeface(fontReg);
        date_txtview.setTypeface(fontBold);
        textClock.setTypeface(fontBold);
        guestname_txtview.setTypeface(fontRegBold);

        date_txtview.setText(currentDateString);
        guestname_txtview.setText("Welcome "+vdata.getGuestFirstName()+" !");



//        Picasso.with(getApplicationContext()).load(vdata.getServerURL()+vdata.getHotelLogo()).into(hotellogo_imgview);

        onFocusFrames();
//        videoViewPlay();







        preferenceSettings = getSharedPreferences("occupied", MODE_PRIVATE);
        preferenceSettingsTime = getSharedPreferences("checkintime", MODE_PRIVATE);




    }

    @Override
    public void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
//        getWeatherReport();

        mTracker.setScreenName(vdata.getHotelName()+" ~ Room No. "+vdata.getRoomNumber()+" ~ "+"Main View");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


//        videoViewPlay();
    }

    @Override
    public void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar


//        videoViewPlay();
    }

    @Override
    public void onBackPressed(){

//        super.onBackPressed();
        main_activity();
    }




    public void watchtv_activity(View view){


        setContentView(R.layout.view_choices);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        channelButton = findViewById(R.id.chanBttn);
        vodclickButton = findViewById(R.id.vodBttn);
        channelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channel_activity();
            }

        });

        channelButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                    HKFocus = 0;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                }

            }
        });


        vodclickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.android.tvleanback");
                startActivity(launchIntent);

            }
        });
        vodclickButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                    HKFocus = 0;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                }

            }
        });

    }



    public void channel_activity(){
        Intent i = new Intent(LauncherActivity.this, ChannelListActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "launcher");
        startActivity(i);
    }


    public void main_activity(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "launcher");
        startActivity(i);
    }

    public void hotelservices_activity(View view){
        Intent i = new Intent(this, HotelServicesActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "launcher");
        startActivity(i);
    }

    public void launcher_activity(){
        Intent i = new Intent(this, LauncherActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "launcher");
        startActivity(i);
    }

    public void onFocusFrames(){
        watchtv_frame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),LauncherActivity.this);
            }
        });
        hotelservices_frame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),LauncherActivity.this);
            }
        });
        checkout_frame.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),LauncherActivity.this);
            }
        });

    }

    private void getWeatherReport(){
        String url = "http://api.openweathermap.org/data/2.5/weather?id=1701668&appid=f4d527523a8ec6989f6e543ffc16ea25&units=metric";
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if(response.getString("cod").toString().equals("200")) {
                        JSONArray weather = response.getJSONArray("weather");
                        JSONObject weat = weather.getJSONObject(0);
                        String main = weat.getString("main");
                        String description = weat.getString("description");
                        String icon = weat.getString("icon");
                        JSONObject ma = response.getJSONObject("main");
                        String temp = ma.get("temp").toString();

                        String desc = main + "( " + description + " )";
                        String icon_url = "http://openweathermap.org/img/w/" + icon + ".png";
                        String tem = temp + "ᵒC";

                        weather_description.setText(desc);
                        weather_temp.setText(tem);
                        Picasso.with(getApplicationContext()).load(icon_url).resize(80, 80).into(weather_icon);
                        weatherLayout.setVisibility(VISIBLE);
                    }
                    else{
                        weatherLayout.setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    Log.d("Weather Error",e.getLocalizedMessage());
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }



    public void openCheckoutActivity(View view){

            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.checkin_extend_activity);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            extendButton = dialog.findViewById(R.id.extendBttn);
            checkoutButton = dialog.findViewById(R.id.checkoutBttn);

            extendButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                        HKFocus = 0;
                    } else {
                        view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                    }

                }
            });


            checkoutButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                        HKFocus = 0;
                    } else {
                        view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                    }

                }
            });

            dialog.show();


    }



    public void onExtendActivity(View view) {
        qlcheckURL11 = vdata.getApiUrl() + "extendroomactivity.php?room_id=" + vdata.getQLroomID();
        new AlertDialog.Builder(LauncherActivity.this)
                .setTitle("EXTEND STAY")
                .setMessage("Are you sure you want to EXTEND?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        HttpAsyncTask hat = new HttpAsyncTask();
                        hat.execute(qlcheckURL11);

                        preferenceSettingsTime.edit().clear().commit();
                        preferenceEditorTime = preferenceSettingsTime.edit();
                        preferenceEditorTime.putLong("checkintime", System.currentTimeMillis());
                        preferenceEditorTime.commit();

                        launcher_activity();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }



    public void onCheckoutActivity(View view) {
        qlcheckURL12 = vdata.getApiUrl() + "outroomactivity.php?room_id=" + vdata.getQLroomID();
        new AlertDialog.Builder(LauncherActivity.this)
                .setTitle("CHECK-OUT")
                .setMessage("Are you sure you want to CHECKOUT?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        HttpAsyncTask hat = new HttpAsyncTask();
                        hat.execute(qlcheckURL12);
                        main_activity();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return httpRequestResponse(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    //For HttpAsync Functions: sending requests and receiving responses
    public static String httpRequestResponse(String url){
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert InputStream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "InputStream did not work";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }




    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    long millis = System.currentTimeMillis();

                    int seconds = (int) (millis / 1000) % 60 ;
                    int minutes = (int) ((millis / (1000*60)) % 60);
                    int hours   = (int) ((millis / (1000*60*60)) % 12);

                    long oldtime = preferenceSettingsTime.getLong("checkintime", 0);

                    int oldhours = (int) ((oldtime / (1000*60*60)) % 12);
                    int oldminutes = (int) ((oldtime / (1000*60)) % 60);
                    int oldseconds = (int) (oldtime / 1000) % 60 ;



                    currentTime.setText(String.valueOf("Checked In Time:"));
                    consumeTime.setText(String.valueOf(oldhours + ":" + oldminutes + ":" + oldseconds));

                    long longdate = 10811000; // 3 hrs value
                    testTime.setText("Current Time:");
                    TextView txtCurrentTime = (TextView)findViewById(R.id.tm_currentTime);

                    txtCurrentTime.setText(String.valueOf(hours + ":" + minutes + ":" + seconds));
//                    txtCurrentTime.setText(  );

                    if (System.currentTimeMillis() - oldtime > longdate) {


                        final Dialog dialog = new Dialog(LauncherActivity.this);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.checkin_extend_activity);
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);

                        extendButton = dialog.findViewById(R.id.extendBttn);
                        checkoutButton = dialog.findViewById(R.id.checkoutBttn);

                        extendButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                                    HKFocus = 0;
                                } else {
                                    view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                                }

                            }
                        });


                        checkoutButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean hasFocus) {
                                if (hasFocus) {
                                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                                    HKFocus = 0;
                                } else {
                                    view.setBackgroundTintList(getColorStateList(R.color.quantitybuttoncartblur));
                                }

                            }
                        });

                        dialog.show();


                    }

                } catch (Exception e) {
                }
            }
        });
    }

    class CountDownRunner1 implements Runnable{
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    public void videoViewPlay(){

        Resources res = getResources();
        String[] ADS = res.getStringArray(R.array.myADS);
        String randomStr = ADS[new Random().nextInt(ADS.length)];

        VideoView videoView = findViewById(R.id.videoViewLauncher);

        videoView.setVideoPath(String.valueOf(randomStr));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });


    }





}
