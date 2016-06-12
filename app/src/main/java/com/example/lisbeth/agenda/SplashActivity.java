package com.example.lisbeth.agenda;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    private Thread threadSplash;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        star();
    }

    private void star() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        linearLayout.clearAnimation();
        linearLayout.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        animation.reset();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.clearAnimation();
        imageView.startAnimation(animation);

        threadSplash = new Thread() {
            @Override
            public void run() {
                try {
                    int c = 0;
                    while (c < 4000) {
                        sleep(150);
                        c += 150;

                    }

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    //Do nothing
                } finally {
                    SplashActivity.this.finish();
                }
            }
        };
        threadSplash.start();

    }
}
