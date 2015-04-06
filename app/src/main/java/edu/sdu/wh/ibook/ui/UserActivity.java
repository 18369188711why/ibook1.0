package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;


/**
 *
 */
public class UserActivity extends Activity {
    private TextView  tv_userName,tv_userNum,tv_msg;
    private View v_search_his,v_asord,v_preg,v_fine;

    //搜索历史
    private static String histUrl="http://202.194.40.71:8080/reader/search_hist.php";
    //荐购历史
    private static String asordUrl="http://202.194.40.71:8080/reader/asord_lst.php";
    //预约信息
    private static String pregUrl="http://202.194.40.71:8080/reader/preg.php";
    //违章缴费
    private static String fineUrl="http://202.194.40.71:8080/reader/fine_pec.php";


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
        v_search_his.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,SearchHisActivity.class);
                intent.putExtra("链接",histUrl);
                UserActivity.this.startActivityForResult(intent,0);
            }
        });

        v_asord.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,AsordActivity.class);
                intent.putExtra("链接",asordUrl);
                UserActivity.this.startActivityForResult(intent,0);
            }
        });

        v_preg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,PregActivity.class);
                intent.putExtra("链接",pregUrl);
                UserActivity.this.startActivityForResult(intent,0);
            }
        });

        v_fine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserActivity.this,FineActivity.class);
                intent.putExtra("链接",fineUrl);
                UserActivity.this.startActivityForResult(intent,0);
            }
        });
    }


    private void initData() {
        String name = IBookApp.getUser().getUsername();
        tv_userName.setText(name);
        String num= IBookApp.getUser().getUsernumber();
        tv_userNum.setText(num);
        String msg= IBookApp.getUser().getMsg();
        tv_msg.setText(msg);

    }

    private void initView()
    {
        tv_userName= (TextView) findViewById(R.id.tv_userName);
        tv_userNum= (TextView) findViewById(R.id.tv_userNum);
        tv_msg= (TextView) findViewById(R.id.tv_msg);
        v_search_his=findViewById(R.id.v_search_his);
        v_asord=findViewById(R.id.v_asord);
        v_preg=findViewById(R.id.v_preg);
        v_fine=findViewById(R.id.v_fine);
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UserActivity.this,MainActivity.class);
        UserActivity.this.startActivity(intent);
        super.onBackPressed();
    }
}
