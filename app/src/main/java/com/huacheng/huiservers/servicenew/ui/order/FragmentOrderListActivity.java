package com.huacheng.huiservers.servicenew.ui.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderList;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：我的订单
 * 时间：2018/9/5 17:37
 * created by DFF
 */

public class FragmentOrderListActivity extends BaseActivity {
    // String[] mTitle = new String[]{"待上门", "未付款", "待评价", "已评价", "已取消"};
    FragmentOrderCommon currentFragment;
    List<BaseFragment> mFragments = new ArrayList<>();
    private View view_home_bar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    String typeReceipt = "";

    List<String> mStringList = new ArrayList<>();
    ModelOrderList mModelOrdetcate = new ModelOrderList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        findTitleViews();
        titleName.setText("服务订单");
        //view_home_bar = findViewById(R.id.view_home_bar);
        // view_home_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice.getStatuBarHeight(mContext)));
        mTabLayout = findViewById(R.id.tl_tab);
        mViewPager = findViewById(R.id.vp_pager);

      /*  Bundle arguments = this.getArguments();
        if (arguments != null) {
            typeReceipt = arguments.getString("typeReceipt");
            if (typeReceipt.equals("2")) {
                currentFragment = (FragmentOrderCommon) mFragments.get(2);
                Bundle bundle = new Bundle();
                bundle.putString("typeReceipt", "2");
                bundle.putInt("type", 2);
                currentFragment.setArguments(bundle);
            }
        }*/

    }

    @Override
    public void initIntentData() {

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void initData() {
        requestData();
    }

    @Override
    public void initListener() {
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service_list_order;
    }


    /**
     * 请求数据
     */
    private void requestData() {
        // 根据接口请求数据
        MyOkHttp.get().post(ApiHttpClient.GET_ORDER_COUNT, null, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    mModelOrdetcate = (ModelOrderList) JsonUtil.getInstance().parseJsonFromResponse(response, ModelOrderList.class);
                    mStringList.clear();
                    mStringList.add("全部");
                    int dfw = Integer.parseInt(mModelOrdetcate.getDfw());
                    int dpj = Integer.parseInt(mModelOrdetcate.getDpj());
                    int wc = Integer.parseInt(mModelOrdetcate.getWc());

                    mStringList.add("待服务" + (dfw <= 0 ? "" : dfw));
                    mStringList.add("待评价" + (dpj <= 0 ?"" : dpj));
                    mStringList.add("完成" + (wc <= 0 ? "" : wc));
                    getcate();
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(FragmentOrderListActivity.this, msg, XToast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(FragmentOrderListActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();

            }
        });
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            FragmentOrderCommon fragmentCommon = (FragmentOrderCommon) mFragments.get(position);
            currentFragment = fragmentCommon;
            currentFragment.onTabSelectedRefresh(null);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void getcate() {
        mFragments.clear();
        for (int i = 0; i < mStringList.size(); i++) {
            FragmentOrderCommon fragmentCommon = new FragmentOrderCommon();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragmentCommon.setArguments(bundle);
            mFragments.add(fragmentCommon);
        }

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //此方法用来显示tab上的名字
            @Override
            public CharSequence getPageTitle(int position) {
                // return mTitle[position % mTitle.length];
                return mStringList.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                //return mFragments.get(position % mTitle.length);
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mStringList.size();
            }


        });
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        currentFragment = (FragmentOrderCommon) mFragments.get(0);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(getTabView(i, mStringList.get(i)));
        }
        // updateTabTextView(mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()), true);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        TextView tabSelect = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect.setTextColor(getResources().getColor(R.color.orange));
                        tabSelect.setTextSize(16);

                        break;
                    case 1:
                        TextView tabSelect1 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect1.setTextColor(getResources().getColor(R.color.orange));
                        tabSelect1.setTextSize(16);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallvideoblue);
                        break;
                    case 2:
                        TextView tabSelect2 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect2.setTextColor(getResources().getColor(R.color.orange));
                        tabSelect2.setTextSize(16);
                        //tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallgoodsblue);
                        break;
                    case 3:
                        TextView tabSelect3 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect3.setTextColor(getResources().getColor(R.color.orange));
                        tabSelect3.setTextSize(16);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallnewblue);
                        break;
                    case 4:
                        TextView tabSelect4 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect4.setTextColor(getResources().getColor(R.color.orange));
                        tabSelect4.setTextSize(16);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallnewblue);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        TextView tabSelect = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect.setTextColor(getResources().getColor(R.color.grey96));
                        tabSelect.setTextSize(13);

                        break;
                    case 1:
                        TextView tabSelect1 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect1.setTextColor(getResources().getColor(R.color.grey96));
                        tabSelect1.setTextSize(13);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallvideoblue);
                        break;
                    case 2:
                        TextView tabSelect2 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect2.setTextColor(getResources().getColor(R.color.grey96));
                        tabSelect2.setTextSize(13);
                        //tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallgoodsblue);
                        break;
                    case 3:
                        TextView tabSelect3 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect3.setTextColor(getResources().getColor(R.color.grey96));
                        tabSelect3.setTextSize(13);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallnewblue);
                        break;
                    case 4:
                        TextView tabSelect4 = tab.getCustomView().findViewById(R.id.tabtext);
                        tabSelect4.setTextColor(getResources().getColor(R.color.grey96));
                        tabSelect4.setTextSize(13);
                        // tab.getCustomView().findViewById(R.id.tabtext).setBackgroundResource(R.drawable.mallnewblue);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public View getTabView(int position, String cateName) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.icon_service_tab_view, null);
        //ImageView iv = v.findViewById(R.id.tabicon);
        TextView tv = v.findViewById(R.id.tabtext);
        //iv.setBackgroundResource(IconImg[position]);
        // tv.setText(mTitle[position]);
        tv.setText(cateName);
        if (position == 0) {
            tv.setTextColor(v.getResources().getColor(R.color.orange));
            tv.setTextSize(16);
        }
        return v;
    }

    /**
     * 更新数据（取消订单，评价完成）
     *
     * @param modelOrderList
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOrderList(ModelOrderList modelOrderList) {
        try {
            if (modelOrderList != null) {
                if (modelOrderList.getEvent_type() == 0) {
                    //取消订单
                    if (mTabLayout != null) {
                        TextView textView_dfw = mTabLayout.getTabAt(1).getCustomView().findViewById(R.id.tabtext);
                        TextView textView_wc = mTabLayout.getTabAt(3).getCustomView().findViewById(R.id.tabtext);
                        if (mModelOrdetcate != null) {
                            String dfw_count = mModelOrdetcate.getDfw();
                            int dfw_count_int = Integer.parseInt(dfw_count);
                            mModelOrdetcate.setDfw((dfw_count_int - 1 < 0 ? 0 : dfw_count_int - 1) + "");
                            textView_dfw.setText("待服务" + ("0".equals(mModelOrdetcate.getDfw()) ? "" : mModelOrdetcate.getDfw()));
                            //完成
                            String wc_count = mModelOrdetcate.getWc();
                            int wc_count_int = Integer.parseInt(wc_count);
                            mModelOrdetcate.setWc((wc_count_int + 1) + "");
                            textView_wc.setText("完成" + mModelOrdetcate.getWc());
                        }
                    }
                } else if (modelOrderList.getEvent_type() == 1) {
                    //评价完成
                    if (mTabLayout != null) {
                        TextView textView_dpj = mTabLayout.getTabAt(2).getCustomView().findViewById(R.id.tabtext);
                        TextView textView_wc = mTabLayout.getTabAt(3).getCustomView().findViewById(R.id.tabtext);
                        if (mModelOrdetcate != null) {
                            String dpj_count = mModelOrdetcate.getDpj();
                            int dpj_count_int = Integer.parseInt(dpj_count);
                            mModelOrdetcate.setDpj((dpj_count_int - 1 < 0 ? 0 : dpj_count_int - 1) + "");
                            textView_dpj.setText("待评价" + ("0".equals(mModelOrdetcate.getDpj()) ? "" : mModelOrdetcate.getDpj()));
                            //完成
                            String wc_count = mModelOrdetcate.getWc();
                            int wc_count_int = Integer.parseInt(wc_count);
                            mModelOrdetcate.setWc((wc_count_int + 1) + "");
                            textView_wc.setText("完成" + mModelOrdetcate.getWc());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

