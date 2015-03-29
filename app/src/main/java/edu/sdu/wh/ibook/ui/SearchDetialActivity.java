package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.io.InputStream;

import edu.sdu.wh.ibook.R;


/**
 *
 */
public class SearchDetialActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_searchdetial);


    }
}
