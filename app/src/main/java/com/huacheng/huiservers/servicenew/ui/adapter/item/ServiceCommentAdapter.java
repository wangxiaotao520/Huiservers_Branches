package com.huacheng.huiservers.servicenew.ui.adapter.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelServiceDetail;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：评论
 * 时间：2018/9/7 16:28
 * created by DFF
 */
public class ServiceCommentAdapter extends BaseAdapter {
    private Context mContext;
    List<ModelServiceDetail.ScoreInfoBean> mdatas;

    public ServiceCommentAdapter(Context mContext, List<ModelServiceDetail.ScoreInfoBean> mdatas) {

        this.mContext = mContext;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FrescoUtils.getInstance().setImageUri(holder.mSdvHead, StringUtils.getImgUrl(mdatas.get(position).getAvatars()));
        holder.mTvUserName.setText(mdatas.get(position).getNickname());
        holder. mRatingBar.setCountSelected(Integer.valueOf(mdatas.get(position).getScore()));
        holder.mTvTime.setText(StringUtils.getDateToString(mdatas.get(position).getEvaluatime(), "8"));
        holder. mTvContent.setText(mdatas.get(position).getEvaluate());
        holder. mTvReply.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.sdv_head)
        SimpleDraweeView mSdvHead;
        @BindView(R.id.tv_user_name)
        TextView mTvUserName;
        @BindView(R.id.ratingBar)
        XLHRatingBar mRatingBar;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_reply)
        TextView mTvReply;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
