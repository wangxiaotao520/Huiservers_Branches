package com.huacheng.huiservers.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.Circle2DetailsActivity;
import com.huacheng.huiservers.geren.adapter.CircleItemImageAdapter;
import com.huacheng.huiservers.huodong.ImagePagerActivity;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.huiservers.view.MyGridview;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Created by Administrator on 2018/03/20.
 */

public class Circle5Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    ArrayList<String> imgList = null;

    private List<BannerBean> mCircles;

    private Context mContext;

    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;

    public Circle5Adapter(List<BannerBean> circles, Context context) {
        this.mCircles = circles;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 通过判断显示类型，来创建不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.circle5_list_item, parent, false);
            view.setOnClickListener(this);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            recyclerViewHolder.itemView.setTag(position);

            final int i = position;

            if (mCircles.get(position).getAdmin_id().equals("0")) { //0为用户发布的

                recyclerViewHolder.ly_onclick.setVisibility(View.GONE);
                recyclerViewHolder.linear.setVisibility(View.VISIBLE);
                recyclerViewHolder.iv_circle4_official.setVisibility(View.GONE);

                recyclerViewHolder.tv_circlename.setText(mCircles.get(position).getNickname());
                byte[] bytes1 = Base64.decode(mCircles.get(position).getContent(), Base64.DEFAULT);
                recyclerViewHolder.tv_circleContent.setText(new String(bytes1));
                with(mContext).load(StringUtils.getImgUrl(mCircles.get(position).getAvatars())).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(recyclerViewHolder.circularimg);

                if (mCircles.get(position).getImg_list() != null) {
                    recyclerViewHolder.lin_gridViewContinuous.setVisibility(View.VISIBLE);
                    /*for (int j = 0; j < mCircles.get(position).getImg_list().size(); j++) {
                        imgList.add(MyCookieStore.URL + mCircles.get(position).getImg_list().get(j).getImg());
                    }*/
                    CircleItemImageAdapter mGridViewAdapter = new CircleItemImageAdapter(mContext, mCircles.get(position).getImg_list());
                    recyclerViewHolder.gridView_continuous.setAdapter(mGridViewAdapter);
                    recyclerViewHolder.gridView_continuous.setOnTouchInvalidPositionListener(new MyGridview.OnTouchInvalidPositionListener() {
                        @Override
                        public boolean onTouchInvalidPosition(int motionEvent) {
                            Intent intent = new Intent();
                            intent.setClass(mContext, Circle2DetailsActivity.class);
                            intent.putExtra("id", mCircles.get(i).getId());
                            mContext.startActivity(intent);
                            return true;
                        }
                    });
                } else {
                    recyclerViewHolder.lin_gridViewContinuous.setVisibility(View.GONE);
                }
                recyclerViewHolder.tv_circleTime.setText(mCircles.get(position).getAddtime());
                recyclerViewHolder.tv_circleViews.setText(mCircles.get(position).getClick());
                recyclerViewHolder.
                        tv_circleReply.setText(mCircles.get(position).getReply_num());

                recyclerViewHolder.tv_circleTitle.setVisibility(View.GONE);

                recyclerViewHolder.tv_circlename.setTextColor(mContext.getResources().getColor(R.color.black_jain_87));
                if (!mCircles.get(position).getC_name().equals("")) {
                    recyclerViewHolder.tv_cname.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_cname.setText("#" + mCircles.get(position).getC_name());
                }

                if (!mCircles.get(position).getCommunity_name().equals("")) {
                    recyclerViewHolder.tv_cname1.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_cname1.setText("#" + mCircles.get(position).getCommunity_name());
                }

                SharedPreferences pref = mContext.getSharedPreferences("login", 0);
                String uid = pref.getString("login_uid", "");
                if (uid.equals(mCircles.get(position).getUid())) {
                    recyclerViewHolder.tv_circledel.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_circledel.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mOnItemDeleteListener.onDeleteClick(i);
                        }
                    });
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerViewHolder.tv_circleTime.getLayoutParams();
                    lp.setMargins(0, 0, 0, 0);
                    recyclerViewHolder.tv_circleTime.setLayoutParams(lp);
                } else {
                    recyclerViewHolder.tv_circledel.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerViewHolder.tv_circleTime.getLayoutParams();
                    lp.setMargins(0, 0, 50, 0);
                    recyclerViewHolder.tv_circleTime.setLayoutParams(lp);
                }

            } else {
                recyclerViewHolder.ly_onclick.setVisibility(View.VISIBLE);
                recyclerViewHolder.linear.setVisibility(View.GONE);
               /* recyclerViewHolder.tv_circlename.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                if (!mCircles.get(position).getC_name().equals("")) {
                    recyclerViewHolder.tv_cname.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_cname.setText("#" + mCircles.get(position).getC_name());
                }
                if (!mCircles.get(position).getCommunity_name().equals("")) {
                    recyclerViewHolder.tv_cname1.setVisibility(View.VISIBLE);
                    recyclerViewHolder.tv_cname1.setText("#" + mCircles.get(position).getCommunity_name());
                }*/
                recyclerViewHolder.lin_gridViewContinuous.setVisibility(View.GONE);


                byte[] bytes1 = Base64.decode(mCircles.get(position).getTitle(), Base64.DEFAULT);
                recyclerViewHolder.tv_title.setText(new String(bytes1));
                recyclerViewHolder.tv_name.setText(mCircles.get(position).getNickname() + "   发布于  " + mCircles.get(position).getC_name());
                recyclerViewHolder.tv_time.setText(mCircles.get(position).getAddtime());
                byte[] bytes2 = Base64.decode(mCircles.get(position).getContent(), Base64.DEFAULT);
                recyclerViewHolder.tv_content.setText(new String(bytes2));

                recyclerViewHolder.tv_circle_kan.setText(mCircles.get(position).getClick());
                recyclerViewHolder.tv_circle_huifu.setText(mCircles.get(position).getReply_num());
                List<BannerBean> list = mCircles.get(position).getImg_list();
                if (list != null) {
                    if (!TextUtils.isEmpty(list.get(0).getImg())) {
                        recyclerViewHolder.iv_img.setVisibility(View.VISIBLE);
                        with(mContext)
                                .load(MyCookieStore.URL + mCircles.get(position).getImg_list().get(0).getImg())
                                .skipMemoryCache(false)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.icon_girdview)
                                .into(recyclerViewHolder.iv_img);
                    } else {
                        recyclerViewHolder.iv_img.setVisibility(View.GONE);
                    }
                } else {
                    recyclerViewHolder.iv_img.setVisibility(View.GONE);
                }

                Glide
                        .with(mContext)
                        .load(MyCookieStore.URL + mCircles.get(position).getAvatars())
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.facehead1)
                        .placeholder(R.drawable.facehead1)
                        .into(recyclerViewHolder.iv_head);

                /*recyclerViewHolder.ly_onclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, Circle2DetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", mCircles.get(position).getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);

                    }
                });*/
               /* if (list != null) {
                    String img = list.get(0).getImg();
                    if (!StringUtils.isEmpty(img)) {
                        String imgUrl = MyCookieStore.URL + img;
                        Glide.with(mContext).load(imgUrl).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.icon_girdview)
                                .into(recyclerViewHolder.iv_circle4_official);

                        String imgsize = list.get(0).getImg_size();
                        if (!StringUtils.isEmpty(imgsize)) {
                            recyclerViewHolder.iv_circle4_official.getLayoutParams().width = ToolUtils.getScreenWidth(mContext);
                            Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext)) / (Double.valueOf(imgsize));
                            recyclerViewHolder.iv_circle4_official.getLayoutParams().height = (new Double(d)).intValue();
                        }
                        recyclerViewHolder.iv_circle4_official.setVisibility(View.VISIBLE);

                    } else {
                        recyclerViewHolder.iv_circle4_official.setVisibility(View.GONE);
                    }

                } else {
                    recyclerViewHolder.iv_circle4_official.setVisibility(View.GONE);
                }*/

            }


        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;

                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    }


    //删除接口
    public interface onItemDeleteListener {
        void onDeleteClick(int i);
    }

    private onItemDeleteListener mOnItemDeleteListener;

    public void setmOnItemDeleteListener(onItemDeleteListener mOnItemDeleteListener) {
        this.mOnItemDeleteListener = mOnItemDeleteListener;
    }

    //item事件
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mItemClickListener;

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mCircles.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linear;
        CircularImage circularimg, iv_head;
        TextView tv_circlename, tv_circleTime, tv_circleTitle, tv_circleContent, tv_circleViews, tv_circleReply,
                tv_cname, tv_cname1, tv_circledel, tv_circle_huifu, tv_circle_kan, tv_time, tv_name, tv_title, tv_content;

        ImageView
                iv_circle4_official, iv_img;
        MyGridview gridView_continuous;

        LinearLayout lin_gridViewContinuous, ly_onclick;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            circularimg = (CircularImage) itemView.findViewById(R.id.circularimg);
            tv_circleTitle = (TextView) itemView.findViewById(R.id.tv_circleTitle);
            tv_circleContent = (TextView) itemView.findViewById(R.id.tv_circleContent);
            tv_circlename = (TextView) itemView.findViewById(R.id.tv_circlename);
            tv_circleTime = (TextView) itemView.findViewById(R.id.tv_circleTime);
            iv_circle4_official = (ImageView) itemView.findViewById(R.id.iv_circle4_official);
            lin_gridViewContinuous = (LinearLayout) itemView.findViewById(R.id.lin_gridViewContinuous);
            tv_circleViews = (TextView) itemView.findViewById(R.id.tv_circleViews);
            gridView_continuous = (MyGridview) itemView.findViewById(R.id.gridView_continuous);
            tv_circleReply = (TextView) itemView.findViewById(R.id.tv_circleReply);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
            tv_cname = (TextView) itemView.findViewById(R.id.tv_cname);
            tv_cname1 = (TextView) itemView.findViewById(R.id.tv_cname1);
            tv_circledel = (TextView) itemView.findViewById(R.id.tv_circledel);

            //官方
            iv_head = (CircularImage) itemView.findViewById(R.id.iv_head);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_circle_kan = (TextView) itemView.findViewById(R.id.tv_circle_kan);
            tv_circle_huifu = (TextView) itemView.findViewById(R.id.tv_circle_huifu);
            ly_onclick = (LinearLayout) itemView.findViewById(R.id.ly_onclick);

        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(getItemCount()-1);

    }
}
