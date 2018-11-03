package com.huacheng.huiservers.huodong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.ServiceParticipateActivity;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.libraryservice.http.ApiHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hwh on 2017/10/10.
 */

public class EducationListActivity extends BaseUI implements View.OnClickListener {
    private LinearLayout lin_left;
    private TextView title_name, txt_right, tv_name;
    private RelativeLayout rel_no_data;
    private MyListView list;
    private View v_head_line;
    ListAdapter adapter;
    List<BannerBean> bean;
    ShopProtocol protocol = new ShopProtocol();
    List<BannerBean> beans = new ArrayList<BannerBean>();
    private SharePrefrenceUtil sharePrefrenceUtil;
    private String URL;
    private int page = 1;//当前页
    int totalPage = 0;//总页数
    String cid, titleName;
    private String login_type;
    private SharedPreferences preferencesLogin;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_list_education1);
 //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        title_name = (TextView) findViewById(R.id.title_name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        list = (MyListView) findViewById(R.id.list);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
        v_head_line = (View) findViewById(R.id.v_head_line);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        txt_right = (TextView) findViewById(R.id.txt_right);
        txt_right.setVisibility(View.GONE);
        txt_right.setBackgroundResource(R.drawable.icon_center_fuwu);
        sharePrefrenceUtil = new SharePrefrenceUtil(this);

        Intent intent = getIntent();
        URL = intent.getStringExtra("url");
        String URLnew = intent.getStringExtra("urlnew");
        cid = URLnew.substring(URLnew.lastIndexOf("/") + 1, URLnew.length());
        titleName = intent.getStringExtra("name");
        if (!TextUtils.isEmpty(titleName)) {
            title_name.setText(titleName);
        }
        showDialog(smallDialog);
        getdata("1");
        lin_left.setOnClickListener(this);
        txt_right.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                page = 1;
                getdata("1");
                // 延时1s关闭下拉刷新
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 500);

            }
        });

        // 设置加载更多监听
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadState(adapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getdata(page + "");
                                    adapter.setLoadState(adapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 500);

                } else {
                    // 显示加载到底的提示
                    adapter.setLoadState(adapter.LOADING_END);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //清除角标（华为）
        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(this);
            MyReceiver.setBadgeOfHuaWei(this, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_right:
                preferencesLogin = this.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    SharedPreferences.Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();
                    startActivity(new Intent(this, LoginVerifyCode1Activity.class));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(EducationListActivity.this, ServiceParticipateActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("activityId", cid);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    private void getdata(String total_Page) {//列表

        String url;
        url = URL + "/m_id/" + sharePrefrenceUtil.getXiaoQuId() + "/p/" + total_Page;
        new HttpHelper(url, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getActivitys(json, "1");
                System.out.println("-=---" + beans);
                if (bean.size() != 0) {
                    rel_no_data.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        beans.clear();
                        adapter = new ListAdapter(beans, EducationListActivity.this);
                        mRecyclerView.setAdapter(adapter);
                    }
                    beans.addAll(bean);
                    totalPage = Integer.valueOf(beans.get(0).getTotal_Pages());// 设置总页数
                    title_name.setText(beans.get(0).getC_name());
                    adapter.notifyDataSetChanged();
                } else {
                    rel_no_data.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<BannerBean> mBeans;

        private Context mContext;

        // 普通布局
        private final int TYPE_ITEM = 1;
        // 脚布局
        private final int TYPE_FOOTER = 2;
        // 当前加载状态，默认为加载完成
        private int loadState = 2;
        // 正在加载
        public final int LOADING = 1;
        // 加载完成
        public final int LOADING_COMPLETE = 2;
        // 加载到底
        public final int LOADING_END = 3;

        public ListAdapter(List<BannerBean> beans, Context context) {
            this.mBeans = beans;
            this.mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            // 最后一个item设置为FooterView
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 通过判断显示类型，来创建不同的View
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_education_list, parent, false);
                return new RecyclerViewHolder(view);

            } else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_refresh_footer, parent, false);
                return new FootViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof RecyclerViewHolder) {
                RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
                recyclerViewHolder.tv_title.setText(mBeans.get(position).getTitle());
                if (!"".equals(mBeans.get(position).getPicture_size())) {
                    //获取图片的宽高--start
                    recyclerViewHolder.item_iv.getLayoutParams().width = ToolUtils.getScreenWidth(mContext) - 45;
                    Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) - 45) / (Double.valueOf(mBeans.get(position).getPicture_size()));
                    recyclerViewHolder.item_iv.getLayoutParams().height = (new Double(d)).intValue();
                    //获取图片的宽高--end
                }else {
                    recyclerViewHolder.item_iv.getLayoutParams().width = ToolUtils.getScreenWidth(mContext) - 45;
                    Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) - 45) / (2.5);
                    recyclerViewHolder.item_iv.getLayoutParams().height = (new Double(d)).intValue();
                }
                // holder.tv_content.setText(bean.get(position).getIntroduction());
                if (!"".equals(mBeans.get(position).getPicture())) {
                    Glide
                            .with(EducationListActivity.this)
                            .load(mBeans.get(position).getPicture())
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.icon_girdview)
                            .into(recyclerViewHolder.item_iv);
                }
                recyclerViewHolder.tv_canyu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(mBeans.get(position).getOutside_url())) {
                            Intent intent = new Intent(EducationListActivity.this, EducationActivity2.class);
                            intent.putExtra("cid", cid);
                            intent.putExtra("id", mBeans.get(position).getId());
                            intent.putExtra("name", titleName);
                            startActivity(intent);
                        } else {//URL不为空时外链
                            Jump jump = new Jump(mContext, mBeans.get(position).getOutside_url(), 333);
                        }
                    }
                });

            } else if (holder instanceof FootViewHolder) {
                FootViewHolder footViewHolder = (FootViewHolder) holder;
                switch (loadState) {
                    case LOADING: // 正在加载
                        footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                        footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                        footViewHolder.llEnd.setVisibility(View.GONE);
                        break;

                    case LOADING_COMPLETE: // 加载完成
                        footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                        footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                        footViewHolder.llEnd.setVisibility(View.GONE);
                        break;

                    case LOADING_END: // 加载到底
                        footViewHolder.pbLoading.setVisibility(View.GONE);
                        footViewHolder.tvLoading.setVisibility(View.GONE);
                        footViewHolder.llEnd.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
            }
        }

        protected void imageBrower(int position, ArrayList<String> urls2) {
            Intent intent = new Intent(mContext, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            mContext.startActivity(intent);
        }

        @Override
        public int getItemCount() {
            return mBeans.size() + 1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                        return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        private class RecyclerViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title, tv_content, tv_describe, tv_enroll_end;
            ImageView item_iv, iv_status;
            LinearLayout tv_canyu;

            RecyclerViewHolder(View itemView) {
                super(itemView);
                tv_canyu = (LinearLayout) itemView.findViewById(R.id.tv_canyu);
                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
                item_iv = (ImageView) itemView.findViewById(R.id.item_iv);

            }
        }

        private class FootViewHolder extends RecyclerView.ViewHolder {

            ProgressBar pbLoading;
            TextView tvLoading;
            LinearLayout llEnd;

            FootViewHolder(View itemView) {
                super(itemView);
                pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
                tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
                llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
            }
        }

        /**
         * 设置上拉加载状态
         *
         * @param loadState 0.正在加载 1.加载完成 2.加载到底
         */
        public void setLoadState(int loadState) {
            this.loadState = loadState;
            notifyDataSetChanged();
        }
    }

}
