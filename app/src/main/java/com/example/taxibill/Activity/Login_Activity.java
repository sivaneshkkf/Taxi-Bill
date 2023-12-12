package com.example.taxibill.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taxibill.MainActivity;
import com.example.taxibill.Myutils.StatusBar;
import com.example.taxibill.R;
import com.example.taxibill.Utils.MyPrefs;
import com.example.taxibill.databinding.LoginActivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login_Activity extends AppCompatActivity {
    LoginActivityBinding binding;
    Activity activity;

    private String OTP;
    String phone;

    private boolean status = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = Login_Activity.this;
        Window w = getWindow();
        StatusBar.setColorStatusBar(w, R.color.white, activity);

        binding.signinbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Activity.this, Signin_Activity.class);
            startActivity(intent);
        });
        binding.btnlogin.setOnClickListener(v -> {
            phone = binding.tvuserName1.getText().toString();
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                /*token = task.getResult();*/
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
       /* if(!MyPrefs.getInstance(activity).getString(StringClass.name).isEmpty()){
            Intent intent=new Intent(activity, MainActivity.class);
            startActivity(intent);
        }*/
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Login_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}