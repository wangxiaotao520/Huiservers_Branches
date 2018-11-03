package com.huacheng.huiservers.servicenew.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Badge on 2018/9/7.
 */

public class ServicexSearchActivity extends BaseActivity {

    String[] mTitle = new String[]{"商家", "服务"};
    List<BaseFragment> mFragments = new ArrayList<>();
    TabLayout mTabLayout;
    ViewPager mViewPager;
    EditText editText;
    TextView tvBtnSearch;
    FragmentServicexSearchCommon currentFragment;

    @Override
    protected void initView() {
        editText = findViewById(R.id.et_search);

        for (int i = 0; i < mTitle.length; i++) {
            FragmentServicexSearchCommon fragmentCommon = new FragmentServicexSearchCommon();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragmentCommon.setArguments(bundle);
            mFragments.add(fragmentCommon);
        }

        tvBtnSearch = findViewById(R.id.txt_search);
        mTabLayout = findViewById(R.id.tl_tab);
        mViewPager = findViewById(R.id.vp_pager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

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

        currentFragment = (FragmentServicexSearchCommon) mFragments.get(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 11) {
                    String keywords = data.getStringExtra("keywords");
                    editText.setText(keywords);
                    editText.setSelection(keywords.length());
                }
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        findViewById(R.id.search_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToolUtils(ServicexSearchActivity.this, editText).closeInputMethod();
                finish();
            }
        });

        mViewPager.setOnPageChangeListener(onPageChangeListener);
        tvBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyWords = editText.getText().toString();
                if (!NullUtil.isStringEmpty(keyWords)) {

                    if (currentFragment != null) {
                        currentFragment.searchKeyword(keyWords);
                    }
                }
            }
        });

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            FragmentServicexSearchCommon fragmentCommon = (FragmentServicexSearchCommon) mFragments.get(position);
            currentFragment = fragmentCommon;

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.serverx_search;
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
}
