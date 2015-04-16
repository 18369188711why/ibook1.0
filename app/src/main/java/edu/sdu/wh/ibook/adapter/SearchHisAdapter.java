package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.SearchHis;

/**
 *
 */
public class SearchHisAdapter extends BaseAdapter{
    Context context;
    List<SearchHis> searchHises;
    public SearchHisAdapter(Context context,List<SearchHis> searchHises){
        this.context=context;
        this.searchHises=searchHises;
    }
    @Override
    public int getCount() {
        return searchHises.size();
    }

    @Override
    public Object getItem(int i) {
        return searchHises.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_search_his,null);
        //检索历史
        TextView tv_searchHis= (TextView) v.findViewById(R.id.tv_search_content);
        tv_searchHis.setText(searchHises.get(i).getContent());
        //检索时间
        TextView tv_searchTime= (TextView) v.findViewById(R.id.tv_search_time);
        tv_searchTime.setText(searchHises.get(i).getTime());
        return null;
    }
}
