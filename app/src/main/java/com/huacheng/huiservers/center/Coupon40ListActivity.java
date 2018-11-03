package com.huacheng.huiservers.center;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.Coupon40MyFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 4.0优惠券
 */
public class Coupon40ListActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.title_rel)
    RelativeLayout titleRel;
    @BindView(R.id.viewpager)
    ViewPager viewPager;


    private String[] titles = {"优惠券", "到店券"};

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.coupon40_list);
        ButterKnife.bind(this);
   //     SetTransStatus.GetStatus(this);//系统栏默认为黑色
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        setTabLayout(titles);
    }


    private void setTabLayout(String[] titles) {
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(titles[i]));
        }
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }

        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        updateTabTextView(tablayout.getTabAt(tablayout.getSelectedTabPosition()), true);
        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
        viewPager.setAdapter(new Coupon40MyFragmentAdapter(getSupportFragmentManager(), titles));
        //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        viewPager.setOffscreenPageLimit(titles.length);

        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在选中的顶部标签时，为viewpager设置currentitem
                viewPager.setCurrentItem(tab.getPosition());
                updateTabTextView(tab, true);
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
     * 字体加粗
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
          //  tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setText(tab.getText());
            tabSelect.setTextColor(this.getResources().getColor(R.color.orange));
        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tab_item_textview);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setText(tab.getText());
            tabUnSelect.setTextColor(this.getResources().getColor(R.color.text_color));
        }
    }

    /**
     * 引用tab item
     *
     * @param currentPosition
     * @return
     */
    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this).inflate(R.layout.coupon40_tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tab_item_textview);
        textView.setText(titles[currentPosition]);
        return view;
    }


    @OnClick({R.id.lin_left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.right:
                Intent intent = new Intent(this, Coupon40RecordingActivity.class);
                startActivity(intent);
                break;
        }
    }


}
