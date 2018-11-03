package com.huacheng.huiservers.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollChangedScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    private int handlerWhatId = 65984;
    private int timeInterval = 20;
    private int lastY = 0;
    private int count = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == handlerWhatId) {
                if (lastY == getScrollY()) {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(true);
                    }
                } else {
                    if (scrollViewListener != null) {
                        scrollViewListener.onScrollStop(false);
                    }
                    handler.sendMessageDelayed(
                            handler.obtainMessage(handlerWhatId, this),
                            timeInterval);
                    lastY = getScrollY();
                }
            }
        }
    };

    public ScrollChangedScrollView(Context context) {
        super(context);
    }

    public ScrollChangedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChangedScrollView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }

    private OnScrollToBottomListener onScrollToBottom;

    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener) {
        onScrollToBottom = listener;
    }


    public interface OnScrollToBottomListener {
        void onScrollBottomListener(boolean isBottom);
    }



    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }

      /*  View childAt = getChildAt(0);
        if (childAt.getMeasuredHeight() <= getHeight() + getScrollY()) {
            count++;
            if (count == 1) {
//                Log.i("myscrollview", "开始加载更多");
                if (onScrollToBottom != null) {
                    onScrollToBottom.onScrollBottomListener(true);
                    count = 0;
                }
            }
//            Log.i("myscrollview", "开始加载更多1");
            *//*LayoutInflater inflater = LayoutInflater.from(getContext());
            final View view = inflater.inflate(R.layout.refresh_view, null);
            final ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
            iv_img.setVisibility(View.VISIBLE);*//*
//			Toast.makeText(getContext(),"加载更多1",Toast.LENGTH_SHORT).show();
        } else {
            count = 0;
            if (onScrollToBottom != null) {
                onScrollToBottom.onScrollBottomListener(false);
            }
//            Log.i("myscrollview", "开始加载更多2");
        }*/


    }


    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ScrollViewListener {
        /**
         * 滑动监听
         *
         * @param scrollView ScrollView控件
         * @param x          x轴坐标
         * @param y          y轴坐标
         * @param oldx       上一个x轴坐标
         * @param oldy       上一个y轴坐标
         */
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx,
                             int oldy);

        /**
         * 是否滑动停止
         *
         * @param isScrollStop true:滑动停止;false:未滑动停止
         */
        void onScrollStop(boolean isScrollStop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            handler.sendMessageDelayed(
                    handler.obtainMessage(handlerWhatId, this), timeInterval);
        }
        return super.onTouchEvent(ev);
    }


}
