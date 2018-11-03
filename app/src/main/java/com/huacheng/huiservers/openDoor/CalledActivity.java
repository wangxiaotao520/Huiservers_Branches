package com.huacheng.huiservers.openDoor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajb.call.api.ICallView;
import com.ajb.call.api.TalkClient;
import com.ajb.call.utlis.CommonUtils;
import com.huacheng.huiservers.R;



public class CalledActivity extends Activity implements ICallView {

	public static final String TAG = CalledActivity.class.getName();


	TextView titleTextView;
	TextView start_btn;
	TextView stop_btn;
	TextView unlock_btn;
	TextView speaker_btn;
	TextView btn_speaker_off;
	RelativeLayout rlyt_video_area;


	private TextView space_left;
	private TextView space_right;

	private Context mContext = null;

	private TalkClient client = null;

	private PowerManager.WakeLock mWakeLock = null;
	private boolean mStayAwake = false;
	private Sensor mProximitySensor;
	private SensorManager mSensorManager;
	float PROXIMITY_THRESHOLD = 5.0f;// 距离小于5.0，则熄灭屏幕

	private static int TAKE_PHOTO_REQUEST_CODE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate()");
		mContext = this;
		setContentView(R.layout.activity_called);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Log.e(TAG, "onCreate()");
		initView();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(CalledActivity.this,
					Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				// 申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
				ActivityCompat.requestPermissions(CalledActivity.this,
						new String[]{Manifest.permission.CAMERA}, TAKE_PHOTO_REQUEST_CODE);
			}
		} else {
			client = new TalkClient(this, this);
		}
	}



	private void initView() {

		titleTextView = (TextView) findViewById(R.id.title);
		start_btn = (TextView) findViewById(R.id.btn_hang_on);
		stop_btn = (TextView) findViewById(R.id.btn_hang_off);
		space_left = (TextView) findViewById(R.id.space_left);
		space_right = (TextView) findViewById(R.id.space_right);
		unlock_btn = (TextView) findViewById(R.id.btn_unlock);
		speaker_btn = (TextView) findViewById(R.id.btn_speaker_on);
		// speaker_btn.setVisibility(View.GONE);
		btn_speaker_off = (TextView) findViewById(R.id.btn_speaker_off);
		// speaker_btn.setVisibility(View.VISIBLE);
		rlyt_video_area = (RelativeLayout) findViewById(R.id.rlyt_video_area);
		String houseNo = CommonUtils.getHouseNo(getApplicationContext());
		titleTextView.setText("访客视频");
		


		setWakeMode(getApplicationContext(), PowerManager.SCREEN_DIM_WAKE_LOCK);// 屏幕常亮
		stayAwake(true);


			titleTextView.setText("访客通话");
		//	speaker_btn.setVisibility(View.VISIBLE);
			stop_btn.setVisibility(View.VISIBLE);
			unlock_btn.setVisibility(View.VISIBLE);

			start_btn.setVisibility(View.VISIBLE);


		start_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//	speaker_btn.setVisibility(View.VISIBLE);
				start_btn.setVisibility(View.GONE);
				space_left.setVisibility(View.VISIBLE);
				space_right.setVisibility(View.VISIBLE);

				client.answer();
				start_btn.setEnabled(false);
				titleTextView.setText("访客通话");
				stop_btn.setText("结  束");
			}
		});

		stop_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop_btn.setEnabled(false);
				titleTextView.setText("访客视频");
				client.cancel();

			}
		});

		unlock_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			client.unlock();

			}
		});

	}


	private void stayAwake(boolean awake) {
		if (mWakeLock != null) {
			if (awake && !mWakeLock.isHeld()) {
				mWakeLock.acquire();
			} else if (!awake && mWakeLock.isHeld()) {
				mWakeLock.release();
			}
		}
		mStayAwake = awake;
		updateSurfaceScreenOn();
	}

	private void updateSurfaceScreenOn() {
		if (rlyt_video_area != null) {
			rlyt_video_area.setKeepScreenOn(mStayAwake);
		}
	}

	int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;

	public void setWakeMode(Context context, int mode) {

		if (mSensorManager == null) {
			mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		}
		if (mProximitySensor == null) {
			mProximitySensor = mSensorManager
					.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		}
		boolean washeld = false;
		if (mWakeLock != null) {
			if (mWakeLock.isHeld()) {
				Log.i(TAG, "mWakeLock.isHeld");
				washeld = true;
				mWakeLock.release();
			}
			mWakeLock = null;
		}

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		// mWakeLock = pm.newWakeLock(mode ,
		// mWakeLock = pm.newWakeLock(mode | PowerManager.ON_AFTER_RELEASE,
		// mWakeLock =
		// pm.newWakeLock(PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP,
		mWakeLock = pm.newWakeLock(PROXIMITY_SCREEN_OFF_WAKE_LOCK,
				CalledActivity.class.getName());
		mWakeLock.setReferenceCounted(false);

		if (washeld) {
			Log.i(TAG, "mWakeLock.acquire");
			mWakeLock.acquire();
		}
	}

	@Override
	public void onDestroy() {
	 	client.dispose();

		stayAwake(false);

		super.onDestroy();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("提示！");
			alertDialog.setMessage("退出会挂掉通话，确定退出吗？");
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// finish();
							client.cancel();
							return;
						}
					});
			alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							return;
						}
					});
			alertDialog.show();

		}
		return super.onKeyDown(keyCode, event);
	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.i(TAG, "OnNewIntent");
	}


	@Override
	public void onUnlockCallback(boolean isSucceed) {
		TextView res = new TextView(mContext);
		if (isSucceed) {
			res.setTextColor(getResources().getColor(R.color.ajbgreen));
//			res.setText(getString(R.string.toast_open_success));
			res.setText("开锁成功");
			Log.i("-----,", "unlock true");
		} else {
			res.setTextColor(getResources().getColor(R.color.ajbred));
//			res.setText(getString(R.string.toast_open_failure));
			res.setText("开锁失败");
			Log.i("-----,", "unlock failure");
		}

		res.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getResources().getDimension(R.dimen.toast_door_text_size));
		Toast toast = new Toast(getApplicationContext());
		toast.setView(res);
		toast.setGravity(Gravity.TOP, 0, getResources().
				getDimensionPixelOffset(R.dimen.toast_door_margin_top));
		toast.show();
	}

	@Override
	public void onRemoteCancel() {
		finish();
	}

	@Override
	public void onAnswer() {

		start_btn.setVisibility(View.GONE);
		space_left.setVisibility(View.VISIBLE);
		space_right.setVisibility(View.VISIBLE);

		start_btn.setEnabled(false);
		titleTextView.setText("访客通话");
		stop_btn.setText("结  束");
	}

	@Override
	public RelativeLayout getVideoLayout() {
//		return (RelativeLayout) findViewById(R.id.rlyt_video_area);
		return rlyt_video_area;
	}
}
