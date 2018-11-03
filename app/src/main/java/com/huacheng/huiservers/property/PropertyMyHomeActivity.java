package com.huacheng.huiservers.property;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.property.bean.PropertyInfo;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.wuye.WuyeXioaquActivity;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.ToastUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Author: badge
 * Create: 2018/8/3 16:52
 */
public class PropertyMyHomeActivity extends BaseActivity {


    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_btn_verify)
    TextView tvBtnVerify;
    @BindView(R.id.tv_house)
    TextView tvHouse;
    @BindView(R.id.rel_selectaddress)
    RelativeLayout relSelectaddress;
    @BindView(R.id.lin_errorinfo)
    LinearLayout linErrorinfo;
    @BindView(R.id.et_verifyID)
    EditText mEtVerifyID;

    private String xq_id, bd_id, unit_id, code, XQName, budName;
    String departmentID, departmentName, companyID, companyName, roomNum, floors, isYm;
    String personName, personalMp;
    SharedPreferences.Editor facepayShared;
    SharedPreferences facepayPref;

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        titleName.setText("缴费");
        facepayPref = getSharedPreferences("property_home", 0);
        facepayShared = facepayPref.edit();
        /*tvVerifyID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                linErrorinfo.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

    }


    @Override
    protected void initData() {

    }

    private void getVerityInfo() {
        new ToolUtils(mEtVerifyID, this).closeInputMethod();
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("room_id", code);
        params.put("key_str", mEtVerifyID.getText().toString().trim());

        MyOkHttp.get().post(ApiHttpClient.PRO_PERSONAL_INFO, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    PropertyInfo propertyInfo = (PropertyInfo) JsonUtil.getInstance().parseJsonFromResponse(response, PropertyInfo.class);
                    if (propertyInfo != null) {

                        getinfofinish(propertyInfo.getName(), propertyInfo.getMp1(), propertyInfo.getIs_ym());

                    }
                } else {
                    XToast.makeText(PropertyMyHomeActivity.this, "验证错误", XToast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyMyHomeActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 完成绑定
     *
     * @param fullname
     * @param mobile
     * @param is_ym
     */
    private void getinfofinish(final String fullname, String mobile, final String is_ym) {//完成绑定
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id", xq_id);
        params.put("community_name", XQName);
        params.put("company_id", companyID);
        params.put("company_name", companyName);
        params.put("department_id", departmentID);
        params.put("department_name", departmentName);
        params.put("building_id", bd_id);
        params.put("building_name", budName);
        params.put("unit", unit_id);
        params.put("floor", floors);
        params.put("code", roomNum);
        params.put("room_id", code);
        params.put("fullname", fullname);
        params.put("mobile", mobile + "");
        params.put("is_ym", is_ym);
        MyOkHttp.get().post(ApiHttpClient.PRO_BIND_USER, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                if (JsonUtil.getInstance().isSuccess(response)) {
                    HouseBean propertyInfo = (HouseBean) JsonUtil.getInstance().parseJsonFromResponse(response, HouseBean.class);

                    //临时文件存储 绑定成功参数为2
                    SharedPreferences preferences1 = PropertyMyHomeActivity.this.getSharedPreferences("login", 0);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("is_wuye", "2");
                    editor.commit();
                    if (is_ym.equals("0")) {//拖欠旧的物业费用
                        intent.setClass(PropertyMyHomeActivity.this, PropertyHomeListActivity.class);
                    } else {
                        intent.setClass(PropertyMyHomeActivity.this, PropertyHomeNewJFActivity.class);
                    }

//                    intent.putExtra("fullname", fullname);
                    intent.putExtra("room_id", code);
                    startActivity(intent);
                    //绑定成功新增界面刷新
                    EventBus.getDefault().post(propertyInfo);
                    //刷新个人中心
                    EventBus.getDefault().post(new PersoninfoBean());
                } else {
                    XToast.makeText(PropertyMyHomeActivity.this, "绑定失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                XToast.makeText(PropertyMyHomeActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.property_myhome;
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

    Intent intent = new Intent();
    String house, verifyID;

    @OnClick({R.id.lin_left, R.id.tv_btn_verify, R.id.rel_selectaddress})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_btn_verify:
                house = tvHouse.getText().toString();
                verifyID = mEtVerifyID.getText().toString();
                if (NullUtil.isStringEmpty(house)) {
                    ToastUtils.showShort(this, "请选择房屋");
                    return;
                }
                //验证身份是否正确
                if (NullUtil.isStringEmpty(verifyID)) {
                    linErrorinfo.setVisibility(View.VISIBLE);
                    return;
                }
                getVerityInfo();

                break;
            case R.id.rel_selectaddress:
                intent.setClass(this, WuyeXioaquActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        new ToolUtils(mEtVerifyID, PropertyMyHomeActivity.this).closeInputMethod();
        mEtVerifyID.clearFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
        String is_ym = data.getExtras().getString("is_ym");

        switch (resultCode) {
            case 44:
                if (requestCode == 1 && resultCode == 44) {
                    tvHouse.setText(all_name);
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
                    isYm = is_ym;
                    if (!StringUtils.isEmpty(code)) {
//                        getPaymentHouse(code);
                    }
                }
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
