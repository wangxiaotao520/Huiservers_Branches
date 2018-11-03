package com.huacheng.huiservers.house;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.cricle.FaBuActivity;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.FWAddPersonDialog;
import com.huacheng.huiservers.dialog.QuanXianDialog;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 类：
 * 时间：2018/3/20 09:52
 * 功能描述:Huiservers
 */
public class HousePersonInfoActivity extends BaseUI {

    HouseProtocol mHouseProtocol = new HouseProtocol();
    HouseBean mHouseBean = new HouseBean();
    FWAddPersonDialog fwAddPersonDialog;
    @BindView(R.id.lin_zuke)
    LinearLayout mLinZuke;
    private Boolean isdeleteAll = false;//家庭成员
    private Boolean isdeleteZh = false;//家庭成员

    private String room_id;
    @BindView(R.id.lin_all_fw)
    LinearLayout mLinAllFw;
    @BindView(R.id.lin_nodata)
    LinearLayout mLinNodata;
    @BindView(R.id.left)
    ImageView mLeft;
    @BindView(R.id.list_yezhu)
    MyListView mListYezhu;
    @BindView(R.id.list_zuhu)
    MyListView mListZuhu;
    @BindView(R.id.tv_fw)
    TextView mTvFw;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.tv_person_num)
    TextView mTvPersonNum;
    @BindView(R.id.lin_fk)
    LinearLayout mLinFk;
    @BindView(R.id.iv_quanxian)
    ImageView mIvQuanxian;
    @BindView(R.id.iv_addyz)
    ImageView mIvAddyz;
    @BindView(R.id.iv_deleteAllyz)
    ImageView mIvDeleteAllyz;
    @BindView(R.id.iv_quanxian2)
    ImageView mIvQuanxian2;
    @BindView(R.id.iv_addzk)
    ImageView mIvAddzk;
    @BindView(R.id.iv_deleteAllzk)
    ImageView mIvDeleteAllzk;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.lin_zd)
    LinearLayout mLinZd;

    HouseZHListAdapter mHouseZHListAdapter;
    HouseYZListAdapter mHouseYZListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar  即全屏
        setContentView(R.layout.house_person_info);
        ButterKnife.bind(this);
        MyCookieStore.fw_delete = 0;
        room_id = this.getIntent().getExtras().getString("room_id");
        System.out.println("room_id***********" + room_id);
        mTvFw.setFocusable(true);
        mTvFw.setFocusableInTouchMode(true);

        getinfo();

    }

    private void getinfo() {//房屋详细信息
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(urlInfo.room_info,
                params, HousePersonInfoActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mHouseBean = mHouseProtocol.gethousedetail(json);
                //小区信息
                mTvFw.setText(mHouseBean.getRoom_info().getCommunity_name());
                mTvInfo.setText(mHouseBean.getRoom_info().getAddress());
                mTvPersonNum.setText(mHouseBean.getRoom_info().getCount() + "人已绑定房屋");
                //家庭成员
                if (mHouseBean.getPer_li() != null) {//家庭成员不为空
                    mLinAllFw.setVisibility(View.VISIBLE);
                    mHouseYZListAdapter = new HouseYZListAdapter(HousePersonInfoActivity.this, mHouseBean.getPer_li());
                    mListYezhu.setAdapter(mHouseYZListAdapter);
                    if (mHouseBean.getPer_li().size() == 1) {
                        isdeleteAll = false;
                    }
                    if (mHouseBean.getPer_li_() != null) {//判断有没有租户
                        mListZuhu.setVisibility(View.VISIBLE);
                        mLinNodata.setVisibility(View.GONE);
                        mHouseZHListAdapter = new HouseZHListAdapter(HousePersonInfoActivity.this, mHouseBean.getPer_li_());
                        mListZuhu.setAdapter(mHouseZHListAdapter);
                    } else {
                        //无数据默认删除隐藏
                        isdeleteZh = false;
                        mListZuhu.setVisibility(View.GONE);
                        mLinNodata.setVisibility(View.VISIBLE);
                    }

                } else {//家庭成员不显示 。只显示租户成员
                    mLinAllFw.setVisibility(View.GONE);
                    mHouseZHListAdapter = new HouseZHListAdapter(HousePersonInfoActivity.this, mHouseBean.getPer_li_());
                    mListZuhu.setAdapter(mHouseZHListAdapter);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @OnClick({R.id.lin_fk, R.id.iv_quanxian, R.id.iv_addyz, R.id.iv_deleteAllyz, R.id.iv_quanxian2,
            R.id.iv_addzk, R.id.iv_deleteAllzk, R.id.lin_left, R.id.lin_zd, R.id.lin_zuke})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_fk://访客邀请;
                getResult();

                break;
            case R.id.lin_zd://家庭账单
                Intent intent2 = new Intent(this, HouseFamilyBillActivity.class);
                intent2.putExtra("roomInfo", mHouseBean.getRoom_info());
                intent2.putExtra("room_id", mHouseBean.getRoom_info().getRoom_id());
                startActivity(intent2);
                break;
            case R.id.iv_quanxian://业主权限
                QuanXianDialog dialog = new QuanXianDialog(this);
                dialog.show();
                break;
            case R.id.iv_addyz://添加业主家庭成员
                fwAddPersonDialog = new FWAddPersonDialog(this, room_id, "2", new FWAddPersonDialog.OnCustomDialogListener() {
                    @Override
                    public void back(String name) {
                        if (name.equals("1")) {
                            //刷新界面
                            getinfo();
                            MyCookieStore.fw_delete = 1;
                            //  closeInputMethod();

                        }
                    }
                });
                fwAddPersonDialog.show();
                break;
            case R.id.iv_deleteAllyz://删除全部业主家人
                if (mHouseBean.getPer_li().size() == 1) {
                    isdeleteAll = false;
                } else {
                    if (isdeleteAll == false) {
                        isdeleteAll = true;
                    } else {
                        isdeleteAll = false;
                    }
                    mHouseYZListAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.iv_quanxian2://权限
                QuanXianDialog dialog1 = new QuanXianDialog(this);
                dialog1.show();
                break;
            case R.id.iv_addzk://添加租户成员
                fwAddPersonDialog = new FWAddPersonDialog(this, room_id, "3", new FWAddPersonDialog.OnCustomDialogListener() {
                    @Override
                    public void back(String name) {
                        if (name.equals("1")) {
                            //刷新界面
                            getinfo();
                            MyCookieStore.fw_delete = 1;
                        }
                    }
                });
                fwAddPersonDialog.show();
                break;
            case R.id.iv_deleteAllzk://删除全部租户
               /*  if (isdeleteAll == false) {
                    isdeleteAll = true;
                } else {
                    isdeleteAll = false;
                }*/
                if (mHouseBean.getPer_li_() != null) { //判断有没有租户
                    if (isdeleteZh == false) {
                        isdeleteZh = true;
                    } else {
                        isdeleteZh = false;
                    }
                    mHouseZHListAdapter.notifyDataSetChanged();
                } else {
                    isdeleteZh = false;
                }

                break;
            case R.id.lin_left:
                finish();
                break;
            case R.id.lin_zuke:
                startActivity(new Intent(this, FaBuActivity.class));
                break;
        }
    }

    private void getResult() {//判断访客邀请功能是否开放
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(info.checkIsAjb, params, this) {


            @Override
            protected void setData(String json) {
                JSONObject jsonObject, jsonData;
                hideDialog(smallDialog);
                try {
                    jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        jsonData = new JSONObject(data);
                        String mobile = jsonData.getString("mobile");
                        String community = jsonData.getString("community");
                        String building = jsonData.getString("building");
                        String room_code = jsonData.getString("room_code");
                        Intent intent = new Intent(HousePersonInfoActivity.this, HouseInviteActivity.class);
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("community", community);
                        intent.putExtra("building", building);
                        intent.putExtra("room_code", room_code);
                        intent.putExtra("room_id", mHouseBean.getRoom_info().getRoom_id());
                        startActivity(intent);

                    } else {
                        XToast.makeText(HousePersonInfoActivity.this, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
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

    public class HouseYZListAdapter extends BaseAdapter {
        private List<WuYeBean> mList;
        String strinfo;
        ShopProtocol mShopProtocol = new ShopProtocol();

        public HouseYZListAdapter(Context mContext, List<WuYeBean> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(HousePersonInfoActivity.this).inflate(R.layout.house_info_item, null, false);

                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.iv_photo = (CircularImage) convertView.findViewById(R.id.iv_photo);
                holder.lin_type = (LinearLayout) convertView.findViewById(R.id.lin_type);
                holder.lin_xuxian = (LinearLayout) convertView.findViewById(R.id.lin_xuxian);
                holder.iv_detete = (ImageView) convertView.findViewById(R.id.iv_detete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(HousePersonInfoActivity.this).load(StringUtils.getImgUrl(mList.get(position).getAvatars())).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(holder.iv_photo);
            holder.tv_name.setText(mList.get(position).getNickname());
            holder.tv_phone.setText(mList.get(position).getUsername());
            if (mList.get(position).getBind_type().equals("1")) {//显示业主
                holder.lin_type.setVisibility(View.VISIBLE);
                holder.iv_detete.setVisibility(View.GONE);
            } else {
                holder.lin_type.setVisibility(View.GONE);
                if (isdeleteAll == false) {
                    holder.iv_detete.setVisibility(View.GONE);
                } else {
                    holder.iv_detete.setVisibility(View.VISIBLE);
                }
            }
            if (position + 1 == mList.size()) {
                holder.lin_xuxian.setVisibility(View.GONE);
            } else {
                holder.lin_xuxian.setVisibility(View.VISIBLE);
            }
            holder.iv_detete.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {

                    new CommomDialog(HousePersonInfoActivity.this, R.style.my_dialog_DimEnabled, "确定要删除吗？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                getdelete(mList.get(position).getId(), position);

                                dialog.dismiss();
                            }
                        }
                    }).show();//.setTitle("提示")

                }
            });
            return convertView;
        }

        private void getdelete(String delete_id, final int p) {//添加家庭成员或是租户成员
            showDialog(smallDialog);
            RequestParams params = new RequestParams();
            params.addBodyParameter("id", delete_id);
            HttpHelper hh = new HttpHelper(MyCookieStore.SERVERADDRESS + "property/set_bind_status/",
                    params, HousePersonInfoActivity.this) {

                @Override
                protected void setData(String json) {
                    hideDialog(smallDialog);
                    strinfo = mShopProtocol.setShop(json);
                    if (strinfo.equals("1")) {
                        MyCookieStore.fw_delete = 1;
                        mList.remove(p);
                        getinfo();
                        notifyDataSetChanged();

                    } else {
                        XToast.makeText(HousePersonInfoActivity.this, strinfo, XToast.LENGTH_SHORT).show();
                    }

                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    hideDialog(smallDialog);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                }
            };
        }

        private class ViewHolder {
            CircularImage iv_photo;
            TextView tv_name;
            TextView tv_phone;
            ImageView iv_detete;
            LinearLayout lin_type, lin_xuxian;


        }
    }

    public class HouseZHListAdapter extends BaseAdapter {
        private List<WuYeBean> mList;
        String strinfo;
        ShopProtocol mShopProtocol = new ShopProtocol();

        public HouseZHListAdapter(Context mContext, List<WuYeBean> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(HousePersonInfoActivity.this).inflate(R.layout.house_info_item, null, false);

                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.iv_photo = (CircularImage) convertView.findViewById(R.id.iv_photo);
                holder.lin_type = (LinearLayout) convertView.findViewById(R.id.lin_type);
                holder.lin_xuxian = (LinearLayout) convertView.findViewById(R.id.lin_xuxian);
                holder.iv_detete = (ImageView) convertView.findViewById(R.id.iv_detete);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(HousePersonInfoActivity.this).load(StringUtils.getImgUrl(mList.get(position).getAvatars())).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(holder.iv_photo);
            holder.tv_name.setText(mList.get(position).getNickname());
            holder.tv_phone.setText(mList.get(position).getUsername());
            holder.lin_type.setVisibility(View.GONE);
            //租户只可以删除租户
            if (isdeleteZh == false) {
                holder.iv_detete.setVisibility(View.GONE);
            } else {
                holder.iv_detete.setVisibility(View.VISIBLE);
            }
           /* //租户成员删除自己
            if (isdeleteZH == false) {
                holder.iv_detete.setVisibility(View.GONE);
            } else {
                holder.iv_detete.setVisibility(View.VISIBLE);
            }*/
            if (position + 1 == mList.size()) {
                holder.lin_xuxian.setVisibility(View.GONE);
            } else {
                holder.lin_xuxian.setVisibility(View.VISIBLE);
            }
            holder.iv_detete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new CommomDialog(HousePersonInfoActivity.this, R.style.my_dialog_DimEnabled, "确定要删除吗？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                getdelete(mList.get(position).getId(), position);

                                dialog.dismiss();
                            }
                        }
                    }).show();//.setTitle("提示")
                }
            });

            return convertView;
        }

        private void getdelete(String delete_id, final int p) {//添加家庭成员或是租户成员
            showDialog(smallDialog);
            RequestParams params = new RequestParams();
            params.addBodyParameter("id", delete_id);
            HttpHelper hh = new HttpHelper(MyCookieStore.SERVERADDRESS + "property/set_bind_status/",
                    params, HousePersonInfoActivity.this) {

                @Override
                protected void setData(String json) {
                    hideDialog(smallDialog);
                    strinfo = mShopProtocol.setShop(json);
                    if (strinfo.equals("1")) {
                        MyCookieStore.fw_delete = 1;
                        mList.remove(p);
                        getinfo();
                        notifyDataSetChanged();

                    } else {
                        XToast.makeText(HousePersonInfoActivity.this, strinfo, XToast.LENGTH_SHORT).show();
                    }
                }

                @Override
                protected void requestFailure(Exception error, String msg) {
                    hideDialog(smallDialog);
                    UIUtils.showToastSafe("网络异常，请检查网络设置");
                }
            };
        }

        private class ViewHolder {
            CircularImage iv_photo;
            TextView tv_name;
            TextView tv_phone;
            LinearLayout lin_type, lin_xuxian;
            ImageView iv_detete;


        }
    }
}
