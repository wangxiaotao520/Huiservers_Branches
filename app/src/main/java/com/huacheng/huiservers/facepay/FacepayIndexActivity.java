package com.huacheng.huiservers.facepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.facepay.adapter.FacepayIndexAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.FacePayProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.ClearEditText;
import com.huacheng.huiservers.wuye.WuyeXioaquActivity;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/22.
 */

public class FacepayIndexActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.spinnerPayItem)
    Spinner spinnerPayItem;
    @BindView(R.id.tv_yourHouse)
    TextView tv_yourHouse;
    @BindView(R.id.lin_payItem)
    LinearLayout linPayItem;
    @BindView(R.id.lin_yourHouse)
    LinearLayout linYourHouse;
    @BindView(R.id.tv_checkInfo)
    TextView tvCheckInfo;
    @BindView(R.id.lin_checkInfo)
    LinearLayout linCheckInfo;
    @BindView(R.id.lin_facepay_note)
    LinearLayout linFacepayNote;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.lin_right)
    LinearLayout linRight;
    @BindView(R.id.tv_indexNext)
    TextView tvIndexNext;
    @BindView(R.id.cet_inputPrice)
    ClearEditText cetInputPrice;
    @BindView(R.id.rel_PayItem)
    RelativeLayout relPayItem;

    Intent intent = new Intent();
    @BindView(R.id.et_facepay_note)
    EditText etFacepayNote;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_shops)
    TextView mTvShops;
    @BindView(R.id.lin_shops)
    LinearLayout mLinShops;

    private String xq_id, bd_id, unit_id, code, XQName, budName;
    private String SH_id, SH_uid, SH_name;

    List<FacePayBean> facePays;

    private TextView text;
    private SharedPreferences preferencesLogin;
    private String login_type;

    String itemID;
    String selectStr;

    String departmentID, departmentName, companyID, companyName, roomNum, floors;
    String personName, personalMp;

    SharedPreferences facepayPref;
    SharedPreferences.Editor facepayShared;

    public static FacepayIndexActivity sFacePayIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   SetTransStatus.GetStatus(this);
    }

    private static final int DECIMAL_DIGITS = 2;//小数的位数

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.facepay_index);
        ButterKnife.bind(this);
   //     SetTransStatus.GetStatus(this);
        sFacePayIndex = this;
    //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        facepayPref = getSharedPreferences("facepay", 0);
        facepayShared = facepayPref.edit();
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifference = screenHeight - rect.bottom;//计算软键盘占有的高度  = 屏幕高度 - 视图可见高度
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, heightDifference);//设置ScrollView的marginBottom的值为软键盘占有的高度即可
                scrollView.requestLayout();
            }
        });

        cetInputPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        cetInputPrice.setFilters(new InputFilter[]{new InputFilter() {
            int decimalNumber = 2;//小数点后保留位数

            @Override
            //source:即将输入的内容 dest：原来输入的内容
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String sourceContent = source.toString();
                String lastInputContent = dest.toString();

                //验证删除等按键
                if (TextUtils.isEmpty(sourceContent)) {
                    return "";
                }
                //以小数点"."开头，默认为设置为“0.”开头
                if (sourceContent.equals(".") && lastInputContent.length() == 0) {
                    return "0.";
                }
                /*//输入“0”，默认设置为以"0."开头
                if (sourceContent.equals("0") && lastInputContent.length() == 0) {
                    return "0.";
                }*/
                //小数点后保留两位
                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= decimalNumber + 1) {
                        return "";
                    }

                }
                return null;
            }
        }});

        cetInputPrice.addTextChangedListener(new TextWatcher() {
            String temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        cetInputPrice.setText(s);
                        cetInputPrice.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    cetInputPrice.setText(s);
                    cetInputPrice.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        cetInputPrice.setText(s.subSequence(0, 1));
                        cetInputPrice.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initData1();
    }

    FacePayBean face;

    private void initData1() {

        titleName.setText("当面付");
        linRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.ic_order5);
        initDataPayItem();

        spinnerPayItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                face = facePays.get(position);
                if (face != null) {
                    itemID = face.getId();
                    selectStr = face.getC_name().toString();
                    if (selectStr.equals("请选择")) {
                        selectStr = "";
                    }
                    if (selectStr.equals("")) {
                        linFacepayNote.setVisibility(View.GONE);
                        mLinShops.setVisibility(View.GONE);

                        linYourHouse.setVisibility(View.GONE);
                        linCheckInfo.setVisibility(View.GONE);
                    } else if (face.getSign().equals("1")) {//显示房屋选择
                        linFacepayNote.setVisibility(View.VISIBLE);
                        linYourHouse.setVisibility(View.VISIBLE);
                        linCheckInfo.setVisibility(View.VISIBLE);
                        mLinShops.setVisibility(View.GONE);
                        //处理缓存数据
                        getSharedPreFacepay();

                    } else if (face.getSign().equals("0")) {
                        linFacepayNote.setVisibility(View.VISIBLE);
                        mLinShops.setVisibility(View.GONE);

                        linYourHouse.setVisibility(View.GONE);
                        linCheckInfo.setVisibility(View.GONE);
                    } else if (face.getSign().equals("2")) {//商户
                        linFacepayNote.setVisibility(View.VISIBLE);
                        mLinShops.setVisibility(View.VISIBLE);

                        linYourHouse.setVisibility(View.GONE);
                        linCheckInfo.setVisibility(View.GONE);

                    }/*else {
                        linFacepayNote.setVisibility(View.GONE);
                        linYourHouse.setVisibility(View.GONE);
                        linCheckInfo.setVisibility(View.GONE);
                    }*/
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * 处理缓存数据
     */
    private void getSharedPreFacepay() {
        tv_yourHouse.setText(facepayPref.getString("all_name", ""));//房屋详细信息

        xq_id = facepayPref.getString("xq_id", "");
        bd_id = facepayPref.getString("bd_id", "");
        code = facepayPref.getString("code", "");
        unit_id = facepayPref.getString("unit_id", "");
        XQName = facepayPref.getString("XQName", "");
        budName = facepayPref.getString("budName", "");
        roomNum = facepayPref.getString("roomNum", "");
        floors = facepayPref.getString("floors", "");
        departmentID = facepayPref.getString("departmentID", "");
        departmentName = facepayPref.getString("departmentName", "");
        companyID = facepayPref.getString("companyID", "");
        companyName = facepayPref.getString("companyName", "");
        personName = facepayPref.getString("personName", "");
        personalMp = facepayPref.getString("personalMp", "");
        if (!StringUtils.isEmpty(personName) && !StringUtils.isEmpty(personalMp)) {
            tvCheckInfo.setText(personName + "/" + personalMp);//用户的信息
        }


    }

    private void initDataPayItem() {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        new HttpHelper(urlInfo.getFacePayCate, new RequestParams(), this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                facePays = new FacePayProtocol().getFacePayCate(json);
                if (facePays.size() > 0) {
                    //将可选内容与ArrayAdapter连接起来，simple_spinner_item是android系统自带样式
                    FacepayIndexAdapter selectWuyeAdapter = new FacepayIndexAdapter(facePays, FacepayIndexActivity.this);
                    spinnerPayItem.setAdapter(selectWuyeAdapter);//绑定adapter到spinner控件上

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                closeInputMethod();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 关闭软键盘
     */
    public void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(cetInputPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    String inputPrice, facepayNote;

    @OnClick({R.id.lin_left, R.id.lin_right, R.id.tv_indexNext, R.id.tv_yourHouse, R.id.lin_shops})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                UIUtils.closeInputMethod(this, cetInputPrice);
                finish();
                break;
            case R.id.lin_right://当面付订单
                intent.setClass(this, FacepayPaymentHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_indexNext://下一步
                inputPrice = cetInputPrice.getText().toString();
                if (inputPrice.equals("")) {
                    XToast.makeText(this, "请输入金额", XToast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(inputPrice) == 0) {
                    XToast.makeText(this, "缴费金额不能是0元", XToast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(selectStr)) {
                    XToast.makeText(this, "请选择缴费项目", XToast.LENGTH_SHORT).show();
                } else if (face.getSign().equals("1")) {
                    facepayNote = etFacepayNote.getText().toString();

                    String yourHouse = tv_yourHouse.getText().toString();
                    if (StringUtils.isEmpty(yourHouse)) {
                        XToast.makeText(this, "请选择房屋", XToast.LENGTH_SHORT).show();
                    } else {
                        submitFacepay(face.getSign());
                    }

                } else if (face.getSign().equals("0")) {
                    //selectStr.equals("保险") || selectStr.equals("其他")
                    facepayNote = etFacepayNote.getText().toString();
                    if (facepayNote.equals("")) {
                        XToast.makeText(this, "请填写备注", XToast.LENGTH_SHORT).show();
                    } else {
                        submitFacepay(face.getSign());
                    }
                } else if (face.getSign().equals("2")) {
                    //selectStr.equals("保险") || selectStr.equals("其他")
                    facepayNote = etFacepayNote.getText().toString();
                    if (facepayNote.equals("")) {
                        XToast.makeText(this, "请填写备注", XToast.LENGTH_SHORT).show();
                    } else if (StringUtils.isEmpty(mTvShops.getText().toString().trim())) {
                        XToast.makeText(this, "请选择商铺", XToast.LENGTH_SHORT).show();
                    } else {
                        submitFacepay(face.getSign());
                    }
                }
                break;
            case R.id.tv_yourHouse:
                intent.setClass(this, WuyeXioaquActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.lin_shops://选择商铺
                intent.setClass(this, FacePayStoreActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    private void submitFacepay(final String type) {
        Url_info urlInfo = new Url_info(this);
        RequestParams p = new RequestParams();
        p.addBodyParameter("c_id", itemID);
        p.addBodyParameter("c_name", selectStr);
        p.addBodyParameter("money", inputPrice);

        if (type.equals("1")) {
            p.addBodyParameter("community_id", xq_id);
            p.addBodyParameter("community_name", XQName);
            p.addBodyParameter("building_id", bd_id);
            p.addBodyParameter("building_name", budName);
            p.addBodyParameter("unit", unit_id);
            p.addBodyParameter("room_id", code);
            p.addBodyParameter("fullname", personName);
            p.addBodyParameter("mobile", personalMp);

            p.addBodyParameter("floor", floors);
            p.addBodyParameter("code", roomNum);

            p.addBodyParameter("company_id", companyID);
            p.addBodyParameter("company_name", companyName);
            p.addBodyParameter("department_id", departmentID);
            p.addBodyParameter("department_name", departmentName);
        }
        if (type.equals("2")) {
            p.addBodyParameter("m_id", SH_id);
            p.addBodyParameter("m_uid", SH_uid);
            p.addBodyParameter("m_name", SH_name);
        }
        if (StringUtils.isEmpty(facepayNote)) {
            p.addBodyParameter("note", "无");
        } else {
            p.addBodyParameter("note", facepayNote);
        }
        showDialog(smallDialog);
        new HttpHelper(urlInfo.addFacePayOrder, p, this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    String str = jsonObject.getString("status");
                    String jsonmsg = jsonObject.getString("msg");
                    FacePayBean payOrder = new FacePayProtocol().getFacePayOrder(json);
                    if (payOrder != null) {
                        intent.setClass(FacepayIndexActivity.this, FacepayConfirmPaymentActivity.class);
                        intent.putExtra("id", payOrder.getId());
                        intent.putExtra("order_number", payOrder.getOrder_number());
                        intent.putExtra("c_name", payOrder.getC_name());
                        if (type.equals("1")) {
                            intent.putExtra("community_name", payOrder.getCommunity_name());
                            intent.putExtra("building_name", payOrder.getBuilding_name());
                            intent.putExtra("unit", payOrder.getUnit());
                            intent.putExtra("floor", payOrder.getFloor());
                            intent.putExtra("code", payOrder.getCode());
                            intent.putExtra("psname", personName);
                            intent.putExtra("mp1", personalMp);
                        } else if (type.equals("2")) {
                            intent.putExtra("sh_name", SH_name);
                        }
                        intent.putExtra("money", payOrder.getMoney());
                        intent.putExtra("addtime", StringUtils.getDateToString(payOrder.getAddtime(), "7"));
                        if (StringUtils.isEmpty(facepayNote)) {
                            intent.putExtra("note", "无");
                        } else {
                            intent.putExtra("note", facepayNote);
                        }
                        intent.putExtra("sign", type);
                        startActivityForResult(intent, 1000);
                    } else {
                        XToast.makeText(FacepayIndexActivity.this, jsonmsg, XToast.LENGTH_SHORT).show();
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


    ShopProtocol protocol2 = new ShopProtocol();

    private void facepayCallBack(String typebk, String oidbk) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", oidbk);
        if (typebk.equals("wuye")) {
            params.addBodyParameter("type", "property");// 物业的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("shop") || typebk.equals("shop_1")) {// shop_1
            // 是从购物流程一路付款成功的
            params.addBodyParameter("type", "shop");// 购物的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("Yweixiu")) {
            params.addBodyParameter("type", "service");// 维修预付款的
            params.addBodyParameter("prepay", "1");
        } else if (typebk.equals("weixiu")) {
            params.addBodyParameter("type", "service");// 维修尾款的
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("huodong")) {
            params.addBodyParameter("type", "activity");// 活動
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("facepay")) {
            params.addBodyParameter("type", "face");// 当面付
            params.addBodyParameter("prepay", "0");
        } else if (typebk.equals("wired")) {
            params.addBodyParameter("type", "wired");// 当面付
            params.addBodyParameter("prepay", "0");
        }
        new HttpHelper(info.confirm_order_payment, params,
                this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String str = protocol2.setShop(json);
                if (str.equals("1")) {
                    XToast.makeText(FacepayIndexActivity.this, str,
                            XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(FacepayIndexActivity.this, str,
                            XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        String unit = data.getExtras().getString("unit");
        String bud_id = data.getExtras().getString("bud_id");
        String XQ_id = data.getExtras().getString("XQ_id");
        String room_id = data.getExtras().getString("room_id");
        String all_name = data.getExtras().getString("all_name");
        String XQ_name = data.getExtras().getString("XQ_name");
        String bud_Name = data.getExtras().getString("bud_Name");

        String room_num = data.getExtras().getString("code");
        String floors_ = data.getExtras().getString("floors");
        String department_id = data.getExtras().getString("department_id");
        String department_name = data.getExtras().getString("department_name");
        String company_id = data.getExtras().getString("company_id");
        String company_name = data.getExtras().getString("company_name");

        //商户
        String sh_id = data.getExtras().getString("sh_id");
        String sh_uid = data.getExtras().getString("sh_uid");
        String sh_name = data.getExtras().getString("sh_name");


        switch (resultCode) {
            case 44:
                if (requestCode == 1 && resultCode == 44) {
                    tv_yourHouse.setText(all_name);
                    xq_id = XQ_id;
                    bd_id = bud_id;
                    code = room_id;
                    unit_id = unit;
                    XQName = XQ_name;
                    budName = bud_Name;
                    roomNum = room_num;
                    floors = floors_;

                    departmentID = department_id;
                    departmentName = department_name;
                    companyID = company_id;
                    companyName = company_name;
                    if (!StringUtils.isEmpty(code)) {
                        getPaymentHouse(code);
                    }
                    facepayShared.putString("all_name", all_name);
                    facepayShared.putString("xq_id", xq_id);
                    facepayShared.putString("bd_id", bd_id);
                    facepayShared.putString("code", code);
                    facepayShared.putString("unit_id", unit_id);

                    facepayShared.putString("XQName", XQName);
                    facepayShared.putString("budName", budName);
                    facepayShared.putString("roomNum", roomNum);
                    facepayShared.putString("floors", floors);

                    facepayShared.putString("departmentID", departmentID);
                    facepayShared.putString("departmentName", departmentName);
                    facepayShared.putString("companyID", companyID);
                    facepayShared.putString("companyName", companyName);
                    facepayShared.commit();
                }
                break;
            case 100:
                if (requestCode == 1 && resultCode == 100) {
                }
                break;
            case 999:
                String typeCB = data.getExtras().getString("typeCallback");
                String o_idCB = data.getExtras().getString("o_idCallback");
                if (!StringUtils.isEmpty(typeCB) && !StringUtils.isEmpty(o_idCB)) {
                    facepayCallBack(typeCB, o_idCB);
                } else {
                    XToast.makeText(this, "回调type+o_id异常", XToast.LENGTH_SHORT).show();
                }
                break;
            case 200://商户选择返回
                SH_id = sh_id;
                SH_uid = sh_uid;
                SH_name = sh_name;
                mTvShops.setText(SH_name);

                break;
            case 1111://商户没有选择返回

                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void getPaymentHouse(String roomID) {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams p = new RequestParams();
        p.addBodyParameter("room_id", roomID);
        new HttpHelper(urlInfo.getRoomPersonalInfo, p, this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                FacePayBean roomPersonal = new FacePayProtocol().getRoomPersonalInfo(json);
                if (roomPersonal != null) {
                    personName = roomPersonal.getName();
                    personalMp = roomPersonal.getMp1();
                    tvCheckInfo.setText(personName + "/" + personalMp);

                    facepayShared.putString("personName", personName);
                    facepayShared.putString("personalMp", personalMp);
                    facepayShared.commit();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

}
