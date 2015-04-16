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
import edu.sdu.wh.ibook.adapter.AsordAdapter;
import edu.sdu.wh.ibook.po.Asord;
import edu.sdu.wh.ibook.util.ToDocument;

public class AsordActivity extends Activity {

    String link;
    ListView lv_list;
    List<Asord> asords;
    AsordAdapter adapter;
    Handler handler;
    static int ASORD_NULL=0,URL_WRONG=1,OK=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asord);
        link=getIntent().getStringExtra("链接");
        Toast.makeText(getApplicationContext(), link, Toast.LENGTH_SHORT).show();
        lv_list= (ListView) findViewById(R.id.lv_asord);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(getApplicationContext(),"暂时没有预约",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(),"链接错误！",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        adapter=new AsordAdapter(getApplicationContext(),asords);
                        lv_list.setAdapter(adapter);
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
                    msg.what=URL_WRONG;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void parseHtml(String html) {
            asords=new ArrayList<Asord>();
            Elements elements= ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                    select("div[id=\"container\"]").
                    select("div[id=\"mylib_content\"]").
                    select("table").
                    select("tbody");
            if(elements.isEmpty())
            {
                Message msg=new Message();
                msg.what=ASORD_NULL;
                handler.sendMessage(msg);
            }else {
                elements=elements.select("tr");
                for(int i=1;i<elements.size();i++)
                {
                    Asord asord = new Asord();
                    Elements content=elements.get(i).select("td");
                    asord.setName(content.get(0).text().trim());
                    asord.setCharge(content.get(1).text().trim());
                    asord.setPublish(content.get(2).text().trim());
                    asord.setDate(content.get(3).text().trim());
                    asord.setStatus(content.get(4).text().trim());
                    asord.setNote(content.get(5).text().trim());
                    asords.add(asord);
                }
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
        }
    }
}
