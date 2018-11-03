package com.huacheng.huiservers.servicenew.ui.shop;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.inter.OnRefreshAndLoadMoreListener;
import com.huacheng.huiservers.servicenew.model.ModelCommentItem;
import com.huacheng.huiservers.servicenew.ui.adapter.CommentFragmentAdapter;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.widget.loadmorelistview.PagingListView;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.lzy.widget.HeaderScrollHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 店铺评论Fragment
 * created by wangxiaotao
 * 2018/8/31 0031 上午 11:44
 */
public class StoreCommentFragment extends BaseFragment implements OnRefreshAndLoadMoreListener, HeaderScrollHelper.ScrollableContainer {
    private PagingListView listView;
    private RelativeLayout rel_alltag;
    private LinearLayout ll_top;
    private CommentFragmentAdapter adapter;
    private List<ModelCommentItem> mDatas=new ArrayList<>();
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
        rel_alltag = view.findViewById(R.id.rel_alltag);
        rel_alltag.setVisibility(View.GONE);
        ll_top = view.findViewById(R.id.ll_top);
        ll_top.setVisibility(View.GONE);
        listView = (PagingListView) view.findViewById(R.id.listView);
        adapter=new CommentFragmentAdapter(mDatas,mActivity);
        listView.setAdapter(adapter);
        rel_no_data = view.findViewById(R.id.rel_no_data);
    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getActivity(), "点击了条目" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //一开始先设置成false onScrollListener
        listView.setHasMoreItems(false);
        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                requestData();
            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        refreshLayout=((ServiceStoreActivity)mActivity).refreshLayout;
        showDialog(smallDialog);
        requestData();
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        // 刷新
        this.refreshLayout= (SmartRefreshLayout) refreshLayout;
        page=1;
        requestData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //加载更多
        this.refreshLayout= (SmartRefreshLayout) refreshLayout;
        requestData();
    }

    @Override
    public boolean canLoadMore() {
        if (page>total_Pages||mDatas.size()==0){
            return false;
        }
       return true;
    }

    /**
     * 访问接口请求数据
     */
    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id",store_id);
        params.put("p",page+"");
        MyOkHttp.get().get(ApiHttpClient.GET_SHOP_COMMENT_LIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                listView.setIsLoading(false);
                if (refreshLayout!=null){
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishRefresh();
                }
                if (JsonUtil.getInstance().isSuccess(response)){
                    List <ModelCommentItem>data = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelCommentItem.class);
                    if (data!=null&&data.size()>0){
                        if (page==1){
                            mDatas.clear();
                        }
                        mDatas.addAll(data);
                        page++;
                        total_Pages=mDatas.get(0).getTotal_Pages();
                        rel_no_data.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        if (page>total_Pages){
//                            if (refreshLayout!=null){
//                                refreshLayout.setEnableLoadMore(false);
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

}
