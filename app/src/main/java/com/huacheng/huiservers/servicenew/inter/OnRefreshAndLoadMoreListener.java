package com.huacheng.huiservers.servicenew.inter;

import android.support.annotation.NonNull;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Description:
 * created by wangxiaotao
 * 2018/9/4 0004 下午 4:23
 */
public interface OnRefreshAndLoadMoreListener {

    void onRefresh(@NonNull RefreshLayout refreshLayout);

    void onLoadMore(@NonNull RefreshLayout refreshLayout);

    boolean canLoadMore();
}
