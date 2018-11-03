package com.huacheng.huiservers.oldhome;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.adapter.ShopCardPagerGgAdapter;
import com.huacheng.huiservers.fragment.card.IndexShadowTransformer;
import com.huacheng.huiservers.fragment.shop.ShopListFragmentAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.oldhome.adapter.OldListQzAdapter;
import com.huacheng.huiservers.oldhome.bean.Oldbean;
import com.huacheng.huiservers.protocol.OldProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.ShopListActivity;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.ImageCycleView;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.libraryservice.utils.NullUtil;
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2018/5/31 21:25
 * 功能描述:Huiservers
 */
public class OldHomeActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.ad_view)
    ImageCycleView mAdView;
    @BindView(R.id.list_quanzi)
    MyListView mListQuanzi;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    Dialog WaitDialog;
    @BindView(R.id.tv_oldQ_more)
    TextView mTvOldQMore;
    @BindView(R.id.tv_oldShop_more)
    TextView mTvOldShopMore;
    @BindView(R.id.ly_fengcai)
    LinearLayout mLyFengcai;
    @BindView(R.id.ly_chanpin)
    LinearLayout mLyChanpin;
    @BindView(R.id.iv_ad_view)
    ImageView ivAdView;

    Jump jump;
    SharePrefrenceUtil prefrenceUtil;
    ShopProtocol protocol = new ShopProtocol();
    List<BannerBean> beans = new ArrayList<BannerBean>();//广告数据
    List<BannerBean> beansCenter = new ArrayList<BannerBean>();//广告数据
    IndexShadowTransformer mIndexFragmentCardShadowTransformer;
    Oldbean mOldBean_p_url = new Oldbean();
    Oldbean mOldBean_s_url = new Oldbean();

    Oldbean mOldBean_all = new Oldbean();
    OldProtocol mOldProtocol = new OldProtocol();

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.old_home);
        ButterKnife.bind(this);
//        SetTransStatus.GetStatus(this);
        mTitleName.setText("居家养老");
        prefrenceUtil = new SharePrefrenceUtil(this);
        mRecyclerview.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setNestedScrollingEnabled(false);
        initdata();
        getoldQz();
    }


    private void initdata() {
        getbanner();
        getbannerH5();

    }

    private void getbanner() {//获取居家养老首页顶部广告信息
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_old_top");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, OldHomeActivity.this) {

            @Override
            protected void setData(String json) {
                //      hideDialog(smallDialog);
                beans = protocol.bannerInfo(json);
                if (beans != null) {
                    MyCookieStore.Shop_notify = 0;
                    //获取图片的宽高--end
                    System.out.println("777777777777---------" + beans.size());
                    if (beans.size() == 1) {
                        mAdView.setVisibility(View.GONE);
                        ivAdView.setVisibility(View.VISIBLE);

                        if (!NullUtil.isStringEmpty(beans.get(0).getImg_size())) {
                            ivAdView.getLayoutParams().width = ToolUtils.getScreenWidth(OldHomeActivity.this);
                            Double d = Double.valueOf(ToolUtils.getScreenWidth(OldHomeActivity.this)) / (Double.valueOf(beans.get(0).getImg_size()));
                            ivAdView.getLayoutParams().height = (new Double(d)).intValue();
                        }

                        Glide
                                .with(OldHomeActivity.this)
                                .load(beans.get(0).getImg())
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.icon_girdview)
                                .error(R.drawable.icon_girdview)
                                .into(ivAdView);

                        //顶部焦点
                        ivAdView.setFocusable(true);
                        ivAdView.setFocusableInTouchMode(true);
                        ivAdView.requestFocus();
                    } else {
                        ivAdView.setVisibility(View.GONE);
                        mAdView.setVisibility(View.VISIBLE);
                        //获取图片的宽高--start
                        if (!NullUtil.isStringEmpty(beans.get(0).getImg_size()) ) {
                            mAdView.getLayoutParams().width = ToolUtils.getScreenWidth(OldHomeActivity.this);
                            Double d = Double.valueOf(ToolUtils.getScreenWidth(OldHomeActivity.this)) / (Double.valueOf(beans.get(0).getImg_size()));
                            mAdView.getLayoutParams().height = (new Double(d)).intValue();
                        }

                        mAdView.setImageResources(beans, mAdCycleViewListener);
                        //顶部焦点
                        mAdView.setFocusable(true);
                        mAdView.setFocusableInTouchMode(true);
                        mAdView.requestFocus();
                    }

                } else {
                    mAdView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //       hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    private void getbannerH5() {//获取居家养老首页中部软文广告信息
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_old_center");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, OldHomeActivity.this) {

            @Override
            protected void setData(String json) {
                //        hideDialog(smallDialog);
                MyCookieStore.Shop_notify = 0;
                beansCenter = protocol.bannerInfo(json);
                if (beansCenter != null) {
                    //实例Viewpager适配器
                    ShopCardPagerGgAdapter mIndexFragmentCardAdapter = new ShopCardPagerGgAdapter(beansCenter, getSupportFragmentManager(),
                            StringUtils.dpToPixels(0, OldHomeActivity.this));
                    mIndexFragmentCardShadowTransformer = new IndexShadowTransformer(mViewPager, mIndexFragmentCardAdapter);
                    mViewPager.setAdapter(mIndexFragmentCardAdapter);
                    mViewPager.setPageTransformer(false, mIndexFragmentCardShadowTransformer);
                    mViewPager.setVisibility(View.VISIBLE);
                } else {
                    mViewPager.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                //        hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getoldQz() {//老年风采信息
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.old_index_new, params, OldHomeActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mOldBean_all = mOldProtocol.getOldHome(json);
                if (mOldBean_all.getS_list() != null) {
                    mLyFengcai.setVisibility(View.VISIBLE);
                    OldListQzAdapter OldListQzAdapter = new OldListQzAdapter(OldHomeActivity.this, mOldBean_all.getS_list());
                    mListQuanzi.setAdapter(OldListQzAdapter);
                } else {
                    mLyFengcai.setVisibility(View.GONE);
                }
                if (mOldBean_all.getP_list() != null && mOldBean_all.getP_list().size() > 0) {
                    mLyChanpin.setVisibility(View.VISIBLE);
                    ShopListFragmentAdapter fragmentShopListAdapter = new ShopListFragmentAdapter(mOldBean_all.getP_list(), OldHomeActivity.this);//, dataSource
                    mRecyclerview.setAdapter(fragmentShopListAdapter);
                } else {
                    mLyChanpin.setVisibility(View.GONE);
                }
                mOldBean_s_url = mOldBean_all.getS_url();
                mOldBean_p_url = mOldBean_all.getP_url();
                //          WaitDIalog.closeDialog(WaitDialog);
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");

            }
        };
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(BannerBean info, int position, View imageView) {//点击图片事件
            if (TextUtils.isEmpty(info.getUrl())) {
                if (beans.get(position).getUrl_type().equals("0") || TextUtils.isEmpty(beans.get(position).getUrl_type())) {
                    jump = new Jump(OldHomeActivity.this, info.getType_name(), info.getAdv_inside_url());
                } else {
                    jump = new Jump(OldHomeActivity.this, info.getUrl_type(), info.getType_name(), "", info.getUrl_type_cn());
                }
            } else {//URL不为空时外链
                jump = new Jump(OldHomeActivity.this, info.getUrl());

            }
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 使用ImageLoader对图片进行加装！
        }
    };

    @OnClick({R.id.lin_left, R.id.tv_oldQ_more, R.id.tv_oldShop_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        Bundle bunble = new Bundle();
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_oldQ_more://老年风采
                intent = new Intent(OldHomeActivity.this, OldHomeListActivity.class);
                bunble.putString("url", mOldBean_s_url.getPath());
                bunble.putString("c_id", mOldBean_s_url.getParam().getCommunity_id());
                bunble.putString("id", mOldBean_s_url.getParam().getC_id());
                intent.putExtras(bunble);
                startActivity(intent);

                break;
            case R.id.tv_oldShop_more://老年产品
                intent = new Intent(OldHomeActivity.this, ShopListActivity.class);
                intent.putExtra("cateID", mOldBean_p_url.getParam().getId());
                startActivity(intent);

                break;
        }
    }


}
