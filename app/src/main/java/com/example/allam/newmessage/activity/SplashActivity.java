package com.example.allam.newmessage.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.allam.newmessage.R;
import com.example.allam.newmessage.Utiles.Utiles;

public class SplashActivity extends AppCompatActivity {
    private Runnable mRunnable;
    private Handler mHandler;
    private ImageView mLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLogoImageView = (ImageView) findViewById(R.id.splash_icon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        mLogoImageView.startAnimation(animation);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (Utiles.getPhoneNumber(getApplicationContext()) != null) {
                    Intent intent = new Intent(SplashActivity.this, MessagesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

        };
        mHandler.postDelayed(mRunnable, 3000);

    }
}
