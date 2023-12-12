package com.example.taxibill.API;

import static com.example.taxibill.API.AppConstants.shouldEncrypt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ConnectionQuality;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.example.taxibill.R;
import com.example.taxibill.Utils.AES;
import com.example.taxibill.Utils.AlertYesNoListener;
import com.example.taxibill.Utils.App;
import com.example.taxibill.Utils.AppFunctions;
import com.example.taxibill.Utils.CommonConstants;
import com.example.taxibill.Utils.CommonFunctions;
import com.example.taxibill.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

public class NetworkController implements CommonConstants {

Context activity;

static Activity activity1;
static String ermsg = "", emsg = "";


public NetworkController(Context activity, Activity activity1) {
    this.activity = activity;
    this.activity1 = activity1;

    //requestQueue = Volley.newRequestQueue(activity);
}

static APICallbacks apiCallbacks = new APICallbacks() {
    @Override
    public void taskProgress(String tag, int progress, Bundle bundle) {

    }

    @Override
    public void taskFinish(APIStatus apiStatus, String tag, String message, JSONObject objectRes, Bundle bundle) {

    }
};

public static void callPaymentApiFile(Context context, Activity activity, String url, Map<String, String> mapParams, Map<String, String> mapFiles, Map<String, List<String>> mapFilesList, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {

    /*if (dev(activity)) {
        devCheck(activity);
    } else {*/

    ANRequest.MultiPartBuilder<?> multiPartBuilder = AndroidNetworking.upload(url);

    String logStr = url;

    if (mapParams != null) {
        JSONObject object = new JSONObject(mapParams);

        if (shouldEncrypt) {
            String encParams = AES.openssl_encrypt(object.toString());

            logStr += NEXT_LINE + AppConstants.blabla + ":" + encParams + NEXT_LINE + object;

            multiPartBuilder.addMultipartParameter(AppConstants.blabla, encParams);
        } else {

            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                String key = entry.getKey(), value = entry.getValue();
                multiPartBuilder.addMultipartParameter(key, value);
                logStr += NEXT_LINE + buildPostman(new JSONObject(mapParams), tag);

            }
        }


    }
    if (mapFiles != null) {
        buildPostman(new JSONObject(mapFiles), tag);
        for (Map.Entry<String, String> entry : mapFiles.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            multiPartBuilder.addMultipartFile(key, new File(value));
            logStr += NEXT_LINE + buildPostman(new JSONObject(mapFiles), tag);
        }
    }
    if (mapFilesList != null) {
        logStr += NEXT_LINE + buildPostman(new JSONObject(mapFilesList), tag);
        for (Map.Entry<String, List<String>> entry : mapFilesList.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (int j = 0; j < value.size(); j++) {
                multiPartBuilder.addMultipartFile(key, new File(value.get(j)));
            }
        }
    }
    CommonFunctions.writeAPIResponse(url + NEXT_LINE + "" + mapParams + NEXT_LINE + "" + mapFiles, context);

    String token = "Bearer " + App.getInstance().myPrefs.getString("Authorization");
    logStr += NEXT_LINE + token;

    if (!token.equalsIgnoreCase("bearer "))
        multiPartBuilder.addHeaders("Authorization", token);

    multiPartBuilder.setTag(tag);
    multiPartBuilder.setPriority(Priority.IMMEDIATE);
    //if (BuildConfig.DEBUG) {
        Log.i("APIHandler " + tag, logStr);
   // }
    ANRequest<?> request = multiPartBuilder.build().
            setUploadProgressListener(new UploadProgressListener() {
                @Override
                public void onProgress(long bytesUploaded, long totalBytes) {
                    // do anything with progress
                    int progress = (int) ((bytesUploaded * 100) / totalBytes);
                    Log.i("APIHandler " + tag, "Progress " + progress + SPACE + (bytesUploaded / 1024) + "/" + (totalBytes / 1024));
                    if (objectExtras != null) {
                        objectExtras.putLong("bytesUploaded", bytesUploaded);
                        objectExtras.putLong("totalBytes", totalBytes);
                    }
                    apiCallbacks.taskProgress(tag, progress, objectExtras);


                }
            });
    //Toast.makeText(context, logStr, Toast.LENGTH_SHORT).show();
    getResponse(context, activity, request, tag, objectExtras, apiCallbacks);
    //  }
}

public static void callApiFile(Context context, Activity activity, String url, Map<String, String> mapParams, Map<String, String> mapFiles,
                               Map<String, List<String>> mapFilesList, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {


   /* if (dev(activity)) {
        devCheck(activity);
    } else {*/

    ANRequest.MultiPartBuilder<?> multiPartBuilder = AndroidNetworking.upload(url);

    String logStr = url;


    if (mapParams != null) {

        JSONObject object = new JSONObject(mapParams);

        if (shouldEncrypt) {
            String encParams = AES.openssl_encrypt(object.toString());

            logStr += NEXT_LINE + AppConstants.blabla + ":" + encParams + NEXT_LINE + object;

            multiPartBuilder.addMultipartParameter(AppConstants.blabla, encParams);

        } else {

            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                String key = entry.getKey(), value = entry.getValue();
                multiPartBuilder.addMultipartParameter(key, value);
                logStr += NEXT_LINE + buildPostman(new JSONObject(mapParams), tag);

            }
        }
    }
    if (mapFiles != null) {
        buildPostman(new JSONObject(mapFiles), tag);
        for (Map.Entry<String, String> entry : mapFiles.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            multiPartBuilder.addMultipartFile(key, new File(value));
            logStr += NEXT_LINE + buildPostman(new JSONObject(mapFiles), tag);
        }
    }
    if (mapFilesList != null) {
        logStr += NEXT_LINE + buildPostman(new JSONObject(mapFilesList), tag);
        for (Map.Entry<String, List<String>> entry : mapFilesList.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (int j = 0; j < value.size(); j++) {
                multiPartBuilder.addMultipartFile(key, new File(value.get(j)));
            }
        }
    }
    CommonFunctions.writeAPIResponse(url + NEXT_LINE + "" + mapParams + NEXT_LINE + "" + mapFiles, context);

    String token = "Bearer " + App.getInstance().myPrefs.getString("Authorization");
    logStr += NEXT_LINE + token;

    if (!token.equalsIgnoreCase("bearer "))
        multiPartBuilder.addHeaders("Authorization", token);

    multiPartBuilder.setTag(tag);
    multiPartBuilder.setPriority(Priority.IMMEDIATE);
    ///if (BuildConfig.DEBUG) {
        Log.i("APIHandler " + tag, logStr);
  //  }
    ANRequest<?> request = multiPartBuilder.build().
            setUploadProgressListener(new UploadProgressListener() {
                @Override
                public void onProgress(long bytesUploaded, long totalBytes) {
                    // do anything with progress
                    int progress = (int) ((bytesUploaded * 100) / totalBytes);
                    Log.i("APIHandler " + tag, "Progress " + progress + SPACE + (bytesUploaded / 1024) + "/" + (totalBytes / 1024));
                    if (objectExtras != null) {
                        objectExtras.putLong("bytesUploaded", bytesUploaded);
                        objectExtras.putLong("totalBytes", totalBytes);
                    }
                    apiCallbacks.taskProgress(tag, progress, objectExtras);


                }
            });

    getResponse(context, activity, request, tag, objectExtras, apiCallbacks);
    // }
}

public static void callApiFileTest(Context context, Activity activity, String url, Map<String, String> mapParams, Map<String, String> mapFiles, Map<String, List<String>> mapFilesList, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {

   /* if (dev(activity)) {
        devCheck(activity);
    } else {*/

    ANRequest.MultiPartBuilder<?> multiPartBuilder = AndroidNetworking.upload(url);

    String logStr = url;


    if (mapParams != null) {
        JSONObject object = new JSONObject(mapParams);

        if (shouldEncrypt) {
            String encParams = AES.openssl_encrypt(object.toString());

            logStr += NEXT_LINE + AppConstants.blabla + ":" + encParams + NEXT_LINE + object;

            multiPartBuilder.addMultipartParameter(AppConstants.blabla, encParams);
        } else {

            for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                String key = entry.getKey(), value = entry.getValue();
                multiPartBuilder.addMultipartParameter(key, value);
                logStr += NEXT_LINE + buildPostman(new JSONObject(mapParams), tag);

            }
        }


    }
    if (mapFiles != null) {
        buildPostman(new JSONObject(mapFiles), tag);
        for (Map.Entry<String, String> entry : mapFiles.entrySet()) {
            String key = entry.getKey(), value = entry.getValue();
            multiPartBuilder.addMultipartFile(key, new File(value));
            logStr += NEXT_LINE + buildPostman(new JSONObject(mapFiles), tag);
        }
    }
    if (mapFilesList != null) {
        logStr += NEXT_LINE + buildPostman(new JSONObject(mapFilesList), tag);
        for (Map.Entry<String, List<String>> entry : mapFilesList.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            for (int j = 0; j < value.size(); j++) {
                multiPartBuilder.addMultipartFile(key, new File(value.get(j)));
            }
        }
    }
    CommonFunctions.writeAPIResponse(url + NEXT_LINE + "" + mapParams + NEXT_LINE + "" + mapFiles, context);

    String token = "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZjUzNGRkYTMzYzJhM2IwMDE0ZjA5NzI1NTJmMWI0NzI1ZWFlNzVlMGIzY2JkZWYzNTVhNjhhZWQxNjk1MTk3MzE4ZWJkODM2NjAwY2M5NzIiLCJpYXQiOjE2MDY1NzU4OTQsIm5iZiI6MTYwNjU3NTg5NCwiZXhwIjoxNjM4MTExODk0LCJzdWIiOiI3NDkiLCJzY29wZXMiOltdfQ.nvW7L4SVfLC_Ry_FrqVQN3F-I1_eMGsl7S_HdghOdI45OUdc2eFoWwIIIJ-h62QJvLQGrJAaz7JfZbc4J9UucebD4plzmuQXoArZ7nfSYLKH0qZP4qzIleYSEBukE7EDMrGk9w9RdwD2nxQ1EARTCPmvJ0qiDrJURJN3VltusbBwUUiwt2sfFvzJTNgQXe6CWwVl6zpWusxsOneNArlzmKKKVun1ggB3ySYYQNwcMK00Kp3tZUXOlj4zSZ77I7ZoGK6pC5_x9jbd5gXj2cp0NUnR9BXbg9ddVL3GD0HwFhGZ4EoPuQMB1AM053-9lVuEhlH0QmmjPeXZ-jePO1ZH2po5xMwnOs284zVlZDjA9OKQhtRBqXfIXXBg95-v1oVwwBYpu0eSr4uKa8scnbBntrjQQVDOcvt0KdAe6RteAjWzAbFrQhblz40PBWPKNLAxx3D0mX1613-eK1sj5Qnm_XkOkrvTliuAQTkP6Kj-PnIcuCxLANq-gu9hst4G5WVyYTo-CF_mcQyW7o9hGt168IG1pFcJcRn9sA7NTJZgwYBQoXtN3Oup2nxb6gfaMU9Ua9xhPZJpyxZdKCNI5bLC7zhl2zI9eJB74M43RZ4SwdvARSyxhVIQW3sAztpQR4RbrndehpF7HGo3NbTfarOf5pb_tBHdCWckDkLubHr0tr0";
    logStr += NEXT_LINE + token;

    if (!token.equalsIgnoreCase("bearer "))
        multiPartBuilder.addHeaders("Authorization", token);

    if (activity != null) {
        multiPartBuilder.setTag(activity);
    } else {
        multiPartBuilder.setTag(tag);
    }
    multiPartBuilder.setPriority(Priority.IMMEDIATE);
  //  if (BuildConfig.DEBUG) {
        Log.i("APIHandler " + tag, logStr);
  //  }
    ANRequest<?> request = multiPartBuilder.build().
            setUploadProgressListener(new UploadProgressListener() {
                @Override
                public void onProgress(long bytesUploaded, long totalBytes) {
                    // do anything with progress
                    int progress = (int) ((bytesUploaded * 100) / totalBytes);
                    Log.i("APIHandler " + tag, "Progress " + progress + SPACE + (bytesUploaded / 1024) + "/" + (totalBytes / 1024));
                    if (objectExtras != null) {
                        objectExtras.putLong("bytesUploaded", bytesUploaded);
                        objectExtras.putLong("totalBytes", totalBytes);
                    }
                    apiCallbacks.taskProgress(tag, progress, objectExtras);


                }
            });
    //Toast.makeText(context, logStr, Toast.LENGTH_SHORT).show();
    getResponse(context, activity, request, tag, objectExtras, apiCallbacks);
    //}
}

public static void callFileApi(Activity activity, String url, Map<String, String> mapParams, Map<String, String> mapFiles, Map<String, List<String>> mapFilesList, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {
    callApiFile(App.getInstance().getApplicationContext(), activity, url, mapParams, mapFiles, mapFilesList, tag, objectExtras, apiCallbacks);

}


public static void callApiPost(final Context context, Activity activity, JSONObject objectParams, String url, final String tag, Bundle objectExtras, final APICallbacks apiCallbacks) {

   /* if (dev(activity)) {
        devCheck(activity);
    } else {*/

    // do something


    ANRequest.PostRequestBuilder<?> postRequestBuilder = AndroidNetworking.post(url);

    CommonFunctions.writeAPIResponse(url + NEXT_LINE + "" + objectParams, context);
    String logStr = url;
    try {
        if (shouldEncrypt) {
            String encParams = AES.openssl_encrypt(objectParams.toString());
            postRequestBuilder.addBodyParameter(AppConstants.blabla, encParams);
           // if (BuildConfig.DEBUG) {
                logStr += NEXT_LINE + AppConstants.blabla + ":" + encParams + NEXT_LINE + objectParams;
          //  }
        } else {
            if (objectParams.length() > 0) {
                JSONArray arrayParams = objectParams.names();
               // if (BuildConfig.DEBUG) {
                    Log.i("ParamKeys", objectParams.toString());
                    Log.i("ParamKeys", arrayParams.toString());
             //   }
                for (int i = 0; i < arrayParams.length(); i++) {
                    String key = arrayParams.getString(i);
                    String value = objectParams.getString(key);
                    postRequestBuilder.addBodyParameter(key, value);


                }
                logStr += NEXT_LINE + buildPostman(objectParams, tag);
                ;


            }
        }

        postRequestBuilder.setTag(tag);
        postRequestBuilder.setPriority(Priority.IMMEDIATE);
        String token = "Bearer " + App.getInstance().myPrefs.getString("Authorization");
        logStr += NEXT_LINE + token;
      //  if (BuildConfig.DEBUG) {
            Log.i("APIHandler " + tag, logStr);
      //  }
        if (!token.equalsIgnoreCase("bearer "))
            postRequestBuilder.addHeaders("Authorization", token);

        ANRequest<?> request = postRequestBuilder.build();


        getResponse(context, activity, request, tag, objectExtras, apiCallbacks);
    } catch (JSONException e) {
        apiCallbacks.taskFinish(APIStatus.FAILED, tag, e.getMessage(), new JSONObject(), objectExtras);
        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
    // }

}


public static void callPostApi(Activity activity, JSONObject objectParams, String url, final String tag, Bundle objectExtras, final APICallbacks apiCallbacks) {

    /*if (dev(activity)) {
        devCheck(activity);
    } else {*/
    callApiPost(App.getInstance(), activity, objectParams, url, tag, objectExtras, apiCallbacks);
    //}
}

public static void callApiGet(final Context context, Activity activity, JSONObject objectParams, String url, final String tag, Bundle objectExtras, final APICallbacks apiCallbacks) {

    ANRequest.GetRequestBuilder<?> getRequestBuilder = AndroidNetworking.get(url);
    Log.i("APIHandler " + tag, url);
    buildPostman(objectParams, tag);
    try {

        if (objectParams.length() > 0) {
            JSONArray arrayParams = objectParams.names();
            Log.i("ParamKeys", objectParams.toString());
            Log.i("ParamKeys", arrayParams.toString());
            for (int i = 0; i < arrayParams.length(); i++) {
                String key = arrayParams.getString(i);
                String value = objectParams.getString(key);
                getRequestBuilder.addQueryParameter(key, value);


            }

            getRequestBuilder.setTag(tag);
            getRequestBuilder.setPriority(Priority.HIGH);
        }
        getRequestBuilder.addHeaders("Authorization", "Bearer " + context.getSharedPreferences(SHARED_PREF_NAME, 0).getString("Authorization", ""));
        ANRequest<?> request = getRequestBuilder.build();

        getResponse(context, activity, request, tag, objectExtras, apiCallbacks);
    } catch (JSONException e) {
        apiCallbacks.taskFinish(APIStatus.FAILED, tag, e.getMessage(), new JSONObject(), objectExtras);

        e.printStackTrace();
    }

}

public static void getResponse(Context context, final Activity activity, ANRequest<?> request, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {
    AlertYesNoListener alertYesNoListener = new AlertYesNoListener() {
        @Override
        public void onYesClick(String requestCode) {

        }

        @Override
        public void onNoClick(String requestCode) {

        }

        @Override
        public void onNeutralClick(String requestCode) {

        }
    };

    if (NetworkUtils.isConnected(context)) {

        ConnectionQuality connectionQuality = AndroidNetworking.getCurrentConnectionQuality();
        boolean connectOk = connectionQuality == ConnectionQuality.EXCELLENT || connectionQuality == ConnectionQuality.GOOD || connectionQuality == ConnectionQuality.UNKNOWN;
        Log.i("APIHandler " + tag, connectionQuality.toString());


        request.getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                AppFunctions.checkApiRedirect(activity, tag, response, objectExtras, apiCallbacks);
                Log.i("APIHandler " + tag, "" + response);
               // if (BuildConfig.DEBUG) {
                    CommonFunctions.writeAPIResponse(tag + NEXT_LINE + response, context);

             //   }
            }

            @Override
            public void onError(ANError anError) {

                Log.i("APIHandler " + tag, "" + anError.getErrorCode() + SPACE + anError.getMessage() + SPACE + anError.getErrorDetail() + " " + " " + anError.getErrorBody());
            //    String msg = context.getString(R.string.connect_error) + ", please contact " + SUPPORT_EMAIL, errorMsg = "" + anError.getMessage();
              //  ermsg = context.getString(R.string.connect_error) + ", please contact " + SUPPORT_EMAIL;
                emsg = "" + anError.getMessage();
                Log.i("errormsg", ermsg);


                if (anError.getErrorCode() >= 400 && activity != null) {
               //     msg = context.getString(R.string.connect_error) + ".., please contact " + SUPPORT_EMAIL;
              //      AppFunctions.showFailure(activity, msg, "failed", "", alertYesNoListener);
                } else {
                    if (!NetworkUtils.isConnected(context)) {
                  //      msg = context.getString(R.string.connect_error);
                    }

                    if (activity != null && anError.getMessage() != null) {
                        Toast.makeText(context, "Networking Error. Please Try Again..", Toast.LENGTH_SHORT).show();
                //        AppFunctions.showFailure(activity, msg, "failed", "", alertYesNoListener);
                    }
                }
              //  apiCallbacks.taskFinish(APIStatus.FAILED, tag, msg, new JSONObject(), objectExtras);

                //callErrorAPI(String.valueOf(anError)); //If an error occurs this API gets called.
            }
        });


    } else {
        apiCallbacks.taskFinish(APIStatus.FAILED, tag, context.getString(R.string.internet_off_error), new JSONObject(), objectExtras);


        if (activity != null) {
            AppFunctions.showFailure(activity, context.getString(R.string.internet_off_error), "failed", "", new AlertYesNoListener() {
                @Override
                public void onYesClick(String requestCode) {

                }

                @Override
                public void onNoClick(String requestCode) {

                }

                @Override
                public void onNeutralClick(String requestCode) {

                }
            });
        }
    }

}

public static String buildPostman(JSONObject objectParams, String tag) {
    StringBuilder postman = new StringBuilder();
    if (objectParams.length() > 0) {

        try {
            JSONArray arrayParams = objectParams.names();

            for (int i = 0; i < arrayParams.length(); i++) {
                String key = arrayParams.getString(i);
                String value = objectParams.getString(key);
                postman.append(key);
                postman.append(":");
                postman.append(value);
                postman.append("\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //if (BuildConfig.DEBUG) {
        Log.i("APIHandler " + tag, postman.toString());
   // }
    return "" + postman.toString();
}


public static void downloadFile(Activity activity, String url, final String dirPath, final String fileName, final String tag, final Bundle objectExtras, final APICallbacks apiCallbacks) {

    final String filePath = dirPath + "/" + fileName;

    Log.i("Download", dirPath);
    Log.i("Download", fileName);

    AndroidNetworking.download(url, dirPath.toString(), fileName)
            .setTag(tag)
            .setPriority(Priority.MEDIUM)
            .build()
            .setDownloadProgressListener(new DownloadProgressListener() {
                @Override
                public void onProgress(long bytesDownloaded, long totalBytes) {

                    int progress = (int) ((bytesDownloaded * 100) / totalBytes);
                    objectExtras.putLong("bytesUploaded", bytesDownloaded);
                    objectExtras.putLong("totalBytes", totalBytes);
                    apiCallbacks.taskProgress(tag, progress, objectExtras);


                    // do anything with progress
                }
            })
            .startDownload(new DownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Log.i("Download", filePath);
                    try {
                        JSONObject object = new JSONObject();
                        object.put("status", true);
                        object.put("file", filePath);
                        apiCallbacks.taskFinish(APIStatus.SUCCESS, tag, "", object, objectExtras);
                    } catch (JSONException e) {
                        apiCallbacks.taskFinish(APIStatus.FAILED, tag, e.getMessage(), new JSONObject(), objectExtras);
                        e.printStackTrace();
                    }
                    // do anything after completion
                }

                @Override
                public void onError(ANError anError) {
                    Context context = App.getInstance();

                    Log.i("Download", "" + anError.getMessage());
                    String msg = context.getString(R.string.connect_error), errorMsg = "" + anError.getMessage();

                    apiCallbacks.taskFinish(APIStatus.FAILED, tag, msg, new JSONObject(), objectExtras);
                    // handle error

                    if (errorMsg.toLowerCase().contains("timeout")) {
                        msg = context.getString(R.string.timeout_error);
                    }

                    if (errorMsg.toLowerCase().contains("unknownhost")) {
                        msg = "Please Check Internet";
                    }

                    if (!NetworkUtils.isConnected(context)) {
                        msg = context.getString(R.string.internet_off_error);
                    }


                    if (activity != null && anError.getMessage() != null) {
                        AppFunctions.showFailure(activity, msg, "failed", "", new AlertYesNoListener() {
                            @Override
                            public void onYesClick(String requestCode) {

                            }

                            @Override
                            public void onNoClick(String requestCode) {

                            }

                            @Override
                            public void onNeutralClick(String requestCode) {

                            }
                        });
                    }
                }
            });
}

public static boolean dev(Activity activity) {
    /*int developerOptions = Settings.Secure.getInt(activity.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
    boolean areDeveloperOptionsEnabled = (developerOptions == 1);
    if (BuildConfig.DEBUG) {
        areDeveloperOptionsEnabled = false;
    }*/
    return false;
}


/* public static void devCheck(Activity activity) {
    Intent intent = new Intent(activity, DevOptionActivity.class);
    activity.startActivity(intent);
    activity.finishAffinity();
}*/

public static void cancel(Object tag) {
    AndroidNetworking.forceCancel(tag);
}


public static void cancelTag(String[] tags) {
    for (String s : tags
    ) {
        AndroidNetworking.forceCancel(s);
    }

}


public static void cancelActivity(Object tag) {
    AndroidNetworking.forceCancel(tag);
}



}
