package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huacheng.huiservers.huodong.EducationActivity2;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.LifeDetailActivity2;
import com.huacheng.huiservers.center.bean.PropertyRepair;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class ServiceLifeOrderAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PropertyRepair> mBeans;

    private Context mContext;

    private String mType;

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

    private boolean mShowFooter = true;

    // 设置是否显示底部加载提示（将值传递给全局变量）
    public void isShowFooter(boolean showFooter)
    {
        this.mShowFooter = showFooter;
    }
    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter()
    {
        return this.mShowFooter;
    }

    public ServiceLifeOrderAdapter2(List<PropertyRepair> beans, String type, Context context) {
        this.mBeans = beans;
        this.mType = type;
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
                    .inflate(R.layout.service_life_order_item, parent, false);
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
            if (mType.equals("0")) {
                recyclerViewHolder.tv_service_title.setText(mBeans.get(position).getType_cn());
                recyclerViewHolder.tv_service_date.setText(StringUtils.getDateToString(mBeans.get(position).getAddtime(), "1"));
            } else {
                recyclerViewHolder.tv_service_title.setText(mBeans.get(position).getTitle());
                recyclerViewHolder.tv_service_date.setText(StringUtils.getDateToString(mBeans.get(position).getAddtime(), "1"));
            }

            String status = mBeans.get(position).getStatus();
            if (status.equals("0")) {
                recyclerViewHolder.tv_service_status_point.setBackground(mContext.getResources().getDrawable(R.drawable.dotshape_gray));
                recyclerViewHolder.tv_service_status_txt.setText("待处理");
            } else if (status.equals("1")) {
                recyclerViewHolder.tv_service_status_point.setBackground(mContext.getResources().getDrawable(R.drawable.dotshape_black));
                recyclerViewHolder.tv_service_status_txt.setText("已受理");
            } else if (status.equals("2")) {
                recyclerViewHolder.tv_service_status_point.setVisibility(View.GONE);
                recyclerViewHolder.tv_service_status_txt.setText("已完成");
            }

            recyclerViewHolder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType.equals("0")) {
                        Intent intent = new Intent(mContext,
                                LifeDetailActivity2.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("participateID", mBeans.get(position).getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent1 = new Intent(mContext,
                                EducationActivity2.class);
                        intent1.putExtra("id", mBeans.get(position).getId());
                        mContext.startActivity(intent1);
                    }
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

    @Override
    public int getItemCount() {
        int begin = mShowFooter? 1 : 0;
        // 没有数据的时候，直接返回begin
        if (mBeans == null)
        {
            return begin;
        }
        // 因为底部布局要占一个位置，所以总数目要+1
        return mBeans.size() + begin;
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

        LinearLayout linear;
        TextView tv_service_date, tv_service_status_txt, tv_service_status_point, tv_service_title;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            tv_service_title = (TextView) itemView.findViewById(R.id.tv_service_title);
            tv_service_status_point = (TextView) itemView.findViewById(R.id.tv_service_status_point);
            tv_service_status_txt = (TextView) itemView.findViewById(R.id.tv_service_status_txt);
            tv_service_date = (TextView) itemView.findViewById(R.id.tv_service_date);
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
