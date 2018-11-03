package com.huacheng.huiservers.wuye;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class ChooseFloorActivity extends BaseUI implements OnClickListener {
    private TextView title_name;
    private ListView list_center;
    private LinearLayout lin_left;
    private RelativeLayout title_rel;

    private String unit, bud_id,bud_Name, c_id;

    BudlingAdapter adapter;
    WuYeProtocol protocol = new WuYeProtocol();
    List<WuYeBean> beans = new ArrayList<WuYeBean>();
    String floors,department_id, department_name, company_id, company_name,name, XQ_name,is_ym;
    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.public_xiadan_list);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("选择房间");
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);

        list_center = (ListView) findViewById(R.id.list_center);

        c_id = this.getIntent().getExtras().getString("XQ_id");//接收传过来的一级小区id
        XQ_name = this.getIntent().getExtras().getString("XQ_name");//接收传过来的一级小区id

        bud_id = this.getIntent().getExtras().getString("bud_id");//接收传过来的一级小区名称
        bud_Name = this.getIntent().getExtras().getString("bud_Name");
        unit = this.getIntent().getExtras().getString("unit");//接收传过来的所选的单元
        name = this.getIntent().getExtras().getString("name");//接收传过来的一级小区id

        is_ym = this.getIntent().getExtras().getString("is_ym");//

        department_id = this.getIntent().getExtras().getString("department_id");
        department_name = this.getIntent().getExtras().getString("department_name");
        company_id = this.getIntent().getExtras().getString("company_id");
        company_name = this.getIntent().getExtras().getString("company_name");

    }

    @Override
    protected void initData() {
        super.initData();
        getRoom();
    }

    private void getRoom() {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", c_id);
        params.addBodyParameter("buildsing_id", bud_id);
        params.addBodyParameter("units", unit);
        HttpHelper hh = new HttpHelper(info.get_pro_room, params, ChooseFloorActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                beans = protocol.getWuYeCode(json);
                adapter = new BudlingAdapter(ChooseFloorActivity.this, beans);
                list_center.setAdapter(adapter);
                list_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putString("XQ_name",XQ_name);
                        bundle.putString("bud_Name",bud_Name);
                        bundle.putString("all_name", name + "-" + unit + "单元-" + beans.get(arg2).getCode());
                        bundle.putString("code", beans.get(arg2).getCode());
                        bundle.putString("floors", beans.get(arg2).getFloor());
                        bundle.putString("department_id", department_id);
                        bundle.putString("department_name", department_name);
                        bundle.putString("company_id", company_id);
                        bundle.putString("company_name", company_name);

                        bundle.putString("is_ym", is_ym);
                        bundle.putString("XQ_id", c_id);
                        bundle.putString("bud_id", bud_id);
                        bundle.putString("unit", unit);
                        bundle.putString("room_id", beans.get(arg2).getId());

                        intent.putExtras(bundle);
                        setResult(11, intent);
                        finish();
                    }
                });
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("unit", "");
            bundle.putString("bud_id", "");
            bundle.putString("XQ_id", "");
            bundle.putString("room_id", "");
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
        }
        return super.onKeyDown(keyCode, event);
    }

    public class BudlingAdapter extends BaseAdapter {
        List<WuYeBean> beans;
        private Context context;

        public BudlingAdapter(Context context, List<WuYeBean> beans) {
            this.context = context;
            this.beans = beans;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (beans.size() != 0) {
                return beans.size();
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
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
            holder.text.setText(beans.get(position).getCode());
            return convertView;
        }

        public class ViewHolder {
            private TextView text;
        }
    }
}
