package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.direct2guests.d2g_tv.NonActivity.ChannelListAdapter;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.RtmpDataSource;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallback;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiBasePath;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ApiUrl;
import static com.direct2guests.d2g_tv.NonActivity.Constant.ServerUrl;

public class ChannelListActivity extends Activity {
    private Variable vdata;
    private NetworkConnection nc = new NetworkConnection();
    private String[]  channelTitle, channelURL, channelType;
    private ListView channel_listview;
    TextView guestName, chanText, debugtxt;
    private String ChanPath;
    private TextView ChanTitle;


    private VideoView RTSPPlayer;
    private MediaController controller;
    private ProgressBar loadingS;
    private MediaPlayer.OnInfoListener onInfoToPlayStateListener;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView simpleExoPlayer;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private DefaultRenderersFactory defaultRenderersFactory;
    private TrackSelector trackSelector;
    private MediaSource videoSource;
    private Uri mp4VideoUri;


    private ImageView arrowUp, arrowDown;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_channel_list2nd);

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "ChannelListActivity");
        params.putString("app_event", "Viewing Channel Lists Page");
        mFirebaseAnalytics.logEvent("app_activity", params);




        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);

        channel_listview = findViewById(R.id.channelListview);



//        guestName = findViewById(R.id.guestNameTxtV2);
//        guestName.setText("Hi! " + vdata.getGuestFirstName() + " " + vdata.getGuestLastName());

        debugtxt = findViewById(R.id.debugtxt);
        RTSPPlayer = findViewById(R.id.RTSPlayer2);
        loadingS = findViewById(R.id.loadingStream2);
        arrowUp = findViewById(R.id.arrowup);
        arrowDown = findViewById(R.id.arrowdown);



        onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
//                    loadingS.setVisibility(View.GONE);
                    loadingS.setAlpha((float) 0);
                    Log.d("BUFFERING : ", "FALSE");
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
//                    loadingS.setVisibility(View.VISIBLE);
                    loadingS.setAlpha((float) 1);
                    Log.d("BUFFERING : ", "TRUE");
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
//                    loadingS.setVisibility(View.GONE);
                    loadingS.setAlpha((float) 0);
                    Log.d("BUFFERING : ", "FALSE END");
                }
                return false;
            }
        };





    }

    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
//        mTracker.setScreenName(vdata.getHotelName()+" ~ Room No. "+vdata.getRoomNumber()+" ~ "+"Channel List View");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        getChannels();
//        videoViewPlay();




    }


    @Override
    protected void onResume(){
        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar

    }


    @Override
    public void onBackPressed(){

        super.onBackPressed();

//        Intent i = new Intent(this, HotelServicesActivity.class);
//        i.putExtra(Variable.EXTRA, vdata);
//        startActivity(i);

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

    private void getChannels(){

        String url = vdata.getApiUrl() + "channels.php?hotel_id=" + vdata.getHotelID();
        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                JSONArray results = response;
                String serverss_url = vdata.getServerURL().toString();
                ArrayList<JSONObject> channel_list = getArrayListFromJSONArray(results);
                final ChannelListAdapter channel_adapter = new ChannelListAdapter(getApplicationContext(), R.layout.channel_item_list,channel_list,serverss_url);
                channel_listview.setAdapter(channel_adapter);
                channel_adapter.notifyDataSetChanged();

                channel_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        channel_adapter.setPosition(i);
                        channel_adapter.notifyDataSetChanged();

                        RTSPPlayer.stopPlayback();

                        Intent z = new Intent(ChannelListActivity.this, WatchTVActivity.class);
                        z.putExtra("CHANNEL_NUM", i);
                        z.putExtra(Variable.EXTRA, vdata);
                        z.putExtra(LauncherActivity.WATCHTV_FROM, "hotelservices");
                        startActivity(z);

                    }
                });
                channel_listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        channel_adapter.setPosition(i);
                        channel_adapter.notifyDataSetChanged();



                        int arrowCounter = channel_list.size();
//                        String grave = Integer.toString(arrowCounter);
//                        debugtxt.setText(grave);

                            if (i == 0){
                                arrowUp.setAlpha((float) 0.1);
                                arrowDown.setAlpha((float) 1);
                            } else if (i == arrowCounter - 1 ){
                                arrowUp.setAlpha((float) 1);
                                arrowDown.setAlpha((float) 0.1);
                            }else {
                                arrowUp.setAlpha((float) 1);
                                arrowDown.setAlpha((float) 1);
                            }

                        try {


                            ChanPath = channel_list.get(i).getString("channel_url");
                            RTSPPlayer.setVideoURI(Uri.parse(ChanPath));
                            RTSPPlayer.setOnInfoListener(onInfoToPlayStateListener);
                            RTSPPlayer.start();
//                            RTSPPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                public void onPrepared(MediaPlayer mp) {
//
//
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        }catch (JSONException je){je.printStackTrace();}
        return  aList;
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
