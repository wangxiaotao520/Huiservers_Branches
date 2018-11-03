package com.huacheng.huiservers.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/5 15:55
 */
public class MyGridview1 extends GridView {

    private int position = 0;
    public boolean isOnMeasure;

    public MyGridview1(Context context, AttributeSet attrs) {//构造函数
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }
}