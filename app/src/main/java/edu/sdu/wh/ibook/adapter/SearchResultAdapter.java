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

/**
 *借阅结果页面适配器
 */
public class SearchResultAdapter extends BaseAdapter{

    Context context;
    List<BookInfo> bookInfos;

    public SearchResultAdapter(Context context,List<BookInfo> bookInfos){
        this.context=context;
        this.bookInfos=bookInfos;
    }


    @Override
    public int getCount() {
        return bookInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return bookInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_book_info,null);
        //书名称&ISBN
        TextView tv_bookInfoName = (TextView) v.findViewById(R.id.tv_bookInfoName_ISBN);
        tv_bookInfoName.setText(bookInfos.get(i).getName_code());
        //类型
        TextView tv_bookInfoType= (TextView) v.findViewById(R.id.tv_bookInfoType);
        tv_bookInfoType.setText(bookInfos.get(i).getType());
        //作者&出版社
        TextView tv_bookInfoAuthor= (TextView) v.findViewById(R.id.tv_bookInfoAuthor_publisher);
        tv_bookInfoAuthor.setText(bookInfos.get(i).getAuthor_publisher());
        //馆藏副本&可借副本
        TextView tv_bookInfoStoredNum= (TextView) v.findViewById(R.id.tv_bookInfoStored_available_num);
        tv_bookInfoStoredNum.setText(bookInfos.get(i).getStored_available_Num());

        return v;
    }
}
