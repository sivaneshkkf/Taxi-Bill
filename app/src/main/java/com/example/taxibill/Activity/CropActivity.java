package com.example.taxibill.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxibill.R;
import com.example.taxibill.Utils.ResizeImage;
import com.example.taxibill.databinding.ActivityCropBinding;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;

public class CropActivity extends AppCompatActivity {

    ActivityCropBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.DKGRAY);
        }
        super.onCreate(savedInstanceState);

        binding = ActivityCropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // setContentView(R.layout.activity_crop);

        //   final ImageCropView imageCropView = findViewById(R.id.imageCropView);
        binding.imageCropView.setGridInnerMode(ImageCropView.GRID_ON);
        binding.imageCropView.setGridOuterMode(ImageCropView.GRID_ON);
        final int arWidth = getIntent().getIntExtra("arWidth", 4);
        final int arHeight = getIntent().getIntExtra("arHeight", 3);
        binding.imageCropView.setAspectRatio(arWidth, arHeight);
        String filePath = getIntent().getStringExtra("file");
        Bitmap bitmap = ResizeImage.getBitmapFromFile(filePath);
        binding.imageCropView.setImageBitmap(bitmap);
        if (arHeight != arWidth) {
            findViewById(R.id.radioButtonPortrait).setVisibility(View.VISIBLE);
            findViewById(R.id.radioButtonLandscape).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.radioButtonLandscape).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageCropView.setAspectRatio(arWidth, arHeight);
            }
        });
        findViewById(R.id.radioButtonPortrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageCropView.setAspectRatio(arHeight, arWidth);
            }
        });
        binding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.imageCropView.isChangingScale()) {
                    String filePath = "";
                    Bitmap bitmap = binding.imageCropView.getCroppedImage();
                    if (bitmap != null) {

                        if (getIntent().getBooleanExtra("base64", false)) {
                            filePath = "data:image/jpg;base64," + ResizeImage.bitmapToBase64(bitmap, getIntent().getIntExtra("size", 1000));
                        } else {
                            filePath = ResizeImage.saveBitmapToFile(bitmap, getIntent().getIntExtra("size", 1440));

                        }
                        Bundle bundle = new Bundle();
                        Log.i("Crop", filePath);
                        Intent intent = new Intent();
                        intent.putExtra("file", filePath);
                        bundle.putString("CropedFile", filePath);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Crop fail", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}