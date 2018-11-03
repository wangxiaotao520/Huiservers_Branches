package com.huacheng.huiservers.house;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajb.opendoor.data.api.AjbInterface;
import com.ajb.opendoor.data.api.RecordListCallBack;
import com.ajb.opendoor.data.bean.RecordRsp;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.wuye.bean.WuYeBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类：
 * 时间：2018/3/23 15:12
 * 功能描述:Huiservers
 */
public class HouseOpenKeepActivity extends BaseUI {

    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.list)
    ListView mList;

    List<BannerBean> List = new ArrayList<BannerBean>();
    BannerBean adinfo;

    /**
     * 截至时间数据源
     **/
    private List<Date> listData;
    /**
     * 当前时间
     **/
    private long time_Current;
    /**
     * 适配器
     **/
    private HouseOpenKeepAdapter mHouseOpenKeepAdapter;

    //这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            time_Current = time_Current + 1000;
            mHouseOpenKeepAdapter.notifyDataSetChanged();
            handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_keep_jilu);
        ButterKnife.bind(this);
 //       SetTransStatus.GetStatus(this);
        Intent intent = getIntent();
        mTitleName.setText("通行证记录");
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("username", intent.getStringExtra("mobile"));
        params.put("estatecode", intent.getStringExtra("community"));
        params.put("housecode", intent.getStringExtra("building") + intent.getStringExtra("room_code"));
        String addr = "http://47.104.92.9:8080";
        AjbInterface.getRecordList(addr, params, new RecordListCallBack() {
            @Override
            public void onCallBack(RecordRsp recordRsp) {
                hideDialog(smallDialog);
                if (recordRsp.status) {
                    Log.d("qweqeqeqeq", recordRsp.data.toString());
                    for (int i = 0; i < recordRsp.data.size(); i++) {
                        adinfo = new BannerBean();
                        adinfo.setName(recordRsp.data.get(i).guestName);
                        adinfo.setTempPass(recordRsp.data.get(i).valiateTime);
                        adinfo.setState(recordRsp.data.get(i).state + "");
                        adinfo.setAjbPass(recordRsp.data.get(i).tempPass);
                        adinfo.setAjbisQRorTP(recordRsp.data.get(i).isQRorTP + "");
                        List.add(adinfo);
                    }
                }
            }
        });

        listData = new ArrayList<Date>();
        listData.add(new Date(2016, 3, 16, 8, 20, 31));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 13, 21, 22));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 21, 23));
        listData.add(new Date(2016, 3, 16, 14, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 21, 23));
        listData.add(new Date(2016, 3, 16, 8, 21, 24));
        listData.add(new Date(2016, 3, 16, 8, 21, 20));
        listData.add(new Date(2016, 3, 16, 8, 22, 25));
        listData.add(new Date(2016, 3, 16, 8, 23, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 26));
        listData.add(new Date(2016, 3, 16, 8, 25, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 25));
        listData.add(new Date(2016, 3, 16, 8, 25, 20));
        listData.add(new Date(2016, 3, 16, 8, 24, 26));
        listData.add(new Date(2016, 3, 16, 11, 20, 20));
        listData.add(new Date(2016, 3, 16, 14, 40, 20));
        listData.add(new Date(2016, 3, 16, 8, 44, 20));
        listData.add(new Date(2016, 3, 16, 10, 20, 20));

        //模拟当前服务器时间数据
        Date date = new Date(2016, 3, 16, 8, 20, 20);
        time_Current = date.getTime();

        mHouseOpenKeepAdapter = new HouseOpenKeepAdapter(this);
        mList.setAdapter(mHouseOpenKeepAdapter);

        handler_timeCurrent.sendEmptyMessageDelayed(0, 1000);

        mLinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*
     * 将时间转换为时间戳
     */
    public String dateToStamp(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    public String dateToStamp1(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    //防止当前Activity结束以后,   handler依然继续循环浪费资源
    @Override
    protected void onDestroy() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    public class HouseOpenKeepAdapter extends BaseAdapter {
        private List<WuYeBean> mList;

        public HouseOpenKeepAdapter(Context mContext) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return List.size();
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
                convertView = LayoutInflater.from(HouseOpenKeepActivity.this).inflate(R.layout.house_keep_item, null, false);
                holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
                holder.tv_minute = (TextView) convertView.findViewById(R.id.tv_minute);
                holder.tv_second = (TextView) convertView.findViewById(R.id.tv_second);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_st = (TextView) convertView.findViewById(R.id.tv_st);
                holder.ly = (LinearLayout) convertView.findViewById(R.id.ly);
                holder.rel_onclick = (RelativeLayout) convertView.findViewById(R.id.rel_onclick);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String sp = List.get(position).getName().substring(0, List.get(position).getName().indexOf("通"));
            holder.tv_name.setText(sp);
            //Date date_finish = listData.get(position);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            //获取当前时间
            String str = formatter.format(curDate);

            try {
                if (Long.parseLong(dateToStamp(List.get(position).getTempPass())) - Long.parseLong(dateToStamp1(str)) > 0) {
                    updateTextView(Long.parseLong(dateToStamp(List.get(position).getTempPass()))
                            - Long.parseLong(dateToStamp1(str)), holder);
                    holder.tv_st.setText("后过期");
                    holder.ly.setVisibility(View.VISIBLE);
                    holder.rel_onclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (List.get(position).getAjbisQRorTP().equals("1")) {//通行证
                                Intent intent = new Intent(HouseOpenKeepActivity.this, HousePassActivity.class);
                                intent.putExtra("ajb_tagname", List.get(position).getAjbPass());
                                intent.putExtra("ajb_type", "2");
                                startActivity(intent);
                            } else if (List.get(position).getAjbisQRorTP().equals("2")) {//二维码
                                Intent intent = new Intent(HouseOpenKeepActivity.this, HouseCodeActivity.class);
                                intent.putExtra("ajb_tagname", List.get(position).getAjbPass());
                                intent.putExtra("ajb_type", "2");
                                startActivity(intent);

                            }
                        }
                    });

                } else {
                    holder.tv_st.setText("已失效");
                    holder.ly.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // updateTextView(date_finish.getTime() - time_Current, holder);

            return convertView;
        }

        private class ViewHolder {
            TextView tv_hour;
            TextView tv_second;
            TextView tv_minute;
            TextView tv_name;
            TextView tv_st;
            LinearLayout ly;
            RelativeLayout rel_onclick;

        }

        public void updateTextView(long times_remain, ViewHolder hoder) {

            if (times_remain <= 0) {
                hoder.tv_hour.setText("00");
                hoder.tv_minute.setText("00");
                hoder.tv_second.setText("00");
                return;
            }
            //秒钟
            long time_second = (times_remain / 1000) % 60;
            String str_second;
            if (time_second < 10) {
                str_second = "0" + time_second;
            } else {
                str_second = "" + time_second;
            }

            long time_temp = ((times_remain / 1000) - time_second) / 60;
            //分钟
            long time_minute = time_temp % 60;
            String str_minute;
            if (time_minute < 10) {
                str_minute = "0" + time_minute;
            } else {
                str_minute = "" + time_minute;
            }

            time_temp = (time_temp - time_minute) / 60;
            //小时
            long time_hour = time_temp;
            String str_hour;
            if (time_hour < 10) {
                str_hour = "0" + time_hour;
            } else {
                str_hour = "" + time_hour;
            }

            hoder.tv_hour.setText(str_hour);
            hoder.tv_minute.setText(str_minute);
            hoder.tv_second.setText(str_second);

        }
    }

}
