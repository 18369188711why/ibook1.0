package edu.sdu.wh.ibook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.User;
import edu.sdu.wh.ibook.service.UserJsoupHtml;
import edu.sdu.wh.ibook.view.BorrowHisFragment;
import edu.sdu.wh.ibook.view.BorrowNowFragment;
import edu.sdu.wh.ibook.view.BottomBars;
import edu.sdu.wh.ibook.view.MyCommentFragment;
import edu.sdu.wh.ibook.view.SearchFragment;


public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private List<BottomBars> tabIndicators;
    private Date lastTime=null;
    private Handler handler;
    private DefaultHttpClient client;
    private String userNumber;

    private static String URL_INFO="http://202.194.40.71:8080/reader/redr_info.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
        initData();

        load();
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        initEvents();
    }

    private void load() {

        Thread loadThread=new Thread(new LoadThread());
        loadThread.start();
    }


    private void initEvents() {
        for(BottomBars tab:tabIndicators)
        {
            tab.setOnClickListener(this);
        }

        viewPager.setOnPageChangeListener(this);
    }

    private void initData() {

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    case 1:
                        Toast.makeText(getApplicationContext(),"加载数据成功",Toast.LENGTH_SHORT).show();
                }
            }
        };

        Intent intent=getIntent();
        userNumber=intent.getStringExtra("UserNumber");
        adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public long getItemId(int position) {
                return super.getItemId(position);
            }
        };
    }

    private void initView() {
        viewPager= (ViewPager) findViewById(R.id.id_viewPager);

        tabIndicators=new ArrayList<BottomBars>();
        BottomBars one= (BottomBars) findViewById(R.id.indicator_one);
        tabIndicators.add(one);
        BottomBars two= (BottomBars) findViewById(R.id.indicator_two);
        tabIndicators.add(two);
        BottomBars three= (BottomBars) findViewById(R.id.indicator_three);
        tabIndicators.add(three);
        BottomBars four= (BottomBars) findViewById(R.id.indicator_four);
        tabIndicators.add(four);
        one.setIconAlpha(1.0f);


        fragments=new ArrayList<Fragment>();

        SearchFragment sfragment=new SearchFragment();
        fragments.add(sfragment);
        BorrowNowFragment bnFragment=new BorrowNowFragment();
        fragments.add(bnFragment);
        BorrowHisFragment bsFragment=new BorrowHisFragment();
        fragments.add(bsFragment);
        MyCommentFragment mFragment=new MyCommentFragment();
        fragments.add(mFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {
        if (v > 0)
        {
            BottomBars left = tabIndicators.get(i);
            BottomBars right = tabIndicators.get(i + 1);
            left.setIconAlpha(1 - v);
            right.setIconAlpha(v);
        }

    }

    @Override
    public void onPageSelected(int i) {
        try {
            switch (i)
                {
                    case 0:
                        Thread.sleep(200);
                        break;
                    case 1:
                        ((BorrowNowFragment)fragments.get(1)).initData();
                        break;
                    case 2:
                        ((BorrowHisFragment)fragments.get(2)).initData();
                        break;
                    case 3:
                        ((MyCommentFragment)fragments.get(3)).initData();
                        break;
                }
        }
        catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //底部导航点击跳转页面效果
    @Override
    public void onClick(View view) {
        resetOtherTabs();
        switch (view.getId())
        {
            case R.id.indicator_one:
                tabIndicators.get(0).setIconAlpha(1.0f);
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.indicator_two:
                tabIndicators.get(1).setIconAlpha(1.0f);
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.indicator_three:
                tabIndicators.get(2).setIconAlpha(1.0f);
                viewPager.setCurrentItem(2,false);
                break;
            case R.id.indicator_four:
                tabIndicators.get(3).setIconAlpha(1.0f);
                viewPager.setCurrentItem(3,false);
                break;
        }
    }

    private void resetOtherTabs() {
        for(BottomBars tab:tabIndicators)
        {
            tab.setIconAlpha(0);
        }
    }

    //进入User界面
    public void goUser(View v)
    {
        if(IBookApp.getUser() == null )
        {
          load();
        }else{

                Intent intent=new Intent(MainActivity.this,UserActivity.class);
                this.startActivityForResult(intent, 0);

        }
    }


    /*实现连续点击两次返回*/
    @Override
    public void onBackPressed() {
        Date currTime=new Date();
        if(lastTime==null||(currTime.getTime()-lastTime.getTime())>3000)
        {
            lastTime=currTime;
            Toast.makeText(getApplicationContext(), "再次点击退出程序", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private class LoadThread implements Runnable {
        String html;
        @Override
        public void run() {
            client= IBookApp.getHttpClient();
            HttpGet get=new HttpGet(URL_INFO);

            boolean flag;
            try {
                HttpResponse response=client.execute(get);
                flag=response.getStatusLine().getStatusCode()==200;

                html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                if(!flag){
                    Message msg=new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                }
                else
                {
                    UserJsoupHtml userJsoupHtml=new UserJsoupHtml(html);
                    userJsoupHtml.parseHtml();
                    User user=userJsoupHtml.getUser();
                    IBookApp.setUser(user);

                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
