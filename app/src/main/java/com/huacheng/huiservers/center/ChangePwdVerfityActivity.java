package com.huacheng.huiservers.center;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

public class ChangePwdVerfityActivity extends BaseUI implements OnClickListener {

    TextView title_name, tv_flag, right;
    EditText et_new_pwd, et_enter_pwd;//et_old_pwd,
    private boolean isHideNewPwd = true;// 输入框密码是否是隐藏的，默认为true
    private boolean isHideEnterPwd = true;// 输入框密码是否是隐藏的，默认为true

    @Override
    protected void init() {
        super.init();
    //    SetTransStatus.GetStatus(this);
        setContentView(R.layout.verify_editxt_pwd_new);
        // title
        title_name = (TextView) findViewById(R.id.title_name);
        right = (TextView) findViewById(R.id.right);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
//        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_enter_pwd = (EditText) findViewById(R.id.et_enter_pwd);

        // set
        title_name.setText("修改密码");
        right.setTextColor(getResources().getColor(R.color.rednew));
        right.setText("提交");

        // get

        // 限定edittext能输入内容
        /*et_old_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        et_new_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});*/
        // 强制隐藏Android输入法窗口
     /*   et_content.setFocusableInTouchMode(true);
        et_content.setFocusable(true);
        et_content.requestFocus();*/
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) et_new_pwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_new_pwd, 0);
            }
        }, 200);
        // listener
        findViewById(R.id.lin_left).setOnClickListener(this);
        right.setOnClickListener(this);

        //新密码显示和隐藏
        final Drawable[] drawables = et_new_pwd.getCompoundDrawables();
        final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.drawable.openf2);
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略
        et_new_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // getWidth,getHeight必须在这里处理
                    float et_pwdMinX = v.getWidth() - eyeWidth - et_new_pwd.getPaddingRight();
                    float et_pwdMaxX = v.getWidth();
                    float et_pwdMinY = 0;
                    float et_pwdMaxY = v.getHeight();
                    float x = event.getX();
                    float y = event.getY();
                    if (x < et_pwdMaxX && x > et_pwdMinX && y > et_pwdMinY && y < et_pwdMaxY) {
                        // 点击了眼睛图标的位置
                        isHideNewPwd = !isHideNewPwd;
                        if (isHideNewPwd) {
                            et_new_pwd.setCompoundDrawables(drawables[0], drawables[1],
                                    drawables[2],
                                    drawables[3]);

                            et_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            et_new_pwd.setCompoundDrawables(drawables[0], drawables[1],
                                    drawableEyeOpen,
                                    drawables[3]);
                            et_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });

        //新密码显示和隐藏
        final Drawable[] drawables1 = et_enter_pwd.getCompoundDrawables();
        final int eyeWidth1 = drawables1[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen1 = getResources().getDrawable(R.drawable.openf2);
        drawableEyeOpen1.setBounds(drawables1[2].getBounds());//这一步不能省略
        et_enter_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // getWidth,getHeight必须在这里处理
                    float et_pwdMinX = v.getWidth() - eyeWidth1 - et_enter_pwd.getPaddingRight();
                    float et_pwdMaxX = v.getWidth();
                    float et_pwdMinY = 0;
                    float et_pwdMaxY = v.getHeight();
                    float x = event.getX();
                    float y = event.getY();
                    if (x < et_pwdMaxX && x > et_pwdMinX && y > et_pwdMinY && y < et_pwdMaxY) {
                        // 点击了眼睛图标的位置
                        isHideNewPwd = !isHideNewPwd;
                        if (isHideNewPwd) {
                            et_enter_pwd.setCompoundDrawables(drawables1[0], drawables1[1],
                                    drawables1[2],
                                    drawables1[3]);

                            et_enter_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            et_enter_pwd.setCompoundDrawables(drawables1[0], drawables1[1],
                                    drawableEyeOpen1,
                                    drawables1[3]);
                            et_enter_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right:
                //密码至少要8位，且包含数字和字母
//                String old_pwd = et_old_pwd.getText().toString();
                String new_pwd = et_new_pwd.getText().toString();
                String enter_pwd = et_enter_pwd.getText().toString();
               /* if (old_pwd.equals("")) {
                    XToast.makeText(this, "请输入旧密码", XToast.LENGTH_SHORT).show();
                } else*/
                if (new_pwd.equals("")) {
                    XToast.makeText(this, "请输入新密码", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isReguPwd(new_pwd)) {
                    XToast.makeText(this, "新密码的格式至少要6位，且包含数字和字母", XToast.LENGTH_SHORT).show();
                } else if (enter_pwd.equals("")) {
                    XToast.makeText(this, "请输入确认密码", XToast.LENGTH_SHORT).show();
                } else if (!new_pwd.equals(enter_pwd)) {
                    XToast.makeText(this, "新密码与确认密码不一致，请重新填写", XToast.LENGTH_SHORT).show();
                } else {
                    getpass();
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

    ShopProtocol protocol = new ShopProtocol();

    /**
     * 账号安全
     */
    private void getpass() {
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
//        params.addBodyParameter("old_password", et_old_pwd.getText().toString());
        params.addBodyParameter("password_one", et_new_pwd.getText().toString());
        params.addBodyParameter("password_two", et_enter_pwd.getText().toString());
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(info.edit_pass, params, ChangePwdVerfityActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol.setShop(json);
                if (str.equals("1")) {
                    closeInputMethod();
                    XToast.makeText(ChangePwdVerfityActivity.this, "修改成功", XToast.LENGTH_SHORT).show();
                    finish();
                } else {
                    XToast.makeText(ChangePwdVerfityActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
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

            imm.hideSoftInputFromWindow(et_new_pwd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
