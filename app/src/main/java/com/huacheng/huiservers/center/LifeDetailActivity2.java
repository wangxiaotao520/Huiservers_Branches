package com.huacheng.huiservers.center;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.huodong.ImagePagerActivity;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.RepairDetailImgsAdapter;
import com.huacheng.huiservers.center.bean.Imgs;
import com.huacheng.huiservers.center.bean.PropertyRepair;
import com.huacheng.huiservers.center.bean.PropertyRepairDetail;
import com.huacheng.huiservers.center.bean.Reply;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.huiservers.view.MyGridview;
import com.huacheng.huiservers.view.MyListView;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class LifeDetailActivity2 extends BaseUI implements OnClickListener {
    private LinearLayout lin_left;
    private RelativeLayout rel_detail_schedule;
    private TextView title_name, tv_type_cn, tv_description, tv_is_replace, tv_starttime, tv_admin_level_name,
    tv_admin_process_date,
    tv_admin_process_status_mark,
    tv_admin_process_status,tv_address_val;
    private MyGridview mgv_imgs;
    MyListView myList;
    ListAdapter listAdapter;
    String participateID;
    CircularImage circularimg_head1;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.life_detail2);
  //      SetTransStatus.GetStatus(this);//系统栏默认为黑色
        title_name = (TextView) findViewById(R.id.title_name);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        myList = (MyListView) findViewById(R.id.myList);
        tv_type_cn = (TextView) findViewById(R.id.tv_type_cn);
        tv_address_val=(TextView) findViewById(R.id.tv_address_val);
        tv_description = (TextView) findViewById(R.id.tv_description);
        mgv_imgs = (MyGridview) findViewById(R.id.mgv_imgs);
        tv_is_replace = (TextView) findViewById(R.id.tv_is_replace);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        circularimg_head1 = (CircularImage) findViewById(R.id.circularimg_head1);
        tv_admin_level_name = (TextView) findViewById(R.id.tv_admin_level_name);
        tv_admin_process_date = (TextView) findViewById(R.id.tv_admin_process_date);
        tv_admin_process_status_mark = (TextView) findViewById(R.id.tv_admin_process_status_mark);
        tv_admin_process_status = (TextView) findViewById(R.id.tv_admin_process_status);
        rel_detail_schedule = (RelativeLayout) findViewById(R.id.rel_detail_schedule);
        participateID = this.getIntent().getExtras().getString("participateID");
        title_name.setText("订单详情");
        lin_left.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                finish();
                break;
            default:
                break;
        }
    }

    /***
     * 图片放大
     */
    private void ImageZoom(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }


    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        getDetail();
    }

    PropertyRepairDetail propertyRepairDetail = new PropertyRepairDetail();

    private void getDetail() {//维修订单详情
        showDialog(smallDialog);
        Url_info info=new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", participateID);
        new HttpHelper(info.repair_info, params, LifeDetailActivity2.this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                CenterProtocol protocol = new CenterProtocol();
                propertyRepairDetail = protocol.getPropertyRepairsDetails(json);
                if (propertyRepairDetail != null) {
                    PropertyRepair repair = propertyRepairDetail.getPropertyRepair();
                    if (repair != null) {
                        tv_type_cn.setText(repair.getType_cn());
                        String address= repair.getAddress();
                        if(!StringUtils.isEmpty(address) ){
                            tv_address_val.setText(address);
                        }
                        tv_description.setText(repair.getDescription());
                        String is_replace = repair.getIs_replace();
                        if (is_replace.equals("1")) {
                            tv_is_replace.setText("需要更换部件");
                        } else if (is_replace.equals("2")) {
                            tv_is_replace.setText("不需要更换部件");
                        }
                        String startTime = repair.getStarttime();
                        String endTime = repair.getEndtime();
                        startTime = StringUtils.getDateToString(startTime, "1");
                        if (!endTime.equals("0")) {
                            endTime = StringUtils.getDateToString(endTime, "4");
                        }
                        tv_starttime.setText("预约时间：" + startTime + "-" + endTime);
                        //-------------------------------------------------
                        List<Imgs> imgs = propertyRepairDetail.getImgs();
                        final ArrayList<String> imgList = new ArrayList<String>();
                        if (imgs != null) {
                            for (int j = 0; j < imgs.size(); j++) {
                                imgList.add(imgs.get(j).getImg());
                            }
                        }
                        //-------------------------------------------------
                        //加载图片
                        mgv_imgs.setVisibility(View.VISIBLE);
                        RepairDetailImgsAdapter imgsAdapter = new RepairDetailImgsAdapter(imgList, LifeDetailActivity2.this);
                        mgv_imgs.setAdapter(imgsAdapter);


                        String detailStatus = repair.getStatus();
                        if (detailStatus.equals("0")) {//待处理
                            rel_detail_schedule.setVisibility(View.VISIBLE);
                            myList.setVisibility(View.GONE);
                            Glide
                                    .with(LifeDetailActivity2.this)
                                    .load(R.drawable.facehead1)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.facehead1)
                                    .error(R.drawable.facehead1)
                                    .into(circularimg_head1);
                            tv_admin_level_name.setText("华晟物业");
                            String addTime = StringUtils.getDateToString(repair.getAddtime(), "1");
                            tv_admin_process_date.setText(addTime);
                            tv_admin_process_status_mark.setBackground(getResources().getDrawable(R.drawable.dotshape_gray));
//                            tv_admin_process_status_mark.setTextColor(getResources().getColor(R.color.blackgray));
                            tv_admin_process_status.setTextColor(getResources().getColor(R.color.graynew));
                            tv_admin_process_status.setText("待处理");
                        } else if (detailStatus.equals("1")) {//已受理
                            rel_detail_schedule.setVisibility(View.GONE);
                            myList.setVisibility(View.VISIBLE);
                            listAdapter = new ListAdapter(propertyRepairDetail.getR_list(), LifeDetailActivity2.this);
                            myList.setAdapter(listAdapter);
                        } else if (detailStatus.equals("2")) {
                            rel_detail_schedule.setVisibility(View.GONE);
                            myList.setVisibility(View.VISIBLE);
                            listAdapter = new ListAdapter(propertyRepairDetail.getR_list(), LifeDetailActivity2.this);
                            myList.setAdapter(listAdapter);
                            /*rel_detail_schedule.setVisibility(View.VISIBLE);
                            myList.setVisibility(View.GONE);
                            Glide
                                    .with(LifeDetailActivity2.this)
                                    .load(R.drawable.icon_girdview)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.icon_girdview)
                                    .error(R.drawable.icon_girdview)
                                    .into(circularimg_head1);
                            tv_admin_level_name.setText("--");
                            String overTime = StringUtils.getDateToString(repair.getOvertime(), "1");
                            tv_admin_process_date.setText(overTime);
                            tv_admin_process_status_mark.setVisibility(View.GONE);
//                            tv_admin_process_status_mark.setBackground(getResources().getDrawable(R.drawable.dotshape_black));
//                            tv_admin_process_status_mark.setTextColor(getResources().getColor(R.color.blackgray));
                            tv_admin_process_status.setTextColor(getResources().getColor(R.color.blackgray));
                            tv_admin_process_status.setText("已完成");*/
                        }
                    }
                    //加载图片
                } else {
                    mgv_imgs.setVisibility(View.GONE);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        private List<Reply> replys;

        public ListAdapter(List<Reply> replys, Context mContext) {
            this.replys = replys;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return replys.size();
        }

        @Override
        public Object getItem(int position) {
            return replys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.life_detail_item, null);
                holder = new ViewHolder();
                holder.img_head = (CircularImage) convertView.findViewById(R.id.img_head1);
                holder.tv_life_detail_name = (TextView) convertView.findViewById(R.id.tv_life_detail_name);
                holder.tv_life_detail_date = (TextView) convertView.findViewById(R.id.tv_life_detail_date);
                holder.tv_life_detail_status_mark = (TextView) convertView.findViewById(R.id.tv_life_detail_status_mark);
                holder.tv_life_detail_status = (TextView) convertView.findViewById(R.id.tv_life_detail_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide
                    .with(LifeDetailActivity2.this)
                    .load(MyCookieStore.URL + replys.get(position).getAvatars())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon_girdview)
                    .error(R.drawable.icon_girdview)
                    .into(holder.img_head);

            String insName = replys.get(position).getIns_name();//物业公司
            String fullname = replys.get(position).getFullname();//名字
            if (!StringUtils.isEmpty(fullname)) {
                fullname="-"+fullname;
            } else {
                fullname="";
            }
            holder.tv_life_detail_name.setText(insName + fullname);
            String replyTime = StringUtils.getDateToString(replys.get(position).getAddtime(), "1");
            holder.tv_life_detail_date.setText(replyTime);
            holder.tv_life_detail_status_mark.setBackground(getResources().getDrawable(R.drawable.dotshape_black));
            //tv_admin_process_status_mark.setTextColor(getResources().getColor(R.color.blackgray));
            holder.tv_life_detail_status.setTextColor(getResources().getColor(R.color.blackgray));
            holder.tv_life_detail_status.setText( replys.get(position).getRepair());//"已受理 " +

            return convertView;
        }

        public class ViewHolder {
            private TextView tv_life_detail_name, tv_life_detail_date, tv_life_detail_status_mark, tv_life_detail_status;
            private CircularImage img_head;
        }
    }

}
