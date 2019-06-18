package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ImgPath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static java.lang.Integer.parseInt;

public class OffersActivity extends LangSelectActivity {
    private Variable vdata;
    private NetworkConnection nc = new NetworkConnection();
    public final static String OFFER_TYPE = "com.direct2guests.d2g_tv.OFFER_TYPE";
    public final static String OFFERS_ID = "com.direct2guests.d2g_tv.OFFERS_ID";
    public final static String OFFERS_NAME = "com.direct2guests.d2g_tv.OFFERS_NAME";
    public final static String OFFERS_IMAGE = "com.direct2guests.d2g_tv.OFFERS_IMAGE";

    private Button HomeButton, BackButton;
    private ImageView hotelLogo;
    private String chatFrom;

    private View child;
    private Intent i;
    private FrameLayout[] OffersCard;
    private LinearLayout OffersCardsParent;
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

    // Font path
    String fontPathBold = "fonts/brandonbold.ttf";
    String fontPathReg = "fonts/brandonreg.ttf";

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_offers);
        AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
//        mTracker = application.getDefaultTracker();
        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        chatFrom = getIntent().getStringExtra(FrontDeskChatActivity.CHAT_FROM);
        HomeButton = findViewById(R.id.homeButtonOffers);
        BackButton = findViewById(R.id.backButtonOffers);
        hotelLogo = findViewById(R.id.hotelLogo);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);
        OffersCardsParent = findViewById(R.id.offerList);
        chatNotiff = findViewById(R.id.notif);
        // Loading Font Face

        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);


        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "OffersActivity");
        params.putString("app_event", "Viewing Offers Page");
        mFirebaseAnalytics.logEvent("app_activity", params);


//        Picasso.with(getApplicationContext()).load(vdata.getServerURL()+vdata.getHotelLogo()).into(hotelLogo);
        onFocusClick();
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
//                if (action.equals("finish_offers")) {
//                    //finishing the activity
//                    finish();
//                }
//            }
//        };
//        registerReceiver(broadcast_reciever, new IntentFilter("finish_offers"));






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
        OffersCardsParent.removeAllViews();
        loadOffers();
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
                Intent i = new Intent(OffersActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),OffersActivity.this);
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
        OffersCardsParent.removeAllViews();
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                setNotif();
                chatNotiff.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onBackPressed(){
        /*Intent i = new Intent(this, HotelOffersActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(HotelOffersActivity.CLICK_FROM, "offers");
        startActivity(i);*/
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


    private void onFocusClick(){
        //focus
        HomeButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, OffersActivity.this);
            }
        });
        BackButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, OffersActivity.this);
            }
        });

        //click
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < vdata.getHotelAccess().length; i++) {
                    if (vdata.getHotelAccess()[i].equals("chat_acc")) {
                        t.interrupt();
                    }
                }
                Intent i = new Intent(OffersActivity.this, MainActivity.class);
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

    private void loadOffers(){
        String url = vdata.getApiUrl() + "offers.php?hotel_id=" + vdata.getHotelID();
        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(final JSONArray response) {

                Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
                Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);

                try {
                    OffersCard = new FrameLayout[response.length()];
                    ViewGroup.MarginLayoutParams params;


                    for (int i = 0; i < response.length(); i++) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        child = inflater.inflate(R.layout.food_drinks_restaurant_card, OffersCardsParent, false);
                        OffersCard[i] = child.findViewById(R.id.restaurantCard);


                        String image_url2 = vdata.getServerURL() + response.getJSONObject(i).getString("img_path");
                        TextView offerText = child.findViewById(R.id.restoName);
                        offerText.setTypeface(fontBold);



                        OffersCard[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                vdata.focusAnim(view, b, getApplicationContext(), OffersActivity.this);

                                ImageView resImage2 = findViewById(R.id.offersBG);
                                Picasso.with(getApplicationContext()).load(image_url2).fit().into(resImage2);


                                if (b) {
                                    offerText.setTextColor(Color.parseColor("#ffffff"));
                                } else {
                                    offerText.setTextColor(Color.parseColor("#878782"));
                                }



                            }
                        });
                        if (i == 0) {
                            params = (ViewGroup.MarginLayoutParams) OffersCard[i].getLayoutParams();
//                            params.setMargins(110, 0, 0, 0);
                            OffersCard[i].setLayoutParams(params);
                            OffersCard[i].requestFocus();
                        } else if (i == response.length() - 1) {
                            params = (ViewGroup.MarginLayoutParams) OffersCard[i].getLayoutParams();
//                            params.setMargins(0, 0, 110, 0);
                            OffersCard[i].setLayoutParams(params);
                        }

                        ImageView serImage = child.findViewById(R.id.restoImage);
                        TextView serName = child.findViewById(R.id.restoName);
                        if (response.getJSONObject(i).getString("img_path").equals("")) {
                            Picasso.with(getApplicationContext()).load(R.drawable.menus).fit().into(serImage);
                        } else {
                            String image_url = vdata.getServerURL() + response.getJSONObject(i).getString("img_path");
                            Picasso.with(getApplicationContext()).load(image_url).fit().into(serImage);
                        }
                        serName.setText(response.getJSONObject(i).getString("offer_name"));
                        OffersCard[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                final int index = Arrays.asList(OffersCard).indexOf(view);
//                                try {
//                                    cardClick(response.getJSONObject(index).getString("offer_ID").toString(), response.getJSONObject(index).getString("offer_name"), response.getJSONObject(index).getString("img_path"));
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//                                }
                            }
                        });
                        OffersCardsParent.addView(child);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void cardClick(String offerid, String offername, String offerimage){
        for (int i = 0; i < vdata.getHotelAccess().length; i++) {
            if (vdata.getHotelAccess()[i].equals("chat_acc")) {

                t.interrupt();
            }
        }
        OffersCardsParent.removeAllViewsInLayout();
        i = new Intent(this, OffersDetailsActivity.class);
        i.putExtra(Variable.EXTRA, vdata);
        i.putExtra(OFFERS_ID, offerid);
        i.putExtra(OFFERS_NAME, offername);
        i.putExtra(OFFERS_IMAGE, offerimage);
        startActivity(i);
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
