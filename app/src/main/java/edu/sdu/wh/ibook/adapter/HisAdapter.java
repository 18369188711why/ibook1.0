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
import edu.sdu.wh.ibook.po.HisBookInfo;

/**
 *借阅历史页面适配器
 */
public class HisAdapter extends BaseAdapter{
    Context context;
    List<HisBookInfo> bookInfos;

    public HisAdapter(Context context,List<HisBookInfo> bookInfos)
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
        View v=inflater.inflate(R.layout.item_his_book_info,null);
        //书名称
        TextView tv_bookHisName = (TextView) v.findViewById(R.id.tv_bookHisName);
        tv_bookHisName.setText(bookInfos.get(i).getName());
        //作者
        TextView tv_bookHisAuthor = (TextView) v.findViewById(R.id.tv_bookHisAuthor);
        tv_bookHisAuthor.setText(bookInfos.get(i).getAuthor());
        //条码号
        TextView tv_bookHisBarcode= (TextView) v.findViewById(R.id.tv_bookHisBarcode);
        tv_bookHisBarcode.setText(bookInfos.get(i).getBarcode());
        //馆藏地
        TextView tv_bookHisPlace= (TextView) v.findViewById(R.id.tv_bookHisPlace);
        tv_bookHisPlace.setText(bookInfos.get(i).getPlace());
        //借出日期
        TextView tv_bookHisBorrDate= (TextView) v.findViewById(R.id.tv_bookHisBorrDate);
        tv_bookHisBorrDate.setText(bookInfos.get(i).getBorrowDate());
        //归还日期
        TextView tv_bookHisReturnDate= (TextView) v.findViewById(R.id.tv_bookHisRetuDate);
        tv_bookHisReturnDate.setText(bookInfos.get(i).getReturnDate());

        return v;
    }
}
