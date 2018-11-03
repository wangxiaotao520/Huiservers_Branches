package com.huacheng.huiservers.openDoor;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajb.opendoor.UnLockCallBack;
import com.ajb.opendoor.UnlockHelper;
import com.ajb.opendoor.data.api.AjbInterface;
import com.ajb.opendoor.data.api.BleCodeCallBack;
import com.ajb.opendoor.data.bean.BleCodeRsp;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.utils.AesUtils;
import com.huacheng.huiservers.utils.PermissionUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.SetTransStatus;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hwh on 2018/3/31.
 */

public class OpenLanActivity extends BaseUI implements UnLockCallBack {

    UnlockHelper unlockHelper;
    private List<byte[]> bleCodes;
    private String huose;

    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    String room_id, community, building, room_code;

    public static final int REQUEST_PERMISSION_LOCATION = 88;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lan);
        ButterKnife.bind(this);
        SetTransStatus.GetStatus(this);
        mTitleName.setText("蓝牙开门");
        Intent intent = getIntent();
        room_id = intent.getStringExtra("room_id");
        getResult();
        // huose = intent.getStringExtra("community") + intent.getStringExtra("building") + intent.getStringExtra("room_code");
        unlockHelper = new UnlockHelper(this);
        unlockHelper.setOnUnlockListener(this);
        unlockHelper.init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(OpenLanActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
        } else {
            unlockHelper.startBleScan();
        }
    }


    public void onGetCode(View v) {
        getUnlockCode();
    }

    public void onUnlock(View v) {
        unlockCode();
    }

    private void getResult() {//判断邀请功能时候开启
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(info.checkIsAjb, params, this) {


            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject, jsonData;
                try {
                    jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        jsonData = new JSONObject(data);
                        community = jsonData.getString("community");
                        building = jsonData.getString("building");
                        room_code = jsonData.getString("room_code");
                        huose = community + building + room_code;
                        //Toast.makeText(OpenLanActivity.this, "########" + huose, Toast.LENGTH_SHORT).show();
                        System.out.println("**********" + huose);
                        getUnlockCode();

                    } else {
                        XToast.makeText(OpenLanActivity.this, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
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
        AjbInterface.getBleCodes(addr, params, new BleCodeCallBack() { //获取开锁吗
            @Override
            public void onCallBack(BleCodeRsp bleCodeRsp) {
                if (bleCodeRsp.status) {
                    Log.i("sdasdaa", bleCodeRsp.msg + "==" + bleCodeRsp.code + "==" + bleCodeRsp.data.get(0));
                    bleCodes = bleCodeRsp.data;
                    unlockCode();//开门
                } else {
                    //textView.setVisibility(View.VISIBLE);

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    }.start();
                }
            }
        });
    }

    @OnClick({R.id.lin_left, R.id.imageView, R.id.tv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.imageView:
              /*  if (bleCodes != null && bleCodes.size() > 0) {
                    Toast.makeText(OpenLanActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    unlockHelper.unLock(bleCodes); //蓝牙开门
                }*/
                break;
            default:
                break;

        }
    }

    private void unlockCode() {
        if (bleCodes != null && bleCodes.size() > 0) {
            // Toast.makeText(OpenLanActivity.this, "ok****************", Toast.LENGTH_SHORT).show();
            unlockHelper.unLock(bleCodes);
        }
    }

    @Override
    public void onUnlockResult(int i) {
   //     Toast.makeText(OpenLanActivity.this, "########" + i, Toast.LENGTH_SHORT).show();
        if (i == 1 || i == 5) {
            //textView.setVisibility(View.VISIBLE);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }.start();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (PermissionUtils.checkPermissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //调用方法
                unlockHelper.startBleScan();
            } else {
                // Permission Denied
                Toast.makeText(this, "不能打开定位，无法进行开门", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
