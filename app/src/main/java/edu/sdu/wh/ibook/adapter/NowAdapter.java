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
import edu.sdu.wh.ibook.po.NowBookInfo;

/**
 * 当前借阅页面适配器
 */
public class NowAdapter extends BaseAdapter{

    Context context;
    List<NowBookInfo> bookInfos;
    public NowAdapter(Context context,List<NowBookInfo> bookInfos) {
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_now_book_info,null);
        //书名称和作者
        TextView tv_bookNowName_Author = (TextView) v.findViewById(R.id.tv_bookNowNameAuthor);
        tv_bookNowName_Author.setText(bookInfos.get(i).getName_author());
        //条码号
        TextView tv_bookNowBarcode= (TextView) v.findViewById(R.id.tv_bookNowBarcode);
        tv_bookNowBarcode.setText(bookInfos.get(i).getBarcode());
        //馆藏地
        TextView tv_bookNowPlace= (TextView) v.findViewById(R.id.tv_bookNowPlace);
        tv_bookNowPlace.setText(bookInfos.get(i).getPlace());
        //借阅日期
        TextView tv_bookNowBorrDate= (TextView) v.findViewById(R.id.tv_bookNowBorrDate);
        tv_bookNowBorrDate.setText(bookInfos.get(i).getBorrowDate());
        //应还日期
        TextView tv_bookNowRetuDate= (TextView) v.findViewById(R.id.tv_bookNowRetuDate);
        tv_bookNowRetuDate.setText(bookInfos.get(i).getReturnDate());
        //借阅量
        TextView tv_bookNowRenewNum= (TextView) v.findViewById(R.id.tv_bookNowRenewNum);
        tv_bookNowRenewNum.setText(bookInfos.get(i).getRenewNum());
        return v;
    }
}
