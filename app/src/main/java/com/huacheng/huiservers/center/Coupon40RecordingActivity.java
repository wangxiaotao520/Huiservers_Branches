package com.huacheng.huiservers.center;

import android.app.Dialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.Coupon40RecordingAdapter;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.MyListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/16.
 */

public class Coupon40RecordingActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;

    @BindView(R.id.listview_couponUse)
    MyListView listviewCouponUse;
    @BindView(R.id.lin_couponUse)
    LinearLayout linCouponUse;

    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;

    Coupon40RecordingAdapter useAdapter;
    CenterProtocol protocol = new CenterProtocol();
    List<CouponBean> coupons;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    Dialog waitDialog;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.coupon40_recording);
        ButterKnife.bind(this);
    //    SetTransStatus.GetStatus(this);//系统栏默认为黑色
        titleName.setText("记录");
        couponRecording();
    }

    /**
     * 优惠券记录
     */
    private void couponRecording() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        new HttpHelper(info.coupon_over_40, new RequestParams(), this, waitDialog) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                coupons = new CenterProtocol().getCoupon40Recording(json);
                if (coupons != null) {
                    if (coupons.size() > 0) {
                        scrollView.setVisibility(View.VISIBLE);
                        relNoData.setVisibility(View.GONE);
                        useAdapter = new Coupon40RecordingAdapter(coupons, Coupon40RecordingActivity.this);
                        listviewCouponUse.setAdapter(useAdapter);

                    } else {
                        scrollView.setVisibility(View.GONE);
                        relNoData.setVisibility(View.VISIBLE);
                    }
                } else {
                    scrollView.setVisibility(View.GONE);
                    relNoData.setVisibility(View.VISIBLE);

                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @OnClick(R.id.lin_left)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
        }
    }

}
