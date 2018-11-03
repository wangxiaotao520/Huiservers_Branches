package com.huacheng.huiservers.servicenew.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceDetail;
import com.huacheng.huiservers.servicenew.ui.adapter.item.ServiceCommentAdapter;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：评论列表
 * 时间：2018/9/7 18:19
 * created by DFF
 */
public class ServiceCommentActivity extends BaseActivity {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.list)
    ListView mList;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rel_no_data)
    RelativeLayout mRelNoData;

    String service_id = "";
    private int page = 1;
    ServiceCommentAdapter serviceCommentAdapter;
    List<ModelServiceDetail.ScoreInfoBean> mDatas = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        findTitleViews();
        mTitleName.setText("评论");

        serviceCommentAdapter = new ServiceCommentAdapter(this, mDatas);
        mList.setAdapter(serviceCommentAdapter);

    }

    @Override
    protected void initData() {

        showDialog(smallDialog);
        requestData();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                requestData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                requestData();
            }
        });
       /* mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", mDatas.get(position).getId());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });*/
    }

    /**
     * 请求数据
     */
    private void requestData() {
        /// 根据接口请求数据
        HashMap<String, String> params = new HashMap<>();
        params.put("id", service_id);
        params.put("p", page + "");
        MyOkHttp.get().post(ApiHttpClient.GET_SSERVICE_SCORELIST, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelServiceDetail.ScoreInfoBean> mScoreList = JsonUtil.getInstance().getDataArrayByName(response, "data", ModelServiceDetail.ScoreInfoBean.class);
                    if (mScoreList != null && mScoreList.size() > 0) {
                        mRelNoData.setVisibility(View.GONE);
                        if (page == 1) {
                            mDatas.clear();
                        }
                        mDatas.addAll(mScoreList);
                        page++;
                        if (page > mScoreList.get(0).getTotal_Pages()) {
                            mRefreshLayout.setEnableLoadMore(false);
                        } else {
                            mRefreshLayout.setEnableLoadMore(true);
                        }
                        serviceCommentAdapter.notifyDataSetChanged();
                    } else {
                        if (page == 1) {
                            mRelNoData.setVisibility(View.VISIBLE);
                            mDatas.clear();
                        }
                        mRefreshLayout.setEnableLoadMore(false);
                        serviceCommentAdapter.notifyDataSetChanged();
                    }

                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(mContext, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                XToast.makeText(mContext, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
                if (page == 1) {
                    mRefreshLayout.setEnableLoadMore(false);
                }
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_comment_list;
    }

    @Override
    protected void initIntentData() {
        service_id = getIntent().getStringExtra("service_id");
    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
