package com.huacheng.huiservers.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.huacheng.huiservers.utils.CommonMethod;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;

import java.util.List;

/**
 * Created by Administrator on 2018/03/20.
 */

public class HomeListFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;

    List<ShopIndexBean> mDatas;
    String login_type;
    SharedPreferences preferencesLogin;
    private boolean mShowFooter = true;
    private TextView mtvShopCar;
    int mPage = 0;

    public HomeListFragmentAdapter(List<ShopIndexBean> proLists, Context context) {//, List<String> dataSource
        this.mDatas = proLists;
        this.mContext = context;

    }

    public HomeListFragmentAdapter(List<ShopIndexBean> proLists, TextView tvShopCar, int page, Context context) {//, List<String> dataSource
        this.mDatas = proLists;
        this.mContext = context;
        this.mtvShopCar = tvShopCar;
        this.mPage = page;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_list_item, parent, false);
        view.setOnClickListener(this);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.itemView.setTag(position);

            recyclerViewHolder.iv_shop_list_flag.setVisibility(View.VISIBLE);
            ShopIndexBean index=  mDatas.get(position);
            String discount = index.getDiscount();
            if (discount.equals("1")) {
//            holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_spike);
            } else {
                if (index.getIs_hot().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_hotsell);
                } else if (index.getIs_new().equals("1")) {
//                holder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_newest);
                } else {
                    recyclerViewHolder.iv_shop_list_flag.setBackground(null);
                }
            }

            /*if (mDatas.get(position).getIs_hot().equals("1") && mDatas.get(position).getIs_new().equals("1")) {

                recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_spike);
            } else {
                if (mDatas.get(position).getIs_hot().equals("1")) {
                    recyclerViewHolder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_hotsell);
                } else {
                    if (mDatas.get(position).getIs_new().equals("1")) {
                        recyclerViewHolder.iv_shop_list_flag.setVisibility(View.VISIBLE);
                        recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_newest);
                    }
                }
            }*/

            Glide.with(mContext).load(MyCookieStore.URL + mDatas.get(position).getTitle_img()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
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
            if (mDatas.get(position).getInventory().equals("0") || TextUtils.isEmpty(mDatas.get(position).getInventory())
                    || 0 > Integer.valueOf(mDatas.get(position).getInventory())) {
                recyclerViewHolder.tv_shouqing.setVisibility(View.VISIBLE);
            } else {
                recyclerViewHolder.tv_shouqing.setVisibility(View.GONE);
            }
            recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).getInventory().equals("0") || TextUtils.isEmpty(mDatas.get(position).getInventory())) {
                        XToast.makeText(mContext, "商品已售罄", XToast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(mContext, ShopDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("shop_id", mDatas.get(position).getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }

                }
            });
            recyclerViewHolder.tv_shop_price.setText("¥" + mDatas.get(position).getPrice());
            if (mDatas.get(position).getUnit().equals("")) {
                recyclerViewHolder.tv_shop_weight.setText("");
            } else {
                recyclerViewHolder.tv_shop_weight.setText("/" + mDatas.get(position).getUnit());
            }

            String onum = mDatas.get(position).getOrder_num();
            recyclerViewHolder.tv_shop_price_original.setText("¥" + mDatas.get(position).getOriginal());
            recyclerViewHolder.tv_shop_price_original.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            recyclerViewHolder.tv_orders_sold_num.setText("已售" + onum);

            recyclerViewHolder.iv_shopcar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
                        if (mDatas.get(position).getInventory().equals("0") || TextUtils.isEmpty(mDatas.get(position).getInventory())) {
                            XToast.makeText(mContext, "商品已售罄", XToast.LENGTH_SHORT).show();
                        } else {
//                            mOnItemCarListener.onCarClick(position);
                            if (mDatas.get(position).getExist_hours().equals("2")) {
                                XToast.makeText(mContext, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                            } else {
                                if (mDatas.get(position) != null) {
                                    if (mtvShopCar != null) {
                                        new CommonMethod(mDatas.get(position), mtvShopCar, mContext).getShopLimitTag();
                                    } else {
                                        new CommonMethod(mDatas.get(position), mContext).getShopLimitTag();
                                    }
                                }
                            }
                        }
                    } else {
                        XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                    }
                }


            });

        }
    }

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
        return mDatas.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_shop_list_flag, iv_title_img, iv_shopcar;
        TextView tv_title, tv_shop_price, tv_shop_weight, tv_orders_sold_num, tv_shop_price_original, tv_shouqing;

        LinearLayout lin_goodslist_Tag, ly_onclick, lin_shop_list_car;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            iv_shop_list_flag = (ImageView) itemView.findViewById(R.id.iv_shop_list_flag);
            iv_title_img = (ImageView) itemView.findViewById(R.id.iv_title_img);
            iv_shopcar = (ImageView) itemView.findViewById(R.id.iv_shopcar);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_shouqing = (TextView) itemView.findViewById(R.id.tv_shouqing);
            lin_goodslist_Tag = (LinearLayout) itemView.findViewById(R.id.lin_goodslist_Tag);
            ly_onclick = (LinearLayout) itemView.findViewById(R.id.ly_onclick);
            tv_shop_price = (TextView) itemView.findViewById(R.id.tv_shop_price);
            tv_shop_weight = (TextView) itemView.findViewById(R.id.tv_shop_weight);
            tv_shop_price_original = (TextView) itemView.findViewById(R.id.tv_shop_price_original);
            tv_orders_sold_num = (TextView) itemView.findViewById(R.id.tv_orders_sold_num);

        }
    }

}
