package edu.sdu.wh.ibook.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import edu.sdu.wh.ibook.adapter.NowAdapter;
import edu.sdu.wh.ibook.po.NowBookInfo;
import edu.sdu.wh.ibook.ui.BookDetailActivity;
import edu.sdu.wh.ibook.util.ToDocument;

/**
 *
 */
public class BorrowNowFragment extends Fragment implements  AdapterView.OnItemClickListener {

    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private String number;
    private List<NowBookInfo> bookInfos;

    private ListView lv;
    private TextView tv_number;

    private Handler handler;
    private Context context;
    private LoadingDialog dialog;

    private static String URL_LIST="http://202.194.40.71:8080/reader/book_lst.php";
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";
    private static int NOW_NULL =0,OK=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        initEvent();
        return this.v;
    }

    private void initEvent() {
        lv.setOnItemClickListener(this);
    }

    //更新数据进行，应该写线程
    public void initData() {
        dialog.show();
        Thread loadThread = new Thread(new LoadThread());
        loadThread.start();
    }

    private void initView() {
        activity=getActivity();
        context=activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        v = inflater.inflate(R.layout.fragment_borrownow, null);
        lv = (ListView) v.findViewById(R.id.lv_borrowBooksNow);

        lv.setEmptyView(v.findViewById(R.id.empty_view));

        tv_number= (TextView) v.findViewById(R.id.tv_number);

        bookInfos=new ArrayList<NowBookInfo>();
        adapter=new NowAdapter(context,bookInfos);
        lv.setAdapter(adapter);

        dialog=new LoadingDialog(activity,"Loading.....");

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        dialog.hide();
                        tv_number.setText(number);
                        break;
                    case 1:
                        dialog.hide();
                        tv_number.setText(number);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(activity.getApplicationContext(), BookDetailActivity.class);
        intent.putExtra("书名",bookInfos.get(i).getName_author());
        String link=bookInfos.get(i).getLink().substring(8);
        intent.putExtra("链接",URL_BASIC+link);
        activity.startActivityForResult(intent, 2);

    }

    private class LoadThread implements Runnable {
        boolean flag;
        @Override
        public void run() {
                HttpClient client=IBookApp.getHttpClient();
                HttpGet get=new HttpGet(URL_LIST);
                try {
                    HttpResponse response=client.execute(get);
                    flag=response.getStatusLine().getStatusCode()==200;
                    if(!flag)
                    {
                        Message msg=new Message();
                        msg.what= NOW_NULL;
                        dialog.setProgress(100);
                        handler.sendMessage(msg);
                    }
                    else{
                        String html=EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                        parseHtml(html);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


        private void parseHtml(String html){
            Elements elements= ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                    select("div[id=\"container\"]").
                    select("div[id=\"mylib_content\"]");


            Elements contents = elements.
                    select("table").
                    select("tbody").
                    select("tr");

            Elements elementss=elements.select("p");

            number=elementss.text();

            if(contents.size()==1)
            {
                Message msg=new Message();
                msg.what= NOW_NULL;
                handler.sendMessage(msg);
            }else if(contents.size()!=(bookInfos.size()+1)||bookInfos.isEmpty()){
                bookInfos.clear();
                for(int i=1;i<contents.size();i++)
                {
                    NowBookInfo bookInfo = new NowBookInfo();
                    Elements book=contents.get(i).select("td");
                    bookInfo.setBarcode(book.get(0).text().trim());
                    bookInfo.setName_author(book.get(1).text().trim());
                    bookInfo.setLink(book.get(1).select("a").first().attr("href"));
                    bookInfo.setBorrowDate(book.get(2).text().trim());
                    bookInfo.setReturnDate(book.get(3).text().trim());
                    bookInfo.setRenewNum(book.get(4).text().trim());
                    bookInfo.setPlace(book.get(5).text().trim());
                    bookInfos.add(bookInfo);
                }
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
            else {
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
        }
    }
}
