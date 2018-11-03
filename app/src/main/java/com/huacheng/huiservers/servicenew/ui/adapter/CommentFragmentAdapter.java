package com.huacheng.huiservers.servicenew.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelCommentItem;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description: 评论Adapter
 * created by wangxiaotao
 * 2018/9/4 0004 下午 3:13
 */
public class CommentFragmentAdapter <T>extends BaseAdapter {
    private List<T> mDatas;

    public CommentFragmentAdapter(List<T> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    private Context mContext;


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.layout_comment_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ModelCommentItem item = (ModelCommentItem) getItem(position);

        FrescoUtils.getInstance().setImageUri(viewHolder.sdv_head,  StringUtils.getImgUrl(item.getAvatars()));
        if ("1".equals(item.getAnonymous())){
            viewHolder.tv_user_name.setText("***");
        }else {
            viewHolder.tv_user_name.setText(item.getNickname()+"");
        }
        try {
            int score = Integer.parseInt(item.getScore() + "");
            viewHolder.ratingBar.setCountNum(5);
            viewHolder.ratingBar.setCountSelected(score);
            viewHolder.tv_content.setText(item.getEvaluate()+"");
            viewHolder.tv_reply.setVisibility(View.GONE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String format = simpleDateFormat.format(new Date(item.getEvaluatime()*1000));
            viewHolder.tv_time.setText(format+"");
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
    static class ViewHolder{
        SimpleDraweeView sdv_head;
        TextView tv_user_name;
        XLHRatingBar ratingBar;
        TextView tv_time;
        TextView tv_content;
        TextView tv_reply;
        public ViewHolder(View conertView){
            sdv_head=conertView.findViewById(R.id.sdv_head) ;
            tv_user_name=conertView.findViewById(R.id.tv_user_name);
            ratingBar=conertView.findViewById(R.id.ratingBar);
            tv_time=conertView.findViewById(R.id.tv_time);
            tv_content=conertView.findViewById(R.id.tv_content);
            tv_reply=conertView.findViewById(R.id.tv_reply);
        }

    }
}

