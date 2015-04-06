package edu.sdu.wh.ibook.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.util.CheckNetMethod;


import static edu.sdu.wh.ibook.R.*;


public class LoginActivity extends Activity {

    private Button btnLogin;
    private EditText et_user, et_password, et_captcha;
    private ImageView iv_captcha;
    private String userNumber, password,captcha;
    private ProgressBar pb_login;
    private Handler handler;
    private SharedPreferences sp;

    private DefaultHttpClient client;

    private static int GET_CAPTCHA_OK=0,GET_CAPTCHA_WRONG=1,
            LOGIN_OK=2,LOGIN_WRONG=3,URL_CONNECT_OUTTIME=4,USER_PASSWORD_NULL=5,CAPTCHA__NOT_NUMBER=6;
    private static String URL_CAPTCHA="http://202.194.40.71:8080/reader/captcha.php",
                          URL_LOGIN="http://202.194.40.71:8080/reader/redr_verify.php";
    /*创建界面，获取需要操作的组件*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      设置无标题全屏的界面
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout.activity_login);
        sp = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        sp.edit().putBoolean("AUTO_ISCHECK", true).apply();

        checkInternet();
        initView();
        initData();
        initEvent();

    }

    private void initEvent() {
        btnLogin.setOnClickListener(new BtnLoginOnClickListener());
        iv_captcha.setOnClickListener(new CaptchaListener());
    }

    private void initData() {
        client = IBookApp.getHttpClient();

        iv_captcha.setImageResource(drawable.captcha);

        et_user.setText(sp.getString("USER_NUMBER",""));
        et_password.setText(sp.getString("PASSWORD",""));

        userNumber = et_user.getText().toString();
        password = et_password.getText().toString();
        captcha = et_captcha.getText().toString();

        //使用Handler机制，对于不同的信息进行不同的反应
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        pb_login.setVisibility(View.GONE);
                        iv_captcha.setImageDrawable((Drawable) msg.obj);
                        break;
                    case 1:
                        pb_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "URL验证失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        pb_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("UserNumber",userNumber);
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NUMBER", userNumber);
                        editor.putString("PASSWORD",password);
                        editor.apply();
                        break;
                    case 3:
                        pb_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "登陆失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        pb_login.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "连接超时,请重新登录！", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(),"账号密码不能为空！",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getApplicationContext(),"验证码为2-3位数字！",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void initView() {
        et_user = (EditText) findViewById(id.userNameText);
        et_user.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_password = (EditText) findViewById(id.passwordText);
        et_captcha = (EditText) findViewById(id.et_captcha);
        et_captcha.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        pb_login = (ProgressBar) findViewById(id.pb_login);

        pb_login.setVisibility(View.GONE);

        iv_captcha= (ImageView) findViewById(R.id.iv_captcha);
        btnLogin = (Button) findViewById(R.id.btn_login);
    }



    //检查网络连接状况，未连接进行设置，连接进行下一步操作
    private void checkInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //如果3G、wifi、2G等网络状态是连接的，则退出，否则显示提示信息进入网络设置界面
        if (!(mobile == NetworkInfo.State.CONNECTED ||
                mobile == NetworkInfo.State.CONNECTING||
                wifi == NetworkInfo.State.CONNECTED ||
                wifi == NetworkInfo.State.CONNECTING))
        {
            CheckNetMethod.setNetworkMethod(LoginActivity.this);
        }else
        {
            Toast.makeText(getApplicationContext(),"INTERNET",Toast.LENGTH_SHORT).show();
        }
    }



    /*点击登录按钮，登录进入系统*/
    public class BtnLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            userNumber=et_user.getText().toString();
            password=et_password.getText().toString();
            captcha= et_captcha.getText().toString();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(et_captcha, InputMethodManager.SHOW_FORCED);
            imm.hideSoftInputFromWindow(et_captcha.getWindowToken(), 0);

            pb_login.setVisibility(View.VISIBLE);

            Thread loginThread = new Thread(new LoginThread());
            loginThread.start();
        }
    }

    private class LoginThread extends Thread {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            if(!isNumber(captcha)){
                Message msg=new Message();
                msg.what=CAPTCHA__NOT_NUMBER;
                handler.sendMessage(msg);
                return;
            }
            List<NameValuePair> parmas=new ArrayList<NameValuePair>();
            parmas.add(new BasicNameValuePair("number",userNumber));
            parmas.add(new BasicNameValuePair("passwd",password));
            parmas.add(new BasicNameValuePair("captcha",captcha));
            parmas.add(new BasicNameValuePair("select","cert_no"));
            parmas.add(new BasicNameValuePair("returnUrl",""));
            try {
                HttpPost post=new HttpPost(URL_LOGIN);
                post.setEntity(new UrlEncodedFormEntity(parmas, HTTP.UTF_8));

                post.addHeader("Host","202.194.40.71:8080");
                post.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                post.addHeader("Accept-Language","zh-CN");
                post.addHeader("Accept-Encoding	gzip","deflate");
                post.addHeader("Connection","Keep-Alive");
                post.addHeader("Cache-Control","no-cache");

                HttpResponse response=client.execute(post);

                switch (response.getStatusLine().getStatusCode()){
                    case 302:
                        Message msg=new Message();
                        msg.what = LOGIN_OK;
                        handler.sendMessage(msg);
                        break;
                    case 200:
                        Message msg0=new Message();
                        msg0.what=LOGIN_WRONG;
                        handler.sendMessage(msg0);
                        break;
                }
            }catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
           }
        }

        private boolean isNumber(String captcha) {
            for(int i=0;i<captcha.length();i++)
            {
                if(captcha.charAt(i)<'0' || captcha.charAt(i)>'9')
                    return false;
            }
            return true;
        }

    }

    private class CaptchaListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            userNumber = et_user.getText().toString();
            password = et_password.getText().toString();

            pb_login.setVisibility(View.VISIBLE);

            Thread captchaThread = new Thread(new CaptchaThread());
            captchaThread.start();
        }
    }

    /*获取验证码图像线程*/
    @SuppressWarnings("deprecation")
    private class CaptchaThread implements Runnable {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            boolean flag;
            if(userNumber==null||password==null){
                Message msg=new Message();
                msg.what=USER_PASSWORD_NULL;
                handler.sendMessage(msg);
            }

            String captcha=URL_CAPTCHA+"?code="+userNumber;
            HttpGet get=new HttpGet(captcha);
            get.addHeader("Accept","*/*");
            get.addHeader("Referer","http://202.194.40.71:8080/reader/captcha.php");
            get.addHeader("Accept-Language","zh-CN");
            get.addHeader("Accept-Encoding	gzip","deflate");
            get.addHeader("Host","202.194.40.71:8080");
            get.addHeader("Connection","Keep-Alive");

            InputStream html = null;

            try {
                HttpResponse response = client.execute(get);
                flag = response.getStatusLine().getStatusCode() == 200;
                HttpEntity entity = response.getEntity();

                CookieStore cookieStore = client.getCookieStore();

                IBookApp.setCookieStore(cookieStore);

                html = entity.getContent();
                if (!flag) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
                    Message msg = new Message();
                    msg.what = GET_CAPTCHA_WRONG;
                    msg.obj = drawable;
                    handler.sendMessage(msg);
                } else {
                    Bitmap bm_captcha = BitmapFactory.decodeStream(html);
                    Drawable drawable = new BitmapDrawable(bm_captcha);
                    Message msg = new Message();
                    msg.what = GET_CAPTCHA_OK;
                    msg.obj = drawable;
                    handler.sendMessage(msg);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (html != null) {
                    try {
                        html.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
