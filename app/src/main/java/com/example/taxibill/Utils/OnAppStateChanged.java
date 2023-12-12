package com.example.taxibill.Utils;

import org.json.JSONObject;

public interface OnAppStateChanged {
    void onForeground(JSONObject object);
    void onBackground(JSONObject object);
}
