package com.huacheng.huiservers.cricle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.bean.ModelRefreshCircle;
import com.huacheng.huiservers.dialog.ChooseFabuDialog;
import com.huacheng.huiservers.uploadimage.GlideImageLoader;
import com.huacheng.huiservers.uploadimage.ImagePickerAdapter;
import com.huacheng.huiservers.uploadimage.SelectDialog;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.huacheng.huiservers.R.id.recyclerView;

/**
 * Created by hwh on 2018/3/16.
 */

public class FaBuActivity extends BaseUI implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private SharePrefrenceUtil sharePrefrenceUtil;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;//允许选择图片最大数
    private String type, circle_id;
    private int i = 0;


    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.ly_choose)
    LinearLayout ly_choose;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.txt_right1)
    TextView txt_right1;
    @BindView(R.id.tv_lan)
    TextView tv_lan;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private ArrayList<File> images_submit=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        ButterKnife.bind(this);
//        SetTransStatus.GetStatus(this);

        MyCookieStore.Circle_notify = 0;
        //MyCookieStore.MyCircle_notify = 0;
        sharePrefrenceUtil = new SharePrefrenceUtil(FaBuActivity.this);
        mTitleName.setText("发帖");
        txt_right1.setText("发布");
        txt_right1.setVisibility(View.VISIBLE);
        initImagePicker();
        initWidget();

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
                                Intent intent = new Intent(FaBuActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(FaBuActivity.this, ImageGridActivity.class);
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

    @OnClick({R.id.lin_left, R.id.txt_right1, R.id.ly_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                closeInputMethod();
                finish();
                break;
            case R.id.txt_right1:
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    XToast.makeText(this, "请填写内容", XToast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(circle_id)) {
                    XToast.makeText(this, "请选择发布栏目", XToast.LENGTH_SHORT).show();
                } else {
                    // 换了一种压缩图片的方式
                    try {
                        if (selImageList.size()>0) {
                            showDialog(smallDialog);
                            compressImg();
                        }else {
                            getSubmit();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.ly_choose:

                new ChooseFabuDialog(FaBuActivity.this, i, new ChooseFabuDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(View view, int position, String s, String id) {
                        tv_lan.setText(s);
                        i = position;
                        circle_id = id;
                    }
                }).show();
                break;

        }
    }

    /**
     * 压缩图片（git）
     */
    private void compressImg() {
        images_submit.clear();
        ArrayList<File> files = new ArrayList<>();
        for (int i1 = 0; i1 < selImageList.size(); i1++) {
            File file = new File(selImageList.get(i1).path);
            files.add(file);
        }
        Luban.with(this)
                .load(files)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .setFocusAlpha(false)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() { }

                    @Override
                    public void onSuccess(File file) {
                        //这个方法重复调用
                        images_submit.add(file);
                        if (images_submit.size()==selImageList.size()){
                            //完成了
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        getSubmit();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                XToast.makeText(FaBuActivity.this,"图片压缩失败", Toast.LENGTH_SHORT).show();
                                hideDialog(smallDialog);
                            }
                        });

                    }
                }).launch();

    }
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/huiservers/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
    private void getSubmit() throws IOException {// 发布圈子
        showDialog(smallDialog);
        txt_right1.setEnabled(false);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        String content = Base64.encodeToString(et_content.getText().toString().getBytes(), Base64.DEFAULT);
        params.addBodyParameter("community_id", sharePrefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_id", circle_id);
        params.addBodyParameter("img_num", selImageList.size() + "");
        params.addBodyParameter("content", content);
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
//        // 以for循环方式上传多张图片， Bimp.tempSelectBitmap为存放图片集合
//        for (int i = 0; i < selImageList.size(); i++) {
//            String path = selImageList.get(i).path;
//            File filepaths = new File(path);
//            ToolUtils.compressBmpToFile(Bimp.revitionImageSize(path), filepaths);
//            params.addBodyParameter("img" + i, filepaths);
//
//        }
        // 换了一种压缩图片的方式
        //添加新压缩
        if (images_submit.size()>0){
            for (int i1 = 0; i1 < images_submit.size(); i1++) {
                params.addBodyParameter("img"+i1,images_submit.get(i1));
            }
        }

        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.configCurrentHttpCacheExpiry(1000 * 10);
        utils.configTimeout(1000 * 5);
        utils.send(HttpRequest.HttpMethod.POST, MyCookieStore.SERVERADDRESS + "social/social_save/", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        hideDialog(smallDialog);
                        txt_right1.setEnabled(true);
                        UIUtils.showToastSafe("网络异常，请检查网络设置");
                       /* txt_right.setText("提交");
                        txt_right.setClickable(true);*/
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
//                            txt_right.setClickable(false);
                            JSONObject jsonObject = new JSONObject(arg0.result);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            String data = jsonObject.getString("data");
                            if (StringUtils.isEquals(status, "1")) {
                                MyCookieStore.Circle_notify = 1;
                                ModelRefreshCircle modelRefreshCircle = new ModelRefreshCircle();
                                modelRefreshCircle.setTab_id(circle_id);
                                EventBus.getDefault().post(modelRefreshCircle);
                                // startActivity(new Intent(NewReleaseActivity.this, ServiceParticipateActivity.class));
                                finish();
                                hideDialog(smallDialog);
                                txt_right1.setEnabled(true);
                                // 删除文件夹下的文件
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        delFolder(getPath());
                                    }
                                }).start();
                            } else {
                                XToast.makeText(FaBuActivity.this, msg, XToast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            hideDialog(smallDialog);
                            txt_right1.setEnabled(false);
                            e.printStackTrace();

                        }
                    }
                });
    }

    /**
     * 关闭软键盘
     */
    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // View v = null;
            imm.hideSoftInputFromWindow(et_content.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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
    }

    //通过dispatchTouchEvent每次ACTION_DOWN事件中动态判断非EditText本身区域的点击事件，然后在事件中进行屏蔽。
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    //删除文件夹
//param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
//param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }


}
