package com.huacheng.huiservers.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.FaBuActivity;
import com.huacheng.huiservers.cricle.MyCircleActivity;
import com.huacheng.huiservers.cricle.bean.CircleDetail4Bean;
import com.huacheng.huiservers.cricle.bean.ModelRefreshCircle;
import com.huacheng.huiservers.fragment.circle.TabFragment;
import com.huacheng.huiservers.geren.adapter.MyFragmentPagerAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.CircleProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.PopupWindowUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class CircleFragment5 extends BaseFragmentOld implements View.OnClickListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout rel_editText;

    List<BannerBean> tabs;
    private ArrayList<TabFragment> mFragmentList = new ArrayList<TabFragment>();
    private TabFragment currentFragment;
    private SharedPreferences preferencesLogin;
    private String login_type;

    MyFragmentPagerAdapter pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        //    SetTransStatus.GetStatus(getActivity());
        MyCookieStore.CircleOwn_notify = 0;
        //
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        rel_editText = (RelativeLayout) findViewById(R.id.rel_editText);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        initData();
    }

    /**
     * 获取数据
     */
    public void initData() {
        showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        new HttpHelper(info.getSocialCategory + "/sign/0", new RequestParams(), getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                tabs = new CircleProtocol().getSocialCategory(json);
                if (tabs.size() > 0) {
                    setTabLayout(tabs);
                } else {
                    //WaitDIalog.closeDialog(MyCookieStore.waitDialogc);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
        rel_editText.setOnClickListener(this);
    }

    /**
     * 处理内容
     *
     * @param tabs
     */
    private void setTabLayout(final List<BannerBean> tabs) {
        for (int i = 0; i < tabs.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabs.get(i).getC_name()));
        }
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        updateTabTextView(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()), true);
        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        for (int i = 0; i < tabs.size(); i++) {
            TabFragment tabFragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("mCid", tabs.get(i).getId());
            bundle.putInt("mPro", tabs.get(i).getIs_pro());//是否是物业公告字段
            bundle.putString("type", i + "");
            tabFragment.setArguments(bundle);
            mFragmentList.add(tabFragment);
        }
        currentFragment = mFragmentList.get(0);
        pager = new MyFragmentPagerAdapter(getChildFragmentManager(), tabs, mFragmentList);
        viewPager.setAdapter(pager);
        //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(tabs.size()); //0 设置缓存页面，当前页面的相邻N各页面都会被缓存

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                viewPager.setCurrentItem(tab.getPosition());
                updateTabTextView(tab, true);
//                if (MyCookieStore.CircleOwn_notify == 1) {
//                    pager.notifyDataSetChanged();
//                }
                currentFragment = mFragmentList.get(tab.getPosition());
                if (currentFragment != null) {
                    currentFragment.selected_init();
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

    }

    /**
     * 引用tab item
     *
     * @param currentPosition
     * @return
     */
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.circle5_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(tabs.get(currentPosition).getC_name());
        return view;
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
            tabSelect.setTextSize(20);
            tabSelect.setText(tab.getText());
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
//            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(getResources().getColor(R.color.black_jain_87));
            tabUnSelect.setTextSize(16);
            tabUnSelect.setText(tab.getText());
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_circle5;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_editText:
                preferencesLogin = getActivity().getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");

                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(getActivity(), LoginVerifyCode1Activity.class));
                } else {
                    showPopupWindow(rel_editText);
                }
                break;
        }
    }

    PopupWindow mPopupWindow;

    private void showPopupWindow(final View anchorView) {
        showDialog(smallDialog);
        final Url_info info = new Url_info(getActivity());
        new HttpHelper(info.getSocialNum, new RequestParams(), getActivity()) {//我的圈子

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                CircleDetail4Bean circleNumBean = new CircleProtocol().getMyCircleNum(json);
                if (circleNumBean != null) {
                    String num = circleNumBean.getSocial_num();
                    View contentView;
                    if (!num.equals("0")) {
                        contentView = getPopupWindowContentView(num);
                    } else {
                        contentView = getPopupWindowContentView(num);
                    }
                    mPopupWindow = new PopupWindow(contentView,
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
                    mPopupWindow.setBackgroundDrawable(new ColorDrawable());
                    //设置屏幕外阴影
                    popOutShadow(mPopupWindow);
                    // 设置好参数之后再show
                    int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
                    int xOff = 20; // 可以自己调整偏移
                    windowPos[0] -= xOff;
                    mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private View getPopupWindowContentView(String num) {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_content_layout;   // 布局ID
        final View contentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
        final TextView item1 = (TextView) contentView.findViewById(R.id.menu_item1_num);
        if (!num.equals("0")) {
            item1.setText(num);
        } else {
            item1.setText("");
        }
        contentView.findViewById(R.id.lin_menu_item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyCircleActivity.class));
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        contentView.findViewById(R.id.menu_item2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FaBuActivity.class));
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        });
        return contentView;
    }

    /**
     * 定位到指定tab并刷新
     *
     * @param modelRefreshCircle
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCircle(ModelRefreshCircle modelRefreshCircle) {
        if (modelRefreshCircle != null) {
            String tab_id = modelRefreshCircle.getTab_id();
            int position = 0;
            if (tabs != null) {
                for (int i = 0; i < tabs.size(); i++) {
                    if (tabs.get(i).getId().equals(tab_id)) {
                        position = i;
                    }
                }
                if (mFragmentList.size() > 0 && position < mFragmentList.size()) {
                    TabFragment tabFragment = mFragmentList.get(position);
                    if (currentFragment == tabFragment) {//是当前fragment直接刷新
                        currentFragment.isInit = false;
                        currentFragment.selected_init();
                    } else {
                        tabFragment.isInit = false;//其他的切换即可
                        viewPager.setCurrentItem(position);
                    }

                }

            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f;//设置阴影透明度
        mActivity.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }
}
