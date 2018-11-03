package com.huacheng.huiservers.servicenew.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelOrderList;
import com.huacheng.huiservers.servicenew.ui.order.ServiceOrderDetailCommonActivity;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.StringUtils;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 类描述：订单列表item
 * 时间：2018/7/20 11:58
 * created by DFF
 */
public class FragmentOrderAdapter extends BaseAdapter {
    private Context mContext;
    private int type;
    List<ModelOrderList> mdatas;

    public FragmentOrderAdapter(Context mContext, List<ModelOrderList> mdatas, int type) {
        this.mContext = mContext;
        this.type = type;
        this.mdatas = mdatas;
    }

    @Override
    public int getCount() {
        return mdatas.size();
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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.fragment_order_common_item, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTvName.setText(mdatas.get(position).getS_name());
        viewHolder.mTvAdress.setText("地址：" + mdatas.get(position).getAddress());
        if (!NullUtil.isStringEmpty(mdatas.get(position).getDescription())) {
            viewHolder.mTvBeizhu.setText("备注：" + mdatas.get(position).getDescription());
        } else {
            viewHolder.mTvBeizhu.setText("备注：无");
        }
        FrescoUtils.getInstance().setImageUri(viewHolder.mSinpleIcon, ApiHttpClient.IMG_SERVICE_URL + mdatas.get(position).getTitle_img());
        //订单状态(1->待派单 2->已派单/待上门 3->待付款 4->已完成未评价 5->已完成已评价 6->取消订单)
        if (type == 0) {//全部

            if (mdatas.get(position).getStatus().equals("1")) {
                viewHolder.mTvStatusTxt.setText("待派单");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type1));
                viewHolder.mTvBtnType.setText("取消订单");
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("2")) {
                viewHolder.mTvStatusTxt.setText("已派单");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type1));
                viewHolder.mTvBtnType.setText("取消订单");
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("3")) {
                viewHolder.mTvStatusTxt.setText("服务中");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type2));
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("4")) {//已完成订单 但是未评价状态
                viewHolder.mTvStatusTxt.setText("待评价");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type2));
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mTvBtnType.setText("评价");
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("5")) {//已完成并且是已评价状态
                viewHolder.mTvStatusTxt.setText("完成");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type3));
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.VISIBLE);

                viewHolder.mRatingBar.setCountSelected(Integer.valueOf(mdatas.get(position).getScore()));
                viewHolder.mTvCommentTime.setText(StringUtils.getDateToString(mdatas.get(position).getEvaluatime(), "2"));
                viewHolder.mTvComment.setText(mdatas.get(position).getEvaluate());

            } else if (mdatas.get(position).getStatus().equals("6")) {//订单已取消
                viewHolder.mTvStatusTxt.setText("订单已取消");
                viewHolder.mIvStatus.setVisibility(View.GONE);
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.GONE);
            }

        } else if (type == 1) {//待服务

            if (mdatas.get(position).getStatus().equals("1")) {
                viewHolder.mTvStatusTxt.setText("待派单");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type1));
                viewHolder.mTvBtnType.setText("取消订单");
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("2")) {
                viewHolder.mTvStatusTxt.setText("已派单");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type1));
                viewHolder.mTvBtnType.setText("取消订单");
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mLyComment.setVisibility(View.GONE);
            }

        } else if (type == 2) {//待评价

            if (mdatas.get(position).getStatus().equals("3")) {
                viewHolder.mTvStatusTxt.setText("服务中");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type2));
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.GONE);

            } else if (mdatas.get(position).getStatus().equals("4")) {//已完成订单 但是未评价状态
                viewHolder.mTvStatusTxt.setText("待评价");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type2));
                viewHolder.mTvBtnType.setVisibility(View.VISIBLE);
                viewHolder.mTvBtnType.setText("评价");
                viewHolder.mLyComment.setVisibility(View.GONE);
            }

        } else if (type == 3) {//已完成

            if (mdatas.get(position).getStatus().equals("5")) {//已完成并且是已评价状态
                viewHolder.mTvStatusTxt.setText("完成");
                viewHolder.mIvStatus.setVisibility(View.VISIBLE);
                viewHolder.mIvStatus.setBackground(mContext.getResources().getDrawable(R.mipmap.ic_service_order_type3));
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.VISIBLE);

                viewHolder.mRatingBar.setCountSelected(Integer.valueOf(mdatas.get(position).getScore()));
                viewHolder.mTvCommentTime.setText(StringUtils.getDateToString(mdatas.get(position).getEvaluatime(), "2"));
                viewHolder.mTvComment.setText(mdatas.get(position).getEvaluate());

            } else if (mdatas.get(position).getStatus().equals("6")) {//订单已取消
                viewHolder.mTvStatusTxt.setText("订单已取消");
                viewHolder.mIvStatus.setVisibility(View.GONE);
                viewHolder.mTvBtnType.setVisibility(View.GONE);
                viewHolder.mLyComment.setVisibility(View.GONE);
            }
        }
        viewHolder.mTvBtnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mdatas.get(position).getStatus().equals("1") || mdatas.get(position).getStatus().equals("2") || mdatas.get(position).getStatus().equals("3")) {
                    //  2018/9/6   //取消订单跳转

                    Intent intent = new Intent(mContext, ServiceOrderDetailCommonActivity.class);
                    intent.putExtra("type",0);
                    intent.putExtra("order_id",mdatas.get(position).getId()+"");
                    mContext.startActivity(intent);
                } else if (mdatas.get(position).getStatus().equals("4")) {
                    //  2018/9/6  //评价订单
                    Intent intent = new Intent(mContext, ServiceOrderDetailCommonActivity.class);
                    intent.putExtra("type",2);
                    intent.putExtra("order_id",mdatas.get(position).getId()+"");
                    mContext.startActivity(intent);
                }
            }
        });




       /* viewHolder.tv_shangmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.click(datas.get(position), type);
            }
        });*/
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.sinple_icon)
        SimpleDraweeView mSinpleIcon;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_status_txt)
        TextView mTvStatusTxt;
        @BindView(R.id.iv_status)
        ImageView mIvStatus;
        @BindView(R.id.tv_adress)
        TextView mTvAdress;
        @BindView(R.id.tv_beizhu)
        TextView mTvBeizhu;
        @BindView(R.id.tv_btn_type)
        TextView mTvBtnType;
        @BindView(R.id.ratingBar)
        XLHRatingBar mRatingBar;
        @BindView(R.id.tv_comment_time)
        TextView mTvCommentTime;
        @BindView(R.id.tv_comment)
        TextView mTvComment;
        @BindView(R.id.tv_reply)
        TextView mTvReply;
        @BindView(R.id.ly_reply)
        LinearLayout mLyReply;
        @BindView(R.id.ly_comment)
        LinearLayout mLyComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
