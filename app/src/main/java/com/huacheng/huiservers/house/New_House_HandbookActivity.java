package com.huacheng.huiservers.house;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.card.HouseBookAdapter;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.HouseProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.huacheng.huiservers.R.id.lin_activity;

/**
 * 类：
 * 时间：2018/3/24 09:14
 * 功能描述:Huiservers
 */
public class New_House_HandbookActivity extends BaseUI {
    private ListView mlist;
    private ImageView iv_book;
    HouseProtocol mHouseProtocol = new HouseProtocol();
    List<HouseBean> mList = new ArrayList<HouseBean>();
    NewBookAdapter mBookAdapter;
    private LinearLayout lin_left;
    SharedPreferences preferencesLogin;
    String login_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_house_handbook);
  //      SetTransStatus.GetStatus(this);

        mlist = (ListView) findViewById(R.id.list);
        iv_book = (ImageView) findViewById(R.id.iv_book);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);

        getNewHouse();

        iv_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLinshi();
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    Intent intent = new Intent(New_House_HandbookActivity.this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                } else if (login_type.equals("1")) {
                    Intent intent = new Intent(New_House_HandbookActivity.this, HouseMemorandumActivity.class);
                    startActivity(intent);
                } else {
                    XToast.makeText(New_House_HandbookActivity.this, "当前账号不是个人账号", XToast.LENGTH_SHORT).show();
                }

            }
        });
        lin_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getLinshi() {
        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
    }

    private void getNewHouse() {//获取我的新房详情
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        HttpHelper hh = new HttpHelper(info.get_new_house, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mList = mHouseProtocol.getNewHouse(json);
                mBookAdapter = new NewBookAdapter(New_House_HandbookActivity.this, mList);
                mlist.setAdapter(mBookAdapter);

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");

            }
        };
    }

    public class NewBookAdapter extends BaseAdapter {
        private Context mContext;
        List<HouseBean> mHouseBeanList;

        public NewBookAdapter(Context context, List<HouseBean> mHouseBeanList) {
            this.mContext = context;
            this.mHouseBeanList = mHouseBeanList;
        }

        @Override
        public int getCount() {
            return mHouseBeanList.size();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.new_house_handbook_item, null, false);

                holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                holder.tv_fw_content = (TextView) convertView.findViewById(R.id.tv_fw_content);
                holder.tv_act_content = (TextView) convertView.findViewById(R.id.tv_act_content);
                holder.lin_activity = (LinearLayout) convertView.findViewById(lin_activity);
                holder.rel_fw = (RelativeLayout) convertView.findViewById(R.id.rel_fw);
                holder.grid = (GridView) convertView.findViewById(R.id.grid);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //获取图片的宽高--start

            if (!mHouseBeanList.get(position).getImg_size().equals("")) {
                holder.iv_img.getLayoutParams().width = ToolUtils.getScreenWidth(mContext) - 60;
                Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) - 60) / (Double.valueOf(mHouseBeanList.get(position).getImg_size()));
                holder.iv_img.getLayoutParams().height = (new Double(d)).intValue();
            }
            Glide.with(New_House_HandbookActivity.this).load(MyCookieStore.URL + mHouseBeanList.get(position).getArticle_image()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_girdview).error(R.drawable.icon_girdview).into(holder.iv_img);

            if (position < 9) {
                holder.tv_fw_content.setText("0" + mHouseBeanList.get(position).getOrder_num() + "." +
                        mHouseBeanList.get(position).getTitle());
            } else {
                holder.tv_fw_content.setText(mHouseBeanList.get(position).getOrder_num() + "." +
                        mHouseBeanList.get(position).getTitle());
            }
            holder.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(mHouseBeanList.get(position).getUrl())) {
                        if (mHouseBeanList.get(position).getUrl_type_id().equals("0") || TextUtils.isEmpty(mHouseBeanList.get(position).getUrl_type_id())) {
                            // Jump jump = new Jump(mContext, mHouseBeanList.get(position).getType_name(), mHouseBeanList.get(position).getAdv_inside_url());
                            // } else {
                            Jump jump = new Jump(mContext, mHouseBeanList.get(position).getUrl_type_id(), mHouseBeanList.get(position).getUrl_type_name(), "", "");
                        }

                    } else {//URL新房链接
                        Jump jump = new Jump(mContext, mHouseBeanList.get(position).getUrl(), 0);

                    }

                }
            });

            if (mHouseBeanList.get(position).getList() != null) {
                holder.lin_activity.setVisibility(View.VISIBLE);
                holder.tv_act_content.setText(mHouseBeanList.get(position).getList().getInfo());

                HouseBookAdapter bookAdapter = new HouseBookAdapter(mContext, mHouseBeanList.get(position).getList().getLi());
                holder.grid.setAdapter(bookAdapter);
                setHorizontalGridView(mHouseBeanList.get(position).getList().getLi().size(), holder.grid);
            } else {
                holder.lin_activity.setVisibility(View.GONE);
            }


            return convertView;
        }

        private class ViewHolder {
            TextView tv_fw_content, tv_act_content;
            ImageView iv_img;
            LinearLayout lin_activity;
            RelativeLayout rel_fw;
            GridView grid;


        }
    }

    /**
     * 水平滚动的GridView的控制
     */
    private void setHorizontalGridView(int siz, GridView gridView) {
        int size = siz;
//      int length = (int) getActivity().getResources().getDimension(
//              R.dimen.coreCourseWidth);
        int length = 165;
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length) * density);
        int itemWidth = (int) ((length) * density);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(0); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数


    }
}
