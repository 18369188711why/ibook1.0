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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.HotAdapter;
import edu.sdu.wh.ibook.po.HotBookInfo;
import edu.sdu.wh.ibook.service.BookInfoHotJsoupHtml;

public class HotActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static String url="http://202.194.40.71:8080/top/top_lend.php";
    private Spinner spinn_hot;
    private ListView lv_hot;
    private ProgressBar progressBar;
    private HotAdapter adapter;
    private List<HotBookInfo> bookInfos;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hot);

        initView();
        initData();
        initEvent();
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
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter=new HotAdapter(getApplicationContext(),bookInfos);
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
        progressBar= (ProgressBar) findViewById(R.id.pb_loading);
        progressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hot, menu);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        progressBar.setVisibility(View.VISIBLE);
        Intent intent=new Intent(HotActivity.this,BookDetailActivity.class);
        intent.putExtra("书名",bookInfos.get(i).getName());
        String link=bookInfos.get(i).getLink().substring(8);
        intent.putExtra("链接",URL_BASIC+link);
        this.startActivityForResult(intent,1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        parmas=typeValue.get(spinn_hot.getSelectedItem().toString());
        progressBar.setVisibility(View.VISIBLE);
        Thread loadThread=new Thread(new LoadThread());
        loadThread.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            bookInfos=new ArrayList<HotBookInfo>();
            DefaultHttpClient client= IBookApp.getHttpClient();
            HttpGet get=new HttpGet(url+parmas);
            try {
                HttpResponse response=client.execute(get);
                boolean flag=response.getStatusLine().getStatusCode()==200;
                if(flag)
                {
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    BookInfoHotJsoupHtml jsoupHtml=new BookInfoHotJsoupHtml(html);
                    List<HotBookInfo> b=jsoupHtml.getHotBookInfos();

                    for(int i=0;i<b.size();i++)
                    {
                        bookInfos.add(b.get(i));
                    }
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
