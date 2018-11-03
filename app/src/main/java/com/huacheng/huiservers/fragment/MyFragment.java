package com.huacheng.huiservers.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.CenterMoneyActivity;
import com.huacheng.huiservers.center.Coupon40ListActivity;
import com.huacheng.huiservers.center.MyInfoActivity2;
import com.huacheng.huiservers.center.MyRenvationActivity;
import com.huacheng.huiservers.center.New_Shop_OrderActivity;
import com.huacheng.huiservers.center.ServiceParticipateActivity;
import com.huacheng.huiservers.center.SetActivity;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
import com.huacheng.huiservers.cricle.MyCircleActivity;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.house.HouseGuideActivity;
import com.huacheng.huiservers.house.HouseInviteActivity;
import com.huacheng.huiservers.house.HousePersonInfoActivity;
import com.huacheng.huiservers.house.HouseSelectActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.servicenew.ui.order.FragmentOrderListActivity;
import com.huacheng.huiservers.shop.ShopCartActivityTwo;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.http.RequestParams;
import com.yan.pullrefreshlayout.PullRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hwh on 2018/3/20.
 */

public class MyFragment extends BaseFragmentOld implements View.OnClickListener {
    private TextView tv_name, txt_yezhu, tv_phone, tv_guest_num, tv_decoration_num,
            tv_social_num, tv_cart_num, tv_order_num, tv_service_num;
    private RelativeLayout ry_set, ry, ry_my_renvation, ry_fang, rel_gouwuche, rel_xiaofejilu, rel_shop_order, rel_fuwu_order, rel_fw_new;
    private LinearLayout lin_yeshurenzheng;
    SharePrefrenceUtil prefrenceUtil;
    CenterProtocol protocol = new CenterProtocol();
    PersoninfoBean bean = new PersoninfoBean();
    private CircularImage img_info;
    private Dialog WaitDialog;
    private LinearLayout lin_my_info, lin_my_fuli;
    SharedPreferences preferencesLogin;
    private String login_type;
    private PullRefreshLayout refreshLayout;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_my;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initView() {// 系统栏默认为黑色
        //     SetTransStatus.GetStatus(getActivity());
        prefrenceUtil = new SharePrefrenceUtil(getActivity());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        tv_name = (TextView) findViewById(R.id.tv_name);
        txt_yezhu = (TextView) findViewById(R.id.txt_yezhu);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_guest_num = (TextView) findViewById(R.id.tv_guest_num);
        tv_decoration_num = (TextView) findViewById(R.id.tv_decoration_num);
        tv_social_num = (TextView) findViewById(R.id.tv_social_num);
        tv_cart_num = (TextView) findViewById(R.id.tv_cart_num);
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_service_num = (TextView) findViewById(R.id.tv_service_num);

        ry_set = (RelativeLayout) findViewById(R.id.ry_set);
        ry = (RelativeLayout) findViewById(R.id.ry);
        ry_fang = (RelativeLayout) findViewById(R.id.ry_fang);
        ry_my_renvation = (RelativeLayout) findViewById(R.id.ry_my_renvation);
        rel_gouwuche = (RelativeLayout) findViewById(R.id.rel_gouwuche);
        rel_xiaofejilu = (RelativeLayout) findViewById(R.id.rel_xiaofejilu);
        rel_shop_order = (RelativeLayout) findViewById(R.id.rel_shop_order);
        rel_fuwu_order = (RelativeLayout) findViewById(R.id.rel_fuwu_order);
        lin_my_info = (LinearLayout) findViewById(R.id.lin_my_info);
        lin_my_fuli = (LinearLayout) findViewById(R.id.lin_my_fuli);
        rel_fw_new = (RelativeLayout) findViewById(R.id.rel_fw_new);
        lin_yeshurenzheng = (LinearLayout) findViewById(R.id.lin_yeshurenzheng);
        refreshLayout = (PullRefreshLayout) findViewById(R.id.refreshLayout);
        lin_my_info.setOnClickListener(this);
        lin_my_fuli.setOnClickListener(this);
        img_info = (CircularImage) findViewById(R.id.img_info);
        findViewById(R.id.rel_myCircle).setOnClickListener(this);

        showDialog(smallDialog);
        getLinshi();
        // inintent();
        getinfo();

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                bean = null;
                getinfo();
                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 500);

            }
        });

        ry.setOnClickListener(this);
        ry_set.setOnClickListener(this);
        ry_fang.setOnClickListener(this);
        ry_my_renvation.setOnClickListener(this);
        rel_gouwuche.setOnClickListener(this);
        rel_xiaofejilu.setOnClickListener(this);
        rel_shop_order.setOnClickListener(this);
        rel_fuwu_order.setOnClickListener(this);
        rel_fw_new.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (MyCookieStore.My_notify == 1 || MyCookieStore.My_info == 1) {
//            getinfo();
//            MyCookieStore.My_notify = 0;
//        }
    }

    private void getLinshi() {
        preferencesLogin = getActivity().getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    @Override
    public void onClick(View v) {
        getLinshi();
        Intent intent;
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.ry:// 个人资料
                intent = new Intent(getActivity(), MyInfoActivity2.class);
                startActivity(intent);
                break;
            case R.id.lin_my_info:// 我的房屋
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(getActivity(), LoginVerifyCode1Activity.class);
                    startActivity(intent);
                } else {
                    if (login_type.equals("1")) {//个人没有绑定跳转绑定界面
                              /*  if (is_wuye.equals("1")) {//
                                    intent = new Intent(getActivity(), HouseGuideActivity.class);
                                    startActivity(intent);

                                } else if (is_wuye.equals("2")) {*/
                        //获取我的住宅列表来判断住宅有几条
                        getHouseList(2);
                        // openPopupWindow(v);
                        // }
                    } else {//
                        XToast.makeText(getActivity(), "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.lin_my_fuli:// 优惠券
                intent = new Intent(getActivity(), Coupon40ListActivity.class);//CouponListActivity
                bundle.putString("tag", "center");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.ry_set:// 设置
                startActivity(new Intent(getActivity(), SetActivity.class));
                break;
            case R.id.rel_myCircle:// 我的圈子
                startActivity(new Intent(getActivity(), MyCircleActivity.class));
                break;
            case R.id.ry_my_renvation:// 我的装修
                if (prefrenceUtil.getIsNew().equals("1")) {
                    startActivity(new Intent(getActivity(), MyRenvationActivity.class));
                } else {
                    XToast.makeText(getActivity(), "该小区暂未开启此功能", XToast.LENGTH_SHORT).show();
                }

                break;
            case R.id.ry_fang:// 访客邀请
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    intent = new Intent(getActivity(), LoginVerifyCode1Activity.class);
                    startActivity(intent);
                } else {
                    if (login_type.equals("1")) {//个人
                       /* if (is_wuye.equals("1")) {//没有绑定跳转绑定界面
                            intent = new Intent(getActivity(), HouseGuideActivity.class);
                            startActivity(intent);

                        } else if (is_wuye.equals("2")) {*/
                        //获取我的住宅列表来判断住宅有几条
                        getHouseList(1);
                        ry_fang.setClickable(false);
                        // openPopupWindow(v);
                        // }
                    } else {//
                        XToast.makeText(getActivity(), "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.rel_gouwuche://购物车
                intent = new Intent(getActivity(), ShopCartActivityTwo.class);
                startActivity(intent);
                //KeepAliveService.connectServer(context, "99905901020101");
                break;
            case R.id.rel_xiaofejilu://消费记录
                intent = new Intent(getActivity(), CenterMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.rel_shop_order://商城订单
                intent = new Intent(getActivity(), New_Shop_OrderActivity.class);
                bundle.putString("status", "1111");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rel_fuwu_order://服务订单
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), ServiceParticipateActivity.class);
                intent1.putExtra("type", "0");
                intent1.putExtra("itag", "1");
                startActivity(intent1);
                break;
            case R.id.rel_fw_new://新服务订单
                intent = new Intent(getActivity(), FragmentOrderListActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    HouseProtocol mHouseProtocol = new HouseProtocol();
    List<HouseBean> mHouseBeanList = new ArrayList<>();

    private void getHouseList(final int i) {//获取我的住宅
        showDialog(smallDialog);
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.binding_community, params, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        mHouseBeanList = mHouseProtocol.getHouseList(json);
                        if (mHouseBeanList.size() == 1) {//当数量为1时直接跳转到家庭成员信息界面
                            Intent intent;
                            if (i == 2) {//我的房屋
                                intent = new Intent(getActivity(), HousePersonInfoActivity.class);
                                intent.putExtra("room_id", mHouseBeanList.get(0).getRoom_id());
                                startActivity(intent);
                            } else {//访客邀请
                                getResult(mHouseBeanList.get(0).getRoom_id());

                            }

                        } else {//否则跳转选择房屋绑定界面
                            Intent intent1 = new Intent(getActivity(), HouseSelectActivity.class);
                            if (i == 2) {//我的房屋
                                intent1.putExtra("wuye_type", "fw_info");//为fw_info时 选择小区完成后跳转到房屋信息
                            } else {
                                intent1.putExtra("wuye_type", "fw_invite");//为fw_info时 选择小区完成后跳转到访客邀请
                            }
                            startActivity(intent1);
                            ry_fang.setClickable(true);
                        }

                    } else {
                        //如果状态为0   直接跳未绑定界面让其绑定,缓存值为1
                        //临时文件存储
                        SharedPreferences preferences1 = getActivity().getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = preferences1.edit();
                        editor.putString("is_wuye", "1");
                        editor.commit();
                        Intent intent = new Intent(getActivity(), HouseGuideActivity.class);
                        startActivity(intent);
                        ry_fang.setClickable(true);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getResult(final String room_id) {//判断邀请功能时候开启
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(info.checkIsAjb, params, getActivity()) {


            @Override
            protected void setData(String json) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Intent intent = new Intent(getActivity(), HouseInviteActivity.class);
                        intent.putExtra("room_id", room_id);
                        startActivity(intent);
                        ry_fang.setClickable(true);

                    } else {
                        ry_fang.setClickable(true);
                        XToast.makeText(getActivity(), jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private int[] image = {R.drawable.icon_my_fw_home, R.drawable.my_2};

    /**
     * 水平滚动的GridView的控制
     */
    private void setHorizontalGridView(int siz, GridView gridView) {
        int size = siz;
//      int length = (int) getActivity().getResources().getDimension(
//              R.dimen.coreCourseWidth);
        int length = 150;
        DisplayMetrics dm = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length) * density);
        int itemWidth = (int) ((length) * density);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(-50); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数


    }

    private void getinfo() {// 个人信息
        Url_info info = new Url_info(getActivity());
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.center_index, params,
                getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getinfo(json);
                if (bean != null) {
                    //昵称为空显示手机号
                    if (bean.getNickname() == null || bean.getNickname().equals("null")) {
                        // txt_name.setText("昵称：请填写昵称");
                        tv_name.setText(bean.getNickname());
                    } else {
                        tv_name.setText(bean.getNickname());
                    }
                    tv_phone.setText(bean.getUsername());
                    if (bean.getGuest_num().equals("0")) {
                        tv_guest_num.setVisibility(View.GONE);
                    } else {
                        tv_guest_num.setText(bean.getGuest_num());
                        tv_guest_num.setVisibility(View.VISIBLE);
                    }
                    if (bean.getDecoration_num().equals("0")) {
                        tv_decoration_num.setVisibility(View.GONE);
                    } else {
                        tv_decoration_num.setText(bean.getDecoration_num());
                        tv_decoration_num.setVisibility(View.VISIBLE);
                    }
                    if (bean.getSocial_num().equals("0")) {
                        tv_social_num.setVisibility(View.GONE);
                    } else {
                        tv_social_num.setText(bean.getSocial_num());
                        tv_social_num.setVisibility(View.VISIBLE);
                    }
                    if (bean.getCart_num().equals("0")) {
                        tv_cart_num.setVisibility(View.GONE);
                    } else {
                        tv_cart_num.setText(bean.getCart_num());
                        tv_cart_num.setVisibility(View.VISIBLE);
                    }
                    if (bean.getOrder_num().equals("0")) {
                        tv_order_num.setVisibility(View.GONE);
                    } else {
                        tv_order_num.setText(bean.getOrder_num());
                        tv_order_num.setVisibility(View.VISIBLE);
                    }
                    if (bean.getService_num().equals("0")) {
                        tv_service_num.setVisibility(View.GONE);
                    } else {
                        tv_service_num.setText(bean.getService_num());
                        tv_service_num.setVisibility(View.VISIBLE);
                    }
                    //头像显示
                    if (!StringUtils.isEmpty(bean.getAvatars())) {
                        Glide.with(getActivity()).load(StringUtils.getImgUrl(bean.getAvatars()))
                                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.facehead1)
                                .into(img_info);
                    }
                    //判断是否是业主 物业住宅绑定
                    if (bean.getIs_bind_property().equals("1")) {//未绑定
                        txt_yezhu.setVisibility(View.GONE);
                        lin_yeshurenzheng.setVisibility(View.VISIBLE);
                        lin_yeshurenzheng.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), HouseGuideActivity.class);
                                startActivity(intent);

                            }
                        });
                    } else {
                        txt_yezhu.setVisibility(View.VISIBLE);
                        lin_yeshurenzheng.setVisibility(View.GONE);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(PersoninfoBean bean) {
        getinfo();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}

