package com.example.nisha.animations;

import android.animation.AnimatorSet;
import android.app.Notification;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    int x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText("5");

        /*textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                *//*textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
*//*
                int[] locations = new int[2];
                textView.getLocationOnScreen(locations);
                x = locations[0];
                y = locations[1];


            }
        });
        AnimationSet am = new AnimationSet(true);
        am.setFillEnabled(true);
        am.setInterpolator(new BounceInterpolator());
        TranslateAnimation ta = new TranslateAnimation(-300, 100, 0, 0);
        ta.setDuration(2000);
        ta.setFillEnabled(true);
        am.addAnimation(ta);

        TranslateAnimation ta2 = new TranslateAnimation(x, 0, 0, y+50);
        ta2.setDuration(2000);
        ta2.setStartOffset(2000); // allowing 2000 milliseconds for ta to finish
        ta2.setFillEnabled(true);
        am.addAnimation(ta2);

        textView.setAnimation(am);*/
    }

    public void click(View view) {
        textView.setText("1");
    }
}
