package com.huacheng.huiservers.oldhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import com.huacheng.huiservers.cricle.Circle2DetailsActivity;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.view.CircularImage;

import java.util.List;

/**
 * 类：
 * 时间：2018/5/29 13:01
 * 功能描述:Huiservers
 */
public class OldListQRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<BannerBean> mList;
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

    // 判断是不是最后一个item，默认是true
    private boolean mShowFooter = true;

    public OldListQRecycleAdapter(Context context, List<BannerBean> mList) {
        this.mList = mList;
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

    // 设置是否显示底部加载提示（将值传递给全局变量）
    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public interface ImgClickInterface {
        public void cliskimg(int code);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.old_list_all_q_item, parent, false);
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

            byte[] bytes1 = Base64.decode(mList.get(position).getTitle(), Base64.DEFAULT);
            recyclerViewHolder.tv_title.setText(new String(bytes1));
            recyclerViewHolder.tv_name.setText(mList.get(position).getNickname() + "   发布于  " + mList.get(position).getC_name());
            recyclerViewHolder.tv_time.setText(mList.get(position).getAddtime());
            byte[] bytes2 = Base64.decode(mList.get(position).getContent(), Base64.DEFAULT);
            recyclerViewHolder.tv_content.setText(new String(bytes2));

            recyclerViewHolder.tv_circleViews.setText(mList.get(position).getClick());
            recyclerViewHolder.tv_circleReply.setText(mList.get(position).getReply_num());
            if (mList.get(position).getImg_list() != null) {
                Glide
                        .with(mContext)
                        .load(MyCookieStore.URL + mList.get(position).getImg_list().get(0).getImg())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.icon_girdview)
                        .into(recyclerViewHolder.iv_img);
            }
            Glide
                    .with(mContext)
                    .load(MyCookieStore.URL + mList.get(position).getAvatars())
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_girdview)
                    .into(recyclerViewHolder.iv_head);

            recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Circle2DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", mList.get(position).getId());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

        } else if (holder instanceof FootViewHolder)

        {
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
        // 判断是不是显示底部，是就返回1，不是返回0
        int begin = mShowFooter ? 1 : 0;
        // 没有数据的时候，直接返回begin
        if (mList == null) {
            return begin;
        }
        // 因为底部布局要占一个位置，所以总数目要+1
        return mList.size() + begin;
        //return mList.size() + 1;
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
        private LinearLayout ly_onclick;
        private ImageView iv_img;
        private CircularImage iv_head;
        private TextView tv_time, tv_content, tv_circleViews, tv_circleReply, tv_name, tv_title;

        RecyclerViewHolder(View convertView) {
            super(convertView);
            iv_head = (CircularImage) convertView.findViewById(R.id.iv_head);
            iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            tv_circleViews = (TextView) convertView.findViewById(R.id.tv_circleViews);
            tv_circleReply = (TextView) convertView.findViewById(R.id.tv_circleReply);
            ly_onclick = (LinearLayout) convertView.findViewById(R.id.ly_onclick);
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


