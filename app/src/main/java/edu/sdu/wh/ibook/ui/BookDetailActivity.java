package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.CommentAdapter;
import edu.sdu.wh.ibook.adapter.DetailAdapter;
import edu.sdu.wh.ibook.po.BookDetail;
import edu.sdu.wh.ibook.po.Comment;
import edu.sdu.wh.ibook.service.BookDetailJsoupHtml;
import edu.sdu.wh.ibook.service.IntroJsoupHtml;


/**
 *
 */
public class BookDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private String link;
    private TextView tv_bookName_code;
    private ListView lv_bookDetail;
    private String bookName_code;
    private List<BookDetail> bookDetails;
    private ProgressBar progressBar;
    private DetailAdapter detailAdapter;
    private Handler handler;
    private static int URL_WRONG=0,OK=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_bookdetail);
        Intent intent=getIntent();
        link=intent.getStringExtra("链接");
        bookName_code =intent.getStringExtra("书名");
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        lv_bookDetail.setOnItemClickListener(this);
    }

    private void initData() {
        progressBar.setVisibility(View.VISIBLE);
        Thread thread=new Thread(new LoadThread());
        thread.start();
    }

    private void initView() {
        tv_bookName_code = (TextView) findViewById(R.id.tv_bookDetailName_code);

        lv_bookDetail= (ListView) findViewById(R.id.lv_bookDetail);

        progressBar= (ProgressBar) findViewById(R.id.pb_loading);
        progressBar.setVisibility(View.INVISIBLE);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"链接错误！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        progressBar.setVisibility(View.INVISIBLE);
                        tv_bookName_code.setText(bookName_code);
                        detailAdapter=new DetailAdapter(getApplicationContext(),bookDetails);
                        lv_bookDetail.setAdapter(detailAdapter);
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(BookDetailActivity.this,PostCommentActivity.class);
        String [] marc_no=link.split("=");
        intent.putExtra("marc_no",marc_no[1]);
        this.startActivityForResult(intent,0);
    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            HttpClient client= IBookApp.getHttpClient();
            HttpGet get=new HttpGet(link);
            try {
                HttpResponse response=client.execute(get);
                String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                boolean flag=response.getStatusLine().getStatusCode()==200;
                if(flag)
                {
                    BookDetailJsoupHtml content=new BookDetailJsoupHtml(html);
                    bookDetails=content.getBookDetails();
                    Message msg=new Message();
                    msg.what=OK;
                    handler.sendMessage(msg);
                }
                else {
                    Message msg=new Message();
                    msg.what=URL_WRONG;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
