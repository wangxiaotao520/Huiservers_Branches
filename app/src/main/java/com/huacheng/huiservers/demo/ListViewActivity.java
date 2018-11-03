package com.huacheng.huiservers.demo;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelCommentItem;
import com.huacheng.libraryservice.base.BaseActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 封装的ListViewDemo
 * created by wangxiaotao
 * 2018/9/18 0018 下午 3:50
 */
public class ListViewActivity extends BaseActivity {

    private ListView mListView;
    private List<ModelCommentItem> mDatas=new ArrayList<>();
    CommonAdapter adapter;
    @Override
    protected void initView() {
        mListView = findViewById(R.id.list);
        adapter =new CommonAdapter<ModelCommentItem>(this,R.layout.layout_comment_item,mDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, ModelCommentItem item, final int position) {
                viewHolder.<TextView>getView(R.id.tv_user_name).setText("哆唻A梦");
                viewHolder.<SimpleDraweeView>getView(R.id.sdv_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ListViewActivity.this, "pos = " + position+"头像被点击了", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 15; i++) {
            mDatas.add(new ModelCommentItem());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListViewActivity.this, "pos = " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.house_keep_jilu;
    }

    @Override
    protected void initIntentData() {

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }
}
