package com.example.taxibill.Myutils;

import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBar_increase {
    public static Handler handler=new Handler();

    public static void Increase(int percentage, double income, ProgressBar progressBar, TextView textView, double minusvalue, long delayMillis){


        for(int i=0;i<=percentage;i++){
            final int iteration=i;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(iteration);
                    textView.setText("₹"+Math.round(income*(iteration-minusvalue)/100));

                }
            },i * delayMillis);
        }

    }

    public static void Increase(int percentage, ProgressBar progressBar, TextView textView, long delayMillis){


        for(int i=0;i<=percentage;i++){
            final int iteration=i;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(iteration);
                    textView.setText(""+iteration+"%");

                }
            },i * delayMillis);
        }

    }

    public static void Increase(int value,int percentage, ProgressBar progressBar,long delayMillis){
        int val=value;
        for (int i=val; i<=percentage;i++){
            final int iteration=i;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(iteration);
                }
            },i * delayMillis);
        }
    }
}
