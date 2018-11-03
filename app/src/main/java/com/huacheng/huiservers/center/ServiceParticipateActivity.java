package com.huacheng.huiservers.center;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.fragment.adapter.ServiceFragmentStatePagerAdapter;
import com.huacheng.huiservers.fragment.service.ServiceFragmentPage;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ServiceProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceParticipateActivity extends BaseUI implements OnClickListener {


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TextView title_name;
    private LinearLayout lin_left;
    private RelativeLayout rel_no_data;
    private View v_head_line;
    private String activityId = "";


    private ServiceProtocol serviceProtocol = new ServiceProtocol();
    private List<ListBean> listBeans;
    private ArrayList<ServiceFragmentPage> mFragmentList = new ArrayList<ServiceFragmentPage>();

    private ServiceFragmentPage currentFragment;
    @Override
    protected void init() {
        super.init();
 //       SetTransStatus.GetStatus(this);// 系统栏默认为黑色
        setContentView(R.layout.service_life_order);
        ButterKnife.bind(this);

        title_name = (TextView) findViewById(R.id.title_name);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        v_head_line = (View) findViewById(R.id.v_head_line);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
        v_head_line.setVisibility(View.GONE);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // 设置刷新控件颜色

        //status = intent.getStringExtra("type");
        activityId = getIntent().getStringExtra("activityId");
        title_name.setText("物业工单");
        getServiceType();

        //listener
        lin_left.setOnClickListener(this);
    }


    //服务订单导航栏数据
    private void getServiceType() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        new HttpHelper(info.service_order_menu, new RequestParams(), ServiceParticipateActivity.this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                listBeans = serviceProtocol.getServceMenu(json);
                if (listBeans != null) {
                    if (listBeans.size() > 0) {
                        setTabLayout(listBeans);
                    }
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    /**
     * 处理内容
     *
     * @param tabs
     */
    private void setTabLayout(List<ListBean> tabs) {
        for (int i = 0; i < tabs.size(); i++) {
            tablayout.addTab(tablayout.newTab().setText(tabs.get(i).getName()));
        }
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        for (int i = 0; i < tabs.size(); i++) {
            ServiceFragmentPage tabFragment = new ServiceFragmentPage();
            Bundle bundle = new Bundle();
            bundle.putString("type",i+"");
            bundle.putSerializable("tab",tabs.get(i));
            tabFragment.setArguments(bundle);
            mFragmentList.add(tabFragment);
        }

        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        updateTabTextView(tablayout.getTabAt(tablayout.getSelectedTabPosition()), true);
        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        ServiceFragmentStatePagerAdapter adapter = new ServiceFragmentStatePagerAdapter(getSupportFragmentManager(), tabs, activityId, this, mFragmentList);
        viewPager.setAdapter(adapter);

        //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setOffscreenPageLimit(tabs.size());
//        tablayout.setupWithViewPager(viewPager);
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                updateTabTextView(tab, true);
                viewPager.setCurrentItem(tab.getPosition());
                currentFragment=mFragmentList.get(tab.getPosition());
                if (currentFragment!=null){
                    currentFragment.selectInit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        //从不同服务类型进来选中对应项
        for (int i = 0; i < tabs.size(); i++) {
            if (!StringUtils.isEmpty(activityId)) {
                if (activityId.equals(tabs.get(i).getId())) {
                    updateTabTextView(tablayout.getTabAt(i), true);
                    viewPager.setCurrentItem(i);
                }
            }
        }

    }

    /**
     * 字体加粗
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
//            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabSelect.setTextSize(16);
            tabSelect.setText(tab.getText());
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
//            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(getResources().getColor(R.color.black_jain_87));
            tabUnSelect.setTextSize(16);
            tabUnSelect.setText(tab.getText());
        }
    }

    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.circle5_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(listBeans.get(currentPosition).getName());
        return view;
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            default:
                break;
        }
    }

}
