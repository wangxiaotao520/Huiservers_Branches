package com.huacheng.huiservers.shop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.WaitDIalog;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.uploadimage.GlideImageLoader;
import com.huacheng.huiservers.uploadimage.ImagePickerAdapter;
import com.huacheng.huiservers.uploadimage.SelectDialog;
import com.huacheng.huiservers.utils.Bimp;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huacheng.huiservers.R.id.recyclerView;


/**
 * 类：
 * 时间：2017/10/14 08:56
 * 功能描述:Huiservers
 */
public class NewPingJiaActivity extends BaseUI implements ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private Dialog WaitDialog;
    @BindView(R.id.iv_title)
    ImageView mIvTitle;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edt_content)
    EditText mEdtContent;
    @BindView(R.id.room_ratingbar)
    RatingBar mRoomRatingbar;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.txt_bin)
    TextView mTxtBin;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 4;//允许选择图片最大数
    ShopProtocol protocol = new ShopProtocol();
    private String o_id, p_id, p_info_id, price, numer, img, p_title;
    float rating1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("item_detete_id", "");
                    intent.putExtras(bundle);
                    setResult(5, intent);
                    finish();
                    XToast.makeText(NewPingJiaActivity.this, "评价成功", XToast.LENGTH_SHORT).show();
                    break;
                case 2:
                    mTxtBin.setText("提交");
                    mTxtBin.setClickable(true);
                    String strmsg = (String) msg.obj;
                    XToast.makeText(NewPingJiaActivity.this, strmsg, XToast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pingjia);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        mTitleName.setText("评价");
        o_id = this.getIntent().getExtras().getString("o_id");
        p_id = this.getIntent().getExtras().getString("p_id");
        p_info_id = this.getIntent().getExtras().getString("p_info_id");
        price = this.getIntent().getExtras().getString("price");
        numer = this.getIntent().getExtras().getString("numer");
        img = this.getIntent().getExtras().getString("img");
        p_title = this.getIntent().getExtras().getString("p_title");
        mTvTitle.setText("已选:" + p_title + " x " + numer);
        Glide
                .with(this)
                .load(MyCookieStore.URL + img)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_girdview)
                .into(mIvTitle);
        initImagePicker();
        initWidget();
        mRoomRatingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating1 = mRoomRatingbar.getRating();
            }
        });

    }

    private void getsubmint() {//提交评价
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("oid", o_id);//订单id
        params.addBodyParameter("p_id", p_id);//商品id
        params.addBodyParameter("order_info_id", p_info_id);//商品信息id
        params.addBodyParameter("score", String.valueOf(rating1));
        params.addBodyParameter("pic_num", selImageList.size() + "");
        params.addBodyParameter("description", mEdtContent.getText().toString());
        if (ApiHttpClient.TOKEN != null && ApiHttpClient.TOKEN_SECRET != null) {
            params.addBodyParameter("token", ApiHttpClient.TOKEN + "");
            params.addBodyParameter("tokenSecret", ApiHttpClient.TOKEN_SECRET + "");
        }
        // 以for循环方式上传多张图片， Bimp.tempSelectBitmap为存放图片集合
        for (int i = 0; i < selImageList.size(); i++) {
            String path = selImageList.get(i).path;
            File filepaths = new File(path);
            try {
                ToolUtils.compressBmpToFile(Bimp.revitionImageSize(path), filepaths);
            } catch (IOException e) {
                e.printStackTrace();
            }
            params.addBodyParameter("scoreimg" + i, filepaths);

        }
        utils.configCookieStore(MyCookieStore.cookieStore);
        utils.configCurrentHttpCacheExpiry(1000 * 10);
        utils.configTimeout(1000 * 5);
        utils.send(HttpMethod.POST, info.shopping_order_score, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                hideDialog(smallDialog);
                mTxtBin.setText("提交中");
                mTxtBin.setClickable(true);

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                hideDialog(smallDialog);
                System.out.println("arg0===" + arg0.result);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(arg0.result);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    String strmsg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        System.out.println("666666");
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = strmsg;
                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    @OnClick({R.id.lin_left, R.id.txt_bin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("item_detete_id", "");
                intent.putExtras(bundle);
                setResult(1111, intent);
                finish();
                break;
            case R.id.txt_bin://提交
                if (String.valueOf(rating1).equals("0") || String.valueOf(rating1).equals("")) {
                    XToast.makeText(this, "评价星级不能为空", XToast.LENGTH_SHORT).show();
                } else {
                    WaitDialog = WaitDIalog.createLoadingDialog(this, "正在加载...");
                    getsubmint();
                    mTxtBin.setText("提交中");
                    mTxtBin.setClickable(false);
                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("item_detete_id", "");
            intent.putExtras(bundle);
            setResult(1111, intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
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
                                Intent intent = new Intent(NewPingJiaActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(NewPingJiaActivity.this, ImageGridActivity.class);
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
}
