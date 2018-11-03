package com.huacheng.huiservers.center;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.BaseFragmentOld;
import com.huacheng.huiservers.fragment.bean.ShopIndexBean;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.adapter.ShopXSList4_YesAdapter;
import com.huacheng.huiservers.shop.bean.CateBean;
import com.huacheng.huiservers.shop.bean.ModelSeckillBean;
import com.huacheng.huiservers.shop.inter.OnTabSelectListener;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.RecyclerViewNoBugLinearLayoutManager;
import com.huacheng.libraryservice.utils.DeviceUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 限时抢购（正在抢购）
 */

public class ShopXS4TimeListFragment extends BaseFragmentOld implements OnTabSelectListener {
    private RelativeLayout rel_no_data;
    ShopXSList4_YesAdapter mYesAdapter;
    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;//当前页
    int totalPage = 0;//总页数
    ShopProtocol mProtocol = new ShopProtocol();
  //  List<ShopIndexBean> mBeanList = new ArrayList<>();
    List<ShopIndexBean> mBeanALList = new ArrayList<>();
    private String cate_id;
    SharePrefrenceUtil prefrenceUtil;
    private LinearLayout ll_top;
    private View fl_tag_click;
    private RelativeLayout rel_alltag;
    private View v_trans;
    private TagFlowLayout flowlayout_taglist;
    String[] filters;
    private LinearLayout ll_tag_container;
    private int selectedPosition=0;
    private HorizontalScrollView h_scrollview;
    private List<CateBean> class_nameList=new ArrayList<>();

    public ShopXS4TimeListFragment() {
        super();
    }

    @Override
    protected void initView() {
   //     SetTransStatus.GetStatus(getActivity());//系统栏默认为黑色
        prefrenceUtil = new SharePrefrenceUtil(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);

        mRecyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mYesAdapter = new ShopXSList4_YesAdapter(mActivity, mBeanALList);
        mRecyclerView.setAdapter(mYesAdapter);
        showDialog(smallDialog);
        getdata(page + "",0);
        // 设置下拉刷新
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                // 刷新数据
                page = 1;
                getdata(page + "",selectedPosition);

//                // 延时1s关闭下拉刷新
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 500);

            }
        });

        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (page<=totalPage){
                    mYesAdapter.setLoadState(mYesAdapter.LOADING);
                    getdata(page + "",selectedPosition);
                }

//                if (page < totalPage) {
//                    new Timer().schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    page++;
//                                    getdata(page + "");
//                                    mYesAdapter.setLoadState(mYesAdapter.LOADING_COMPLETE);
//
//
//                                }
//                            });
//                        }
//                    }, 600);
//
//                } else {
//                    // 显示加载到底的提示
//                    mYesAdapter.setLoadState(mYesAdapter.LOADING_END);
//                }

            }
        });
        ll_top = findViewById(R.id.ll_top);
        h_scrollview = findViewById(R.id.h_scrollview);
        fl_tag_click = findViewById(R.id.fl_tag_click);
        ll_tag_container = findViewById(R.id.ll_tag_container);
        rel_alltag = findViewById(R.id.rel_alltag);
        v_trans = findViewById(R.id.v_trans);
        flowlayout_taglist = findViewById(R.id.flowlayout_taglist);
        fl_tag_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rel_alltag.getVisibility()==View.GONE){
                    if (filters==null){
                        return;
                    }
                    rel_alltag.setVisibility(View.VISIBLE);
                    showTabTag();
                }else {
                    rel_alltag.setVisibility(View.GONE);
                }
            }
        });
        v_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_alltag.setVisibility(View.GONE);
            }
        });
    }
    private void showTabTag() {
        flowlayout_taglist.setAdapter(
                new TagAdapter<String>(filters) {

                    @Override
                    public View getView(FlowLayout parent, int position, String mTitle2) {
                        View convertView = LayoutInflater.from(mActivity).inflate(R.layout.goods_tagtxt, flowlayout_taglist, false);
                        TextView tv = convertView.findViewById(R.id.tv_shoptagtxt);
                        if (selectedPosition == position) {
                            tv.setSelected(true);
                            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shoptag_checked_bg));
                            tv.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            tv.setSelected(false);
                            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.shoptag_normal_bg));
                            tv.setTextColor(getResources().getColor(R.color.text_color));
                        }
                        tv.setText(mTitle2);
                        return tv;
                    }
                }
        );

        flowlayout_taglist.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //   RotateUtils.rotateArrow(ivCategoryArrow, true);
                rel_alltag.setVisibility(View.GONE);
                // 请求
                page=1;
                showDialog(smallDialog);
                getdata(page+"",position);
                return true;
            }
        });
    }
    private void getdata(String total_Page, final int position) {

        Url_info info = new Url_info(getActivity());
        String class_id=0+"";
        if (class_nameList.size()>0){
            class_id=class_nameList.get(position).getId()+"";
        }
        String url = info.pro_discount_list + "c_id/" + prefrenceUtil.getXiaoQuId() + "/is_star/" + "1" + "/p/" + total_Page+"/class_id/"+class_id;
        new HttpHelper(url, getActivity()) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                           swipeRefreshLayout.setRefreshing(false);
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (JsonUtil.getInstance().isSuccess(jsonObject)){
                        ModelSeckillBean modelSeckillBean = (ModelSeckillBean) JsonUtil.getInstance().parseJsonFromResponse(jsonObject, ModelSeckillBean.class);
                        updateTag(position);
                        if (modelSeckillBean!=null&&modelSeckillBean.getClass_name()!=null){
                            if (class_nameList.size()==0){
                                CateBean cateBean = new CateBean();
                                cateBean.setId("0");
                                cateBean.setCate_name("全部");
                                class_nameList.add(cateBean);
                                class_nameList.addAll(modelSeckillBean.getClass_name());
                                initCateBean(class_nameList);
                            }
                            if (page==1){
                                mBeanALList.clear();
                            }
                            List<ShopIndexBean> list_data = modelSeckillBean.getList();
                            if (list_data!=null){
                                mBeanALList.addAll(list_data);
                            }

                            if (mBeanALList.size() > 0) {
                                if (page==1){
                                    ll_top.setVisibility(View.VISIBLE);
                                }
                                mRecyclerView.setVisibility(View.VISIBLE);
                                rel_no_data.setVisibility(View.GONE);
                                page++;
                                totalPage = Integer.valueOf(mBeanALList.get(0).getTotal_Pages());// 设置总页数
                                if (page>totalPage){
                                    mYesAdapter.setLoadState(mYesAdapter.LOADING_END);
                                }else {
                                    mYesAdapter.setLoadState(mYesAdapter.LOADING_COMPLETE);
                                }
                                mYesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (page==1){
                                mRecyclerView.setVisibility(View.GONE);
                                rel_no_data.setVisibility(View.VISIBLE);
                                ll_top.setVisibility(View.GONE);
                            }else {
                                mYesAdapter.setLoadState(mYesAdapter.LOADING_COMPLETE);
                            }
                        }
                    }else {
                        if (page==1){
                            mRecyclerView.setVisibility(View.GONE);
                            rel_no_data.setVisibility(View.VISIBLE);
                            ll_top.setVisibility(View.GONE);
                        }else {
                            mYesAdapter.setLoadState(mYesAdapter.LOADING_COMPLETE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (mYesAdapter!=null){
                    mYesAdapter.setLoadState(mYesAdapter.LOADING_COMPLETE);
                }
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }
    /**
     * 分类
     */
    private void initCateBean(List<CateBean> cateBeanList) {
        filters=new String[cateBeanList.size()];
        for (int i = 0; i < cateBeanList.size(); i++) {
            filters[i]= cateBeanList.get(i).getCate_name()+"";
        }
        inflateTag();
    }
    public void inflateTag(){
        if (ll_tag_container.getChildCount() == 0) {
            for (int i = 0; i < filters.length; i++) {
                final CheckedTextView tv = new CheckedTextView(mActivity);
                tv.setText(filters[i] + "");
                tv.setTextSize(14);
                tv.setTextColor(mActivity.getResources().getColorStateList(R.color.text_tag_species));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(DeviceUtils.dip2px(mActivity,28), 0, 0,0);
                tv.setLayoutParams(params);
                // 如果是秒杀或者拼团就设置如下的padding,不是就设置普通的padding
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTag(i);
                if (i==selectedPosition){
                    tv.setChecked(true);
                }else {
                    tv.setChecked(false);
                }
                final  int final_I= i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        if (tv.isChecked()) {

                            //什么也不做
                        } else {
                            //请求数据
                            page = 1;
                            showDialog(smallDialog);
                            getdata(page+"",position);
                        }
                    }
                });
                ll_tag_container.addView(tv);
            }
        }
    }
    /**
     * 请求成功后更新选中状态
     */
    private void updateTag(int position) {
        selectedPosition=position;
        if (ll_tag_container.getChildCount() > 0) {
            for (int i = 0; i < ll_tag_container.getChildCount(); i++) {
                CheckedTextView childAt = (CheckedTextView) ll_tag_container.getChildAt(i);
                if (position==i){
                    childAt.setChecked(true);
                    h_scrollview.smoothScrollTo(childAt.getLeft()- DeviceUtils.dip2px(mActivity,12),0);
                }else {
                    childAt.setChecked(false);
                }
            }
        }
    }
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.shopxs4_timelist;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mYesAdapter != null) {
            mYesAdapter.cancelAllTimers();
        }
    }

    @Override
    public void onCatSelect(String cat_id) {
        //useless
    }
}
