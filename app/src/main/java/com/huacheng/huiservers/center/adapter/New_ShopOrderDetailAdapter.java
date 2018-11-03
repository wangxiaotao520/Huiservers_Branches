package com.huacheng.huiservers.center.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.XorderDetailBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.NewPingJiaActivity;
import com.huacheng.huiservers.shop.NewTuikuanActivity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

import static com.huacheng.huiservers.utils.UIUtils.getResources;


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
        // TODO Auto-generated method stub
        return XorderDetailBean.size();

    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
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
            holder.tv_tui.setVisibility(View.GONE);
            holder.tv_pingjia.setVisibility(View.GONE);
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.iv_icon.setBackground(getResources().getDrawable(R.drawable.iv_daifahuo));
        } else if (XorderDetailBean.get(arg0).getStatus().equals("3")) {//待收货
            holder.tv_tui.setVisibility(View.GONE);
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
            holder.tv_pingjia.setVisibility(View.GONE);
            holder.iv_icon.setVisibility(View.GONE);
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
                context.startActivity(intent);

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
                                getdatas("2", XorderDetailBean.get(arg0).getId(), arg0);
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
                    context.startActivity(intent);


                }
            }
        });

        return contentview;
    }

    String jiekouName;

    private void getdatas(final String type, String id, final int pos) {
        //删除订单中商品与确认收货接口   type为1  是删除 订单 2是确认收货
        RequestParams params = new RequestParams();
        if (type.equals("1")) {
            jiekouName = "userCenter/del_shopping_order/";
        } else if (type.equals("2")) {
            //jiekouName = "personal/order_confirm/";
            jiekouName = "userCenter/order_accept/";
        }
        params.addBodyParameter("id", id);
        System.out.println("order_id====" + id);
        HttpHelper hh = new HttpHelper(MyCookieStore.SERVERADDRESS + jiekouName, params, context) {

            @Override
            protected void setData(String json) {
                shopstr = mShopProtocol.setShop(json);
                if (shopstr.equals("1")) {
                    if (type.equals("1")) {
                        XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                    } else {
                        XToast.makeText(context, "确认收货成功", XToast.LENGTH_SHORT).show();
                    }
                    // XorderDetailBean.remove(pos);
                    notifyDataSetChanged();
                } else {
                    XToast.makeText(context, shopstr, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    public class ViewHolder {
        private ImageView iv_img, iv_icon;
        private TextView tv_price, tv_title, tv_tui, tv_pingjia;
    }

}
