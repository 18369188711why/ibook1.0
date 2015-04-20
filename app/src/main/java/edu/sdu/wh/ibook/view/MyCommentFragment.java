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
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.adapter.MyCommentAdapter;
import edu.sdu.wh.ibook.po.MyComment;
import edu.sdu.wh.ibook.util.GetInteger;
import edu.sdu.wh.ibook.util.ToDocument;

/**
 *
 */
public class MyCommentFragment extends Fragment {

    private View v;
    private BaseAdapter adapter;
    private Activity activity;
    private Context context;

    private ListView lv;

    private LoadingDialog loading;

    private List<MyComment> myComments;

    private Handler handler;

    private static String URL_DOC = "http://202.194.40.71:8080/reader/book_rv.php";

    private static int URL_WRONG = 0, OK = 1, COMMENT_NULL = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        return this.v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        v = inflater.inflate(R.layout.fragment_mydocument, null);
        lv = (ListView) v.findViewById(R.id.lv_myDocument);

        lv.setEmptyView(v.findViewById(R.id.empty_view));

        myComments = new ArrayList<MyComment>();
        adapter = new MyCommentAdapter(context, myComments);
        lv.setAdapter(adapter);


        loading=new LoadingDialog(activity,"Loading.....");

        handler = new Handler() {
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        loading.hide();
                        Toast.makeText(context, "数据加载失败！", Toast.LENGTH_SHORT).show();
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

    private class LoadThread implements Runnable {
        @Override
        public void run() {
            boolean flag;
            DefaultHttpClient client = IBookApp.getHttpClient();
            HttpGet get = new HttpGet(URL_DOC);
            try {
                HttpResponse response = client.execute(get);
                flag = response.getStatusLine().getStatusCode() == 200;
                if (!flag) {
                    Message msg = new Message();
                    msg.what = URL_WRONG;
                    handler.sendMessage(msg);
                } else {
                    String html = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    parseHtml(html);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseHtml(String html) {
        Elements attitudes = ToDocument.getDocument(html).select("div[id=\"mainbox\"]").
                select("div[id=\"container\"]").
                select("div[id=\"mylib_content\"]").
                select("div[id=\"comment\"]").
                select("div[class=\"attitude\"]");
        if (attitudes.isEmpty()) {
            Message msg = new Message();
            msg.what = COMMENT_NULL;
            handler.sendMessage(msg);
        } else if (attitudes.size() != myComments.size()||myComments.isEmpty()  ) {

            myComments.clear();
            for (int i = 0; i < attitudes.size(); i++) {
                Element element = attitudes.get(i);
                Elements documentContent = element.select("div").select("p");
                MyComment mydocument = new MyComment();
                mydocument.setDocumentNum(Integer.toString(i).trim());

                //<p>0设置书名和作者
                String name_author = documentContent.get(0).text();
                String name = documentContent.get(0).select("a").text();
                mydocument.setBookName(name);
                mydocument.setAuthor(name_author.substring(name.length()));

                //<p>1设置内容
                mydocument.setContent(documentContent.get(1).text().trim());

                //<p>2设置支持和反对的人数以及时间
                String number = documentContent.get(2).text().trim();
                String[] numbers = number.split(" ");
                mydocument.setSupportNum(GetInteger.getInteger(numbers[0])[0]);
                mydocument.setAgainstNum(GetInteger.getInteger(numbers[0])[1]);
                mydocument.setTime(numbers[3] + "  " + numbers[4]);
                myComments.add(mydocument);
            }
            Message msg = new Message();
            msg.what = OK;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = OK;
            handler.sendMessage(msg);
        }

    }
}
