package com.huacheng.huiservers.center;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
import com.huacheng.huiservers.dialog.ImgDialog;
import com.huacheng.huiservers.dialog.ImgDialog.OnCustomDialogListener;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.huiservers.view.EditHeadImage;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyInfoActivity2 extends BaseUI implements OnClickListener {
    private static final int CAMERA = 1; // 拍照
    private static final int PHOTO = 2; // 相册
    private static final int PHOTORESOULT = 333; // 结果
    private static final int PICK_CAMERA = 4;
    private LinearLayout lin_left;
    private RelativeLayout rl_name, rl_nickname, rl_sex, rl_birthday, rl_changepwd;
    private TextView title_name, tv_name, tv_nickname, tv_nickname1, tv_sex, tv_birthday;
    private RadioGroup radio;
    private ImgDialog dialog;
    private CircularImage img_head_1;
    private String currentSelected = "1";// 默认选中为男
    private RadioButton radio_nan, radio_nv;
    private String str, strData;
    private File mFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "Gphoto.jpg");

    private File file = new File(Environment.getExternalStorageDirectory() + "/Gphoto.png");
    ShopProtocol protocol = new ShopProtocol();
    CenterProtocol cprotocol = new CenterProtocol();
    PersoninfoBean bean = new PersoninfoBean();
    BitmapUtils bitmapUtils;
    HttpUtils utils = new HttpUtils();
    DefaultHttpClient dh;
    Cookie PHPSESSID;
    String result;
    Intent intent;
    Bundle bundle = new Bundle();

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.my_info2);
        MyCookieStore.My_info = 0;
    //    SetTransStatus.GetStatus(this);// 系统栏默认为黑色
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        title_name = (TextView) findViewById(R.id.title_name);
        img_head_1 = (CircularImage) findViewById(R.id.img_head_1);
        rl_name = (RelativeLayout) findViewById(R.id.rl_name);
        rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_birthday = (RelativeLayout) findViewById(R.id.rl_birthday);
        rl_changepwd = (RelativeLayout) findViewById(R.id.rl_changepwd);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname1 = (TextView) findViewById(R.id.tv_nickname1);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        //
        title_name.setText("个人信息");
        lin_left.setOnClickListener(this);
        img_head_1.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_birthday.setOnClickListener(this);
        rl_changepwd.setOnClickListener(this);

    }

    private static int TAKE_PHOTO_REQUEST_CODE = 1;

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.img_head_1:// 修改头像
                dialog = new ImgDialog(MyInfoActivity2.this, new OnCustomDialogListener() {
                    @Override
                    public void back(String name) {
                        if (name.equals("1")) {// 拍照
                            if (ContextCompat.checkSelfPermission(MyInfoActivity2.this,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // 申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
                                ActivityCompat.requestPermissions(MyInfoActivity2.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        TAKE_PHOTO_REQUEST_CODE);

                            } else {
                                selectPicFromCamera();
                                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // 下面这句指定调用相机拍照后的照片存储的路径
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory(), "Gphoto.png")));
                                startActivityForResult(intent, CAMERA);*/
                            }
                        } else if (name.equals("2")) {// 获取相册
                            verifyStoragePermissions(MyInfoActivity2.this);
                        } else if (name.equals("3")) {// 取消

                        }
                    }
                });
                dialog.show();
                break;
            case R.id.rl_name:
                intent = new Intent(this, NameVerfityActivity.class);
                bundle.putString("name", tv_name.getText().toString());
                bundle.putString("tv_sex", tv_sex.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 11);
                break;
            case R.id.rl_nickname:
                intent = new Intent(this, NamenickVerfityActivity.class);
                bundle.putString("nickname", tv_nickname.getText().toString());
                bundle.putString("tv_sex", tv_sex.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 22);
                break;
            case R.id.rl_sex:
//                sexChoice();
                intent = new Intent(this, SexVerfityActivity.class);
                bundle.putString("sex", tv_sex.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
                break;
            case R.id.rl_birthday:
                /*intent = new Intent(this, AgeVerfityActivity.class);
                bundle.putString("age", tv_age.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 4);*/
//                Calendar calendar = null;
//                int year = 0;
//                int month = 0;
//                int day = 0;
//                String birthDay = tv_birthday.getText().toString();
//                calendar = Calendar.getInstance(Locale.CHINA);
//                if (birthDay.equals("")) {
//                    year = calendar.get(Calendar.YEAR);
//                    month = calendar.get(Calendar.MONTH);
//                    day = calendar.get(Calendar.DAY_OF_MONTH);
//                } else {
//                    if (!StringUtils.isEmpty(birthDay)) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//                        try {
//                            calendar.setTime(sdf.parse(birthDay));
//                            year = calendar.get(Calendar.YEAR);
//                            month = calendar.get(Calendar.MONTH);
//                            day = calendar.get(Calendar.DAY_OF_MONTH);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                final DatePickerDialog picker = new DatePickerDialog(this, null, year, month, day);
//                picker.setCancelable(true);
//                picker.setCanceledOnTouchOutside(true);
//                //设置时间范围
////                picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
//                picker.setButton(DialogInterface.BUTTON_POSITIVE, "继续",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //确定的逻辑代码
//                                DatePicker mDatePicker = picker.getDatePicker();
//                                int mYear = mDatePicker.getYear();
//                                int mMonth = mDatePicker.getMonth();
//                                int mDayOfMonth = mDatePicker.getDayOfMonth();
//                                getMyinfoBirthDay(mYear + "/" + (mMonth + 1) + "/" + mDayOfMonth);
////                              tv_birthday.setText(mYear + "年" + (mMonth + 1) + "月" + mDayOfMonth + "日");
//
////                              ToastUtils.showShort(MyInfoActivity2.this, mYear + "年" + (mMonth + 1) + "月" + mDayOfMonth + "日");
//                               /* new TimePickerDialog(MyInfoActivity2.this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//                                    @Override
//                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                                    }
//                                    //0,0指的是时间，true为24小时制
//                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();*/
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
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                       // Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                        if (date!=null){
                            long time = date.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                            String format = sdf.format(time);
                            getMyinfoBirthDay(format);
                        }
                    }
                }).setSubmitColor(this.getResources().getColor(R.color.orange))//确定按钮文字颜色
                    .setCancelColor(this.getResources().getColor(R.color.graynew)).build();
                String birthDay = tv_birthday.getText().toString();
                if (NullUtil.isStringEmpty(birthDay)){
                    Calendar instance = Calendar.getInstance();
                    pvTime.setDate(instance);
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date date = sdf.parse(birthDay);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        pvTime.setDate(calendar);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                pvTime.show();
                break;
            case R.id.rl_changepwd:
                intent = new Intent(this, ChangePwdVerfityActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private void selectPicFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
        startActivityForResult(intent, PICK_CAMERA);
    }

    /**
     * 修改个人信息
     *
     * @param param
     */
    private void getMyinfoBirthDay(final String param) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("birthday", param);
       /* if (file.exists()) {
            params.addBodyParameter("avatars", file);
        }
        System.out.println("currentSelected-----" + currentSelected);
        params.addBodyParameter("sex", currentSelected);
        params.addBodyParameter("birthday", txt_time.getText().toString());
        */
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        http.configCookieStore(MyCookieStore.cookieStore);
        http.send(HttpRequest.HttpMethod.POST, info.edit_center, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        hideDialog(smallDialog);
                        UIUtils.showToastSafe("网络异常，请检查网络设置");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        hideDialog(smallDialog);
                        ShopProtocol protocol = new ShopProtocol();
                        String str = protocol.setShop(arg0.result);
                        if (str.equals("1")) {
                            getinfo();
                            XToast.makeText(MyInfoActivity2.this, "修改成功", XToast.LENGTH_SHORT)
                                    .show();
                        } else {
                            XToast.makeText(MyInfoActivity2.this, str, XToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void data() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        strData = formatter.format(curDate);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getinfo();
    }

    private void getinfo() {// 个人信息
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        new HttpHelper(info.get_person_index, params, MyInfoActivity2.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = cprotocol.getinfo3(json);
                //获取头像
                if (bean.getAvatars().equals("null")) {
                } else {
                    bitmapUtils = new BitmapUtils(MyInfoActivity2.this);
                    bitmapUtils.display(img_head_1, StringUtils.getImgUrl(bean.getAvatars()));
                }
                file = new File(Environment.getExternalStorageDirectory() + "/Gphoto.png");
                if (file.exists()) {
                    file.delete();
                }
                //姓名
                if (bean.getFullname().equals("null")) {
                    tv_name.setHint("请填写姓名");
                } else {
                    tv_name.setText(bean.getFullname());
                }
                //昵称
                if (bean.getNickname().equals("null")) {
                    tv_nickname.setHint("请填写昵称");
                } else {
                    tv_nickname.setText(bean.getNickname());
                    // tv_nickname1.setText(bean.getNickname());
                }

                //获取性别
                String sex_str = bean.getSex();
                if (sex_str.equals("null")) {
                    tv_sex.setHint("请选择性别");
                } else {
                    if (sex_str.equals("1")) {
                        tv_sex.setText("男");
                    } else if (sex_str.equals("2")) {
                        tv_sex.setText("女");
                    } else {
                        tv_sex.setText("");
                    }
                }
                //获取生日
                String birthday_str = bean.getBirthday();
                if (birthday_str.equals("null")) {
                    tv_birthday.setHint("请选择出生日期");
                } else {
                    if (!StringUtils.isEmpty(birthday_str)) {
//                        birthday_str=  DateUtil.StrToDate(birthday_str);
                        tv_birthday.setText(birthday_str);
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


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public void verifyStoragePermissions(Activity activity) {

        try {
            // 检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String IMAGE_FILE_LOCATION = "file:///"
            + Environment.getExternalStorageDirectory().getPath() + "/Gphoto.jpg";
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

    private void startCameraZoom(Uri uri) {
        // TODO Auto-generated method stub
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        /*
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection*/

        startActivityForResult(intent, CROP_PICTURE);
    }

    private static final int CROP_PICTURE = 222;

    // ++++++++++++++++++++++++++++修改或更换头像start++++++++++++++++++++++++++++++++
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 11:
                result = data.getExtras().getString("name");
                tv_name.setText(result);

                break;
            case 22:
                result = data.getExtras().getString("nickname");
                tv_nickname.setText(result);
                break;
            case 3:
                result = data.getExtras().getString("sex");
                if (result.equals("1")) {
                    result = "男";
                } else if (result.equals("2")) {
                    result = "女";
                }
                tv_sex.setText(result);
                break;
            /*case 4:
                result = data.getExtras().getString("age");
                tv_age.setText(result);
                break;*/
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CAMERA:
                    startCameraZoom(Uri.fromFile(mFile));
                    break;
                case PHOTO:// 读取相册缩放图片
                    startPhotoZoom(data.getData());
                    break;
                case CROP_PICTURE:
                    try {
                        // FileOutputStream out = new FileOutputStream(file);
                            /*photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();*/
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        img_head_1.setImageBitmap(bitmap);
                        /**
                         * 上传头像 写上传头像的接口
                         */
                        uploadCameraAvatar();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case PHOTORESOULT: // 处理结果
                    if (data != null) {
                        try {
                            Bundle extras = data.getExtras();
                            if (extras != null) {
                                Bitmap photo = extras.getParcelable("data");
                                File file = new File(Environment.getExternalStorageDirectory() + "/Gphoto.png");
                                if (file.exists()) {
                                    file.delete();
                                }
                                FileOutputStream out = new FileOutputStream(file);
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.flush();
                                img_head_1.setImageBitmap(EditHeadImage.imageZoom(photo));
                                /**
                                 * 上传头像 写上传头像的接口
                                 */
                                uploadAvatar();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }

                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadCameraAvatar() {
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        if (mFile.exists()) {
            params.addBodyParameter("avatars", mFile);
        }
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        http.configCookieStore(MyCookieStore.cookieStore);
        http.send(HttpRequest.HttpMethod.POST, MyCookieStore.SERVERADDRESS + "userCenter/edit_center/", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ShopProtocol protocol = new ShopProtocol();
                        String str = protocol.setShop(arg0.result);
                        if (str.equals("1")) {
                            getinfo();
                            XToast.makeText(MyInfoActivity2.this, "修改成功", XToast.LENGTH_SHORT)
                                    .show();
                          //  MyCookieStore.My_info = 1;
                            EventBus.getDefault().post(new PersoninfoBean ());
                        } else {
                            XToast.makeText(MyInfoActivity2.this, str, XToast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        XToast.makeText(MyInfoActivity2.this, arg1, XToast.LENGTH_SHORT)
                                .show();
                    }
                });
    }


    /**
     * 上传头像
     */
    private void uploadAvatar() {
        Url_info info = new Url_info(this);
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        if (file.exists()) {
            params.addBodyParameter("avatars", file);
        }
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        http.configCookieStore(MyCookieStore.cookieStore);

        http.send(HttpRequest.HttpMethod.POST, MyCookieStore.SERVERADDRESS + "userCenter/edit_center/", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        ShopProtocol protocol = new ShopProtocol();
                        String str = protocol.setShop(arg0.result);
                        if (str.equals("1")) {
                            getinfo();
                            XToast.makeText(MyInfoActivity2.this, "修改成功", XToast.LENGTH_SHORT)
                                    .show();
                      //      MyCookieStore.My_info = 1;
                            EventBus.getDefault().post(new PersoninfoBean());
                        } else {
                            XToast.makeText(MyInfoActivity2.this, str, XToast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        XToast.makeText(MyInfoActivity2.this, arg1, XToast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void startPhotoZoom(Uri uri) {
        /*
         * Intent intent = new Intent("com.android.camera.action.CROP");
         * intent.setType("image/*"); intent.putExtra("data", data);
         * intent.putExtra("crop", "true"); // aspectX aspectY 是宽高的比例
         * intent.putExtra("aspectX", 1); intent.putExtra("aspectY", 1); //
         * outputX outputY 是裁剪图片宽高 intent.putExtra("outputX", 64);
         * intent.putExtra("outputY", 64); intent.putExtra("return-data", true);
         * startActivityForResult(intent, PHOTORESOULT);
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
      /*  intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection*/
        startActivityForResult(intent, PHOTORESOULT);
    }

    // ++++++++++++++++++++++++++++修改或更换简历头像end++++++++++++++++++++++++++++++++

    /**
     * 关闭软键盘
     */
    /*private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			// imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

			imm.hideSoftInputFromWindow(edt_name.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
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
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top
                    && event.getY() < bottom) {
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
