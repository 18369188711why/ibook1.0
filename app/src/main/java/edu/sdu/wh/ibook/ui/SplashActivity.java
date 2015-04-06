package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import edu.sdu.wh.ibook.R;


/**
 *
 */
public class SplashActivity extends Activity {
    private final int SPLASH_DELAY_LENGTH= 4000;
    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        ivSplash = (ImageView) findViewById(R.id.startImage);

        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(SPLASH_DELAY_LENGTH);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation arg0) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }

            public void onAnimationRepeat(Animation arg0) {

            }

            public void onAnimationStart(Animation arg0) {

            }

        });

        ivSplash.setAnimation(animation);
    }
}
