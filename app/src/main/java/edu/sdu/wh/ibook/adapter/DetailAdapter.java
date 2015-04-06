package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.BookDetail;

/**
 *
 */
public class DetailAdapter extends BaseAdapter{
    Context context;
    List<BookDetail> details;

    public DetailAdapter(Context context, List<BookDetail> details){
        this.context=context;
        this.details =details;
    }
    @Override
    public int getCount() {
        return details.size();
    }

    @Override
    public Object getItem(int i) {
        return details.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_book_detail,null);
        //条码号
        TextView tv_detailBarcode= (TextView) v.findViewById(R.id.tv_bookDetailBarcode);
        tv_detailBarcode.setText(details.get(i).getBarcode());
        //年卷期
        TextView tv_detailYear= (TextView) v.findViewById(R.id.tv_bookDetailYear);
        tv_detailYear.setText(details.get(i).getYear());
        //校区-馆藏地
        TextView tv_detailSchoolPlace= (TextView) v.findViewById(R.id.tv_bookDetailSchool_place);
        tv_detailSchoolPlace.setText(details.get(i).getSchool_place());
        //书刊状况
        TextView tv_docContent= (TextView) v.findViewById(R.id.tv_bookDetailStatus);
        tv_docContent.setText(details.get(i).getStatus());
        return v;
    }
}
