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



import edu.sdu.wh.ibook.R;
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
    }

    private void initView() {
        activity = getActivity();
        context = activity.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.fragment_searchbooks, null);

        et_search = (EditText) v.findViewById(R.id.et_search);
        btn_search = (ImageView) v.findViewById(R.id.btn_search);

        spinn_func = (Spinner) v.findViewById(R.id.spi_searchType);
        ArrayAdapter rightAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.search_books_func,
                R.layout.spinner_item);
        rightAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinn_func.setAdapter(rightAdapter);
    }
    @Override
    public void onClick(View view) {
        String name=et_search.getText().toString();
        String searchFunc=spinn_func.getSelectedItem().toString();

        Intent intent=new Intent(activity, SearchResultActivity.class);
        intent.putExtra("书名",name);
        intent.putExtra("搜索方法",searchFunc);
        activity.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            return;
        }
    }
}
