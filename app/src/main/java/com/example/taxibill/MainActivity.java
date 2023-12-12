package com.example.taxibill;

import static com.example.taxibill.Fragment.Add_Fragment.linBtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Toast;

import com.example.taxibill.Fragment.Add_Fragment;
import com.example.taxibill.Fragment.Home_Fragment;
import com.example.taxibill.Myutils.StatusBar;
import com.example.taxibill.databinding.ActivityMainBinding;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.YearMonth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Activity activity;
    Home_Fragment homeFragment=new Home_Fragment();
    Add_Fragment addFragment=new Add_Fragment();
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        binding.bottomNavigationView.setBackground(null);

        Window w=getWindow();
        StatusBar.setColorStatusBar(w,activity);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_home,homeFragment).commit();

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home,homeFragment).commit();
                }if(item.getItemId()==R.id.shopping){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_home,homeFragment).commit();
                }
                return true;
            }
        });

        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_home,addFragment).commit();
            }
        });

        rootView = findViewById(android.R.id.content);

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private int previousHeight;

            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                Toast.makeText(activity, ""+heightDiff, Toast.LENGTH_SHORT).show();
                if (heightDiff > 200) { // Adjust the threshold as needed
                    binding.bottomAppBar.setVisibility(View.GONE);
                    binding.startBtn.setVisibility(View.GONE);
                    if(linBtn!=null){
                        linBtn.setVisibility(View.GONE);
                    }
                } else {
                    binding.bottomAppBar.setVisibility(View.VISIBLE);
                    binding.startBtn.setVisibility(View.VISIBLE);
                    if(linBtn!=null){
                        linBtn.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);


    }

    private boolean isKeyboardOpen() {
        // You can define your own threshold to determine if the keyboard is open
        int screenHeight = rootView.getHeight();
        int keypadHeight = screenHeight - rootView.getHeight();

        return keypadHeight > screenHeight * 0.15;
    }

    private void onKeyboardOpened() {

    }

    private void onKeyboardClosed() {

    }
}