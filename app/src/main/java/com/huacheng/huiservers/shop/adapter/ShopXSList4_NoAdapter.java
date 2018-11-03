package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.libraryservice.utils.timer.CountDownTimer;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class ShopXSList4_NoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;

    List<ShopIndexBean> mBeanList;
    private SparseArray<CountDownTimer> countDownCounters;

    public ShopXSList4_NoAdapter(Context context, List<ShopIndexBean> mBeanList) {
        this.mContext = context;
        this.mBeanList = mBeanList;
        countDownCounters = new SparseArray<>();

    }

    private boolean mShowFooter = true;

    // 设置是否显示底部加载提示（将值传递给全局变量）
    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shop_xs_no_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.itemView.setTag(position);

            recyclerViewHolder.lin_goodslist_Tag.removeAllViews();
            if (mBeanList.get(position).getGoods_tag() != null) {
                for (int i = 0; i < mBeanList.get(position).getGoods_tag().size(); i++) {
                    if (i < 2) {
                        View view = LinearLayout.inflate(mContext, R.layout.shop_list_tag_item, null);
                        TextView tag1 = (TextView) view.findViewById(R.id.txt_tag1);
                        tag1.setText(mBeanList.get(position).getGoods_tag().get(i).getC_name());
                        recyclerViewHolder.lin_goodslist_Tag.addView(view);
                    }
                }
            }

            Glide.with(mContext)
                    .load(MyCookieStore.URL + mBeanList.get(position).getTitle_img())
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_girdview)
                    .into(recyclerViewHolder.iv_img);
            recyclerViewHolder.tv_title.setText(mBeanList.get(position).getTitle());
            recyclerViewHolder.tv_price.setText("¥" + mBeanList.get(position).getPrice());
            if (mBeanList.get(position).getUnit().equals("")) {
                recyclerViewHolder.tv_unit.setText("");
            } else {
                recyclerViewHolder.tv_unit.setText("/" + mBeanList.get(position).getUnit());
            }
            recyclerViewHolder.tv_ongnail.setText("¥" + mBeanList.get(position).getOriginal());
            recyclerViewHolder.tv_ongnail.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

            ShopIndexBean limitBean = mBeanList.get(position);
//            gettime(recyclerViewHolder, limitBean);

            String discount = limitBean.getDiscount();
            if (discount.equals("1")) {
//            holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_spike);
            } else {
                if (limitBean.getIs_hot().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_hotsell);
                } else if (limitBean.getIs_new().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_newest);
                } else {
//                holder.iv_shop_list_flag.setBackground(null);
                }
            }
            if (!discount.equals("0")) {
                String distance_str = "";
                long distance_int = 0;
                int distance_tag = 0;
                String distanceStart = limitBean.getDistance_start();
                boolean flag = new StringUtils().isNumericZF(distanceStart);
                if (flag) {
                    recyclerViewHolder.lin_shop_downtimer.setVisibility(View.VISIBLE);
                    distance_str = "距开始";
                    distance_tag = 1;
                    distance_int = Long.parseLong(distanceStart) * 1000;//
                    recyclerViewHolder.txt_time_type.setText(distance_str);
                    if (limitBean.getDiscount().equals("1")) {
                        //结束时间-开始时间=结束时间还剩多少时间
                        long timer = 0;
                        if (limitBean.getCurrent_times() == 0) {
                            timer = distance_int;
                            limitBean.setCurrent_times(System.currentTimeMillis());
                        } else {
                            timer = distance_int - (System.currentTimeMillis() - limitBean.getCurrent_times());
                        }

                        handlerTime(recyclerViewHolder, timer, distance_tag, position);
                    }
                } else {
                    recyclerViewHolder.lin_shop_downtimer.setVisibility(View.GONE);

                    if (mBeanList.get(position).getInventory().equals("0") || TextUtils.isEmpty(mBeanList.get(position).getInventory())) {
                        recyclerViewHolder.tv_shouqing.setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewHolder.tv_shouqing.setVisibility(View.GONE);
                    }
                    distance_str = "";
                }
            } else {
                recyclerViewHolder.lin_shop_downtimer.setVisibility(View.GONE);
            }

            recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", mBeanList.get(position).getId());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }

    private void handlerTime(final RecyclerViewHolder holder, long timeTmp, final int dicountTag, final int position) {
        holder.countDownTimer = countDownCounters.get(holder.tv_limit_day.hashCode());
        if (holder.countDownTimer != null) {
            //将复用的倒计时清除
            holder.countDownTimer.cancel();
        }
        //TODO 数据
        long timer = timeTmp;

        //long timer = data.expirationTime;
//        timer = timer - System.currentTimeMillis();
        //expirationTime 与系统时间做比较，timer 小于零，则此时倒计时已经结束。
        if (timer > 0) {
            holder.countDownTimer = new CountDownTimer(timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] times = SetTime(millisUntilFinished);
                    holder.tv_limit_day.setText(fillZero(times[0]));
                    holder.tv_limit_hour.setText(fillZero(times[1]));
                    holder.tv_limit_minute.setText(fillZero(times[2]));
                    holder.tv_limit_second.setText(fillZero(times[3]));
                }

                public void onFinish(String redpackage_id) {
                    if (dicountTag == 1) {
                        //距结束就移除当前item
                        mBeanList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        holder.txt_time_type.setText("已结束");
                        holder.tv_limit_day.setText("00");
                        holder.tv_limit_hour.setText("00");
                        holder.tv_limit_minute.setText("00");
                        holder.tv_limit_second.setText("00");
                    }

                    //结束了该轮倒计时


//                    holder.statusTv.setText(data.name + ":结束");
                }
            }.start();
            //将此 countDownTimer 放入list.
            countDownCounters.put(holder.tv_limit_day.hashCode(), holder.countDownTimer);
        } else {
            //结束
            holder.txt_time_type.setText("已结束");
            holder.tv_limit_day.setText("00");
            holder.tv_limit_hour.setText("00");
            holder.tv_limit_minute.setText("00");
            holder.tv_limit_second.setText("00");
        }
    }


    String start, end;
    private Date data_start, data_now, data_end;
    long diff, mDay, mHour, mMin, mSecond;

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

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        // 没有数据的时候，直接返回begin
        if (mBeanList == null) {
            return begin;
        }
        // 因为底部布局要占一个位置，所以总数目要+1
        return mBeanList.size() + begin;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin_goodslist_Tag, ly_onclick, lin_shop_downtimer;
        ImageView iv_img, iv_shop_list_flag;
        TextView tv_title, tv_shouqing, tv_price, tv_btn, tv_unit, tv_ongnail,

        txt_time_type, tv_limit_day, tv_limit_hour, tv_limit_minute,
                tv_limit_second;
        public CountDownTimer countDownTimer;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            iv_shop_list_flag = (ImageView) itemView.findViewById(R.id.iv_shop_list_flag);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_shouqing = (TextView) itemView.findViewById(R.id.tv_shouqing);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_btn = (TextView) itemView.findViewById(R.id.tv_btn);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
            tv_ongnail = (TextView) itemView.findViewById(R.id.tv_ongnail);
            ly_onclick = (LinearLayout) itemView.findViewById(R.id.ly_onclick);
            lin_goodslist_Tag = (LinearLayout) itemView.findViewById(R.id.lin_goodslist_Tag);

            txt_time_type = (TextView) itemView.findViewById(R.id.txt_time_type);
            tv_limit_day = (TextView) itemView.findViewById(R.id.tv_limit_day);
            tv_limit_hour = (TextView) itemView.findViewById(R.id.tv_limit_hour);
            tv_limit_minute = (TextView) itemView.findViewById(R.id.tv_limit_minute);
            tv_limit_second = (TextView) itemView.findViewById(R.id.tv_limit_second);

            lin_shop_downtimer = (LinearLayout) itemView.findViewById(R.id.lin_shop_downtimer);

        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownCounters == null) {
            return;
        }
        for (int i = 0, length = countDownCounters.size(); i < length; i++) {
            CountDownTimer cdt = countDownCounters.get(countDownCounters.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }
}
