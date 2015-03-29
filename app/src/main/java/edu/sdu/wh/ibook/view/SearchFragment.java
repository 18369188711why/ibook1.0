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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.CoderResult;
import java.util.List;

import edu.sdu.wh.ibook.IBookApp;
import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.service.SearchJsoupHtml;
import edu.sdu.wh.ibook.ui.SearchResultActivity;
import edu.sdu.wh.ibook.ui.UserActivity;

/**
 *
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    private View v;
    private Activity activity;
    private Context context;
    private IBookApp bookApp;
    private EditText et_search;
    private ImageView btn_search;
    private Spinner spinn_func;
    private String[] hotBooks;
    private TextView[] tv_hotBooks;
    private Handler handler;
//    private ProgressBar pb_load;
    private static String url="http://202.194.40.71:8080/opac/search.php";

    public SearchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        initEvent();

        return v;
    }

    private void initEvent() {
        btn_search.setOnClickListener(this);
    }

    private void initView() {
        activity=getActivity();
        context=activity.getApplicationContext();
        LayoutInflater inflater=LayoutInflater.from(context);
        v=inflater.inflate(R.layout.fragment_searchbooks,null);

        et_search= (EditText) v.findViewById(R.id.et_search);
        btn_search= (ImageView) v.findViewById(R.id.btn_search);

        spinn_func= (Spinner) v.findViewById(R.id.spi_searchType);
        ArrayAdapter rightAdapter=ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.search_books_func,
                R.layout.spinner_item);
        rightAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinn_func.setAdapter(rightAdapter);
    }

    public void initData() {

//        bookApp= (IBookApp) activity.getApplication();
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0:
                        Toast.makeText(context,"加载失败",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        for(int i=0;i<hotBooks.length;i++)
                        {
                            tv_hotBooks[i].setText(hotBooks[i]);
                        }
                        Toast.makeText(context,"加载OK:)",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }


    @Override
    public void onClick(View view) {
        String name=et_search.getText().toString();
        String searchFunc=spinn_func.getSelectedItem().toString();

        Intent intent=new Intent(activity, SearchResultActivity.class);
        intent.putExtra("书名",name);
        intent.putExtra("搜索方法",searchFunc);
        activity.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

        }
    }
    private class LoadThread implements Runnable {
        @Override
        public void run() {
            boolean flag;
            HttpClient client = IBookApp.getHttpClient();
            HttpGet get=new HttpGet(url);
            try {
                HttpResponse response=client.execute(get);
                flag=response.getStatusLine().getStatusCode()==200;
                if(!flag) {
                    Message msg=new Message();
                    msg.what=0;
                    handler.sendMessage(msg);
                }else {
                    hotBooks=new String[10];
                    String html= EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                    SearchJsoupHtml jsoupHtml=new SearchJsoupHtml(html);

                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
