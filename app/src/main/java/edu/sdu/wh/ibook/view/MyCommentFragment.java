package edu.sdu.wh.ibook.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.MyCommentAdapter;
import edu.sdu.wh.ibook.po.MyComment;
import edu.sdu.wh.ibook.service.MyCommentJsoupHtml;

/**
 *
 */
public class MyCommentFragment extends Fragment implements LoadListView.LoadListener {

    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private Context context;
    private String name;

    private LoadListView lv;

    private List<MyComment> myComments;
    private ProgressBar pb_loading;

    private Handler handler;

    private static String URL_DOC="http://202.194.40.71:8080/reader/book_rv.php";

    private static int URL_WRONG=0,OK=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        initEvent();
        return this.v;
    }

    private void initEvent() {
        lv.setLoadListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initData() {
       pb_loading.setVisibility(View.VISIBLE);

        Thread loadThread =new Thread(new LoadThread());
        loadThread.start();

    }

    private void initView() {
        activity = getActivity();
        context = activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        v = inflater.inflate(R.layout.fragment_mydocument, null);
        lv = (LoadListView) v.findViewById(R.id.lv_myDocument);

        myComments =new ArrayList<MyComment>();
        adapter=new MyCommentAdapter(context, myComments);
        lv.setAdapter(adapter);

        pb_loading= (ProgressBar) v.findViewById(R.id.pb_loading);

        handler = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        pb_loading.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "数据加载失败！", Toast.LENGTH_SHORT).show();
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

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            if(myComments.size()==0){
                boolean flag;
                DefaultHttpClient client=IBookApp.getHttpClient();
                HttpGet get=new HttpGet(URL_DOC);
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
                        MyCommentJsoupHtml m= new MyCommentJsoupHtml(html);
                        List<MyComment> d=m.getMydocuments();
                        for (int i = 0; i <d.size() ; i++) {
                            myComments.add(d.get(i));
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
