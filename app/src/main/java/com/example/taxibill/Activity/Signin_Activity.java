package com.example.taxibill.Activity;



import static com.example.taxibill.Utils.InputValidator.isValidEmail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxibill.MainActivity;
import com.example.taxibill.Myutils.StatusBar;
import com.example.taxibill.R;
import com.example.taxibill.Utils.MyPrefs;
import com.example.taxibill.databinding.SigninActivityBinding;

import java.util.Calendar;

public class Signin_Activity extends AppCompatActivity {
    SigninActivityBinding binding;
    Activity activity;
    String name,mail,PhNo,Dob,Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=SigninActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity=this;
        Window w = getWindow();
        StatusBar.setColorStatusBar(w, R.color.white, activity);

        binding.tvEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                if (!email.isEmpty() && !isValidEmail(email)) {
                    binding.tvEmail.setError("Invalid email address");
                } else {
                    binding.tvEmail.setError(null);
                    mail=s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(binding.tvName.getText().toString().isEmpty()){
                    binding.tvName.setError("Username Field should not be empty");
                }else if (mail.equals("") && isValidEmail(mail)) {
                    binding.tvEmail.setError("Invalid Mail ID");
                }else if (binding.tvPhoneNumber.getText().toString().trim().length()!=10){
                    binding.tvPhoneNumber.setError("Invalid Mobile Number");
                }else if (binding.tvDOB.getText().toString().trim().isEmpty()){
                    binding.tvDOB.setError("Please Select Your DOB");
                }
                else{
                    name = binding.tvName.getText().toString();
                    PhNo=binding.tvPhoneNumber.getText().toString();
                    Dob=binding.tvDOB.getText().toString();
                  /*  MyPrefs.getInstance(activity).putString(StringClass.name,name);
                    MyPrefs.getInstance(activity).putString(StringClass.email,mail);
                    MyPrefs.getInstance(activity).putString(StringClass.phno,PhNo);
                    MyPrefs.getInstance(activity).putString(StringClass.dob,Dob);*/
                    Intent intent=new Intent(Signin_Activity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        /*binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signin_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });*/

        binding.tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog dpg = new DatePickerDialog(binding.getRoot().getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        binding.tvDOB.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                dpg.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* if(!MyPrefs.getInstance(activity).getString(StringClass.name).isEmpty()){
            Intent intent=new Intent(activity,MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
}