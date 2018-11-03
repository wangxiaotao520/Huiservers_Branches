package com.huacheng.huiservers.center.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.New_Shop_Order_DetailActivity;
import com.huacheng.huiservers.center.bean.ShopOrderBeanType;
import com.huacheng.huiservers.shop.adapter.New_OrderImageAdapter;
import com.huacheng.huiservers.view.HorizontalListView;
import com.huacheng.libraryservice.dialog.CommomDialog;

import java.util.List;

/**
 * 类描述：
 * 时间：2018/9/19 16:53
 * created by DFF
 */
public class Shop_order_listAdapter extends BaseAdapter {

    List<ShopOrderBeanType> adbeanTypes;
    private Callback callback;
    private Context mContext;
    int type;

    public Shop_order_listAdapter(Context context, List<ShopOrderBeanType> ALLbeanTypes, int type, Callback callback) {
        this.mContext = context;
        this.adbeanTypes = ALLbeanTypes;
        this.type = type;
        this.callback = callback;
    }

    //定义接口
    public interface Callback {
        public void click(ShopOrderBeanType adbeanTypes);
    }

    @Override
    public int getCount() {
        return adbeanTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.new_shop_order_item, null);
            holder.hor_scroll = (HorizontalListView) convertView.findViewById(R.id.hor_scroll);
            holder.lin_info = (LinearLayout) convertView.findViewById(R.id.lin_info);
            holder.lin_all = (LinearLayout) convertView.findViewById(R.id.lin_all);
            holder.txt_delete = (TextView) convertView.findViewById(R.id.txt_delete);
            holder.txt_order_num = (TextView) convertView.findViewById(R.id.txt_order_num);
            holder.txt_num = (TextView) convertView.findViewById(R.id.txt_num);
            holder.txt_danprice = (TextView) convertView.findViewById(R.id.txt_danprice);
            holder.txt_type = (TextView) convertView.findViewById(R.id.txt_type);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_order_num.setText("订单编号：" + adbeanTypes.get(position).getOrder_number());
        holder.txt_num.setText("共" + adbeanTypes.get(position).getPro_num() + "件");
        holder.txt_danprice.setText("¥" + adbeanTypes.get(position).getPrice_total());
        // order_id = ALLbeanTypes.get(position).getId();
        //order_num = ALLbeanTypes.get(position).getOrder_number();
        if (type == 0) {
            holder.lin_all.setVisibility(View.GONE);
        } else if (type == 1) {//待付款
            holder.txt_delete.setText("删除");
            holder.txt_type.setText("付款");
        } else if (type == 2) {//待收货
            holder.txt_delete.setVisibility(View.GONE);
            //recyclerViewHolder.txt_type.setText("收货");
            holder.lin_all.setVisibility(View.GONE);
        } else if (type == 3) {//待评价  待退款
            holder.lin_all.setVisibility(View.GONE);
            //  recyclerViewHolder.txt_delete.setText("删除");
            holder.txt_type.setVisibility(View.GONE);
        }
        holder.txt_delete.setOnClickListener(new View.OnClickListener() {  //删除接口
            @Override
            public void onClick(View view) {
                new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认要删除吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            callback.click(adbeanTypes.get(position));
                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")

            }
        });

        holder.txt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, New_Shop_Order_DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("order_id", adbeanTypes.get(position).getId());
                bundle.putString("status", type + "");
                bundle.putString("order_num", adbeanTypes.get(position).getOrder_number());
                bundle.putString("item_id", position + "");
                System.out.println("item_id$$$$$$$$&" + position + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }

        });

        holder.lin_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, New_Shop_Order_DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("order_id", adbeanTypes.get(position).getId());
                bundle.putString("status", type+"");
                bundle.putString("order_num", adbeanTypes.get(position).getOrder_number());
                bundle.putString("item_id", position + "");
                System.out.println("item_id$$$$$$$$&" + position + "");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });
        if (adbeanTypes.get(position).getImg() != null) {
            //这里图片的显示是个适配器   控制图片数量
            New_OrderImageAdapter imageAdapter = new New_OrderImageAdapter(mContext, adbeanTypes.get(position).getImg());
            holder.hor_scroll.setAdapter(imageAdapter);

        }
        return convertView;
    }

    //0417
  /*  private void getdatas(final String type, String id, final int arg0) {
          Url_info info = new Url_info(New_Shop_OrderActivity.this);
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
          showDialog(smallDialog);
          new HttpHelper(jiekouName, params, mContext) {

              @Override
              protected void setData(String json) {
                  hideDialog(smallDialog);
                  String shopstr = mShopProtocol.setShop(json);
                  if (shopstr.equals("1")) {
                      if (type.equals("1")) {
                          XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                      } else {
                          XToast.makeText(context, "确认收货成功", XToast.LENGTH_SHORT).show();
                      }
                      list_info_id.clear();
                      adbeanTypes.remove(arg0);
                      new_shop_orderAdapter.notifyDataSetChanged();
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
  */
    private class ViewHolder {

        HorizontalListView hor_scroll;
        TextView txt_delete;
        TextView txt_type, txt_order_num, txt_num, txt_danprice;
        LinearLayout lin_info, lin_all;
    }


}
