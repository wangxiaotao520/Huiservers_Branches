package com.huacheng.huiservers.house.adapter;

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
import com.huacheng.huiservers.geren.ZhifuActivity;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.WuYeProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.huacheng.libraryservice.dialog.SmallDialog;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 */

public class HouseFamilyBillWuyeAdapter extends BaseAdapter {

    private List<HouseBean> mWuyes;
    private WuYeBean mRoomInfo;
    private Context mContext;
    String Is_available, Is_available_cn;
    SmallDialog smallDialog;

    public HouseFamilyBillWuyeAdapter(List<HouseBean> wuyes, WuYeBean roomInfo, String Is_available, String Is_available_cn, Context context) {
        this.mWuyes = wuyes;
        this.mRoomInfo = roomInfo;
        this.mContext = context;
        this.Is_available = Is_available;
        this.Is_available_cn = Is_available_cn;
        smallDialog=new SmallDialog(context);
    }

    @Override
    public int getCount() {
        return mWuyes.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.house_familybillwuye_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final HouseBean wuye = mWuyes.get(position);
        viewHolder.tv_familyDate.setText(wuye.getStartdate() + "-" + wuye.getEnddate());
        /*viewHolder.tv_familyDate.setText(
                StringUtils.getDateToString(wuye.getStartdate(), "7") + "-" +
                        StringUtils.getDateToString(wuye.getEnddate(), "7")
        );*/
        viewHolder.tv_familyPrice.setTextColor(mContext.getResources().getColor(R.color.orange5));
       /* String value = wuye.getSumvalue();
        float val = Float.parseFloat(value);
        if (val >= 0) {
            viewHolder.tv_familyPrice.setTextColor(mContext.getResources().getColor(R.color.black));
        } else {
            viewHolder.tv_familyPrice.setTextColor(mContext.getResources().getColor(R.color.orange5));
        }*/
        viewHolder.tv_familyPrice.setText("-" + wuye.getSumvalue() + "元");
        if (position == mWuyes.size() - 1) {
            viewHolder.lin_driver.setVisibility(View.GONE);
        } else {
            viewHolder.lin_driver.setVisibility(View.VISIBLE);
        }
        viewHolder.lin_wuye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Is_available.equals("0")) {//为0直接跳转
                    getWuyeSubmit(wuye);
                } else {
                    XToast.makeText(mContext, Is_available_cn, XToast.LENGTH_SHORT).show();
                }

            }
        });
        return convertView;
    }

    private void getWuyeSubmit(final HouseBean wuye) {
        Url_info info = new Url_info(mContext);
        RequestParams params = new RequestParams();
        params.addBodyParameter("bill_id", wuye.getBill_id());
        params.addBodyParameter("room_id", wuye.getRoom_id());
      /*  params.addBodyParameter("community_id", wuye.getCommunity_id());
        params.addBodyParameter("community_name", wuye.getCommunity_name());
        params.addBodyParameter("building_name", wuye.getBuilding_name());
        params.addBodyParameter("unit", wuye.getUnit());
        params.addBodyParameter("code", wuye.getCode());
        params.addBodyParameter("charge_type", wuye.getCharge_type());
        params.addBodyParameter("sumvalue", wuye.getSumvalue());
        params.addBodyParameter("bill_time", wuye.getTime());
        params.addBodyParameter("startdate", wuye.getStartdate());
        params.addBodyParameter("enddate", wuye.getEnddate());*/
        if (smallDialog!=null){
            smallDialog.show();
        }
        new HttpHelper(info.make_property_order, params, mContext) {

            @Override
            protected void setData(String json) {
                if (smallDialog!=null&&smallDialog.isShowing()){
                    smallDialog.dismiss();
                }
                WuYeBean bean = new WuYeProtocol().getWuorder(json);
                if (bean != null) {
                    Intent intent = new Intent(context, ZhifuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("o_id", bean.getId());
                    bundle.putString("price", wuye.getSumvalue());
                    bundle.putString("type", "wuye");
                    bundle.putString("order_type", "wy");
                    bundle.putSerializable("room", mRoomInfo);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } else {
                    XToast.makeText(context, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                if (smallDialog!=null&&smallDialog.isShowing()){
                    smallDialog.dismiss();
                }
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }


    class ViewHolder {
        LinearLayout lin_wuye, lin_driver;
        TextView tv_familyDate,
                tv_familyPrice;

        public ViewHolder(View v) {
            lin_wuye = (LinearLayout) v.findViewById(R.id.lin_wuye);
            lin_driver = (LinearLayout) v.findViewById(R.id.lin_driver);
            tv_familyDate = (TextView) v.findViewById(R.id.tv_familyDate);
            tv_familyPrice = (TextView) v.findViewById(R.id.tv_familyPrice);


        }
    }
}
