package com.huacheng.huiservers.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.huacheng.huiservers.fragment.card.ShopCardFragment;
import com.huacheng.huiservers.shop.bean.BannerBean;

import java.util.ArrayList;
import java.util.List;

public class ShopCardPagerGgAdapter extends FragmentStatePagerAdapter implements HomeCardAdapter {

    private List<BannerBean> mActs;
    private List<ShopCardFragment> mFragments;
    private float mBaseElevation;

    public ShopCardPagerGgAdapter(List<BannerBean> acts, FragmentManager fm, float baseElevation) {
        super(fm);
        mActs = acts;
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;

        for (int i = 0; i < acts.size(); i++) {
            addCardFragment(new ShopCardFragment(acts,i));
        }
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (ShopCardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(ShopCardFragment fragment) {
        mFragments.add(fragment);
    }

}
