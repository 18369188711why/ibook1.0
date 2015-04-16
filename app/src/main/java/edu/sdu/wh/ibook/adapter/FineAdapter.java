package edu.sdu.wh.ibook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.po.Fine;

/**
 *
 */
public class FineAdapter extends BaseAdapter{
    Context context;
    private List<Fine> fines;

    public FineAdapter(Context context,List<Fine> fines){
        this.context=context;
        this.fines=fines;
        }
    @Override
    public int getCount() {
        return fines.size();
    }

    @Override
    public Object getItem(int i) {
        return fines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.item_fine,null);
        //条码号
        TextView tv_fineBarcode= (TextView) v.findViewById(R.id.tv_fineBarcode);
        tv_fineBarcode.setText(fines.get(i).getBarcode());
        //索书号
        TextView tv_fineCode= (TextView) v.findViewById(R.id.tv_fineCode);
        tv_fineCode.setText(fines.get(i).getCode());
        //题名
        TextView tv_fineName= (TextView) v.findViewById(R.id.tv_fineName);
        tv_fineName.setText(fines.get(i).getName());
       //责任者
        TextView tv_fineCharge= (TextView) v.findViewById(R.id.tv_fineCharge);
        tv_fineCharge.setText(fines.get(i).getCharge());
        //借阅日
        TextView tv_fineBorrDate= (TextView) v.findViewById(R.id.tv_fineBorrDate);
        tv_fineBorrDate.setText(fines.get(i).getBorrDate());
        //应还日
        TextView tv_fineShouldRetuDate= (TextView) v.findViewById(R.id.tv_fineShouldRetuDate);
        tv_fineShouldRetuDate.setText(fines.get(i).getShouldReturnDate());
        //馆藏地
        TextView tv_finePlace= (TextView) v.findViewById(R.id.tv_finePlace);
        tv_finePlace.setText(fines.get(i).getPlace());
        //应缴
        TextView tv_fine= (TextView) v.findViewById(R.id.tv_fine);
        tv_fine.setText(fines.get(i).getFine());
        //实缴
        TextView tv_finePay= (TextView) v.findViewById(R.id.tv_finePay);
        tv_finePay.setText(fines.get(i).getPay());
        //状态
        TextView tv_fineStatus= (TextView) v.findViewById(R.id.tv_fineStatus);
        tv_fineStatus.setText(fines.get(i).getStatus());

        return v;
    }
}
