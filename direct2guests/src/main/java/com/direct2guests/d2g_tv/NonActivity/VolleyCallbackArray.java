package com.direct2guests.d2g_tv.NonActivity;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface VolleyCallbackArray {
    void onSuccess(JSONArray response);
    void onError(VolleyError error);
}
