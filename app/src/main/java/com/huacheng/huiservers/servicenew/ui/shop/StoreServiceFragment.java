package com.huacheng.huiservers.servicenew.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.inter.OnRefreshAndLoadMoreListener;
import com.huacheng.huiservers.servicenew.model.CategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelServiceItem;
import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.servicenew.ui.adapter.ServiceFragmentAdapter;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.widget.loadmorelistview.PagingListView;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.DeviceUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.lzy.widget.HeaderScrollHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 店铺页服务Fragment
 * created by wangxiaotao
 * 2018/8/31 0031 上午 11:40
 */
public class StoreServiceFragment extends BaseFragment implements View.OnClickListener , OnRefreshAndLoadMoreListener, HeaderScrollHelper.ScrollableContainer {
    private PagingListView listView;//加载更多的listview
    private List <ModelServiceItem>mDatas = new ArrayList();
    private ServiceFragmentAdapter<ModelServiceItem> adapter;
    //筛选条件展示tag
    private RelativeLayout rel_alltag;
    private TagFlowLayout mtagFlowLayout;
    private View v_trans;
    private LinearLayout ll_tag_container;
    private FrameLayout fl_tag_click;
    String[] filters;
    public List<CategoryBean> categoryBeanList=new ArrayList<>();
    private int selectedPosition=0;
    private HorizontalScrollView h_scrollview;
    private int page=1;
    private SmartRefreshLayout refreshLayout;
    private View rel_no_data;
    private String store_id="";
    private int total_Pages=0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        store_id= arguments.getString("store_id");
    }

    @Override
    public void initView(View view) {
        listView = (PagingListView) view.findViewById(R.id.listView);
        adapter = new ServiceFragmentAdapter<>(mDatas, mActivity);
        listView.setAdapter(adapter);
        rel_no_data = view.findViewById(R.id.rel_no_data);
        rel_no_data.setVisibility(View.GONE);
        rel_alltag = view.findViewById(R.id.rel_alltag);
        mtagFlowLayout = view.findViewById(R.id.flowlayout_taglist);
        v_trans=view.findViewById(R.id.v_trans);
        ll_tag_container=view.findViewById(R.id.ll_tag_container);
        fl_tag_click=view.findViewById(R.id.fl_tag_click);
        h_scrollview = view.findViewById(R.id.h_scrollview);
    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {
        fl_tag_click.setOnClickListener(this);
        v_trans.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent = new Intent(mActivity, ServiceDetailActivity.class);
                intent.putExtra("service_id", mDatas.get(position).getId());
                startActivity(intent);
            }
        });
        //一开始先设置成false onScrollListener
        listView.setHasMoreItems(false);
        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                requestData(selectedPosition);
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //第一次加载的数据是从activity的接口中带过来的
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_store_service;
    }

    @Override
    public View getScrollableView() {
        return listView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.fl_tag_click){
            //点击按钮
            if (rel_alltag.getVisibility()==View.GONE){
                if (filters==null){
                    return;
                }
                    rel_alltag.setVisibility(View.VISIBLE);
                    showTabTag();
            }else {
                    rel_alltag.setVisibility(View.GONE);
            }
        }else if (v.getId()==R.id.v_trans){
            rel_alltag.setVisibility(View.GONE);
        }
    }
    private void showTabTag() {
        mtagFlowLayout.setAdapter(
                new TagAdapter<String>(filters) {

                    @Override
                    public View getView(FlowLayout parent, int position, String mTitle2) {
                        View convertView = LayoutInflater.from(mContext).inflate(R.layout.goods_tagtxt, mtagFlowLayout, false);
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

        mtagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //   RotateUtils.rotateArrow(ivCategoryArrow, true);
                rel_alltag.setVisibility(View.GONE);
                // 请求
                page=1;
                showDialog(smallDialog);
                requestData(position);
                return true;
            }
        });

    }

    public void inflateTag(){
        if (ll_tag_container.getChildCount() == 0) {
            for (int i = 0; i < filters.length; i++) {
                final CheckedTextView tv = new CheckedTextView(mContext);
                tv.setText(filters[i] + "");
                tv.setTextSize(14);
                tv.setTextColor(mContext.getResources().getColorStateList(R.color.text_tag_species));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                            requestData(final_I);
                        }
                    }
                });
                ll_tag_container.addView(tv);
            }
        }
    }

    /**
     * 请求数据
     * @param final_i
     */
    private void requestData(final int final_i) {
        if (categoryBeanList.size()==0){
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id",store_id);
        params.put("category",categoryBeanList.get(final_i).getCategory()+"");
        params.put("p",page+"");
        MyOkHttp.get().get(ApiHttpClient.GET_SHOP_SERVICE_LIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                listView.setIsLoading(false);
                updateTag( final_i);
                if (refreshLayout!=null){
                  refreshLayout.finishLoadMore();
                   refreshLayout.finishRefresh();
                }
                if (JsonUtil.getInstance().isSuccess(response)){
                    List <ModelServiceItem>data = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelServiceItem.class);
                    if (data!=null&&data.size()>0){
                        if (page==1){
                            mDatas.clear();
                            listView.post(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setSelection(0);
                                }
                            });
                        }
                        mDatas.addAll(data);
                        page++;
                        total_Pages=mDatas.get(0).getTotal_Pages();
                        rel_no_data.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        if (page>total_Pages){
//                            if (refreshLayout!=null){
//                                refreshLayout.setEnableLoadMore(false);
//
//                            }
                            listView.setHasMoreItems(false);
                        }else {
//                            if (refreshLayout!=null){
//                                refreshLayout.setEnableLoadMore(true);
//                            }
                            listView.setHasMoreItems(true);
                        }
                    }else {
                        if (page==1){
                            mDatas.clear();
                            rel_no_data.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            total_Pages=0;
                        }
//                        if (refreshLayout!=null){
//                            refreshLayout.setEnableLoadMore(false);
//                        }
                        listView.setHasMoreItems(false);
                    }
                }else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response,"获取数据失败");
                    XToast.makeText(mContext, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                listView.setIsLoading(false);
                if (refreshLayout!=null){
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                }
                XToast.makeText(mContext, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
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
                    h_scrollview.smoothScrollTo(childAt.getLeft()-DeviceUtils.dip2px(mActivity,12),0);
                }else {
                    childAt.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        this.refreshLayout= (SmartRefreshLayout) refreshLayout;
        // 刷新
        page=1;
        requestData(selectedPosition);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        // 加载更多
        this.refreshLayout= (SmartRefreshLayout) refreshLayout;
        requestData(selectedPosition);
    }

    /**
     * 是否可加载更多
     * @return
     */
    @Override
    public boolean canLoadMore() {
        if (mDatas.size()==0||page > total_Pages){
            return false;
        }
        return true;
    }

    public List<ModelServiceItem> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<ModelServiceItem> datas) {
        //这里获取一下refreshlayout
        refreshLayout=((ServiceStoreActivity)mActivity).refreshLayout;
        //调用此方法时还没调initview
        if (datas!=null&&datas.size()>0){
            if (page==1){
                mDatas.clear();
            }
            mDatas.addAll(datas);
            page++;
            total_Pages=mDatas.get(0).getTotal_Pages();
            rel_no_data.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            if (page>total_Pages){
//                if (refreshLayout!=null){
//                    refreshLayout.setEnableLoadMore(false);
//                }
                listView.setHasMoreItems(false);
            }else {
//                if (refreshLayout!=null){
//                    refreshLayout.setEnableLoadMore(true);
//                }
                listView.setHasMoreItems(true);
            }
        }else {
            if (page==1){
                rel_no_data.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
                total_Pages=0;
            }
//            if (refreshLayout!=null){
//                refreshLayout.setEnableLoadMore(false);
            listView.setHasMoreItems(false);
//            }
        }
    }

    public void setCategoryBeanList(List<CategoryBean> categoryBeanList) {
        this.categoryBeanList .clear();
        this.categoryBeanList.addAll(categoryBeanList);
        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setCategory("0");
        categoryBean.setCategory_cn("全部");
        this.categoryBeanList.add(0,categoryBean);
        filters=new String[this.categoryBeanList.size()];
        for (int i = 0; i < this.categoryBeanList.size(); i++) {
            filters[i]= this.categoryBeanList.get(i).getCategory_cn()+"";
        }
        inflateTag();
    }

}
