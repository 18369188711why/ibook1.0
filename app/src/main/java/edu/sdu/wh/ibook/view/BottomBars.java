package edu.sdu.wh.ibook.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import edu.sdu.wh.ibook.R;


/**
 * Created by Wang Hongyu on 2015/1/20.
 */
public class BottomBars extends View{

    private int mColor=0xFF45C01A;
    private Bitmap miconBitmap;
    private String mText="通";
    private int mTextSize= (int)TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics());

    private Canvas mcanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha;

    private Rect mIconRect;
    private Rect mTextBound;

    private Paint mTextPaint;

    public BottomBars(Context context) {
        this(context,null);
    }
    public BottomBars(Context context, AttributeSet attrs) {
       this(context, attrs,0);
    }
    public BottomBars(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs,
                R.styleable.BottomBars);
        int n=a.getIndexCount();
        for(int i=0;i<n;i++){
           int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.BottomBars_icon:
                    BitmapDrawable drawable= (BitmapDrawable) a.getDrawable(attr);
                    miconBitmap=drawable.getBitmap();
                    break;
                case R.styleable.BottomBars_color:
                    mColor=a.getColor(attr,0xFF45C01A);
                    break;
                case R.styleable.BottomBars_text:
                    mText=a.getString(attr);
                    break;
                case R.styleable.BottomBars_text_size:
                    mTextSize=a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        mTextBound=new Rect();
        mTextPaint=new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xc8757575);

        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth=Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());
        int left=getMeasuredWidth()/2-iconWidth/2;
        int top=getMeasuredHeight()/2-mTextBound.height()/2-iconWidth/2;
        mIconRect=new Rect(left,top,left+iconWidth,top+iconWidth);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(miconBitmap,null,mIconRect,null);
        //在内存准备bitmap，alpha，纯色,xfermode,图标
        int alpha= (int) Math.ceil(255*mAlpha);
        setupTargetBitMap(alpha);

        //绘制原文本
        drawSourceText(canvas,alpha);
        //绘制变色的文本
        drawTargetText(canvas,alpha);

        canvas.drawBitmap(mBitmap,0,0,null);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255-alpha);
        int x=getMeasuredWidth()/2-mTextBound.width()/2;
        int y=mIconRect.bottom+mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    /*
    * 在内存中绘制可变色的Icon*/
    private void setupTargetBitMap(int alpha) {
        mBitmap=Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        mcanvas=new Canvas(mBitmap);
        mPaint=new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mcanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mcanvas.drawBitmap(miconBitmap,null,mIconRect,mPaint);
    }

    public void setIconAlpha(float alpha){
        this.mAlpha=alpha;
        invaliDateView();
    }

    private void invaliDateView() {
        if(Looper.getMainLooper()==Looper.getMainLooper())
        {
            invalidate();
        }else{
            postInvalidate();
        }
    }


    private static final String INSTANCE_STATUS="instance_status";
    private static final String STATUS_ALPHA="status_alpha";
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE_STATUS,super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA,mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle)
        {
            Bundle bundle= (Bundle) state;
            mAlpha=bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
