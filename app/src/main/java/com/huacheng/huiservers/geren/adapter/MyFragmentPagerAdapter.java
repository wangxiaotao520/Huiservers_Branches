package com.huacheng.huiservers.geren.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huacheng.huiservers.fragment.circle.TabFragment;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.List;


/**
 * @Author: pyz
 * @Package: com.pyz.viewpagerdemo.adapter
 * @Description: TODO
 * @Project: ViewPagerDemo
 * @Date: 2016/8/18 11:49
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BannerBean> mTabs;
    private List<TabFragment> mFragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm, List<BannerBean> tabs,List<TabFragment> fragment5List) {
        super(fm);
        this.mTabs = tabs;
        this.mFragmentList=fragment5List;
    }

    @Override
    public Fragment getItem(int position) {
       // return new TabFragment(mTabs.get(position).getId());
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getC_name();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    /*默认返回POSITION_UNCHANGED，所以如果你不对Adapter的getItemPosition进行重写的画，就会出现无法更改内部视图效果的问题。
    下面对从PagerAdapter继承的代码进行改造，加上对getItemPosition的修改，让他直接返回POSITION_NONE，POSITION_NONE每次数据发生变化，
    都会引起视图的重建，比较消耗内存，所以不需要变化内部视图时，避免使用。*/
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
