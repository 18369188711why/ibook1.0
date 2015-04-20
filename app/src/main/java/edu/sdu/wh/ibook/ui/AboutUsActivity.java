package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import edu.sdu.wh.ibook.R;


public class AboutUsActivity extends Activity {

    TextView tv_about_us;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_us);
        tv_about_us= (TextView) findViewById(R.id.tv_about_us);
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
