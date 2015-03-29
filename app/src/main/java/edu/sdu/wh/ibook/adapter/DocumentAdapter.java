package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.BookInfo;
import edu.sdu.wh.ibook.po.Mydocument;

/**
 * 我的书评页面适配器
 */
public class DocumentAdapter extends BaseAdapter{
    Context context;
    List<Mydocument> mydocuments;
    public DocumentAdapter(Context context,List<Mydocument> mydocuments){
        this.context=context;
        this.mydocuments=mydocuments;
    }

    @Override
    public int getCount() {
        return mydocuments.size();
    }

    @Override
    public Object getItem(int i) {
        return mydocuments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_mydocument,null);
        //
        TextView tv_documentNum= (TextView) v.findViewById(R.id.tv_documentNum);
        tv_documentNum.setText(mydocuments.get(i).getDocumentNum());
        //
        TextView tv_bookName= (TextView) v.findViewById(R.id.tv_bookName);
        tv_bookName.setText(mydocuments.get(i).getBookName());
        //
        TextView tv_author= (TextView) v.findViewById(R.id.tv_author);
        tv_author.setText(mydocuments.get(i).getAuthor());
        //
        TextView tv_time= (TextView) v.findViewById(R.id.tv_time);
        tv_time.setText(mydocuments.get(i).getTime());
        //
        TextView tv_content= (TextView) v.findViewById(R.id.tv_content);
        tv_content.setText(mydocuments.get(i).getContent());
        //
        TextView tv_supportNum= (TextView) v.findViewById(R.id.tv_supportNum);
        tv_supportNum.setText(mydocuments.get(i).getSupportNum());
        //
        TextView tv_againstNum= (TextView) v.findViewById(R.id.tv_againstNum);
        tv_againstNum.setText(mydocuments.get(i).getAgainstNum());

        return v;
    }
}
