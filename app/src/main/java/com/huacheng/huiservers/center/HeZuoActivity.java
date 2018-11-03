package com.huacheng.huiservers.center;

import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.utils.ToastUtils;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hwh on 2018/3/21.
 */

public class HeZuoActivity extends BaseUI implements View.OnClickListener {

    private LinearLayout lin_left;
    private RelativeLayout rel_ti;
    private TextView title_name;
    private EditText et_name, et_phone, et_content;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_hezuo);
     //   SetTransStatus.GetStatus(this);

        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        rel_ti = (RelativeLayout) findViewById(R.id.rel_ti);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_content = (EditText) findViewById(R.id.et_content);
        title_name = (TextView) findViewById(R.id.title_name);

        title_name.setText("商务合作");

        lin_left.setOnClickListener(this);
        rel_ti.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;

            case R.id.rel_ti:
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    XToast.makeText(this, "请填写姓名", XToast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    XToast.makeText(this, "请填写电话", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isMobileNO(et_phone.getText().toString().trim())) {
                    XToast.makeText(this, "手机号码格式不正确", XToast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    XToast.makeText(this, "请填写内容", XToast.LENGTH_SHORT).show();
                } else {
                    getResult();
                }
                break;
            default:
                break;
        }
    }

    private void getResult() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("content", et_content.getText().toString().trim());
        params.addBodyParameter("name", et_name.getText().toString().trim());
        params.addBodyParameter("mobile", et_phone.getText().toString().trim());
        params.addBodyParameter("type", "2");
        HttpHelper hh = new HttpHelper(info.yijian, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                      //  XToast.makeText(HeZuoActivity.this, "提交成功", XToast.LENGTH_SHORT).show();
                        ToastUtils.showShort(HeZuoActivity.this,"提交成功");
                        finish();
                    } else {

                        XToast.makeText(HeZuoActivity.this, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
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
}
