package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;

public class PostCommentActivity extends Activity implements View.OnClickListener {
    private EditText et_comment;
    private Button btn_post;
    private String comment,marc_no;
    private Handler handler;
    private static String url="http://202.194.40.71:8080/opac/ajax_review_man.php";
    private static int URL_WRNG=0,OK=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_comment);

        Intent intent=getIntent();
        marc_no=intent.getStringExtra("marc_no");
        initView();
        initEvent();
    }

    private void initEvent() {
        btn_post.setOnClickListener(this);
    }

    private void initView() {
        et_comment= (EditText) findViewById(R.id.et_comment);
        btn_post= (Button) findViewById(R.id.btn_post);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"已发表过对此书的评论或者发表失败！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();
                        break;
                }
                finish();
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        comment=et_comment.getText().toString();
        if(comment==null)
        {
            Toast.makeText(getApplicationContext(),"未发表内容！",Toast.LENGTH_SHORT).show();
        }else {
             Thread thread=new Thread(new PostThread());
             thread.start();
        }
    }

    private class PostThread implements Runnable {
        @Override
        public void run() {
            try {
                DefaultHttpClient client= IBookApp.getHttpClient();
                HttpPost post=new HttpPost(url);
                List<NameValuePair> parmas=new ArrayList<NameValuePair>();
                parmas.add(new BasicNameValuePair("action","add"));
                parmas.add(new BasicNameValuePair("marc_no",marc_no));
                System.out.println(marc_no);
                parmas.add(new BasicNameValuePair("review",comment));

                post.setEntity(new UrlEncodedFormEntity(parmas, HTTP.UTF_8));

                HttpResponse response=client.execute(post);
                boolean flag=response.getStatusLine().getStatusCode()==302;
                if(flag) {
                    Message msg = new Message();
                    msg.what = OK;
                    handler.sendMessage(msg);
                }
                else
                {
                    Message msg=new Message();
                    msg.what=URL_WRNG;
                    handler.sendMessage(msg);
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
