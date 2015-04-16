package edu.sdu.wh.ibook.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import edu.sdu.wh.ibook.R;

/**
 * 加载显示的Dialog
 */
public class LoadingDialog extends ProgressDialog{
    private Context context;
    private final int theme;
    private final String msg;


    public LoadingDialog(Context context,String msg)
    {
        super(context);
        this.context = context;
        this.theme = R.style.loading_dialog;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressStyle(this.theme);
        setProgressDrawable(context.getResources().getDrawable(R.drawable.loading));
        setMessage(this.msg);
    }
}
