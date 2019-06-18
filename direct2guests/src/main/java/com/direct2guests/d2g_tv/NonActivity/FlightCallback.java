package com.direct2guests.d2g_tv.NonActivity;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface FlightCallback {
    JSONArray onSuccess(JSONObject response);
    JSONArray onError(VolleyError error);
}
