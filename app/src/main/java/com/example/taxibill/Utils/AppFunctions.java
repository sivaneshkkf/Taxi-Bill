package com.example.taxibill.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.example.taxibill.API.APICallbacks;
import com.example.taxibill.API.APIStatus;
import com.example.taxibill.API.UserData;
import com.example.taxibill.MainActivity;
import com.example.taxibill.databinding.DialogFailureBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class AppFunctions implements CommonConstants {

    public static void redirectToHomeToken(Activity activity) {

        MyPrefs.getInstance(activity.getApplicationContext()).putString("chatbotdate", "");
        MyPrefs.getInstance(activity.getApplicationContext()).putString("adminreplytime", "");

        App.executorService.execute(new Runnable() {
            @Override
            public void run() {
            }
        });
        App.getInstance().myPrefs.putBoolean(UserData.KEY_LOGGED_IN, false);

        //Freshchat.resetUser(activity);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //activity.finishAffinity();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void checkApiRedirect(Activity activity, String tag, String response, Bundle objectExtras, APICallbacks apiCallbacks) {
        /*response = "{\n" +
                "    \"status\": false,\n" +
                "    \"type\": \"error\",\n" +
                "    \"message\": {\n" +
                "        \"msg\": \"Unauthorized\"\n" +
                "    }\n" +
                "}";*/
        AlertYesNoListener alertYesNoListener = new AlertYesNoListener() {
            @Override
            public void onYesClick(String requestCode) {
                if (requestCode.equalsIgnoreCase("expire")) {
                    AppFunctions.redirectToHomeToken(activity);
                }
            }

            @Override
            public void onNoClick(String requestCode) {

            }

            @Override
            public void onNeutralClick(String requestCode) {

            }
        };
        try {
            //JSONObject objectR = new JSONObject(response.replace("null", "\"\"")), object = objectR;
            JSONObject objectR = new JSONObject(response), object;

            if (objectR.has("decryptval")) {
                String decryptJsonStr = AES.decrypt(objectR.getString("decryptval")).replace(doubleQuotes + ":null", doubleQuotes + ":" + doubleQuotes + doubleQuotes);
                object = new JSONObject(decryptJsonStr);

                CommonFunctions.writeAPIResponse(tag + NEXT_LINE + decryptJsonStr, App.getInstance());
                //responseToProcess = AES.decrypt(responseToProcess);
            } else {
                object = new JSONObject(response.replace(doubleQuotes + ":null", doubleQuotes + ":" + doubleQuotes + doubleQuotes));
            }
            Log.i("APIHandler" + tag, object.toString());
            if (!object.getBoolean("status")) {
                if (object.has("type")) {
                    String type = object.getString("type");

                    apiCallbacks.taskFinish(APIStatus.FAILED, tag, getApiError(object), object, objectExtras);
                    if (type.equalsIgnoreCase("error")) {

                        if (activity != null) {
                            Log.i("RestartHome1", "true");
                            App.getInstance().myPrefs.putBoolean(UserData.KEY_LOGGED_IN, false);
                            AppFunctions.showFailure(activity, AppFunctions.getApiError(object), "expire", "", alertYesNoListener);
                        }
                    }

                } else {

                    apiCallbacks.taskFinish(APIStatus.SUCCESS, tag, "", object, objectExtras);
                }

            } else {
                apiCallbacks.taskFinish(APIStatus.SUCCESS, tag, "", object, objectExtras);

            }
        } catch (JSONException e) {
            apiCallbacks.taskFinish(APIStatus.FAILED, tag, e.getMessage(), new JSONObject(), objectExtras);
            e.printStackTrace();

        }
    }

    public static void showFailure(Activity activity, String message, final String code, String label, final AlertYesNoListener alertYesNoListener) {
        DialogFailureBinding successBinding = DialogFailureBinding.inflate(activity.getLayoutInflater(), null, false);
        final AlertDialog alertDialog = DialogUtils.getCustomAlertDialog(activity, successBinding.getRoot());
        if (!label.equalsIgnoreCase("")) {
            successBinding.buttonContinue.setText(label);
        }
        successBinding.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                alertYesNoListener.onYesClick(code);
            }
        });
        successBinding.textViewMessage.setText(message);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static String getApiError(JSONObject objectRes) throws JSONException {

        return objectRes.getJSONObject("message").getString("msg");

    }
}
