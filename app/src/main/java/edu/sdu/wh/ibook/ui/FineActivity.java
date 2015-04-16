package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.sdu.wh.ibook.R;

public class FineActivity extends Activity {
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine);
        link=getIntent().getStringExtra("链接");
        Toast.makeText(getApplicationContext(), link, Toast.LENGTH_SHORT).show();

    }

}
