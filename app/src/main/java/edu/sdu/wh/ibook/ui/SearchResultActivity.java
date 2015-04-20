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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.SearchResultAdapter;
import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.util.GetContent;
import edu.sdu.wh.ibook.util.ToDocument;
import edu.sdu.wh.ibook.view.LoadingDialog;

public class SearchResultActivity extends Activity implements AdapterView.OnItemClickListener {
    private Handler handler;
    private static String url="http://202.194.40.71:8080/opac/openlink.php?";
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";
    private ListView lv_bookList;
    private LoadingDialog loading;

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

    private static int URL_WRONG=0,OK=1,INFO_NULL=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_result);

        Intent intent=getIntent();

        String typeName=intent.getStringExtra("搜索方法");
        type = typeValue.get(typeName);
        name=intent.getStringExtra("书名");

        initView();
        initData();
        initEvent();

        load();
    }

    private void load() {
        loading.show();
        Thread loadThread=new Thread(new LoadThread());
        loadThread.start();
    }

    private void initEvent() {
        lv_bookList.setOnItemClickListener(this);
    }

    private void initView() {
        lv_bookList= (ListView) findViewById(R.id.lv_searchResult);

        lv_bookList.setEmptyView(findViewById(R.id.empty_view));

        loading=new LoadingDialog(this,"Loading....");

    }
    private void initData() {
        handler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what)
                {
                    case 0:
                        loading.hide();
                        Toast.makeText(getApplicationContext(),"搜索错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loading.hide();
                        Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
                        SearchResultAdapter adapter=new SearchResultAdapter(getApplicationContext(),
                                bookInfos);
                        lv_bookList.setAdapter(adapter);
                        break;
                    case 2:
                        loading.hide();
                        Toast.makeText(getApplicationContext(),"搜索结果为空",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(SearchResultActivity.this,BookDetailActivity.class);
        intent.putExtra("书名",bookInfos.get(i).getName_code());
        intent.putExtra("链接",bookInfos.get(i).getLink());
        SearchResultActivity.this.startActivityForResult(intent, 0);
    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            boolean flag;
            bookInfos=new ArrayList<BookInfo>();
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
                    msg.what = URL_WRONG;
                    handler.sendMessage(msg);
                } else {
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    parseHtml(html);
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

    public void parseHtml(String html) {
        Elements contents= ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").select("div[id=\"content\"]");

        if(contents.size()==0){
            Message msg=new Message();
            msg.what=INFO_NULL;
            handler.sendMessage(msg);
            return;
        }
        else{
            Elements books=contents.select("div[class=\"book_article\"]").
                    get(3).
                    select("ol[id=\"search_book_list\"]").
                    select("li[class=\"book_list_info\"]");

            for (int i = 0; i < books.size(); i++) {
                BookInfo bookInfo=new BookInfo();

                String type_name_ISBN=books.get(i).select("h3").text();
                String type=books.get(i).select("h3").select("span").text();
                String name_ISBN=type_name_ISBN.substring(type.length(),type_name_ISBN.length());

                Element a=books.get(i).select("a").first();
                String link=a.attr("href");
                //书的链接
                bookInfo.setLink(URL_BASIC+link);

                //书名和ISBN
                bookInfo.setName_code(name_ISBN);

                //书种类
                bookInfo.setType(type);

                String author_publish_number=books.get(i).select("p").text();
                String number=books.get(i).select("p").select("span").text();
                String author_publisher= GetContent.getContent(author_publish_number.substring(number.length(), author_publish_number.length()));

                //馆藏&可借
                bookInfo.setStored_available_Num(number);
                //作者&出版社
                bookInfo.setAuthor_publisher(author_publisher);
                bookInfos.add(bookInfo);
            }
            Message msg = new Message();
            msg.what = OK;
            handler.sendMessage(msg);
        }
    }
}
