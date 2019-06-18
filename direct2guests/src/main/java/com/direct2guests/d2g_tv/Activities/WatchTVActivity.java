package com.direct2guests.d2g_tv.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.direct2guests.d2g_tv.NonActivity.ChannelListAdapter;
import com.direct2guests.d2g_tv.NonActivity.NetworkConnection;
import com.direct2guests.d2g_tv.NonActivity.RtmpDataSource;
import com.direct2guests.d2g_tv.NonActivity.Variable;
import com.direct2guests.d2g_tv.NonActivity.VolleyCallbackArray;
import com.direct2guests.d2g_tv.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class  WatchTVActivity extends Activity {
    private static final String TAG = WatchTVActivity.class.getSimpleName();

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENTS = 256;

    private Variable vdata;
    private NetworkConnection nc = new NetworkConnection();
    private SimpleExoPlayerView simpleExoPlayer;
    private Uri mp4VideoUri;
    private SimpleExoPlayer player;
    private String[] channelTitle, channelURL, channelType;
    private TrackSelector trackSelector;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private DefaultRenderersFactory defaultRenderersFactory;
    private LoadControl ldControl;
    private DefaultAllocator dfAllocator;
    private int onChannel, firstRun;
    private String  watchTVfrom;
    private Handler mainHandler;
    private int number;
    private TextView textView;
    private String marquee_text, marqueets;
    private Tracker mTracker;
    private MediaSource videoSource;
    private RtmpDataSource.RtmpDataSourceFactory rtmpDataSourceFactory;

    private VideoView RTSPPlayer;

    private JsonArrayRequest jsonRequest;
    private RequestQueue queue;
    private Thread t;

    private ProgressBar loadingS;
    private MediaPlayer.OnInfoListener onInfoToPlayStateListener;


    private Calendar calendar;
    private String chour, cminutes;
    private int dbhour, dbminutes, openHr, openMin, timeO = 0;
    private String[] time_open;

    private LinearLayout pushBase;
    private LayoutInflater inflater;
    private View[] pushView;
    private TextView pushText;


    private ListView channel_listview;
    private String ChanPath;

    TextView debugtxt;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        setContentView(R.layout.activity_watch_tv);

        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("app_page", "WatchTVActivity");
        params.putString("app_event", "Watching TV");
        mFirebaseAnalytics.logEvent("app_activity", params);





        vdata = (Variable)getIntent().getSerializableExtra(Variable.EXTRA);
        watchTVfrom = getIntent().getStringExtra(LauncherActivity.WATCHTV_FROM);
        number = getIntent().getExtras().getInt("CHANNEL_NUM");

        textView = findViewById(R.id.sliding_text_marquee);
        queue = Volley.newRequestQueue(this);
        mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        simpleExoPlayer = findViewById(R.id.channel_view);
        simpleExoPlayer.setUseController(false );
        defaultRenderersFactory = new DefaultRenderersFactory(this.getApplicationContext(), null, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
        calendar = Calendar.getInstance();
        onChannel = number;
        firstRun = 1;
        RTSPPlayer = findViewById(R.id.rtspPlayer);
        loadingS = findViewById(R.id.loadingStream);
        onInfoToPlayStateListener = new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what) {
                    loadingS.setVisibility(View.GONE);
                    Log.d("BUFFERING : ", "FALSE");
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    loadingS.setVisibility(View.VISIBLE);
                    Log.d("BUFFERING : ", "TRUE");
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    loadingS.setVisibility(View.GONE);
                    Log.d("BUFFERING : ", "FALSE END");
                }
                return false;
            }
        };
        pushBase = findViewById(R.id.linearBase);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marqueeThread();


        channel_listview = findViewById(R.id.tvchannelListView);




    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
        if(channelType[onChannel].equals("HLS") || channelType[onChannel].equals("RTMP") || channelType[onChannel].equals("HTTP")) {
            player.stop();
            player.release();
            player = null;
            trackSelector = null;
            Log.d("CHANNEL TYPE : ", "HLS, HTTP, RTMP");
        }else{
            RTSPPlayer.stopPlayback();
            Log.d("CHANNEL TYPE : ", "RTSP");
        }
        /*if(watchTVfrom.equals("launcher")){
            Intent i = new Intent(this, LauncherActivity.class);
            i.putExtra(Variable.EXTRA, vdata);
            startActivity(i);
        }else if(watchTVfrom.equals("hotelservices")){
            Intent i = new Intent(this, HotelServicesActivity.class);
            i.putExtra(Variable.EXTRA, vdata);
            i.putExtra(HotelServicesActivity.CLICK_FROM, "watchtv");
            startActivity(i);
        }*/
        t.interrupt();
        super.onBackPressed();

    }

    @Override
    protected void onStart(){
        super.onStart();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar


//        mTracker.setScreenName(vdata.getHotelName()+" ~ Room No. "+vdata.getRoomNumber()+" ~ "+"Watching TV View");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        getChannels();


    }
    @Override
    protected void onResume(){

        super.onResume();
        //start code hide status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //end code hide status bar
        //getChannels();


    }

    @Override
    protected void onPause(){
        super.onPause();


    }

    @Override
    protected void onStop(){

        super.onStop();

    }

    @Override
    protected void onRestart(){

        super.onRestart();

    }


    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && KeyEvent.ACTION_DOWN == event.getAction()){
            if(channelType[onChannel].equals("HLS") || channelType[onChannel].equals("HTTP") || channelType[onChannel].equals("RTMP")){

                if(channelURL.length == onChannel+1) {
                    if (channelType[0].equals("HLS") || channelType[0].equals("RTMP") || channelType[0].equals("HTTP")) {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (channelType[onChannel + 1].equals("HLS") || channelType[onChannel + 1].equals("RTMP") || channelType[onChannel + 1].equals("HTTP")) {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;;
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }
                    }else{
                if(channelURL.length == onChannel+1) {
                    if (channelType[0].equals("HLS") || channelType[0].equals("RTMP") || channelType[0].equals("HTTP")) {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (channelType[onChannel + 1].equals("HLS") || channelType[onChannel + 1].equals("RTMP") || channelType[onChannel + 1].equals("HTTP")) {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }
            }
            if(channelURL.length == onChannel+1){

                onChannel = 0;
            }else {
                onChannel++;
            }
            displayChannel(onChannel);
            //displayRTSP(onChannel);
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && KeyEvent.ACTION_DOWN == event.getAction()){
            if(channelType[onChannel].equals("HLS") || channelType[onChannel].equals("HTTP") || channelType[onChannel].equals("RTMP")){

                if(onChannel == 0) {
                    if (channelType[channelURL.length -1].equals("HLS") || channelType[channelURL.length -1].equals("RTMP") || channelType[channelURL.length-1].equals("HTTP")) {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (channelType[onChannel - 1].equals("HLS") || channelType[onChannel - 1].equals("RTMP") || channelType[onChannel - 1].equals("HTTP")) {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        player.stop();
                        player.release();
                        player = null;
                        trackSelector = null;
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                if(onChannel == 0) {
                    if (channelType[channelURL.length-1].equals("HLS") || channelType[channelURL.length-1].equals("RTMP") || channelType[channelURL.length-1].equals("HTTP")) {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }else{
                    if (channelType[onChannel - 1].equals("HLS") || channelType[onChannel - 1].equals("RTMP") || channelType[onChannel - 1].equals("HTTP")) {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.VISIBLE);
                        RTSPPlayer.setVisibility(View.GONE);
                    } else {
                        RTSPPlayer.stopPlayback();
                        simpleExoPlayer.setVisibility(View.GONE);
                        RTSPPlayer.setVisibility(View.VISIBLE);
                    }
                }
            }
            if(onChannel == 0){
                onChannel = channelURL.length-1;
            }else{
                onChannel--;
            }
            displayChannel(onChannel);


            //displayRTSP(onChannel);
        }
        else    if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            ToastHomeButton();
            return true;
        }


//        else if((keyCode == KeyEvent.KEYCODE_HOME && KeyEvent.ACTION_DOWN == event.getAction()) || keyCode == KeyEvent.KEYCODE_HOME && KeyEvent.ACTION_UP == event.getAction()){
            //do nothing
//        }

        return super.onKeyDown(keyCode, event);
    }

    private void getChannels(){
        String url = vdata.getApiUrl() + "channels.php?hotel_id=" + vdata.getHotelID();
        nc.getdataArray(url, getApplicationContext(), new VolleyCallbackArray() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    JSONArray results = response;
                    channelTitle = new String[results.length()];
                    channelURL = new String[results.length()];
                    channelType = new String[results.length()];
                    for(int i = 0; i < results.length(); i++){
                        channelTitle[i] = results.getJSONObject(i).getString("channel_name");
                        channelURL[i] = results.getJSONObject(i).getString("channel_url");
                        channelType[i] = results.getJSONObject(i).getString("channel_type");
                    }

                    displayChannel(onChannel);
                    //displayRTSP(onChannel);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void displayChannel(int onChannel){


        if(channelType[onChannel].equals("HLS")) {
            displayHLS(onChannel);
        }else if(channelType[onChannel].equals("RTMP")) {
            displayRTMP(onChannel);
        }else if(channelType[onChannel].equals("HTTP")){
            displayHTTP(onChannel);
        }else if(channelType[onChannel].equals("RTSP")){

            simpleExoPlayer.setVisibility(View.GONE);
            RTSPPlayer.setVisibility(View.VISIBLE);
            displayRTSP(onChannel);

        }

    }

    private void restartChannel(){
        if(firstRun == 1){
            firstRun = 0;
        }else{
            player.release();
            player = null;
            trackSelector = null;

        }

        if(channelType[onChannel].equals("HLS")) {
            displayHLS(onChannel);
        }else if(channelType[onChannel].equals("RTMP")) {
            displayRTMP(onChannel);
        }else if(channelType[onChannel].equals("HTTP")){
            displayHTTP(onChannel);
        }else if(channelType[onChannel].equals("RTSP")){

            simpleExoPlayer.setVisibility(View.GONE);
            RTSPPlayer.setVisibility(View.VISIBLE);
            displayRTSP(onChannel);
        }
    }
    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private void displayHLS(int onChannel){
        if(player != null) {
            player.setPlayWhenReady(true);
        }else{
            loadingS.setVisibility(View.GONE);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector);

            mp4VideoUri = Uri.parse(channelURL[onChannel]);
            Toast.makeText(getApplicationContext(), channelTitle[onChannel], Toast.LENGTH_SHORT).show();

            Bundle params = new Bundle();
            params.putString("app_page", "WatchTVActivity");
            params.putString("app_event", "Watching: " + channelTitle[onChannel]);
            mFirebaseAnalytics.logEvent("app_activity", params);



            simpleExoPlayer.setPlayer(player);

            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "Direct2GuestsTV"), defaultBandwidthMeter);
            // This is the MediaSource representing the media to be played.
            //hls
            videoSource = new HlsMediaSource(mp4VideoUri, dataSourceFactory, null, null);
            simpleExoPlayer.setVisibility(View.VISIBLE);
            RTSPPlayer.setVisibility(View.GONE);
            Log.d("Channel Type :", " HLS");
            // Prepare the player with the source.
            player.prepare(videoSource);
            player.setPlayWhenReady(true);
            player.addListener(new ExoPlayer.EventListener() {


                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        //Toast.makeText(getApplicationContext(), "Playback ended", Toast.LENGTH_LONG).show();
                    } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
                        loadingS.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "Buffering..", Toast.LENGTH_SHORT).show();
                    } else if (playbackState == ExoPlayer.STATE_READY) {
                    /*player.setPlayWhenReady(false);
                    player.getPlaybackState();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            player.setPlayWhenReady(true);
                            player.getPlaybackState();
                            loadingS.setVisibility(View.INVISIBLE);

                        }
                    }, 5000);*/


                        loadingS.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                    switch (error.type) {
                        case ExoPlaybackException.TYPE_SOURCE:
                            if (isBehindLiveWindow(error)) {
                                Log.d("Channel Restarted", "true");
                                restartChannel();
                            }
                            error.printStackTrace();
                            break;

                        case ExoPlaybackException.TYPE_RENDERER:
                            if (isBehindLiveWindow(error)) {
                                Log.d("Channel Restarted", "true");
                                restartChannel();
                            }
                            error.printStackTrace();
                            break;

                        case ExoPlaybackException.TYPE_UNEXPECTED:
                            if (isBehindLiveWindow(error)) {
                                Log.d("Channel Restarted", "true");
                                restartChannel();
                            }
                            error.printStackTrace();
                            break;
                    }
                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });
        }
    }

    private void displayRTMP(int onChannel){

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(defaultRenderersFactory, trackSelector, new DefaultLoadControl());

        mp4VideoUri = Uri.parse(channelURL[onChannel]);
        Toast.makeText(getApplicationContext(), channelTitle[onChannel], Toast.LENGTH_SHORT).show();

        Bundle params = new Bundle();
        params.putString("app_page", "WatchTVActivity");
        params.putString("app_event", "Watching: " + channelTitle[onChannel]);
        mFirebaseAnalytics.logEvent("app_activity", params);


        simpleExoPlayer.setPlayer(player);

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        //DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "Direct2GuestsTV"), defaultBandwidthMeter);
        // This is the MediaSource representing the media to be played.
        rtmpDataSourceFactory = new RtmpDataSource.RtmpDataSourceFactory();
        videoSource = new ExtractorMediaSource(mp4VideoUri, rtmpDataSourceFactory, new DefaultExtractorsFactory(), mainHandler, null);
        simpleExoPlayer.setVisibility(View.VISIBLE);
        RTSPPlayer.setVisibility(View.GONE);
        Log.d("Channel Type :", " RTMP");
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    //Toast.makeText(getApplicationContext(), "Playback ended", Toast.LENGTH_LONG).show();
                    restartChannel();
                }
                else if (playbackState == ExoPlayer.STATE_BUFFERING)
                {
                    loadingS.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "Buffering..", Toast.LENGTH_SHORT).show();
                }
                else if (playbackState == ExoPlayer.STATE_READY)
                {
                    loadingS.setVisibility(View.INVISIBLE);
                    player.setPlayWhenReady(true);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                switch(error.type){
                    case ExoPlaybackException.TYPE_SOURCE:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;

                    case ExoPlaybackException.TYPE_RENDERER:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;

                    case ExoPlaybackException.TYPE_UNEXPECTED:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;
                }
            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }

    private void displayHTTP(int onChannel){

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector);

        mp4VideoUri = Uri.parse(channelURL[onChannel]);
        Toast.makeText(getApplicationContext(), channelTitle[onChannel], Toast.LENGTH_SHORT).show();

        Bundle params = new Bundle();
        params.putString("app_page", "WatchTVActivity");
        params.putString("app_event", "Watching: " + channelTitle[onChannel]);
        mFirebaseAnalytics.logEvent("app_activity", params);


        simpleExoPlayer.setPlayer(player);

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "Direct2GuestsTV"), defaultBandwidthMeter);
        // This is the MediaSource representing the media to be played.
        videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, new DefaultExtractorsFactory(), mainHandler, null);
        simpleExoPlayer.setVisibility(View.VISIBLE);
        RTSPPlayer.setVisibility(View.GONE);
        Log.d("Channel Type :", " HTTP");
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    Toast.makeText(getApplicationContext(), "Playback ended", Toast.LENGTH_LONG).show();
                }
                else if (playbackState == ExoPlayer.STATE_BUFFERING)
                {
                    loadingS.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Buffering..", Toast.LENGTH_SHORT).show();
                }
                else if (playbackState == ExoPlayer.STATE_READY)
                {
                    loadingS.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                switch(error.type){
                    case ExoPlaybackException.TYPE_SOURCE:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;

                    case ExoPlaybackException.TYPE_RENDERER:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;

                    case ExoPlaybackException.TYPE_UNEXPECTED:
                        if (isBehindLiveWindow(error)) {
                            Log.d("Channel Restarted", "true");
                            restartChannel();
                        }
                        error.printStackTrace();
                        break;
                }
            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }

    private void displayRTSP(int onChannel){
        RTSPPlayer.setVideoURI(Uri.parse(channelURL[onChannel]));
        RTSPPlayer.setOnInfoListener(onInfoToPlayStateListener);
        RTSPPlayer.requestFocus();
        RTSPPlayer.start();
        Toast.makeText(getApplicationContext(), channelTitle[onChannel], Toast.LENGTH_SHORT).show();

        Bundle params = new Bundle();
        params.putString("app_page", "WatchTVActivity");
        params.putString("app_event", "Watching: " + channelTitle[onChannel]);
        mFirebaseAnalytics.logEvent("app_activity", params);


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(RTSPPlayer.isPlaying()){
                    loadingS.setVisibility(View.INVISIBLE);
                    Log.d("BUFFERING : ", "FALSE");
                }else{
                    loadingS.setVisibility(View.VISIBLE);
                    Log.d("BUFFERING : ", "TRUE");
                }
            }
        }, 1000);*/

        /*try{
            MediaController mediaController = new MediaController(this);

            Uri video = Uri.parse(channelURL[onChannel]);

            mediaController.setAnchorView(RTSPPlayer);
            RTSPPlayer.requestFocus();
            RTSPPlayer.setMediaController(mediaController);
            RTSPPlayer.setVideoURI(video);


            RTSPPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {

                @Override
                public void onPrepared(MediaPlayer arg0)
                {
                    RTSPPlayer.start();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }*/
    }



    private void marqueeThread(){
        final String url = vdata.getApiUrl() + "marquee.php?hotel_id=" + vdata.getHotelID();
        marqueets = "                                      ";
        marquee_text = "";
        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(75000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                        new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {
                                                try {

                                                    if (response.length() <= 0) {
                                                        textView.setVisibility(View.GONE);
                                                        Log.d("MARQUEE : ", "ZERO");
                                                    } else {
                                                        textView.setText(marqueets);
                                                        for (int i = 0; i < response.length(); i++) {
                                                            pushView = new View[response.length()];
                                                            calendar = Calendar.getInstance();
                                                            chour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                                                            cminutes = Integer.toString(calendar.get(Calendar.MINUTE));
                                                            time_open = response.getJSONObject(i).getString("ticker_start").split(":");
                                                            openHr = Integer.parseInt(time_open[0]);
                                                            openMin = Integer.parseInt(time_open[1]);

                                                            if (chour.length() == 1) {
                                                                chour = "0" + chour;
                                                            }
                                                            if (cminutes.length() == 1) {
                                                                cminutes = "0" + cminutes;
                                                            }
                                                            dbhour = Integer.parseInt(chour);
                                                            dbminutes = Integer.parseInt(cminutes);
                                                            if (dbhour == openHr) {
                                                                if (dbminutes == openMin) {
                                                                    timeO = 1;
                                                                } else {
                                                                    timeO = 0;
                                                                }
                                                            } else {
                                                                timeO = 0;
                                                            }

                                                            if(timeO == 1){
                                                                pushView[i] = inflater.inflate(R.layout.push_notification_item, pushBase, false);
                                                                pushText = pushView[i].findViewById(R.id.pushText);
                                                                pushText.setText(response.getJSONObject(i).getString("ticker_description"));
                                                                pushBase.addView(pushView[i]);
                                                                removePushNotif(Integer.parseInt(response.getJSONObject(i).getString("duration")),i, pushView[i]);
                                                                Log.d("TIME ", "EQUAL");
                                                            }else{
                                                                Log.d("TIME", "NOT EQUAL");
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
    }
    protected void removePushNotif(int duration,final int index, final View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //pushBase.removeViewAt(index);
                pushBase.removeView(view);
                Log.d("REMOVE ", "PUSH NOTIFICATION");
            }
        }, duration);
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

