package edu.sdu.wh.ibook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.HotAdapter;
import edu.sdu.wh.ibook.po.HotBookInfo;
import edu.sdu.wh.ibook.util.ToDocument;
import edu.sdu.wh.ibook.view.LoadingDialog;

public class HotActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static String url="http://202.194.40.71:8080/top/top_lend.php";
    private Spinner spinn_hot;
    private ListView lv_hot;
    private LoadingDialog loading;

    private HotAdapter adapter;
    private List<HotBookInfo> hotBookInfos;
    private Handler handler;
    private String parmas;
    private static int URL_WRONG=0,OK=1;
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";
    private static Map<String,String> typeValue=new HashMap<String, String>(){
        {
            put("总体排行","");
            put("A 马列主义、毛泽东思想、邓小平理论","?cls_no=A");
            put("B 哲学、宗教","?cls_no=B");
            put("C 社会科学总论","?cls_no=C");
            put("D 政治、法律","?cls_no=D");
            put("E 军事","?cls_no=E");
            put("F 经济","?cls_no=F");
            put("G 文化、科学、教育、体育","?cls_no=G");
            put("H 语言、文学","?cls_no=H");
            put("I 文学","?cls_no=I");
            put("J 艺术","?cls_no=J");
            put("K 历史、地理","?cls_no=K");
            put("N 自然科学总论","?cls_no=N");
            put("O 数理科学与化学","?cls_no=O");
            put("P 天文学、地球科学","?cls_no=P");
            put("Q 生物科学","?cls_no=Q");
            put("R 医药、卫生","?cls_no=R");
            put("S 农业科学","?cls_no=S");
            put("T 工业技术","?cls_no=T");
            put("U 交通运输","?cls_no=U");
            put("V 航空、航天","?cls_no=V");
            put("X 环境科学，安全科学","?cls_no=X");
            put("Z 综合性图书","?cls_no=Z");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot);

        initView();
        initData();
        initEvent();
        loading.show();
        Thread thread=new Thread(new LoadThread());
        thread.start();
    }

    private void initEvent() {
        lv_hot.setOnItemClickListener(this);
        spinn_hot.setOnItemSelectedListener(this);
    }

    private void initData() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loading.dismiss();
                        adapter=new HotAdapter(getApplicationContext(),hotBookInfos);
                        lv_hot.setAdapter(adapter);
                        break;
                }
            }
        };
        parmas="";
    }

    private void initView() {
        spinn_hot= (Spinner) findViewById(R.id.spinn_hotType);
        final String[] typeList=getResources().getStringArray(R.array.hot_type);
        ArrayAdapter rightAdapter =new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_item, typeList)
        {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                label.setText(typeList[position]);
                if (spinn_hot.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.white));
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.light_white));
                }

                return view;
            }
        };
        spinn_hot.setAdapter(rightAdapter);

        lv_hot= (ListView) findViewById(R.id.lv_hot);

        loading=new LoadingDialog(this,"Loading....");
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(HotActivity.this,BookDetailActivity.class);
        intent.putExtra("书名",hotBookInfos.get(i).getName());
        String link=hotBookInfos.get(i).getLink().substring(8);
        intent.putExtra("链接",URL_BASIC+link);
        this.startActivityForResult(intent,1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        parmas=typeValue.get(spinn_hot.getSelectedItem().toString());
        loading.show();
        Thread loadThread=new Thread(new LoadThread());
        loadThread.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            DefaultHttpClient client= IBookApp.getHttpClient();
            HttpGet get=new HttpGet(url+parmas);
            try {
                HttpResponse response=client.execute(get);
                boolean flag=response.getStatusLine().getStatusCode()==200;
                if(flag)
                {
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
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
        hotBookInfos=new ArrayList<HotBookInfo>();
        Elements contents=ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("table[class=\"table_line\"]").
                select("tbody").
                select("tr");
        for (int i = 1; i <contents.size() ; i++) {
            HotBookInfo hot=new HotBookInfo();
            Elements book=contents.get(i).select("td");
            hot.setName(book.get(0).text()+"、"+book.get(1).text());
            hot.setLink(book.get(1).select("a").first().attr("href"));
            hot.setAuthor(book.get(2).text());
            hot.setPublisher(book.get(3).text());
            hot.setCode(book.get(4).text());
            hot.setPlaceInfo(book.get(5).text());
            hot.setBorrowNum(book.get(6).text());
            hot.setBorrowRate(book.get(7).text());
            hotBookInfos.add(hot);
        }
    }
}
