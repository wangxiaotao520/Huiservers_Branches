package com.huacheng.huiservers.center;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.PersoninfoBean;
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

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class NamenickVerfityActivity extends BaseUI implements OnClickListener {

    TextView title_name,tv_description, tv_flag, right;
    EditText et_content;

    @Override
    protected void init() {
        super.init();
   //     SetTransStatus.GetStatus(this);
        setContentView(R.layout.verify_editxt_new);
        // title
        title_name = (TextView) findViewById(R.id.title_name);
        right = (TextView) findViewById(R.id.right);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
        et_content = (EditText) findViewById(R.id.et_content);
        // set
        title_name.setText("昵称");
        right.setTextColor(getResources().getColor(R.color.rednew));
        right.setText("提交");
        // get
        String nickname = getIntent().getExtras().getString("nickname");
        if (!nickname.equals("")) {
            et_content.setText(nickname);
        } else {
            et_content.setHint("请输入");
        }
        String sex = getIntent().getExtras().getString("tv_sex");
        if (sex.equals("男")) {
            tv_description.setText("先生");
        } else if (sex.equals("女")) {
            tv_description.setText("女士");
        } else {
            tv_description.setText("先生/女士");
        }

        tv_flag.setText("将用于社区慧生活社区交流，昵称不能超过8位，包含汉字、字母或数字，且不能与别人重复");
        // 限定edittext能输入内容
        et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        // 强制隐藏Android输入法窗口
        et_content.setFocusableInTouchMode(true);
        et_content.setFocusable(true);
        et_content.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_content, 0);
            }
        }, 100);
        // listener
        findViewById(R.id.lin_left).setOnClickListener(this);
        right.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                String nickname = et_content.getText().toString();
                if (!nickname.equals("")) {
                    getMyinfo(nickname);
                    /*closeInputMethod();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("nickname", nickname);
                    intent.putExtras(bundle);
                    setResult(22, intent);
                    finish();*/
                } else {
                    XToast.makeText(this, "昵称不能为空", XToast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lin_left:
                closeInputMethod();
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
        Url_info info=new Url_info(this);
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("nickname", param);
       /* if (file.exists()) {
            params.addBodyParameter("avatars", file);
        }
        System.out.println("currentSelected-----" + currentSelected);
        params.addBodyParameter("sex", currentSelected);
        params.addBodyParameter("birthday", txt_time.getText().toString());
        */
        if (ApiHttpClient.TOKEN!=null&&ApiHttpClient.TOKEN_SECRET!=null){
            params.addBodyParameter("token",ApiHttpClient.TOKEN+"");
            params.addBodyParameter("tokenSecret",ApiHttpClient.TOKEN_SECRET+"");
        }
        http.configCookieStore(MyCookieStore.cookieStore);
        http.send(HttpRequest.HttpMethod.POST,info.edit_center, params,new RequestCallBack<String>() {

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
                            closeInputMethod();
                            EventBus.getDefault().post(new PersoninfoBean());
                          //  MyCookieStore.My_info = 1;
//                            Intent intent = new Intent(NamenickVerfityActivity.this, MyInfoActivity2.class);
                            /*Bundle bundle = new Bundle();
                            bundle.putString("name", param);
                            intent.putExtras(bundle);
                            setResult(11, intent);*/

                            /*MyCookieStore.My_info = 1;
                            */
                            finish();
                            XToast.makeText(NamenickVerfityActivity.this, "修改成功", XToast.LENGTH_SHORT)
                                    .show();
                        } else {
                            XToast.makeText(NamenickVerfityActivity.this, str, XToast.LENGTH_SHORT).show();
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
            // imm.toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

            imm.hideSoftInputFromWindow(et_content.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
