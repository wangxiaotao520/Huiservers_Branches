package com.huacheng.huiservers.house;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajb.opendoor.data.api.AjbInterface;
import com.ajb.opendoor.data.api.GusetCodeCallBack;
import com.ajb.opendoor.data.bean.GuestCodeRsp;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.ImgShareDialog;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2018/3/23 08:49
 * 功能描述:Huiservers
 */
public class HouseCodeActivity extends BaseUI {

    @BindView(R.id.txt_time)
    TextView mTxtTime;
    private String name;
    Bitmap bitmap;

    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.txt_btn)
    TextView mTxtBtn;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    String ajb_type,ajb_tagname;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_code);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        mTitleName.setText("二维码");
        // 获取当前时间戳再加12小时
        long curren = System.currentTimeMillis();
        curren += 12 * 60 * 60 * 1000;
        Date da = new Date(curren);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println(dateFormat.format(da));
        mTxtTime.setText("1.有效期至  " + dateFormat.format(da));
        intent = getIntent();
        ajb_type = intent.getStringExtra("ajb_type");
        if (ajb_type.equals("1")) {//为1是请求数据
            getdata();
        }else {//为2是记录跳转进来的
            ajb_tagname = intent.getStringExtra("ajb_tagname");
            Bitmap qrBitmap = generateBitmap(ajb_tagname, 800, 800);
            imageView.setImageBitmap(qrBitmap);
            linear.setDrawingCacheEnabled(true);
            linear.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            linear.layout(0, 0, linear.getMeasuredWidth(), linear.getMeasuredHeight());
            bitmap = Bitmap.createBitmap(linear.getDrawingCache());
            linear.setDrawingCacheEnabled(false);
        }



    }
    private void getdata() {
        name = intent.getStringExtra("name");
        HashMap<String, String> params = new HashMap<>();
        params.put("username", intent.getStringExtra("mobile"));
        params.put("estatecode", intent.getStringExtra("community"));
        params.put("housecode", intent.getStringExtra("building") + intent.getStringExtra("room_code"));
        params.put("isQRorTemPass", "2");
        params.put("guestName", name);
        String addr = "http://47.104.92.9:8080";

        AjbInterface.getGuestCode(addr, params, new GusetCodeCallBack() {

            @Override
            public void onCallBack(GuestCodeRsp guestCodeRsp) {
                Log.e("qweqeqeqeq", guestCodeRsp.code + guestCodeRsp.msg);
                if (guestCodeRsp.status) {
                    Bitmap qrBitmap = generateBitmap(guestCodeRsp.data.tempPass, 800, 800);
                    imageView.setImageBitmap(qrBitmap);

                    linear.setDrawingCacheEnabled(true);
                    linear.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    linear.layout(0, 0, linear.getMeasuredWidth(), linear.getMeasuredHeight());
                    bitmap = Bitmap.createBitmap(linear.getDrawingCache());
                    linear.setDrawingCacheEnabled(false);
                    // imageView1.setImageBitmap(bitmap);
                }
            }
        });
    }


    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    @OnClick({R.id.lin_left, R.id.txt_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_btn://分享
                Intent intent = new Intent();
                intent.setClass(HouseCodeActivity.this, ImgShareDialog.class);
                intent.putExtra("url", "");
                MyCookieStore.bitmap = bitmap;
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (20 == resultCode) {
            String type = data.getExtras().getString("type");
            if (type.equals("qq")) {
                ShareUtils.shareImg(HouseCodeActivity.this,
                        bitmap, SHARE_MEDIA.QQ);
            } else if (type.equals("qqzone")) {
                ShareUtils.shareImg(HouseCodeActivity.this, bitmap, SHARE_MEDIA.QZONE);
            } else if (type.equals("wei")) {
                ShareUtils.shareImg(HouseCodeActivity.this, bitmap, SHARE_MEDIA.WEIXIN);
            } else if (type.equals("wei_c")) {
                ShareUtils.shareImg(HouseCodeActivity.this, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        }
    }
}
