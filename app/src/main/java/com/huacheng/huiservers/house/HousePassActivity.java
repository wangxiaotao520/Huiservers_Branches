package com.huacheng.huiservers.house;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajb.opendoor.UnLockCallBack;
import com.ajb.opendoor.UnlockHelper;
import com.ajb.opendoor.data.api.AjbInterface;
import com.ajb.opendoor.data.api.BleCodeCallBack;
import com.ajb.opendoor.data.api.GusetCodeCallBack;
import com.ajb.opendoor.data.bean.BleCodeRsp;
import com.ajb.opendoor.data.bean.GuestCodeRsp;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.ImgShareDialog;
import com.huacheng.huiservers.utils.AesUtils;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2018/3/23 08:49
 * 功能描述:Huiservers
 */
public class HousePassActivity extends BaseUI {

    UnlockHelper unlockHelper;
    @BindView(R.id.txt_time)
    TextView mTxtTime;
    private List<byte[]> bleCodes;

    Bitmap bitmap;
    private String huose;

    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.tv_pass)
    TextView mTvPass;
    @BindView(R.id.txt_two)
    TextView mTxtTwo;
    @BindView(R.id.linear)
    LinearLayout linear;
    String ajb_type, ajb_tagname;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_pass);
        ButterKnife.bind(this);
        //       SetTransStatus.GetStatus(this);


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
        } else {//为2是记录跳转进来的
            ajb_tagname = intent.getStringExtra("ajb_tagname");
            mTvPass.setText(ajb_tagname);
        }

        mTitleName.setText("通行码");
        mTxtTwo.setText(Html.fromHtml("2.请先按<font color='#FF6A24'>“*” “#”</font>，当显示屏出现请输入访客码，再输入6位数"));


        mLinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getdata() {
        huose = intent.getStringExtra("community") + intent.getStringExtra("building") + intent.getStringExtra("room_code");
        getUnlockCode();
        unlockHelper = new UnlockHelper(this);
        unlockHelper.setOnUnlockListener(new UnLockCallBack() {
            @Override
            public void onUnlockResult(int i) {
                Toast.makeText(HousePassActivity.this, "" + i, Toast.LENGTH_SHORT).show();
            }
        });
        unlockHelper.init();
        // unlockHelper.startBleScan();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", intent.getStringExtra("mobile"));
        params.put("estatecode", intent.getStringExtra("community"));
        params.put("housecode", intent.getStringExtra("building") + intent.getStringExtra("room_code"));
        params.put("isQRorTemPass", "1");
        params.put("guestName", intent.getStringExtra("name"));
        String addr = "http://47.104.92.9:8080";

        AjbInterface.getGuestCode(addr, params, new GusetCodeCallBack() {

            @Override
            public void onCallBack(GuestCodeRsp guestCodeRsp) {
                Log.e("qweqeqeqeq", guestCodeRsp.code + guestCodeRsp.msg);
                if (guestCodeRsp.status) {
                    mTvPass.setText(guestCodeRsp.data.tempPass);

           /*         linear.setDrawingCacheEnabled(true);
                    linear.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    linear.layout(0, 0, linear.getMeasuredWidth(), linear.getMeasuredHeight());
                    bitmap = Bitmap.createBitmap(linear.getDrawingCache());
                    linear.setDrawingCacheEnabled(false);*/

                    bitmap = Bitmap.createBitmap(linear.getMeasuredWidth(),
                            linear.getMeasuredHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas c = new Canvas(bitmap);
                    linear.draw(c);
                    //imageView1.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void getUnlockCode() {
        /**
         *
         * 参数estateAndHouseCipher：楼栋号密文 6位小区号+8位楼房号+两位随机数（例子中123456 +
         * 22020202+00）组成16位明文字符串，
         * 用biguiyuananjubao作为密钥对明文字符串进行加密，产生的密文进行urlsafeBase64进行编码
         * 本例子中在java中直接加密，个人建议在你们服务端直接加密或者用jni加密防止反编译。
         * 参数：key 访问接口的口令 固定值（70981a92bad3442dcfcb295e5756e596c850210810fe728f6e8c1783df6b495e）
         */

        String addr = "http://47.104.92.9:8080";
        String houseNo = huose;
        String key = "70981a92bad3442dcfcb295e5756e596c850210810fe728f6e8c1783df6b495e";

        String ram = System.currentTimeMillis() + "";
        ram = ram.substring(ram.length() - 2);
        String estateAndHouseCipher = AesUtils.aesEncrypt(houseNo + ram);
        estateAndHouseCipher = estateAndHouseCipher.substring(0,
                estateAndHouseCipher.indexOf("="));
        HashMap<String, String> params = new HashMap<>();
        params.put("estateAndHouseCipher", estateAndHouseCipher);
        params.put("key", key);
        AjbInterface.getBleCodes(addr, params, new BleCodeCallBack() {
            @Override
            public void onCallBack(BleCodeRsp bleCodeRsp) {
                if (bleCodeRsp.status) {
                    Log.i("sdasdaa", bleCodeRsp.msg + "==" + bleCodeRsp.code + "==" + bleCodeRsp.data.get(0));
                    bleCodes = bleCodeRsp.data;
                }
            }
        });
    }

    @OnClick({R.id.lin_left, R.id.txt_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.txt_btn://分享
                Intent intent = new Intent();
                intent.setClass(HousePassActivity.this, ImgShareDialog.class);
                intent.putExtra("url", "");
                MyCookieStore.bitmap = bitmap;
                startActivityForResult(intent, 1);
              /*  if(bleCodes != null && bleCodes.size() > 0){
                    Toast.makeText(HousePassActivity.this,"ok",Toast.LENGTH_SHORT).show();
                    unlockHelper.unLock(bleCodes);
                }*/
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
                ShareUtils.shareImg(HousePassActivity.this,
                        bitmap, SHARE_MEDIA.QQ);
            } else if (type.equals("qqzone")) {
                ShareUtils.shareImg(HousePassActivity.this, bitmap, SHARE_MEDIA.QZONE);
            } else if (type.equals("wei")) {
                ShareUtils.shareImg(HousePassActivity.this, bitmap, SHARE_MEDIA.WEIXIN);
            } else if (type.equals("wei_c")) {
                ShareUtils.shareImg(HousePassActivity.this, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        }
    }

}
