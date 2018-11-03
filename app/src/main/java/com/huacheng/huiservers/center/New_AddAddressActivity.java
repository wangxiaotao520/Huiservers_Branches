package com.huacheng.huiservers.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.geren.bean.AddressBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.GerenProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2017/10/14 15:11
 * 功能描述:Huiservers
 */
public class New_AddAddressActivity extends BaseUI {
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_menpai)
    EditText mEdtMenpai;
    @BindView(R.id.edt_mobile)
    EditText mEdtMobile;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.right)
    TextView mRight;
    @BindView(R.id.txt_address)
    TextView mTxtAddress;

    private String id, tag, xiaoqu_id, a_id, a_name, xiaoqu_name;
    private GerenProtocol protocol = new GerenProtocol();
    private AddressBean beans = new AddressBean();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_add_address);
        ButterKnife.bind(this);
  //      SetTransStatus.GetStatus(this);//系统栏默认为黑色
        TextView mTitleName = (TextView) findViewById(R.id.title_name);
        TextView mRight = (TextView) findViewById(R.id.right);
        mRight.setTextColor(getResources().getColor(R.color.colorPrimary));
        mRight.setVisibility(View.VISIBLE);
        mRight.setText("保存");

        id = getIntent().getExtras().getString("id");
        tag = getIntent().getExtras().getString("tag");

        if (tag.equals("edit")) {
            mTitleName.setText("修改地址");
            getDetail();
        } else {
            mTitleName.setText("添加地址");
        }
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtName.getText().toString().equals("")) {
                    XToast.makeText(New_AddAddressActivity.this, "请输入联系人", XToast.LENGTH_SHORT).show();
                } else if (mTxtAddress.getText().toString().equals("")) {
                    XToast.makeText(New_AddAddressActivity.this, "请选择您所在的小区", XToast.LENGTH_SHORT).show();
                } else if (mEdtMenpai.getText().toString().equals("")) {
                    XToast.makeText(New_AddAddressActivity.this, "请输入您的详细地址", XToast.LENGTH_SHORT).show();
                } else if (mEdtMobile.getText().toString().equals("")) {
                    XToast.makeText(New_AddAddressActivity.this, "请输入联系电话", XToast.LENGTH_SHORT).show();
                } else if (!ToolUtils.isMobileNO(mEdtMobile.getText().toString())) {
                    XToast.makeText(New_AddAddressActivity.this, "请输入正确的手机号", XToast.LENGTH_SHORT).show();
                } else {
                    getdata();
                    New_AddAddressActivity.this.mRight.setClickable(false);
                }
            }
        });
    }

    @OnClick({R.id.lin_left, R.id.right, R.id.txt_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                UIUtils.closeInputMethod(this, mEdtName);
                finish();
                break;
            /*case R.id.right://保存

                break;*/
            case R.id.txt_address://选择小区
                Intent intent = new Intent(New_AddAddressActivity.this, XiaoquActivity.class);
                startActivityForResult(intent, 3);

                break;
        }
    }

    private void getdata() {// 添加修改
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        System.out.println("^^^^^^^^^^" + tag);
        RequestParams params = new RequestParams();
        params.addBodyParameter("region_id", "");
        params.addBodyParameter("region_cn", "");
        params.addBodyParameter("community_id", xiaoqu_id);
        params.addBodyParameter("community_cn", xiaoqu_name);
        params.addBodyParameter("consignee_name", mEdtName.getText().toString());
        params.addBodyParameter("consignee_mobile", mEdtMobile.getText().toString());
        params.addBodyParameter("doorplate", mEdtMenpai.getText().toString());
        System.out.println("consignee_name-----" + mEdtName.getText().toString());
        if (tag.equals("edit")) {
            params.addBodyParameter("id", id);
        }
        HttpHelper hh = new HttpHelper(info.add_user_address, params, New_AddAddressActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = protocol.addSuss(json);
                if (str.equals("1")) {
                    //ToastUtils.showShort(Add_addressActivity.this, "成功");
                    //Toast.makeText(Add_addressActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    UIUtils.closeInputMethod(New_AddAddressActivity.this, mEdtName);

                    XToast.makeText(New_AddAddressActivity.this, "成功", XToast.LENGTH_SHORT).show();
                    finish();
                } else {
                    mRight.setClickable(true);
                    XToast.makeText(New_AddAddressActivity.this, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getDetail() {// 修改获取详情
        showDialog(smallDialog);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        HttpHelper hh = new HttpHelper(MyCookieStore.SERVERADDRESS + "userCenter/address_details/", params, New_AddAddressActivity.this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    String msg = jsonObject.getString("msg");
                    if ("1".equals(status)){
                        beans = protocol.addressDetail(json);
                        mEdtName.setText(beans.getConsignee_name());
                        mEdtMobile.setText(beans.getConsignee_mobile());
                        mTxtAddress.setText(beans.getRegion_cn() + beans.getCommunity_cn());
                        mEdtMenpai.setText(beans.getDoorplate());
                        a_id = beans.getRegion_id();
                        a_name = beans.getRegion_cn();
                        xiaoqu_id = beans.getCommunity_id();
                        xiaoqu_name = beans.getCommunity_cn();
                    }else {
                        UIUtils.showToastSafe(msg+"");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getExtras().getString("name");
        String id = data.getExtras().getString("id");
        String all_id = data.getExtras().getString("all_id");
        String all_name = data.getExtras().getString("all_name");
        System.out.println("allid====" + all_id);
        System.out.println("allname====" + all_name);
        System.out.println("id====" + id);
        System.out.println("name====" + name);
        switch (resultCode) {
            case 1111:
                break;

            case 100:// 小区返回值
                mTxtAddress.setText(name);
                xiaoqu_id = id;
                xiaoqu_name = name;
                a_name = all_name;
                a_id = all_id;

                break;
            case 200:
                mTxtAddress.setText(name);
                xiaoqu_id = id;
                xiaoqu_name = name;
                a_name = all_name;
                a_id = all_id;
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
