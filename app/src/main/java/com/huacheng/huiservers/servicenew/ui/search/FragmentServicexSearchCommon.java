package com.huacheng.huiservers.servicenew.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.view.FlowLayout;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.base.BaseFragment;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Badge on 2018/9/7.
 */

public class FragmentServicexSearchCommon extends BaseFragment {

    @BindView(R.id.rel_no_data)
    RelativeLayout relNoData;
    Unbinder unbinder;

    private FlowLayout mFlowLayout;
    List<String> tags = new ArrayList<>();
    Intent intent = new Intent();

    public static final String EXTRA_KEY_TYPE = "extra_key_type";
    public static final String EXTRA_KEY_KEYWORD = "extra_key_keyword";
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    private String mType;
    private List<String> mHistoryKeywords;//记录文本
    private ServiceSearchHistoryAdapter histroyAdapter;
    private LinearLayout mSearchHistoryLl;
    LinearLayout lin_hotTag;
    int type;
    MyListView listView;

    SharePrefrenceUtil sharePrefrenceUtil;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        type = getArguments().getInt("type");
    }

    @Override
    public void initView(View view) {
        lin_hotTag = view.findViewById(R.id.lin_hotTag);
        mFlowLayout = view.findViewById(R.id.flowlayout);
        mSearchHistoryLl = view.findViewById(R.id.search_history_ll);
        listView = view.findViewById(R.id.search_history_lv);
        sharePrefrenceUtil=new SharePrefrenceUtil(mContext);

        view.findViewById(R.id.tv_clear_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanHistory();
            }
        });
    }

    /**
     * 清除历史纪录
     */
    public void cleanHistory() {
        mPref = mContext.getSharedPreferences("servicex_input" + type, MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        mHistoryKeywords.clear();
        histroyAdapter.notifyDataSetChanged();
        mSearchHistoryLl.setVisibility(View.GONE);
//        XToast.makeText(this, "清楚搜索历史成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void initIntentData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        showDialog(smallDialog);
        getContentTag("");
        initHistoryView();
    }

    public void searchKeyword(String keyword) {
        intent.setClass(mContext, SearchServiceResultActivity.class);
        Bundle bundle = new Bundle();
        if (type == 0) {
            bundle.putString("i_key", keyword);
        } else {
            bundle.putString("s_key", keyword);
        }
        bundle.putInt("serviceType", type);
        intent.putExtras(bundle);
        startActivityForResult(intent, 11);
        save(keyword);

    }

    private void getContentTag(String keyword) {
        HashMap<String, String> params = new HashMap<>();
        if (type == 0) {
            if (!"".equals(keyword)) {
                params.put("i_key", keyword);
            }
        } else {
            if (!"".equals(keyword)) {
                params.put("s_key", keyword);
            }
        }
        params.put("c_id", sharePrefrenceUtil.getXiaoQuId());
        MyOkHttp.get().get(ApiHttpClient.GET_SERVICEKEYS, params, new JsonResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                try {
                    String status = response.getString("status");
                    String data = response.getString("data");
                    String mData;
                    if (status.equals("1")) {

                        JSONObject jsonms = new JSONObject(data);
                        if (type == 0) {//商家
                            mData = jsonms.getString("i_key");
                        } else {//服务
                            mData = jsonms.getString("s_key");
                        }
                        JSONArray jsonAry = new JSONArray(mData);
                        for (int i = 0; i < jsonAry.length(); i++) {
                            tags.add(jsonAry.get(i).toString());
                        }
                        inflateFlowLayout();
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        initHistoryView();
    }

    private void initHistoryView() {
        mPref = mContext.getSharedPreferences("servicex_input" + type, MODE_PRIVATE);
        mType = mActivity.getIntent().getStringExtra(EXTRA_KEY_TYPE);
        String keyword = mActivity.getIntent().getStringExtra(EXTRA_KEY_KEYWORD);
        if (!TextUtils.isEmpty(keyword)) {
//            input.setText(keyword);
        }
        mHistoryKeywords = new ArrayList<String>();
        initSearchHistory();
    }

    /**
     * 初始化搜索历史记录
     */
    public void initSearchHistory() {

        String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(history)) {
//            mSearchHistoryLl.setVisibility(View.VISIBLE);
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        } else {
//            mSearchHistoryLl.setVisibility(View.GONE);

        }
        if (mHistoryKeywords != null && mHistoryKeywords.size() > 0) {
            mSearchHistoryLl.setVisibility(View.VISIBLE);
            histroyAdapter = new ServiceSearchHistoryAdapter(mHistoryKeywords, type, mContext);
            listView.setAdapter(histroyAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    intent.setClass(mContext, SearchServiceResultActivity.class);
                    Bundle bundle = new Bundle();
                    if (type == 0) {
                        bundle.putString("i_key", mHistoryKeywords.get(i));
                    } else {
                        bundle.putString("s_key", mHistoryKeywords.get(i));
                    }
                    bundle.putInt("serviceType", type);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 11);

                    save(mHistoryKeywords.get(i));
//                input.setText(mHistoryKeywords.get(i));
//                mSearchHistoryLl.setVisibility(View.GONE);
                }
            });
            histroyAdapter.notifyDataSetChanged();
        } else {
            mSearchHistoryLl.setVisibility(View.GONE);
        }


    }

    public static final int REQUEST_CODE_SEARCH_CAT = 222;

    /**
     * 将数据放入流式布局
     */
    private void inflateFlowLayout() {

        if (tags.size() > 0) {
            lin_hotTag.setVisibility(View.VISIBLE);
            for (int i = 0; i < tags.size(); i++) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(
                        R.layout.search_label_tv1, mFlowLayout, false);
                tv.setText(tags.get(i));
                final String str = tv.getText().toString();
                //点击事件
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Intent intent = new Intent();
                        intent.setClass(mContext, SearchServiceResultActivity.class);
                        Bundle bundle = new Bundle();
                        if (type == 0) {
                            bundle.putString("i_key", str);
                        } else {
                            bundle.putString("s_key", str);
                        }
                        bundle.putInt("serviceType", type);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 11);
//                        mActivity.setResult(REQUEST_CODE_SEARCH_CAT, intent);

                        //加入搜索历史纪录记录
//                        intent.putExtra("keystr", str);


                        save(str);
//                        initHistoryView();
//                        startActivity();
//                    Toast.makeText(SearchShopActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                mFlowLayout.addView(tv);
            }
        } else {
            lin_hotTag.setVisibility(View.GONE);
        }

    }

    /**
     * 储存搜索历史
     *
     * @param str
     */
    public void save(String str) {
        String text = str;
        String oldText = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        /*Log.e("tag", "" + oldText);
        Log.e("Tag", "" + text);
        Log.e("Tag", "" + oldText.contains(text));*/
        if (!TextUtils.isEmpty(text)) {//&& !()
            SharedPreferences.Editor editor;
            editor = mPref.edit();
            if (oldText.contains(text)) {
                oldText = StringUtils.subLimit(text + "," + oldText);
                editor.putString(KEY_SEARCH_HISTORY_KEYWORD, oldText);
                editor.commit();
                mHistoryKeywords.add(0, text);
            } else {
                editor.putString(KEY_SEARCH_HISTORY_KEYWORD, text + "," + oldText);
                editor.commit();
                mHistoryKeywords.add(0, text);
            }
            mSearchHistoryLl.setVisibility(View.GONE);
//            histroyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragement_service_search_common;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
