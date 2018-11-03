package com.huacheng.huiservers.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.timer.CountDownTimer;

import java.util.List;

/**
 * Description:
 * Author: badge
 * Create: 2018/8/11 10:26
 */
public class ShopLimitAdapter extends BaseAdapter {

    private Context mContext;
    List<ShopIndexBean> mShopIndex;
    long mDay, mHour, mMin, mSecond;
    //用于退出 Activity,避免 Countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownCounters;


    public ShopLimitAdapter(List<ShopIndexBean> shopIndex, Context context) {
        this.mShopIndex = shopIndex;
        this.mContext = context;
        countDownCounters = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mShopIndex.size();
    }//

    @Override
    public Object getItem(int i) {
        return mShopIndex.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.commodity_limit_item, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final ShopIndexBean shopIndex = mShopIndex.get(i);
        String discount = shopIndex.getDiscount();
        if (discount.equals("1")) {
//            holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
            holder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_spike);
        } else {
            if (shopIndex.getIs_hot().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                holder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_hotsell);
            } else if (shopIndex.getIs_new().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                holder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_newest);
            } else {
//                holder.iv_shop_list_flag.setBackground(null);
            }
        }
        gettime(holder, shopIndex);

        Glide.with(mContext)
                .load(MyCookieStore.URL + shopIndex.getTitle_img())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false).placeholder(R.drawable.icon_girdview).error(R.drawable.icon_girdview).into(holder.iv_item_image);

        holder.tv_item_name.setText(shopIndex.getTitle());
        holder.txt_price_limit.setText("¥" + shopIndex.getPrice());
        String unit = shopIndex.getUnit();
        if (!NullUtil.isStringEmpty(unit)) {
            holder.txt_shop_unit.setText("/" + shopIndex.getUnit());
        } else {
            holder.txt_shop_unit.setText("");
        }
        holder.txt_price_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        holder.txt_price_original.setText("¥" + shopIndex.getOriginal());

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShopDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shop_id", shopIndex.getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    class ViewHolder {
        RelativeLayout linear;
        ImageView iv_item_image, iv_buynow, iv_shop_list_flag;
        TextView tv_limit_day, tv_limit_hour, tv_limit_minute,
                tv_limit_second, tv_item_name, txt_price_limit,
                txt_shop_unit, txt_price_original, txt_time_type;

        public ViewHolder(View v) {
            iv_item_image = (ImageView) v.findViewById(R.id.iv_item_image);
            iv_buynow = (ImageView) v.findViewById(R.id.iv_buynow);
            iv_shop_list_flag = (ImageView) v.findViewById(R.id.iv_shop_list_flag);

            txt_time_type = (TextView) v.findViewById(R.id.txt_time_type);
            tv_limit_day = (TextView) v.findViewById(R.id.tv_limit_day);
            tv_limit_hour = (TextView) v.findViewById(R.id.tv_limit_hour);
            tv_limit_minute = (TextView) v.findViewById(R.id.tv_limit_minute);
            tv_limit_second = (TextView) v.findViewById(R.id.tv_limit_second);

            tv_item_name = (TextView) v.findViewById(R.id.tv_item_name);
            txt_price_limit = (TextView) v.findViewById(R.id.txt_price_limit);
            txt_shop_unit = (TextView) v.findViewById(R.id.txt_shop_unit);
            txt_price_original = (TextView) v.findViewById(R.id.txt_price_original);
            linear = v.findViewById(R.id.linear);

        }
    }


    private void gettime(final ViewHolder holder, final ShopIndexBean data) {//限购时间
        String distanceEnd = data.getDistance_end();
        if (!NullUtil.isStringEmpty(distanceEnd)) {
            long distance_int = 0;
            String distance_str = "";
            distance_str = "距结束";
            distance_int = Long.parseLong(distanceEnd) * 1000;

            holder.txt_time_type.setText(distance_str);
            if (data.getDiscount().equals("1")) {
                long timer = 0;
                if (data.getCurrent_times() == 0) {
                    timer = distance_int;
                    data.setCurrent_times(System.currentTimeMillis());
                } else {
                    timer = distance_int - (System.currentTimeMillis() - data.getCurrent_times());
                }

                handlerTime(holder, timer);
            }
        }


    }

    private void handlerTime(final ViewHolder holder, long timeTmp) {
        CountDownTimer countDownTimer = countDownCounters.get(holder.tv_limit_day.hashCode());
        if (countDownTimer != null) {
            //将复用的倒计时清除
            countDownTimer.cancel();
        }
        //TODO 数据
        long timer = timeTmp;
        //expirationTime 与系统时间做比较，timer 小于零，则此时倒计时已经结束。
        if (timer > 0) {
            countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] times = SetTime(millisUntilFinished);
                    holder.tv_limit_day.setText(fillZero(times[0]));
                    holder.tv_limit_hour.setText(fillZero(times[1]));
                    holder.tv_limit_minute.setText(fillZero(times[2]));
                    holder.tv_limit_second.setText(fillZero(times[3]));
                }

                public void onFinish(String redpackage_id) {
                    //结束了该轮倒计时
                    holder.txt_time_type.setText("已结束");
                    holder.tv_limit_day.setText("00");
                    holder.tv_limit_hour.setText("00");
                    holder.tv_limit_minute.setText("00");
                    holder.tv_limit_second.setText("00");

                }
            }.start();
            //将此 countDownTimer 放入list.
            countDownCounters.put(holder.tv_limit_day.hashCode(), countDownTimer);
        } else {
            //结束
            holder.txt_time_type.setText("已结束");
            holder.tv_limit_day.setText("00");
            holder.tv_limit_hour.setText("00");
            holder.tv_limit_minute.setText("00");
            holder.tv_limit_second.setText("00");
        }
    }


    private String[] SetTime(long time) {
        mDay = time / (1000 * 60 * 60 * 24);
        mHour = (time - mDay * (1000 * 60 * 60 * 24))
                / (1000 * 60 * 60);
        mMin = (time - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60))
                / (1000 * 60);
        mSecond = (time - mDay * (1000 * 60 * 60 * 24) - mHour
                * (1000 * 60 * 60) - mMin * (1000 * 60))
                / 1000;
        String[] str = new String[4];
        str[0] = mDay + "";
        str[1] = mHour + "";
        str[2] = mMin + "";
        str[3] = mSecond + "";
        return str;
    }

    private String fillZero(String time) {
        String timeStr = "";
        if (time.length() == 1)
            return "0" + time;
        else
            return time;
    }

    /**
     * 清空当前 CountTimeDown 资源
     */
    public void cancelAllTimers() {
        if (countDownCounters == null) {
            return;
        }
        Log.e("TAG", "size :  " + countDownCounters.size());
        for (int i = 0, length = countDownCounters.size(); i < length; i++) {
            CountDownTimer cdt = countDownCounters.get(countDownCounters.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
