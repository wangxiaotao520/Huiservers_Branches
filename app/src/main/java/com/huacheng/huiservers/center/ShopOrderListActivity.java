package com.huacheng.huiservers.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.huacheng.huiservers.R;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：商城订单
 * 时间：2018/9/19 15:42
 * created by DFF
 */
public class ShopOrderListActivity extends BaseActivity {
    String[] mTitle = new String[]{"全部", "待付款", "待收货", "评价/售后"};
    List<BaseFragment> mFragments = new ArrayList<>();
    TabLayout mTabLayout;
    ViewPager mViewPager;
    String type_back;
    ShopOrderListCommon currentFragment;

    @Override
    protected void initView() {

        findTitleViews();
        titleName.setText("商城订单");
        type_back = this.getIntent().getExtras().getString("type");
        for (int i = 0; i < mTitle.length; i++) {
            ShopOrderListCommon shopOrderListCommon = new ShopOrderListCommon();
            Bundle bundle = new Bundle();
            if (type_back.equals("type_zf_dfk") || type_back.equals("type_zf_dsh")) {
                bundle.putString("type_back", type_back);
            }
            bundle.putInt("type", i);
            shopOrderListCommon.setArguments(bundle);
            mFragments.add(shopOrderListCommon);
        }
        mTabLayout = findViewById(R.id.tl_tab);
        mViewPager = findViewById(R.id.vp_pager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //此方法用来显示tab上的名字
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle[position % mTitle.length];
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position % mTitle.length);
            }

            @Override
            public int getCount() {
                return mTitle.length;
            }
        });
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        if (type_back.equals("type_zf_dfk")) {
            mViewPager.setCurrentItem(1);
        } else if (type_back.equals("type_zf_dsh")) {
            mViewPager.setCurrentItem(2);
        } else {
            currentFragment = (ShopOrderListCommon) mFragments.get(0);
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ShopOrderListCommon fragmentCommon = (ShopOrderListCommon) mFragments.get(position);
            currentFragment = fragmentCommon;
            // 到时候根据不同的参数请求
            currentFragment.onTabSelectedRefresh(null);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {
        mViewPager.setOnPageChangeListener(onPageChangeListener);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_order_list;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 评价完成 申请退款 订单中支付成功
     *
     * @param str_type
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void back(String str_type) {

        if (str_type.equals("pj")) {//评价
            mViewPager.setCurrentItem(0);
        } else if (str_type.equals("tk_sh")) {//退款 以及收货
            mViewPager.setCurrentItem(3);
        } else if (str_type.equals("zf")) {//订单中支付成功
            mViewPager.setCurrentItem(2);
        }
        if (currentFragment != null) {
            currentFragment.onTabRefresh(null);
        }
    }
}
