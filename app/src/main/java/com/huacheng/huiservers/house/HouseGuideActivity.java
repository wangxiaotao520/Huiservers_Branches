package com.huacheng.huiservers.house;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/20.
 */

public class HouseGuideActivity extends BaseUI implements View.OnClickListener {
    public static HouseGuideActivity instant;
    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tv_desc1)
    TextView tvDesc1;
    @BindView(R.id.tv_desc2)
    TextView tvDesc2;
    @BindView(R.id.tv_desc3)
    TextView tvDesc3;
    @BindView(R.id.tv_house_bound)
    TextView tvHouseBound;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.house_bound_desc);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        instant=this;
        linLeft.setOnClickListener(this);
        titleName.setText("绑定房屋后获取以下功能");

        String str = "开通<font color='#FF6A24'>家人</font>开门权限";
        tvDesc1.setText(Html.fromHtml(str));
        String str1 = "开通<font color='#FF6A24'>租客</font>开门权限";
        tvDesc2.setText(Html.fromHtml(str1));
        String str2 = "开通<font color='#FF6A24'>客人临时</font>开门权限";
        tvDesc3.setText(Html.fromHtml(str2));
        tvHouseBound.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_house_bound:
                startActivity(new Intent(this, MyZhuZhaiRZActivity.class));
//                XToast.makeText(this, "我点了", XToast.LENGTH_SHORT).show();
                break;
        }
    }

}
