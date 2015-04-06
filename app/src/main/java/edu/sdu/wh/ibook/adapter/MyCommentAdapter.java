package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.MyComment;

/**
 * 我的书评页面适配器
 */
public class MyCommentAdapter extends BaseAdapter{
    Context context;
    List<MyComment> myComments;
    public MyCommentAdapter(Context context, List<MyComment> mydocuments){
        this.context=context;
        this.myComments =mydocuments;
    }

    @Override
    public int getCount() {
        return myComments.size();
    }

    @Override
    public Object getItem(int i) {
        return myComments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_mydocument,null);
        //书名
        TextView tv_bookName= (TextView) v.findViewById(R.id.tv_bookName);
        tv_bookName.setText(myComments.get(i).getBookName());
        //作者
        TextView tv_author= (TextView) v.findViewById(R.id.tv_author);
        tv_author.setText(myComments.get(i).getAuthor());
        //发表时间
        TextView tv_time= (TextView) v.findViewById(R.id.tv_time);
        tv_time.setText(myComments.get(i).getTime());
        //内容
        TextView tv_content= (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(myComments.get(i).getContent());
        //支持人数
        TextView tv_supportNum= (TextView) v.findViewById(R.id.tv_supportNum);
        tv_supportNum.setText(myComments.get(i).getSupportNum());
        //反对人数
        TextView tv_againstNum= (TextView) v.findViewById(R.id.tv_againstNum);
        tv_againstNum.setText(myComments.get(i).getAgainstNum());
        return v;
    }
}
