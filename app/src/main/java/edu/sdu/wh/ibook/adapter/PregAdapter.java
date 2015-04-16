package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.Preg;

/**
 *
 */
public class PregAdapter extends BaseAdapter{
    Context context;
    List<Preg> pregs;
    public PregAdapter(Context context,List<Preg> pregs){
        this.context=context;
        this.pregs=pregs;
    }
    @Override
    public int getCount() {
        return pregs.size();
    }

    @Override
    public Object getItem(int i) {
        return pregs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_preg,null);
        //索书号
        TextView tv_pregBarcode= (TextView) v.findViewById(R.id.tv_pregCode);
        tv_pregBarcode.setText(pregs.get(i).getCode());
        //题名/责任者
        TextView tv_pregName_charge= (TextView) v.findViewById(R.id.tv_pregName);
        tv_pregName_charge.setText(pregs.get(i).getName_author());
        //馆藏地
        TextView tv_pregPlace= (TextView) v.findViewById(R.id.tv_pregPlace);
        tv_pregPlace.setText(pregs.get(i).getPlace());
        //预约(到书)日
        TextView tv_pregDate= (TextView) v.findViewById(R.id.tv_pregDate);
        tv_pregDate.setText(pregs.get(i).getPregDate());
        //截止日期
        TextView tv_pregDeadDate= (TextView) v.findViewById(R.id.tv_pregDeadDate);
        tv_pregDeadDate.setText(pregs.get(i).getDeadDate());
        //取书地
        TextView tv_pregGetPlace= (TextView) v.findViewById(R.id.tv_pregGetPlace);
        tv_pregGetPlace.setText(pregs.get(i).getGetPlace());
        //状态
        TextView tv_pregStatus= (TextView) v.findViewById(R.id.tv_pregStatus);
        tv_pregStatus.setText(pregs.get(i).getStatus());

        return v;
    }
}
