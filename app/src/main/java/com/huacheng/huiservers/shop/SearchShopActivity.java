package com.huacheng.huiservers.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.shop.adapter.SearchHistoryAdapter;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.FlowLayout;
import com.huacheng.huiservers.view.MyListView;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchShopActivity extends BaseUI implements View.OnClickListener {

    Intent intent = new Intent();
    //搜索
    private FlowLayout mFlowLayout;
    private LayoutInflater mInflater;

    /************
     * 以上为流式标签相关
     ************/
    public static final String EXTRA_KEY_TYPE = "extra_key_type";
    public static final String EXTRA_KEY_KEYWORD = "extra_key_keyword";
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    private String mType;
    private TextView txt_search, btn_search;
    private List<String> mHistoryKeywords;//记录文本

    private SearchHistoryAdapter histroyAdapter;
    private LinearLayout mSearchHistoryLl;
    ImageView search_back;
    EditText input;

    @Override
    protected void init() {
        super.init();
 //       SetTransStatus.GetStatus(this);
        setContentView(R.layout.shop_search);
        getTab();
        initHistoryView();
        search_back = (ImageView) findViewById(R.id.search_back);
        txt_search = (TextView) findViewById(R.id.txt_search);
        input = (EditText) findViewById(R.id.et_search);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(input, 0);
            }
        }, 300);

        search_back.setOnClickListener(this);
        txt_search.setOnClickListener(this);

    }


    private void initFlowView() {
        mInflater = LayoutInflater.from(this);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        initData1();
    }

    List<String> tags = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        initHistoryView();
    }

    private void getTab() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HttpHelper hh = new HttpHelper(info.goods_search_keys, new RequestParams(), this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    String status = jsonObj.getString("status");
                    String data = jsonObj.getString("data");
                    if (status.equals("1")) {
                        JSONArray jsonAry = new JSONArray(data);
                        for (int i = 0; i < jsonAry.length(); i++) {
                            tags.add(jsonAry.get(i).toString());
                        }
                        initFlowView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    /**
     * 将数据放入流式布局
     */
    private void initData1() {

        if (tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                TextView tv = (TextView) mInflater.inflate(
                        R.layout.search_label_tv, mFlowLayout, false);
                tv.setText(tags.get(i));
                final String str = tv.getText().toString();
                //点击事件
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加入搜索历史纪录记录
                        intent.setClass(SearchShopActivity.this, SearchResultActivity.class);
                        intent.putExtra("keystr", str);
                        startActivityForResult(intent, 1);
                        save(str);
//                        initHistoryView();
//                        startActivity();
//                    Toast.makeText(SearchShopActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                });
                mFlowLayout.addView(tv);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 11) {
                    String keywords = data.getStringExtra("keywords");
                    input.setText(keywords);
                    input.setSelection(keywords.length());
//                    initHistoryView();
                }
                break;
        }
    }

    private void initHistoryView() {

        btn_search = (TextView) findViewById(R.id.txt_search);
        btn_search.setOnClickListener(this);

        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);
        mType = getIntent().getStringExtra(EXTRA_KEY_TYPE);
        String keyword = getIntent().getStringExtra(EXTRA_KEY_KEYWORD);
        if (!TextUtils.isEmpty(keyword)) {
            input.setText(keyword);
        }
        mHistoryKeywords = new ArrayList<String>();

        initSearchHistory();
    }

    /**
     * 初始化搜索历史记录
     */
    public void initSearchHistory() {
        mSearchHistoryLl = (LinearLayout) findViewById(R.id.search_history_ll);
        MyListView listView = (MyListView) findViewById(R.id.search_history_lv);
        findViewById(R.id.tv_clear_history).setOnClickListener(this);
        String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            for (Object o : history.split(",")) {
                list.add((String) o);
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords.size() > 0) {
            mSearchHistoryLl.setVisibility(View.VISIBLE);
        } else {
            mSearchHistoryLl.setVisibility(View.GONE);
        }
        histroyAdapter = new SearchHistoryAdapter(mHistoryKeywords, this);
        listView.setAdapter(histroyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.setClass(SearchShopActivity.this, SearchResultActivity.class);
                intent.putExtra("keystr", mHistoryKeywords.get(i));
                startActivityForResult(intent, 1);
                save(mHistoryKeywords.get(i));
//                input.setText(mHistoryKeywords.get(i));
//                mSearchHistoryLl.setVisibility(View.GONE);
            }
        });

        histroyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                closeInputMethod();
                finish();
                break;
            case R.id.txt_search:
                if (input.getText().toString().trim().equals("")) {
                    XToast.makeText(this, "请输入关键字", XToast.LENGTH_SHORT).show();

                } else {
                    intent.setClass(SearchShopActivity.this, SearchResultActivity.class);
                    intent.putExtra("keystr", input.getText().toString().trim());
                    startActivity(intent);
                    save(input.getText().toString());
//                    initHistoryView();
                }
                break;
            case R.id.tv_clear_history:
                cleanHistory();
                break;
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
        Log.e("tag", "" + oldText);
        Log.e("Tag", "" + text);
        Log.e("Tag", "" + oldText.contains(text));
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
            histroyAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 清除历史纪录
     */
    public void cleanHistory() {
        mPref = getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        mHistoryKeywords.clear();
        histroyAdapter.notifyDataSetChanged();
        mSearchHistoryLl.setVisibility(View.GONE);
//        XToast.makeText(this, "清楚搜索历史成功", Toast.LENGTH_LONG).show();
    }

    /**
     * 关闭软键盘
     */
    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

            imm.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
