package com.huacheng.huiservers.center;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.huacheng.huiservers.HomeActivity;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.XiaoquAdapter;
import com.huacheng.huiservers.center.bean.CityBean;
import com.huacheng.huiservers.center.bean.ModelRegion;
import com.huacheng.huiservers.dialog.IsChooseXiaoquDialog;
import com.huacheng.huiservers.geren.bean.GroupMemberBean;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.GetJsonDataUtil;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.PermissionUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.TextPinyinUtil;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.ClearEditText;
import com.huacheng.huiservers.view.Cn2Spell;
import com.huacheng.huiservers.view.PinyinComparator;
import com.huacheng.huiservers.view.SideBar;
import com.huacheng.huiservers.view.SideBar.OnTouchingLetterChangedListener;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class XiaoquActivity extends BaseUI implements OnClickListener, AMapLocationListener {
    private TextView txt_dialog, txt_search, text_city;
    private ListView list_center;
    private ImageView search_back;
    private XiaoquAdapter adapter;
    private RelativeLayout rel_no_data;
    private LinearLayout ly_tag;
    private HashMap<Integer, Boolean> sss;
    CenterProtocol protocol = new CenterProtocol();
    List<CityBean> beans = new ArrayList<CityBean>();
    /**
     * 汉字转换成拼音的类
     */
    //private CharacterParser characterParser;
    private List<GroupMemberBean> SourceDateList = new ArrayList<>();
    private SideBar sideBar;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String id, name, type, region_city;
    private ClearEditText mClearEditText, et_search;
    private SharePrefrenceUtil sharePrefrenceUtil;
    // private IsChooseXiaoquDialog isChooseXiaoquDialog;
    private ArrayList<ModelRegion> jsonBean;
    private ArrayList<String> options1Items = new ArrayList<>();//省
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();//市
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();//区
    private int selected_options1, selected_options2, selected_options3;//默认选中
    private String location_provice, location_district, location_city;//
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public static final int COARSE_LOCATION_REQUEST_CODE = 88;
    private Thread thread_parse_json;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_SUCCESS:
                    hideDialog(smallDialog);
                    //   showCityWheel();
                    break;
                case MSG_LOAD_FAILED:
                    hideDialog(smallDialog);
                    Toast.makeText(XiaoquActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private String mFirstIn_region_id = "";
    private boolean isInitLocaion = false;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.xiaoqu_pinyin_list);
     //   SetTransStatus.GetStatus(this);//系统栏默认为黑色

        sharePrefrenceUtil = new SharePrefrenceUtil(this);
        pinyinComparator = new PinyinComparator();
        et_search = findViewById(R.id.et_search);
        et_search.setHint("请输入小区名称");
        text_city = findViewById(R.id.text_city);
        search_back = findViewById(R.id.search_back);
        txt_search = findViewById(R.id.txt_search);
        ly_tag = findViewById(R.id.ly_tag);
        rel_no_data = findViewById(R.id.rel_no_data);
        search_back.setOnClickListener(this);
        txt_search.setOnClickListener(this);
        ly_tag.setOnClickListener(this);

        // 实例化汉字转拼音类
        //characterParser = CharacterParser.getInstance();
        sideBar = findViewById(R.id.sidrbar);
        txt_dialog = findViewById(R.id.txt_dialog);
        sideBar.setTextView(txt_dialog);
        list_center = findViewById(R.id.list_center);

        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("type"))) {
            type = intent.getStringExtra("type");
            if (type.equals("splash")) {
                search_back.setVisibility(View.GONE);
            }
        } else {
            type = "else";
        }
        text_city.setText("定位中...");
        listonclick();
    }

    @Override
    protected void initData() {
        super.initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
            } else {
                startLocation();
            }
        } else {
            startLocation();
        }

    }

    /**
     * 根据定位获取小区信息
     *
     * @param region
     */
    private void getloactionCommunity(final String region, final int istag) {
        HashMap<String, String> params = new HashMap<>();
        if (istag == 1) {
            params.put("region_name", region);
        } else {
            showDialog(smallDialog);
            params.put("region_id", region);
        }

        MyOkHttp.get().post(ApiHttpClient.GET_COMMUNITY_BYCITY, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    CityBean cityBean = (CityBean) JsonUtil.getInstance().parseJsonFromResponse(response, CityBean.class);
                    if (cityBean != null) {
                        if (cityBean.getCommunity_list() != null && cityBean.getCommunity_list().size() > 0) {
                            rel_no_data.setVisibility(View.GONE);
                            sideBar.setVisibility(View.VISIBLE);
                            List<GroupMemberBean> SourceDateList_new = filledData(cityBean.getCommunity_list());
                            SourceDateList.clear();
                            SourceDateList.addAll(SourceDateList_new);
                            // 根据a-z进行排序源数据
                            Collections.sort(SourceDateList, pinyinComparator);
                            if (adapter == null) {
                                adapter = new XiaoquAdapter(XiaoquActivity.this, SourceDateList);
                                list_center.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            mFirstIn_region_id = cityBean.getCommunity_list().get(0).getRegion_id();
                        } else {
                            rel_no_data.setVisibility(View.VISIBLE);
                            sideBar.setVisibility(View.GONE);
                            txt_dialog.setVisibility(View.GONE);
                            SourceDateList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mFirstIn_region_id = 0 + "";
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(XiaoquActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 根据关键字搜索获取小区信息
     *
     * @param region_id
     */
    private void getSearchCommunity(String region_id) {
        new ToolUtils(et_search, this).closeInputMethod();
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("community_str", et_search.getText().toString().trim());
        params.put("region_id", region_id);

        MyOkHttp.get().post(ApiHttpClient.SERCH_COMMUNITY, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    CityBean cityBean = (CityBean) JsonUtil.getInstance().parseJsonFromResponse(response, CityBean.class);
                    if (cityBean != null) {
                        if (cityBean.getCommunity_list() != null && cityBean.getCommunity_list().size() > 0) {
                            rel_no_data.setVisibility(View.GONE);
                            CityBean cityBean1 = cityBean.getCommunity_list().get(0);
                            String name = cityBean1.getName();
                            String pinyinFirstLetter = getPinyinFirstLetter(name);
                            final int position = adapter.getPositionForSection(pinyinFirstLetter.charAt(0));
                            if (position != -1) {
                                list_center.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        list_center.setSelection(position);
                                    }
                                });
                            }
                        } else {
                            XToast.makeText(XiaoquActivity.this, "暂无该小区", XToast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(XiaoquActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.search_back:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name", "");
                bundle.putString("id", "");
                bundle.putString("all_id", "");
                bundle.putString("all_name", "");
                intent.putExtras(bundle);
                setResult(1111, intent);
                finish();
                break;
            case R.id.ly_tag:
                if (options1Items.size() > 0) {
                    showCityWheel();
                }
                new ToolUtils(et_search, XiaoquActivity.this).closeInputMethod();
                et_search.clearFocus();
                break;

            case R.id.txt_search:
                if (options1Items.size() <= 0 || NullUtil.isStringEmpty(mFirstIn_region_id)) {
                    return;
                }
                getSearchCommunity(mFirstIn_region_id + "");

                break;
            default:
                break;
        }
    }

    /**
     * 解析cityjson
     */
    private void parseJson() {
        showDialog(smallDialog);
        if (thread_parse_json == null) {
            thread_parse_json = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 子线程中解析省市区数据
                    initJsonData();
                }
            });
        }
        thread_parse_json.start();
    }


    /**
     * 显示三级城市联动
     */
    private void showCityWheel() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(XiaoquActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                selected_options1 = options1;
                selected_options2 = option2;
                selected_options3 = options3;
                if (jsonBean != null && jsonBean.size() > 0) {
                    if (jsonBean.get(options1).getS_list().get(option2).getSs_list().size()==0) {
                        return;
                    }
                    text_city.setText(jsonBean.get(options1).getS_list().get(option2).getRegion_name() + "  " +
                            jsonBean.get(options1).getS_list().get(option2).getSs_list().get(options3).getRegion_name());
                    getloactionCommunity(jsonBean.get(options1).getS_list().get(option2).getSs_list().get(options3).getRegion_id() + "", 2);
//                    XToast.makeText(XiaoquActivity.this, "" + jsonBean.get(options1).getRegion_name() +
//                            jsonBean.get(options1).getS_list().get(option2).getRegion_name() +
//                            jsonBean.get(options1).getS_list().get(option2).getSs_list().get(options3).getRegion_name(), Toast.LENGTH_SHORT).show();
                }
            }
        }).setSubmitColor(this.getResources().getColor(R.color.orange))//确定按钮文字颜色
                .setCancelColor(this.getResources().getColor(R.color.graynew))
                .build();//取消按钮文字颜色;
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.setSelectOptions(selected_options1, selected_options2, selected_options3);
        pvOptions.show();

    }

    private void startLocation() {
        showDialog(smallDialog);
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置需要地理位置信息
            mLocationOption.isNeedAddress();
            //     mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(true);
            mlocationClient.startLocation();
        }
    }

    Intent intentXiaoQuA = new Intent();

    private void listonclick() {
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (SourceDateList==null||adapter==null){
                    return;
                }
                if (SourceDateList.size()==0){
                    return;
                }
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    list_center.setSelection(position);
                }
                new ToolUtils(et_search, XiaoquActivity.this).closeInputMethod();
                et_search.clearFocus();
            }
        });

        list_center.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                if (type.equals("splash")) {
                    new IsChooseXiaoquDialog(XiaoquActivity.this, type, new IsChooseXiaoquDialog.LeaveMeetingDialogListener() {
                        @Override
                        public void onClick(String data) {
                            sharePrefrenceUtil.setIsChooseXiaoqu("1");
                            sharePrefrenceUtil.setXiaoQuId(SourceDateList.get(arg2).getId());
                            sharePrefrenceUtil.setXiaoQuName(SourceDateList.get(arg2).getName());
                            sharePrefrenceUtil.setIsNew(SourceDateList.get(arg2).getIs_new());
                            intentXiaoQuA = new Intent(XiaoquActivity.this, HomeActivity.class);
                            startActivity(intentXiaoQuA);
//                            HomeActivity.instant.finish();
                            finish();
                        }
                    }).show();
                } else if (type.equals("home")) {
                    new IsChooseXiaoquDialog(XiaoquActivity.this, type, new IsChooseXiaoquDialog.LeaveMeetingDialogListener() {
                        @Override
                        public void onClick(String data) {
                            sharePrefrenceUtil.setIsChooseXiaoqu("1");
                            sharePrefrenceUtil.setXiaoQuId(SourceDateList.get(arg2).getId());
                            sharePrefrenceUtil.setXiaoQuName(SourceDateList.get(arg2).getName());
                            sharePrefrenceUtil.setIsNew(SourceDateList.get(arg2).getIs_new());
                            intentXiaoQuA = new Intent(XiaoquActivity.this, HomeActivity.class);
                            startActivity(intentXiaoQuA);
//                            HomeActivity.instant.finish();
                            finish();
                        }
                    }).show();
                } else {
                    MyCookieStore.Index_notify = 1;
                    MyCookieStore.Shop_notify = 1;
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", SourceDateList.get(arg2).getName());
                    bundle.putString("id", SourceDateList.get(arg2).getId());
                    bundle.putString("all_id", id);
                    bundle.putString("all_name", name);
                    intent.putExtras(bundle);
                    setResult(200, intent);
                    MyCookieStore.Circle_notify = 1;
                    MyCookieStore.service_id = 1;
                    finish();
                }
            }
        });
        list_center.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new ToolUtils(et_search, XiaoquActivity.this).closeInputMethod();
                et_search.clearFocus();
                return false;
            }
        });
    }

    public static void startCommunityActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    //================================================

    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    String pinyin, str;

    private List<GroupMemberBean> filledData(List<CityBean> datas) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

        for (int i = 0; i < datas.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setName(datas.get(i).getName());
            sortModel.setId(datas.get(i).getId());
            sortModel.setCity_id(datas.get(i).getCity_id());
            sortModel.setRegion_id(datas.get(i).getRegion_id());
            sortModel.setIs_new(datas.get(i).getIs_new());
            //sortModel.setParentid(datas.get(i).getParentid());*/
            // 处理2级字体 “晟”
            String sub = datas.get(i).getName().substring(0, 1);
            if (sub.equals("晟")) {
                pinyin = "C";
            } else {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    pinyin = TextPinyinUtil.getInstance().getPinyin(datas.get(i).getName());
                } else {
                    pinyin = Cn2Spell.getPinYin(datas.get(i).getName());
                }
            }
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (GroupMemberBean sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || Cn2Spell.getPinYin(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    private String getPinyinFirstLetter(String name) {
        if (NullUtil.isStringEmpty(name)) {
            return "";
        }
        String first_letter = "";
        String sub = name.substring(0, 1);
        if (sub.equals("晟")) {
            first_letter = "C";
        } else {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                first_letter = TextPinyinUtil.getInstance().getPinyin(name);
            } else {
                first_letter = Cn2Spell.getPinYin(name);
            }
        }
        first_letter = first_letter.substring(0, 1).toUpperCase();
        return first_letter;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    //=====================================
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("name", "");
            bundle.putString("id", "");
            bundle.putString("all_id", "");
            bundle.putString("all_name", "");
            intent.putExtras(bundle);
            setResult(1111, intent);
            XiaoquActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "region.json");//获取assets目录下的json文件数据

        //用Gson 转成实体
        jsonBean = parseData(JsonData);

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        for (int i = 0; i < jsonBean.size(); i++) {
            options1Items.add(jsonBean.get(i).getRegion_name());
            if (jsonBean.get(i).getRegion_name().equals(location_provice)) {
                selected_options1 = i;
            }
        }
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getS_list().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getS_list().get(c).getRegion_name();
                CityList.add(CityName);//添加城市
                //默认选中
                if (CityName.equals(location_city)) {
                    selected_options2 = c;
                }
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getS_list().get(c).getSs_list() == null
                        || jsonBean.get(i).getS_list().get(c).getSs_list().size() == 0) {
                    City_AreaList.add("");
                } else {
                    for (int i1 = 0; i1 < jsonBean.get(i).getS_list().get(c).getSs_list().size(); i1++) {
                        City_AreaList.add(jsonBean.get(i).getS_list().get(c).getSs_list().get(i1).getRegion_name());
                        if (jsonBean.get(i).getS_list().get(c).getSs_list().get(i1).getRegion_name().equals(location_district)) {
                            selected_options3 = i1;
                        }
                    }

                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<ModelRegion> parseData(String result) {//Gson 解析
        ArrayList<ModelRegion> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ModelRegion entity = gson.fromJson(data.optJSONObject(i).toString(), ModelRegion.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
        mLocationOption = null;
        if (et_search != null) {
            new ToolUtils(et_search, this).closeInputMethod();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (null != location) {
            StringBuffer sb = new StringBuffer();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");
                sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                sb.append("提供者    : " + location.getProvider() + "\n");

                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.getSatellites() + "\n");
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");

                /*sharePrefrenceUtil.setLongitude(location.getLongitude() + "");
                sharePrefrenceUtil.setAtitude(location.getLatitude() + "");*/

                //定位完成的时间
                //  sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                if (!isInitLocaion) {
                    if (NullUtil.isStringEmpty(location.getDistrict())){
//                        hideDialog(smallDialog);
//                        // tvResult.setText("定位失败，loc is null");
//                        text_city.setText("定位失败...点击重新定位");
//                        text_city.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                startLocation();
//                            }
//                        });
                    }else {
                        text_city.setText(location.getCity() + "  " + location.getDistrict());
                        text_city.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (options1Items.size() > 0) {
                                    showCityWheel();
                                    new ToolUtils(et_search, XiaoquActivity.this).closeInputMethod();
                                    et_search.clearFocus();
                                }
                            }
                        });
                        isInitLocaion = true;
                        //默认选中
                        location_provice = location.getProvince() + "";
                        location_city = location.getCity() + "";
                        location_district = location.getDistrict() + "";
                        parseJson();
                        getloactionCommunity(location.getDistrict(), 1);
                    }
                }
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
                hideDialog(smallDialog);
                // tvResult.setText("定位失败，loc is null");
                text_city.setText("定位失败...点击重新定位");
                text_city.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startLocation();
                    }
                });
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
            sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间
            //   sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

            //解析定位结果，
            String result = sb.toString();
            //tvResult.setText(result);

        } else {
            hideDialog(smallDialog);
            // tvResult.setText("定位失败，loc is null");
            text_city.setText("定位失败...点击重新定位");
            text_city.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLocation();
                }
            });
        }
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == COARSE_LOCATION_REQUEST_CODE) {
            if (PermissionUtils.checkPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //调用方法
                startLocation();
            } else {
                // Permission Denied
                Toast.makeText(this, "不能打开定位", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        new ToolUtils(et_search, XiaoquActivity.this).closeInputMethod();
        super.finish();
    }
}
