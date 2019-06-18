package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.direct2guests.d2g_tv.Activities.ChatActivity.FrontDeskChatActivity;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static java.lang.Integer.parseInt;

public class FlightTrackerActivity extends Activity {
    Variable vdata;
    NetworkConnection nc = new NetworkConnection();

    private Button HomeButton, BackButton, SearchButton;
    private EditText TrackingID;
    private String base_url;

    private int counterEnroute, counterUpcoming, counterPast;

    //fTable
    private ScrollView flightT;
    private TextView fTomorrow, fToday, fYesterday;
    private TableLayout upcoming, enroute, arrived;
    private TextView txtdate,txtdeparture,txtarrival,txtaircraft,txtstatus;
    private TableRow trU,trE,trP;
    private String[] dataUdate,dataUdeparture,dataUarrival,dataUaircraft,dataUstatus;
    private String[] dataEdate,dataEdeparture,dataEarrival,dataEaircraft,dataEstatus;
    private String[] dataAdate,dataAdeparture,dataAarrival,dataAaircraft,dataAstatus;
    private int l1,l2,l3;

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
    public final static String PREFERENCE = "MY_PREFERENCE";
    public final static String CHATCOUNT = "CHAT_COUNT";
    public final static String LASTCOUNT = "LAST_COUNT";
    private BroadcastReceiver broadcast_reciever;
    private RelativeLayout chatNotiff;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_flight_tracker);
        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();

        HomeButton = findViewById(R.id.homeButtonFlight);
        BackButton = findViewById(R.id.backButtonFlight);
        SearchButton = findViewById(R.id.searchFlight);
        TrackingID = findViewById(R.id.trackingNumber);

        //fTable
        flightT = findViewById(R.id.fTable);
        fTomorrow = findViewById(R.id.flightTomorrow);
        fToday = findViewById(R.id.flightToday);
        fYesterday = findViewById(R.id.flightPast);
        upcoming = findViewById(R.id.upcomingTable);
        enroute = findViewById(R.id.enrouteTable);
        arrived = findViewById(R.id.arrivedTable);

        chatNotiff = findViewById(R.id.notif);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        counterEnroute = 0;
        counterPast = 0;
        counterUpcoming = 0;

            }
    @Override
    protected void onStart(){
        super.onStart();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        flightT.setVisibility(View.INVISIBLE);
        focusButton();
        trackByFlight();
        broadcast_reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_offers")) {
                    //finishing the activity
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_offers"));
        mTracker.setScreenName(vdata.getHotelName()+" ~ Room No. "+vdata.getRoomNumber()+" ~ "+"Flight View");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
                chatNotiff.setVisibility(View.VISIBLE);
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
                Intent i = new Intent(FlightTrackerActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });

        TrackingID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    trackByFlight();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
                chatNotiff.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onBackPressed(){
        /*Intent i = new Intent(this, HotelServicesActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(HotelServicesActivity.CLICK_FROM, "offers");
        startActivity(i);*/
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                t.interrupt();
            }
        }
        super.onBackPressed();
    }


    private void trackByFlight(){
        final RequestQueue queue = Volley.newRequestQueue(this);
        SearchButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.focusIcons(view, b, getApplicationContext(), FlightTrackerActivity.this);
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ident = (TrackingID.getText().toString().toUpperCase());
                flightT.setVisibility(View.VISIBLE);
                base_url = "https://flightxml.flightaware.com/json/FlightXML3/FlightInfoStatus?ident="+ident;
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, base_url, null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{

                                    JSONObject result = response.getJSONObject("FlightInfoStatusResult");
                                    JSONArray flight = result.getJSONArray("flights");
                                    int length = flight.length();
                                    for(int i = 0; i < length; i++){
                                        String status = flight.getJSONObject(i).getString("status");
                                        Log.d("status :", status);
                                        if(status.equals("En")){
                                            counterEnroute++;
                                        }
                                        if(status.equals("Scheduled")){
                                            counterUpcoming++;
                                        }
                                        if(status.equals("Arrived")){
                                            counterPast++;
                                        }
                                    }

                                    if(counterEnroute != 0){
                                        dataEdate = new String[counterEnroute];
                                        dataEdeparture = new String[counterEnroute];
                                        dataEarrival = new String[counterEnroute];
                                        dataEaircraft = new String[counterEnroute];
                                        dataEstatus = new String[counterEnroute];
                                        enroute = findViewById(R.id.enrouteTable);
                                        l1 = 0;
                                        enroute.removeAllViewsInLayout();
                                        addHeaderRowEnroute();
                                        for(int j = 0; j < length; j++){
                                            String status = flight.getJSONObject(j).getString("status");
                                            if(status.equals("En")){
                                                String date = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("date");
                                                String dow = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("dow");

                                                String dtime = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("time");
                                                String dtz = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("tz");
                                                String dapn = flight.getJSONObject(j).getJSONObject("origin").getString("airport_name");
                                                String dalt_ident = flight.getJSONObject(j).getJSONObject("origin").getString("alternate_ident");

                                                String atime = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("time");
                                                String atz = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("tz");
                                                String aapn = flight.getJSONObject(j).getJSONObject("destination").getString("airport_name");
                                                String aalt_ident = flight.getJSONObject(j).getJSONObject("destination").getString("alternate_ident");

                                                trE = new TableRow(getApplicationContext());
                                                trE.setLayoutParams(new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));

                                                dataEdate[l1] = dow + " " + date;
                                                dataEdeparture[l1] = dtime + " " + dtz + "  " + dapn + " - " + dalt_ident;
                                                dataEarrival[l1] = atime + " " + atz + "  " + aapn + " - " + aalt_ident;
                                                dataEaircraft[l1] = flight.getJSONObject(j).getString("aircrafttype");
                                                dataEstatus[l1] = flight.getJSONObject(j).getString("status");

                                                txtdate = new TextView(getApplicationContext());
                                                txtdate.setText(dataEdate[l1]);
                                                txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdate.setPadding(5,5,5,5);
                                                txtdate.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
                                                trE.addView(txtdate);

                                                txtdeparture = new TextView(getApplicationContext());
                                                txtdeparture.setText(dataEdeparture[l1]);
                                                txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdeparture.setPadding(5,5,5,5);
                                                txtdeparture.setLayoutParams(new TableRow.LayoutParams(480,TableRow.LayoutParams.WRAP_CONTENT));
                                                trE.addView(txtdeparture);

                                                txtarrival = new TextView(getApplicationContext());
                                                txtarrival.setText(dataEarrival[l1]);
                                                txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtarrival.setPadding(5,5,5,5);
                                                txtarrival.setLayoutParams(new TableRow.LayoutParams(480, TableRow.LayoutParams.WRAP_CONTENT));
                                                trE.addView(txtarrival);

                                                txtaircraft = new TextView(getApplicationContext());
                                                txtaircraft.setText(dataEaircraft[l1]);
                                                txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtaircraft.setPadding(5,5,5,5);
                                                txtaircraft.setLayoutParams(new TableRow.LayoutParams(180, TableRow.LayoutParams.WRAP_CONTENT));
                                                trE.addView(txtaircraft);

                                                txtstatus = new TextView(getApplicationContext());
                                                txtstatus.setText(dataEstatus[l1]);
                                                txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtstatus.setPadding(5,5,5,5);
                                                txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
                                                trE.addView(txtstatus);

                                                enroute.addView(trE, new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                                l1++;
                                            }
                                        }

                                    }else{
                                        fToday.setVisibility(View.INVISIBLE);
                                        enroute.setVisibility(View.INVISIBLE);
                                    }

                                    if(counterUpcoming != 0){
                                        dataUdate = new String[counterUpcoming];
                                        dataUdeparture = new String[counterUpcoming];
                                        dataUarrival = new String[counterUpcoming];
                                        dataUaircraft = new String[counterUpcoming];
                                        dataUstatus = new String[counterUpcoming];
                                        upcoming = findViewById(R.id.upcomingTable);
                                        fTomorrow.setVisibility(View.VISIBLE);
                                        upcoming.setVisibility(View.VISIBLE);
                                        l2 = 0;
                                        int count=upcoming.getChildCount();
                                        upcoming.removeAllViewsInLayout();
                                        addHeaderRowUpcoming();
                                        for(int j = 0; j < length; j++){
                                            String status = flight.getJSONObject(j).getString("status");
                                            if(status.equals("Scheduled")){
                                                String date = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("date");
                                                String dow = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("dow");

                                                String dtime = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("time");
                                                String dtz = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("tz");
                                                String dapn = flight.getJSONObject(j).getJSONObject("origin").getString("airport_name");
                                                String dalt_ident = flight.getJSONObject(j).getJSONObject("origin").getString("alternate_ident");

                                                String atime = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("time");
                                                String atz = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("tz");
                                                String aapn = flight.getJSONObject(j).getJSONObject("destination").getString("airport_name");
                                                String aalt_ident = flight.getJSONObject(j).getJSONObject("destination").getString("alternate_ident");

                                                trU = new TableRow(getApplicationContext());
                                                trU.setLayoutParams(new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));

                                                dataUdate[l2] = dow + " " + date;
                                                dataUdeparture[l2] = dtime + " " + dtz + "  " + dapn + " - " + dalt_ident;
                                                dataUarrival[l2] = atime + " " + atz + "  " + aapn + " - " + aalt_ident;
                                                dataUaircraft[l2] = flight.getJSONObject(j).getString("aircrafttype");
                                                dataUstatus[l2] = flight.getJSONObject(j).getString("status");

                                                txtdate = new TextView(getApplicationContext());
                                                txtdate.setText(dataUdate[l2]);
                                                txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdate.setPadding(5,5,5,5);
                                                txtdate.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
                                                trU.addView(txtdate);

                                                txtdeparture = new TextView(getApplicationContext());
                                                txtdeparture.setText(dataUdeparture[l2]);
                                                txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdeparture.setPadding(5,5,5,5);
                                                txtdeparture.setLayoutParams(new TableRow.LayoutParams(480,TableRow.LayoutParams.WRAP_CONTENT));
                                                trU.addView(txtdeparture);

                                                txtarrival = new TextView(getApplicationContext());
                                                txtarrival.setText(dataUarrival[l2]);
                                                txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtarrival.setPadding(5,5,5,5);
                                                txtarrival.setLayoutParams(new TableRow.LayoutParams(480, TableRow.LayoutParams.WRAP_CONTENT));
                                                trU.addView(txtarrival);

                                                txtaircraft = new TextView(getApplicationContext());
                                                txtaircraft.setText(dataUaircraft[l2]);
                                                txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtaircraft.setPadding(5,5,5,5);
                                                txtaircraft.setLayoutParams(new TableRow.LayoutParams(180, TableRow.LayoutParams.WRAP_CONTENT));
                                                trU.addView(txtaircraft);

                                                txtstatus = new TextView(getApplicationContext());
                                                txtstatus.setText(dataUstatus[l2]);
                                                txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtstatus.setPadding(5,5,5,5);
                                                txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
                                                trU.addView(txtstatus);

                                                upcoming.addView(trU, new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                                l2++;
                                            }
                                        }
                                    }else{
                                        fTomorrow.setVisibility(View.INVISIBLE);
                                        upcoming.setVisibility(View.INVISIBLE);
                                    }

                                    if(counterPast != 0){
                                        dataAdate = new String[counterPast];
                                        dataAdeparture = new String[counterPast];
                                        dataAarrival = new String[counterPast];
                                        dataAaircraft = new String[counterPast];
                                        dataAstatus = new String[counterPast];
                                        arrived = findViewById(R.id.arrivedTable);
                                        fYesterday.setVisibility(View.VISIBLE);
                                        arrived.setVisibility(View.VISIBLE);
                                        l3 = 0;
                                        arrived.removeAllViewsInLayout();
                                        addHeaderRowArrived();
                                        for(int j = 0; j < length; j++){
                                            String status = flight.getJSONObject(j).getString("status");
                                            if(status.equals("Arrived")){
                                                String date = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("date");
                                                String dow = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("dow");

                                                String dtime = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("time");
                                                String dtz = flight.getJSONObject(j).getJSONObject("estimated_departure_time").getString("tz");
                                                String dapn = flight.getJSONObject(j).getJSONObject("origin").getString("airport_name");
                                                String dalt_ident = flight.getJSONObject(j).getJSONObject("origin").getString("alternate_ident");

                                                String atime = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("time");
                                                String atz = flight.getJSONObject(j).getJSONObject("estimated_arrival_time").getString("tz");
                                                String aapn = flight.getJSONObject(j).getJSONObject("destination").getString("airport_name");
                                                String aalt_ident = flight.getJSONObject(j).getJSONObject("destination").getString("alternate_ident");

                                                trP = new TableRow(getApplicationContext());
                                                trP.setLayoutParams(new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));

                                                dataAdate[l3] = dow + " " + date;
                                                dataAdeparture[l3] = dtime + " " + dtz + "  " + dapn + " - " + dalt_ident;
                                                dataAarrival[l3] = atime + " " + atz + "  " + aapn + " - " + aalt_ident;
                                                dataAaircraft[l3] = flight.getJSONObject(j).getString("aircrafttype");
                                                dataAstatus[l3] = flight.getJSONObject(j).getString("status");

                                                txtdate = new TextView(getApplicationContext());
                                                txtdate.setText(dataAdate[l3]);
                                                txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdate.setPadding(5,5,5,5);
                                                txtdate.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
                                                trP.addView(txtdate);

                                                txtdeparture = new TextView(getApplicationContext());
                                                txtdeparture.setText(dataAdeparture[l3]);
                                                txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtdeparture.setPadding(5,5,5,5);
                                                txtdeparture.setLayoutParams(new TableRow.LayoutParams(480,TableRow.LayoutParams.WRAP_CONTENT));
                                                trP.addView(txtdeparture);

                                                txtarrival = new TextView(getApplicationContext());
                                                txtarrival.setText(dataAarrival[l3]);
                                                txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtarrival.setPadding(5,5,5,5);
                                                txtarrival.setLayoutParams(new TableRow.LayoutParams(480, TableRow.LayoutParams.WRAP_CONTENT));
                                                trP.addView(txtarrival);

                                                txtaircraft = new TextView(getApplicationContext());
                                                txtaircraft.setText(dataAaircraft[l3]);
                                                txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtaircraft.setPadding(5,5,5,5);
                                                txtaircraft.setLayoutParams(new TableRow.LayoutParams(180, TableRow.LayoutParams.WRAP_CONTENT));
                                                trP.addView(txtaircraft);

                                                txtstatus = new TextView(getApplicationContext());
                                                txtstatus.setText(dataAstatus[l3]);
                                                txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                                        getResources().getDimension(R.dimen.result_font));
                                                txtstatus.setPadding(5,5,5,5);
                                                txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
                                                trP.addView(txtstatus);

                                                arrived.addView(trP, new TableLayout.LayoutParams(
                                                        TableLayout.LayoutParams.MATCH_PARENT,
                                                        TableLayout.LayoutParams.WRAP_CONTENT));
                                                l3++;
                                            }
                                        }
                                    }else{
                                        fYesterday.setVisibility(View.INVISIBLE);
                                        arrived.setVisibility(View.INVISIBLE);
                                    }

                                }catch (JSONException e){
                                    AlertDialog alertDialog = new AlertDialog.Builder(FlightTrackerActivity.this).create();
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setMessage("Flight details not found. Flight ID is invalid.");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            //start code hide status bar
                                            View decorView = getWindow().getDecorView();
                                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                            decorView.setSystemUiVisibility(uiOptions);
                                            //end code hide status bar
                                        }
                                    });
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                AlertDialog alertDialog = new AlertDialog.Builder(FlightTrackerActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("Flight details not found. Flight ID is invalid.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        //start code hide status bar
                                        View decorView = getWindow().getDecorView();
                                        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                                        decorView.setSystemUiVisibility(uiOptions);
                                        //end code hide status bar
                                    }
                                });
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s", "d2granz","e899201a985c0d1f1a143a409e02bf6dcb9e5e5b");
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        return params;
                    }

                };
                queue.add(getRequest);
            }
        });
    }

    private void addHeaderRowEnroute(){

        trE = new TableRow(getApplicationContext());
        trE.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        txtdate = new TextView(getApplicationContext());
        txtdate.setText("Date");
        txtdate.setTypeface(txtdate.getTypeface(), Typeface.BOLD);
        txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdate.setPadding(5,5,5,5);
        txtdate.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trE.addView(txtdate);

        txtdeparture = new TextView(getApplicationContext());
        txtdeparture.setText("Departure");
        txtdeparture.setTypeface(txtdeparture.getTypeface(), Typeface.BOLD);
        txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdeparture.setPadding(5,5,5,5);
        txtdeparture.setLayoutParams(new TableRow.LayoutParams(310,TableRow.LayoutParams.WRAP_CONTENT));
        trE.addView(txtdeparture);

        txtarrival = new TextView(getApplicationContext());
        txtarrival.setText("Arrival");
        txtarrival.setTypeface(txtarrival.getTypeface(), Typeface.BOLD);
        txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtarrival.setPadding(5,5,5,5);
        txtarrival.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
        trE.addView(txtarrival);

        txtaircraft = new TextView(getApplicationContext());
        txtaircraft.setText("Aircraft");
        txtaircraft.setTypeface(txtaircraft.getTypeface(), Typeface.BOLD);
        txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtaircraft.setPadding(5,5,5,5);
        txtaircraft.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trE.addView(txtaircraft);

        txtstatus = new TextView(getApplicationContext());
        txtstatus.setText("Status");
        txtstatus.setTypeface(txtstatus.getTypeface(), Typeface.BOLD);
        txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtstatus.setPadding(5,5,5,5);
        txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trE.addView(txtstatus);

        enroute.addView(trE, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }
    private void addHeaderRowUpcoming(){

        trU = new TableRow(getApplicationContext());
        trU.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        txtdate = new TextView(getApplicationContext());
        txtdate.setText("Date");
        txtdate.setTypeface(txtdate.getTypeface(), Typeface.BOLD);
        txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdate.setPadding(5,5,5,5);
        txtdate.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trU.addView(txtdate);

        txtdeparture = new TextView(getApplicationContext());
        txtdeparture.setText("Departure");
        txtdeparture.setTypeface(txtdeparture.getTypeface(), Typeface.BOLD);
        txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdeparture.setPadding(5,5,5,5);
        txtdeparture.setLayoutParams(new TableRow.LayoutParams(310,TableRow.LayoutParams.WRAP_CONTENT));
        trU.addView(txtdeparture);

        txtarrival = new TextView(getApplicationContext());
        txtarrival.setText("Arrival");
        txtarrival.setTypeface(txtarrival.getTypeface(), Typeface.BOLD);
        txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtarrival.setPadding(5,5,5,5);
        txtarrival.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
        trU.addView(txtarrival);

        txtaircraft = new TextView(getApplicationContext());
        txtaircraft.setText("Aircraft");
        txtaircraft.setTypeface(txtaircraft.getTypeface(), Typeface.BOLD);
        txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtaircraft.setPadding(5,5,5,5);
        txtaircraft.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trU.addView(txtaircraft);

        txtstatus = new TextView(getApplicationContext());
        txtstatus.setText("Status");
        txtstatus.setTypeface(txtstatus.getTypeface(), Typeface.BOLD);
        txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtstatus.setPadding(5,5,5,5);
        txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trU.addView(txtstatus);

        upcoming.addView(trU, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }
    private void addHeaderRowArrived(){

        trP = new TableRow(getApplicationContext());
        trP.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        txtdate = new TextView(getApplicationContext());
        txtdate.setText("Date");
        txtdate.setTypeface(txtdate.getTypeface(), Typeface.BOLD);
        txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdate.setPadding(5,5,5,5);
        txtdate.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trP.addView(txtdate);

        txtdeparture = new TextView(getApplicationContext());
        txtdeparture.setText("Departure");
        txtdeparture.setTypeface(txtdeparture.getTypeface(), Typeface.BOLD);
        txtdeparture.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtdeparture.setPadding(5,5,5,5);
        txtdeparture.setLayoutParams(new TableRow.LayoutParams(310,TableRow.LayoutParams.WRAP_CONTENT));
        trP.addView(txtdeparture);

        txtarrival = new TextView(getApplicationContext());
        txtarrival.setText("Arrival");
        txtarrival.setTypeface(txtarrival.getTypeface(), Typeface.BOLD);
        txtarrival.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtarrival.setPadding(5,5,5,5);
        txtarrival.setLayoutParams(new TableRow.LayoutParams(310, TableRow.LayoutParams.WRAP_CONTENT));
        trP.addView(txtarrival);

        txtaircraft = new TextView(getApplicationContext());
        txtaircraft.setText("Aircraft");
        txtaircraft.setTypeface(txtaircraft.getTypeface(), Typeface.BOLD);
        txtaircraft.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtaircraft.setPadding(5,5,5,5);
        txtaircraft.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trP.addView(txtaircraft);

        txtstatus = new TextView(getApplicationContext());
        txtstatus.setText("Status");
        txtstatus.setTypeface(txtstatus.getTypeface(), Typeface.BOLD);
        txtstatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.header_font));
        txtstatus.setPadding(5,5,5,5);
        txtstatus.setLayoutParams(new TableRow.LayoutParams(200, TableRow.LayoutParams.WRAP_CONTENT));
        trP.addView(txtstatus);

        arrived.addView(trP, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
    }

    private void focusButton(){
        HomeButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.focusIcons(view, b, getApplicationContext(), FlightTrackerActivity.this);
            }
        });
        BackButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.focusIcons(view, b, getApplicationContext(), FlightTrackerActivity.this);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),FlightTrackerActivity.this);
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                    if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                        t.interrupt();
                    }
                }
                Intent i = new Intent(FlightTrackerActivity.this, LauncherActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                startActivity(i);
                i = new Intent("finish_hotelservices");
                sendBroadcast(i);
                finish();
            }
        });
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void setNotif(){
        //if(chatNotiff.getVisibility() == View.VISIBLE) {
        thread_url = ApiUrl + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
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
        //}
    }
}
