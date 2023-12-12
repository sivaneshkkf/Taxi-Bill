package com.example.taxibill.Myutils;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.taxibill.R;

public class StatusBar {
    public static void setColorStatusBar(Window w, Activity activity){
       w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.TRANSPARENT);
        android.graphics.drawable.Drawable background = activity.getResources().getDrawable(R.drawable.statusbar_bg);
        w.setBackgroundDrawable(background);
    }

    public static void setColorStatusBar(Window w, int color,Activity activity){
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(ContextCompat.getColor(activity, color));
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


}
