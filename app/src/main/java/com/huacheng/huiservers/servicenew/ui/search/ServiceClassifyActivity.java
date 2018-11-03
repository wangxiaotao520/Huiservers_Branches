package com.huacheng.huiservers.servicenew.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceCat;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.servicenew.ui.adapter.ServiceCatAdapter;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * created by wangxiaotao
 * 2018/9/7 0007 下午 6:04
 */
public class ServiceClassifyActivity extends BaseActivity implements ServiceCatAdapter.OnServiceCatClickListener {
    private ListView mListView;
    private ServiceCatAdapter<ModelServiceCat> adapter;
    private List<ModelServiceCat> mDatas = new ArrayList<>();
    public static final int REQUEST_CODE_CAT = 111;
    SharePrefrenceUtil prefrenceUtil;

    @Override
    protected void initView() {
        findTitleViews();
        titleName.setText("服务分类");
        mListView = findViewById(R.id.listview);
        prefrenceUtil = new SharePrefrenceUtil(this);
        adapter = new ServiceCatAdapter<>(mDatas, this, this);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        showDialog(smallDialog);
        HashMap<String,String> params=new HashMap<>();
        params.put("community_id",prefrenceUtil.getXiaoQuId());
        MyOkHttp.get().get(ApiHttpClient.SERVICE_CLASSIF, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<ModelServiceCat> list = (List<ModelServiceCat>) JsonUtil.getInstance().getDataArrayByName(response, "data", ModelServiceCat.class);
                    mDatas.clear();
                    mDatas.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(mContext, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(mContext, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_cat;
    }

    String mAllCate = "";

    @Override
    protected void initIntentData() {
        String allCate = getIntent().getStringExtra("allCate");
        if (!NullUtil.isStringEmpty(allCate)) {
            if ("0".equals(allCate)) {
                mAllCate = allCate;
            }
        }

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onServiceCatClick(int catPosition, int gridPositon) {

        if (!NullUtil.isStringEmpty(mAllCate)) {
            Intent intent = new Intent(this, MerchantServiceListActivity.class);
            Bundle bundle = new Bundle();
            //加入全部分类
            if (catPosition == 0) {
                bundle.putString("top_id", "0");
                bundle.putString("sub_id", "0");
                bundle.putString("sub_name", "全部");
            } else {
                ModelServiceCat modelServiceCat = mDatas.get(catPosition - 1);
                ModelServiceCat.GridBean gridBean = modelServiceCat.getList().get(gridPositon);
                bundle.putString("top_id", gridBean.getP_id() + "");
                bundle.putString("sub_id", gridBean.getId() + "");
                bundle.putString("sub_name", gridBean.getName() + "");
            }
            bundle.putString("allCate", "all");
            intent.putExtras(bundle);
            startActivity(intent);

        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            //加入全部分类
            if (catPosition == 0) {
                bundle.putString("top_id", "0");
                bundle.putString("sub_id", "0");
                bundle.putString("sub_name", "全部");
            } else {
                ModelServiceCat modelServiceCat = mDatas.get(catPosition - 1);
                ModelServiceCat.GridBean gridBean = modelServiceCat.getList().get(gridPositon);
                bundle.putString("top_id", gridBean.getP_id() + "");
                bundle.putString("sub_id", gridBean.getId() + "");
                bundle.putString("sub_name", gridBean.getName() + "");
            }
            intent.putExtras(bundle);
            setResult(REQUEST_CODE_CAT, intent);
        }

        finish();


    }
}
