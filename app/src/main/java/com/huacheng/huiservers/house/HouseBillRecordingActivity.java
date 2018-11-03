package com.huacheng.huiservers.house;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.adapter.HouseBillRecordingAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/21.
 */

public class HouseBillRecordingActivity extends BaseUI implements View.OnClickListener {

    @BindView(R.id.lin_left)
    LinearLayout linLeft;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.listview_billcording)
    MyListView listviewBillcording;
    @BindView(R.id.lin_nodata)
    LinearLayout linNodata;

    WuYeBean roomInfo;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.house_billrecording);
        ButterKnife.bind(this);
//        SetTransStatus.GetStatus(this);
        titleName.setText("账单记录");
        linLeft.setOnClickListener(this);

        roomInfo = (WuYeBean) getIntent().getSerializableExtra("room");
        if (!StringUtils.isEmpty(roomInfo.getRoom_id())) {
            initDataRecord(roomInfo.getRoom_id());
        }

    }

    private void initDataRecord(String roomID) {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams p = new RequestParams();
        p.addBodyParameter("room_id", roomID);
        new HttpHelper(urlInfo.get_property_order, p, this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                List<HouseBean> orders = new HouseProtocol().getPropertyOrder(json);
                if (orders != null) {
                    if (orders.size() > 0) {
                        linNodata.setVisibility(View.GONE);
                        listviewBillcording.setVisibility(View.VISIBLE);
                        HouseBillRecordingAdapter adapter = new HouseBillRecordingAdapter(orders, HouseBillRecordingActivity.this);
                        listviewBillcording.setAdapter(adapter);
                    } else {
                        listviewBillcording.setVisibility(View.GONE);
                        linNodata.setVisibility(View.VISIBLE);
                    }
                } else {
                    listviewBillcording.setVisibility(View.GONE);
                    linNodata.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
        }
    }

}
