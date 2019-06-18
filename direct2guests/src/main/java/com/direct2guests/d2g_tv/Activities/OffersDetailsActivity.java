package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.direct2guests.d2g_tv.NonActivity.Constant;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static java.lang.Integer.parseInt;

public class OffersDetailsActivity extends LangSelectActivity {
    private Variable vdata;
    private NetworkConnection nc;
    private String offerID, offerName, offerImage, chatFrom;
    private LayoutInflater inflater;
    private View childHolder, childInner;
    private LinearLayout offerHolder;
    private LinearLayout[] childOfferHolder;

    private RelativeLayout[] cardOff;
    private TextView title, desc, promoprice, origprice, offerTitle;
    private ImageView image;
    private Button HomeButton, BackButton;

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
        setContentView(R.layout.activity_offers_details);


        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "OffersDetailsActivity");
        params.putString("app_event", "Viewing Offers");
        mFirebaseAnalytics.logEvent("app_activity", params);

        nc = new NetworkConnection();
        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        chatFrom = getIntent().getStringExtra(FrontDeskChatActivity.CHAT_FROM);
        offerID = getIntent().getStringExtra(OffersActivity.OFFERS_ID);
        offerName = getIntent().getStringExtra(OffersActivity.OFFERS_NAME);
        offerImage = getIntent().getStringExtra(OffersActivity.OFFERS_IMAGE);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        offerHolder = findViewById(R.id.outerCardHolder);
        offerTitle = findViewById(R.id.OfferTitle);

        HomeButton = findViewById(R.id.HomeBtnOffer);
        BackButton = findViewById(R.id.BackBtnOffer);

        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        notif_button = findViewById(R.id.chat_message);
        notif_number = findViewById(R.id.new_message);
        queue = Volley.newRequestQueue(this);

        chatNotiff = findViewById(R.id.notif);
        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);
        offerTitle = findViewById(R.id.OfferTitle);
        offerTitle.setTypeface(fontBold);

        setFocusClick();
    }
    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        broadcast_reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_offerdetails")) {
                    //finishing the activity
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish_offerdetails"));

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
        getOffers();
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
                Intent i = new Intent(OffersDetailsActivity.this, FrontDeskChatActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, "hotelservicesbutt");
                startActivity(i);
            }
        });
        notif_button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                vdata.focusIcons(view,hasFocus,getApplicationContext(),OffersDetailsActivity.this);
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


    private void getOffers(){
        String url = vdata.getApiUrl() + "offerdetails.php?offer_id=" + offerID + "&hotel_id=" + vdata.getHotelID();
        Log.d("URL", url);
        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                try{
                    if(response.length()!= 0) {
                        JSONArray results = response;
                        int countOffer = 0;
                        int z = 0;
                        int y = 1;
                        int v = 3;
                        int b = 1;
                        int w = 0;
                        String[] titleoff,imageoff,descoff,promopriceoff,origpriceoff;

                        titleoff = new String[response.length()];
                        imageoff = new String[response.length()];
                        descoff = new String[response.length()];
                        promopriceoff = new String[response.length()];
                        origpriceoff = new String[response.length()];
                        for(int l = 0; l < results.length(); l++){
                                imageoff[l] = results.getJSONObject(l).getString("img_path");
                                titleoff[l] = results.getJSONObject(l).getString("offerdetail_name");
                                descoff[l] = results.getJSONObject(l).getString("offer_description");
                                promopriceoff[l] = results.getJSONObject(l).getString("selling_price");
                                origpriceoff[l] = results.getJSONObject(l).getString("original_price");
                        }
                        if(response.length() != 0) {
                            if ((response.length() % 3) != 0) {
                                z = (response.length() / 3) + 1;
                            } else {
                                z = response.length() / 3;
                            }
                            childOfferHolder = new LinearLayout[z];
                            for (int j = 0; j < z; j++) {
                                childHolder = inflater.inflate(R.layout.offer_card_holder, offerHolder, false);
                                childOfferHolder[j] = childHolder.findViewById(R.id.cardHolder);
                                offerHolder.addView(childHolder);
                                int c = j + 1;
                                for(int k = y*b; k <= v*c; k++){
                                    try{
                                        String titlestr = titleoff[k-1];
                                        String descstr = descoff[k-1];
                                        String promopricestr = promopriceoff[k-1];
                                        String origpricestr = origpriceoff[k-1];
                                        String imgstr = imageoff[k-1];
                                        childInner = inflater.inflate(R.layout.offer_card, childOfferHolder[j], false);

                                        // Loading Font Face
                                        Typeface fontBold = Typeface.createFromAsset(getAssets(), fontPathBold);
                                        Typeface fontReg = Typeface.createFromAsset(getAssets(), fontPathReg);

                                        image = childInner.findViewById(R.id.offImage);
                                        title = childInner.findViewById(R.id.offTitle);
                                        desc = childInner.findViewById(R.id.offDesc);
                                        promoprice = childInner.findViewById(R.id.offPromoPrice);
                                        origprice = childInner.findViewById(R.id.offOldPrice);

                                        //Applying font
                                        title.setTypeface(fontBold);
                                        desc.setTypeface(fontReg);
                                        promoprice.setTypeface(fontReg);
                                        origprice.setTypeface(fontReg);

                                        title.setText(titlestr);
                                        desc.setText(descstr);
                                        promoprice.setText("Promo Price: P " + promopricestr);
                                        origprice.setText("Price: P " + origpricestr);
                                        origprice.setPaintFlags(origprice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                                        childOfferHolder[j].addView(childInner);
//                                        Picasso.with(getApplicationContext()).load(vdata.getServerURL() + imgstr).resize(140, 140).into(image);
                                        Picasso.with(getApplicationContext()).load(vdata.getServerURL() + imgstr).fit().into(image);
                                    }catch (ArrayIndexOutOfBoundsException e){
                                        //do nothing
                                        Log.d("ArrayIndextOB", "fail");
                                    }
                                }
                                b = b + 3;
                            }
                        }else{
                            Log.d("ZERO", "");
                        }
                    }else{
                        Log.d("ZERROOO","");
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("volley error", error.getLocalizedMessage());
            }
        });

    }

    private void setFocusClick(){
        HomeButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, OffersDetailsActivity.this);
            }
        });
        BackButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                vdata.ButtonFocus(view, b, OffersDetailsActivity.this);
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
                Intent i = new Intent(OffersDetailsActivity.this, LauncherActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                startActivity(i);
                i = new Intent("finish_offers");
                sendBroadcast(i);
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
