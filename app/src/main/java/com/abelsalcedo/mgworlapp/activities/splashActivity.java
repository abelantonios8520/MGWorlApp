package com.abelsalcedo.mgworlapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.abelsalcedo.mgworlapp.R;
import com.airbnb.lottie.LottieAnimationView;

public class splashActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAnimation = findViewById(R.id.animationfull);
        mTextAnimation = findViewById(R.id.textAnimation);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashActivity.this, MainActivityMapa.class);
                startActivity(intent);
                finish();
            }
        },3000);
        mAnimation.playAnimation();

    }
}