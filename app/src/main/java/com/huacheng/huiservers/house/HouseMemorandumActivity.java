package com.huacheng.huiservers.house;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类：
 * 时间：2018/3/24 15:31
 * 功能描述:Huiservers
 */
public class HouseMemorandumActivity extends BaseUI {

    HouseBean mHouseBean = new HouseBean();
    HouseProtocol mProtocol = new HouseProtocol();
    ShopProtocol mShopProtocol = new ShopProtocol();
    String STRContent;

    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.right)
    TextView mRight;
    @BindView(R.id.id_expand_textview)
    EditText mIdExpandTextview;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_memorandum);
 //       SetTransStatus.GetStatus(this);
        ButterKnife.bind(this);
        mTitleName.setText("备忘录");
        mRight.setVisibility(View.VISIBLE);
        mRight.setText("编辑");
        mRight.setTextColor(getResources().getColor(R.color.gray2));

        getmem();

    }

    private void getmem() {//获取我的备忘录
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.get_memorandum, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mHouseBean = mProtocol.getMem(json);

                if (!TextUtils.isEmpty(mHouseBean.getContent())) {
                    mIdExpandTextview.setText(mHouseBean.getContent());
                    //设置不可编辑
                    mIdExpandTextview.setFocusable(false);
                    mIdExpandTextview.setFocusableInTouchMode(false);
                    mTvTime.setText(StringUtils.getDateToString(mHouseBean.getUptime(), "2"));
                } else {
                    mIdExpandTextview.setText("");
                    mTvTime.setVisibility(View.GONE);
                    mRight.setText("完成");
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getADDmem() {//添加修改我的备忘录
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("content", mIdExpandTextview.getText().toString());
        HttpHelper hh = new HttpHelper(info.set_memorandum, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                STRContent = mShopProtocol.setShop(json);
                if (STRContent.equals("1")) {
                    mRight.setText("完成");
                    XToast.makeText(HouseMemorandumActivity.this, "编辑成功", XToast.LENGTH_SHORT).show();
                    getmem();

                } else {
                    //设置为可编辑状态
                    mIdExpandTextview.setFocusableInTouchMode(true);
                    mIdExpandTextview.setFocusable(true);
                    mIdExpandTextview.requestFocus();
                    mRight.setText("编辑");
                    XToast.makeText(HouseMemorandumActivity.this, STRContent, XToast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @OnClick({R.id.lin_left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.right:

                if (mRight.getText().toString().equals("编辑")) {
                    mRight.setText("完成");
                    //设置为可编辑状态
                    mIdExpandTextview.setFocusableInTouchMode(true);
                    mIdExpandTextview.setFocusable(true);
                    mIdExpandTextview.requestFocus();
                    //光标显示在末尾
                    mIdExpandTextview.setSelection(mHouseBean.getContent().length());

                } else if (mRight.getText().toString().equals("完成")) {

                    if (mIdExpandTextview.getText().toString().equals("")) {
                        XToast.makeText(this, "内容不能为空", XToast.LENGTH_SHORT).show();
                    } else {
                        getADDmem();
                    }
                }
                break;
        }
    }
}
