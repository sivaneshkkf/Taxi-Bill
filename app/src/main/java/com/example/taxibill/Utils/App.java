package com.example.taxibill.Utils;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.androidnetworking.AndroidNetworking;
import com.droidnet.DroidNet;
import com.example.taxibill.API.APICallbacks;
import com.example.taxibill.API.APIStatus;
import com.example.taxibill.API.UserData;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class App extends Application implements CommonConstants, LifecycleObserver {
    //private FirebaseAnalytics mFirebaseAnalytics;
    public static long TIMEOUT = 60;
    public MyPrefs myPrefs;
    public static Gson gson = new Gson();

    int cacheSize = 10 * 1024 * 1024; // 10MB
    public boolean logoutRemote = false;
    public int sms = 0, location = 0;

    public static OkHttpClient okHttpClient;
    public static boolean isForeground = true, firebaseFetched = false, changeFromMinimize = true;
    ;
    public static List<Integer> beneficiary_colors = new ArrayList<>();

    static App app;

    public Activity mRunningActivity;
    public MyLifecycleCallbacks myLifecycleCallbacks;
    public String message = "";

    //public String blablaIv = BuildConfig.API_KEY;
    public String blablaIv ="fgvbhafgvbha8994";

    public static Handler handler1 = new Handler(Looper.getMainLooper());
    public Handler handler = new Handler(Looper.myLooper());

    public IntentFilter s_intentFilter = new IntentFilter();

    public OnAppStateChanged onAppStateChanged;

    public static boolean appStarted = false;
    public static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public MutableLiveData<Bundle> liveDataChatClose = new MediatorLiveData<>();


    APICallbacks apiCallbacks = new APICallbacks() {
        @Override
        public void taskProgress(String tag, int progress, Bundle bundle) {

        }

        @Override
        public void taskFinish(APIStatus apiStatus, String tag, String message, JSONObject objectRes, Bundle bundle) {

        }


    };


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };


    public static String
    addCharToStringUsingSubString(String str, char c,
                                  int pos) {
        return str.substring(0, pos) + c
                + str.substring(pos);
    }

    static String firstLetterWord(String str) {
        String result = "";

        // Traverse the string.
        boolean v = true;
        for (int i = 0; i < str.length(); i++) {
            // If it is space, set v as true.
            if (str.charAt(i) == ' ') {
                v = true;
            }

            // Else check if v is true or not.
            // If true, copy character in output
            // string and set v as false.
            else if (str.charAt(i) != ' ' && v == true) {
                result += (str.charAt(i));
                v = false;
            }
        }

        return result;
    }


    static int rev = 0;

    // Function to reverse the number
    static void reverse(int n) {

        if (n <= 0)
            return;

        int rem = n % 10;  // remainder

        rev = (rev * 10) + rem;

        reverse(n / 10);
    }

    @Override
    public void onCreate() {
        super.onCreate();



        /*FirebaseApp.initializeApp(this);
        FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
        String installationId = String.valueOf(firebaseInstallations.getId());
        firebaseInstallations.getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                if (task.isSuccessful()) {
                    // Successful
                    String installationId = task.getResult();

                } else {
                    // Failure
                    Exception exception = task.getException();

                }
            }
        });*/

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        getSharedPreferences("myloginapp", 0).edit().clear().apply();

        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        s_intentFilter.addAction( ".gotonext");
        s_intentFilter.addAction("closechat");
        s_intentFilter.addAction("kyc");
        // s_intentFilter.addAction(Freshchat.FRESHCHAT_USER_RESTORE_ID_GENERATED);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       /* new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, crashKey);*/
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        DroidNet.init(this);
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT * 5, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT * 5, TimeUnit.SECONDS)
                .cache(new Cache(getCacheDir(), cacheSize))
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        myPrefs = new MyPrefs(getApplicationContext(), SHARED_PREF_NAME);
        Bundle bundle = new Bundle();



        myLifecycleCallbacks = new MyLifecycleCallbacks();
        registerActivityLifecycleCallbacks(myLifecycleCallbacks);

        app = this;

       // ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

    }

    class MyLifecycleCallbacks implements ActivityLifecycleCallbacks {


        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

            // mRunningActivity = activity;
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

            isForeground = true;

            if (logoutRemote && App.getInstance().myPrefs.getBoolean(UserData.KEY_LOGGED_IN)) {
                AppFunctions.redirectToHomeToken(activity);
                if (!message.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    message = "";
                }
                logoutRemote = false;
            }

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

            isForeground = false;
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            //  mRunningActivity = null;


        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }

    }


    public static synchronized App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {


        try {
            onAppStateChanged.onForeground(new JSONObject());
            Map<String, String> articleParams = new HashMap<>();

//param keys and values have to be of String type
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            articleParams.put("Foreground", "" + hour + "-" + (hour + 1));
//up to 10 params can be logged with each event
            /*FlurryEventRecordStatus recordStatus = FlurryAgent.logEvent("AppStatus", articleParams);
            FlurryAgent.setUserId(App.getInstance().myPrefs.getStringNum(UserData.KEY_USER_ID));*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {


        Map<String, String> articleParams = new HashMap<>();

//param keys and values have to be of String type
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        articleParams.put("Background", "" + hour + "-" + (hour + 1));
//up to 10 params can be logged with each event
       /* FlurryEventRecordStatus recordStatus = FlurryAgent.logEvent("AppStatus", articleParams);
        FlurryAgent.setUserId(App.getInstance().myPrefs.getStringNum(UserData.KEY_USER_ID));*/

        // app moved to background
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onMoveToResume() {

        // app moved to foreground

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onMoveToPause() {


        // app moved to background
    }

}
