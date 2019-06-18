package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static java.lang.Integer.parseInt;

public class PlacesNearbyDetailsActivity extends LangSelectActivity {
    private NetworkConnection nc = new NetworkConnection();
    private Variable vdata;
    private String placesType, pt, chatFrom;
    private TextView placesDetailTitle, adTitle, adDescription, adAddress, adContactNo;
    private Button HomeButtonDetail, BackButtonDetail;
    private LinearLayout detailsCard;
    private int i, imCount;
    private String[] adTitleX, adDescriptionX, adAddressX, adContactX, adImg1X, adImg2X, adImg3X, adImg4X, adImg5X;
    private FrameLayout[] detailFrameRestaurant, detailFrameActivity, detailFramePub;
    private View child;

    private Button bttnImage, bttnVideo, bttnMap;
    private int HKFocus = 0;


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
    private ImageView mImageBG, mImage;
    private TextView mTextGB;
    // Font path
    String fontPathBold = "fonts/brandonbold.ttf";
    String fontPathReg = "fonts/brandonreg.ttf";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);




        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_places_nearby_details);

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "PlacesNearbyDetailsActivity");
        params.putString("app_event", "Viewing Places Nearby Details Page");
        mFirebaseAnalytics.logEvent("app_activity", params);

        mImageBG = findViewById(R.id.mImageBG);
        mImage = findViewById(R.id.mImage);


        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        chatFrom = getIntent().getStringExtra(FrontDeskChatActivity.CHAT_FROM);
        placesType = getIntent().getStringExtra(PlacesNearbyActivity.PLACES_TYPE);

        HomeButtonDetail = findViewById(R.id.HomeButtonDetails);
        BackButtonDetail = findViewById(R.id.BackButtonDetails);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        // Loading Font Face
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);

        //TextView label
        adTitle = findViewById(R.id.adTitle);
        adDescription = findViewById(R.id.adDescription);
        adAddress = findViewById(R.id.adAddress);
        adContactNo = findViewById(R.id.adAddress);
        placesDetailTitle = findViewById(R.id.PlacesDetailTitle);

//        adTitle.setTypeface(fontBold);
//        adTitle.setAllCaps(true);



        chatNotiff = findViewById(R.id.notif);
        //Applying font
        placesDetailTitle.setTypeface(fontBold);

        placesDetailTitle.setText(placesType);
        if(placesType.equals("Activities")){
            pt = "activity";
        }else if(placesType.equals("Nightlife")){
            pt = "pub";
        }else if(placesType.equals("Restaurants")){
            pt = "restaurant";
        }

        detailsCard = findViewById(R.id.DetailsCards);


        bttnImage = findViewById(R.id.bttnImage);
        bttnVideo = findViewById(R.id.bttnVideo);
        bttnMap = findViewById(R.id.bttnMap);



        onFocusIcon();
        loadAds();
    }

    private void onFocusIcon(){
        HomeButtonDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
            }
        });

        BackButtonDetail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
            }
        });

        HomeButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                    if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                        t.interrupt();
                    }
                }
                Intent i = new Intent(PlacesNearbyDetailsActivity.this, LauncherActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                startActivity(i);
                i = new Intent("finish_placesnearby");
                sendBroadcast(i);
                i = new Intent("finish_hotelservices");
                sendBroadcast(i);
                finish();
            }
        });
        BackButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar


//        broadcast_reciever = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent intent) {
//                String action = intent.getAction();
//                if (action.equals("finish_placesnearbydetails")) {
//                    //finishing the activity
//                    finish();
//                }
//            }
//        };
//        registerReceiver(broadcast_reciever, new IntentFilter("finish_placesnearbydetails"));
//
//

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
                Intent i = new Intent(PlacesNearbyDetailsActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
            }
        });

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
                chatNotiff.setVisibility(View.VISIBLE);
            }
        }

    }
    @Override
    public void onBackPressed(){
        /*if(placesType.equals("Activities")){
            Intent i = new Intent(this, PlacesNearbyActivity.class);
            i.putExtra(Variable.EXTRA, vdata);
            i.putExtra(PlacesNearbyActivity.PLACES_TYPE, "activity");
            startActivity(i);
        }else if(placesType.equals("Pubs")){
            Intent i = new Intent(this, PlacesNearbyActivity.class);
            i.putExtra(Variable.EXTRA, vdata);
            i.putExtra(PlacesNearbyActivity.PLACES_TYPE, "pubs");
            startActivity(i);
        }else if(placesType.equals("Restaurants")){
            Intent i = new Intent(this, PlacesNearbyActivity.class);
            i.putExtra(Variable.EXTRA, vdata);
            i.putExtra(PlacesNearbyActivity.PLACES_TYPE, "restaurant");
            startActivity(i);
        }*/
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {

                t.interrupt();
            }
        }
        super.onBackPressed();

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



    private void loadAds(){
        if (pt.equals("restaurant")) {
            adTitleX = new String[vdata.getAdsRestaurantTitle().length];
            adDescriptionX = new String[vdata.getAdsRestaurantDescription().length];
            adAddressX = new String[vdata.getAdsRestaurantAddress().length];
            adContactX = new String[vdata.getAdsRestaurantContact().length];
            adImg1X = new String[vdata.getAdsRestaurantImage1().length];
            adImg2X = new String[vdata.getAdsRestaurantImage2().length];
            adImg3X = new String[vdata.getAdsRestaurantImage3().length];
            adImg4X = new String[vdata.getAdsRestaurantImage4().length];
            adImg5X = new String[vdata.getAdsRestaurantImage5().length];
            ViewGroup.MarginLayoutParams params;
            detailFrameRestaurant = new FrameLayout[vdata.getAdsRestaurantTitle().length];
            for (i = 0; i < vdata.getAdsRestaurantTitle().length; i++) {
                adTitleX[i] = vdata.getAdsRestaurantTitle()[i];
                adDescriptionX[i] = vdata.getAdsRestaurantDescription()[i];
                adAddressX[i] = vdata.getAdsRestaurantAddress()[i];
                adContactX[i] = vdata.getAdsRestaurantContact()[i];
                adImg1X[i] = vdata.getAdsRestaurantImage1()[i];
                adImg2X[i] = vdata.getAdsRestaurantImage2()[i];
                adImg3X[i] = vdata.getAdsRestaurantImage3()[i];
                adImg4X[i] = vdata.getAdsRestaurantImage4()[i];
                adImg5X[i] = vdata.getAdsRestaurantImage5()[i];
                Log.d("Image", adImg1X[i]);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                child = inflater.inflate(R.layout.places_detail_card, detailsCard, false);
                detailFrameRestaurant[i] = child.findViewById(R.id.detailCard);


                final ImageView cardImage = child.findViewById(R.id.detailImage);
                int counts = i;
                String imgHand0 = adImg1X[counts];


                Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
                TextView adTitleHandler = child.findViewById(R.id.detailText);
                adTitleHandler.setAllCaps(true);
                adTitleHandler.setTypeface(fontBold);

                // Analytics
                Bundle paramsPlaces = new Bundle();
                paramsPlaces.putString("app_page", "PlacesNearbyDetailsActivity");
                paramsPlaces.putString("app_event", "Viewing Places Nearby: " + adTitleHandler);
                mFirebaseAnalytics.logEvent("app_activity", paramsPlaces);


                detailFrameRestaurant[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        vdata.focusAnim(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
                        Picasso.with(getApplicationContext()).load(imgHand0).fit().into(mImageBG);
                        if (hasFocus) {


                                adTitleHandler.setTextColor(Color.parseColor("#ffffff"));




                        } else {
                            adTitleHandler.setTextColor(Color.parseColor("#878782"));

                        }
                    }
                });



                TextView detailText = child.findViewById(R.id.detailText);
                detailText.setText(adTitleX[i]);
                detailText.setTypeface(fontBold);
                detailText.setAllCaps(true);
                detailsCard.addView(child);

            }
        } else if (pt.equals("activity")) {

            adTitleX = new String[vdata.getAdsActivitiesTitle().length];
            adDescriptionX = new String[vdata.getAdsActivitiesDescription().length];
            adAddressX = new String[vdata.getAdsActivitiesAddress().length];
            adContactX = new String[vdata.getAdsActivitiesContact().length];
            adImg1X = new String[vdata.getAdsActivitiesImage1().length];
            adImg2X = new String[vdata.getAdsActivitiesImage2().length];
            adImg3X = new String[vdata.getAdsActivitiesImage3().length];
            adImg4X = new String[vdata.getAdsActivitiesImage4().length];
            adImg5X = new String[vdata.getAdsActivitiesImage5().length];
            ViewGroup.MarginLayoutParams params;
            detailFrameActivity = new FrameLayout[vdata.getAdsActivitiesTitle().length];
            for (i = 0; i < vdata.getAdsActivitiesTitle().length; i++) {
                adTitleX[i] = vdata.getAdsActivitiesTitle()[i];
                adDescriptionX[i] = vdata.getAdsActivitiesDescription()[i];
                adAddressX[i] = vdata.getAdsActivitiesAddress()[i];
                adContactX[i] = vdata.getAdsActivitiesContact()[i];
                adImg1X[i] = vdata.getAdsActivitiesImage1()[i];
                adImg2X[i] = vdata.getAdsActivitiesImage2()[i];
                adImg3X[i] = vdata.getAdsActivitiesImage3()[i];
                adImg4X[i] = vdata.getAdsActivitiesImage4()[i];
                adImg5X[i] = vdata.getAdsActivitiesImage5()[i];
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                child = inflater.inflate(R.layout.places_detail_card, detailsCard, false);
                detailFrameActivity[i] = child.findViewById(R.id.detailCard);

                final ImageView cardImage = child.findViewById(R.id.detailImage);
                int counts = i;
                String imgHand1 = adImg1X[counts];


                Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
                TextView adTitleHandler = child.findViewById(R.id.detailText);
                adTitleHandler.setAllCaps(true);

                adTitleHandler.setTypeface(fontBold);


                // Analytics
                Bundle paramsPlaces = new Bundle();
                paramsPlaces.putString("app_page", "PlacesNearbyDetailsActivity");
                paramsPlaces.putString("app_event", "Viewing Places Nearby: " + adTitleHandler);
                mFirebaseAnalytics.logEvent("app_activity", paramsPlaces);


                detailFrameActivity[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        vdata.focusAnim(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
                        Picasso.with(getApplicationContext()).load(imgHand1).fit().into(mImageBG);
                        if (hasFocus) {


                            adTitleHandler.setTextColor(Color.parseColor("#ffffff"));



                        } else {

                            adTitleHandler.setTextColor(Color.parseColor("#878782"));
//                            Picasso.with(getApplicationContext()).load(adImg1X[1]).fit().into(mImageBG);
                        }
                    }
                });


                TextView detailText = child.findViewById(R.id.detailText);
                detailText.setText(adTitleX[i]);
                detailText.setTypeface(fontBold);
                detailText.setAllCaps(true);
                detailsCard.addView(child);


            }
        } else if(pt.equals("pub")){

            adTitleX = new String[vdata.getAdsPubsTitle().length];
            adDescriptionX = new String[vdata.getAdsPubsDescription().length];
            adAddressX = new String[vdata.getAdsPubsAddress().length];
            adContactX = new String[vdata.getAdsPubsContact().length];
            adImg1X = new String[vdata.getAdsPubsImage1().length];
            adImg2X = new String[vdata.getAdsPubsImage2().length];
            adImg3X = new String[vdata.getAdsPubsImage3().length];
            adImg4X = new String[vdata.getAdsPubsImage4().length];
            adImg5X = new String[vdata.getAdsPubsImage5().length];
            ViewGroup.MarginLayoutParams params;
            detailFramePub = new FrameLayout[vdata.getAdsPubsTitle().length];
            for(i = 0; i < vdata.getAdsPubsTitle().length; i++){
                adTitleX[i] = vdata.getAdsPubsTitle()[i];
                adDescriptionX[i] = vdata.getAdsPubsDescription()[i];
                adAddressX[i] = vdata.getAdsPubsAddress()[i];
                adContactX[i] = vdata.getAdsPubsContact()[i];
                adImg1X[i] = vdata.getAdsPubsImage1()[i];
                adImg2X[i] = vdata.getAdsPubsImage2()[i];
                adImg3X[i] = vdata.getAdsPubsImage3()[i];
                adImg4X[i] = vdata.getAdsPubsImage4()[i];
                adImg5X[i] = vdata.getAdsPubsImage5()[i];

                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                child = inflater.inflate(R.layout.places_detail_card, detailsCard, false);
                detailFramePub[i] = child.findViewById(R.id.detailCard);

                final ImageView cardImage = child.findViewById(R.id.detailImage);
                int counts = i;
                String imgHand2 = adImg1X[counts];






                Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
                TextView adTitleHandler = child.findViewById(R.id.detailText);
                adTitleHandler.setAllCaps(true);
                adTitleHandler.setTypeface(fontBold);

                // Analytics
                Bundle paramsPlaces = new Bundle();
                paramsPlaces.putString("app_page", "PlacesNearbyDetailsActivity");
                paramsPlaces.putString("app_event", "Viewing Places Nearby: " + adTitleHandler);
                mFirebaseAnalytics.logEvent("app_activity", paramsPlaces);



//                Picasso.with(getApplicationContext()).load(adImg1X[i]).fit().into(cardImage);
//                cardImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.adcleartint), PorterDuff.Mode.MULTIPLY);
                detailFramePub[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {

                        vdata.focusAnim(view,hasFocus,getApplicationContext(),PlacesNearbyDetailsActivity.this);
                        Picasso.with(getApplicationContext()).load(imgHand2).fit().into(mImageBG);
                        if(hasFocus){

                                adTitleHandler.setTextColor(Color.parseColor("#ffffff"));


                        } else{
//                            cardImage.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.adcleartint), PorterDuff.Mode.MULTIPLY);
                            adTitleHandler.setTextColor(Color.parseColor("#878782"));


                        }
                    }
                });


                TextView detailText = child.findViewById(R.id.detailText);
                detailText.setText(adTitleX[i]);
                detailText.setTypeface(fontBold);
                detailText.setAllCaps(true);
                detailsCard.addView(child);


            }
        }



    }


    private void setNotif(){
        //if(chatNotiff.getVisibility() == View.VISIBLE) {
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
        //}
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
