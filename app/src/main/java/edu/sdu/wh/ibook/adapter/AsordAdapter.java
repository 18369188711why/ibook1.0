package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.Asord;

/**
 *
 */
public class AsordAdapter extends BaseAdapter{
    Context context;
    List<Asord> asords;
    public AsordAdapter(Context context, List<Asord> asords){
        this.context=context;
        this.asords =asords;
    }
    @Override
    public int getCount() {
        return asords.size();
    }

    @Override
    public Object getItem(int i) {
        return asords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_asord,null);
        //题名
        TextView tv_name= (TextView) v.findViewById(R.id.tv_asordName);
        tv_name.setText(asords.get(i).getStatus());
        //责任者
        TextView tv_charge= (TextView) v.findViewById(R.id.tv_asordCharge);
        tv_charge.setText(asords.get(i).getCharge());
        //出版信息
        TextView tv_publish= (TextView) v.findViewById(R.id.tv_asordPublish);
        tv_publish.setText(asords.get(i).getPublish());
        //荐购日期
        TextView tv_asordDate= (TextView) v.findViewById(R.id.tv_asordDate);
        tv_asordDate.setText(asords.get(i).getDate());
        //荐购状态
        TextView tv_asordStatus= (TextView) v.findViewById(R.id.tv_asordStatus);
        tv_asordStatus.setText(asords.get(i).getStatus());
        //处理备注
        TextView tv_asordNote= (TextView) v.findViewById(R.id.tv_asordNote);
        tv_asordNote.setText(asords.get(i).getNote());

        return v;
    }
}
