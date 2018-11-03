package com.huacheng.huiservers.house.card;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.ToolUtils;

import java.util.List;

import static com.huacheng.huiservers.R.id.iv_img;

/**
 * 类：
 * 时间：2018/3/24 10:47
 * 功能描述:Huiservers
 */
public class HouseBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<HouseBean> mBeanList;

    public HouseBookAdapter(Context context, List<HouseBean> mBeanList) {
        this.mContext = context;
        this.mBeanList = mBeanList;

    }

    @Override
    public int getCount() {
        return mBeanList.size();
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
        if (null == convertView) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.new_house_handbook_item_img, null);
            holder.iv_img = (ImageView) convertView.findViewById(iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取图片的宽高--start
        holder.iv_img.getLayoutParams().width = ToolUtils.getScreenWidth(mContext) / 2;
        Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext) / 2) / (Double.valueOf(mBeanList.get(position).getImg_size()));
        holder.iv_img.getLayoutParams().height = (new Double(d)).intValue();

        Glide.with(mContext).load(MyCookieStore.URL + mBeanList.get(position).getImg()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.icon_girdview).error(R.drawable.icon_girdview).into(holder.iv_img);

        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mBeanList.get(position).getUrl())) {
                    if (mBeanList.get(position).getUrl_type().equals("0") || TextUtils.isEmpty(mBeanList.get(position).getUrl_type())) {
                        Jump jump = new Jump(mContext, mBeanList.get(position).getType_name(), mBeanList.get(position).getAdv_inside_url());
                    } else {
                        Jump jump = new Jump(mContext, mBeanList.get(position).getUrl_type(), mBeanList.get(position).getType_name(), "", "");
                    }
                } else {//URL不为空时外链
                    Jump jump = new Jump(mContext, mBeanList.get(position).getUrl());

                }


            }
        });
        return convertView;
    }

    public class ViewHolder {

        private ImageView iv_img;


    }
}
