package com.huacheng.huiservers.house;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crazysunj.cardslideview.CardViewPager;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.card.MySelectCardHandler;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HouseSelectActivity extends BaseUI implements View.OnClickListener {

    @BindView(R.id.viewpager)
    CardViewPager viewPager;

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_rel)
    RelativeLayout titleRel;
    @BindView(R.id.v_head_line)
    View vHeadLine;
    String wuye_type;
    HouseProtocol mHouseProtocol = new HouseProtocol();
    List<HouseBean> mHouseBeanList = new ArrayList<>();

    @Override
    protected void init() {
        super.init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.house_select);

        ButterKnife.bind(this);
        titleRel.setBackground(null);
        titleName.setText("请选择房屋");

        vHeadLine.setVisibility(View.GONE);
        wuye_type = getIntent().getStringExtra("wuye_type");
        getHouseList();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

//                Log.d("MainActivity", "position:" + position + " pos:" + (position % list.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        linLeft.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyCookieStore.fw_delete == 1) {
            getHouseList();
        }
    }

    private void getHouseList() {//获取我的住宅
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.binding_community, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mHouseBeanList = mHouseProtocol.getHouseList(json);
                viewPager.setCardTransformer(180, 0.38f);
                viewPager.setCardPadding(54);
                viewPager.setCardMargin(20);

                viewPager.bind(getSupportFragmentManager(), new MySelectCardHandler(wuye_type), mHouseBeanList);

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
        }
    }

}
