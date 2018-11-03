package com.huacheng.huiservers.servicenew.ui.scan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.StatusBarUtil;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.utils.TDevice;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Description:自定义条形码/二维码扫描
 * Author: badge
 * Create: 2018/9/18 17:50
 */
public class CustomCaptureActivity extends BaseActivity {

    LinearLayout linLeft;
    TextView titleName;
    RelativeLayout titleRel;
    View statusBar;
    /**
     * 条形码扫描管理器
     */
    private CustomCaptureManager mCaptureManager;

    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isStatusBar = true;
        StatusBarUtil.setTransparent(this);
        super.onCreate(savedInstanceState);

        mCaptureManager = new CustomCaptureManager(this, mBarcodeView);

        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mCaptureManager.decode();

    }

    @Override
    protected void initView() {
        mBarcodeView = findViewById(R.id.zxing_barcode_scanner);
        statusBar = findViewById(R.id.status_bar);
        titleRel = findViewById(R.id.title_rel);
        linLeft = findViewById(R.id.lin_left);
        titleName = findViewById(R.id.title_name);
        statusBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice.getStatuBarHeight(this)));
    }

    @Override
    protected void initData() {
        statusBar.setAlpha(0);
        titleName.setText("扫描二维码付款");

//        statusBar.setAlpha(0);
    }

    @Override
    protected void initListener() {
        linLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zxing_layout;
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

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureManager.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCaptureManager.onSaveInstanceState(outState);

    }

    /**
     * 权限处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mCaptureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 按键处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}