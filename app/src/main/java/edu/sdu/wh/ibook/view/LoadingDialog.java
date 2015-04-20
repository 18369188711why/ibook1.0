package edu.sdu.wh.ibook.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        setMessage(this.msg);
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.loading_dialog,null);

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);//
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_dialog_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
                setProgressStyle(this.theme);
        setCancelable(true);
        setContentView(layout,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
