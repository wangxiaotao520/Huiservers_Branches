package com.huacheng.huiservers.facepay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.ZhifuActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/23.
 */

public class FacepayConfirmPaymentActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_confirmInfo)
    TextView tvConfirmInfo;

    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_fullname)
    TextView tvFullname;
    @BindView(R.id.tv_house_desc)
    TextView tvHouseDesc;
    @BindView(R.id.tv_note)
    TextView tvNote;

    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.rel_owner)
    RelativeLayout relOwner;
    @BindView(R.id.lin_owner)
    LinearLayout linOwner;
    @BindView(R.id.tv_sh_name)
    TextView mTvShName;

    String id, order_number, c_name, community_name, building_name,
            unit, floor, code, psname, mp1, money, addtime, note, sh_name;

    public static FacepayConfirmPaymentActivity sFacepayConfirm;


    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.facepay_confirm_payment);
        ButterKnife.bind(this);
   //     SetTransStatus.GetStatus(this);
        sFacepayConfirm = this;
        titleName.setText("请确认信息后付款");
        String sign = getIntent().getStringExtra("sign");
        id = getIntent().getStringExtra("id");
        order_number = getIntent().getStringExtra("order_number");
        c_name = getIntent().getStringExtra("c_name");

        if (sign.equals("1")) {
            linOwner.setVisibility(View.VISIBLE);
            mTvShName.setVisibility(View.GONE);
            community_name = getIntent().getStringExtra("community_name");
            building_name = getIntent().getStringExtra("building_name");
            unit = getIntent().getStringExtra("unit");
            floor = getIntent().getStringExtra("floor");
            code = getIntent().getStringExtra("code");

            psname = getIntent().getStringExtra("psname");
            mp1 = getIntent().getStringExtra("mp1");
            tvFullname.setText("业主信息：" + psname + "/" + mp1);
            tvHouseDesc.setText("房屋：" + community_name + building_name + "号楼" + unit + "单元" + code);
        } else if (sign.equals("2")) {
            mTvShName.setVisibility(View.VISIBLE);
            linOwner.setVisibility(View.GONE);
            sh_name = getIntent().getStringExtra("sh_name");
            mTvShName.setText("商户：" + sh_name);


        } else {
            mTvShName.setVisibility(View.GONE);
            linOwner.setVisibility(View.GONE);

        }
        money = getIntent().getStringExtra("money");
        addtime = getIntent().getStringExtra("addtime");
        note = getIntent().getStringExtra("note");

        tvOrderNumber.setText("下单编号：" + order_number);
        tvOrderDate.setText("下单时间：" + addtime);
        tvNote.setText("备注：" + note);
        tvPrice.setText(c_name + "：" + money + "元");

    }

    @OnClick({R.id.lin_left, R.id.tv_confirmInfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("typeCallback", "facepay");
                bundle.putString("o_idCallback", id);
                intent.putExtras(bundle);
                setResult(999, intent);
                finish();
                break;
            case R.id.tv_confirmInfo:
                //跳支付页后，再跳成功页
                tvConfirmInfo.setEnabled(false);
                againLookOrder();
                /*Intent intent = new Intent(this, FacepayPaymentHistoryActivity.class);
                startActivity(intent);*/
                break;
        }
    }

    /**
     * 确认订单
     */
    private void againLookOrder() {
        Intent intent = new Intent(this, ZhifuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("o_id", id);
        bundle.putString("price", money);
        bundle.putString("type", "facepay");
        bundle.putString("order_type", "dm");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                if (resultCode == 998) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("typeCallback", "facepay");
                    bundle.putString("o_idCallback", id);
                    intent.putExtras(bundle);
                    setResult(999, intent);
                    finish();
                }
                break;
        }
    }*/
}
