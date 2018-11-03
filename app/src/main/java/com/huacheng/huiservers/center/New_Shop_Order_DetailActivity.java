package com.huacheng.huiservers.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.XorderDetailBean;
import com.huacheng.huiservers.geren.ZhifuActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.NewPingJiaActivity;
import com.huacheng.huiservers.shop.NewTuikuanActivity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类：
 * 时间：2017/10/23 17:14
 * 功能描述:Huiservers
 */
public class New_Shop_Order_DetailActivity extends BaseUI implements View.OnClickListener {


    public static New_Shop_Order_DetailActivity instant;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.shop_list)
    MyListView mShopList;
    @BindView(R.id.tv_xiadan_time)
    TextView mTvXiadanTime;
    @BindView(R.id.tv_order_num)
    TextView mTvOrderNum;
    @BindView(R.id.tv_pay_type)
    TextView mTvPayType;
    @BindView(R.id.tv_all_price)
    TextView mTvAllPrice;
    /* @BindView(R.id.tv_yunfei_price)
     TextView mTvYunfeiPrice;*/
    @BindView(R.id.tv_shi_price)
    TextView mTvShiPrice;
    @BindView(R.id.tv_pay_time)
    TextView mTvPayTime;
    @BindView(R.id.txt_pay)
    TextView mTxtPay;
    @BindView(R.id.txt_delete)
    TextView mTxtDelete;
    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    @BindView(R.id.tv_liuyan)
    TextView mTvLiuyan;
    private String item_id, jiekouName;
    ShopProtocol mShopProtocol = new ShopProtocol();
    String shopstr;
    List<String> list_info_id = new ArrayList<String>();
    String status_type, order_id, order_num;
    New_ShopOrderDetailAdapter new_shopOrderDetailAdapter;
    CenterProtocol protocol = new CenterProtocol();
    List<XorderDetailBean> XorderDetailBean = new ArrayList<XorderDetailBean>();
    Float totalPrice = (float) 0;
    Float shi_price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shop_orderdetail);
        ButterKnife.bind(this);
        //      SetTransStatus.GetStatus(this);
        instant = this;
        MyCookieStore.WX_notify = 0;
        mTitleName.setText("订单详情");
        order_id = this.getIntent().getExtras().getString("order_id");
        status_type = this.getIntent().getExtras().getString("status");
        order_num = this.getIntent().getExtras().getString("order_num");
        item_id = this.getIntent().getExtras().getString("item_id");

        mLinLeft.setOnClickListener(this);
        mTxtPay.setOnClickListener(this);
        mTxtDelete.setOnClickListener(this);
    }


    @Override
    protected void initData() {
        super.initData();
        getDetail();
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.lin_left:
            /*    intent = new Intent();
                bundle.putString("item_detete_id", "");
                intent.putExtras(bundle);
                setResult(100, intent);*/
                finish();
                break;
            case R.id.txt_pay://支付
                for (int i = 0; i < XorderDetailBean.size(); i++) {
                    if (XorderDetailBean.get(i).getIs_shop().equals("1")) {
                        XToast.makeText(New_Shop_Order_DetailActivity.this, "该订单含有已下架商品，请重新下单", XToast.LENGTH_SHORT).show();
                    } else {
                        MyCookieStore.item_delete_id = item_id;
                        intent = new Intent(New_Shop_Order_DetailActivity.this, ZhifuActivity.class);
                        bundle.putString("o_id", XorderDetailBean.get(0).getOid());
                        bundle.putString("price", shi_price + "");
                        bundle.putString("type", "shop");
                        bundle.putString("order_type", "gw");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 22222);
                    }
                }
                break;
            case R.id.txt_delete://删除
                MyCookieStore.item_delete_id = item_id;

                new CommomDialog(New_Shop_Order_DetailActivity.this, R.style.my_dialog_DimEnabled, "确认要删除吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            list_info_id.clear();
                            for (int j = 0; j < XorderDetailBean.size(); j++) {
                                String info_id = XorderDetailBean.get(j).getId();
                                list_info_id.add(info_id);
                            }
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < list_info_id.size(); i++) {
                                if (i == 0) {
                                    sb.append(String.valueOf(list_info_id.get(i)));
                                } else {
                                    sb.append("," + String.valueOf(list_info_id.get(i)));
                                }
                            }
                            System.out.println("删除sb-------" + sb.toString());
                            getdataStr(sb.toString());
                            dialog.dismiss();
                        } else {
                            list_info_id.clear();
                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")
                break;
        }
    }

    private void getDetail() {//详情
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", order_id);
        params.addBodyParameter("status", status_type);
        new HttpHelper(info.shop_order_details, params, New_Shop_Order_DetailActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                // XorderDetailBean.clear();
                XorderDetailBean = protocol.getXOrderDetail(json);
                if (XorderDetailBean != null || XorderDetailBean.size() > 0) {
                    mTvXiadanTime.setText("下单日期：" + StringUtils.getDateToString(XorderDetailBean.get(0).getAddtime(), "1"));//下单日期
                    mTvOrderNum.setText("订单编号：" + order_num);//下单日期

                    String str = XorderDetailBean.get(0).getIs_pay();
                    if (str.equals("1")) {
                        mTvPayType.setText("支付方式：未支付");
                        mTvPayTime.setText("支付日期：无");//发货时间
                        mTxtPay.setVisibility(View.VISIBLE);
                        mTxtDelete.setVisibility(View.VISIBLE);
                    } else {
                        if (XorderDetailBean.get(0).getPay_type().equals("alipay")) {
                            mTvPayType.setText("支付方式：支付宝");//支付方式
                        } else if (XorderDetailBean.get(0).getPay_type().equals("wxpay")) {
                            mTvPayType.setText("支付方式：微信支付");//支付方式
                        } else if (XorderDetailBean.get(0).getPay_type().equals("bestpay")) {
                            mTvPayType.setText("支付方式：翼支付");//支付方式
                        } else if (XorderDetailBean.get(0).getPay_type().equals("hcpay")) {
                            mTvPayType.setText("支付方式：一卡通支付");//支付方式
                        }
                        mTvPayTime.setText("支付日期：" + StringUtils.getDateToString(XorderDetailBean.get(0).getPay_time(), "1"));//支付时间
                        mTxtPay.setVisibility(View.GONE);
                        mTxtDelete.setVisibility(View.GONE);
                    }
                    /*//判断是否是待付款
                    if (status_type.equals("1") || XorderDetailBean.get(0).getStatus().equals("1")) {
                        mTxtPay.setVisibility(View.VISIBLE);
                    } else {
                        mTxtPay.setVisibility(View.GONE);
                    }*/
                    if (XorderDetailBean.get(0).getM_c_id().equals("")) {
                        mTvCoupon.setText("优惠使用：无");
                    } else {
                        mTvCoupon.setText("优惠使用：" + XorderDetailBean.get(0).getM_c_name());
                    }
                    if (XorderDetailBean.get(0).getDescription().equals("")) {
                        mTvLiuyan.setText("买家留言：无");
                    } else {
                        mTvLiuyan.setText("买家留言：" + XorderDetailBean.get(0).getDescription());
                    }
                    for (int i = 0; i < XorderDetailBean.size(); i++) {
                        totalPrice += Float.valueOf(XorderDetailBean.get(i).getNumber()) *
                                Float.valueOf(XorderDetailBean.get(i).getPrice());
                        shi_price = totalPrice - Float.parseFloat(XorderDetailBean.get(i).getM_c_amount());
                    }
                    setFloat();
                    setFloat2();
                    mTvAllPrice.setText("商品总金额：¥" + totalPrice);
                    mTvShiPrice.setText("实付：¥" + shi_price);
                    new_shopOrderDetailAdapter = new New_ShopOrderDetailAdapter(New_Shop_Order_DetailActivity.this, XorderDetailBean);
                    mShopList.setAdapter(new_shopOrderDetailAdapter);

                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    private void getdataStr(final String id) {
        Url_info info = new Url_info(this);
        //删除订单中商品与确认收货接口   type为1  是删除 订单
        RequestParams params = new RequestParams();
        showDialog(smallDialog);
        params.addBodyParameter("id", id);
        System.out.println("order_id====" + id);
        HttpHelper hh = new HttpHelper(info.del_order, params, New_Shop_Order_DetailActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                shopstr = mShopProtocol.setShop(json);
                if (shopstr.equals("1")) {
                    XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                    XorderDetailBean XorderDetail = new XorderDetailBean();
                    XorderDetail.setId(order_id);
                    XorderDetail.setBack_type(1);
                    EventBus.getDefault().post(XorderDetail);
                    list_info_id.clear();
                 /*     Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("item_detete_id", item_id);
                    intent.putExtras(bundle);
                    setResult(3, intent);*/

                    finish();
                } else {
                    XToast.makeText(context, shopstr, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private int is_tui = 0;

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        String item_detete_id = null;
        if (data == null) { // 判断数据是否为空，就可以解决这个问题
            return;
        } else {
            // if (!TextUtils.isEmpty(data.getExtras().getString("item_detete_id"))) {
            item_detete_id = data.getExtras().getString("item_detete_id");
            System.out.println("@@@@@@@" + item_detete_id);
            //  }
            switch (arg1) {
                case 100:
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("item_detete_id", "");
                    intent.putExtras(bundle);
                    setResult(100, intent);
                    finish();
                    break;
                case 1111:
                    is_tui = 0;
                   /* Intent intent2 = new Intent();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("item_detete_id", "");
                    intent2.putExtras(bundle2);
                    setResult(100, intent2);*/

                    break;
                case 333:
                    Intent intent1 = new Intent();
                    /*Bundle bundle1 = new Bundle();
                    bundle1.putString("item_detete_id", item_id);
                    intent1.putExtras(bundle1);
                    setResult(3, intent1);*/
                    XorderDetailBean XorderDetail2 = new XorderDetailBean();
                    XorderDetail2.setId(order_id);
                    XorderDetail2.setBack_type(4);//支付成功
                    EventBus.getDefault().post(XorderDetail2);
                    finish();
                    break;
                case 4://退款
                   /* is_tui = 1;
                    //XorderDetailBean.clear();
                    totalPrice = (float) 0;
                    shi_price = (float) 0;
                    mTvAllPrice.setText("商品总金额：¥" + "");
                    mTvShiPrice.setText("实付：¥" + "");
                    *//*status_type = data.getExtras().getString("status");
                    getDetail();*/

                    is_tui = 1;
                    totalPrice = (float) 0;
                    shi_price = (float) 0;
                    mTvAllPrice.setText("商品总金额：¥" + "");
                    mTvShiPrice.setText("实付：¥" + "");
                   /* Intent intent3 = new Intent();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("item_detete_id", item_id);
                    intent3.putExtras(bundle3);
                    setResult(444, intent3);*/
                    XorderDetailBean XorderDetail = new XorderDetailBean();
                    XorderDetail.setId(info_id);
                    XorderDetail.setBack_type(3);//退款
                    EventBus.getDefault().post(XorderDetail);
                    finish();
                    break;
                case 5://评价
                    is_tui = 1;
                    //XorderDetailBean.clear();
                    totalPrice = (float) 0;
                    shi_price = (float) 0;
                    mTvAllPrice.setText("商品总金额：¥" + "");
                    mTvShiPrice.setText("实付：¥" + "");
                    Intent intent2 = new Intent();
                   /* Bundle bundle2 = new Bundle();
                    bundle2.putString("item_detete_id", "");
                    intent2.putExtras(bundle2);
                    setResult(55555, intent2);*/
                    XorderDetailBean XorderDetail1 = new XorderDetailBean();
                    XorderDetail1.setId(info_id);
                    XorderDetail1.setBack_type(2);//评价
                    EventBus.getDefault().post(XorderDetail1);
                    finish();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(arg0, arg1, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            System.out.println("is_tui------" + is_tui);
            System.out.println("item_id--------" + item_id);
          /*  if (is_tui == 1) {
                intent = new Intent();
                bundle.putString("item_detete_id", item_id);
                intent.putExtras(bundle);
                setResult(2, intent);
                finish();
            } else {*/
            intent = new Intent();
            bundle.putString("item_detete_id", "");
            intent.putExtras(bundle);
            setResult(100, intent);
            finish();
            // }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setFloat() {//保留两位小数点
        BigDecimal b = new BigDecimal(totalPrice);
        totalPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    private void setFloat2() {//保留两位小数点
        BigDecimal b = new BigDecimal(shi_price);
        shi_price = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    String info_id = "";

    public class New_ShopOrderDetailAdapter extends BaseAdapter {
        private Context context;
        private String status;
        private List<XorderDetailBean> XorderDetailBean;
        ShopProtocol mShopProtocol = new ShopProtocol();
        String shopstr;

        public New_ShopOrderDetailAdapter(Context context, List<XorderDetailBean> XorderDetailBean) {
            this.context = context;
            this.XorderDetailBean = XorderDetailBean;
        }

        @Override
        public int getCount() {
            return XorderDetailBean.size();

        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int arg0, View contentview, ViewGroup arg2) {
            ViewHolder holder = null;
            if (null == contentview) {
                // 获得ViewHolder对象
                holder = new ViewHolder();
                contentview = LinearLayout.inflate(context, R.layout.new_shop_order_detail_item, null);
                holder.tv_title = (TextView) contentview.findViewById(R.id.tv_title);
                holder.tv_price = (TextView) contentview.findViewById(R.id.tv_price);
                holder.tv_tui = (TextView) contentview.findViewById(R.id.tv_tui);
                holder.tv_pingjia = (TextView) contentview.findViewById(R.id.tv_pingjia);
                holder.iv_img = (ImageView) contentview.findViewById(R.id.iv_img);
                holder.iv_icon = (ImageView) contentview.findViewById(R.id.iv_icon);

                contentview.setTag(holder);
            } else {
                holder = (ViewHolder) contentview.getTag();
            }
            holder.tv_title.setText(XorderDetailBean.get(arg0).getTitle() + " x " + XorderDetailBean.get(arg0).getNumber());
            holder.tv_price.setText("¥" + XorderDetailBean.get(arg0).getPrice());
            Glide
                    .with(context)
                    .load(MyCookieStore.URL + XorderDetailBean.get(arg0).getTitle_img())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_girdview)
                    .into(holder.iv_img);


            if (XorderDetailBean.get(arg0).getStatus().equals("1")) {//待付款
                holder.tv_tui.setVisibility(View.GONE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.GONE);
            } else if (XorderDetailBean.get(arg0).getStatus().equals("2")) {//待发货
                holder.tv_tui.setVisibility(View.VISIBLE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.VISIBLE);
                holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.iv_daifahuo));
            } else if (XorderDetailBean.get(arg0).getStatus().equals("3")) {//待收货
                holder.tv_tui.setVisibility(View.VISIBLE);
                holder.tv_pingjia.setText("收货");
                holder.tv_pingjia.setVisibility(View.VISIBLE);
                holder.iv_icon.setVisibility(View.GONE);
            } else if (XorderDetailBean.get(arg0).getStatus().equals("4")) {//待评价
                holder.tv_tui.setVisibility(View.VISIBLE);
                holder.tv_pingjia.setVisibility(View.VISIBLE);
                holder.iv_icon.setVisibility(View.GONE);
            } else if (XorderDetailBean.get(arg0).getStatus().equals("5")) {//删除
                holder.tv_tui.setVisibility(View.GONE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.GONE);
            } else if (XorderDetailBean.get(arg0).getStatus().equals("6")) {//申请退款
                holder.tv_tui.setVisibility(View.GONE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.VISIBLE);
                holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.iv_daituikuan));
            } else if (XorderDetailBean.get(arg0).getStatus().equals("7")) {//完成
                holder.tv_tui.setVisibility(View.VISIBLE);
                holder.tv_pingjia.setVisibility(View.VISIBLE);
                holder.tv_tui.setText("退款");
                holder.tv_pingjia.setText("删除");
                holder.iv_icon.setVisibility(View.VISIBLE);
                holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.icon_finish));
            } else if (XorderDetailBean.get(arg0).getStatus().equals("8")) {//退款中
                holder.tv_tui.setVisibility(View.GONE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.VISIBLE);
                holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.iv_tuikuanzhong));
            } else if (XorderDetailBean.get(arg0).getStatus().equals("9")) {//已退款
                holder.tv_tui.setVisibility(View.GONE);
                holder.tv_pingjia.setVisibility(View.GONE);
                holder.iv_icon.setVisibility(View.VISIBLE);
                holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.iv_yituikuan));

            }
            info_id = XorderDetailBean.get(arg0).getId();
            holder.tv_tui.setOnClickListener(new View.OnClickListener() {//退款
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewTuikuanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("o_id", XorderDetailBean.get(arg0).getOid());
                    bundle.putString("p_info_id", XorderDetailBean.get(arg0).getId());
                    bundle.putString("price", XorderDetailBean.get(arg0).getPrice());
                    bundle.putString("numer", XorderDetailBean.get(arg0).getNumber());
                    bundle.putString("img", XorderDetailBean.get(arg0).getTitle_img());
                    bundle.putString("p_title", XorderDetailBean.get(arg0).getTitle());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);

                }
            });
            holder.tv_pingjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (XorderDetailBean.get(arg0).getStatus().equals("3")) {//待收货

                        new CommomDialog(context, R.style.my_dialog_DimEnabled, "是否确认收货？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    getdatas("2", XorderDetailBean.get(arg0).getId(), arg0, "3");
                                    dialog.dismiss();
                                }

                            }
                        }).show();//.setTitle("提示")

                    } else if (XorderDetailBean.get(arg0).getStatus().equals("4")) {//待评价
                        Intent intent = new Intent(context, NewPingJiaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("o_id", XorderDetailBean.get(arg0).getOid());
                        bundle.putString("p_id", XorderDetailBean.get(arg0).getP_id());
                        bundle.putString("p_info_id", XorderDetailBean.get(arg0).getId());
                        bundle.putString("price", XorderDetailBean.get(arg0).getPrice());
                        bundle.putString("numer", XorderDetailBean.get(arg0).getNumber());
                        bundle.putString("img", XorderDetailBean.get(arg0).getTitle_img());
                        bundle.putString("p_title", XorderDetailBean.get(arg0).getTitle());
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);


                    } else if (XorderDetailBean.get(arg0).getStatus().equals("7")) {//待收货

                        new CommomDialog(context, R.style.my_dialog_DimEnabled, "是否确认删除？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    if (XorderDetailBean.size() == 1) {
                                        getdatas("1", XorderDetailBean.get(arg0).getId(), arg0, "1");
                                    } else {
                                        getdatas("1", XorderDetailBean.get(arg0).getId(), arg0, "2");
                                    }
                                }
                            }
                        }).show();//.setTitle("提示")

                    }
                }
            });

            return contentview;
        }

        String jiekouName;

        private void getdatas(final String type, String id, final int pos, final String strbool) {
            Url_info info = new Url_info(context);
            //删除订单中商品与确认收货接口   type为1  是删除 订单 2是确认收货
            RequestParams params = new RequestParams();
            if (type.equals("1")) {
                jiekouName = info.del_order;
            } else if (type.equals("2")) {
                //jiekouName = "personal/order_confirm/";
                jiekouName = info.shop_order_accept;
            }
            params.addBodyParameter("id", id);
            System.out.println("order_id====" + id);
            System.out.println("jiekouName====" + jiekouName);
            showDialog(smallDialog);
            HttpHelper hh = new HttpHelper(jiekouName, params, context) {

                @Override
                protected void setData(String json) {
                    hideDialog(smallDialog);
                    shopstr = mShopProtocol.setShop(json);
                    if (shopstr.equals("1")) {
                        if (type.equals("1")) {
                            if (strbool.equals("1")) {
                                Intent intent = new Intent();
                               /* Bundle bundle = new Bundle();
                                bundle.putString("item_detete_id", item_id);
                                intent.putExtras(bundle);
                                setResult(3, intent);*/
                                XorderDetailBean XorderDetail = new XorderDetailBean();
                                XorderDetail.setBack_type(3);
                                finish();
                            } else {
                                getDetail();
                                MyCookieStore.SC_notify = 1;
                                XorderDetailBean XorderDetail = new XorderDetailBean();
                                XorderDetail.setBack_type(3);
                                //XorderDetailBean.remove(pos);

                            }
                            XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                        } else {
                            finish();
                            // MyCookieStore.Sh_notify = 1;
                            XorderDetailBean XorderDetail1 = new XorderDetailBean();
                            XorderDetail1.setId(info_id);
                            XorderDetail1.setBack_type(3);//收货
                            EventBus.getDefault().post(XorderDetail1);
                            // notifyDataSetChanged();
                            XToast.makeText(context, "确认收货成功", XToast.LENGTH_SHORT).show();
                        }

                    } else {
                        XToast.makeText(context, shopstr, XToast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    hideDialog(smallDialog);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                }
            };
        }

        public class ViewHolder {
            private ImageView iv_img, iv_icon;
            private TextView tv_price, tv_title, tv_tui, tv_pingjia;
        }

    }
}
