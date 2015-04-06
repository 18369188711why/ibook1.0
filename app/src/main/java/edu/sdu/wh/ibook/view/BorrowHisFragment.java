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
import edu.sdu.wh.ibook.adapter.HisAdapter;
import edu.sdu.wh.ibook.po.HisBookInfo;
import edu.sdu.wh.ibook.service.BookInfoHisJsoupHtml;
import edu.sdu.wh.ibook.ui.BookDetailActivity;

/**
 *
 */
public class BorrowHisFragment extends Fragment implements LoadListView.LoadListener, AdapterView.OnItemClickListener {
    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private Context context;

    private LoadListView lv;
    private ProgressBar pb_loading;

    private List<HisBookInfo> bookInfos;

    private Handler handler;

    private static String URL_HIS="http://202.194.40.71:8080/reader/book_hist.php";
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";


    private static int URL_WRONG=0,OK=1;
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

    public void initData() {
        pb_loading.setVisibility(View.VISIBLE);

        Thread loadThread = new Thread(new LoadThread());
        loadThread.start();
    }

    private void initView() {
        activity = getActivity();
        context = activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        v = inflater.inflate(R.layout.fragment_borrowhistory, null);
        lv = (LoadListView) v.findViewById(R.id.lv_borrowBooksHistory);

        bookInfos=new ArrayList<HisBookInfo>();
        adapter=new HisAdapter(context,bookInfos);
        lv.setAdapter(adapter);
        pb_loading= (ProgressBar) v.findViewById(R.id.pb_loading);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        pb_loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(context,"加载失败！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        pb_loading.setVisibility(View.INVISIBLE);
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
        Intent intent = new Intent(activity.getApplicationContext(), BookDetailActivity.class);
        intent.putExtra("书名",bookInfos.get(i).getName());
        String link=bookInfos.get(i).getLink().substring(8);
        intent.putExtra("链接",URL_BASIC+link);
        activity.startActivityForResult(intent, 3);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            boolean flag;
            if(bookInfos.size()==0){
                HttpClient client = IBookApp.getHttpClient();
                HttpGet get = new HttpGet(URL_HIS);
                try {
                    HttpResponse response=client.execute(get);
                    flag=response.getStatusLine().getStatusCode()==200;
                    if(!flag)
                    {
                        Message msg=new Message();
                        msg.what=URL_WRONG;
                        handler.sendMessage(msg);
                    }
                    else {
                        String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                        BookInfoHisJsoupHtml b = new BookInfoHisJsoupHtml(html);
                        for(int i=0;i<b.getBookInfos().size();i++)
                        {
                            bookInfos.add(b.getBookInfos().get(i));
                        }
                        Message msg=new Message();
                        msg.what=OK;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
        }
    }
}
