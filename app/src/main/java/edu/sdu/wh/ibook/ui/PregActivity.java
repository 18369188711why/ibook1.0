package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
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
import edu.sdu.wh.ibook.adapter.PregAdapter;
import edu.sdu.wh.ibook.po.Preg;
import edu.sdu.wh.ibook.util.ToDocument;

public class PregActivity extends Activity {

    String link;
    ListView lv_preg;
    Handler handler;
    List<Preg> pregs;
    PregAdapter adapter;
    static int PREG_NULL=0,URL_WRONG=1,OK=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preg);
        link=getIntent().getStringExtra("链接");
        lv_preg= (ListView) findViewById(R.id.lv_preg);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"连接错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"暂时没有预约",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        adapter=new PregAdapter(getApplicationContext(),pregs);
                        lv_preg.setAdapter(adapter);
                        break;
                }
            }
        };
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
            pregs = new ArrayList<Preg>();
            Elements elements = ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                    select("div[id=\"container\"]").
                    select("div[id=\"mylib_content\"]").
                    select("table").
                    select("tbody").
                    select("tr");
            if (elements.size()==1) {
                Message msg = new Message();
                msg.what = PREG_NULL;
                handler.sendMessage(msg);
            } else {
                for (int i = 1; i < elements.size(); i++) {
                    Preg preg = new Preg();
                    Elements content = elements.get(i).select("td");
                    preg.setCode(content.get(0).text().trim());
                    preg.setName_author(content.get(1).text().trim());
                    preg.setLink(content.get(1).select("a").first().attr("href"));
                    preg.setPlace(content.get(2).text().trim());
                    preg.setPregDate(content.get(3).text().trim());
                    preg.setDeadDate(content.get(4).text().trim());
                    preg.setGetPlace(content.get(5).text().trim());
                    preg.setStatus(content.get(6).text().trim());
                    pregs.add(preg);
                }
                Message msg = new Message();
                msg.what = OK;
                handler.sendMessage(msg);
            }
        }
    }
}
