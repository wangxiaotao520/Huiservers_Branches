package com.huacheng.huiservers.center;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.Coupon40ListMyAdapter;
import com.huacheng.huiservers.center.adapter.Coupon40ListToShopAdapter;
import com.huacheng.huiservers.center.bean.CouponBean;
import com.huacheng.huiservers.fragment.BaseFragmentOld;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.shop.ConfirmOrderActivity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class Coupon40ToShopFragment extends BaseFragmentOld {

    ScrollView scrollView1;
    LinearLayout lin_coupon40, lin_unusedCoupon;
    MyListView listview_myCoupon40, listview_unusedCoupon;
    Coupon40ListToShopAdapter toShopMyAdapter, toShopUnusedAdapter;
    Coupon40ListMyAdapter myAdapter, unusedAdapter;
    RelativeLayout rel_no_data1;

    CouponBean couponBean;
    List<CouponBean> myCoupon40list, unusedCouponlist;
    String tag, coupon_id, all_shop_id, all_shop_money, jump_url, url, mID;
    CenterProtocol protocol = new CenterProtocol();

    Intent intent = new Intent();
    Bundle bundle = new Bundle();
    Dialog waitDialog;

    public Coupon40ToShopFragment() {
        super();
    }

    public Coupon40ToShopFragment(String id) {
        super();
        mID = id;
    }


    @Override
    protected void initView() {
    //    SetTransStatus.GetStatus(getActivity());//系统栏默认为黑色
        scrollView1 = findViewById(R.id.scrollView1);
        lin_coupon40 = findViewById(R.id.lin_coupon40);
        lin_unusedCoupon = findViewById(R.id.lin_unusedCoupon);
        listview_myCoupon40 = findViewById(R.id.listview_myCoupon40);
        listview_unusedCoupon = findViewById(R.id.listview_unusedCoupon);

        rel_no_data1 = findViewById(R.id.rel_no_data);

        tag = getActivity().getIntent().getExtras().getString("tag");
        if (tag.equals("order")) {
            all_shop_id = getActivity().getIntent().getExtras().getString("all_id");
            all_shop_money = getActivity().getIntent().getExtras().getString("all_shop_money");
        } else if (tag.equals("jump")) {
            jump_url = getActivity().getIntent().getExtras().getString("url");
        }
        getdata();

        /*listview_myCoupon40.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tag.equals("center")) {
                    Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", myCoupon40list.get(position - 1).getId());
                    bundle.putString("c_id", myCoupon40list.get(position - 1).getC_id());
                    bundle.putString("categroy_id", myCoupon40list.get(position - 1).getCategory_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (tag.equals("jump")) {
                    Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", myCoupon40list.get(position - 1).getId());
                    bundle.putString("c_id", myCoupon40list.get(position - 1).getC_id());
                    bundle.putString("categroy_id", myCoupon40list.get(position - 1).getCategory_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    coupon_id = myCoupon40list.get(position - 1).getId();
                    getCoupon();
                }
            }
        });*/

    }

    private void getdata() {
        Url_info info = new Url_info(getActivity());
        String servers = MyCookieStore.SERVERADDRESS + "userCenter/my_coupon_40/";

        if (tag.equals("center")) {
            url = servers + "coupon_type/" + "1" + "/";
        } else if (tag.equals("jump")) {
            url = jump_url + "/coupon_type/" + "1" + "/";
        } else {
            url = servers + "category_id/" + "54" +
                    "/shop_id_str/" + all_shop_id + "/amount/" + all_shop_money + "/coupon_type/" + "1" + "/";
        }
        new HttpHelper(url, getActivity()) {// new RequestParams(),

            @Override
            protected void setData(String json) {
                couponBean = protocol.getCoupon40List(json);
                if (couponBean != null) {
                    rel_no_data1.setVisibility(View.GONE);
                    scrollView1.setVisibility(View.VISIBLE);

                    myCoupon40list = couponBean.getMy_coupon_list();//领取的优惠券
                    if (myCoupon40list != null) {
                        if (myCoupon40list.size() > 0) {
                            lin_coupon40.setVisibility(View.VISIBLE);

                            toShopMyAdapter = new Coupon40ListToShopAdapter(myCoupon40list, 1, getActivity());
                            listview_myCoupon40.setAdapter(toShopMyAdapter);

                            listview_myCoupon40.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    toUse(position);
                                }
                            });

                        } else {
                            lin_coupon40.setVisibility(View.GONE);
                        }

                    } else {
                        lin_coupon40.setVisibility(View.GONE);
                    }

                    unusedCouponlist = couponBean.getCoupon_list();//未领取的优惠券
                    if (unusedCouponlist != null) {
                        if (unusedCouponlist.size() > 0) {
                            lin_unusedCoupon.setVisibility(View.VISIBLE);

                            toShopUnusedAdapter = new Coupon40ListToShopAdapter(unusedCouponlist, 0, getActivity());
                            listview_unusedCoupon.setAdapter(toShopUnusedAdapter);

                            listview_unusedCoupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    XToast.makeText(getActivity(), "remove" + unusedCouponlist.get(position).getId(), XToast.LENGTH_SHORT).show();
                                    //点击领取
                                    couponAdd(unusedCouponlist.get(position).getId());
                                }
                            });
                        } else {
                            lin_unusedCoupon.setVisibility(View.GONE);
                        }
                    } else {
                        lin_unusedCoupon.setVisibility(View.GONE);
                    }
                    if (myCoupon40list == null && unusedCouponlist == null) {
                        rel_no_data1.setVisibility(View.VISIBLE);
                        scrollView1.setVisibility(View.GONE);
                    }
                } else {
                    rel_no_data1.setVisibility(View.VISIBLE);
                    scrollView1.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }


    /**
     * 跳转到详情页
     *
     * @param position
     */
    private void toUse(int position) {
        if (tag.equals("center") || tag.equals("jump")) {
            Intent intent = new Intent(getActivity(), Coupon40ToshopUseActivity.class);
            intent.putExtra("coupon_id", myCoupon40list.get(position).getId());
/*            intent.putExtra("tag",tag);
            intent.putExtra("coupon_id", all_shop_id);
            intent.putExtra("coupon_id", all_shop_money);*/
            startActivityForResult(intent, 100);
        } else {
            coupon_id = myCoupon40list.get(position).getM_c_id();
//            XToast.makeText(getActivity(), "这次不选择优惠券，coupon_id=" + coupon_id, XToast.LENGTH_SHORT).show();
            getCoupon();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == 1) {
                getdata();
            }
        }

    }

    private void getCoupon() {//选择优惠券
        showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", coupon_id);
        new HttpHelper(info.select_poupon, params, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                couponBean = protocol.getOrderCouPon(json);
                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("coupon_id", coupon_id);
                bundle.putString("coupon_price", couponBean.getAmount());
                bundle.putString("coupon_name", couponBean.getName());
                intent.putExtras(bundle);
                getActivity().setResult(100, intent);
                getActivity().finish();
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
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("coupon_id", id);
        new HttpHelper(info.coupon_add, params, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String str = new CenterProtocol().setStatus(json);
                if (str.equals("1")) {
                    getdata();
                    toShopUnusedAdapter.notifyDataSetChanged();
//                    toShopMyAdapter.notifyDataSetChanged();
                    XToast.makeText(getActivity(), "领取到店优惠券成功", XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(getActivity(), str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_coupon40_toshop;
    }
}
