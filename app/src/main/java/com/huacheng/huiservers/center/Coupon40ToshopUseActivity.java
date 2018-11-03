package com.huacheng.huiservers.center;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.Coupon40ToShopUseAdapter;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/4.
 */

public class Coupon40ToshopUseActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.tv_toshopuse_title)
    TextView tvToshopuseTitle;

    @BindView(R.id.tv_howManywhatCoupon)
    TextView tvHowManywhatCoupon;
    @BindView(R.id.tv_died_date)
    TextView tvDiedDate;
    @BindView(R.id.tv_coupon_toshopUse_status)
    TextView tvCouponToshopUseStatus;
    @BindView(R.id.listview_use)
    MyListView listviewUse;
    @BindView(R.id.iv_toshopuse_bg)
    ImageView ivToshopuseBg;
    String couponID;
    CouponBean coupon;
    BitmapUtils bitmapUtils;
    @BindView(R.id.scrollViewDialog)
    ScrollView scrollViewDialog;

    String tag, coupon_id, all_shop_id, all_shop_money, jump_url, url, mID;

    @Override
    protected void init() {
        super.init();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.coupon40_toshopuse);
        ButterKnife.bind(this);
        //   SetTransStatus.GetStatus(this);//系统栏默认为黑色

        bitmapUtils = new BitmapUtils(this);
        couponID = getIntent().getStringExtra("coupon_id");

       /* if (tag.equals("order")) {
            all_shop_id = getIntent().getExtras().getString("all_id");
            all_shop_money = getIntent().getExtras().getString("all_shop_money");
        } else if (tag.equals("jump")) {
            jump_url = getIntent().getExtras().getString("url");
        }*/
        if (!StringUtils.isEmpty(couponID)) {
            getDetail(couponID);
        } else {
            XToast.makeText(this, "couponID不允许为空", XToast.LENGTH_SHORT).show();
        }

    }


    @OnClick({R.id.lin_left, R.id.tv_coupon_toshopUse_status})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
        }
    }

    String couponStatus;

    private void getDetail(String couponID) {//详情
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", couponID);
        new HttpHelper(info.coupon_details_40, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                coupon = new CenterProtocol().getCoupon40Detail(json);
                if (coupon != null) {
                    String boxImg = coupon.getBox_img();
                    if (!StringUtils.isEmpty(boxImg)) {
                        bitmapUtils.display(ivToshopuseBg, coupon.getBox_img());
                        ivToshopuseBg.getLayoutParams().width = ToolUtils.getScreenWidth(Coupon40ToshopUseActivity.this);
                        Double d = Double.valueOf(ToolUtils.getScreenWidth(Coupon40ToshopUseActivity.this)) / (Double.valueOf(coupon.getImg_size()));
                        ivToshopuseBg.getLayoutParams().height = (new Double(d)).intValue();
                    }
                    tvToshopuseTitle.setText(coupon.getAddress());


                    String amount = coupon.getAmount();
                    if (!StringUtils.isEmpty(amount)) {
                        double doubleAmount = Double.valueOf(amount);
                        if (doubleAmount > 1) {
                            tvHowManywhatCoupon.setText(doubleAmount + "元" + coupon.getCategory_name());
                        } else {
                            if (doubleAmount > 0 && doubleAmount < 1) {
                                String decimal = (doubleAmount + "").substring(2);
                                tvHowManywhatCoupon.setText(decimal + "折" + coupon.getCategory_name());
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(coupon.getEndtime())) {
                        tvDiedDate.setText("有效期至 " + StringUtils.getDateToString(coupon.getEndtime(), "2"));
                    }
                    couponStatus = (coupon.getCoupon_status());
                    String couponStatustxt = "";
                    //
                    if (!StringUtils.isEmpty(couponStatus)) {
                        if (couponStatus.equals("0")) {
                            couponStatustxt = "立即领取";
                            tvCouponToshopUseStatus.setEnabled(true);
                            tvCouponToshopUseStatus.setBackground(getResources().getDrawable(R.drawable.corners5_green_use));
                        } else if (couponStatus.equals("1")) {
                            couponStatustxt = "立即使用";
                            tvCouponToshopUseStatus.setEnabled(true);
                            tvCouponToshopUseStatus.setBackground(getResources().getDrawable(R.drawable.corners5_green_use));
                        } else if (couponStatus.equals("2")) {
                            couponStatustxt = "已使用";
                            tvCouponToshopUseStatus.setEnabled(false);
                            tvCouponToshopUseStatus.setBackground(getResources().getDrawable(R.drawable.corners5_green_use_trans));

                        }
                        tvCouponToshopUseStatus.setText(couponStatustxt);
                    }

                    tvCouponToshopUseStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (couponStatus.equals("0")) {
                                couponAdd(coupon.getId());
                            } else if (couponStatus.equals("1")) {
                                new CommomDialog(Coupon40ToshopUseActivity.this, R.style.my_dialog_DimEnabled, "每张优惠券只能使用一次，您确定立即使用？", new CommomDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            getOpen(coupon.getM_c_id());
                                            dialog.dismiss();
                                        }
                                    }
                                }).show();//.setTitle("提示")
                            }
                        }
                    });
                    Coupon40ToShopUseAdapter toShopUseAdapter = new Coupon40ToShopUseAdapter(coupon, Coupon40ToshopUseActivity.this);
                    listviewUse.setAdapter(toShopUseAdapter);
                    scrollViewDialog.setVisibility(View.VISIBLE);

                } else {
                    scrollViewDialog.setVisibility(View.GONE);

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    private void couponAdd(String id) {//领取优惠券
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("coupon_id", id);
        new HttpHelper(info.coupon_add, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String str = new CenterProtocol().setStatus(json);
                if (str.equals("1")) {
                    coupon = null;
                    getDetail(couponID);
                    XToast.makeText(Coupon40ToshopUseActivity.this, "领取到店优惠券成功", XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(Coupon40ToshopUseActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private void getOpen(String m_c_id) {//立即使用，开启使用
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("m_c_id", m_c_id);
        showDialog(smallDialog);
        new HttpHelper(info.use_coupon, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String str = new CenterProtocol().setStatus(json);
                if (str.equals("1")) {

                    XToast.makeText(Coupon40ToshopUseActivity.this, "使用成功", XToast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Coupon40ToshopUseActivity.this, Coupon40RecordingActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    XToast.makeText(Coupon40ToshopUseActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


}
