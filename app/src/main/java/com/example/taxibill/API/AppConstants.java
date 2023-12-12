package com.example.taxibill.API;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.taxibill.Utils.CommonConstants;


public class AppConstants implements CommonConstants {

    public static double MIN_AMT = 100, MAX_AMT = 500000, MAX_AMT_CC = 200000;

    public static String topicAll = "", topicWhitelist = "whitelisted";

    String[] TRIP_STATUSES = new String[]{};

    public static String blabla = "inputdata";
    public static String URL_SCHEME = "https://";
    public static int API_VERSION_CODE = 75;
    public static String API_VERSION = "ve" + API_VERSION_CODE;
    //public static String HOST = "http://192.168.100.52/eventwise/admin/";
    //public static String HOST = "https://eazyevent.akprojects.co/";
    public static String HOST = "https://petcare.akprojects.co/";
    public static String Image = "https://petcare.akprojects.co/";
    //public static String HOST = "https://mitezy.com/";
    public static String API_URL = "api/";
    public static String PAY_URL = "payment/";
    public static String MAIN_URL = HOST + API_URL ;

    public static String sampleImage = "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg";
    public static boolean shouldEncrypt = true;
    public static float weightText = 0.25f, weightFile = 1f;
    public static LinearLayout.LayoutParams paramsChat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, weightFile);
    public static int startTerms = 38, endTerms = 52, startPrivacy = 57, endPrivacy = 71;
    public static LinearLayout.LayoutParams paramsChatText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, weightText);
    public static LinearLayout.LayoutParams paramsChatFile = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, weightFile);

}
