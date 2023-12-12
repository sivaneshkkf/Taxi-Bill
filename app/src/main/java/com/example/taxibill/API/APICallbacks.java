package com.example.taxibill.API;

import android.os.Bundle;

import org.json.JSONObject;

public interface APICallbacks {
    void taskProgress(String tag, int progress, Bundle bundle);
    void taskFinish(APIStatus apiStatus, String tag, String message, JSONObject objectRes, Bundle bundle);
    //void taskFail(String tag, String response, Bundle bundle);
}
