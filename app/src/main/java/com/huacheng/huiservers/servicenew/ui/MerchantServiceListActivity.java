package com.huacheng.huiservers.servicenew.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.huacheng.huiservers.R;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.utils.NullUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/9/6 16:26
 */
public class MerchantServiceListActivity extends BaseActivity {

    String[] mTitle = new String[]{"商家", "服务"};
    private String keyword = "";

    TabLayout mTabLayout;
    ViewPager mViewPager;
    List<BaseFragment> mFragments = new ArrayList<>();
    MerchantServiceListFragmentCommon currentFragment;
    LinearLayout lin_left;
    private String store_id;

    @Override
    protected void initView() {

        for (int i = 0; i < mTitle.length; i++) {
            MerchantServiceListFragmentCommon fragmentCommon = new MerchantServiceListFragmentCommon();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            if (!NullUtil.isStringEmpty(store_id)){
                bundle.putString("store_id",store_id);
            }
            fragmentCommon.setArguments(bundle);
//            fragmentCommon.setListViewOnTouchListener(this);
            mFragments.add(fragmentCommon);
        }

        lin_left = findViewById(R.id.lin_left);
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
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
        currentFragment = (MerchantServiceListFragmentCommon) mFragments.get(0);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        lin_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager.setOnPageChangeListener(onPageChangeListener);

        String tabType = getIntent().getStringExtra("tabType");
        if (!NullUtil.isStringEmpty(tabType)) {
            if ("service".equals(tabType)||"servicePop".equals(tabType)) {
                mTabLayout.getTabAt(1).select();
                mViewPager.setCurrentItem(1);

            }
        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            MerchantServiceListFragmentCommon fragmentCommon = (MerchantServiceListFragmentCommon) mFragments.get(position);
            currentFragment = fragmentCommon;
            fragmentCommon.searchRefresh();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.layout_merchat_service_list;
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        if (intent!=null){
            store_id = intent.getStringExtra("store_id");

        }
    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }


}
