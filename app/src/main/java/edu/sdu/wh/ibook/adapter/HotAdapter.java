package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.HisBookInfo;
import edu.sdu.wh.ibook.po.HotBookInfo;

/**
 *
 */
public class HotAdapter extends BaseAdapter{
    Context context;
    List<HotBookInfo> bookInfos;

    public HotAdapter(Context context,List<HotBookInfo> bookInfos)
    {
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
        View v=inflater.inflate(R.layout.item_hot_book_info,null);
        //书名称
        TextView tv_bookHotName = (TextView) v.findViewById(R.id.tv_bookHotName);
        tv_bookHotName.setText(bookInfos.get(i).getName());
        //作者
        TextView tv_bookHotAuthor = (TextView) v.findViewById(R.id.tv_bookHotAuthor);
        tv_bookHotAuthor.setText(bookInfos.get(i).getAuthor());
        //出版信息
        TextView tv_bookHotPublishInfo= (TextView) v.findViewById(R.id.tv_bookHotPublishInfo);
        tv_bookHotPublishInfo.setText(bookInfos.get(i).getPublisher());
        //索书号
        TextView tv_bookHotCode= (TextView) v.findViewById(R.id.tv_bookHotCode);
        tv_bookHotCode.setText(bookInfos.get(i).getCode());
        //馆藏
        TextView tv_bookHotPlaceInfo= (TextView) v.findViewById(R.id.tv_bookHotPlaceInfo);
        tv_bookHotPlaceInfo.setText(bookInfos.get(i).getPlaceInfo());
        //借阅册次
        TextView tv_bookHotBorrNum= (TextView) v.findViewById(R.id.tv_bookHotBorrNum);
        tv_bookHotBorrNum.setText(bookInfos.get(i).getBorrowNum());
        //借阅率
        TextView tv_bookHotBorrRate=(TextView) v.findViewById(R.id.tv_bookHotBorrRate);
        tv_bookHotBorrRate.setText(bookInfos.get(i).getBorrowRate());

        return v;
    }
}
