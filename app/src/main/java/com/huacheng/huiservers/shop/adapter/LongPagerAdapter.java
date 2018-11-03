package com.huacheng.huiservers.shop.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.TextView;

import com.huacheng.huiservers.fragment.BaseFragmentOld;
import com.huacheng.huiservers.fragment.shop.ShopListFragment;
import com.huacheng.huiservers.shop.bean.CateBean;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/8/22 10:38
 */
public class LongPagerAdapter extends FragmentPagerAdapter {

    List<CateBean> mCates;
    List<ShopListFragment> mFragment5List;
    TextView mTxtShopNum;

    public LongPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public LongPagerAdapter(List<CateBean> cates, TextView txtShopNum, List<ShopListFragment> fragment5List, FragmentManager fm) {
        super(fm);
        this.mCates = cates;
        this.mFragment5List = fragment5List;
        this.mTxtShopNum = txtShopNum;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCates.get(position).getCate_name();
    }

    @Override
    public BaseFragmentOld getItem(int position) {
        return mFragment5List.get(position); //new ShopListFragment(mTxtShopNum);//mCates.get(position).getId(),
    }

    @Override
    public int getCount() {
        return mCates.size();
    }
}
