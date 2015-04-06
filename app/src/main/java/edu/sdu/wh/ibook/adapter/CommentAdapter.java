package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.Comment;

/**
 *书籍评论适配器
 */
public class CommentAdapter extends BaseAdapter{
    Context context;
    List<Comment> comments;
    public CommentAdapter(Context context, List<Comment> documents){
        this.context=context;
        this.comments =documents;
    }
    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_document,null);
        //姓名
        TextView tv_docName= (TextView) v.findViewById(R.id.tv_docName);
        tv_docName.setText(comments.get(i).getName());
        //时间
        TextView tv_docTime= (TextView) v.findViewById(R.id.tv_docTime);
        tv_docTime.setText(comments.get(i).getTime());
        //内容
        TextView tv_docContent= (TextView) v.findViewById(R.id.tv_docContent);
        tv_docContent.setText(comments.get(i).getContent());

        return v;
    }
}
