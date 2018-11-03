package com.huacheng.huiservers.fragment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.huacheng.huiservers.center.ServiceParticipateActivity;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.fragment.service.ServiceFragmentPage;

import java.util.List;


/**
 * @Author: pyz
 * @Package: com.pyz.viewpagerdemo.adapter
 * @Description: TODO
 * @Project: ViewPagerDemo
 * @Date: 2016/8/18 11:49
 */
public class ServiceFragmentStatePagerAdapter extends FragmentStatePagerAdapter {


    private List<ListBean> mTabs;
    private String mActivityID;

    Context mContext;
    private List<ServiceFragmentPage> mFragmentList ;

    public ServiceFragmentStatePagerAdapter(FragmentManager fm, List<ListBean> tabs, String activityId, ServiceParticipateActivity serviceParticipateActivity,List<ServiceFragmentPage> mFragmentList) {
        super(fm);
        this.mTabs = tabs;
        this.mActivityID = activityId;
        this.mContext = serviceParticipateActivity;
        this.mFragmentList=mFragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        //获取缓存文件夹下参数并加密
//        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("login", 0);
//        String user = mSharedPreferences.getString("login_username", "");
//        String uid = mSharedPreferences.getString("login_uid", "");
//        String str_md5 = MD5.hexdigest(uid + user);
//        return new ServiceFragmentPage(mTabs.get(position), "", str_md5);
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).getName();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }
}
