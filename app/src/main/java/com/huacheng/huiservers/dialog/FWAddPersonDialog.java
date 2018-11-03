package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

/**
 * 类：
 * 时间：2018/3/20 17:22
 * 功能描述:Huiservers
 */
public class FWAddPersonDialog extends Dialog {
    OnCustomDialogListener listener;
    Context mContext;
    EditText edt_phone;
    ShopProtocol mShopProtocol = new ShopProtocol();
    private String strinfo;
    private String room_id, bind_type;

    /**
     * 自定义Dialog监听器
     */
    public interface OnCustomDialogListener {
        public void back(String name);
    }

    public FWAddPersonDialog(Context context, String room_id, String bind_type, OnCustomDialogListener listener) {
        super(context);
        this.mContext = context;
        this.listener = listener;
        this.room_id = room_id;
        this.bind_type = bind_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fw_add_people_dialog);

       /* Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.SOFT_INPUT_ADJUST_NOTHING);*/

        LinearLayout lin_btn = (LinearLayout) findViewById(R.id.lin_btn);
        edt_phone = (EditText) findViewById(R.id.edt_phone);
        System.out.println("^^^^^^^^^^^^" + bind_type);
        lin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_phone.getText().toString().equals("")) {
                    XToast.makeText(mContext, "请输入被邀请人手机号", XToast.LENGTH_SHORT).show();
                } else {
                    if (!ToolUtils.isMobileNO(edt_phone.getText().toString())) {
                        XToast.makeText(mContext, "请输入正确的手机格式", XToast.LENGTH_SHORT).show();
                    } else {
                        getAdd(bind_type);
                    }
                }

            }
        });

    }

    private void getAdd(String bindType_str) {//添加家庭成员或是租户成员
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        params.addBodyParameter("mobile", edt_phone.getText().toString());
        params.addBodyParameter("bind_type", bindType_str);
        HttpHelper hh = new HttpHelper(MyCookieStore.SERVERADDRESS + "property/house_member_save/",
                params, mContext) {

            @Override
            protected void setData(String json) {
                strinfo = mShopProtocol.setShop(json);
                if (strinfo.equals("1")) {
                    closeInputMethod();
                    listener.back("1");
                    FWAddPersonDialog.this.dismiss();

                } else {
                    XToast.makeText(mContext, strinfo, XToast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0,
            // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示

            imm.hideSoftInputFromWindow(edt_phone.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
