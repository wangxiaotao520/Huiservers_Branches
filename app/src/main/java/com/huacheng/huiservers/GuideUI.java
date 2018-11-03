package com.huacheng.huiservers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huacheng.huiservers.center.XiaoquActivity;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.utils.CacheUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首次进入应用的欢迎页
 *
 * @author Demon
 */
public class GuideUI extends BaseActivity implements OnPageChangeListener {

    private ViewPager viewPager;
    private List<ImageView> imageViewList;
    private SharePrefrenceUtil sharePrefrenceUtil;
    private String login_type;
    SharedPreferences preferencesLogin;

    private String token;
    private String tokenSecret;

    @Override
    protected void initView() {
        isStatusBar = true;
        sharePrefrenceUtil = new SharePrefrenceUtil(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    @Override
    protected void initData() {
        int[] imageResIds = { R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};//
        imageViewList = new ArrayList<ImageView>();
        ImageView iv;
        for (int i = 0; i < imageResIds.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageResIds[i]);
            imageViewList.add(iv);
        }
        MyAdapter mAdapter = new MyAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == (imageViewList.size() - 1)) {
            CacheUtils.putBoolean(this, SplashUI.IS_FIRST_OPEN, false);
            Timer timer = new Timer();
            TimerTask tt = new TimerTask() {

                @Override
                public void run() {

                    if (sharePrefrenceUtil.getIsChooseXiaoqu().equals("1")) {
                        Intent intent = new Intent(GuideUI.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent = new Intent(GuideUI.this, XiaoquActivity.class);
                        intent.putExtra("type", "splash");
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.schedule(tt, 1000);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.guide;
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }


    }

}

