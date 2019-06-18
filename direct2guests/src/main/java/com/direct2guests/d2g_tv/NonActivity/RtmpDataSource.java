package com.direct2guests.d2g_tv.NonActivity;

import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

import net.butterflytv.rtmp_client.RtmpClient;

import java.io.IOException;


public class RtmpDataSource implements DataSource {

    private static String TAG = "RtmpSouce";

    public static class RtmpDataSourceFactory implements DataSource.Factory {

        @Override
        public DataSource createDataSource() {
            return new RtmpDataSource();
        }
    }

    private final RtmpClient rtmpClient;
    private Uri uri;

    public RtmpDataSource() {
        rtmpClient = new RtmpClient();
    }


    @Override
    public Uri getUri() {
        return uri;
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        Log.w(TAG, "open is called");
        String uriString = dataSpec.uri.toString();
        try {
            rtmpClient.open(uriString, false);
            uri = dataSpec.uri;
        }
        catch (Exception e) {
            if (e instanceof RtmpClient.RtmpIOException) {
                RtmpClient.RtmpIOException rtmpIOException = (RtmpClient.RtmpIOException) e;
                Log.e(TAG, "error code is:" + rtmpIOException.errorCode);
            }
            e.printStackTrace();
            return 0;
        }
        return C.LENGTH_UNSET;
    }

    @Override
    public void close() throws IOException {
        Log.w(TAG, "close is called");
        rtmpClient.close();
    }

    @Override
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        return rtmpClient.read(buffer, offset, readLength);

    }
}
