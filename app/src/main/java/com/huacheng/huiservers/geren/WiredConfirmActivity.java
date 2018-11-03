package com.huacheng.huiservers.geren;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.WiredBean;
import com.huacheng.huiservers.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2018/5/18 09:19
 * 功能描述:Huiservers
 */
public class WiredConfirmActivity extends BaseUI {


    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_date)
    TextView mTvOrderDate;
    @BindView(R.id.tv_wiredId)
    TextView mTvWiredId;
    @BindView(R.id.tv_fullname)
    TextView mTvFullname;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_confirmInfo)
    TextView mTvConfirmInfo;

    WiredBean mWiredBean;
    String wiredlID;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.wired_confirm_info);
  //      SetTransStatus.GetStatus(this);
        ButterKnife.bind(this);
        mTitleName.setText("确认订单");

        mWiredBean = (WiredBean) getIntent().getExtras().getSerializable("mWiredBean");
        wiredlID = getIntent().getExtras().getString("wiredlID");

        mTvOrderNumber.setText("订单编号：" + mWiredBean.getOrder_number());
        mTvOrderDate.setText("下单时间：" + StringUtils.getDateToString(mWiredBean.getAddtime(), "7"));
        mTvWiredId.setText("卡号：" + wiredlID);
        mTvFullname.setText("姓名：" + mWiredBean.getFullname());
        mTvPrice.setText("有线电视缴费：" + mWiredBean.getAmount() + "元");


    }

    @OnClick({R.id.lin_left, R.id.tv_confirmInfo})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.lin_left:
                intent = new Intent();
                bundle.putString("typeCallback", "wired");
                bundle.putString("o_idCallback", mWiredBean.getId());
                intent.putExtras(bundle);
                setResult(1, intent);
                finish();
                break;
            case R.id.tv_confirmInfo:
                intent=new Intent(this, ZhifuActivity.class);
                bundle.putString("o_id", mWiredBean.getId());
                bundle.putString("price", mWiredBean.getAmount());
                bundle.putString("type", "wired");
                bundle.putString("order_type", "yx");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("typeCallback", "wired");
            bundle.putString("o_idCallback", mWiredBean.getId());
            intent.putExtras(bundle);
            setResult(1, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
