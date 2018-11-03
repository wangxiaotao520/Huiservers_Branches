package com.huacheng.huiservers.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.model.ModelCategoryBean;
import com.huacheng.libraryservice.http.ApiHttpClient;

import java.util.List;

/**
 * GridView加载数据的适配器
 *
 * @author Administrator
 */
public class ServiceMyGridViewAdpter extends BaseAdapter {

    private Context context;
    private List<ModelCategoryBean> lists;//数据源
    private int mIndex; // 页数下标，标示第几页，从0开始
    private int mPargerSize;// 每页显示的最大的数量

    //private int num=9;
    public ServiceMyGridViewAdpter(Context context, List<ModelCategoryBean> lists,
                                   int mIndex, int mPargerSize) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        if (lists.size() > 0) {
            return (lists.size() + 1) > (mIndex + 1) * mPargerSize ?
                    mPargerSize : ((lists.size() + 1) - mIndex * mPargerSize);
        } else {
            return 0;
        }
        /*return lists.size() > (mIndex + 1) * mPargerSize ?
                mPargerSize : (lists.size() - mIndex * mPargerSize);*/

    }

    @Override
    public Object getItem(int arg0) {
        return lists.get((arg0 + 1) + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int arg0) {
        return (arg0 + 1) + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            /*convertView = View.inflate(context, R.layout.item_view, null);
			holder.tv_name = (TextView)convertView.findViewById(R.id.item_name);
			holder.iv_nul = (ImageView)convertView.findViewById(R.id.item_image);*/
            convertView = View.inflate(context, R.layout.home_my_cate_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_nul = (ImageView) convertView.findViewById(R.id.iv_img);
//            holder.txt_shop_price = (TextView)convertView.findViewById(R.id.txt_shop_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //重新确定position因为拿到的总是数据源，数据源是分页加载到每页的GridView上的
        final int pos = position + mIndex * mPargerSize;//假设mPageSiez
        if (pos == 0) {
            holder.tv_name.setText("全部分类");
            holder.iv_nul.setImageResource(R.mipmap.ic_servicex_cate);
        } else {

            //假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
            holder.tv_name.setText(lists.get(pos - 1).getName() + "");
//		holder.txt_shop_price.setText("¥"+lists.get(pos).getMin_price() + "");
            Glide
                    .with(context)
                    .load(ApiHttpClient.IMG_SERVICE_URL + lists.get(pos - 1).getImg())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_girdview)
                    .into(holder.iv_nul);
        }


        return convertView;
    }

    static class ViewHolder {
        private TextView tv_name;
        private ImageView iv_nul;
    }
}
