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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.DetailAdapter;
import edu.sdu.wh.ibook.po.BookDetail;
import edu.sdu.wh.ibook.util.ToDocument;
import edu.sdu.wh.ibook.view.LoadingDialog;


/**
 *
 */
public class BookDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private String link;
    private TextView tv_bookName_code;
    private ListView lv_bookDetail;
    private LoadingDialog loading;

    private String bookName_code;
    private List<BookDetail> bookDetails;
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
        loading.show();
        Thread thread=new Thread(new LoadThread());
        thread.start();
    }

    private void initView() {
        tv_bookName_code = (TextView) findViewById(R.id.tv_bookDetailName_code);

        lv_bookDetail= (ListView) findViewById(R.id.lv_bookDetail);

        loading=new LoadingDialog(this,"Loading.....");

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"链接错误！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loading.dismiss();
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
                    parseHtml(html);
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

    public void parseHtml(String html) {

        bookDetails =new ArrayList<BookDetail>();
        Elements elements= ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"content_item\"]").
                select("div[id=\"tabs2\"]").
                select("div[id=\"tab_item\"]").
                select("table[id=\"item\"]").
                select("tbody").
                select("tr[class=\"whitetext\"]");

        for(int i=0;i<elements.size();i++)
        {
            BookDetail bookDetail=new BookDetail();
            Elements contents=elements.get(i).select("td");
            bookDetail.setBarcode(contents.get(1).text());
            bookDetail.setYear(contents.get(2).text());
            bookDetail.setSchool_place(contents.get(3).text());
            bookDetail.setStatus(contents.get(4).text());
            bookDetails.add(bookDetail);
        }
    }
}
