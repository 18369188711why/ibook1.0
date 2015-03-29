package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import edu.sdu.wh.ibook.R;


/**
 *
 */
public class HisDetialActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_hisdetial);
    }
}
