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
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.NowAdapter;
import edu.sdu.wh.ibook.po.NowBookInfo;
import edu.sdu.wh.ibook.ui.NowDetialActivity;
import edu.sdu.wh.ibook.service.BookInfoNowJsoupHtml;

/**
 *
 */
public class BorrowNowFragment extends Fragment implements LoadListView.LoadListener, AdapterView.OnItemClickListener {

    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private String name;
    private List<NowBookInfo> bookInfos;

    private LoadListView lv;
    private ProgressBar pb_loading;

    private Handler handler;
    private Context context;

    private static String URL_LIST="http://202.194.40.71:8080/reader/book_lst.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        initEvent();
        return this.v;
    }

    private void initEvent() {
        lv.setLoadListener(this);
        lv.setOnItemClickListener(this);
    }

    //更新数据进行，应该写线程
    public void initData() {
        if(name!=null)
        {
            pb_loading.setVisibility(View.VISIBLE);
            Thread loadThread = new Thread(new LoadThread());
            loadThread.start();
        }
    }

    private void initView() {
        activity=getActivity();
        context=activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        name="why";

        v = inflater.inflate(R.layout.fragment_borrownow, null);
        lv = (LoadListView) v.findViewById(R.id.lv_borrowBooksNow);

        bookInfos=new ArrayList<NowBookInfo>();
        adapter=new NowAdapter(context,bookInfos);
        lv.setAdapter(adapter);
        pb_loading= (ProgressBar) v.findViewById(R.id.pb_loading);

        handler = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        pb_loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "当前未借阅图书", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        pb_loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "数据已加载", Toast.LENGTH_SHORT).show();
                        onLoad(lv);
                        break;
                }
            }
        };
    }
    @Override
    public void onLoad(View view) {
        adapter.notifyDataSetChanged();
        lv.loadComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent0 = new Intent(activity.getApplicationContext(), NowDetialActivity.class);
        activity.startActivityForResult(intent0, 0);

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
                    msg.what=0;
                    handler.sendMessage(msg);
                }
                else{
                    String html=EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    BookInfoNowJsoupHtml b=new BookInfoNowJsoupHtml(html);

                    for(int i=0;i<b.getBookInfos().size();i++)
                    {
                        bookInfos.add(b.getBookInfos().get(i));
                    }

                    if(bookInfos.size()==1)
                    {
                        Message msg=new Message();
                        msg.what=0;
                        handler.sendMessage(msg);
                    }
                    else {
                        Message msg=new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
