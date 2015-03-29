package edu.sdu.wh.ibook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import edu.sdu.wh.ibook.R;


/**
 * 已经实现底部加载和加载完毕的功能，利用接口回调的机制
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener{

    private View vw_footer;
    private int totalItemCount;
    private int lastVisibleItem;
    private Boolean isLoading=false;
    private LoadListener loadListener;


    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }


    private void initView(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        vw_footer=inflater.inflate(R.layout.lv_footer,null);
        vw_footer.findViewById(R.id.lv_loadFooter).setVisibility(View.GONE);
        this.addFooterView(vw_footer);
        this.setOnScrollListener(this);

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView,
                                     int scrollState) {
        if(totalItemCount ==lastVisibleItem
                &&scrollState==SCROLL_STATE_IDLE){
            if(!isLoading)
            {
                isLoading=true;
                vw_footer.findViewById(R.id.lv_loadFooter).setVisibility(View.VISIBLE);
               //滑动到底部的时候调用加载更多的方法
                loadListener.onLoad(this);
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView,
                         int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        this.lastVisibleItem=firstVisibleItem+visibleItemCount;
        this.totalItemCount =totalItemCount;
    }


    public void setLoadListener(LoadListener loadListener){
        this.loadListener=loadListener;
    }



    //此接口可以在MainActivity中implements并且实现，接口回调
    public interface LoadListener{
        public void onLoad(View view);
    }

    public void loadComplete(){
        this.isLoading=false;
        this.vw_footer.findViewById(R.id.lv_loadFooter).setVisibility(View.GONE);
    }
}
