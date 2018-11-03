package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.ShopOrderListActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.ConfirmOrderActivity;
import com.huacheng.huiservers.shop.ShopCartActivityTwo;
import com.huacheng.huiservers.shop.adapter.AddShopAdapter;
import com.huacheng.huiservers.shop.bean.ShopDetailBean;
import com.huacheng.huiservers.shop.bean.SubmitOrderBean;
import com.huacheng.huiservers.shop.bean.XGBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.LoadingDialog;
import com.huacheng.huiservers.widget.FlowTag.FlowTagLayout;
import com.huacheng.huiservers.widget.FlowTag.OnTagSelectListener;
import com.huacheng.huiservers.widget.FlowTag.TagAdapter;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddShopDialog extends Dialog implements OnClickListener {
    private FlowTagLayout mSizeFlowTagLayout;
    private TagAdapter<String> mSizeTagAdapter;
    private LinearLayout back;
    private Context context;
    private String id, morenPrice, tagname, tagid, unit, invenyory, strtag;
    private ImageView img_fu_jian, img_zheng_jia, img_title;
    private TextView txt_btn, txt_price, txt_yuan_price;
    private AddShopAdapter addShopAdapter;
    private LoadingDialog dialog;
    ShopProtocol protocol = new ShopProtocol();
    BitmapUtils bitmapUtils;
    private Boolean isbool = true;
    EditText edit_num;
    TextView txt_inv, txt_title;
    ShopDetailBean detailBean = new ShopDetailBean();
    TextView txt_shop_num;
    SharePrefrenceUtil prefrenceUtil;
    ShopDetailBean cartnum = new ShopDetailBean();
    PriorityListener listener;

    public interface OnCustomDialogListener {
        public void back(String name);
    }

    List<ShopDetailBean> beans = new ArrayList<ShopDetailBean>();
    XGBean xgBean = new XGBean();
    XGBean CxgBean;

    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        public void refreshPriorityUI(String string);
    }

    private Dialog WaitDialog;

    public AddShopDialog(Context context, String id, TextView txt_shop_num,
                         List<ShopDetailBean> beans,
                         ShopDetailBean detailBean, String strtag, PriorityListener listener) {
        super(context, R.style.Dialog_down);
        this.context = context;
        this.id = id;
        //this.CxgBean = CxgBean;
        bitmapUtils = new BitmapUtils(context);
        this.txt_shop_num = txt_shop_num;
        this.beans = beans;
        this.detailBean = detailBean;
        this.strtag = strtag;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_shop);
        prefrenceUtil = new SharePrefrenceUtil(context);
        back = (LinearLayout) findViewById(R.id.back);//取消
        txt_title = (TextView) findViewById(R.id.txt_title);//名称
        txt_inv = (TextView) findViewById(R.id.txt_inv);//库存
        img_title = (ImageView) findViewById(R.id.img_title);//头图
        img_fu_jian = (ImageView) findViewById(R.id.img_fu_jian);//减号
        img_zheng_jia = (ImageView) findViewById(R.id.img_zheng_jia);//加号
        edit_num = (EditText) findViewById(R.id.edit_num);//选择数量
        edit_num.setOnClickListener(this);
        edit_num.setEnabled(false);
        /*edit_num.setFocusable(false);//让EditText失去焦点，然后获取点击事件
        edit_num.setOnClickListener(this);*/
        txt_btn = (TextView) findViewById(R.id.txt_btn);//确定
        txt_price = (TextView) findViewById(R.id.txt_price);//价格
        txt_yuan_price = (TextView) findViewById(R.id.txt_yuan_price);
        txt_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
        back.setOnClickListener(this);
        txt_btn.setOnClickListener(this);
        img_fu_jian.setOnClickListener(this);
        img_zheng_jia.setOnClickListener(this);

        //尺寸
        mSizeFlowTagLayout = (FlowTagLayout) findViewById(R.id.size_flow_layout);
        mSizeTagAdapter = new TagAdapter<String>(context);
        mSizeFlowTagLayout.setAdapter(mSizeTagAdapter);
        mSizeFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        for (int i = 0; i < beans.size(); i++) {
            System.out.println("beans@@@@@@@@@@@" + beans.get(i).getTagname());
            System.out.println("detail@@@@@@@@@@@" + detailBean.getTagname());
        }

        WaitDialog = WaitDIalog.createLoadingDialog(context, "正在加载...");
        getTag();
        mSizeFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {

            @Override
            public void onItemSelect(FlowTagLayout parent,
                                     List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i : selectedList) {
                        sb.append(parent.getAdapter().getItem(i));
                        sb.append(":");
                        morenPrice = beans.get(i).getPrice();
                        tagid = beans.get(i).getId();
                        tagname = beans.get(i).getTagname();
                        listener.refreshPriorityUI(beans.get(i).getTagname());

                        unit = beans.get(i).getUnit();
                        invenyory = beans.get(i).getInventory();
                        txt_title.setText("已选：" + beans.get(i).getTagname());
                        txt_inv.setText("库存" + invenyory + beans.get(i).getUnit());
                        if (invenyory.equals("0")) {
                            edit_num.setText("0");
                        }
                        /* tag_name.setText(tagname);*/
                        System.out.println("beans.get(arg2).getUnit()----" + beans.get(i).getUnit());
                        if (beans.get(i).getUnit().equals("")) {
                            System.out.println("9999");
                            txt_price.setText("¥" + morenPrice);//根据显示不同的标签价格会发生相应的变化
                            txt_yuan_price.setText("¥" + beans.get(i).getOriginal());
                        } else {
                            txt_price.setText("¥" + morenPrice + "/" + unit);//根据显示不同的标签价格会发生相应的变化
                            txt_yuan_price.setText("¥" + beans.get(i).getOriginal());
                            System.out.println("88888");
                        }
                    }
                    getXG();//获取限购商品数量
                    //txt_bianhao.setText("产品编号："+biaohao);//显示产品编号
                    System.out.println("tagid====" + tagid);
                    System.out.println("价格===" + morenPrice);
                    System.out.println("名称===" + tagname);
                    System.out.println("库存===" + invenyory);
                    //Toast.makeText(context, "移动研发:" + sb.toString(), Toast.LENGTH_SHORT).show();
                    isbool = true;
                } else {
                    isbool = false;
                    //Toast.makeText(context, "没有选择标签", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initSizeData();
        if (detailBean.getTitle_img() == null) {

        } else {
            bitmapUtils.display(img_title, MyCookieStore.URL + detailBean.getTitle_img());
        }
        setCanceledOnTouchOutside(true);//
        getWindow().setGravity(Gravity.BOTTOM); //此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.take_photo_anim);  //添加动画
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        getWindow().getDecorView().setPadding(0, 0, 0, 0); //就能够水平占满了
        lp.width = (int) (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }

    private void getTag() {   //获取商品标签
        //获取限购商品数量
        Url_info info = new Url_info(context);
        RequestParams params = new RequestParams();
        params.addBodyParameter("p_id", id);
        params.addBodyParameter("tagid", detailBean.getTagid());
        System.out.println("XG—moren—Tag_id*********" + detailBean.getTagid());
        HttpHelper hh = new HttpHelper(info.shop_limit, params, context) {

            @Override
            protected void setData(String json) {
                try {
                    JSONObject obj = new JSONObject(json);
                    String status = obj.getString("status");
                    WaitDIalog.closeDialog(WaitDialog);
                    if (status.equals("1")) {
                        xgBean = protocol.getAGNum(json);
                        if (xgBean.getLimit().equals("0")) {
                            edit_num.setText("1");
                        } else {
                            int intAll = Integer.valueOf(xgBean.getCart_num()) + Integer.valueOf(xgBean.getOrder_num());
                            if (intAll >= Integer.valueOf(xgBean.getLimit())) {
                                //XToast.makeText(context, "此商品限购数量为" + xgBean.getLimit(), XToast.LENGTH_SHORT).show();
                                edit_num.setText("0");
                            }
                        }
//start===
                        for (int i = 0; i < beans.size(); i++) {
                            System.out.println("####111111111111####@@@@@@@@@@@@@@@@");
                            if (beans.get(i).getTagname().equals(detailBean.getTagname())) {
                                System.out.println("####2222222222222####@@@@@@@@@@@@@@@@");
                                beans.get(i).setSelect(true);
                                if (beans.get(i).getUnit().equals("")) {
                                    txt_price.setText("¥" + beans.get(i).getPrice());//根据显示不同的标签价格会发生相应的变化
                                    txt_yuan_price.setText("¥" + beans.get(i).getOriginal());

                                } else {
                                    txt_price.setText("¥" + beans.get(i).getPrice() + "/" + beans.get(i).getUnit());//根据显示不同的标签价格会发生相应的变化
                                    txt_yuan_price.setText("¥" + beans.get(i).getOriginal());
                                }
                                System.out.println("####^^^^^#######@@@@@@@@@@@@@@@@");
                                txt_title.setText("已选：" + beans.get(i).getTagname());
                                morenPrice = beans.get(i).getPrice();
                                //txt_bianhao.setText("产品编号："+beans.get(i).getId());//显示产品编号
                                tagid = beans.get(i).getId();//显示一个标签
                                tagname = beans.get(i).getTagname();
                                listener.refreshPriorityUI(beans.get(i).getTagname());
                                invenyory = beans.get(i).getInventory();
                                txt_title.setText("已选：" + beans.get(i).getTagname());
                                txt_inv.setText("库存" + beans.get(i).getInventory() + beans.get(i).getUnit());
                                if ("0".equals(beans.get(i).getInventory())) {
                                    System.out.println("&&&&&&&&");
                                    edit_num.setText("0");
                                }
                                System.out.println("invenyory===*****" + beans.get(i).getInventory());
                                System.out.println("tagid默认的时候===" + tagid);
                                System.out.println("价格默认的时候===" + txt_price.getText().toString());
                                System.out.println("名称默认的时候===" + tagname);
                                System.out.println("库存默认的时候===" + invenyory);
                            } else {
                                beans.get(i).setSelect(false);
                            }

                        }
//end===
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
        edit_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_num.getText().toString())) {
                    // XToast.makeText(context, "数量不能为空", XToast.LENGTH_SHORT).show();
                    edit_num.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        //	}
        //};
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
           /* case R.id.txt_detel://取消
                AddShopDialog.this.dismiss();
                break;*/
            case R.id.back://取消
                AddShopDialog.this.dismiss();
                break;
            case R.id.edit_num:
                edit_num.setFocusableInTouchMode(true);
                break;
            case R.id.img_fu_jian://减
               /*if (invenyory.equals("0")) {
                    edit_num.setText("0");
                    XToast.makeText(context, "已售罄", XToast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(edit_num.getText().toString())) {
                        // XToast.makeText(context, "数量不能为空", XToast.LENGTH_SHORT).show();
                        edit_num.setText("1");
                        edit_num.setSelection(edit_num.getText().toString().length());
                    } else {*/
                if (Integer.valueOf(edit_num.getText().toString()) <= Integer.valueOf("1")) {

                }
                    /*if ("1".equals(Integer.valueOf(edit_num.getText().toString())+"")) {
                    }else if ("0".equals(Integer.valueOf(edit_num.getText().toString())+"")) {
					}*/
                else {
                    edit_num.setText(Integer.valueOf(edit_num.getText().toString()) - 1 + "");
                }
                //}
                // }
                break;
            case R.id.img_zheng_jia://加
                if (("0").equals(invenyory)) {
                    edit_num.setText("0");
                    XToast.makeText(context, "已售罄", XToast.LENGTH_SHORT).show();
                } else {
                /*
                    if (TextUtils.isEmpty(edit_num.getText().toString())) {
                        // XToast.makeText(context, "数量不能为空", XToast.LENGTH_SHORT).show();
                        edit_num.setText("1");
                        edit_num.setSelection(edit_num.getText().toString().length());
                        // edit_num.setText(Integer.valueOf(edit_num.getText().toString())+1+"");
                    } else {*/

                    // (invenyory != null && !TextUtils.isEmpty(invenyory)) {

                    if (Integer.valueOf(invenyory) <= (Integer.valueOf(edit_num.getText().toString().trim()))) {
                        XToast.makeText(context, "已售罄", XToast.LENGTH_SHORT).show();
                    } else {
                        if (xgBean.getLimit().equals("0")) {
                            edit_num.setText(Integer.valueOf(edit_num.getText().toString()) + 1 + "");
                        } else {
                            int intAll = Integer.valueOf(xgBean.getCart_num()) + Integer.valueOf(xgBean.getOrder_num());
                            if (intAll >= Integer.valueOf(xgBean.getLimit())) {
                                edit_num.setText("0");
                                // XToast.makeText(context, "此商品限购数量为" + xgBean.getLimit(), XToast.LENGTH_SHORT).show();
                                XToast.makeText(context, "此商品已超出限购数量", XToast.LENGTH_SHORT).show();

                            } else {
                                int all = Integer.valueOf(xgBean.getLimit()) - intAll;
                                if (Integer.valueOf(edit_num.getText().toString()) >= all) {
                                    XToast.makeText(context, "此商品剩余限购数量为" + all, XToast.LENGTH_SHORT).show();
                                } else {
                                    edit_num.setText(Integer.valueOf(edit_num.getText().toString()) + 1 + "");
                                }
                            }
                        }

                    }
                    // }
                    //}
                }
                break;
            case R.id.txt_btn://加入购物车
                if (isbool == true) {
                /*    if (edit_num.getText().toString().equals("0")) {
                        XToast.makeText(context, "数量最少为1", XToast.LENGTH_SHORT).show();
                    } else {*/
                    System.out.println("invenyory=-==========" + invenyory);
                    if ("0".equals(invenyory)) {
                        XToast.makeText(context, "已售罄", XToast.LENGTH_SHORT).show();
                    } else {
                        if (Integer.valueOf(invenyory) < (Integer.valueOf(edit_num.getText().toString().trim()))) {
                            XToast.makeText(context, "已售罄", XToast.LENGTH_SHORT).show();
                        } else {

                            if (xgBean.getLimit().equals("0")) {
                                System.out.println("tag****^^^********" + strtag);
                                if (strtag.equals("1")) {//tag值为1是立即购买
                                    getSubmiter();
                                } else if (strtag.equals("2")) {//tag值为2是加入购物车
                                    getAddShop();
                                }
                            } else {
                                int intAll = Integer.valueOf(xgBean.getCart_num()) + Integer.valueOf(xgBean.getOrder_num());
                                if (intAll >= Integer.valueOf(xgBean.getLimit())) {
                                    //XToast.makeText(context, "此商品限购数量为" + xgBean.getLimit(), XToast.LENGTH_SHORT).show();
                                    edit_num.setText("0");
                                    if (Integer.valueOf(xgBean.getCart_num()) > 0) {
                                        Intent intent = new Intent(context, ShopCartActivityTwo.class);
                                        context.startActivity(intent);
                                        AddShopDialog.this.dismiss();
                                    } else {
                                        Intent intent = new Intent(context, ShopOrderListActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("type", "type_zf_dfk");
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                        AddShopDialog.this.dismiss();
                                    }
                                } else {
                                    int all = Integer.valueOf(xgBean.getLimit()) - intAll;
                                    if (Integer.valueOf(edit_num.getText().toString()) > all) {
                                        XToast.makeText(context, "此商品剩余限购数量为" + all, XToast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println("tag****^^^********" + strtag);
                                        if (strtag.equals("1")) {//tag值为1是立即购买
                                            getSubmiter();
                                        } else if (strtag.equals("2")) {//tag值为1是加入购物车
                                            getAddShop();
                                        }
                                    }

                                }
                            }
                        }
                    }
                    //  }
                } else {
                    XToast.makeText(context, "请选择产品类型", XToast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    float totalPrice = (float) 0; //商品总价
    List<SubmitOrderBean> pro = new ArrayList<SubmitOrderBean>();
    String strExist_hours;

    private void getSubmiter() {//直接购买确认订单数据
        SubmitOrderBean info = new SubmitOrderBean();
        info.setTagid(tagid);
        info.setP_id(id);
        info.setP_title(detailBean.getTitle());
        info.setP_title_img(detailBean.getTitle_img());
        info.setPrice(String.valueOf(morenPrice));
        info.setNumber(String.valueOf(edit_num.getText().toString()));
        strExist_hours = detailBean.getExist_hours();
        pro.add(info);
        totalPrice += Float.parseFloat(edit_num.getText().toString()) * Float.parseFloat(morenPrice);
        setFloat();
        getex();//判断商品是否在派送范围时间
    }

    private void getex() {//判断商品是否在派送范围时间
        if (strExist_hours.equals("2")) {
            XToast.makeText(context, "当前时间不在派送时间范围内", XToast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(context, ConfirmOrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("all", totalPrice + "");
            bundle.putSerializable("pro", (Serializable) pro);
            intent.putExtras(bundle);
            context.startActivity(intent);
            AddShopDialog.this.dismiss();
            System.out.println("pro=======" + pro.size());
        }
    }

    private void getAddShop() {//加入购物车
        WaitDialog = WaitDIalog.createLoadingDialog(context, "正在加载...");
        Url_info info = new Url_info(context);
        RequestParams params = new RequestParams();
        params.addBodyParameter("p_id", id);
        params.addBodyParameter("p_title", detailBean.getTitle());
        params.addBodyParameter("tagid", tagid);
        params.addBodyParameter("p_title_img", detailBean.getTitle_img());
        params.addBodyParameter("price", morenPrice);
        //params.addBodyParameter("inventory", invenyory);
        params.addBodyParameter("number", edit_num.getText().toString());
        params.addBodyParameter("tagname", tagname);
        System.out.println("p_id====" + id + "==p_title===" + detailBean.getTitle() + "==img==" + detailBean.getTitle_img()
                + "===price==" + txt_price.getText().toString() + ""
                + "===" + edit_num.getText().toString() + "***tagname===" + tagname + "^^tagid----" + tagid
                + "**inventory=====" + invenyory);

        HttpHelper hh = new HttpHelper(info.add_shop_cart, params, context) {

            @Override
            protected void setData(String json) {
                str = protocol.setShop(json);
                WaitDIalog.closeDialog(WaitDialog);
                if (str.equals("1")) {
                    XToast.makeText(context, "加入购物车成功", XToast.LENGTH_SHORT).show();
                    AddShopDialog.this.dismiss();
                    getCartNum();
                } else {
                    XToast.makeText(context, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                WaitDIalog.closeDialog(WaitDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getCartNum() {//购物车商品数量
        Url_info info = new Url_info(context);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.cart_num, params, context) {

            @Override
            protected void setData(String json) {
                cartnum = protocol.getCartNum(json);
                if (cartnum.getCart_num().equals("0")) {
                    txt_shop_num.setVisibility(View.GONE);
                } else {
                    txt_shop_num.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    public void getXG() {
        Url_info info = new Url_info(context);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("p_id", id);
        params.addBodyParameter("tagid", tagid);
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.send(HttpRequest.HttpMethod.POST, info.shop_limit, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                xgBean = protocol.getAGNum(responseInfo.result);

                if (xgBean.getLimit().equals("0")) {
                    edit_num.setText("1");
                } else {
                    int intAll = Integer.valueOf(xgBean.getCart_num()) + Integer.valueOf(xgBean.getOrder_num());
                    if (intAll >= Integer.valueOf(xgBean.getLimit())) {
                        //XToast.makeText(context, "此商品限购数量为" + xgBean.getLimit(), XToast.LENGTH_SHORT).show();
                        edit_num.setText("0");
                    }
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    /**
     * 初始化数据
     */
    private void initSizeData() {
        List<String> dataSource = new ArrayList<String>();
        for (int i = 0; i < beans.size(); i++) {
            dataSource.add(beans.get(i).getTagname());
        }
        mSizeTagAdapter.onlyAddAll(dataSource);
    }

    private void setFloat() {//保留两位小数点
        BigDecimal b = new BigDecimal(totalPrice);
        totalPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
