package com.direct2guests.d2g_tv.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.direct2guests.d2g_tv.Activities.ChatActivity.FrontDeskChatActivity;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.FOCUS_LEFT;
import static com.direct2guests.d2g_tv.Activities.LauncherActivity.WATCHTV_FROM;
import static java.lang.Integer.parseInt;




public class HotelServicesActivity extends LangSelectActivity {
    Variable vdata;
    NetworkConnection nc = new NetworkConnection();
    public final static String CLICK_FROM = "com.direct2guests.d2g_tv.CLICK_FROM";
    public final static String PREFERENCE = "MY_PREFERENCE";
    public final static String CHATCOUNT = "CHAT_COUNT";
    public final static String LASTCOUNT = "LAST_COUNT";

    private HorizontalScrollView menuserviceshotel;


    private TextView FDClock;

    private LinearLayout linearLayoutServices;
    /* ========== Hotel Service HomeScreen ========== */
    private ImageView hotelimg_view, weather_icon;
    private TextView welcomeguest_txtview, date_txtview, weather_description, weather_temp;
    private Button chat_message, currty, channelButton, vodclickButton;

    private TextView  txtfrontdesk, txtfood, txtplaces, txtservices, txthousekeeping, txtoffers, txtfeedback,txtinfo ,txtwatchtv, txtwatchvod, txtflight;
    private TextClock timeDaily;
    private FrameLayout Foods, Services, Offers, Housekeeping, FrontdeskChat, Places, Feedback, Info, Faq, WatchTV, WatchVOD, FlightTracker;
    private TextView dateView_hs;
    /* ========== End ========== */

    private String[] access;
    private String currentDateString, lastClick;

    /* ========== Housekeeping Modal ========== */
    private Button HKToday, HKWhole, HKRequest;
    private TextView HKDate, HKStatus, HKCancelHouseKeeping, HKTitle, HKKeeping;
    private String urlCancelToday, urlCancelWhole, urlRequestHK, urlGetStatus, chatFrom;
    private int HKFocus = 0;
    /* ========== End ========== */

    /* ========== Guest Info Modal ========== */
    private TextView GName, GRoom, GCheckIn, GCheckOut, GWithOutTax, GWithTax, GBilling;
    private TextView GuestInfoTitle, GuestName, GuestRoomNo, GCheckIO, GuestCheckIn, GuestCheckOut;
    /* ========== End ========== */

    /* ========== Feedback Info Modal ========== */
    private TextView txtFeedbackTitle,txtstar, txtoverall, txtlocation, txtroom,  txtservice, txtvalue, txtcleanliness, txtrestaurant;
    private EditText txtcomment;
    private Button btnsubmit;
    private RatingBar ratingOverall, ratingLocation, ratingRoom, ratingService, ratingValue, ratingCleanliness, ratingRestaurant;
    // ========== Feedback Info Modal float ========== //
    int overall, location, rooms, service, value, cleanliness, restaurant;
    /* ========== End ========== */
    private Dialog feedbackDialog;

    /* ============== Forex ============= */
    private Spinner fromcurr, tocurr;
    private EditText fromVal, toVal;
    private int fromSpinVal, toSpinVal, equalityFrom, equalityTo;
    /* ========== End ========== */

    /* ============ Weather ============ */

    private RelativeLayout weatherLayout;

    /* ============ End ============= */

    /* =========== Flight Tracker ============ */
    private Button searchFlight;
    private EditText trackID;
    private String base_url;
    private TextView fIdent, fStatus, fDCode, fOCode, fOCity, fODate, fDCity, fDDate;
    private RelativeLayout fDetails;

    /* ============ End =========== */

    private Tracker mTracker;

    /* Chat Notification */
    private TextView notif_number;
    private Button notif_button;
    private JsonRequest jsonRequest;
    private String thread_url;
    private String[] msgs, frm;
    private JSONArray array;
    private JSONObject chatobj;
    private RequestQueue queue;
    private int lastCount, resCount, anotherCount;
    private Ringtone r;
    private Uri notification;
    private Thread t;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private BroadcastReceiver broadcast_reciever;
    private RelativeLayout chatNotiff;


    private ImageView arrowLeft, arrowRight;
    private FirebaseAnalytics mFirebaseAnalytics;

    // Font path

    String fontPathBold = "fonts/brandonbold.ttf";
    String fontPathReg = "fonts/brandonreg.ttf";


    TextView guestName;

    @SuppressLint("SetTextI18n")
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
            setContentView(R.layout.activity_hotel_services);
        }catch (RuntimeException e){
            onCreate(configBundle);
        }









        menuserviceshotel = findViewById(R.id.horizontalScrollView);
        linearLayoutServices = findViewById(R.id.lineLayoutServices);



        AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "HotelServicesActivity");
        params.putString("app_event", "Menu Selection");
        mFirebaseAnalytics.logEvent("app_activity", params);

        arrowLeft = findViewById(R.id.arrowLeft);
        arrowRight = findViewById(R.id.arrowRight);



        // Loading Font Face
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        chatFrom = getIntent().getStringExtra(FrontDeskChatActivity.CHAT_FROM);
        lastClick = getIntent().getStringExtra(HotelServicesActivity.CLICK_FROM);
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        currentDateString = dateFormat.format(date);

        weatherLayout = findViewById(R.id.weather);
        weather_description = findViewById(R.id.weatherDescription);
        weather_icon = findViewById(R.id.weatherIcon);
        weather_temp = findViewById(R.id.weatherTemp);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        welcomeguest_txtview = findViewById(R.id.welcomeGuest_hs);
        hotelimg_view = findViewById(R.id.hotelLogo_hs);
        date_txtview = findViewById(R.id.dateView_hs);
        FrontdeskChat = findViewById(R.id.frontdesk_frame);
        Foods = findViewById(R.id.foods_frame);
        Services = findViewById(R.id.services_frame);
        Offers = findViewById(R.id.offers_frame);
        Housekeeping = findViewById(R.id.house_frame);
        Places = findViewById(R.id.places_frame);
        Feedback = findViewById(R.id.feedback_frame);
        Info = findViewById(R.id.info_frame);
        Faq = findViewById(R.id.faq_frame);
        WatchTV = findViewById(R.id.watchtv_frame);
        WatchVOD = findViewById(R.id.watchvod_frame);
        chat_message = findViewById(R.id.chat_message);
        FlightTracker = findViewById(R.id.flight_frame);
        currty = findViewById(R.id.currency);
        // text view label
        txtfrontdesk = findViewById(R.id.txtfrontdesk);
        txtfood = findViewById(R.id.txtfood);
        txtplaces = findViewById(R.id.txtplaces);
        txtservices = findViewById(R.id.txtservices);
        txthousekeeping = findViewById(R.id.txthousekeeping);
        txtoffers = findViewById(R.id.txtoffer);
        txtfeedback = findViewById(R.id.txtfeedback);
        txtinfo = findViewById(R.id.txtinfo);
        txtwatchtv = findViewById(R.id.txtwatchtv);
        txtwatchvod = findViewById(R.id.txtwatchvod);
        timeDaily = findViewById(R.id.timeDaily);
        txtflight = findViewById(R.id.txtflight);

        chatNotiff = findViewById(R.id.notif);


        FDClock = findViewById(R.id.textClock3);
        guestName = findViewById(R.id.guestNameTxtV3);
        guestName.setText(vdata.getGuestFirstName() + " " + vdata.getGuestLastName());



        //Applying font
        txtfrontdesk.setTypeface(fontBold);
        txtfood.setTypeface(fontBold);
        txtplaces.setTypeface(fontBold);
        txtservices.setTypeface(fontBold);
        txthousekeeping.setTypeface(fontBold);
        txtoffers.setTypeface(fontBold);
        txtfeedback.setTypeface(fontBold);
        txtinfo.setTypeface(fontBold);
        txtwatchtv.setTypeface(fontBold);
        txtwatchvod.setTypeface(fontBold);
        date_txtview.setTypeface(fontReg);
        timeDaily.setTypeface(fontReg);
        txtflight.setTypeface(fontBold);
       welcomeguest_txtview.setTypeface(fontBold);
        guestName.setTypeface(fontBold);
        FDClock.setTypeface(fontBold);

        date_txtview.setText(currentDateString);
        welcomeguest_txtview.setText(getString(R.string.launcherHello) + " " + vdata.getGuestFirstName() + "!");

        access = vdata.getHotelAccess();
//        Picasso.with(getApplicationContext()).load(vdata.getServerURL()+vdata.getHotelLogo()).into(hotelimg_view);

        onFocusFrames();
        setAccess(access);


    }

    @Override
    public void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        broadcast_reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_hotelservices")) {
                    //finishing the activity
                    finish();
                }
            }
        };
//        registerReceiver(broadcast_reciever, new IntentFilter("finish_hotelservices"));

//        mTracker.setScreenName(vdata.getHotelName()+" ~ Room No. "+vdata.getRoomNumber()+" ~ "+"Hotel Services View");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//
        getWeatherReport();
        if(sharedPreferences.contains(CHATCOUNT)){
            anotherCount = Integer.parseInt(sharedPreferences.getString(CHATCOUNT, ""));
        }
        if(sharedPreferences.contains(LASTCOUNT)){
            lastCount = Integer.parseInt(sharedPreferences.getString(LASTCOUNT, ""));
        }
        if(anotherCount <= 0) {
            notif_number.setVisibility(View.INVISIBLE);
        }else{
            notif_number.setText(Integer.toString(anotherCount));
            notif_number.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                loadChatMessages();
                setNotif();
            }
        }
        notif_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notif_number.setVisibility(View.INVISIBLE);
                anotherCount = 0;
                editor.putString(CHATCOUNT, Integer.toString(anotherCount));
                editor.commit();
                t.interrupt();
                Intent i = new Intent(HotelServicesActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        //end code hide status bar
        /*if(lastClick != null){
            FocusOn(lastClick);
        }*/

//        FrontdeskChat.requestFocus();




    }

    @Override
    public void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
//                chatNotiff.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
    }
    @Override
    public void onBackPressed(){
        /*Intent i = new Intent(this, LauncherActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);*/
//        t.interrupt();
//        super.onBackPressed();



        Intent i = new Intent(HotelServicesActivity.this, MainActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "hotelservices");
        startActivity(i);


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
//            //Toast.makeText(this, "Home button disabled!", Toast.LENGTH_LONG).show();
//            View view;
//            Toast toast = Toast.makeText(getApplicationContext(), "    Home button disabled!    ", Toast.LENGTH_SHORT);
//            view = toast.getView();
//            view.setBackgroundColor(getResources().getColor(R.color.questpurple));
//            toast.show();
            ToastHomeButton();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    // end of disable home button



    public void onFocusFrames(){
        FrontdeskChat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {


                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                    if(hasFocus){
                        txtfrontdesk.setTextColor(Color.parseColor("#ffffff"));
                    } else{
                        txtfrontdesk.setTextColor(Color.parseColor("#878782"));
                    }
            }
        });
        Foods.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {


                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtfood.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtfood.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Services.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtservices.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtservices.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Offers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtoffers.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtoffers.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Housekeeping.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txthousekeeping.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txthousekeeping.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Places.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtplaces.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtplaces.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Feedback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtfeedback.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtfeedback.setTextColor(Color.parseColor("#878782"));
                }
            }
        });
        Info.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtinfo.setTextColor(Color.parseColor("#ffffff"));

                    arrowRight.setAlpha((float) 0);

                } else{
                    txtinfo.setTextColor(Color.parseColor("#878782"));
                    arrowRight.setAlpha((float) 1);

                }
            }
        });
        Faq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);

            }
        });
        WatchTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtwatchtv.setTextColor(Color.parseColor("#ffffff"));
                    arrowLeft.setAlpha((float) 0);

                } else{
                    txtwatchtv.setTextColor(Color.parseColor("#878782"));
                    arrowLeft.setAlpha((float) 1);

                }
            }
        });
        WatchVOD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusAnim(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
                if(hasFocus){
                    txtwatchvod.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    txtwatchvod.setTextColor(Color.parseColor("#878782"));
                }
            }
        });

        FlightTracker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               vdata.focusAnim(view,b,getApplicationContext(),HotelServicesActivity.this);

            }
        });

        //change location onclick
        chat_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HotelServicesActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservices");
                startActivity(i);
                finish();

            }

        });

        currty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),HotelServicesActivity.this);
            }
        });

        /*menuserviceshotel.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = menuserviceshotel.getScrollY();
                int scrollX = menuserviceshotel.getScrollX();
                Log.d("X and Y : ", scrollX+" & "+scrollY);
                if(scrollX >= 2613){
                    Log.d("Passed","/");
                    menuserviceshotel.scrollTo(98,0);
                    FrontdeskChat.requestFocus();
                }else if(scrollX <= 80){
                    menuserviceshotel.scrollTo(2531,0);
                    WatchTV.requestFocus();
                }
            }
        });*/

    }

    public void setAccess(String[] access){
        for (int i = 0; i < access.length; i++){
            if(access[i].equals("food_acc")){
                Foods.setVisibility(View.VISIBLE);
                Log.d("Access", "::  " + access[i].toString());
            }else if(access[i].equals("services_acc")){
                Services.setVisibility(View.VISIBLE);
                Log.d("Access", "::  " + access[i].toString());
            }else if(access[i].equals("offers_acc")){
                Offers.setVisibility(View.VISIBLE);
                Log.d("Access", "::  " + access[i].toString());
            }else if(access[i].equals("housekeeping_acc")){
                Housekeeping.setVisibility(View.VISIBLE);
                Log.d("Access", "::  " + access[i].toString());
            }else if(access[i].equals("forex_acc")){
                currty.setVisibility(View.VISIBLE);
            }else if(access[i].equals("flight_acc")){
                FlightTracker.setVisibility(View.VISIBLE);
            }else if(access[i].equals("chat_acc")){
                FrontdeskChat.setVisibility(View.VISIBLE);
                chatNotiff.setVisibility(View.VISIBLE);
            }else if(access[i].equals("ads_acc")){
                Places.setVisibility(View.VISIBLE);
            }else if(access[i].equals("feedback_acc")){
                Feedback.setVisibility(View.VISIBLE);
            }else if(access[i].equals("info_acc")){
                Info.setVisibility(View.VISIBLE);
            }else if(access[i].equals("watchtv_acc")){
                WatchTV.setVisibility(View.VISIBLE);
            }else if(access[i].equals("faq_acc")){
                Places.setVisibility(View.VISIBLE);
            }else {
                Log.d("Access", "Type: "+access[i].toString());
            }
        }
    }

    public void openChat(View view){
        t.interrupt();
        Intent i = new Intent(this, FrontDeskChatActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
        startActivity(i);
    }

    public void openFood(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, FoodDrinksActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }

    public void openPlaces(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, PlacesNearbyActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }


    public void openHousekeeping(final View view){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.activity_house_keeping);

        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar

        // Loading Font Face
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);


        HKToday = dialog.findViewById(R.id.TodayHKBtnS);
        HKWhole = dialog.findViewById(R.id.WholeHKBtn);
        HKRequest = dialog.findViewById(R.id.RequestHKBtnS);

        HKDate = dialog.findViewById(R.id.checkinDateDialog);
        HKStatus = dialog.findViewById(R.id.HousekeepingStatusS);
        HKCancelHouseKeeping = dialog.findViewById(R.id.CancelHouseKeeping);
        HKTitle = dialog.findViewById(R.id.HousekeepingTitleS);
        HKKeeping = dialog.findViewById(R.id.RequestHouseKeepingS);

        //Applying font
        HKToday.setTypeface(fontReg);
        HKWhole.setTypeface(fontReg);
        HKRequest.setTypeface(fontReg);
        HKStatus.setTypeface(fontReg);
        HKKeeping.setTypeface(fontReg);
        HKCancelHouseKeeping.setTypeface(fontReg);

        HKTitle.setTypeface(fontBold);

        HKDate.setText(currentDateString);

        urlCancelToday = vdata.getApiUrl() + "canceltodayhousekeeping.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        urlCancelWhole = vdata.getApiUrl() + "cancelwholehousekeeping.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        urlRequestHK = vdata.getApiUrl() + "requesthousekeeping.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        urlGetStatus = vdata.getApiUrl() + "housekeepingstatus.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        nc.getdataObject(urlGetStatus, getApplicationContext(), new VolleyCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if(status.equals("Requested Housekeeping")){
                        HKStatus.setText("Status : Housekeeping Requested");
                    }else if(status.equals("Cancel Housekeeping Today")){
                        HKStatus.setText("Status : Cancelled Today");
                    }else if(status.equals("Cancel Housekeeping Whole Stay")){
                        HKStatus.setText("Status : Cancelled for Whole Stay");
                    }else{
                        HKStatus.setText("Status : Cancelled for Whole Stay");
                    }
                }catch (JSONException e){
                    Log.d("HK Status", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        HKToday.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(HotelServicesActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                nc.getdataObject(urlCancelToday, getApplicationContext(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        HKStatus.setText("Status : Cancelled Today");
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.d("Cancel Today", error.getLocalizedMessage());
                                        error.printStackTrace();
                                    }
                                });
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        HKWhole.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(HotelServicesActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                nc.getdataObject(urlCancelWhole, getApplicationContext(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        HKStatus.setText("Status : Cancelled for Whole Stay");
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.d("Cancel WholeStay", error.getLocalizedMessage());
                                        error.printStackTrace();
                                    }
                                });
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        HKRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AlertDialog.Builder(HotelServicesActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                nc.getdataObject(urlRequestHK, getApplicationContext(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        HKStatus.setText("Status : Housekeeping Requested");
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.d("Request HK", error.getLocalizedMessage());
                                        error.printStackTrace();
                                    }
                                });
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        HKToday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonpurple));
                    HKFocus = 0;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonbrown));
                }

            }
        });
        HKWhole.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonpurple));
                    HKFocus = 1;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonbrown));
                }
            }
        });
        HKRequest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonpurple));
                    HKFocus = 2;
                } else{
                    view.setBackgroundTintList(getColorStateList(R.color.crimsonbrown));
                }
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
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

    public void openServices(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, ServicesActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }

    public void openOffers(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, OffersActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }

    public void openFeedback(View view){
        feedbackDialog = new Dialog(this);
        feedbackDialog.setCanceledOnTouchOutside(false);
        feedbackDialog.setContentView(R.layout.activity_feed_back);

//        // Loading Font Face
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        txtFeedbackTitle = feedbackDialog.findViewById(R.id.txtFeedbackTitle);
        txtoverall = feedbackDialog.findViewById(R.id.txtoverall);
        txtlocation = feedbackDialog.findViewById(R.id.txtlocation);
        txtroom = feedbackDialog.findViewById(R.id.txtroom);
        txtservice = feedbackDialog.findViewById(R.id.txtservice);
        txtvalue = feedbackDialog.findViewById(R.id.txtvalue);
        txtcleanliness = feedbackDialog.findViewById(R.id.txtcleanliness);
        txtrestaurant = feedbackDialog.findViewById(R.id.txtrestaurant);
        txtcomment = feedbackDialog.findViewById(R.id.txtcomment);
        txtstar = feedbackDialog.findViewById(R.id.txtstar);
        txtcomment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    sendRating();
                    return true;
                }
                return false;
            }
        });
        //Applying font
        txtoverall.setTypeface(fontReg);
        txtlocation.setTypeface(fontReg);
        txtroom.setTypeface(fontReg);
        txtservice.setTypeface(fontReg);
        txtvalue.setTypeface(fontReg);
        txtcleanliness.setTypeface(fontReg);
        txtrestaurant.setTypeface(fontReg);
        txtFeedbackTitle.setTypeface(fontBold);
        txtstar.setTypeface(fontBold);

        ratingOverall  = feedbackDialog.findViewById(R.id.ratingOverall);
        ratingLocation = feedbackDialog.findViewById(R.id.ratingLocation);
        ratingRoom = feedbackDialog.findViewById(R.id.ratingRoom);
        ratingService = feedbackDialog.findViewById(R.id.ratingService);
        ratingValue = feedbackDialog.findViewById(R.id.ratingValue);
        ratingCleanliness = feedbackDialog.findViewById(R.id.ratingcleanliness);
        ratingRestaurant = feedbackDialog.findViewById(R.id.ratingRestaurant);

        btnsubmit = feedbackDialog.findViewById(R.id.btnsubmit);


        btnsubmit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.hometint));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }
            }
        });

        ratingOverall.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingRoom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingService.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingCleanliness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        ratingRestaurant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.quantitybuttoncartblur));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(HotelServicesActivity.this, R.color.feedbackblur));
                }
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                clickSubmits(v);
            }
        });

        feedbackDialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    feedbackDialog.dismiss();
                    //start code hide status bar
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    //end code hide status bar
                }
                return false;
            }
        });
        feedbackDialog.show();
    }

    public void clickSubmits(View view) {
        overall = Math.round(ratingOverall.getRating());
        location = Math.round(ratingLocation.getRating());
        rooms = Math.round(ratingRoom.getRating());
        service = Math.round(ratingService.getRating());
        value = Math.round(ratingValue.getRating());
        cleanliness = Math.round(ratingCleanliness.getRating());
        restaurant = Math.round(ratingRestaurant.getRating());


        Log.d("Feedbacks",""+overall+","+location+","+rooms+","+service+","+value+","+cleanliness+","+restaurant+","+txtcomment.getText()+","+vdata.getHotelID()+","+vdata.getUniqueID());

        String commentValue = String.valueOf(txtcomment.getText());

        String url = vdata.getApiUrl() + "sendfeedback.php";

        JSONObject data = new JSONObject();

        try {
            data.put("message", txtcomment.getText().toString());
            data.put("hotel_id", vdata.getHotelID());
            data.put("guest_id", vdata.getGuestID());
            data.put("overall",Integer.toString(overall));
            data.put("location", Integer.toString(location));
            data.put("room", Integer.toString(rooms));
            data.put("service",Integer.toString(service));
            data.put("value", Integer.toString(value));
            data.put("cleanliness", Integer.toString(cleanliness));
            data.put("restaurant", Integer.toString(restaurant));

        } catch (JSONException e) {
            Log.d("JSON DataPOSTException", e.getLocalizedMessage());
        }

        nc.postdataObject(url, getApplicationContext(), data, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                //Toast.makeText(getApplicationContext(), " Thank you for successfully submitting your feedback ", Toast.LENGTH_LONG).show();

                View view;
                Toast toast = Toast.makeText(getApplicationContext(), "    Thank you for successfully submitting your feedback    ", Toast.LENGTH_SHORT);
                view = toast.getView();
                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
                toast.show();



                ratingOverall.setRating(0);
                ratingLocation.setRating(0);
                ratingRoom.setRating(0);
                ratingService.setRating(0);
                ratingValue.setRating(0);
                ratingCleanliness.setRating(0);
                ratingRestaurant.setRating(0);
                txtcomment.setText("");
                feedbackDialog.dismiss();
                //start code hide status bar
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                //end code hide status bar
            }

            @Override
            public void onError(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "filled ", Toast.LENGTH_LONG).show();

                View view;
                Toast toast = Toast.makeText(getApplicationContext(), "    filled    ", Toast.LENGTH_SHORT);
                view = toast.getView();
                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
                toast.show();


            }
        });
    }

    private void sendRating(){
        overall = Math.round(ratingOverall.getRating());
        location = Math.round(ratingLocation.getRating());
        rooms = Math.round(ratingRoom.getRating());
        service = Math.round(ratingService.getRating());
        value = Math.round(ratingValue.getRating());
        cleanliness = Math.round(ratingCleanliness.getRating());
        restaurant = Math.round(ratingRestaurant.getRating());


        Log.d("Feedbacks",""+overall+","+location+","+rooms+","+service+","+value+","+cleanliness+","+restaurant+","+txtcomment.getText()+","+vdata.getHotelID()+","+vdata.getUniqueID());

        String commentValue = String.valueOf(txtcomment.getText());

        String url = vdata.getApiUrl() + "sendfeedback.php";

        JSONObject data = new JSONObject();

        try {
            data.put("message", txtcomment.getText().toString());
            data.put("hotel_id", vdata.getHotelID());
            data.put("guest_id", vdata.getGuestID());
            data.put("overall",Integer.toString(overall));
            data.put("location", Integer.toString(location));
            data.put("room", Integer.toString(rooms));
            data.put("service",Integer.toString(service));
            data.put("value", Integer.toString(value));
            data.put("cleanliness", Integer.toString(cleanliness));
            data.put("restaurant", Integer.toString(restaurant));
            Log.d("FEEDBACK :", ""+Integer.toString(overall)+","+Integer.toString(location)+","+Integer.toString(rooms)+","+Integer.toString(service)+","+Integer.toString(value)+","+Integer.toString(cleanliness)+","+Integer.toString(restaurant));
        } catch (JSONException e) {
            Log.d("JSON DataPOSTException", e.getLocalizedMessage());
        }

        nc.postdataObject(url, getApplicationContext(), data, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
//                Toast.makeText(getApplicationContext(), " Thank you for successfully submitting your feedback ", Toast.LENGTH_LONG).show();

                View view;
                Toast toast = Toast.makeText(getApplicationContext(), "    Thank you for successfully submitting your feedback    ", Toast.LENGTH_SHORT);
                view = toast.getView();
                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
                toast.show();



                ratingOverall.setRating(0);
                ratingLocation.setRating(0);
                ratingRoom.setRating(0);
                ratingService.setRating(0);
                ratingValue.setRating(0);
                ratingCleanliness.setRating(0);
                ratingRestaurant.setRating(0);
                txtcomment.setText("");
                feedbackDialog.dismiss();
                //start code hide status bar
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
                //end code hide status bar
            }

            @Override
            public void onError(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "filled ", Toast.LENGTH_LONG).show();

                View view;
                Toast toast = Toast.makeText(getApplicationContext(), "    filled    ", Toast.LENGTH_SHORT);
                view = toast.getView();
                view.setBackgroundColor(getResources().getColor(R.color.questpurple));
                toast.show();


            }
        });
    }

    public void openFlights(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, FlightTrackerActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra("LAST_COUNT", lastCount);
        i.putExtra("ANOTHER_COUNT", anotherCount);
        startActivity(i);

    }

    public void openInfo(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.activity_guest_info);

        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar

        // Loading Font Face
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

        GName = dialog.findViewById(R.id.GuestName);
        GRoom = dialog.findViewById(R.id.GuestRoomNo);
        GCheckIn = dialog.findViewById(R.id.GuestCheckIn);
        GCheckOut = dialog.findViewById(R.id.GuestCheckOut);
        GWithOutTax = dialog.findViewById(R.id.GWithOutTax);
        GWithTax = dialog.findViewById(R.id.GWithTax);
        GBilling = dialog.findViewById(R.id.GBilling);
        GWithTax = dialog.findViewById(R.id.GWithTax);

        GuestInfoTitle = dialog.findViewById(R.id.GuestInfoTitle);
        GuestName = dialog.findViewById(R.id.GName);
        GuestRoomNo = dialog.findViewById(R.id.GRoom);
        GCheckIO = dialog.findViewById(R.id.GCheckIO);

        //Applying font
        GName.setTypeface(fontReg);
        GRoom.setTypeface(fontReg);
        GCheckIn.setTypeface(fontReg);
        GCheckOut.setTypeface(fontReg);

        GuestInfoTitle.setTypeface(fontReg);
        GuestName.setTypeface(fontReg);
        GuestRoomNo.setTypeface(fontReg);
        GCheckIO.setTypeface(fontReg);

        GName.setText(vdata.getGuestFirstName()+" "+vdata.getGuestLastName());
        GRoom.setText(vdata.getRoomNumber());
//        GCheckIn.setText("Check-In : " + vdata.getCheckIn());
//        GCheckOut.setText("Check-Out : " + vdata.getCheckOut());

        GWithOutTax.setText(vdata.getBillWithOutTax());
        GWithTax.setText(vdata.getBillWithTax() + " php");


        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {

                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    //start code hide status bar
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    //end code hide status bar
                }

                return false;

            }


        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();



    }

    public void openFaq(View view){
        for (int i = 0; i < access.length; i++) {
            if (access[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        Intent i = new Intent(this, FaqActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        startActivity(i);
    }

    public void openCurrency(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_forex_modal);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        fromcurr = dialog.findViewById(R.id.fromCurrency);
        tocurr = dialog.findViewById(R.id.toCurrency);
        fromVal = dialog.findViewById(R.id.fromValue);
        toVal = dialog.findViewById(R.id.toValue);
        fromVal.setFocusable(true);
        toVal.setFocusable(true);
        fromcurr.setFocusable(true);
        tocurr.setFocusable(true);
        equalityFrom = 0;
        equalityTo = 0;
        fromVal.setText("1");
        fromcurr.setSelection(1);
        fromSpinVal = fromcurr.getSelectedItemPosition();
        toSpinVal = tocurr.getSelectedItemPosition();
        //on show function
        String url = "http://api.fixer.io/latest?base=USD&symbols=PHP";
        nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject rates = response.getJSONObject("rates");
                    String PesoVal = rates.getString("PHP");
                    toVal.setText(PesoVal);
                }catch (JSONException e){

                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
        //end function
        fromcurr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(fromcurr.getSelectedItem().toString().equals(tocurr.getSelectedItem().toString())){
                    String oldVal = fromVal.getText().toString();
                    fromVal.setText(toVal.getText().toString());
                    toVal.setText(oldVal);
                    fromcurr.setSelection(toSpinVal);
                    tocurr.setSelection(fromSpinVal);
                    fromSpinVal = fromcurr.getSelectedItemPosition();
                    toSpinVal = tocurr.getSelectedItemPosition();
                    equalityFrom = 1;
                    equalityTo = 1;
                }else {
                    if(equalityFrom == 1){
                        equalityFrom = 0;
                    }else {
                        Log.d("currency : ", fromcurr.getSelectedItem().toString());
                        fromSpinVal = fromcurr.getSelectedItemPosition();
                        toSpinVal = tocurr.getSelectedItemPosition();
                        String url1 = "http://api.fixer.io/latest?base=" + fromcurr.getSelectedItem().toString() + "&symbols=" + tocurr.getSelectedItem().toString();
                        nc.getdataObject(url1, getApplicationContext(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    JSONObject rates = response.getJSONObject("rates");
                                    String currval = rates.getString(tocurr.getSelectedItem().toString());
                                    Double fVal = Double.parseDouble(fromVal.getText().toString());
                                    Double tVal = Double.parseDouble(currval);
                                    Double forexVal = fVal * tVal;
                                    toVal.setText(String.format("%.2f", forexVal));
                                } catch (JSONException e) {

                                }
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tocurr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("currency : ", tocurr.getSelectedItem().toString());
                if(tocurr.getSelectedItem().toString().equals(fromcurr.getSelectedItem().toString())){
                    String oldVal = toVal.getText().toString();
                    toVal.setText(fromVal.getText().toString());
                    fromVal.setText(oldVal);
                    tocurr.setSelection(fromSpinVal);
                    fromcurr.setSelection(toSpinVal);
                    fromSpinVal = fromcurr.getSelectedItemPosition();
                    toSpinVal = tocurr.getSelectedItemPosition();
                    equalityFrom = 1;
                    equalityTo = 1;
                }else {
                    if(equalityTo == 1){
                        equalityTo = 0;
                    }else {
                        fromSpinVal = fromcurr.getSelectedItemPosition();
                        toSpinVal = tocurr.getSelectedItemPosition();
                        String url1 = "http://api.fixer.io/latest?base=" + fromcurr.getSelectedItem().toString() + "&symbols=" + tocurr.getSelectedItem().toString();
                        nc.getdataObject(url1, getApplicationContext(), new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                try {
                                    JSONObject rates = response.getJSONObject("rates");
                                    String currval = rates.getString(tocurr.getSelectedItem().toString());
                                    Double fVal = Double.parseDouble(fromVal.getText().toString());
                                    Double tVal = Double.parseDouble(currval);
                                    Double forexVal = fVal * tVal;
                                    toVal.setText(String.format("%.2f", forexVal));
                                } catch (JSONException e) {

                                }
                            }

                            @Override
                            public void onError(VolleyError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fromVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("Caught", "FromValue changing");
                if (fromVal.getText().toString().isEmpty()) {
                    fromVal.setText("0");
                }
                String url = "http://api.fixer.io/latest?base=" + fromcurr.getSelectedItem().toString() + "&symbols=" + tocurr.getSelectedItem().toString();
                nc.getdataObject(url, getApplicationContext(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            JSONObject rates = response.getJSONObject("rates");
                            String currval = rates.getString(tocurr.getSelectedItem().toString());
                            Double fVal = Double.parseDouble(fromVal.getText().toString());
                            Double tVal = Double.parseDouble(currval);
                            Double forexVal = fVal * tVal;
                            toVal.setText(String.format("%.2f", forexVal));
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    //start code hide status bar
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                    //end code hide status bar
                }
                return false;
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    public void openTV(View view){

        Intent i = new Intent(this, ChannelListActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(WATCHTV_FROM, "launcher");
        startActivity(i);
    }

    public void openVOD(View view){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.android.tvleanback");
        startActivity(launchIntent);
    }


    /*private void FocusOn(String from){
        if(from.equals("chat")){
            FrontdeskChat.requestFocus();
        }else if(from.equals("food")){
            Foods.requestFocus();
        }else if(from.equals("places")){
            Places.requestFocus();
        }else if(from.equals("services")){
            Services.requestFocus();
        }else if(from.equals("offers")){
            Offers.requestFocus();
        }else if(from.equals("watchtv")){
            WatchTV.requestFocus();
        }
    }*/

    private void getWeatherReport(){
        String url = "http://api.openweathermap.org/data/2.5/weather?id="+vdata.getWeatherid()+"&appid=f4d527523a8ec6989f6e543ffc16ea25&units=metric";
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
                        weatherLayout.setVisibility(View.VISIBLE);
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

    private void setNotif(){
//        if(chatNotiff.getVisibility() == View.VISIBLE) {
            thread_url = vdata.getApiUrl() + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
            t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(6000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    jsonRequest = new JsonArrayRequest(Request.Method.GET, thread_url, null,
                                            new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    try {
                                                        array = response;
                                                        if (array.length() > 0) {
                                                            msgs = new String[array.length()];
                                                            frm = new String[array.length()];
                                                            resCount = 0;
                                                            anotherCount = 0;
                                                            if (array != null) {
                                                                for (int i = 0; i < array.length(); i++) {
                                                                    chatobj = array.getJSONObject(i);
                                                                    if (chatobj.getString("status").equals("unseen") && chatobj.getString("msg_from").equals("admin")) {
                                                                        resCount++;
                                                                    }
                                                                }
                                                            }
                                                            Log.d("resCount :", Integer.toString(resCount));
                                                            if (lastCount < resCount) {
                                                                if (notif_number.getVisibility() == View.INVISIBLE) {
                                                                    notif_number.setVisibility(View.VISIBLE);
                                                                }
                                                                notif_number.setText(Integer.toString(lastCount + (resCount - lastCount)));
                                                                lastCount = resCount;
                                                                anotherCount = Integer.parseInt(notif_number.getText().toString());
                                                                editor.putString(CHATCOUNT, Integer.toString(anotherCount));
                                                                editor.putString(LASTCOUNT, Integer.toString(lastCount));
                                                                editor.commit();
                                                                try {
                                                                    notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                                    r.play();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                    } catch (JSONException e) {
                                                        Log.d("JSONException", "Unable to parse JSONArray");
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("Error.Response", error.getLocalizedMessage());
                                                }
                                            }
                                    ) {
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> params = new HashMap<String, String>();
                                            String creds = String.format("%s:%s", "api", "qwerty!@#123");
                                            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                            params.put("Authorization", auth);
                                            return params;
                                        }
                                    };
                                    queue.add(jsonRequest);

                                }
                            });
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
//        }
    }
    public void loadChatMessages() {
        lastCount = 0;
        if(chatNotiff.getVisibility() == View.VISIBLE) {
            String url = vdata.getApiUrl() + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
            nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
                @Override
                public void onSuccess(JSONArray response) {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                if (response.getJSONObject(i).getString("status").equals("unseen") && response.getJSONObject(i).getString("msg_from").equals("admin")) {
                                    lastCount++;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (lastCount > 0) {
                            if (notif_number.getVisibility() == View.INVISIBLE) {
                                notif_number.setVisibility(View.VISIBLE);
                            }
                            notif_number.setText(Integer.toString(lastCount));
                        }

                    }
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d("Downloading Chat", "Fail");
                }
            });
        }
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
