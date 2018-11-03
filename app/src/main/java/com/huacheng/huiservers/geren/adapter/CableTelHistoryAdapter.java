package com.huacheng.huiservers.geren.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.WiredBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class CableTelHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<WiredBean> beanList;
    Context mContext;

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


    public CableTelHistoryAdapter(Context context, List<WiredBean> beanList) {
        this.beanList = beanList;
        this.mContext = context;
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
                    .inflate(R.layout.cabtel_history_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

            recyclerViewHolder.tv_order_date.setText(StringUtils.getDateToString(beanList.get(position).getAddtime(), "7"));
            recyclerViewHolder.tv_order_price.setText("有线电视缴费：" + beanList.get(position).getAmount() + "元");
            recyclerViewHolder.tv_order_number.setText("付款编号：" + beanList.get(position).getOrder_number());

            if (beanList.get(position).getPay_type().equals("alipay")) {
                recyclerViewHolder.tv_paytype.setText("缴费方式：支付宝");
            } else if (beanList.get(position).getPay_type().equals("wxpay")) {
                recyclerViewHolder.tv_paytype.setText("缴费方式：微信支付");
            } else if (beanList.get(position).getPay_type().equals("bestpay")) {
                recyclerViewHolder.tv_paytype.setText("缴费方式：翼支付");
            } else if (beanList.get(position).getPay_type().equals("hcpay")) {
                recyclerViewHolder.tv_paytype.setText("缴费方式：一卡通支付");
            }

            recyclerViewHolder.tv_wtredID.setText("卡号：" + beanList.get(position).getWired_num());
            recyclerViewHolder.tv_name.setText("姓名：" + beanList.get(position).getFullname());

            if (beanList.get(position).getStatus().equals("1")) {//待开通
                recyclerViewHolder.ly_kaitong.setVisibility(View.GONE);
                recyclerViewHolder.tv_nokaitong.setVisibility(View.VISIBLE);

            } else if (beanList.get(position).getStatus().equals("2")) {//已开通
                recyclerViewHolder.ly_kaitong.setVisibility(View.VISIBLE);
                recyclerViewHolder.tv_nokaitong.setVisibility(View.GONE);
                recyclerViewHolder.tv_kaitong_data.setText("开通时间：" +
                        StringUtils.getDateToString(beanList.get(position).getUptime(), "7"));
            }


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


    @Override
    public int getItemCount() {
        return beanList.size() + 1;
    } //mFacePays.size()

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

        LinearLayout lin_fullname_house, ly_kaitong;
        TextView tv_order_number,
                tv_order_date,
                tv_paytype, tv_wtredID, tv_note, tv_name, tv_nokaitong, tv_order_price, tv_kaitong, tv_kaitong_data;


        RecyclerViewHolder(View itemView) {
            super(itemView);
            lin_fullname_house = (LinearLayout) itemView.findViewById(R.id.lin_fullname_house);
            tv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            tv_order_price = (TextView) itemView.findViewById(R.id.tv_order_price);
            tv_order_number = (TextView) itemView.findViewById(R.id.tv_order_number);
            tv_paytype = (TextView) itemView.findViewById(R.id.tv_paytype);
            tv_wtredID = (TextView) itemView.findViewById(R.id.tv_wtredID);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_nokaitong = (TextView) itemView.findViewById(R.id.tv_nokaitong);
            tv_note = (TextView) itemView.findViewById(R.id.tv_note);

            ly_kaitong = (LinearLayout) itemView.findViewById(R.id.ly_kaitong);
            tv_kaitong = (TextView) itemView.findViewById(R.id.tv_kaitong);
            tv_kaitong_data = (TextView) itemView.findViewById(R.id.tv_kaitong_data);

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
}
