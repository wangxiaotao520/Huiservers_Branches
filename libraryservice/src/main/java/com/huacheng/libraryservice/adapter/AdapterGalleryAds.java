package com.huacheng.libraryservice.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.libraryservice.R;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.model.ModelAds;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.widget.FunctionAdvertise;

import java.util.ArrayList;
import java.util.List;

/**
 * 类说明：广告轮播adapter
 * @date 2014-12-11
 */
public class AdapterGalleryAds extends PagerAdapter {
    private Context mContext;
    private List<ModelAds> mAds;
    private ArrayList<View> mImageDatas;
    private FunctionAdvertise.OnAdvertiseClickListener listener;

    public void setOnAdvertiseClistener(FunctionAdvertise.OnAdvertiseClickListener listener) {
        this.listener = listener;
    }

    public AdapterGalleryAds(Context context) {
        this.mContext = context;
        this.mAds = new ArrayList<ModelAds>();
        mImageDatas = new ArrayList<View>();
    }

    public AdapterGalleryAds(Context context, List<ModelAds> list) {
        this(context);
        this.mAds.addAll(list);
    }

    public void removeDatas() {
        this.mAds.clear();
    }

    public int getCount() {
        return NullUtil.isListEmpty(mAds)?0:mAds.size()==1?1:Integer.MAX_VALUE;
//        return mAds==null?0:mAds.size();
    }

    public int getRealCount() {
        return mAds==null?0:mAds.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(getRealCount() == 0)
            return;
//        container.removeView((View)object);
    }

    public ModelAds getItem(int position) {
        return mAds==null||mAds.size()==0?null:(ModelAds) this.mAds.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if(getRealCount() == 0)
            return null;
        int virtualPosition = position % getRealCount();
        ImageView adsView = null;
        if(virtualPosition >= mImageDatas.size()) {
            adsView = new ImageView(container.getContext());
            adsView.setScaleType(ImageView.ScaleType.FIT_XY);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            adsView.setLayoutParams(params);
            mImageDatas.add(adsView);
        }else {
            adsView = (ImageView)mImageDatas.get(virtualPosition);
        }

        if(adsView.getParent() != null) {
            container.removeView(adsView);
        }

        final ModelAds ads = getItem(virtualPosition);
        if(ads!=null) {
            Glide.with(mContext.getApplicationContext()).load(ApiHttpClient.IMG_URL+ads.getImg())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(R.mipmap.default_image_small)
                    .error(R.mipmap.default_image_small)
                    .into(adsView);
            adsView.setTag(R.id.tag_object, ads);
        }
        //设置图片点击事件
        adsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(v);
                }
            }
        });

        container.addView(adsView);

        return adsView;
    }

    public void addAds(List<ModelAds> ads) {
        mAds.addAll(ads);
        this.notifyDataSetChanged();
    }
}