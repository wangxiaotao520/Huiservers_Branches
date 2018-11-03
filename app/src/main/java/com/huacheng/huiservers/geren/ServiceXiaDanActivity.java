package com.huacheng.huiservers.geren;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.New_AddressActivity;
import com.huacheng.huiservers.center.ServiceParticipateActivity;
import com.huacheng.huiservers.dialog.ShopTagDiglog1;
import com.huacheng.huiservers.dialog.tabGround.TagContainerLayout;
import com.huacheng.huiservers.geren.bean.AddressBean;
import com.huacheng.huiservers.geren.bean.GerenBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.GerenProtocol;
import com.huacheng.huiservers.protocol.ServiceProtocol;
import com.huacheng.huiservers.uploadimage.GlideImageLoader;
import com.huacheng.huiservers.uploadimage.ImagePickerAdapter;
import com.huacheng.huiservers.uploadimage.SelectDialog;
import com.huacheng.huiservers.utils.Bimp;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.OSUtils;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.MyReceiver;
import com.huacheng.huiservers.view.ScrollEditText;
import com.huacheng.huiservers.view.SwitchButton;
import com.huacheng.huiservers.view.WheelView;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

import static com.huacheng.huiservers.R.id.recyclerView;

public class ServiceXiaDanActivity extends BaseUI implements OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    private TextView title_name, txt_two, txt_address,
            txt_right, tv_door_time, txt_mobile, txt_name;
    private ImageLoader imageLoader;
    private LinearLayout lin_dianqi, lin_guzhang, lin_start_time, lin_end_time, ll_cnm, lin_address2, ll_popup, linear_date, lin_left, lin_yesaddress, lin_noadress;
    private View v_head_line;
    private EditText edt_content;
    private PopupWindow pop = null;
    private RelativeLayout title_rel, parent, rel_time_segment, rl_address;
    private String person_name, person_mobile, person_address, tag_id, strData;//group_name
    private String xiaoquId, xiaoquName = "";
    private String switch_str = "1";
    private String login_type, door_time_str, door_timeRange, door_yearMonthDay, door_startTime, door_endTime;
    private String wheelItem = "";
    private static String[] time2 = new String[0];

    private GerenProtocol protocol = new GerenProtocol();

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    public static ServiceXiaDanActivity instant;
    // 一级
    private ArrayList<String> mListDate = new ArrayList<String>();
    // 二级
    private ArrayList<ArrayList<String>> mListTime1 = new ArrayList<ArrayList<String>>();
    // 三级
    private ArrayList<ArrayList<ArrayList<String>>> mListArea = new ArrayList<ArrayList<ArrayList<String>>>();

    private SharePrefrenceUtil sharePrefrenceUtil;
    private SharedPreferences preferencesLogin;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;//允许选择图片最大数
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    SwitchButton switch_status;

    private static final String TAG = ServiceXiaDanActivity.class.getSimpleName();

    String replaceParts = "0";
    String cate_idnew = "";
    String xiadanAddressID = "";
    /**
     * 日历定义
     */
    Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private static final String[] TIMES = new String[]{"08:00-08:59", "09:00-09:59", "10:00-10:59", "11:00-11:59", "12:00-12:59", "13:00-13:59", "14:00-14:59", "15:00-15:59", "16:00-16:59", "17:00-17:59", "18:00-18:59", "19:00-19:59", "20:00-20:59", "21:00-21:59", "22:00-22:59"};
    List<GerenBean> gerenBeens;
    private static final int TAKE_PICTURES = 0x000001;

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(true);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);*/
        //  RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void init() {
        super.init();
        instant = this;
        setContentView(R.layout.service_geren_xiadan3);
        //      SetTransStatus.GetStatus(this);// 系统栏默认为黑色
        title_name = (TextView) findViewById(R.id.title_name);
        title_rel = (RelativeLayout) findViewById(R.id.title_rel);
        title_rel.setBackgroundColor(getResources().getColor(R.color.white));
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        txt_right = (TextView) findViewById(R.id.txt_right);
        v_head_line = (View) findViewById(R.id.v_head_line);
//        txt_one = (TextView) findViewById(R.id.txt_one);
        ll_cnm = (LinearLayout) findViewById(R.id.ll_nsm);
        lin_dianqi = (LinearLayout) findViewById(R.id.lin_dianqi);
        txt_two = (TextView) findViewById(R.id.txt_two);
        edt_content = (EditText) findViewById(R.id.edt_content);
        tv_door_time = (TextView) findViewById(R.id.tv_door_time);
        linear_date = (LinearLayout) findViewById(R.id.linear_date);
        switch_status = (SwitchButton) findViewById(R.id.switch_status);
        rel_time_segment = (RelativeLayout) findViewById(R.id.rel_time_segment);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        lin_yesaddress = (LinearLayout) findViewById(R.id.lin_yesaddress);
        lin_noadress = (LinearLayout) findViewById(R.id.lin_noadress);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_name = (TextView) findViewById(R.id.txt_name);
        // Assigment
        // 提交
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");

        lin_yesaddress.setVisibility(View.GONE);
        lin_noadress.setVisibility(View.VISIBLE);
        txt_right.setVisibility(View.VISIBLE);
        txt_right.setTextColor(getResources().getColor(R.color.colorPrimary));
        txt_right.setText("提交");
        txt_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.t_18sp));
        // 提交
        sharePrefrenceUtil = new SharePrefrenceUtil(this);
        xiaoquId = sharePrefrenceUtil.getXiaoQuId();
        xiaoquName = sharePrefrenceUtil.getXiaoQuNanme();
//        group_id = getIntent().getExtras().getString("id");
        title_name.setText("维修");
//        group_name = getIntent().getExtras().getString("name");

        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
        }
        txt_two.setText("选择维修类目");
        // 图片上传
//        img();
        //1018
        getDefaultAddress();
        ButterKnife.bind(this);
        initImagePicker();
        initWidget();
        switch_status.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    switch_str = "1";
                } else {
                    switch_str = "2";
                }
            }
        });
        // Listener
        lin_left.setOnClickListener(this);
        lin_dianqi.setOnClickListener(this);
        txt_right.setOnClickListener(this);
        rel_time_segment.setOnClickListener(this);
        rl_address.setOnClickListener(this);
    }

    //获取维修收货地址
    private void getDefaultAddress() {
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("c_id", xiaoquId);//
        showDialog(smallDialog);
        new HttpHelper(info.submit_repair_before, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                AddressBean address = new ServiceProtocol().getAddress(json);
                if (address != null) {
                    if (TextUtils.isEmpty(address.getAddress_id())) {
                        lin_yesaddress.setVisibility(View.GONE);
                        lin_noadress.setVisibility(View.VISIBLE);
                    } else {
                        lin_yesaddress.setVisibility(View.VISIBLE);
                        lin_noadress.setVisibility(View.GONE);

                        txt_address.setText(address.getAddress());
                        xiadanAddressID = address.getAddress_id();
                        txt_name.setText(address.getContact());
                        txt_mobile.setText(address.getMobile());
                    }
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
    protected void onResume() {
        super.onResume();
        //清除角标（华为）
        if (OSUtils.getSystemBrand() == OSUtils.SYSTEM_HUAWEI) {
            JPushInterface.clearAllNotifications(this);
            MyReceiver.setBadgeOfHuaWei(this, 0);
        }

        mListDate.clear();
        // 选择日期开始啦啦啦~~~~~~~~~~~~~~~~~~~
        for (int i = 0; i < 7; i++) {
            getOldDate(i);
        }

    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("SimpleDateFormat")
    public String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int hour = t.hour;
        int minute = t.minute;
        ArrayList<String> options2Items_01 = new ArrayList<String>();
        ArrayList<ArrayList<String>> options3Items_01 = new ArrayList<ArrayList<String>>();

        if (distanceDay == 0) {
            if (0 < hour && hour < 22) {// 18
                mListDate.add("今天");
                for (int i = hour; i < 23; i++) { // 19
                    ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                    /*if (minute < 30 && i == hour) {
                        options2Items_01.add(i + ":00");// 添加二级
						options3Items_01_01.add("");
					} else {
						options2Items_01.add(i + ":00");// 添加二级
						options3Items_01_01.add("");
						options3Items_01_01.add("");
					}*/
                    if (i > hour) {
                        options2Items_01.add(i + ":00");// 添加二级
                        options3Items_01_01.add("");
                    }
                    options3Items_01.add(options3Items_01_01);
                }
                mListTime1.add(options2Items_01);
                mListArea.add(options3Items_01);
            }

        } else if (distanceDay == 1) {
            mListDate.add("明天");
            for (int i = 8; i < 23; i++) {// 19
                options2Items_01.add(i + ":00");// 添加二级
                ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                options3Items_01_01.add("");
                options3Items_01_01.add("");
                options3Items_01.add(options3Items_01_01);
            }
            mListTime1.add(options2Items_01);
            mListArea.add(options3Items_01);
        } else if (distanceDay == 2) {
            mListDate.add("后天");
            for (int i = 8; i < 23; i++) {// 19
                options2Items_01.add(i + ":00");// 添加二级
                ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                options3Items_01_01.add("");
                options3Items_01_01.add("");
                options3Items_01.add(options3Items_01_01);
            }
            mListTime1.add(options2Items_01);
            mListArea.add(options3Items_01);
        } else {
            mListDate.add(dft.format(endDate));
            for (int i = 8; i < 23; i++) {
                options2Items_01.add(i + ":00");// 添加二级
                ArrayList<String> options3Items_01_01 = new ArrayList<String>();
                options3Items_01_01.add("");
                options3Items_01_01.add("");

                options3Items_01.add(options3Items_01_01);
            }
            mListTime1.add(options2Items_01);
            mListArea.add(options3Items_01);
        }

        // LogUtil.d("前7天==" + dft.format(endDate));
        return dft.format(endDate);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(ServiceXiaDanActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(ServiceXiaDanActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 获取分类列表
     */
    private void gettype() {
        Url_info info = new Url_info(this);
        showDialog(smallDialog);
        new HttpHelper(info.repair_type, new RequestParams(), this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                gerenBeens = protocol.getRepairType(json);
                if (gerenBeens != null) {
                    ShopTagDiglog1.Builder builder = new ShopTagDiglog1.Builder(ServiceXiaDanActivity.this, new ShopTagDiglog1.Builder.IShopTagDialogEventListener() {
                        @Override
                        public void shopTagDialogEvent(String valueYouWantToSendBackToTheActivity, String id) {
                            txt_two.setHint("");
                            txt_two.setText(valueYouWantToSendBackToTheActivity);
                            cate_idnew = id;
                            Log.e("service", "typeId" + id);
                        }
                    });
                    builder
                            .setBanViewColor(new TagContainerLayout.ViewColor())
                            .setDefaultViewColor(new TagContainerLayout.ViewColor(ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.white), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.graynew), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.graynew)))
                            .setClickViewColor(new TagContainerLayout.ViewColor(ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.colorPrimary), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.colorPrimary), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.colorPrimary)))
//                            .setClickViewColor(new TagContainerLayout.ViewColor(ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.clickBackGroundColor), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.clickBackGroundColor), ContextCompat.getColor(ServiceXiaDanActivity.this, R.color.white)))
                            .setTagBean(gerenBeens)
                            .create().show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onClick(View arg0) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.parent:
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
            case R.id.item_popupwindows_camera:// 拍照
                if (ContextCompat.checkSelfPermission(ServiceXiaDanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // 申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                    ActivityCompat.requestPermissions(ServiceXiaDanActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    photo();
                    pop.dismiss();
                    ll_popup.clearAnimation();
                }
                break;
            case R.id.item_popupwindows_Photo:// 获取照片
//			verifyStoragePermissions(this);
                break;
            case R.id.item_popupwindows_cancel:
                pop.dismiss();
                ll_popup.clearAnimation();
                break;
            case R.id.lin_dianqi:// 一级数据
                gettype();
                break;
            case R.id.rel_time_segment:
//                final Calendar calendar = Calendar.getInstance(Locale.CHINA);
//                final int year = calendar.get(Calendar.YEAR);
//                final int month = calendar.get(Calendar.MONTH);
//                final int day = calendar.get(Calendar.DAY_OF_MONTH);
//                final DatePickerDialog picker = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, null, year, month, day);
//                picker.setCancelable(true);
//                picker.setCanceledOnTouchOutside(true);
//
//                //设置时间最大范围
//                Time t = new Time();
//                t.setToNow();
//                picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
//
//                //设置时间最小范围
//                Date date = new Date();
//                calendar.setTime(date);
//                calendar.add(Calendar.MONTH, 1);
//
//                date = calendar.getTime();
//                picker.getDatePicker().setMaxDate(date.getTime());
//
//                picker.setButton(DialogInterface.BUTTON_POSITIVE, "继续",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //确定的逻辑代码
//                                DatePicker mDatePicker = picker.getDatePicker();
//                                int mYear = mDatePicker.getYear();
//                                int mMonth = mDatePicker.getMonth();
//                                int mDayOfMonth = mDatePicker.getDayOfMonth();
//
//                                //选择时间
//                                try {
//                                    selectTime(mYear, mMonth, mDayOfMonth);
//                                } catch (java.text.ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        });
//                picker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //取消什么也不用做
//                            }
//                        });
//                picker.show();
                showOptionDialog();

                break;
            case R.id.txt_right:
                preferencesLogin = this.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    /*SharedPreferences.Editor editor = preferencesLogin.edit();
                    editor.putString("login_shop", "shop_login");
                    editor.commit();*/
                    startActivity(new Intent(this, LoginVerifyCode1Activity.class));
                } else {
                    //如果是登录状态，则进入条件判断
                    if (txt_two.getText().toString().contains("选择")) {
                        if (cate_idnew.equals("")) {
                            XToast.makeText(this, "请选择维修分类", XToast.LENGTH_SHORT).show();
                        }

                    } else if (edt_content.getText().toString().equals("")) {
                        XToast.makeText(this, "请填写维修的其他要求", XToast.LENGTH_SHORT).show();
                    } else if (tv_door_time.getText().toString().equals("")) {
                        XToast.makeText(this, "请选择希望上门时间", XToast.LENGTH_SHORT).show();
                    } else if (lin_noadress.getVisibility() == View.VISIBLE || TextUtils.isEmpty(txt_address.getText().toString()) || TextUtils.isEmpty(txt_mobile.getText().toString())
                            || TextUtils.isEmpty(txt_name.getText().toString())) {
                        XToast.makeText(this, "地址不能为空", XToast.LENGTH_SHORT).show();
                    } else {
                        try {
                            txt_right.setClickable(false);
                            getSubmit();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case R.id.rl_address:
                preferencesLogin = this.getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(this, LoginVerifyCode1Activity.class));
                } else {
                    intent = new Intent(this, New_AddressActivity.class);
                    bundle.putString("address", "serviceDan");//shopyes
                    bundle.putString("type", "order");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 200);
                }

                break;
            default:
                break;
        }
    }

    /**
     * 选择日期dialog
     */
    private void showOptionDialog() {
        try {
            //起始时间
            Calendar calendar_start = Calendar.getInstance();
            calendar_start.setTime(new Date());
            //结束时间
            Calendar calendar_end =Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String next_year =(calendar.get(Calendar.YEAR)+1)+"/01/01";
            long end = simpleDateFormat.parse(next_year).getTime();
            calendar_end.setTime(new Date(end));

            TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    // Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                    if (date!=null){
                        long time = date.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        String format = sdf.format(time);
                        String[] split = format.split("/");
                        selectTime(split[0],split[1],split[2]);

                    }
                }
            }).setSubmitColor(this.getResources().getColor(R.color.orange))//确定按钮文字颜色
                    .setCancelColor(this.getResources().getColor(R.color.graynew))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setRangDate( calendar_start,calendar_end)
                    .build();



            pvTime.setDate(calendar_start);


            pvTime.show();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择具体日期
     * @param year
     * @param month
     * @param day
     */
    private void selectTime(final String year, final String month, final String day) {
        final ArrayList<String> options1Items = new ArrayList<>();
        try {
            String[] time = findTime(year + "-" + (month) + "-" + month);
            for (int i = 0; i < time.length; i++) {
                options1Items.add(time[i]);
            }
            OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, View v) {
                    wheelItem = options1Items.get(options1);
                    tv_door_time.setText((month) + "-" + day + " " + wheelItem);
                    door_yearMonthDay = year + "-" + (month) + "-" + day;
                    //拆分时间段
                    String[] start_end_time = wheelItem.split("-");
                    door_startTime = door_yearMonthDay + " " + start_end_time[0];
                    door_endTime = door_yearMonthDay + " " + start_end_time[1];

                }
            }).setSubmitColor(this.getResources().getColor(R.color.orange))//确定按钮文字颜色
                    .setCancelColor(this.getResources().getColor(R.color.graynew))
                    .build();//取消按钮文字颜色;
            pvOptions.setPicker(options1Items);
            pvOptions.setSelectOptions(0);
            pvOptions.show();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择时间
     *
     * @param mYear
     * @param mMonth
     * @param mDayOfMonth
     */
    public void selectTime(final int mYear, final int mMonth, final int mDayOfMonth) throws java.text.ParseException {
        View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        wv.setOffset(2);

        time2 = findTime(mYear + "-" + (mMonth + 1) + "-" + mDayOfMonth);
        wv.setItems(Arrays.asList(time2));
        wv.setSeletion(0);
        wheelItem = time2[0];

        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                wheelItem = item;
                Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialog)
                .setTitle("选择时间")
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_door_time.setText((mMonth + 1) + "-" + mDayOfMonth + " " + wheelItem);
                        door_time_str = mYear + "-" + (mMonth + 1) + "-" + mDayOfMonth + " " + wheelItem;
                        //
                        door_yearMonthDay = mYear + "-" + (mMonth + 1) + "-" + mDayOfMonth;
                        door_timeRange = wheelItem;
                        //拆分时间段
                        String[] start_end_time = wheelItem.split("-");
                        door_startTime = door_yearMonthDay + " " + start_end_time[0];
                        door_endTime = door_yearMonthDay + " " + start_end_time[1];

//                        Toast.makeText(ServiceXiaDanActivity.this, mYear + "年" + (mMonth+1) + "月" + mDayOfMonth + "日 "  + wheelItem, Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight()/2);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth()*0.8);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效
    }


    //findTime()int distanceDay
    private static final String[] findTime(String ymd) throws java.text.ParseException {
        Time t = new Time();
        t.setToNow();
        int hour = t.hour;
        int minute = t.minute;
        int j = 0;
        int j1 = 0;
        String time2[] = new String[15];
        for (int k = 8; k < 23; k++) {
            time2[j1] = k + ":00-" + k + ":59";
            j1++;
        }
        return time2;
    }

    /**
     * 提交订单
     *
     * @throws IOException
     */
    private void getSubmit() throws IOException {
        Url_info info = new Url_info(this);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("type", cate_idnew);
        params.addBodyParameter("description", edt_content.getText().toString());
        params.addBodyParameter("pic_num", selImageList.size() + "");
        params.addBodyParameter("starttime", door_startTime);
        params.addBodyParameter("endtime", door_endTime);
        params.addBodyParameter("c_id", xiaoquId);
        params.addBodyParameter("is_replace", switch_str);//1为需要，2为不需要
        params.addBodyParameter("address", txt_address.getText().toString());//地址内容
        params.addBodyParameter("address_id", xiadanAddressID);//地址
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        // 以for循环方式上传多张图片， Bimp.tempSelectBitmap为存放图片集合
        for (int i = 0; i < selImageList.size(); i++) {
            String path = selImageList.get(i).path;
            File filepaths = new File(path);
            ToolUtils.compressBmpToFile(Bimp.revitionImageSize(path), filepaths);
            params.addBodyParameter("upload" + i, filepaths);

        }
        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.configCurrentHttpCacheExpiry(1000 * 10);
        utils.configTimeout(1000 * 5);
        showDialog(smallDialog);
        utils.send(HttpRequest.HttpMethod.POST, info.submit_repair, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        hideDialog(smallDialog);
                        try {
                            txt_right.setClickable(false);
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            String data = jsonObject.getString("data");
                            if (StringUtils.isEquals(status, "1")) {
                                Intent intent = new Intent(ServiceXiaDanActivity.this, ServiceParticipateActivity.class);
                                intent.putExtra("type", "0");
                                startActivity(intent);
                                finish();
                            } else {
                                XToast.makeText(ServiceXiaDanActivity.this, msg, XToast.LENGTH_SHORT).show();
                                txt_right.setClickable(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {
                        hideDialog(smallDialog);
                        txt_right.setText("提交");
                        txt_right.setClickable(true);
                        hideDialog(smallDialog);
                        UIUtils.showToastSafe("网络异常，请检查网络设置");
                    }
                });
    }

    public void data() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        strData = formatter.format(curDate);
        System.out.println("str--------" + strData);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
        switch (resultCode) {
            case 200://地址返回
                person_name = data.getExtras().getString("person_name");
                person_mobile = data.getExtras().getString("person_mobile");
                person_address = data.getExtras().getString("address");
                xiadanAddressID = data.getExtras().getString("address_id");
                if (!person_address.equals("")) {
                    lin_yesaddress.setVisibility(View.VISIBLE);
                    lin_noadress.setVisibility(View.GONE);
                    txt_mobile.setText(person_mobile);
                    txt_name.setText(person_name);
                    txt_address.setText(person_address);
                }
                break;
        }

    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURES);
    }

    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        } else if (v != null && (v instanceof ScrollEditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
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

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
