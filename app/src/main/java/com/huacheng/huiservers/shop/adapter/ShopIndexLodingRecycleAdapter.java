package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.utils.CommonMethod;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类：
 * 时间：2018/5/29 13:01
 * 功能描述:Huiservers
 */
public class ShopIndexLodingRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ShopDetailBean> beanstag = new ArrayList<ShopDetailBean>();
    List<ShopIndexBean> mList = new ArrayList<ShopIndexBean>();

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

    BitmapUtils utils;
    String login_type;
    SharedPreferences preferencesLogin;
    TextView mTvShopNum;


    public ShopIndexLodingRecycleAdapter(Context context, TextView tvShopNum, List<ShopIndexBean> mList) {
        this.mList = mList;
        this.mContext = context;
        utils = new BitmapUtils(mContext);
        this.mTvShopNum = tvShopNum;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TYPE_ITEM;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shop_myshop_grad_item, parent, false);
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

            if (!StringUtils.isEmpty(mList.get(position).getTitle_img())) {
                Glide.with(mContext)
                        .load(MyCookieStore.URL + mList.get(position).getTitle_img())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_goods_bg)
                        .into(recyclerViewHolder.item_image);
            }
            recyclerViewHolder.item_name.setText(mList.get(position).getTitle());//
            recyclerViewHolder.txt_shop_price.setText("¥" + mList.get(position).getPrice());
            recyclerViewHolder.txt_orangil.setText("¥" + mList.get(position).getOriginal());
            recyclerViewHolder.txt_orangil.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线

            if (mList.get(position).getUnit().equals("")) {
                recyclerViewHolder.txt_shop_unit.setText("");
            } else {
                recyclerViewHolder.txt_shop_unit.setText("/" + mList.get(position).getUnit());
            }

            if (mList.get(position).getGoods_tag() != null) {
                if (mList.get(position).getGoods_tag().size() == 1) {
                    recyclerViewHolder.tv_tag1.setText(mList.get(position).getGoods_tag().get(0).getC_name());
                    recyclerViewHolder.tv_tag1.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_tag2.setVisibility(View.INVISIBLE);
                } else {
                    recyclerViewHolder.tv_tag1.setText(mList.get(position).getGoods_tag().get(0).getC_name());
                    recyclerViewHolder.tv_tag2.setText(mList.get(position).getGoods_tag().get(1).getC_name());
                    recyclerViewHolder.tv_tag1.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_tag2.setVisibility(View.VISIBLE);
                }

            } else {
                recyclerViewHolder.tv_tag1.setVisibility(View.INVISIBLE);
                recyclerViewHolder.tv_tag2.setVisibility(View.INVISIBLE);
            }
            recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", mList.get(position).getId());
                    System.out.println("shop_id********" + mList.get(position).getId());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });


            recyclerViewHolder.iv_shop_list_flag.setVisibility(View.VISIBLE);
            ShopIndexBean index = mList.get(position);
            String discount = index.getDiscount();
            if (discount.equals("1")) {
                recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_spike);
            } else {
                if (index.getIs_hot().equals("1")) {
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_hotsell);
                } else if (index.getIs_new().equals("1")) {
                    recyclerViewHolder.iv_shop_list_flag.setBackgroundResource(R.drawable.ic_shoplist_newest);
                } else {
                    recyclerViewHolder.iv_shop_list_flag.setBackground(null);
                }
            }

            recyclerViewHolder.iv_gouwu.setOnClickListener(new View.OnClickListener() {
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

                        if (mList.get(position).getExist_hours().equals("2")) {
                            XToast.makeText(mContext, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
                        } else {
                            if (mList.get(position) != null) {
                                new CommonMethod(mList.get(position), mTvShopNum, mContext).getShopLimitTag();
                            }
                        }

                    } else {
                        XToast.makeText(mContext, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                    }


                    // getAddShop(position);

                }
            });


        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                /*case LOADING: // 正在加载
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
                    break;*/

                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
       /* if ((mList.size() % 2) == 0) {

        } else {
            return mList.size() - 1;
        }*/
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
        LinearLayout lin_article1, ly_onclick;
        ImageView item_image, iv_gouwu, iv_shop_list_flag;
        TextView item_name, txt_shop_price, txt_orangil, txt_shop_unit, tv_tag1, tv_tag2;

        RecyclerViewHolder(View v) {
            super(v);
            iv_shop_list_flag = (ImageView) itemView.findViewById(R.id.iv_shop_list_flag);
            lin_article1 = (LinearLayout) v.findViewById(R.id.lin_article1);
            ly_onclick = (LinearLayout) v.findViewById(R.id.ly_onclick);
            item_image = (ImageView) v.findViewById(R.id.item_image);
            iv_gouwu = (ImageView) v.findViewById(R.id.iv_gouwu);
            item_name = (TextView) v.findViewById(R.id.item_name);
            txt_shop_price = (TextView) v.findViewById(R.id.txt_shop_price);
            txt_shop_unit = (TextView) v.findViewById(R.id.txt_shop_unit);
            txt_orangil = (TextView) v.findViewById(R.id.txt_orangil);
            tv_tag1 = (TextView) v.findViewById(R.id.tv_tag1);
            tv_tag2 = (TextView) v.findViewById(R.id.tv_tag2);
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


