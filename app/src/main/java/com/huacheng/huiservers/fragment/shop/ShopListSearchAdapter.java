package com.huacheng.huiservers.fragment.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.huacheng.huiservers.utils.CommonMethod;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;

import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class ShopListSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

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


    List<ShopIndexBean> mDatas;
    String tag;
    // 判断是不是最后一个item，默认是true
    private boolean mShowFooter = true;

    public ShopListSearchAdapter(List<ShopIndexBean> proLists, Context context, String tag) {//, List<String> dataSource
        this.mDatas = proLists;
        this.mContext = context;
        this.tag = tag;

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
    public void isShowFooter(boolean showFooter)
    {
        this.mShowFooter = showFooter;
    }


    // 判断是否显示底部，数据来自全局变量
    public boolean isShowFooter()
    {
        return this.mShowFooter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shop_list_item, parent, false);
            view.setOnClickListener(this);
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
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.itemView.setTag(position);

            if (mDatas.get(position).getIs_hot().equals("1") && mDatas.get(position).getIs_new().equals("1")) {
                recyclerViewHolder.iv_shop_list_flag.setBackground(mContext.getResources().getDrawable(R.drawable.ic_shoplist_hotsell));
            } else {
                if (mDatas.get(position).getIs_hot().equals("1")) {
                    recyclerViewHolder.iv_shop_list_flag.setBackground(mContext.getResources().getDrawable(R.drawable.ic_shoplist_hotsell));
                } else {
                    recyclerViewHolder.iv_shop_list_flag.setVisibility(View.GONE);
                }

                if (mDatas.get(position).getIs_new().equals("1")) {
                    recyclerViewHolder.iv_shop_list_flag.setBackground(mContext.getResources().getDrawable(R.drawable.ic_shoplist_newest));
                } else {
                    recyclerViewHolder.iv_shop_list_flag.setVisibility(View.GONE);
                }

            }

            Glide.with(mContext).load(mDatas.get(position).getTitle_img()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_girdview).into(recyclerViewHolder.iv_title_img);
            recyclerViewHolder.tv_title.setText(mDatas.get(position).getTitle());

            recyclerViewHolder.lin_goodslist_Tag.removeAllViews();
            if (mDatas.get(position).getGoods_tag() != null) {
                for (int i = 0; i < mDatas.get(position).getGoods_tag().size(); i++) {
                    if (i < 2) {
                        View view = LinearLayout.inflate(mContext, R.layout.shop_list_tag_item, null);
                        TextView tag1 = (TextView) view.findViewById(R.id.txt_tag1);
                        tag1.setText(mDatas.get(position).getGoods_tag().get(i).getC_name());
                        recyclerViewHolder.lin_goodslist_Tag.addView(view);
                    }
                }
            }

            recyclerViewHolder.tv_shop_price.setText("¥" + mDatas.get(position).getPrice() + "/");
            recyclerViewHolder.tv_shop_weight.setText(mDatas.get(position).getUnit());
            recyclerViewHolder.tv_shop_price_original.setText("¥" + mDatas.get(position).getOriginal() + "元");
            recyclerViewHolder.tv_orders_sold_num.setText("已售" + mDatas.get(position).getOrder_num() + "单");

            recyclerViewHolder.iv_shopcar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mOnItemCarListener.onCarClick(position);
                    preferencesLogin = mContext.getSharedPreferences("login", 0);
                    login_type = preferencesLogin.getString("login_type", "");
                    if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                        Intent intent = new Intent(mContext, LoginVerifyCode1Activity.class);
                        mContext.startActivity(intent);
                        preferencesLogin = mContext.getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = preferencesLogin.edit();
                        editor.putString("login_shop", "shop_login");
                        editor.commit();
                    } else if (login_type.equals("1")) {

                        if (mDatas.get(position) != null) {
                            new CommonMethod(mDatas.get(position), mContext).getShopLimitTag();

                        }
                    } else {
                        XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                    }

                }
            });

            recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", mDatas.get(position).getId());
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

    String login_type;
    SharedPreferences preferencesLogin;

    //购物车接口
    public interface OnItemCarListener {
        void onCarClick(int i);
    }

    public OnItemCarListener mOnItemCarListener;

    public void setOnItemCarListener(OnItemCarListener onItemCarListener) {
        this.mOnItemCarListener = onItemCarListener;
    }

    //item事件
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mItemClickListener;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
    //

    @Override
    public int getItemCount() {
        // 判断是不是显示底部，是就返回1，不是返回0
        int begin = mShowFooter? 1 : 0;
        // 没有数据的时候，直接返回begin
        if (mDatas == null)
        {
            return begin;
        }
        // 因为底部布局要占一个位置，所以总数目要+1
        return mDatas.size() + begin;
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

        ImageView iv_shop_list_flag, iv_title_img, iv_shopcar;
        TextView tv_title, tv_shop_price, tv_shop_weight, tv_orders_sold_num, tv_shop_price_original;

        LinearLayout lin_goodslist_Tag,ly_onclick;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ly_onclick = (LinearLayout) itemView.findViewById(R.id.ly_onclick);
            iv_shop_list_flag = (ImageView) itemView.findViewById(R.id.iv_shop_list_flag);
            iv_title_img = (ImageView) itemView.findViewById(R.id.iv_title_img);
            iv_shopcar = (ImageView) itemView.findViewById(R.id.iv_shopcar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            lin_goodslist_Tag = (LinearLayout) itemView.findViewById(R.id.lin_goodslist_Tag);
            tv_shop_price = (TextView) itemView.findViewById(R.id.tv_shop_price);
            tv_shop_weight = (TextView) itemView.findViewById(R.id.tv_shop_weight);
            tv_shop_price_original = (TextView) itemView.findViewById(R.id.tv_shop_price_original);
            tv_orders_sold_num = (TextView) itemView.findViewById(R.id.tv_orders_sold_num);

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
