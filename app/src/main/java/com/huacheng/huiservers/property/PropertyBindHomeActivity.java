package com.huacheng.huiservers.property;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
import com.huacheng.huiservers.geren.bean.GroupMemberBean;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.property.bean.ModelSelectCommon;
import com.huacheng.huiservers.property.bean.PropertyInfo;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Description: 0.4.1 物业绑定房屋页面
 * created by wangxiaotao
 * 2018/10/25 0025 下午 3:06
 */
public class PropertyBindHomeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_community;
    private TextView tv_community;
    private TextView tv_user_cate_title;
    private LinearLayout ll_user_cate;
    private TextView tv_user_cate;

    private LinearLayout ll_shop_cotainer;
    private LinearLayout ll_shop;
    private TextView tv_shop_num;
    private LinearLayout ll_person_cotainer;
    private LinearLayout ll_floor;
    private TextView tv_floor;
    private LinearLayout ll_unit;
    private TextView tv_unit;
    private LinearLayout ll_room;
    private TextView tv_room;
    private LinearLayout lin_btn;
    public static final int REQUEST_COMMUNITY=101;
    public static final int REQUEST_HOUSE_TYPE=102;
    public static final int REQUEST_CHAOBIAO=103;
    public static final int REQUEST_SHOP=104;
    public static final int REQUEST_FLOOR=105;
    public static final int REQUEST_UNIT=106;
    public static final int REQUEST_ROOM=107;

    private String community_id="";//小区id
    private String community_name="";//小区name
    private String company_id="";//
    private String company_name ="";//
    private String department_id  ="";//
    private String department_name   ="";//
    private String is_ym="";

    private String  houses_type="";//住房类型
    private String buildsing_id="";//楼号id
    private String unit="";        //单元号
    private String code="";       //房间号
    private String shopCode="";       //商铺号
    private String room_id="";    //房间id
    private String floor = "";     //层


    private EditText et_verifyID; //业主姓名
    private String name_phone="";

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
    }
    @Override
    protected void initView() {
        findTitleViews();
        titleName.setText("绑定房屋");
        //小区
        ll_community = findViewById(R.id.ll_community);
        ll_community.setOnClickListener(this);
        tv_community = findViewById(R.id.tv_community);
        //住户类型
        ll_user_cate = findViewById(R.id.ll_user_cate);
        ll_user_cate.setOnClickListener(this);
        tv_user_cate = findViewById(R.id.tv_user_cate);

        //商户编号
        ll_shop_cotainer = findViewById(R.id.ll_shop_cotainer);
        ll_shop = findViewById(R.id.ll_shop);
        ll_shop.setOnClickListener(this);
        tv_shop_num = findViewById(R.id.tv_shop_num);
        //住户
        ll_person_cotainer = findViewById(R.id.ll_person_cotainer);
        //楼号
        ll_floor = findViewById(R.id.ll_floor);
        ll_floor.setOnClickListener(this);
        tv_floor = findViewById(R.id.tv_floor);
        //单元号
        ll_unit = findViewById(R.id.ll_unit);
        ll_unit.setOnClickListener(this);
        tv_unit = findViewById(R.id.tv_unit);
        //房间号
        ll_room = findViewById(R.id.ll_room);
        ll_room.setOnClickListener(this);
        tv_room = findViewById(R.id.tv_room);
        //业主姓名
        et_verifyID = findViewById(R.id.et_verifyID);
        et_verifyID.setEnabled(false);

        lin_btn = findViewById(R.id.lin_btn);
        lin_btn.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_property_bind_home;
    }


    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.ll_community:
                //选择小区
                intent= new Intent(this,SelectCommunityActivity.class);
                startActivityForResult(intent,REQUEST_COMMUNITY);
                break;
            case R.id.ll_user_cate:
                // 选择住户类型
                if (NullUtil.isStringEmpty(community_id)){
                    XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent= new Intent(this,SelectCommonActivity.class);
                intent.putExtra("name","选择住房类型");
                intent.putExtra("type",0);
                startActivityForResult(intent,REQUEST_HOUSE_TYPE);
                break;
//
            case R.id.ll_shop:
                // 选择商户编号
                if (NullUtil.isStringEmpty(community_id)){
                    XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();

                    return;
                }
                if (NullUtil.isStringEmpty(houses_type)){
                    XToast.makeText(this,"请选择住户类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent= new Intent(this,SelectCommonActivity.class);
                intent.putExtra("name","选择商户编号");
                intent.putExtra("type",5);
                intent.putExtra("community_id",community_id);
                intent.putExtra("houses_type",houses_type);
                startActivityForResult(intent,REQUEST_SHOP);
                break;
            case R.id.ll_floor:
                // 选择楼号
                if (NullUtil.isStringEmpty(community_id)){
                    XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NullUtil.isStringEmpty(houses_type)){
                    XToast.makeText(this,"请选择住户类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                intent= new Intent(this,SelectCommonActivity.class);
                intent.putExtra("name","选择楼号");
                intent.putExtra("type",2);
                intent.putExtra("community_id",community_id);
                intent.putExtra("houses_type",houses_type);
                startActivityForResult(intent,REQUEST_FLOOR);
                break;
            case R.id.ll_unit:
                // 选择单元号
                if (NullUtil.isStringEmpty(community_id)){
                    XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NullUtil.isStringEmpty(houses_type)){
                    XToast.makeText(this,"请选择住户类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (NullUtil.isStringEmpty(buildsing_id)){
                    XToast.makeText(this,"请选择楼号", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent= new Intent(this,SelectCommonActivity.class);
                intent.putExtra("name","选择单元号");
                intent.putExtra("type",3);
                intent.putExtra("community_id",community_id);
                intent.putExtra("houses_type",houses_type);
                intent.putExtra("buildsing_id",buildsing_id);
                startActivityForResult(intent,REQUEST_UNIT);
                break;
            case R.id.ll_room:
                // 选择房间号
                if (NullUtil.isStringEmpty(community_id)){
                    XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NullUtil.isStringEmpty(houses_type)){
                    XToast.makeText(this,"请选择住户类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NullUtil.isStringEmpty(buildsing_id)){
                    XToast.makeText(this,"请选择楼号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NullUtil.isStringEmpty(unit)){
                    XToast.makeText(this,"请选择单元号", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent= new Intent(this,SelectCommonActivity.class);
                intent.putExtra("name","选择房间号");
                intent.putExtra("type",4);
                intent.putExtra("community_id",community_id);
                intent.putExtra("houses_type",houses_type);
                intent.putExtra("buildsing_id",buildsing_id);
                intent.putExtra("unit",unit);
                startActivityForResult(intent,REQUEST_ROOM);
                break;
            case R.id.lin_btn:
                if (checkReady()) {
                    getVerityInfo();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 验证业主
     */
    private void getVerityInfo() {
        new ToolUtils(et_verifyID, this).closeInputMethod();
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("room_id", room_id);
        params.put("key_str", name_phone);

        MyOkHttp.get().post(ApiHttpClient.PRO_PERSONAL_INFO, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
             //   hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    PropertyInfo propertyInfo = (PropertyInfo) JsonUtil.getInstance().parseJsonFromResponse(response, PropertyInfo.class);
                    if (propertyInfo != null) {

                        getinfofinish(propertyInfo.getName(), propertyInfo.getMp1(), propertyInfo.getIs_ym());

                    }
                } else {
                    hideDialog(smallDialog);
                    XToast.makeText(PropertyBindHomeActivity.this, "验证错误", XToast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyBindHomeActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
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
        params.put("community_id", community_id);
        params.put("community_name", community_name);
        params.put("company_id", company_id);
        params.put("company_name", company_name);
        params.put("department_id", department_id);
        params.put("department_name", department_name);
        params.put("house_type",houses_type);
        if ("2".equals(houses_type)){//商铺
//            params.put("building_id", "0");
//            params.put("building_name", "0");
//            params.put("unit", "0");
//            params.put("floor", "0");
            params.put("code", shopCode);
            params.put("room_id", room_id);
        }else {//住宅和公寓
            params.put("building_id", buildsing_id);
            params.put("building_name", buildsing_id+"号楼");
            params.put("unit", unit);
            params.put("floor", floor);
            params.put("code", code);
            params.put("room_id", room_id);
        }
        params.put("fullname", fullname);
        params.put("mobile", mobile + "");
        params.put("is_ym", is_ym);
        MyOkHttp.get().post(ApiHttpClient.PRO_BIND_USER, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    HouseBean propertyInfo = (HouseBean) JsonUtil.getInstance().parseJsonFromResponse(response, HouseBean.class);

                    //临时文件存储 绑定成功参数为2
                    SharedPreferences preferences1 = PropertyBindHomeActivity.this.getSharedPreferences("login", 0);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("is_wuye", "2");
                    editor.commit();
                    Intent intent = new Intent();
                    if (is_ym.equals("0")) {//拖欠旧的物业费用
                        intent.setClass(PropertyBindHomeActivity.this, PropertyHomeListActivity.class);
                    } else {
                        intent.setClass(PropertyBindHomeActivity.this, PropertyHomeNewJFActivity.class);
                    }

//                    intent.putExtra("fullname", fullname);
                    intent.putExtra("room_id", room_id);
                    startActivity(intent);
                    //绑定成功新增界面刷新
                    EventBus.getDefault().post(propertyInfo);
                    //刷新个人中心
                    EventBus.getDefault().post(new PersoninfoBean());
                    finish();
                } else {
                    XToast.makeText(PropertyBindHomeActivity.this, "绑定失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(PropertyBindHomeActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkReady() {
            //抄表
            if (NullUtil.isStringEmpty(community_id)){
                XToast.makeText(this,"请选择小区", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (NullUtil.isStringEmpty(houses_type)){
                XToast.makeText(this,"请选择住户类型", Toast.LENGTH_SHORT).show();
                return false;
            }
        if ("2".equals(houses_type)){
            if (NullUtil.isStringEmpty(shopCode)){
                XToast.makeText(this,"请选择商铺门牌号", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            if (NullUtil.isStringEmpty(buildsing_id)){
                XToast.makeText(this,"请选择楼号", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (NullUtil.isStringEmpty(unit)){
                XToast.makeText(this,"请选择单元号", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (NullUtil.isStringEmpty(code)){
                XToast.makeText(this,"请选择门牌号", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        name_phone = et_verifyID.getText().toString().trim();

        if (NullUtil.isStringEmpty(name_phone)){
            XToast.makeText(this,"请输入业主姓名或手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (data!=null){
                if (requestCode==REQUEST_COMMUNITY){
                    GroupMemberBean groupMemberBean = (GroupMemberBean) data.getSerializableExtra("community");
                    if (groupMemberBean.getId().equals(community_id)){
                    }else {
                        community_id = groupMemberBean.getId();
                        community_name=groupMemberBean.getName();
                        tv_community.setText(community_name+"");
                        company_id=groupMemberBean.getCompany_id();
                        company_name=groupMemberBean.getCompany_name();
                        department_id=groupMemberBean.getDepartment_id();
                        department_name=groupMemberBean.getDepartment_name();
                        is_ym=groupMemberBean.getIs_ym();

                        houses_type="";
                        tv_user_cate.setText("");
                        buildsing_id="";
                        tv_floor.setText("");
                        unit="";
                        tv_unit.setText("");
                        code="";
                        room_id="";
                        floor="";
                        tv_room.setText("");
                        shopCode="";
                        tv_shop_num.setText("");
                        et_verifyID.setEnabled(false);
                    }
                }
                else if (requestCode==REQUEST_HOUSE_TYPE){
                    ModelSelectCommon modelSelectCommon = (ModelSelectCommon) data.getSerializableExtra("modelselectcommon");
                    houses_type=modelSelectCommon.getId();
                    tv_user_cate.setText(modelSelectCommon.getType_name()+"");
                    if ("2".equals(houses_type)){
                        ll_person_cotainer.setVisibility(View.GONE);
                        ll_shop_cotainer.setVisibility(View.VISIBLE);
                    }else {
                        ll_person_cotainer.setVisibility(View.VISIBLE);
                        ll_shop_cotainer.setVisibility(View.GONE);
                    }
                }else if (requestCode==REQUEST_CHAOBIAO){
                    //useless
                }else if (requestCode==REQUEST_SHOP){
                    //商铺
                    ModelSelectCommon modelSelectCommon = (ModelSelectCommon) data.getSerializableExtra("modelselectcommon");
                    if (modelSelectCommon.getCode().equals("-1")){//全部
                        shopCode="";
                        tv_shop_num.setText("");
                    }else {
                        room_id=modelSelectCommon.getId();
                        shopCode=modelSelectCommon.getCode();
                        tv_shop_num.setText(modelSelectCommon.getCode()+"");
                        et_verifyID.setEnabled(true);
                    }
                }else if (requestCode==REQUEST_FLOOR){
                    ModelSelectCommon modelSelectCommon = (ModelSelectCommon) data.getSerializableExtra("modelselectcommon");
                    if (modelSelectCommon.getBuildsing_id().equals(buildsing_id)){
                    }else {
                        if (modelSelectCommon.getBuildsing_id().equals("-1")){
                            buildsing_id="";
                            tv_floor.setText("");
                            unit="";
                            tv_unit.setText("");
                            code="";
                            room_id="";
                            floor="";
                            tv_room.setText("");
                            et_verifyID.setEnabled(false);
                        }else {
                            buildsing_id=modelSelectCommon.getBuildsing_id();
                            tv_floor.setText(modelSelectCommon.getBuildsing_id()+"号楼");
                            unit="";
                            tv_unit.setText("");
                            code="";
                            room_id="";
                            floor="";
                            tv_room.setText("");
                            et_verifyID.setEnabled(false);
                        }
                    }
                }else if (requestCode==REQUEST_UNIT){
                    ModelSelectCommon modelSelectCommon = (ModelSelectCommon) data.getSerializableExtra("modelselectcommon");
                    if (modelSelectCommon.getUnit().equals(unit)){
                    }else {
                        if (modelSelectCommon.getUnit().equals("-1")){
                            unit="";
                            tv_unit.setText("");
                            code="";
                            room_id="";
                            floor="";
                            tv_room.setText("");
                        }else {
                            unit=modelSelectCommon.getUnit();
                            tv_unit.setText(modelSelectCommon.getUnit()+"单元");
                            code="";
                            room_id="";
                            floor="";
                            tv_room.setText("");
                            et_verifyID.setEnabled(false);
                        }
                    }
                }else if (requestCode==REQUEST_ROOM){
                    ModelSelectCommon modelSelectCommon = (ModelSelectCommon) data.getSerializableExtra("modelselectcommon");
                    if (modelSelectCommon.getCode().equals("-1")){//全部
                        code="";
                        tv_room.setText("");
                    }else {
                        room_id=modelSelectCommon.getId();
                        code=modelSelectCommon.getCode();
                        tv_room.setText(modelSelectCommon.getCode()+"");
                        floor=modelSelectCommon.getFloor();
                        et_verifyID.setEnabled(true);
                    }
                }else {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
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
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
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
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
