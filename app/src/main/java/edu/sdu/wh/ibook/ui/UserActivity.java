package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;


/**
 *
 */
public class UserActivity extends Activity implements View.OnClickListener{
    private ImageView iv_userHead;
    private TextView  tv_userName,tv_userNum;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        iv_userHead.setOnClickListener(this);
    }

    private void initData() {
        iv_userHead.setImageDrawable(getResources().getDrawable(R.drawable.user_head));
        String name = IBookApp.getUser().getUsername();
        tv_userName.setText(name);
        String num= IBookApp.getUser().getUsernumber();
        tv_userNum.setText(num);
    }

    private void initView()
    {
        iv_userHead= (ImageView) findViewById(R.id.iv_userHead);
        tv_userName= (TextView) findViewById(R.id.tv_userName);
        tv_userNum= (TextView) findViewById(R.id.tv_userNum);
    }

    @Override
    public void onClick(View view) {
        //实现点击图片调用相册的功能
       switch (view.getId())
       {
           case R.id.iv_userHead:

               break;
       }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UserActivity.this,MainActivity.class);
        UserActivity.this.startActivity(intent);
        super.onBackPressed();
    }
}
