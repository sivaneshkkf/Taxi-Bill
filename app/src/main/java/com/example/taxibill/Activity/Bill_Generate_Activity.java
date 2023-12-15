package com.example.taxibill.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.taxibill.R;
import com.example.taxibill.databinding.ActivityBillGenerateBinding;

public class Bill_Generate_Activity extends AppCompatActivity {
    ActivityBillGenerateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBillGenerateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}