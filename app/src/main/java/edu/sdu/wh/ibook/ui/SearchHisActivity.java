package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.SearchHisAdapter;
import edu.sdu.wh.ibook.po.SearchHis;
import edu.sdu.wh.ibook.util.ToDocument;
import edu.sdu.wh.ibook.view.LoadingDialog;

public class SearchHisActivity extends Activity {

    ListView lv_search_his;
    LoadingDialog loading;

    Handler handler;
    SearchHisAdapter adapter;

    List<SearchHis> searchHises;

    static int PREG_NULL=0,URL_WRONG=1,OK=2;

    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_his);
        link=getIntent().getStringExtra("链接");
        lv_search_his= (ListView) findViewById(R.id.lv_search_his);
        lv_search_his.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        loading=new LoadingDialog(this,"Loading....");

        searchHises=new ArrayList<SearchHis>();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        loading.hide();
                        Toast.makeText(getApplicationContext(),"连接错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loading.hide();
                        Toast.makeText(getApplicationContext(),"暂时没有预约",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        loading.hide();
                        adapter=new SearchHisAdapter(getApplicationContext(),searchHises);
                        lv_search_his.setAdapter(adapter);
                        break;
                }
            }
        };

        loading.show();
        Thread thread=new Thread(new LoadThread());
        thread.start();

    }


    private class LoadThread implements Runnable {
        @Override
        public void run() {
            DefaultHttpClient client= IBookApp.getHttpClient();
            HttpGet get=new HttpGet(link);
            try {
                HttpResponse response=client.execute(get);
                if(response.getStatusLine().getStatusCode()==200)
                {
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    parseHtml(html);
                }else {
                    Message msg=new Message();
                    msg.what= URL_WRONG;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void parseHtml(String html) {
            Elements elements = ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                    select("div[id=\"container\"]").
                    select("div[id=\"mylib_content\"]").
                    select("form").
                    select("table").
                    select("tbody").
                    select("tr");
            if (elements.size()==1) {
                Message msg = new Message();
                msg.what = PREG_NULL;
                handler.sendMessage(msg);
            } else {
                for (int i = 1; i < elements.size(); i++) {
                    SearchHis searchHis=new SearchHis();
                    Elements contents=elements.get(i).select("td");
                    searchHis.setContent(contents.get(1).text().substring(contents.get(1).text().indexOf("=")+1));
                    searchHis.setTime(contents.get(2).text());
                    searchHises.add(searchHis);
                }
                Message msg = new Message();
                msg.what = OK;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        loading.dismiss();
        super.onDestroy();
    }
}
