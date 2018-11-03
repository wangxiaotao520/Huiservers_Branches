package com.huacheng.huiservers.center.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


/**
 * @Author: pyz
 * @Package: com.pyz.viewpagerdemo.adapter
 * @Description:
 * @Project: ViewPagerDemo
 * @Date: 2016/8/18 11:49
 */
public class ShopXS4TimeListFragmentAdapter extends FragmentStatePagerAdapter {
    String[] mTitles;
    private List<Fragment> fragments ;

    public ShopXS4TimeListFragmentAdapter(FragmentManager fm, String[] mTitles,List<Fragment> fragments) {
        super(fm);
        this.mTitles = mTitles;
        this.fragments=fragments;
    }


    @Override
    public Fragment getItem(int position) {
//        if (position == 0) {
//            return new ShopXS4TimeListFragment();
//        } else {
//            return new ShopXS4TimeListFragment1();
//        }
        return fragments.get(position);
        /*if (mTitles[position].equals("优惠券")) {
            return new Coupon40Fragment(mTitles[position]);
        } else if (mTitles[position].equals("到店券")) {
            return new Coupon40ToShopFragment(mTitles[position]);
        }*/

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
