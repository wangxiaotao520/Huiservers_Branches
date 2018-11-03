package com.huacheng.libraryservice.widget;

import android.content.Context;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huacheng.libraryservice.R;
import com.huacheng.libraryservice.adapter.AdapterGalleryAds;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 类说明： 广告轮播功能点 通过getView方法获取到view，一般作为listview的头部组件使用，addheaderview
 * 在页面被挂起的时候调用stopTimer功能，否则再次进入页面会一次性接受到多条信息，导致快速播放多张图片 在页面进入的时候开启轮播，否则不会运行轮播功能
 *
 * @author wz
 * @version 1.0
 * @date 2014-12-20
 */
public class FunctionAdvertise extends RelativeLayout {
    private static final String CACHE_KEY = "ads_list";
    private Context mContext;

    private TimerTask mCycleTask;        // 轮流播放任务
    private Timer mCycleTimer;            // 轮流播放定时器
    private Timer mResumingTimer;
    private TimerTask mResumingTask;

    private ViewPager viewPager;
    private AdapterGalleryAds adapterGalleryAds;    // 广告位适配器
    private List<ModelAds> list_ads;          // 广告列表

    private ImageView smalldot;                     // 广告位小圆点
    private ImageView[] smalldots;                  // 广告位所有小圆点
    private LinearLayout ll_find_ads_dots;          // 广告位红点

    private boolean mCycle;             //是否自动播放广告
    private boolean mCycleing;          //是否正在轮播
    private boolean isInitDot;
    private boolean mAutoRecover = true;    //是否手势触摸广告栏后再放开，广告自动恢复轮播

    private int mCycleDuration = 3000;  //广告切换时间间隔
    private OnClickAdsListener listener;

    public FunctionAdvertise(Context context) {
        this(context, null);
    }

    public FunctionAdvertise(Context context,AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunctionAdvertise(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.header_ads, this, true);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        ll_find_ads_dots = (LinearLayout)findViewById(R.id.ll_find_ads_dot);

        adapterGalleryAds = new AdapterGalleryAds(context);
        adapterGalleryAds.setOnAdvertiseClistener(new OnAdvertiseClickListener() {
            @Override
            public void onClick(View view) {
                ModelAds ads = (ModelAds)view.getTag(R.id.tag_object);
                if (listener!=null){
                    listener.OnClickAds(ads);
                }
            }
        });

        viewPager.setAdapter(adapterGalleryAds);

        list_ads = new ArrayList<>();
        initListener();

        if(mCycle) {
            startCycle();
        }
    }


    public void startCycle() {
        if(list_ads.size()>1) {
            startAutoCycle(mCycleDuration, mCycleDuration, mAutoRecover);
        }
    }

    public void startAutoCycle(int cycleDuration, long delay, boolean autoRecover) {
        if(mCycleTask != null)
            mCycleTask.cancel();
        if(mCycleTimer != null)
            mCycleTimer.cancel();
        if(mResumingTask != null)
            mResumingTask.cancel();
        if(mResumingTimer != null)
            mResumingTimer.cancel();

        mCycleDuration = cycleDuration;
        mCycleTimer = new Timer();
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                Message message= Message.obtain();
                message.what=0;
                cycleHandler.sendMessage(message);
            }
        };

        mCycleTimer.schedule(mCycleTask, delay, mCycleDuration);
        mAutoRecover = autoRecover;
        mCycleing = true;
        mCycle = true;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle(){
        if(mCycleing){
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycleing = false;
        }else{
            if(mResumingTimer != null && mResumingTask != null){
                recoverCycle();
            }
        }
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle() {
        if (mCycleTask != null) {
            mCycleTask.cancel();
        }
        if (mCycleTimer != null) {
            mCycleTimer.cancel();
        }
        if (mResumingTimer != null) {
            mResumingTimer.cancel();
        }
        if (mResumingTask != null) {
            mResumingTask.cancel();
        }

        mCycle = false;
        mCycleing = false;

    }

    /**是否在轮播***/
    public boolean isCycling() {
        return mCycleing;
    }

    private android.os.Handler cycleHandler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    moveNextPosition(true);
                    break;
                case 1:
                    moveToFirst(true);
                    break;
            }
        }
    };

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle(){
        if(!mAutoRecover || !mCycle){
            return;
        }

        if(!mCycleing&&list_ads.size()>1){
            if(mResumingTask != null && mResumingTimer!= null){
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startCycle();
                }
            };
            //3s后重新启动轮播
            mResumingTimer.schedule(mResumingTask, 3000);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseAutoCycle();
                break;
        }
        return false;
    }


    /**
     * 初始化监听事件
     */
    private void initListener() {


        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        recoverCycle();
                        break;
                }
                return false;
            }
        });

        //ViewPager切换事件
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(adapterGalleryAds!=null&&adapterGalleryAds.getRealCount()!=0) {
                    int nextRealPostion = position % adapterGalleryAds.getRealCount();
                    if (smalldots != null & smalldots.length > 0) {
                        for (int i = 0; i < smalldots.length; i++) {
                            if (nextRealPostion == i) {
                                smalldots[i].setBackgroundResource(R.mipmap.page_focuese);
                            } else {
                                smalldots[i].setBackgroundResource(R.mipmap.page_unfocused);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //初始化小圆点
    private void initSmalDots() {
        if(list_ads.size() == 0)
            return;

        ll_find_ads_dots.removeAllViews();
        // 红点提示初始化
        smalldots = new ImageView[list_ads.size()];
        for (int i = 0; i < smalldots.length; i++) {
            smalldot = new ImageView(mContext);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DeviceUtils.dip2px(mContext,7),
                    DeviceUtils.dip2px(mContext,7));
            lp.setMargins(10, 0, 0, 0);
            smalldot.setLayoutParams(lp);
            smalldots[i] = smalldot;
            if (i == 0) {
                //默认第一个选中
                smalldots[i].setBackgroundResource(R.mipmap.page_focuese);
            } else {
                smalldots[i].setBackgroundResource(R.mipmap.page_unfocused);
            }
            ll_find_ads_dots.addView(smalldots[i]);
        }
     //   isInitDot = true;
    }



    /**
     * 初始化广告位
     */
    public void initAds(List<ModelAds> result) {
        executeDataSuccess(result);
    }




    /**
     * 创建广告圆点，自动开始轮播
     * <p>
     *   由于要有左右可以循环滑动的效果，将viewpager的当前位置定位到  10 * result.size
     * </p>
     *
     * @param result
     */
    private void executeDataSuccess(List<ModelAds> result) {
        list_ads.clear();
        list_ads.addAll(result);
        if(adapterGalleryAds.getRealCount() != 0) {
            adapterGalleryAds.removeDatas();
        }

        adapterGalleryAds.addAds(result);
//        viewPager.setCurrentItem(result.size() * 10);
        // 红点提示
        if (!isInitDot) {
            initSmalDots();
        }
        //开启轮播
        startCycle();
    }

    //广告点击监听接口
    public interface OnAdvertiseClickListener {
        public void onClick(View view);
    }

    /**
     * get the current item position
     * @return
     */
    public int getCurrentPosition(){
        if(adapterGalleryAds == null)
            throw new IllegalStateException("You did not set a slider adapter");

        return viewPager.getCurrentItem() % adapterGalleryAds.getRealCount();

    }

    /**
     * move to next slide.
     */
    public void moveNextPosition(boolean smooth) {

        if (adapterGalleryAds == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if(adapterGalleryAds.getRealCount() <= 0)
            return;
        int nextPosition = viewPager.getCurrentItem() + 1;
//        if(nextPosition<adapterGalleryAds.getRealCount()) {
            viewPager.setCurrentItem(nextPosition, smooth);
//        }
    }

    public void moveToFirstPosition(boolean smooth){
        Message message=Message.obtain();
        message.what=1;
        cycleHandler.sendMessage(message);
    }

    private void moveToFirst(boolean smooth){
        if (adapterGalleryAds == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if(adapterGalleryAds.getRealCount() <= 0)
            return;

        int index=viewPager.getCurrentItem();
        if(index%3==2){
            index=index+1;
        }else {
            index = index - index % 3;
        }
        viewPager.setCurrentItem(index, smooth);
        startCycle();

    }

    public void moveNextPosition() {
        moveNextPosition(true);
    }

    public interface OnClickAdsListener{
        void OnClickAds(ModelAds ads);
    }

    public void setListener(OnClickAdsListener listener) {
        this.listener = listener;
    }

}