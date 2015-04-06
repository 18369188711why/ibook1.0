package edu.sdu.wh.ibook.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import edu.sdu.wh.ibook.R;
import edu.sdu.wh.ibook.ui.HotActivity;
import edu.sdu.wh.ibook.ui.SearchResultActivity;

/**
 *
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    private View v;
    private Activity activity;
    private Context context;
    private EditText et_search;
    private ImageView btn_search;
    private Spinner spinn_func;
    private TextView tv_hot;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();
        initEvent();

        return v;
    }

    private void initEvent() {
        btn_search.setOnClickListener(this);
        tv_hot.setOnClickListener(this);
    }

    private void initView() {
        activity = getActivity();
        context = activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.fragment_searchbooks, null);

        et_search = (EditText) v.findViewById(R.id.et_search);
        btn_search = (ImageView) v.findViewById(R.id.btn_search);
        tv_hot= (TextView) v.findViewById(R.id.tv_hot);

        spinn_func = (Spinner) v.findViewById(R.id.spi_searchType);
        final String[] typeList=getResources().getStringArray(R.array.search_books_type);
        ArrayAdapter rightAdapter =new ArrayAdapter<String>(context,
                        R.layout.spinner_item, typeList)
                {
                @Override
                public View getDropDownView(int position, View convertView,
                        ViewGroup parent) {
                    View view = LayoutInflater.from(context).inflate(R.layout.spinner_item_layout,
                            null);
                    TextView label = (TextView) view
                            .findViewById(R.id.spinner_item_label);
                    label.setText(typeList[position]);
                    if (spinn_func.getSelectedItemPosition() == position) {
                        view.setBackgroundColor(getResources().getColor(
                                R.color.white));
                    } else {
                        view.setBackgroundColor(getResources().getColor(
                                R.color.light_white));
                    }

                    return view;
                }
        };
        rightAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinn_func.setAdapter(rightAdapter);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_search:
                String name=et_search.getText().toString();
                if(name==null)
                {
                    Toast.makeText(context,"请输入查询内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    String searchFunc=spinn_func.getSelectedItem().toString();
                    Intent intent=new Intent(activity, SearchResultActivity.class);
                    intent.putExtra("书名",name);
                    intent.putExtra("搜索方法",searchFunc);
                    activity.startActivity(intent);
                }
                break;
            case R.id.tv_hot:
                Intent intent1=new Intent(activity, HotActivity.class);
                activity.startActivity(intent1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            return;
        }
    }
}
