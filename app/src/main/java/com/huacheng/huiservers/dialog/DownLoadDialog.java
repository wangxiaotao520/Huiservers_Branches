package com.huacheng.huiservers.dialog;

import java.io.File;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.DownloadManagerPro;
import com.huacheng.huiservers.utils.ToastUtils;
import com.huacheng.huiservers.utils.XToast;

public class DownLoadDialog extends Activity {


    public static final String DOWNLOAD_FOLDER_NAME = "Trinea";
    public static  String DOWNLOAD_FILE_NAME = "hui.apk";

    public static  String APK_URL = "http://m.hui-shenghuo.cn/data/upload/apk/HuiServers.apk";
    public static final String KEY_NAME_DOWNLOAD_ID = "downloadId";

    private ProgressBar downloadProgress;
    private TextView downloadTip;
    private TextView downloadSize;
    private TextView downloadPrecent;

    private DownloadManager downloadManager;
    private DownloadManagerPro downloadManagerPro;
    private long downloadId = 0;

    private MyHandler handler;

    private DownloadChangeObserver downloadObserver;
    private CompleteReceiver completeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_download);

        handler = new MyHandler();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManagerPro = new DownloadManagerPro(downloadManager);

        // see android mainfest.xml,
        // accept minetype of cn.trinea.download.file
        Intent intent = getIntent();
        APK_URL = intent.getStringExtra("download_src");
        DOWNLOAD_FILE_NAME = intent.getStringExtra("file_name");
        initView();
        initData();

        downloadObserver = new DownloadChangeObserver();
        completeReceiver = new CompleteReceiver();
        /** register download success broadcast **/
        registerReceiver(completeReceiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        

        File folder = new File(DOWNLOAD_FOLDER_NAME);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(APK_URL));
        request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
                DOWNLOAD_FILE_NAME);
        request.setTitle("社区慧生活");
        request.setDescription("hui desc");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);
        // request.allowScanningByMediaScanner();
        // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // request.setShowRunningNotification(false);
        // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setMimeType("application/cn.trinea.download.file");

        downloadId = downloadManager.enqueue(request);
        /** save download id to preferences **/
      /*  PreferencesUtils.putLong(context, KEY_NAME_DOWNLOAD_ID,
                downloadId);*/
        updateView();
    
    }

    @Override
    protected void onResume() {
        super.onResume();
        /** observer download change **/
        getContentResolver().registerContentObserver(
                DownloadManagerPro.CONTENT_URI, true, downloadObserver);
        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(downloadObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(completeReceiver);
    }

    private void initView() {
        downloadProgress = (ProgressBar) findViewById(R.id.download_progress);
        downloadTip = (TextView) findViewById(R.id.download_tip);
        downloadTip.setText("正在下载，请稍后");
        downloadSize = (TextView) findViewById(R.id.download_size);
        downloadPrecent = (TextView) findViewById(R.id.download_precent);
    }



    private void initData() {
        /**
         * get download id from preferences.<br/>
         * if download id bigger than 0, means it has been downloaded, then
         * query status and show right text;
         */
   //     downloadId = PreferencesUtils.getLong(context, KEY_NAME_DOWNLOAD_ID);
        updateView();
    }

    /**
     * install app
     *
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath),
                    "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }

    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateView();
        }

    }

    class CompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * get the id of download which have download success, if the id is
             * my id and it's status is successful, then install it
             **/
            long completeDownloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId == downloadId) {
                initData();
                updateView();
                // if download successful, install apk
                if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
                    String apkFilePath = new StringBuilder(Environment
                            .getExternalStorageDirectory().getAbsolutePath())
                            .append(File.separator)
                            .append(DOWNLOAD_FOLDER_NAME)
                            .append(File.separator).append(DOWNLOAD_FILE_NAME)
                            .toString();
                    install(context, apkFilePath);
                    finish();
                }
            }
        }
    };

    public void updateView() {
        int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downloadId);
        handler.sendMessage(handler.obtainMessage(0, bytesAndStatus[0],
                bytesAndStatus[1], bytesAndStatus[2]));
    }

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    int status = (Integer) msg.obj;
                    if (isDownloading(status)) {
                        downloadProgress.setVisibility(View.VISIBLE);
                        downloadProgress.setMax(0);
                        downloadProgress.setProgress(0);
                        downloadSize.setVisibility(View.VISIBLE);
                        downloadPrecent.setVisibility(View.VISIBLE);

                        if (msg.arg2 < 0) {
                            downloadProgress.setIndeterminate(true);
                            downloadPrecent.setText("0%");
                            downloadSize.setText("0M/0M");
                        } else {
                            downloadProgress.setIndeterminate(false);
                            downloadProgress.setMax(msg.arg2);
                            downloadProgress.setProgress(msg.arg1);
                            downloadPrecent.setText(getNotiPercent(msg.arg1,
                                    msg.arg2));
                            downloadSize.setText(getAppSize(msg.arg1) + "/"
                                    + getAppSize(msg.arg2));
                        }
                    } else {
                        downloadProgress.setVisibility(View.GONE);
                        downloadProgress.setMax(0);
                        downloadProgress.setProgress(0);
                        downloadSize.setVisibility(View.GONE);
                        downloadPrecent.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

    public static final int MB_2_BYTE = 1024 * 1024;
    public static final int KB_2_BYTE = 1024;

    /**
     * @param size
     * @return
     */
    public static CharSequence getAppSize(long size) {
        if (size <= 0) {
            return "0M";
        }

        if (size >= MB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format((double) size / MB_2_BYTE))
                    .append("M");
        } else if (size >= KB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format((double) size / KB_2_BYTE))
                    .append("K");
        } else {
            return size + "B";
        }
    }

    public static String getNotiPercent(long progress, long max) {
        int rate = 0;
        if (progress <= 0 || max <= 0) {
            rate = 0;
        } else if (progress > max) {
            rate = 100;
        } else {
            rate = (int) ((double) progress / max * 100);
        }
        return new StringBuilder(16).append(rate).append("%").toString();
    }

    public static boolean isDownloading(int downloadManagerStatus) {
        return downloadManagerStatus == DownloadManager.STATUS_RUNNING
                || downloadManagerStatus == DownloadManager.STATUS_PAUSED
                || downloadManagerStatus == DownloadManager.STATUS_PENDING;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
    		XToast.makeText(DownLoadDialog.this, "正在下载，请耐心等待", XToast.LENGTH_SHORT).show();
			return true;   
		}
    	// TODO Auto-generated method stub
    	return super.onKeyDown(keyCode, event);
    }

}

