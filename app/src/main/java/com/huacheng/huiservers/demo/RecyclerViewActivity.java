package com.huacheng.huiservers.demo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelCommentItem;
import com.huacheng.libraryservice.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * created by wangxiaotao
 * 2018/9/18 0018 下午 4:19
 */
public class RecyclerViewActivity extends BaseActivity{

    private RecyclerView mRecyclerView;
    private List<ModelCommentItem> mDatas=new ArrayList<>();
    CommonAdapter adapter;
    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rlv_service);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter<ModelCommentItem>(this,R.layout.layout_comment_item,mDatas) {
            @Override
            protected void convert(ViewHolder holder, ModelCommentItem modelCommentItem, final int position) {
                holder.<TextView>getView(R.id.tv_user_name).setText("哆唻A梦");
                holder.<SimpleDraweeView>getView(R.id.sdv_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RecyclerViewActivity.this, "pos = " + position+"头像被点击了", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        mRecyclerView.setAdapter(adapter);
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
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                Toast.makeText(RecyclerViewActivity.this, "pos = " + position, Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position)
            {
                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frgment_service_new;
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
