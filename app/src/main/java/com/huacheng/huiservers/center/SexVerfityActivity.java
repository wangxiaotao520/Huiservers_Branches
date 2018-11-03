package com.huacheng.huiservers.center;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class SexVerfityActivity extends BaseUI implements OnClickListener {

    LinearLayout lin_left;
    TextView title_name, right;
    RadioGroup radiogroup;
    RadioButton radio_nan, radio_nv;
    String currentSelectedVal;

    @Override
    protected void init() {
        super.init();
 //       SetTransStatus.GetStatus(this);
        setContentView(R.layout.verify_sex_new);
        title_name = (TextView) findViewById(R.id.title_name);
        right = (TextView) findViewById(R.id.right);
        // set
        title_name.setText("性别");
        right.setTextColor(getResources().getColor(R.color.rednew));
        right.setText("提交");
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radio_nan = (RadioButton) findViewById(R.id.radio_nan);
        radio_nv = (RadioButton) findViewById(R.id.radio_nv);
        final Drawable drawableCheck = getResources().getDrawable(R.drawable.radio_check1);
        final Drawable drawableunCheck = getResources().getDrawable(R.drawable.radio_uncheck);
        drawableCheck.setBounds(0, 0, drawableCheck.getMinimumWidth(), drawableCheck.getMinimumHeight());  //
        drawableunCheck.setBounds(0, 0, drawableunCheck.getMinimumWidth(), drawableunCheck.getMinimumHeight());  //
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rb = (RadioButton) findViewById(arg0.getCheckedRadioButtonId());
                if (rb.getText().toString().equals("男")) {
                    currentSelectedVal = "1";
                    radio_nan.setCompoundDrawables(null, null, drawableCheck, null);
                    radio_nv.setCompoundDrawables(null, null, drawableunCheck, null);
                } else {
                    currentSelectedVal = "2";
                    radio_nv.setCompoundDrawables(null, null, drawableCheck, null);
                    radio_nan.setCompoundDrawables(null, null, drawableunCheck, null);
                }
            }
        });
        //getExtra

        String sex = getIntent().getExtras().getString("sex");
        if (!sex.equals("")) {
            if (sex.equals("男")) {
                radio_nan.setChecked(true);
                radio_nan.setCompoundDrawables(null, null, drawableCheck, null);
                radio_nv.setCompoundDrawables(null, null, drawableunCheck, null);
            } else if (sex.equals("女")) {
                radio_nv.setChecked(true);
                radio_nv.setCompoundDrawables(null, null, drawableCheck, null);
                radio_nan.setCompoundDrawables(null, null, drawableunCheck, null);
            }
        } else {
            radio_nan.setCompoundDrawables(null, null, drawableunCheck, null);
            radio_nv.setCompoundDrawables(null, null, drawableunCheck, null);
        }
        // listener
        findViewById(R.id.lin_left).setOnClickListener(this);
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                if (!currentSelectedVal.equals("")) {
                    getMyinfo(currentSelectedVal);
                    /*Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("sex", currentSelectedVal);
                    intent.putExtras(bundle);
                    setResult(3, intent);
                    finish();*/
                } else {
                    XToast.makeText(this, "性别为空", XToast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_left:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 修改个人信息
     *
     * @param param
     */
    private void getMyinfo(final String param) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sex", param);
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

                            finish();
                            XToast.makeText(SexVerfityActivity.this, "修改成功", XToast.LENGTH_SHORT)
                                    .show();
                        } else {
                            XToast.makeText(SexVerfityActivity.this, str, XToast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
