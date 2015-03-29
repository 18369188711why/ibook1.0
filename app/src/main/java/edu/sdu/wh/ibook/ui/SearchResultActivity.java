package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.SearchResultAdapter;
import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.service.SearchJsoupHtml;

public class SearchResultActivity extends Activity implements AdapterView.OnItemClickListener {
    private IBookApp bookApp;
    private Handler handler;
    private static String url="http://202.194.40.71:8080/opac/openlink.php?";
    private ListView lv_bookList;
    private String type;
    private String name;
    private List<BookInfo> bookInfos;
    private static Map<String,String> typeValue=new HashMap<String, String>(){
        {
            put("题名","title");
            put("责任者","author");
            put("主题词","keyword");
            put("ISBN/ISSN","isbn");
            put("订购号","asordno");
            put("分类号","coden");
            put("索书号","callno");
            put("出版社","publisher");
            put("丛书名","series");
            put("题名拼音","tpinyin");
            put("责任者拼音","apinyin");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_result);

        Intent intent=getIntent();

        String typeName=intent.getStringExtra("搜索方法");
        type = typeValue.get(typeName);
        name=intent.getStringExtra("书名");

        initData();
        initView();
        initEvent();

        load();
    }

    private void load() {

        Thread loadThread=new Thread(new LoadThread());
        loadThread.start();
    }

    private void initEvent() {
        lv_bookList.setOnItemClickListener(this);
    }

    private void initView() {
        lv_bookList= (ListView) findViewById(R.id.lv_searchResult);



    }

    private void initData() {
        bookApp= (IBookApp) getApplication();
        handler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what)
                {
                    case 0:

                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
//                        SearchResultAdapter adapter=new SearchResultAdapter(getApplicationContext(),
//                                bookInfos);
//                        lv_bookList.setAdapter(adapter);
                        break;
                    case 2:

                        break;
                }

            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            boolean flag;
            HttpClient client =IBookApp.getHttpClient();
            String searchurl = url
                    + "strSearchType="
                    +type
                    +"&match_flag=forward&historyCount=1&strText="
                    + name
                    +"&doctype=ALL&displaypg=20&showmode=list&sort=CATA_DATE&orderby=desc&dept=ALL";
            HttpGet get = new HttpGet(searchurl);
            try {
                HttpResponse response = client.execute(get);
                flag = response.getStatusLine().getStatusCode() == 200;
                if (!flag) {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else {
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    System.out.println(html);

//                    bookInfos=new SearchJsoupHtml(html).getBookInfos();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }
}
