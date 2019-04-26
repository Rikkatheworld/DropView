package com.example.dropview;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//wowowowowo
        final DropView drop = findViewById(R.id.dropview);
        drop.start();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 9);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animation.getAnimatedValue();
                drop.setText(i);
            }
        });
        valueAnimator.start();

        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(9, 20);
        valueAnimator1.setDuration(600);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animation.getAnimatedValue();
                drop.setText(i);
            }
        });
        valueAnimator1.setStartDelay(1600);
        valueAnimator1.start();

        ValueAnimator valueAnimator3 = ValueAnimator.ofInt(20, 50);
        valueAnimator3.setDuration(600);
        valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animation.getAnimatedValue();
                drop.setText(i);
            }
        });
        valueAnimator3.setStartDelay(2600);
        valueAnimator3.start();

        ValueAnimator valueAnimator4 = ValueAnimator.ofInt(50, 100);
        valueAnimator4.setDuration(500);
        valueAnimator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animation.getAnimatedValue();
                drop.setText(i);
            }
        });
        valueAnimator4.setStartDelay(3600);
        valueAnimator4.start();


    }
}
