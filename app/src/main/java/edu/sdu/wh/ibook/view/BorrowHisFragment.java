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
import android.widget.Toast;

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
import edu.sdu.wh.ibook.adapter.HisAdapter;
import edu.sdu.wh.ibook.po.HisBookInfo;
import edu.sdu.wh.ibook.ui.BookDetailActivity;
import edu.sdu.wh.ibook.util.ToDocument;

/**
 *
 */
public class BorrowHisFragment extends Fragment implements AdapterView.OnItemClickListener {
    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private Context context;

    private ListView lv;

    private LoadingDialog loading;

    private List<HisBookInfo> bookInfos;

    private Handler handler;

    private static String URL_HIS="http://202.194.40.71:8080/reader/book_hist.php";
    private static String URL_BASIC="http://202.194.40.71:8080/opac/";


    private static int URL_WRONG=0,OK=1,HIS_NULL=2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        initEvent();
        return this.v;
    }

    private void initEvent() {
        lv.setOnItemClickListener(this);
    }

    public void initData() {

        loading.show();
        Thread loadThread = new Thread(new LoadThread());
        loadThread.start();
    }

    private void initView() {
        activity = getActivity();
        context = activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        v = inflater.inflate(R.layout.fragment_borrowhistory, null);
        lv = (ListView) v.findViewById(R.id.lv_borrowBooksHistory);

        lv.setEmptyView(v.findViewById(R.id.empty_view));

        bookInfos=new ArrayList<HisBookInfo>();
        adapter=new HisAdapter(context,bookInfos);
        lv.setAdapter(adapter);

        loading=new LoadingDialog(activity,"Loading......");

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        loading.hide();
                        Toast.makeText(context,"加载失败！",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        loading.hide();
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        loading.hide();
                        break;
                }
            }
        };


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
                        String html=EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
                        parseHtml(html);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
        private void parseHtml(String html) {
            Elements contents=ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                    select("div[id=\"container\"]").
                    select("div[id=\"mylib_content\"]").
                    select("table").
                    select("tbody").
                    select("tr");
            if(contents.size()==1)
            {
                Message msg=new Message();
                msg.what=HIS_NULL;
                handler.sendMessage(msg);
            }else if(contents.size()!=(bookInfos.size()+1)||bookInfos.isEmpty()){
                bookInfos.clear();
                for(int i=1;i<contents.size();i++)
                {
                    HisBookInfo bookInfo = new HisBookInfo();
                    Elements book=contents.get(i).select("td");

                    bookInfo.setBarcode(book.get(1).text().trim());
                    bookInfo.setName(book.get(2).text().trim());
                    bookInfo.setLink(book.get(2).select("a").first().attr("href"));
                    bookInfo.setAuthor(book.get(3).text().trim());
                    bookInfo.setBorrowDate(book.get(4).text().trim());
                    bookInfo.setReturnDate(book.get(5).text().trim());
                    bookInfo.setPlace(book.get(6).text().trim());
                    bookInfos.add(bookInfo);
                }
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
            else{
                Message msg=new Message();
                msg.what=OK;
                handler.sendMessage(msg);
            }
        }
    }
}
