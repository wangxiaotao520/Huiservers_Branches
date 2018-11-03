package com.huacheng.huiservers.shop;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.DataBean;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.shop.bean.SubmitOrderBean;
import com.huacheng.huiservers.shop.bean.XGBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyGridview;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.huacheng.huiservers.R.id.check_box;

public class ShopCartActivityTwo extends BaseUI implements OnClickListener {
    ShopProtocol protocol = new ShopProtocol();
    ShopDetailBean listlBean = new ShopDetailBean();
    private static final int INITIALIZE = 0;
    private LinearLayout lin_left, lin_tuijian, bottom_bar;
    private MyListView mListView;// 列表
    private MyGridview grid_view;// 列表
    SharePrefrenceUtil prefrenceUtil;
    private ListAdapter mListAdapter;// adapter
    public static ShopCartActivityTwo instant;
    private List<DataBean> mListData = new ArrayList<DataBean>();// 数据
    private boolean isBatchModel;// 是否可删除模式
    private RelativeLayout mBottonLayout, rel_no_data;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView title_name;

    private TextView mPriceAll; // 商品总价

    private TextView mSelectNum; // 选中数量

    private TextView mDelete; // 删除 结算

    float totalPrice = (float) 0; //商品总价
    //private String totalPrice = "0.00"; // 商品总价
    /**
     * 批量模式下，用来记录当前选中状态
     */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
 //       SetTransStatus.GetStatus(this);
        prefrenceUtil = new SharePrefrenceUtil(this);
        MyCookieStore.is_notify = 0;
        initViews();//初始化id
        getShopList();//数据请求
        //getAddress();//获取收货地址
        initListener();//事件监听
        loadData();
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final CheckBox check_box = (CheckBox) view.findViewById(R.id.check_box);

                new CommomDialog(ShopCartActivityTwo.this, R.style.my_dialog_DimEnabled, "确定要删除吗？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            if (check_box.isChecked()) {
                                if (mSelectState.size() == 1) {
                                    mSelectState.clear();
                                }
                                DataBean bean = mListData.get(i);
                                totalPrice -= bean.getNumber() * bean.getPrice();
                                setFloat();
                                mPriceAll.setText("¥" + totalPrice + "");
                            }
                            System.out.println("mListData.get(position).getId()---" +
                                    mListData.get(i).getId());
                            System.out.println("position--" + i);
                            getdatas(mListData.get(i).getId(), i);
                            MyCookieStore.shopcar_notify = 1;

                            dialog.dismiss();
                        }
                    }
                }).show();//.setTitle("提示")
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyCookieStore.is_notify == 1) {
            if (mListData != null) {
                System.out.println("mListData.size()******" + mListData.size());
                if (mListData.size() >= 1) {
                    mListData.clear();
                }
            }

            getSelectedIds().clear();
            getShopList();
            mCheckAll.setChecked(false);
            totalPrice = (float) 0;
            mSelectState.clear();
            refreshListView();
            mPriceAll.setText("¥" + 0.00 + "");
            mSelectNum.setText("已选" + 0 + "件商品");
            mListView.setAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
            initListener();//事件监听
            loadData();
        }
    }
    private void getShopList() {//获取购物车列表、
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.shopping_cart, params, ShopCartActivityTwo.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                listlBean = protocol.getShopList(json);
                mListData = listlBean.getList();
                //refreshListView();
                if (mListData != null) {
                    mListAdapter = new ListAdapter(mListData);
                    mListView.setAdapter(mListAdapter);
                    mListView.setOnItemClickListener(mListAdapter);
                    rel_no_data.setVisibility(View.GONE);
                    bottom_bar.setVisibility(View.VISIBLE);

                } else {
                    bottom_bar.setVisibility(View.GONE);
                    refreshListView();

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    //删除全部商品
    int deleteId;

    private void doDelete(List<Integer> ids) {
        for (int i = 0; i < mListData.size(); i++) {
            long dataId = mListData.get(i).getId();
            for (int j = 0; j < ids.size(); j++) {
                deleteId = ids.get(j);
                System.out.println("deleteId===" + deleteId);
                if (dataId == deleteId) {
                    mListData.remove(i);
                    i--;
                    ids.remove(j);
                    j--;
                }
            }
        }
        refreshListView();
        mSelectState.clear();
        totalPrice = (float) 0;
        mSelectNum.setText("已选" + 0 + "件商品");
        mPriceAll.setText("¥" + 0.00 + "");
        mCheckAll.setChecked(false);

    }

    //全部选中 删除某一件商品
    private void doDelete1(String id) {
        totalPrice = (float) 0;
        List<SubmitOrderBean> detletpro = new ArrayList<SubmitOrderBean>();
        if (getSelectedIds().size() == 0) {
            mPriceAll.setText("¥" + 0.00 + "");
            mCheckAll.setChecked(false);

        }
        if (mListData.size() == 0) {
            rel_no_data.setVisibility(View.VISIBLE);
            bottom_bar.setVisibility(View.GONE);
        }
        System.out.println("pro=sss======" + mListData.size() + "--" + getSelectedIds().size());
        for (int i = 0; i < mListData.size(); i++) {
            for (int j = 0; j < getSelectedIds().size(); j++) {
                if (mListData.get(i).getId() == getSelectedIds().get(j)) {
                    SubmitOrderBean info = new SubmitOrderBean();
                    info.setPrice(String.valueOf(mListData.get(i).getPrice()));
                    info.setNumber(String.valueOf(mListData.get(i).getNumber()));
                    detletpro.add(info);
                }
            }
        }
        for (int i = 0; i < detletpro.size(); i++) {
            totalPrice += Float.parseFloat(detletpro.get(i).getNumber()) * Float.parseFloat(detletpro.get(i).getPrice());
            refreshListView();
            mSelectNum.setText("已选" + detletpro.size() + "件商品");
            setFloat();
            mPriceAll.setText("¥" + totalPrice + "");
        }
    }

    private final List<Integer> getSelectedIds() {
        ArrayList<Integer> selectedIds = new ArrayList<Integer>();
        for (int index = 0; index < mSelectState.size(); index++) {
            if (mSelectState.valueAt(index)) {
                selectedIds.add(mSelectState.keyAt(index));
            }
        }
        return selectedIds;
    }

    public void initViews() {
        //初始化ID
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        bottom_bar = (LinearLayout) findViewById(R.id.bottom_bar);
        lin_left.setOnClickListener(this);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("购物车");
        lin_tuijian = (LinearLayout) findViewById(R.id.lin_tuijian);
        mBottonLayout = (RelativeLayout) findViewById(R.id.cart_rl_allprie_total);
        rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
        mCheckAll = (CheckBox) findViewById(check_box);
        mPriceAll = (TextView) findViewById(R.id.tv_cart_total);
        mSelectNum = (TextView) findViewById(R.id.tv_cart_select_num);
        mDelete = (TextView) findViewById(R.id.tv_cart_buy_or_del);
        mListView = (MyListView) findViewById(R.id.listview);
        grid_view = (MyGridview) findViewById(R.id.grid_view);
        //mListView.setSelector(R.drawable.list_selector);
        mDelete.setOnClickListener(this);
        mCheckAll.setOnClickListener(ShopCartActivityTwo.this);
    }

    private void initListener() {


    }

    private void loadData() {
        new LoadDataTask().execute(new Params(INITIALIZE));
    }

    private void refreshListView() {
        if (mListData != null) {
            mListAdapter = new ListAdapter(mListData);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);
            rel_no_data.setVisibility(View.GONE);
        } else {
            totalPrice = (float) 0;
            mSelectState.clear();
            //refreshListView();
            mPriceAll.setText("¥" + 0.00 + "");
            mSelectNum.setText("已选" + 0 + "件商品");
            rel_no_data.setVisibility(View.VISIBLE);
            mCheckAll.setClickable(false);
            //mListAdapter.notifyDataSetChanged();
        }

    }

    private void getdatas(final int id, final int arg0) {//删除购物车商品
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", String.valueOf(id));
        System.out.println("id====" + id);
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(info.del_shopping_cart, params, ShopCartActivityTwo.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol.setShop(json);
                if (str.equals("1")) {
                    mListData.remove(arg0);
                    mListAdapter.notifyDataSetChanged();
                    XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                    //	getShopList();
                    mSelectState.delete(id);
                    mSelectNum.setText("已选" + mSelectState.size() + "件商品");
                    //setFloat();
                    //mPriceAll.setText("¥" + totalPrice +"");
                    doDelete1(String.valueOf(id));
                } else {
                    XToast.makeText(context, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    class Params {
        int op;

        public Params(int op) {
            this.op = op;
        }

    }

    class Result {
        int op;
        List<DataBean> list;
    }

    private class LoadDataTask extends AsyncTask<Params, Void, Result> {
        @Override
        protected Result doInBackground(Params... params) {
            Params p = params[0];
            Result result = new Result();
            result.op = p.op;
            try {// 模拟耗时
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.list = mListData;
            return result;
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (result.op == INITIALIZE) {
                mListData = result.list;
            } else {
                mListData.addAll(result.list);
                XToast.makeText(ShopCartActivityTwo.this, "添加成功！", XToast.LENGTH_SHORT).show();
            }
            refreshListView();
        }
    }

    private class ListAdapter extends BaseAdapter implements OnItemClickListener {
        List<DataBean> mListData;
        ViewHolder holder = null;

        public ListAdapter(List<DataBean> mListData) {
            this.mListData = mListData;
        }

        @Override
        public int getCount() {
            if (mListData != null) {
                return mListData.size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(ShopCartActivityTwo.this).inflate(R.layout.cart_list_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            DataBean data = mListData.get(position);
            bindListItem(holder, data);

            holder.add.setOnClickListener(new OnClickListener() {//商品数添加

                @Override
                public void onClick(View v) {
                 /*   if (TextUtils.isEmpty(mListData.get(position).getInventory()) || 0 >= Integer.valueOf(mListData.get(position).getInventory())) {
                        XToast.makeText(ShopCartActivityTwo.this, "此商品已超出库存数量", XToast.LENGTH_SHORT).show();
                    } else {*/
                    if (Integer.valueOf(mListData.get(position).getInventory()) < Integer.valueOf(mListData.get(position).getNumber() + 1)) {
                        XToast.makeText(ShopCartActivityTwo.this, "此商品已超出库存数量", XToast.LENGTH_SHORT).show();
                    } else {

                        Url_info info = new Url_info(ShopCartActivityTwo.this);
                        HttpUtils utils = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("p_id", mListData.get(position).getP_id());
                        params.addBodyParameter("tagid", mListData.get(position).getTagid());
                        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
                            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
                            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
                        }
                        utils.configCookieStore(MyCookieStore.cookieStore);
                        utils.send(HttpRequest.HttpMethod.POST, info.shop_limit, params,
                                new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {

                                        XGBean xgBean = protocol.getAGNum(responseInfo.result);
                                        System.out.println("responseInfo.result********" + responseInfo.result);
                                        if (xgBean.getLimit().equals("0")) {
                                            int _id = (int) mListData.get(position).getId();

                                            boolean selected = mSelectState.get(_id, false);

                                            mListData.get(position).setNumber(mListData.get(position).getNumber() + 1);

                                            notifyDataSetChanged();

                                            if (selected) {
                                                totalPrice += mListData.get(position).getPrice();
                                                setFloat();
                                                mPriceAll.setText("¥" + totalPrice + "");
                                            }
                                        } else {
                                            int intAll = Integer.valueOf(mListData.get(position).getNumber()) + Integer.valueOf(xgBean.getOrder_num());
                                            System.out.println("intAll#############" + intAll);
                                            System.out.println("intAll#############" + xgBean.getLimit());
                                            if (intAll >= Integer.valueOf(xgBean.getLimit())) {
                                                XToast.makeText(ShopCartActivityTwo.this, "此商品已超出限购数量", XToast.LENGTH_SHORT).show();
                                            } else {
                                                int all = Integer.valueOf(xgBean.getLimit()) - intAll;
                                                if (Integer.valueOf(xgBean.getLimit()) < all) {
                                                    XToast.makeText(ShopCartActivityTwo.this, "此商品已超出限购数量", XToast.LENGTH_SHORT).show();
                                                } else {
                                                    int _id = (int) mListData.get(position).getId();

                                                    boolean selected = mSelectState.get(_id, false);

                                                    mListData.get(position).setNumber(mListData.get(position).getNumber() + 1);

                                                    notifyDataSetChanged();

                                                    if (selected) {
                                                        totalPrice += mListData.get(position).getPrice();
                                                        setFloat();
                                                        mPriceAll.setText("¥" + totalPrice + "");
                                                    }
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                        UIUtils.showToastSafe("网络异常，请检查网络设置");
                                    }
                                });
                    }

                }
            });
            holder.red.setOnClickListener(new OnClickListener() {//商品数减少
                @Override
                public void onClick(View v) {
                    if (mListData.get(position).getNumber() == 1)
                        return;

                    int _id = (int) mListData.get(position).getId();

                    boolean selected = mSelectState.get(_id, false);
                    mListData.get(position).setNumber(mListData.get(position).getNumber() - 1);
                    notifyDataSetChanged();

                    if (selected) {
                        totalPrice -= mListData.get(position).getPrice();
                        //保留两位小数点
                        setFloat();
                        mPriceAll.setText("¥" + totalPrice + "");

                    }

                }
            });
            return view;
        }

        private void bindListItem(ViewHolder holder, DataBean data) {
            /*if (data.getExist_hours().equals("2")) {
                holder.txt_paisong.setVisibility(View.VISIBLE);
            } else {
                holder.txt_paisong.setVisibility(View.INVISIBLE);
            }*/
            holder.content.setText(data.getTitle());
            holder.price.setText("¥" + data.getPrice());
            holder.carNum.setText(data.getNumber() + "");
            BitmapUtils utils = new BitmapUtils(ShopCartActivityTwo.this);
            utils.display(holder.image, MyCookieStore.URL + data.getTitle_img());
            int _id = data.getId();
            boolean selected = mSelectState.get(_id, false);
            holder.checkBox.setChecked(selected);
            holder.txt_type.setText(data.getTagname());
            //holder.tv_yuan_price.setText(data.getOriginal());
            //holder.tv_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DataBean bean = mListData.get(position);

            ViewHolder holder = (ViewHolder) view.getTag();
            int _id = (int) bean.getId();

            boolean selected = !mSelectState.get(_id, false);
            holder.checkBox.toggle();
            if (selected) {
                mSelectState.put(_id, true);
                totalPrice += bean.getNumber() * bean.getPrice();
                setFloat();
            } else {
                mSelectState.delete(_id);
                totalPrice -= bean.getNumber() * bean.getPrice();
                setFloat();
            }

            mSelectNum.setText("已选" + mSelectState.size() + "件商品");
            mPriceAll.setText("¥" + totalPrice + "");
            if (mSelectState.size() == mListData.size()) {
                mCheckAll.setChecked(true);
            } else {
                mCheckAll.setChecked(false);
            }
        }
    }

    class ViewHolder {
        CheckBox checkBox;
        ImageView image, img_detel;
        TextView content;
        TextView carNum;
        TextView price;
        TextView add;
        TextView red;
        TextView txt_type;
        TextView txt_paisong;
        TextView tv_yuan_price;
        LinearLayout lin_cart;

        public ViewHolder(View view) {
            checkBox = (CheckBox) view.findViewById(check_box);
            image = (ImageView) view.findViewById(R.id.iv_adapter_list_pic);
            content = (TextView) view.findViewById(R.id.tv_intro);
            carNum = (TextView) view.findViewById(R.id.tv_num);
            price = (TextView) view.findViewById(R.id.tv_price);
            add = (TextView) view.findViewById(R.id.tv_add);
            red = (TextView) view.findViewById(R.id.tv_reduce);
            txt_paisong = (TextView) view.findViewById(R.id.txt_paisongtime);
            txt_type = (TextView) view.findViewById(R.id.txt_type);
            //simg_detel = (ImageView) view.findViewById(R.id.img_detel);
            tv_yuan_price = (TextView) view.findViewById(R.id.tv_yuan_price);
            lin_cart = (LinearLayout) view.findViewById(R.id.lin_cart);


        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case check_box://选中未选中状态的变化
                if (mCheckAll.isChecked()) {
                    totalPrice = (float) 0;
                    if (mListData != null) {
                        mSelectState.clear();
                        int size = mListData.size();
                        if (size == 0) {
                            return;
                        }
                        for (int i = 0; i < size; i++) {
                            int _id = (int) mListData.get(i).getId();
                            mSelectState.put(_id, true);
                            totalPrice += mListData.get(i).getNumber() * mListData.get(i).getPrice();
                            setFloat();
                        }

                        mPriceAll.setText("¥" + totalPrice + "");
                        mSelectNum.setText("已选" + mSelectState.size() + "件商品");
                        refreshListView();
                    }
                } else {
                    if (mListAdapter != null) {
                        totalPrice = (float) 0;
                        mSelectState.clear();
                        refreshListView();
                        mPriceAll.setText("¥" + 0.00 + "");
                        mSelectNum.setText("已选" + 0 + "件商品");

                    }
                }


                break;

            case R.id.tv_cart_buy_or_del://结算
                if (getSelectedIds().size() == 0) {
                    XToast.makeText(this, "无结算商品", XToast.LENGTH_SHORT).show();
                } else {
                    getxiadan();
                }
                break;
            case R.id.lin_left://返回
                if (mListData != null) {
                    saveShopNum();
                }
                finish();
                break;
           /* case R.id.right://首页图标   返回首页销毁所有活动
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                BaseUI.finishAll();
                break;*/
            default:
                break;
        }
    }

    String str = "";

    private void saveShopNum() {
        Url_info info = new Url_info(this);
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < mListData.size(); i++) {
            String id = mListData.get(i).getId() + "";
            String number = mListData.get(i).getNumber() + "";
            strBuilder.append(id + "." + number + ",");
        }
        int lastIndex = strBuilder.lastIndexOf(",");
        if (lastIndex != -1) {
            str = strBuilder.substring(0, lastIndex) + strBuilder.substring(lastIndex + 1, strBuilder.length());
//            Log.e("ShopCateStr", "===" + str);
        }
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cart_id_str", str);
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.send(HttpRequest.HttpMethod.POST, info.edit_shopping_cart, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                str = protocol.setShop(responseInfo.result);
                if (str.equals("1")) {
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


    }

    List<SubmitOrderBean> pro;

    public void getxiadan() {//提交结算商品
        pro = new ArrayList<SubmitOrderBean>();
        System.out.println("pro=sss======" + mListData.size() + "--" + getSelectedIds().size());
        for (int i = 0; i < mListData.size(); i++) {
            for (int j = 0; j < getSelectedIds().size(); j++) {
                if (mListData.get(i).getId() == getSelectedIds().get(j)) {
                    SubmitOrderBean info = new SubmitOrderBean();
                    info.setTagid(mListData.get(i).getTagid());
                    info.setP_id(mListData.get(i).getP_id());
                    info.setP_title(mListData.get(i).getTitle());
                    info.setP_title_img(mListData.get(i).getTitle_img());
                    info.setPrice(String.valueOf(mListData.get(i).getPrice()));
                    info.setNumber(String.valueOf(mListData.get(i).getNumber()));
                    // str = mListData.get(i).getExist_hours();
                    System.out.println("tagid=============" + mListData.get(i).getTagid());
                    pro.add(info);
                }
            }
        }
        getex();//判断商品是否在派送范围时间

    }

    private void getex() {//取消判断商品是否在派送范围时间

        Intent intent = new Intent(ShopCartActivityTwo.this, ConfirmOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("all", totalPrice + "");
        bundle.putSerializable("pro", (Serializable) pro);
        intent.putExtras(bundle);
        startActivity(intent);
        System.out.println("pro=======" + pro.size());
    }

    private void setFloat() {//保留两位小数点
        BigDecimal b = new BigDecimal(totalPrice);
        totalPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mListData != null) {
                saveShopNum();
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
