package com.huacheng.huiservers.center;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.ShopOrderBeanType;
import com.huacheng.huiservers.fragment.listener.EndlessRecyclerOnScrollListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.NewPingJiaActivity;
import com.huacheng.huiservers.shop.NewTuikuanActivity;
import com.huacheng.huiservers.shop.adapter.New_OrderImageAdapter;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.HorizontalListView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.huacheng.huiservers.R.id.radio3;


/**
 * 类：
 * 时间：2017/10/13 15:15
 * 功能描述:Huiservers
 */
public class New_Shop_OrderActivity extends BaseUI {
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.radio1)
    RadioButton mRadio1;
    @BindView(R.id.radio2)
    RadioButton mRadio2;
    @BindView(radio3)
    RadioButton mRadio3;
    @BindView(R.id.radio4)
    RadioButton mRadio4;
    @BindView(R.id.rg_colleague)
    RadioGroup mRgColleague;
    @BindView(R.id.v_head_line)
    View mVHeadLine;
    @BindView(R.id.title_name)
    TextView mTitleName;

    /*@BindView(R.id.list_order)
    Zks_FL_ListView mListOrder;*/
    //private String status = "0";
    private int page = 1;//当前页
    int totalPage = 0;//总页数
    private int count = 0;
    String status = "0";

    New_Shop_orderAdapter new_shop_orderAdapter;
    CenterProtocol protocol = new CenterProtocol();
    List<ShopOrderBeanType> beanTypes;
    List<ShopOrderBeanType> ALLbeanTypes = new ArrayList<ShopOrderBeanType>();
    ShopProtocol mShopProtocol = new ShopProtocol();

    @BindView(R.id.rel_no_data)
    RelativeLayout mRelNoData;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

//    Dialog waitDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_shop_order);
        ButterKnife.bind(this);
        //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));//
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mTitleName.setText("商城订单");
        mVHeadLine.setVisibility(View.GONE);
        mLinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String info_status = this.getIntent().getExtras().getString("status");
        showDialog(smallDialog);
        if (info_status.equals("2")) {
            mRadio3.setChecked(true);
            getdata("2");
        } else if (info_status.equals("1")) {
            mRadio2.setChecked(true);
            getdata("1");
        } else {
            getdata(status);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*lin_circle5.setFocusable(true);
                lin_circle5.requestFocus();
                lin_circle5.setFocusableInTouchMode(true);*/
                // 刷新数据

                /*ALLbeanTypes = new ArrayList<ShopOrderBeanType>();
                new_shop_orderAdapter = new New_Shop_orderAdapter(New_Shop_OrderActivity.this, ALLbeanTypes, status);
                recyclerview.setAdapter(new_shop_orderAdapter);*/
                page = 1;
                getdata(status);

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
        recyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                new_shop_orderAdapter.setLoadState(new_shop_orderAdapter.LOADING);
                if (page < totalPage) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    page++;
                                    getdata(status);
                                    new_shop_orderAdapter.setLoadState(new_shop_orderAdapter.LOADING_COMPLETE);

                                }
                            });
                        }
                    }, 500);

                } else {
                    // 显示加载到底的提示
                    new_shop_orderAdapter.setLoadState(new_shop_orderAdapter.LOADING_END);
                }

            }
        });
        rb_onclick();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("****");
        // MyCookieStore.WX_dialog=0;
        if (MyCookieStore.Sh_notify == 1 || MyCookieStore.SC_notify == 1) {//收货成功返回刷新
            System.out.println("****");
            beanTypes.clear();
            ALLbeanTypes.clear();
            showDialog(smallDialog);
            getdata(status);
        }
        if (MyCookieStore.WX_notify == 1) {
            mRadio3.setChecked(true);
            showDialog(smallDialog);
            getdata("2");
        }
    }

    private void rb_onclick() {
        mRgColleague.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                page = 1;
                if (mRadio1.isChecked()) {
                    status = "0";
                    getdata(status);//全部
                } else if (mRadio2.isChecked()) {
                    status = "1";
                    getdata(status);//待付款接口
                } else if (mRadio3.isChecked()) {
                    status = "2";
                    getdata(status);//待付款接口
                } else if (mRadio4.isChecked()) {
                    status = "3";
                    getdata(status);//待付款接口
                }
            }
        });

    }

    private void getdata(final String status) {//获取商品数据
        Url_info info = new Url_info(this);
        String url = info.shop_order_list + "status/" + status + "/p/" + page + "/";
        new HttpHelper(url, New_Shop_OrderActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                beanTypes = protocol.getXOrderList(json);
                if (beanTypes.size() != 0) {
                    mRelNoData.setVisibility(View.GONE);
                    recyclerview.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        ALLbeanTypes.clear();
                        new_shop_orderAdapter = new New_Shop_orderAdapter(New_Shop_OrderActivity.this, ALLbeanTypes, status);
                        recyclerview.setAdapter(new_shop_orderAdapter);
                    }
                    ALLbeanTypes.addAll(beanTypes);
                    totalPage = Integer.valueOf(ALLbeanTypes.get(0).getTotal_Pages());// 设置总页数
                    new_shop_orderAdapter.notifyDataSetChanged();
                } else {
                    recyclerview.setVisibility(View.GONE);
                    mRelNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    String jiekouName;
    List<String> list_info_id = new ArrayList<String>();
    StringBuffer sb;


    public class New_Shop_orderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ShopOrderBeanType> adbeanTypes;

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

        private Context mContext;
        String status;

        public New_Shop_orderAdapter(Context context, List<ShopOrderBeanType> ALLbeanTypes, String status) {
            this.mContext = context;
            this.adbeanTypes = ALLbeanTypes;
            this.status = status;
        }

        @Override
        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
            // 最后一个item设置为FooterView
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;

            }
        }

        //0417

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 通过判断显示类型，来创建不同的View
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.new_shop_order_item, parent, false);
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
                recyclerViewHolder.txt_order_num.setText("订单编号：" + adbeanTypes.get(position).getOrder_number());
                recyclerViewHolder.txt_num.setText("共" + adbeanTypes.get(position).getPro_num() + "件");
                recyclerViewHolder.txt_danprice.setText("¥" + adbeanTypes.get(position).getPrice_total());
                // order_id = ALLbeanTypes.get(position).getId();
                //order_num = ALLbeanTypes.get(position).getOrder_number();
                if (status.equals("0")) {
                    recyclerViewHolder.lin_all.setVisibility(View.GONE);
                } else if (status.equals("1")) {//待付款
                    recyclerViewHolder.txt_delete.setText("删除");
                    recyclerViewHolder.txt_type.setText("付款");
                } else if (status.equals("2")) {//待收货
                    recyclerViewHolder.txt_delete.setVisibility(View.GONE);
                    //recyclerViewHolder.txt_type.setText("收货");
                    recyclerViewHolder.lin_all.setVisibility(View.GONE);
                } else if (status.equals("3")) {//待评价  待退款
                    recyclerViewHolder.lin_all.setVisibility(View.GONE);
                    //  recyclerViewHolder.txt_delete.setText("删除");
                    recyclerViewHolder.txt_type.setVisibility(View.GONE);
                }

                recyclerViewHolder.txt_delete.setOnClickListener(new View.OnClickListener() {//删除接口
                    @Override
                    public void onClick(View view) {
                        new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认要删除吗？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    list_info_id.clear();
                                    for (int j = 0; j < adbeanTypes.get(position).getImg().size(); j++) {
                                        String info_id = adbeanTypes.get(position).getImg().get(j).getInfo_id();
                                        list_info_id.add(info_id);
                                    }
                                    sb = new StringBuffer();
                                    for (int i = 0; i < list_info_id.size(); i++) {
                                        if (i == 0) {
                                            sb.append(String.valueOf(list_info_id.get(i)));
                                        } else {
                                            sb.append("," + String.valueOf(list_info_id.get(i)));
                                        }
                                    }
                                    System.out.println("删除888sb-------" + sb.toString());
                                    getdatas("1", sb.toString(), position);
                                    dialog.dismiss();
                                } else {
                                    list_info_id.clear();
                                    dialog.dismiss();
                                }
                            }
                        }).show();//.setTitle("提示")

                    }
                });

                recyclerViewHolder.txt_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (status.equals("1")) {//待付款
                            Intent intent = new Intent(New_Shop_OrderActivity.this, New_Shop_Order_DetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", adbeanTypes.get(position).getId());
                            bundle.putString("status", status);
                            bundle.putString("order_num", adbeanTypes.get(position).getOrder_number());
                            bundle.putString("item_id", position + "");
                            System.out.println("item_id$$$$$$$$&" + position + "");
                            System.out.println("getOrder_numbe$$$$$$$$&" + adbeanTypes.get(position).getOrder_number() + "");
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 1);
                            //mContext.startActivity(intent);

                        } else if (status.equals("2")) {//待收货

                            new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "是否确认收货？", new CommomDialog.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm) {
                                        list_info_id.clear();
                                        for (int j = 0; j < adbeanTypes.get(position).getImg().size(); j++) {
                                            String info_id = adbeanTypes.get(position).getImg().get(j).getInfo_id();
                                            list_info_id.add(info_id);
                                        }
                                        sb = new StringBuffer();
                                        for (int i = 0; i < list_info_id.size(); i++) {
                                            if (i == 0) {
                                                sb.append(String.valueOf(list_info_id.get(i)));
                                            } else {
                                                sb.append("," + String.valueOf(list_info_id.get(i)));
                                            }
                                        }
                                        System.out.println("收货888sb-------" + sb.toString());
                                        getdatas("2", sb.toString(), position);
                                        dialog.dismiss();
                                    } else {
                                        list_info_id.clear();
                                        dialog.dismiss();
                                    }
                                }
                            }).show();//.setTitle("提示")

                        } else if (status.equals("3")) {
                            Intent intent = new Intent(mContext, NewPingJiaActivity.class);
                            mContext.startActivity(intent);
                        } else if (status.equals("4")) {
                            Intent intent = new Intent(mContext, NewTuikuanActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });

                recyclerViewHolder.lin_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, New_Shop_Order_DetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", adbeanTypes.get(position).getId());
                        bundle.putString("status", status);
                        bundle.putString("order_num", adbeanTypes.get(position).getOrder_number());
                        bundle.putString("item_id", position + "");
                        System.out.println("item_id$$$$$$$$&" + position + "");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                    }
                });
                if (ALLbeanTypes.get(position).getImg() != null) {
                    //这里图片的显示是个适配器   控制图片数量
                    New_OrderImageAdapter imageAdapter = new New_OrderImageAdapter(mContext, ALLbeanTypes.get(position).getImg());
                    recyclerViewHolder.hor_scroll.setAdapter(imageAdapter);

                }


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

        @Override
        public int getItemCount() {
            return adbeanTypes.size() + 1;
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

        private void getdatas(final String type, String id, final int arg0) {
            Url_info info = new Url_info(New_Shop_OrderActivity.this);
            //删除订单中商品与确认收货接口   type为1  是删除 订单 2是确认收货
            RequestParams params = new RequestParams();
            if (type.equals("1")) {
                jiekouName = info.del_order;
            } else if (type.equals("2")) {
                //jiekouName = "personal/order_confirm/";
                jiekouName = info.shop_order_accept;
            }
            params.addBodyParameter("id", id);
            System.out.println("order_id====" + id);
            showDialog(smallDialog);
            new HttpHelper(jiekouName, params, mContext) {

                @Override
                protected void setData(String json) {
                    hideDialog(smallDialog);
                    String shopstr = mShopProtocol.setShop(json);
                    if (shopstr.equals("1")) {
                        if (type.equals("1")) {
                            XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                        } else {
                            XToast.makeText(context, "确认收货成功", XToast.LENGTH_SHORT).show();
                        }
                        list_info_id.clear();
                        adbeanTypes.remove(arg0);
                        new_shop_orderAdapter.notifyDataSetChanged();
                    } else {
                        XToast.makeText(context, shopstr, XToast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    hideDialog(smallDialog);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                }
            };
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder {

            HorizontalListView hor_scroll;
            TextView txt_delete;
            TextView txt_type, txt_order_num, txt_num, txt_danprice;
            LinearLayout lin_info, lin_all;

            RecyclerViewHolder(View convertView) {
                super(convertView);
                hor_scroll = (HorizontalListView) convertView.findViewById(R.id.hor_scroll);
                lin_info = (LinearLayout) convertView.findViewById(R.id.lin_info);
                lin_all = (LinearLayout) convertView.findViewById(R.id.lin_all);
                txt_delete = (TextView) convertView.findViewById(R.id.txt_delete);
                txt_order_num = (TextView) convertView.findViewById(R.id.txt_order_num);
                txt_num = (TextView) convertView.findViewById(R.id.txt_num);
                txt_danprice = (TextView) convertView.findViewById(R.id.txt_danprice);
                txt_type = (TextView) convertView.findViewById(R.id.txt_type);

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

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub
        if (data == null) { //判断数据是否为空，就可以解决这个问题
            return;
        } else {
            String item_detete_id = null;
            if (!TextUtils.isEmpty(data.getExtras().getString("item_detete_id"))) {
                item_detete_id = data.getExtras().getString("item_detete_id");
            }
            switch (arg1) {
                case 100:

                    break;
                case 444:
                    mRadio4.setChecked(true);
                    break;
                case 55555:
                    getdata(status);

                    break;
                case 2:///除待付款以外的其他
                    if (arg0 == 1 && arg1 == 2) {
                        ALLbeanTypes.remove(Integer.parseInt(item_detete_id));
                        new_shop_orderAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3://待付款
                    mRadio3.setChecked(true);
                    /*if (arg0 == 1 && arg1 == 3) {
                        adbeanTypes.remove(Integer.parseInt(item_detete_id));
                        new_shop_orderAdapter.notifyDataSetChanged();
                    }*/
                    break;

                default:
                    break;
            }
        }
        super.onActivityResult(arg0, arg1, data);

    }
}
