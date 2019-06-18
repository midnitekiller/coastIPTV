package com.direct2guests.d2g_tv.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.R;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import java.util.Locale;
import java.util.Random;

import mateuszklimek.framevideoview.FrameVideoView;


import static android.view.View.VISIBLE;
import static com.direct2guests.d2g_tv.Activities.LauncherActivity.WATCHTV_FROM;
import static com.direct2guests.d2g_tv.Activities.MainLangActivity.LANG_SELECT_FROM;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ImgPlaces;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.apkPath;


public class MainActivity extends LangSelectActivity {
    NetworkConnection nc = new NetworkConnection();
    Variable vdata = new Variable();




    private FirebaseAnalytics mFirebaseAnalytics;
    Locale locale;

    private int HKFocus = 0;
    private String currentDateString;
    private TextView date_txtview;

    String unique_id;
    String message;
    String[] access;

    TextView hotelName;
    TextView guestName;
    TextView welcomeTxtV;
    TextView deviceID;
    TextView currentTime;
    TextView chanText;
    private java.util.Calendar calendar;
    private String cmonth, chour, cminutes, versionName, appname;
    private int cyear, cday;

    private String[] adsRTitle,adsRDescription,adsRAddress,adsRContact;
    private String[] adsATitle,adsADescription,adsAAddress,adsAContact;
    private String[] adsPTitle,adsPDescription,adsPAddress,adsPContact;

    private String[] adsRimg1,adsRimg2,adsRimg3,adsRimg4,adsRimg5;
    private String[] adsAimg1,adsAimg2,adsAimg3,adsAimg4,adsAimg5;
    private String[] adsPimg1,adsPimg2,adsPimg3,adsPimg4,adsPimg5;
    private String[] version, verName;

    DownloadManager downloadManager;
    String downloadFileUrl = ServerUrl + apkPath + "direct2guesttv.apk";
    private long myDownloadReference;
    private int noupdate = 0;

    private String thread_url;
    private Thread t;
    private JsonArrayRequest jsonRequest;
    private RequestQueue queue;

    private String validated;
    private EditText roomnumber, hotelname;
    private Button buttonValidate, mainBttn;

    private Button shortBttn, longBttn, promoBttn;

    private String api_url_used = "";
    private String shortBttnUrl, longBttnUrl, promoBttnUrl, qlcheckURL01, qlcheckURL02, qlcheckURL03, qlcheckURL04, qlcheckURL05, qlcheckURL06;

    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECORD_AUDIO,
    };



    private FrameVideoView videoView;
    private MediaPlayer mediaPlayer;



    // For Language Selection method

    // End Lang Select



    private Button localButton, liveButton, goButton, langButton, mainButton;
    private EditText ipText;
    private RelativeLayout weatherLayout, selectBack, inputBackIP;
    private TextView watctv_txt_launcher, weather_description, weather_temp;
    private ImageView weather_icon;
    private SharedPreferences preferenceSettings, preferenceSettingsTime;
    private SharedPreferences.Editor preferenceEditor, preferenceEditorTime;

    private MediaPlayer backgroundMusic;

    private static final int PREFERENCE_MODE_PRIVATE = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//        //end code hide status bar

        Bundle configBundle = new Bundle();
        try {
            setContentView(R.layout.activity_main2nd);
        }catch (RuntimeException e){
            onCreate(configBundle);
        }

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_name", "Crimson");
        params.putString("app_page", "MainActivity");
        params.putString("app_event", "loading app data");
        mFirebaseAnalytics.logEvent("app_activity", params);





//        String Lang;
//        locale = getCurrentLanguage();
//        Lang = String.valueOf(locale);
       // Toast.makeText(getApplicationContext(), String.valueOf(Lang), Toast.LENGTH_LONG).show();









        unique_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        vdata.setUniqueID(unique_id);


        String fontPathBold1 = "fonts/brandonreg.ttf";
        hotelName = findViewById(R.id.hotelNameTxtV);
        guestName = findViewById(R.id.guestNameTxtV4);
        Typeface typeface = Typeface.createFromAsset(getAssets(), fontPathBold1);
        hotelName.setTypeface(typeface);
        guestName.setTypeface(typeface);


        deviceID = findViewById(R.id.uid);
        deviceID.setText(unique_id);
        //change font area
        // Font path
//        String fontPathBold = "raleway/Raleway_Bold.ttf";
        // text view label
        welcomeTxtV = findViewById(R.id.welcomeTxtV);
        // Loading Font Face
        Typeface fontbold = Typeface.createFromAsset(getAssets(), fontPathBold1);
        //Applying font
        welcomeTxtV.setTypeface(fontbold);

        calendar = java.util.Calendar.getInstance();
        cyear = calendar.get(java.util.Calendar.YEAR);
        cmonth = Integer.toString(calendar.get(java.util.Calendar.MONTH) + 1);
        cday = calendar.get(java.util.Calendar.DATE);
        appname = "direct2guesttv.apk";
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);


        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        currentDateString = dateFormat.format(date);
        date_txtview = findViewById(R.id.dateTxtmain);
        date_txtview.setText(currentDateString);


        roomnumber = new EditText(this);
        hotelname = new EditText(this);


        shortBttn = findViewById(R.id.shortBttn);
        longBttn = findViewById(R.id.longBttn);
//        promoBttn = findViewById(R.id.promoBttn);

        preferenceSettings = getSharedPreferences("occupied", MODE_PRIVATE);
        preferenceSettingsTime = getSharedPreferences("checkintime", MODE_PRIVATE);



        currentTime = findViewById(R.id.dateTxtmain);



        langButton = findViewById(R.id.langSelectButton);
        mainButton = findViewById(R.id.button);


//        videoViewPlay();
//        getWeatherReport();


    }


    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar


        mainButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                    HKFocus = 0;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.orange_transparent));
                }

            }
        });

        langButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.hkfocustint));
                    HKFocus = 0;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.orange_transparent));
                }

            }
        });




        unique_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        Log.d("DIR",getApplicationContext().getFilesDir()+"/d2g_support.apk");


//        videoViewPlay();

        if(preferenceSettings.contains("firstrun")){

        }else{
            preferenceEditor = preferenceSettings.edit();
            preferenceEditor.putBoolean("firstrun",true);
            preferenceEditor.commit();
        }

        if(isNetworkAvailable()) {

            if (preferenceSettings.getBoolean("firstrun", true)) {

                // Analytics
                Bundle params = new Bundle();
                params.putString("app_page", "MainActivity");
                params.putString("app_event", "Setting up IP connection");
                mFirebaseAnalytics.logEvent("app_activity", params);




                preferenceSettings.edit().putBoolean("firstrun", false).commit();
                final Dialog bdialog = new Dialog(MainActivity.this);
                bdialog.setCancelable(false);
                bdialog.setCanceledOnTouchOutside(false);
                bdialog.setContentView(R.layout.local_modal);

                selectBack = bdialog.findViewById(R.id.backendSwitch);
                selectBack.setVisibility(View.VISIBLE);

                inputBackIP = bdialog.findViewById(R.id.localBackendLay);
                inputBackIP.setVisibility(View.GONE);

                localButton = bdialog.findViewById(R.id.localBtn);
                liveButton = bdialog.findViewById(R.id.liveBtn);
                goButton = bdialog.findViewById(R.id.goButton);
                ipText = bdialog.findViewById(R.id.ipLocal);



                localButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectBack.setVisibility(View.GONE);
                        inputBackIP.setVisibility(View.VISIBLE);

                        goButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                preferenceEditor = preferenceSettings.edit();
                                preferenceEditor.putString("backendtype", "local");
                                preferenceEditor.putString("localip", ipText.getText().toString());
                                preferenceEditor.commit();
                                vdata.setApiUrl("http://"+preferenceSettings.getString("localip","178.128.90.166")+"/api/");
                                vdata.setServerURL("http://"+preferenceSettings.getString("localip","178.128.90.166")+"/");
                                bdialog.dismiss();
                                checkBackendType();
//                                roomdata();
                            }
                        });
                    }
                });
                liveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        preferenceEditor = preferenceSettings.edit();
                        preferenceEditor.putString("backendtype", "live");
                        preferenceEditor.commit();
//                        vdata.setApiUrl(ApiUrl);
//                        vdata.setServerURL(ServerUrl);
                        vdata.setApiUrl("http://178.128.90.166/api/");
                        vdata.setServerURL("http://178.128.90.166/");


                        checkBackendType();
//                        roomdata();
                        bdialog.dismiss();
                    }
                });
                bdialog.setOnKeyListener(new Dialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            //start code hide status bar
                            View decorView = getWindow().getDecorView();
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                            //end code hide status bar
                        }
                        return false;
                    }
                });
                bdialog.show();
            }else {
                if(preferenceSettings.getString("backendtype", "").equals("live")){
                    vdata.setApiUrl(ApiUrl);
                    vdata.setServerURL(ServerUrl);
                    checkBackendType();
//                    roomdata();
                }else{
                    vdata.setApiUrl("http://178.128.90.166/api/");
                    vdata.setServerURL("http://178.128.90.166/");
                    checkBackendType();
//                    roomdata();
                }
            }

        }else{



            message = "Device is not connected to internet. Please connect to internet to continue.";
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("No Internet Connection");
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Go To Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
            alertDialog.show();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            });
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            });
        }

    }


    // disable home button
    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            ToastHomeButton();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // end of disable home button


    @Override
    public void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        mainButton.requestFocus();
//        videoViewPlay();


    }

    @Override
    public void onBackPressed(){


    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();


    }


    public void checkBackendType(){
        Log.d("API Url: ", vdata.getApiUrl().toString());
        Log.d("Server Url: ", vdata.getServerURL().toString());
        String url = vdata.getApiUrl() + "index.php?device_id=" + unique_id;

        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    Boolean res = response.getBoolean("success");
                    if(res){
                        JSONObject hotel = response.getJSONObject("hotel");
                        String status = hotel.getString("hotel_status");
                        loadDatas(vdata.getApiUrl());
                    } else {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.validate_device);

                        //start code hide status bar
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        //end code hide status bar

                        hotelname = dialog.findViewById(R.id.hotelnameValidate);
                        roomnumber = dialog.findViewById(R.id.roomnumberValidate);
                        buttonValidate = dialog.findViewById(R.id.validateButton);

                        buttonValidate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //start code hide status bar
                                View decorView = getWindow().getDecorView();
                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                decorView.setSystemUiVisibility(uiOptions);
                                //end code hide status bar
                                if(!hotelname.getText().toString().equals("") && !roomnumber.getText().toString().equals("")){
                                    validateDevice(hotelname.getText().toString(), roomnumber.getText().toString());
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    //start code hide status bar
                                    View decorView = getWindow().getDecorView();
                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                    decorView.setSystemUiVisibility(uiOptions);
                                    //end code hide status bar
                                }
                                return false;
                            }
                        });


                        dialog.show();

                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    message = "Device Unassigned";
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("No Guest is assigned on this device.");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            View decorView = getWindow().getDecorView();
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    });
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(error instanceof NetworkError){
                    message = "Cannot connect to Internet...Please check your connection!";
                    Log.d("MainActivity", "Error Network");
                }else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Log.d("MainActivity", "Error Server");
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Log.d("MainActivity", "Error AuthFailure");
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Log.d("MainActivity", "Error Parse");
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Log.d("MainActivity", "Error No Connection");
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Log.d("MainActivity", "Error Timeout");
                }
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
            }
        });
    }


    public void loadDatas(String backend_url){
        String url = backend_url + "index.php?device_id=" + unique_id;
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    Boolean res = response.getBoolean("success");
                    if(res){
                        JSONObject hotel = response.getJSONObject("hotel");
                        String status = hotel.getString("hotel_status");


                        JSONArray access = response.getJSONArray("access");
                        JSONObject guest = response.getJSONObject("guest");

                        JSONArray restaurants = response.getJSONArray("restaurants");
                        JSONArray places_restaurants = response.getJSONObject("ads").getJSONArray("restaurant");
                        JSONArray places_activities = response.getJSONObject("ads").getJSONArray("activities");
                        JSONArray places_nightlife = response.getJSONObject("ads").getJSONArray("nightlife");
                        loadRestaurants(restaurants);
                        Log.d("Ads :", "restaurants");
                        setAds(places_restaurants,"restaurants");
                        Log.d("Ads :", "activities");
                        setAds(places_activities,"activities");
                        Log.d("Ads :", "nightlife");
                        setAds(places_nightlife,"nightlife");
                        String hotelID = hotel.getString("hotel_ID").toString();
                        String hotel_name = hotel.getString("hotel_name");
                        String guest_first_name = guest.getString("firstname");
                        String guest_last_name = guest.getString("lastname");
                        String guest_id = guest.getString("guest_ID");

                        String room_number = guest.getString("room_no");
                        String check_in = guest.getString("check_in");
                        String check_out = guest.getString("check_out");

                        String billwithouttax = guest.getString("totalroomrate");
                        String billwithtax = guest.getString("totalwithtax");


                        String hotel_logo = hotel.getString("full_image");
                        String background_image = hotel.getString("background_image");
                        String currency = hotel.getString("hotel_currency");
                        String weatherid = hotel.getString("weather_ID");
                        String airportid = hotel.getString("flight_code");
                        String[] hotel_access = new String[access.length()];
                        for(int i = 0; i < access.length(); i++){
                            hotel_access[i] = access.getString(i);
                        }
                        vdata.setHotelAccess(hotel_access);

                        vdata.setHotelID(hotelID);
                        hotelName.setText(hotel_name);
                        guestName.setText(guest_first_name + " " + guest_last_name);
                        vdata.setHotelName(hotel_name);
                        vdata.setGuestFirstName(guest_first_name);
                        vdata.setGuestLastName(guest_last_name);
                        vdata.setGuestID(guest_id);

                        vdata.setBillWithOutTax(billwithouttax);
                        vdata.setBillWithTax(billwithtax );



                        vdata.setRoomNumber(room_number);
                        vdata.setCheckIn(check_in);
                        vdata.setCheckOut(check_out);
                        vdata.setHotelLogo(hotel_logo);
                        vdata.setHotelBackground(hotel_logo);
                        vdata.setCurrency(currency);
                        vdata.setWeatherid(weatherid);
                        vdata.setAirportid(airportid);


                    } else {
                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.validate_device);

                        //start code hide status bar
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                        //end code hide status bar

                        hotelname = dialog.findViewById(R.id.hotelnameValidate);
                        roomnumber = dialog.findViewById(R.id.roomnumberValidate);
                        buttonValidate = dialog.findViewById(R.id.validateButton);

                        buttonValidate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //start code hide status bar
                                View decorView = getWindow().getDecorView();
                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                decorView.setSystemUiVisibility(uiOptions);
                                //end code hide status bar
                                if(!hotelname.getText().toString().equals("") && !roomnumber.getText().toString().equals("")){
                                    validateDevice(hotelname.getText().toString(), roomnumber.getText().toString());
                                    dialog.dismiss();
                                }
                            }
                        });
                        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
                            @Override
                            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                if (keyCode == KeyEvent.KEYCODE_BACK) {
                                    //start code hide status bar
                                    View decorView = getWindow().getDecorView();
                                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                    decorView.setSystemUiVisibility(uiOptions);
                                    //end code hide status bar
                                }
                                return false;
                            }
                        });
                        dialog.show();

                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    message = "Device Unassigned";
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("No Guest is assigned on this device.");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            View decorView = getWindow().getDecorView();
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    });
                }
            }
            @Override
            public void onError(VolleyError error) {
                if(error instanceof NetworkError){
                    message = "Cannot connect to Internet...Please check your connection!";
                }else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
            }
        });
    }


    public void continueButton(View view){
      hotelservices_activity();

    }


    private void setAds(JSONArray ads, String type){
        try {
            int count = 0;
            JSONArray liveads = new JSONArray();
            for (int i = 0; i < ads.length(); i++) {
                JSONObject ad = ads.getJSONObject(i);
                String datetime_start = ad.getString("ad_time_start");
                String datetime_end = ad.getString("ad_time_end");
                String[] date_start = datetime_start.split("-");
                String[] date_end = datetime_end.split("-");

                int month_start = Integer.parseInt(date_start[1]);
                int year_start = Integer.parseInt(date_start[0]);
                int day_start = Integer.parseInt(date_start[2]);

                int month_end = Integer.parseInt(date_end[1]);
                int year_end = Integer.parseInt(date_end[0]);
                int day_end = Integer.parseInt(date_end[2]);

                chour = Integer.toString(calendar.get(java.util.Calendar.HOUR_OF_DAY));
                cminutes = Integer.toString(calendar.get(java.util.Calendar.MINUTE));
                if (cmonth.length() == 1) {
                    cmonth = "0" + cmonth;
                }
                if (chour.length() == 1) {
                    chour = "0" + chour;
                }
                if (cminutes.length() == 1) {
                    cminutes = "0" + cminutes;
                }
                int ccmonth = Integer.parseInt(cmonth);
                int cchour = Integer.parseInt(chour);
                int ccminutes = Integer.parseInt(cminutes);

                int startd = 0;
                int endd = 0;
                if (cyear == year_start) {
                    if (ccmonth == month_start) {
                        if (cday == day_start) {
                            startd = 1;
                        } else if (cday > day_start) {
                            startd = 1;
                        } else {
                            startd = 0;
                        }
                    } else if (ccmonth > month_start) {
                        startd = 1;
                    } else {
                        startd = 0;
                    }
                } else if (cyear > year_start) {
                    startd = 1;
                } else {
                    startd = 0;
                }

                if (cyear == year_end) {
                    if (ccmonth == month_end) {
                        if (cday == day_end) {
                            endd = 1;
                        } else if (cday < day_end) {
                            endd = 1;
                        } else {
                            endd = 0;
                        }
                    } else if (ccmonth < month_end) {
                        endd = 1;
                    } else {
                        endd = 0;
                    }
                } else if (cyear <= year_end) {
                    endd = 1;
                } else {
                    endd = 0;
                }

                if (startd == 1 && endd == 1) {
                    count++;
                    liveads.put(ad);
                }
            }

            adsRTitle = new String[count];
            adsRDescription = new String[count];
            adsRAddress = new String[count];
            adsRContact = new String[count];

            adsRimg1 = new String[count];
            adsRimg2 = new String[count];
            adsRimg3 = new String[count];
            adsRimg4 = new String[count];
            adsRimg5 = new String[count];

            for (int i = 0; i < count; i++) {
                JSONObject ad = liveads.getJSONObject(i);
                adsRTitle[i] = ad.getString("ad_title");
                Log.d("Res"+i,":: "+adsRTitle[i]);
                adsRDescription[i] = ad.getString("ad_description");
                String image = ad.getString("image1");
//                String url = ServerUrl + ImgPlaces + ad.getString("img_path");
                String url = vdata.getServerURL()  + ImgPlaces + ad.getString("img_path");
                int imagecount;
                try {
                    adsRAddress[i] = ad.getString("ad_address");
                } catch (JSONException e) {
                    adsRAddress[i] = "";
                }
                try {
                    adsRContact[i] = ad.getString("ad_contact");
                }catch (JSONException e){
                    adsRContact[i] = "";
                }
                try{
                    imagecount = ad.getInt("image_count");
                }catch (JSONException e){
                    imagecount = 1;
                }
                Log.d("ImageCount", ""+imagecount);
                if (imagecount == 5) {
                    adsRimg1[i] = url + image;
                    adsRimg2[i] = url + ad.get("image2");
                    adsRimg3[i] = url + ad.get("image3");
                    adsRimg4[i] = url + ad.get("image4");
                    adsRimg5[i] = url + ad.get("image5");
                } else if (imagecount == 4) {
                    adsRimg1[i] = url + image;
                    adsRimg2[i] = url + ad.get("image2");
                    adsRimg3[i] = url + ad.get("image3");
                    adsRimg4[i] = url + ad.get("image4");
                    adsRimg5[i] = "";
                } else if (imagecount == 3) {
                    adsRimg1[i] = url + image;
                    adsRimg2[i] = url + ad.get("image2");
                    adsRimg3[i] = url + ad.get("image3");
                    adsRimg4[i] = "";
                    adsRimg5[i] = "";
                } else if (imagecount == 2) {
                    adsRimg1[i] = url + image;
                    adsRimg2[i] = url + ad.get("image2");
                    adsRimg3[i] = "";
                    adsRimg4[i] = "";
                    adsRimg5[i] = "";
                } else if (imagecount == 1) {
                    adsRimg1[i] = url + image;
                    adsRimg2[i] = "";
                    adsRimg3[i] = "";
                    adsRimg4[i] = "";
                    adsRimg5[i] = "";
                }
                Log.d("Ads","Loaded Ad"+i);
            }
            if(type.equals("restaurants")){
                vdata.setAdsRestaurantTitle(adsRTitle);
                vdata.setAdsRestaurantDescription(adsRDescription);
                vdata.setAdsRestaurantAddress(adsRAddress);
                vdata.setAdsRestaurantContact(adsRContact);
                vdata.setAdsRestaurantImage1(adsRimg1);
                vdata.setAdsRestaurantImage2(adsRimg2);
                vdata.setAdsRestaurantImage3(adsRimg3);
                vdata.setAdsRestaurantImage4(adsRimg4);
                vdata.setAdsRestaurantImage5(adsRimg5);
            }else if(type.equals("activities")){
                vdata.setAdsActivitiesTitle(adsRTitle);
                vdata.setAdsActivitiesDescription(adsRDescription);
                vdata.setAdsActivitiesAddress(adsRAddress);
                vdata.setAdsActivitiesContact(adsRContact);
                vdata.setAdsActivitiesImage1(adsRimg1);
                vdata.setAdsActivitiesImage2(adsRimg2);
                vdata.setAdsActivitiesImage3(adsRimg3);
                vdata.setAdsActivitiesImage4(adsRimg4);
                vdata.setAdsActivitiesImage5(adsRimg5);
            }else if(type.equals("nightlife")){
                vdata.setAdsPubsTitle(adsRTitle);
                vdata.setAdsPubsDescription(adsRDescription);
                vdata.setAdsPubsAddress(adsRAddress);
                vdata.setAdsPubsContact(adsRContact);
                vdata.setAdsPubsImage1(adsRimg1);
                vdata.setAdsPubsImage2(adsRimg2);
                vdata.setAdsPubsImage3(adsRimg3);
                vdata.setAdsPubsImage4(adsRimg4);
                vdata.setAdsPubsImage5(adsRimg5);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadRestaurants(JSONArray restaurants){
        try {
            String[] id = new String[restaurants.length()];
            String[] name = new String[restaurants.length()];
            String[] open = new String[restaurants.length()];
            String[] close = new String[restaurants.length()];
            String[] desc = new String[restaurants.length()];
            String[] image = new String[restaurants.length()];

            for (int i = 0; i < restaurants.length(); i++) {
                id[i] = restaurants.getJSONObject(i).getString("restaurant_ID");
                name[i] = restaurants.getJSONObject(i).getString("restaurant_name");
                open[i] = restaurants.getJSONObject(i).getString("time_open");
                close[i] = restaurants.getJSONObject(i).getString("time_close");
                desc[i] = restaurants.getJSONObject(i).getString("description");
                try {
                    if(restaurants.getJSONObject(i).getString("image").equals(null)){
                        image[i] = "";
                    }else{
                        image[i] = restaurants.getJSONObject(i).getString("img_path");
                    }
                    Log.d("Has Image", image[i]);
                } catch (JSONException e) {
                    image[i] = "";
                    Log.d("Empty Image", image[i]);
                }

            }
            vdata.setRestaurantID(id);
            vdata.setRestaurantName(name);
            vdata.setRestaurantOpen(open);
            vdata.setRestaurantClose(close);
            vdata.setRestaurantDesc(desc);
            vdata.setRestaurantImage(image);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void validateDevice(String hotelname, String roomnumber){
        //String url = api_url_used + "validate.php";
        String url2 = vdata.getApiUrl() + "validate.php";
        JSONObject data = new JSONObject();
        String devicemodelname = getDeviceName();
        try {
            data.put("deviceid", unique_id);
            data.put("modelname", devicemodelname);
            data.put("hotelname", hotelname);
            data.put("roomnumber", roomnumber);
        } catch (JSONException e) {
            Log.d("JSON DataPOSTException", e.getLocalizedMessage());
        }

        nc.postdataObject(url2, getApplicationContext(), data, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                //checkDevice();
                Log.d("validated","");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void hotelservices_activity(){
        Intent i = new Intent(this, HotelServicesActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }

    public void launcher_activity(){
        Intent i = new Intent(this, LauncherActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "main");
        startActivity(i);
    }

//    public void changeBackground(){
//        //Dynamic Background
//
//        final RelativeLayout background = (RelativeLayout) findViewById(R.id.continueButton);
//        Resources res = getResources();
//        final TypedArray myImages = res.obtainTypedArray(R.array.myImages);
//        final Random random = new Random();
//        int randomInt = random.nextInt(myImages.length());
//        int drawableID = myImages.getResourceId(randomInt, -1);
//        background.setBackgroundResource(drawableID);
//
//
//
//    }

    // End of Dynamic Background




    // For Lang Select Method
    public void langSelect(View view){
        Intent launchIntent = new Intent(this, MainLangActivity.class);
        launchIntent.putExtra(Variable.EXTRA, vdata);
        launchIntent.putExtra(LANG_SELECT_FROM, "launcher");
        startActivity(launchIntent);
    }





    public void openCheckinPackage(View view){

//        Toast.makeText(getApplicationContext(), String.valueOf(vdata.getQLroomID()), Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(), String.valueOf(vdata.getQLroomStatus()), Toast.LENGTH_LONG).show();

        if(vdata.getQLroomStatus().equals("active")){
            Toast.makeText(getApplicationContext(), "You are still checked In. Please Checkout", Toast.LENGTH_LONG).show();
            launcher_activity();
        }else {
            final Dialog dialog = new Dialog(this);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.checkin_type);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            promoBttn = dialog.findViewById(R.id.promoBttn);

            promoBttn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

            dialog.show();
        }


    }


    public void onClickPromoTime(View view){
        qlcheckURL02 = vdata.getApiUrl() + "addroomactivity.php?room_id=" + vdata.getQLroomID();

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("CHECKIN")
                        .setMessage("Press OK to CHECKIN!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                HttpAsyncTask hat = new HttpAsyncTask();
                                hat.execute(qlcheckURL02);
                                timesession();
                                launcher_activity();

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



    public void timesession(){

        preferenceSettingsTime.edit().clear().commit();
        preferenceEditorTime = preferenceSettingsTime.edit();
        preferenceEditorTime.putLong("checkintime", System.currentTimeMillis());
        preferenceEditorTime.commit();
    }

    

    public void roomdata(){
        Log.d("API Url: ", vdata.getApiUrl().toString());
        Log.d("Server Url: ", vdata.getServerURL().toString());
        String url = vdata.getApiUrl() + "getroominfo.php?device_id=" + unique_id;
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
//                Toast.makeText(getApplicationContext(), String.valueOf(url), Toast.LENGTH_LONG).show();
                try {
                    Boolean res = response.getBoolean("success");
                    if(res){
                        JSONObject roominfo = response.getJSONObject("roominfo");
                        String qlroom_no = roominfo.getString("room_no");
                        String qlroom_id = roominfo.getString("room_id");
                        String qlroom_type = roominfo.getString("room_type");
                        String qlroom_status = roominfo.getString("room_status");
                        String qlroom_price = roominfo.getString("room_price");


                        vdata.setQLroomID(qlroom_id);
                        vdata.setQLroomNo(qlroom_no);
                        vdata.setQLroomType(qlroom_type);
                        vdata.setQLroomStatus(qlroom_status);
                        vdata.setQLroomPrice(qlroom_price);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message = "Room is not assigned on this device.";
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Device Not Assigned");
                    alertDialog.setMessage(message);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            View decorView = getWindow().getDecorView();
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                    });
                }

            }

            @Override
            public void onError(VolleyError error) {
                message = "Not Connected to Internet";
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("The device is not connected to Internet");
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        View decorView = getWindow().getDecorView();
                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                });
            }
        });
    }

//    public void videoViewPlay(){
//
//        Resources res = getResources();
//        String[] ADS = res.getStringArray(R.array.myADS);
//        String randomStr = ADS[new Random().nextInt(ADS.length)];
//
//        VideoView videoView = findViewById(R.id.videoViewMain);
//
//        videoView.setVideoPath(String.valueOf(randomStr));
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//                videoView.start();
//            }
//        });
//
//
//    }



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


    public void ToastHomeButton() {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_container,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView ToastText = (TextView) layout.findViewById(R.id.toastText);
        ToastText.setText("  Home button disabled!  ");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }



}
