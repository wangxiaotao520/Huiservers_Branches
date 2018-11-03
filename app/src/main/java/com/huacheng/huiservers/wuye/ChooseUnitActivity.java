package com.huacheng.huiservers.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.WuYeProtocol;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class ChooseUnitActivity extends BaseUI implements OnClickListener {
    private TextView title_name;
    private ListView list_center;
    private LinearLayout lin_left;
    private RelativeLayout title_rel;
    private String buildsing_id, bud_Name;
    UnitAdapter adapter;
    WuYeProtocol protocol = new WuYeProtocol();
    List<String> list = new ArrayList<String>();
    String department_id, department_name, company_id, company_name, floors, c_id, name, XQ_name, unit, is_ym;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.public_xiadan_list);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("选择单元");
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);

        list_center = (ListView) findViewById(R.id.list_center);

        c_id = this.getIntent().getExtras().getString("XQ_id");//接收传过来的一级的小区id
        XQ_name = this.getIntent().getExtras().getString("XQ_name");//接收传过来的2级的名称
        name = this.getIntent().getExtras().getString("name");//接收传过来的2级的名称
        buildsing_id = this.getIntent().getExtras().getString("bud_id");//接收传过来的一级的小区id
        bud_Name = this.getIntent().getExtras().getString("bud_Name");

        unit = this.getIntent().getExtras().getString("unit");//接收传过来的2级的小区所有单元
        is_ym = this.getIntent().getExtras().getString("is_ym");//


        department_id = this.getIntent().getExtras().getString("department_id");
        department_name = this.getIntent().getExtras().getString("department_name");
        company_id = this.getIntent().getExtras().getString("company_id");
        company_name = this.getIntent().getExtras().getString("company_name");
    }

    @Override
    protected void initData() {
        super.initData();
        getUnit();
    }


    private void getUnit() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", c_id);
        params.addBodyParameter("buildsing_id", buildsing_id);
        HttpHelper hh = new HttpHelper(info.get_pro_unit, params, ChooseUnitActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                List<WuYeBean> unit = protocol.getWuYeUnit(json);
                adapter = new UnitAdapter(ChooseUnitActivity.this, unit);
                list_center.setAdapter(adapter);
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");

            }
        };
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left://返回
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("unit", "");
                bundle.putString("bud_id", "");
                bundle.putString("XQ_id", "");
                bundle.putString("room_id", "");
                bundle.putString("all_name", "");
                bundle.putString("XQ_name", "");
                bundle.putString("bud_Name", "");

                bundle.putString("floors", "");
                bundle.putString("code", "");
                bundle.putString("department_id", "");
                bundle.putString("department_name", "");
                bundle.putString("company_id", "");
                bundle.putString("company_name", "");
                bundle.putString("is_ym", "");
                intent.putExtras(bundle);
                setResult(100, intent);
                finish();
                break;
            default:
                break;
        }

    }

    /*
     *以下方法是第三级需要的
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("---");
        String unit = data.getExtras().getString("unit");
        String bud_id = data.getExtras().getString("bud_id");
        String XQ_id = data.getExtras().getString("XQ_id");
        String room_id = data.getExtras().getString("room_id");
        String all_name = data.getExtras().getString("all_name");
        String XQ_name = data.getExtras().getString("XQ_name");
        String bud_Name = data.getExtras().getString("bud_Name");

        String code = data.getExtras().getString("code");
        String floors = data.getExtras().getString("floors");
        String department_id = data.getExtras().getString("department_id");
        String department_name = data.getExtras().getString("department_name");
        String company_id = data.getExtras().getString("company_id");
        String company_name = data.getExtras().getString("company_name");
        String is_ym = data.getExtras().getString("is_ym");

        switch (resultCode) {
            case 11:

                if (requestCode == 1 && resultCode == 11) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("unit", unit);
                    bundle.putString("bud_id", bud_id);
                    bundle.putString("XQ_id", XQ_id);
                    bundle.putString("room_id", room_id);
                    bundle.putString("all_name", all_name);
                    bundle.putString("XQ_name", XQ_name);
                    bundle.putString("bud_Name", bud_Name);

                    bundle.putString("code", code);
                    bundle.putString("floors", floors);
                    bundle.putString("department_id", department_id);
                    bundle.putString("department_name", department_name);
                    bundle.putString("company_id", company_id);
                    bundle.putString("company_name", company_name);
                    bundle.putString("is_ym", is_ym);
                    intent.putExtras(bundle);
                    setResult(22, intent);
                    finish();
                }
                break;
            case 100:
                if (requestCode == 1 && resultCode == 100) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("unit", "");
                    bundle.putString("bud_id", "");
                    bundle.putString("XQ_id", "");
                    bundle.putString("room_id", "");
                    bundle.putString("all_name", "");
                    bundle.putString("XQ_name", "");
                    bundle.putString("bud_Name", "");

                    bundle.putString("code", "");
                    bundle.putString("floors", "");
                    bundle.putString("department_id", "");
                    bundle.putString("department_name", "");
                    bundle.putString("company_id", "");
                    bundle.putString("company_name", "");
                    bundle.putString("is_ym", "");
                    intent.putExtras(bundle);
                    setResult(100, intent);
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("unit", "");
            bundle.putString("bud_id", "");
            bundle.putString("XQ_id", "");
            bundle.putString("room_id", "");
            bundle.putString("all_name", "");
            bundle.putString("XQ_name", "");
            bundle.putString("bud_Name", "");

            bundle.putString("code", "");
            bundle.putString("floors", "");
            bundle.putString("department_id", "");
            bundle.putString("department_name", "");
            bundle.putString("company_id", "");
            bundle.putString("company_name", "");
            bundle.putString("is_ym", "");
            intent.putExtras(bundle);
            setResult(100, intent);
            ChooseUnitActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public class UnitAdapter extends BaseAdapter {
        List<WuYeBean> mUnits;
        private Context context;

        public UnitAdapter(Context context, List<WuYeBean> units) {
            this.context = context;
            this.mUnits = units;
        }

        @Override
        public int getCount() {
            if (mUnits != null && mUnits.size() > 0) {
                return mUnits.size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int arg0) {
            return mUnits.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            ViewHolder holder = null;
            if (null == convertView) {
                // 获得ViewHolder对象
                holder = new ViewHolder();
                convertView = LinearLayout.inflate(context, R.layout.city_one_item, null);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(mUnits.get(position).getUnit() + "单元");
            holder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ChooseUnitActivity.this, ChooseFloorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("XQ_id", c_id);
                    bundle.putString("XQ_name", XQ_name);
                    bundle.putString("unit", mUnits.get(position).getUnit());//所选单元
                    bundle.putString("name", name + "号楼");
                    bundle.putString("is_ym", is_ym);

                    bundle.putString("bud_id", buildsing_id);
                    bundle.putString("bud_Name", bud_Name);
                    bundle.putString("department_id", department_id);
                    bundle.putString("department_name", department_name);
                    bundle.putString("company_id", company_id);
                    bundle.putString("company_name", company_name);

                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);

                }
            });

            return convertView;
        }

        public class ViewHolder {
            private TextView text;
        }
    }
}
