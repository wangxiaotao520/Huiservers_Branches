package com.huacheng.huiservers.facepay.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.facepay.FacePayBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class FacepayHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FacePayBean> mFacePays;

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

    public FacepayHistoryAdapter(List<FacePayBean> facePays, Context context) {
        this.mFacePays = facePays;
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
                    .inflate(R.layout.facepay_history_item, parent, false);
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

            FacePayBean pay = mFacePays.get(position);
            recyclerViewHolder.tv_order_number.setText("付款编号：" + pay.getOrder_number());
            recyclerViewHolder.tv_order_date.setText(StringUtils.getDateToString(pay.getAddtime(), "7"));

            if (!TextUtils.isEmpty(pay.getM_name())) {
                recyclerViewHolder.tv_m_name.setText("付款对象："+pay.getM_name());
                recyclerViewHolder.tv_m_name.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHolder.tv_m_name.setVisibility(View.GONE);
            }

            String communityName = pay.getCommunity_name();
            if (!StringUtils.isEmpty(communityName)) {
                recyclerViewHolder.lin_fullname_house.setVisibility(View.VISIBLE);
                recyclerViewHolder.tv_fullname.setText("业主信息：" + pay.getFullname() + "/" + pay.getMobile());
                recyclerViewHolder.tv_house_desc.setText("房屋：" + pay.getCommunity_name() + pay.getBuilding_name() + "号楼" + pay.getUnit() + "单元" + pay.getCode());
            } else {
                recyclerViewHolder.lin_fullname_house.setVisibility(View.GONE);
            }
            recyclerViewHolder.tv_note.setText("备注：" + pay.getNote());
            recyclerViewHolder.tv_order_price.setText(pay.getC_name() + "：" + pay.getMoney() + "元");

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
        return mFacePays.size() + 1;
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

        LinearLayout lin_fullname_house;
        TextView tv_order_number,
                tv_order_date,
                tv_fullname,
                tv_house_desc,
                tv_note,
                tv_order_price,
                tv_m_name;


        RecyclerViewHolder(View itemView) {
            super(itemView);
            lin_fullname_house = (LinearLayout) itemView.findViewById(R.id.lin_fullname_house);
            tv_order_number = (TextView) itemView.findViewById(R.id.tv_order_number);
            tv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            tv_fullname = (TextView) itemView.findViewById(R.id.tv_fullname);
            tv_m_name = (TextView) itemView.findViewById(R.id.tv_m_name);
            tv_house_desc = (TextView) itemView.findViewById(R.id.tv_house_desc);
            tv_note = (TextView) itemView.findViewById(R.id.tv_note);
            tv_order_price = (TextView) itemView.findViewById(R.id.tv_order_price);
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
