package com.huacheng.huiservers.center;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.New_AddressAdapter;
import com.huacheng.huiservers.dialog.DeleteAddressDialog;
import com.huacheng.huiservers.geren.ServiceXiaDanActivity;
import com.huacheng.huiservers.geren.bean.GerenBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.GerenProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.ConfirmOrderActivity;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huacheng.huiservers.R.id.list_address;

/**
 * 类：
 * 时间：2017/10/14 15:11
 * 功能描述:Huiservers
 */
public class New_AddressActivity extends BaseUI {
    @BindView(R.id.lin_btn)
    LinearLayout mLinBtn;
    @BindView(R.id.left)
    ImageView mLeft;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView title_name;
    @BindView(list_address)
    ListView mListAddress;
    @BindView(R.id.rel_no_data)
    RelativeLayout mRelNoData;
    private String edit_id, person_address, person_name, person_mobile;
    private String address, tag_id, type;
    private GerenProtocol protocol = new GerenProtocol();
    GerenBean beans = new GerenBean();
    ShopProtocol shopprotocol = new ShopProtocol();
    New_AddressAdapter new_addressAdapter;
    List<String> ids;
    SharePrefrenceUtil prefrenceUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_address);
        ButterKnife.bind(this);
        prefrenceUtil = new SharePrefrenceUtil(this);
        TextView mRight = (TextView) findViewById(R.id.right);
        mRight.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        mRight.setText("添加");
        //       SetTransStatus.GetStatus(this);//系统栏默认为黑色
        address = this.getIntent().getExtras().getString("address");
        type = this.getIntent().getExtras().getString("type");
        title_name.setText("收货地址");
        /*String itag = this.getIntent().getExtras().getString("itag");
        if(!StringUtils.isEmpty(itag)){
            title_name.setText("收货地址");
        }else{
            title_name.setText("我的地址");
        }*/
        if (address.equals("shopyes")) {
            // lin_peisong.setVisibility(View.VISIBLE);
            ids = this.getIntent().getExtras().getStringArrayList("list_id");
            //shop_id=this.getIntent().getExtras().getString("shop_id");
            StringBuilder sbs = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                if (i == 0) {
                    sbs.append(String.valueOf(ids.get(i)));
                } else {
                    sbs.append("," + String.valueOf(ids.get(i)));
                }
            }
            tag_id = new String(sbs);
            System.out.println("tag----" + tag_id);
        }
        getdata();
        listonclick();
        if (type.equals("center")) {
            mListAddress.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                    final String addressid = beans.getCom_list().get(i).getId();
                    DeleteAddressDialog deleteAddressDialog = new DeleteAddressDialog(New_AddressActivity.this, new DeleteAddressDialog.OnCustomDialogListener() {
                        @Override
                        public void back(String name) {
                            if (name.equals("1")) {//编辑成功"
                                Intent intent = new Intent(New_AddressActivity.this, New_AddAddressActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", addressid);
                                bundle.putString("tag", "edit");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                // XToast.makeText(New_AddressActivity.this, "编辑成功", XToast.LENGTH_SHORT).show();
                            } else if (name.equals("2")) {
                                getdelete(addressid, i);
                                //XToast.makeText(New_AddressActivity.this, "删除成功", XToast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    deleteAddressDialog.show();
                    return false;
                }
            });
        }
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (beans.getCommunity_cn() == null || beans.getCom_list().size() < 5) {
                Intent intent = new Intent(New_AddressActivity.this, New_AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "");
                bundle.putString("tag", "add");
                intent.putExtras(bundle);
                startActivity(intent);
               /* } else {
                    XToast.makeText(New_AddressActivity.this, "地址最多添加5个", XToast.LENGTH_SHORT).show();
                }*/
                /*intent = new Intent(this, New_AddAddressActivity.class);
                startActivity(intent);*/
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    private void listonclick() {
        mListAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                edit_id = beans.getCom_list().get(arg2).getId();
                person_name = beans.getCom_list().get(arg2).getConsignee_name();
                person_mobile = beans.getCom_list().get(arg2).getConsignee_mobile();
                String str = beans.getCom_list().get(arg2).getRegion_cn().replaceAll(",", "");
                person_address = str + beans.getCom_list().get(arg2).getCommunity_cn()
                        + beans.getCom_list().get(arg2).getDoorplate();
                System.out.println("-----[" + edit_id);
                System.out.println("-----[" + person_name + person_mobile + person_address);
                if (address.equals("shopyes")) {//判断当前地址是否可选
                    if (beans.getCom_list().get(arg2).getIs_select().equals("0")) {
                        //	ToastUtils.showShort(AddressListActivity.this, "该地址不可选");
                        //	Toast.makeText(AddressListActivity.this, "该地址不可选", Toast.LENGTH_SHORT).show();
                        XToast.makeText(New_AddressActivity.this, "您选择的地址不在当前小区配送范围内，请返回APP首页选择和您地址相符合的小区", XToast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(New_AddressActivity.this, ConfirmOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("person_name", person_name);
                        bundle.putString("person_mobile", person_mobile);
                        bundle.putString("address", person_address);
                        bundle.putString("address_id", edit_id);
                        intent.putExtras(bundle);
                        setResult(200, intent);
                        finish();
                    }
                } else if (address.equals("serviceDan")) {
                    Intent intent = new Intent(New_AddressActivity.this, ServiceXiaDanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("person_name", person_name);
                    bundle.putString("person_mobile", person_mobile);
                    bundle.putString("address", person_address);
                    bundle.putString("address_id", edit_id);
                    intent.putExtras(bundle);
                    setResult(200, intent);
                    finish();
                }
            }
        });
    }

    @OnClick({R.id.lin_left, R.id.right, R.id.lin_btn})
    public void onViewClicked(View view) {
        Intent intent;
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.lin_left:
                /*if (address.equals("no")) {
                    System.out.println("---========");
                }*/
                finish();
                break;
            /*case R.id.right://添加

                break;*/
            case R.id.lin_btn:
            /*    intent = new Intent(New_AddressActivity.this, MyZhuZhaiRZActivity.class);
                startActivity(intent);*/
                break;
        }
    }

    private void getdata() {//地址列表
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        if (address.equals("shopyes")) {
            params.addBodyParameter("m_id_arr", tag_id);
        } /*else if (address.equals("serviceDan")) {
        }*/
        params.addBodyParameter("c_id", prefrenceUtil.getXiaoQuId());
        HttpHelper hh = new HttpHelper(info.get_user_address, params, New_AddressActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                beans = protocol.addressList(json);
                System.out.println("-=---" + beans);
                if (beans.getCommunity_cn() != null) {
                    mRelNoData.setVisibility(View.GONE);
                    new_addressAdapter = new New_AddressAdapter(New_AddressActivity.this, beans.getCom_list(), address);
                    mListAddress.setAdapter(new_addressAdapter);
                   /* adapter=new AddressListAdapter(New_AddressActivity.this,beans.getCom_list(),address);
                    list_address.setAdapter(adapter);*/
                } else {
                    mRelNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getdelete(String id, final int arg0) {//删除地址
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        System.out.println("p_id====" + id);
        showDialog(smallDialog);
        HttpHelper hh = new HttpHelper(info.del_user_address, params, New_AddressActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                str = shopprotocol.setShop(json);
                if (str.equals("1")) {
                    beans.getCom_list().remove(arg0);
                    new_addressAdapter.notifyDataSetChanged();
                    XToast.makeText(context, "删除成功", XToast.LENGTH_SHORT).show();
                } else {
                    XToast.makeText(context, str, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

}
