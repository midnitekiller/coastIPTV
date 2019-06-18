package com.direct2guests.d2g_tv.Activities.ChatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.direct2guests.d2g_tv.Activities.AnalyticsApplication;
import com.direct2guests.d2g_tv.Activities.HotelServicesActivity;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;
import static java.lang.Integer.parseInt;

public class FrontDeskChatActivity extends Activity {
    public final static String CHAT_FROM = "com.direct2guests.d2g_tv.CHAT_FROM";
    NetworkConnection nc = new NetworkConnection();
    private Variable vdata;
    private Button sendChatButton;
    private EditText chatEdit;
    private ListView chatMessages;
    private String[] messages, from;
    private ArrayList<ChatItem> items;
    private ChatCustomAdapter chatCustomAdapter;
    private int lastCount;
    private Thread t;
    private String thread_url, chatFrom;
    private String[] msgs, frm;
    private JSONArray array;
    private Ringtone r;
    private Uri notification;
    private JsonArrayRequest jsonRequest;
    private JSONObject chatobj;
    private RequestQueue queue;
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
       /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.d("Error", "UncaughtException!!!");
                System.exit(2);
            }
        });*/
        Bundle configBundle = new Bundle();
        try {
            setContentView(R.layout.activity_front_desk_chat);
        }catch (RuntimeException e){
            onCreate(configBundle);
        }
        AnalyticsApplication application = (AnalyticsApplication) getApplicationContext();
        mTracker = application.getDefaultTracker();
        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        chatFrom = getIntent().getStringExtra(FrontDeskChatActivity.CHAT_FROM);
        sendChatButton = findViewById(R.id.sendChatButton);
        chatMessages = findViewById(R.id.ChatBoxList);
        chatEdit = findViewById(R.id.chatTextView);
        queue = Volley.newRequestQueue(this);
        sendChatButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    view.setBackgroundTintList(ContextCompat.getColorStateList(FrontDeskChatActivity.this, R.color.sendchat_focus));
                }else{
                    view.setBackgroundTintList(ContextCompat.getColorStateList(FrontDeskChatActivity.this, R.color.sendchat_blur));
                }
            }
        });
        thread_url = vdata.getApiUrl() + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();

        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                jsonRequest = new JsonArrayRequest(Request.Method.GET, thread_url, null,
                                    new Response.Listener<JSONArray>()
                                    {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                array = response;
                                                if(array.length() > 0) {
                                                    msgs = new String[array.length()];
                                                    frm = new String[array.length()];
                                                    int resCount = response.length();
                                                    if (array != null) {
                                                        for (int i = 0; i < array.length(); i++) {
                                                            chatobj = array.getJSONObject(i);
                                                            msgs[i] = chatobj.getString("msg");
                                                            frm[i] = chatobj.getString("msg_from");

                                                        }
                                                        Log.d("Last Message", "::  " + msgs[resCount - 1] + ":: Rescount:: " + resCount);
                                                    }
                                                    if (lastCount < resCount && frm[resCount - 1].equals("admin")) {
                                                        items.add(new ChatItem(msgs[resCount - 1], ChatItem.ChatType.ADMIN));
                                                        chatCustomAdapter.notifyDataSetChanged();
                                                        chatMessages.setSelection(chatCustomAdapter.getCount() - 1);
                                                        lastCount = resCount;
                                                        try {
                                                            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                                            r.play();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        updateChatStatus();
                                                    }
                                                }

                                            }catch (JSONException e){
                                                Log.d("JSONException", "Unable to parse JSONArray");
                                            }
                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("Error.Response", error.getLocalizedMessage());
                                        }
                                    }
                            ){
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
        chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEND){
                    sendChatByKeyboard();
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        loadChatMessages();

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadChatMessages();
    }


    public void loadChatMessages(){
        updateChatStatus();
        String url = vdata.getApiUrl() + "chats.php?hotel_id=" + vdata.getHotelID() + "&guest_id=" + vdata.getGuestID();
        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                try {

                    items = new ArrayList<ChatItem>();
                    if(response.length() > 0) {
                        messages = new String[response.length()];
                        from = new String[response.length()];
                        lastCount = response.length();
                        if (response != null) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject chatobj = response.getJSONObject(i);
                                Log.d("JSONObject", "::  " + chatobj.toString());
                                messages[i] = chatobj.getString("msg");
                                from[i] = chatobj.getString("msg_from");
                            }
                        }
                        if (from != null && messages != null) {
                            for (int i = 0; i < from.length; i++) {
                                if (from[i].equals("guest")) {
                                    items.add(new ChatItem(messages[i], ChatItem.ChatType.GUEST));
                                } else if (from[i].equals("admin")) {
                                    items.add(new ChatItem(messages[i], ChatItem.ChatType.ADMIN));
                                } else {
                                    //nothing
                                }
                            }
                        }

                    }
                    chatCustomAdapter = new ChatCustomAdapter(getApplicationContext(), items);
                    chatMessages.setAdapter(chatCustomAdapter);
                    chatMessages.setSelection(chatCustomAdapter.getCount() - 1);

                }catch (JSONException e){
                    Log.d("JSONException", "Unable to parse JSONArray");
                }
            }
            @Override
            public void onError(VolleyError error) {
                Log.d("Downloading Chat", "Fail");
            }
        });


    }

    public void sendChat(View view){
        if (("".equals(chatEdit.getText().toString().trim()))){
            Toast.makeText(this, "The Field is empty.", Toast.LENGTH_LONG).show();
            return;
        }
        else {

            String url = vdata.getApiUrl() + "sendchat.php";
            JSONObject data = new JSONObject();
            try {
                data.put("msg", chatEdit.getText().toString());
                data.put("hotel_id", vdata.getHotelID());
                data.put("guest_id", vdata.getGuestID());
            } catch (JSONException e) {
                Log.d("JSON DataPOSTException", e.getLocalizedMessage());
            }

            nc.postdataObject(url, getApplicationContext(), data, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Successfully sent the message.", Toast.LENGTH_LONG).show();
                    Log.d("POST Success", response.toString());
                    items.add(new ChatItem(chatEdit.getText().toString(), ChatItem.ChatType.GUEST));
                    chatCustomAdapter.notifyDataSetChanged();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    chatEdit.setText("");
                    chatMessages.setSelection(chatCustomAdapter.getCount() - 1);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d("POST Fail", error.getLocalizedMessage());
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t.interrupt();
        /*if (chatFrom != null) {
            if (chatFrom.equals("hotelservices")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("places")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("placesdetail")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("services")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("offers")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("offersdetail")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            } else if (chatFrom.equals("fooddrinks")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            }else if (chatFrom.equals("hotelservicesbutt")) {
                Intent i = new Intent(this, HotelServicesActivity.class);
                i.putExtra(Variable.EXTRA, vdata);
                i.putExtra(FrontDeskChatActivity.CHAT_FROM, chatFrom);
                startActivity(i);
            }
        }*/

    }

    private void sendChatByKeyboard(){
        if (("".equals(chatEdit.getText().toString().trim()))){
            Toast.makeText(this, "The Field is empty.", Toast.LENGTH_LONG).show();
            return;
        }
        else {

            String url = vdata.getApiUrl() + "sendchat.php";
            JSONObject data = new JSONObject();
            try {
                data.put("msg", chatEdit.getText().toString());
                data.put("hotel_id", vdata.getHotelID());
                data.put("guest_id", vdata.getGuestID());
            } catch (JSONException e) {
                Log.d("JSON DataPOSTException", e.getLocalizedMessage());
            }

            nc.postdataObject(url, getApplicationContext(), data, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Successfully sent the message.", Toast.LENGTH_LONG).show();
                    Log.d("POST Success", response.toString());
                    items.add(new ChatItem(chatEdit.getText().toString(), ChatItem.ChatType.GUEST));
                    chatCustomAdapter.notifyDataSetChanged();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    chatEdit.setText("");
                    chatMessages.setSelection(chatCustomAdapter.getCount() - 1);
                }

                @Override
                public void onError(VolleyError error) {
                    Log.d("POST Fail", error.getLocalizedMessage());
                }
            });
        }
    }

    private void updateChatStatus(){
        String url_status = vdata.getApiUrl() + "chatstatus.php";
        JSONObject data = new JSONObject();
        try {
            data.put("hotel_id", vdata.getHotelID());
            data.put("guest_id", vdata.getGuestID());
        } catch (JSONException e) {
            Log.d("JSON DataPOSTException", e.getLocalizedMessage());
        }
        nc.postdataObject(url_status, getApplicationContext(), data, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("Chat Status :", "UPDATED");
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("Chat Status :", "FAIL");
                error.printStackTrace();
            }
        });

    }
}
