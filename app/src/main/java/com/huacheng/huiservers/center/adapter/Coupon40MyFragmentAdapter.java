package com.huacheng.huiservers.center.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.huacheng.huiservers.center.Coupon40Fragment;
import com.huacheng.huiservers.center.Coupon40ToShopFragment;


/**
 * @Author: pyz
 * @Package: com.pyz.viewpagerdemo.adapter
 * @Description: TODO
 * @Project: ViewPagerDemo
 * @Date: 2016/8/18 11:49
 */
public class Coupon40MyFragmentAdapter extends FragmentStatePagerAdapter {
    private String[] mTitles;

    public Coupon40MyFragmentAdapter(FragmentManager fm, String[] titles) {
        super(fm);

        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (mTitles[position].equals("优惠券")) {
            return new Coupon40Fragment(mTitles[position]);
        } else if (mTitles[position].equals("到店券")) {
            return new Coupon40ToShopFragment(mTitles[position]);
        }
        return null;
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
