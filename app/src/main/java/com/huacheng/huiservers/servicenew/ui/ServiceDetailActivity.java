package com.huacheng.huiservers.servicenew.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.xlhratingbar_lib.XLHRatingBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.servicenew.model.ModelServiceDetail;
import com.huacheng.huiservers.servicenew.ui.adapter.ServiceCateAdapter;
import com.huacheng.huiservers.servicenew.ui.shop.ServiceStoreActivity;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.ToolUtils;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.ScrollChangedScrollView;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.sharesdk.PopupWindowShare;
import com.huacheng.libraryservice.utils.AppConstant;
import com.huacheng.libraryservice.utils.DeviceUtils;
import com.huacheng.libraryservice.utils.NullUtil;
import com.huacheng.libraryservice.utils.TDevice;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.huacheng.libraryservice.utils.linkme.LinkedMeUtils;
import com.huacheng.libraryservice.widget.ScrollviewListView;
import com.microquation.linkedme.android.referral.LMError;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类描述：服务详情界面
 * 时间：2018/9/4 09:25
 * created by DFF
 */
public class ServiceDetailActivity extends BaseActivity {
    @BindView(R.id.ic_banner)
    SimpleDraweeView mIcBanner;
    @BindView(R.id.tv_service_name)
    TextView mTvServiceName;
    @BindView(R.id.list)
    ScrollviewListView mList;
    @BindView(R.id.sinple_icon)
    SimpleDraweeView mSinpleIcon;
    @BindView(R.id.flowlayout)
    TagFlowLayout mFlowlayout;
    @BindView(R.id.scrollView)
    ScrollChangedScrollView mScrollView;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.ly_share)
    LinearLayout mLyShare;
    @BindView(R.id.title_rel)
    LinearLayout mTitleRel;
    @BindView(R.id.ly_call)
    LinearLayout mLyCall;
    @BindView(R.id.ly_store)
    LinearLayout mLyStore;
    @BindView(R.id.tv_btn)
    TextView mTvBtn;
    @BindView(R.id.tv_pingjia_num)
    TextView mTvPingjiaNum;
    @BindView(R.id.ly_onclck_pingjia)
    LinearLayout mLyOnclckPingjia;
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
    @BindView(R.id.ly_store_addview)
    LinearLayout mLyStoreAddview;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.ly_pingjia)
    LinearLayout mLyPingjia;
    @BindView(R.id.ly_img)
    LinearLayout mLyImg;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.tv_store_coupon)
    TextView mTvStoreCoupon;
    @BindView(R.id.tv_store_num)
    TextView mTvStoreNum;

    View status_bar;
    int width;
    List<String> StoreCatedata = new ArrayList<>();
    ServiceCateAdapter cateAdapter;
    List<ModelServiceDetail.TagListBean> tagListBeans = new ArrayList<>();//服务分类标签
    String tag_id, tag_name, tag_price;//服务标签id
    String call, service_id = "";
    ModelServiceDetail mModelOrdetDetail;
    private String share_url;
    private String share_title;
    private String share_desc;
    private String share_icon;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTitleRel.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        mTitleName.setTextColor(Color.argb((int) 0, 0, 0, 0));
        status_bar = findViewById(R.id.status_bar);
        status_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice.getStatuBarHeight(this)));
        status_bar.setAlpha(0);

        cateAdapter = new ServiceCateAdapter(this, tagListBeans);
        mList.setAdapter(cateAdapter);

    }


    @Override
    protected void initData() {
        showDialog(smallDialog);
        requestData();
    }

    @Override
    protected void initListener() {
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag_id = tagListBeans.get(position).getId();
                tag_name = tagListBeans.get(position).getTagname();
                tag_price = tagListBeans.get(position).getPrice();
                mTvPrice.setText(tagListBeans.get(position).getPrice());
                cateAdapter.setSelectItem(position);
                cateAdapter.notifyDataSetChanged();
            }
        });
        mScrollView.setScrollViewListener(new ScrollChangedScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {   //设置标题的背景颜色
                    status_bar.setAlpha(0);
                    mTitleRel.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
                    mTitleName.setTextColor(Color.argb((int) 0, 0, 0, 0));
                } else if (y > 0 && y <= 180) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    status_bar.setAlpha(255);
                    float scale = (float) y / 180;
                    float alpha = (255 * scale);
                    mTitleRel.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    mTitleName.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                }
            }

            @Override
            public void onScrollStop(boolean isScrollStop) {

            }
        });
    }

    /**
     * 请求数据
     */
    private void requestData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", service_id);
        MyOkHttp.get().post(ApiHttpClient.GET_SSERVICE_DETAIL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    mModelOrdetDetail = (ModelServiceDetail) JsonUtil.getInstance().parseJsonFromResponse(response, ModelServiceDetail.class);
                    inflateContent(mModelOrdetDetail);
                } else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response, "请求失败");
                    XToast.makeText(ServiceDetailActivity.this, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(ServiceDetailActivity.this, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    private void inflateContent(ModelServiceDetail mModelOrdetDetail) {
        if (mModelOrdetDetail != null) {
            mTitleName.setText(mModelOrdetDetail.getTitle());
            mTvServiceName.setText(mModelOrdetDetail.getTitle());
            //GlideUtils.getInstance().glideLoad(this, ApiHttpClient.IMG_SERVICE_URL + , mIcBanner, R.drawable.ic_goods_bg);
            String imgSize = mModelOrdetDetail.getTitle_img_size();
            if (!NullUtil.isStringEmpty(imgSize)) {
                int screenWidth = ToolUtils.getScreenWidth(mContext);
                mIcBanner.getLayoutParams().width = screenWidth;//屏幕边距减去30dp像素
                Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext)) / (Double.valueOf(imgSize));
                mIcBanner.getLayoutParams().height = (new Double(d)).intValue();

            } else {
                mIcBanner.getLayoutParams().width = (ToolUtils.getScreenWidth(mContext));
                Double d = Double.valueOf(ToolUtils.getScreenWidth(mContext)) / 2.5;
                mIcBanner.getLayoutParams().height = (new Double(d)).intValue();
            }

            FrescoUtils.getInstance().setImageUri(mIcBanner, ApiHttpClient.IMG_SERVICE_URL + mModelOrdetDetail.getTitle_img());
            //获取服务标签
            getServiceCateTag(mModelOrdetDetail.getTag_list());
            //用户评价
            getPingjia(mModelOrdetDetail.getScore_info());
            //服务图片详情
            getImgView(mModelOrdetDetail.getImg_list());

            //店铺信息
            FrescoUtils.getInstance().setImageUri(mSinpleIcon, ApiHttpClient.IMG_SERVICE_URL + mModelOrdetDetail.getIns_info().getLogo());
            mTvStoreName.setText(mModelOrdetDetail.getIns_info().getName());
            mTvStoreCoupon.setText("共" + mModelOrdetDetail.getIns_info().getCoupon_num() + "个优惠券");
            mTvStoreNum.setText("共" + mModelOrdetDetail.getIns_info().getService_num() + "个服务项");
            call = mModelOrdetDetail.getIns_info().getTelphone();

            //店铺服务标签
            getStoreTag(mModelOrdetDetail.getIns_info().getCate_list());
            //店铺服务项
            getStoreServiceView(mModelOrdetDetail.getIns_info().getService_list());


        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_detail;
    }

    @Override
    protected void initIntentData() {
        service_id = getIntent().getStringExtra("service_id");

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isStatusBar = true;
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.lin_left, R.id.tv_btn, R.id.ly_onclck_pingjia, R.id.ly_call, R.id.ly_store, R.id.ly_share})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.tv_btn:

                SharedPreferences preferencesLogin = ServiceDetailActivity.this.getSharedPreferences("login", 0);
                String login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("1")&& ApiHttpClient.TOKEN!=null&&ApiHttpClient.TOKEN_SECRET!=null) {
                    if (tagListBeans != null && tagListBeans.size() > 0) {
                        intent = new Intent(this, ServiceConfirmOrderActivity.class);
                        intent.putExtra("service_id", mModelOrdetDetail.getId());
                        intent.putExtra("service_name", mModelOrdetDetail.getTitle());
                        intent.putExtra("tag_id", tag_id);
                        intent.putExtra("tag_name", tag_name);
                        intent.putExtra("tag_price", tag_price);
                        startActivity(intent);

                    } else {
                        XToast.makeText(mContext, "分类不能为空", XToast.LENGTH_SHORT).show();
                    }
                } else {
                    intent = new Intent(this, LoginVerifyCode1Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ly_onclck_pingjia:
                intent = new Intent(this, ServiceCommentActivity.class);
                intent.putExtra("service_id", mModelOrdetDetail.getId());
                startActivity(intent);
                break;
            case R.id.ly_call://
                if (!StringUtils.isEmpty(call)) {
                    new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"
                                        + call));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                dialog.dismiss();
                            }

                        }
                    }).show();//.setTitle("提示")
                }
                break;
            case R.id.ly_store://
                intent = new Intent(this, ServiceStoreActivity.class);
                intent.putExtra("store_id", mModelOrdetDetail.getI_id());
                startActivity(intent);
                break;
            case R.id.ly_share://
                if (mModelOrdetDetail == null) {
                    return;
                }
                share_title = mModelOrdetDetail.getTitle() + "";
                share_desc = "我在社区慧生活发现了一个优质服务,快过来看看吧";
                share_icon = ApiHttpClient.IMG_SERVICE_URL + mModelOrdetDetail.getTitle_img();
                // 分享路径
                share_url = ApiHttpClient.API_URL_SHARE+"home/service/service_details/id/" + mModelOrdetDetail.getId();
                HashMap<String, String> params = new HashMap<>();
                params.put("type", "service_detail");
                params.put("id", mModelOrdetDetail.getId());
                showDialog(smallDialog);
                LinkedMeUtils.getInstance().getLinkedUrl(this, share_url, share_title, params, new LinkedMeUtils.OnGetLinkedmeUrlListener() {
                    @Override
                    public void onGetUrl(String url, LMError error) {
                        hideDialog(smallDialog);
                        if (error == null) {
                            String share_url_new = share_url + "?linkedme=" + url;
                            showSharePop(share_title, share_desc, share_icon, share_url_new);
                        } else {
                          //  XToast.makeText(ServiceDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            String share_url_new = share_url + "?linkedme=" + "";
                            showSharePop(share_title, share_desc, share_icon, share_url_new);
                        }
                    }
                });

                break;
        }
    }

    /**
     * 显示分享弹窗
     *
     * @param share_title
     * @param share_desc
     * @param share_icon
     * @param share_url_new
     */
    private void showSharePop(String share_title, String share_desc, String share_icon, String share_url_new) {
        PopupWindowShare popup = new PopupWindowShare(this, share_title, share_desc, share_icon, share_url_new, AppConstant.SHARE_COMMON);
        popup.showBottom(mIvShare);
    }

    /**
     * 获取服务分类标签
     *
     * @param tag_list
     */
    private void getServiceCateTag(List<ModelServiceDetail.TagListBean> tag_list) {

        if (tag_list != null && tag_list.size() > 0) {
            tagListBeans.clear();
            tagListBeans.addAll(tag_list);
            cateAdapter.notifyDataSetChanged();
            //默认选中第一条
            cateAdapter.setSelectItem(0);
            mTvPrice.setText(tagListBeans.get(0).getPrice());
            tag_id = tagListBeans.get(0).getId();
            tag_name = tagListBeans.get(0).getTagname();
            tag_price = tagListBeans.get(0).getPrice();
            //获取list显示数量
            int totalHei;
            View listItem = cateAdapter.getView(0, null, mList);
            listItem.measure(0, 0);
            if (tagListBeans.size() > 5) {//大于5条滚动
                totalHei = (listItem.getMeasuredHeight() + mList.getDividerHeight()) * 5;
            } else {
                totalHei = (listItem.getMeasuredHeight() + mList.getDividerHeight()) * tagListBeans.size();
            }
            ViewGroup.LayoutParams params = mList.getLayoutParams();
            params.height = totalHei;
        }
    }

    /**
     * 获取评价
     *
     * @param scoreInfoBean
     */
    private void getPingjia(ModelServiceDetail.ScoreInfoBean scoreInfoBean) {
        if (scoreInfoBean != null) {
            mLyPingjia.setVisibility(View.VISIBLE);
            mTvPingjiaNum.setText("共" + scoreInfoBean.getScore_num() + "条评论");
            FrescoUtils.getInstance().setImageUri(mSdvHead, StringUtils.getImgUrl(scoreInfoBean.getAvatars()));
            mTvUserName.setText(scoreInfoBean.getNickname());
            mRatingBar.setCountSelected(Integer.valueOf(scoreInfoBean.getScore()));
            mTvTime.setText(StringUtils.getDateToString(scoreInfoBean.getEvaluatime(), "8"));
            mTvContent.setText(scoreInfoBean.getEvaluate());
            mTvReply.setVisibility(View.GONE);
        } else {
            mLyPingjia.setVisibility(View.GONE);
        }
    }

    /**
     * 获取详情图片
     *
     * @param imgList
     */
    private void getImgView(List<ModelServiceDetail.ImgListBean> imgList) {
        final int gridWidth = DeviceUtils.getWindowWidth(this) - DeviceUtils.dip2px(this, 30);
        int nHeight = (int) gridWidth;
        mLyImg.removeAllViews();
        if (imgList != null && imgList.size() > 0) {
            for (int i = 0; i < imgList.size(); i++) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, nHeight);
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.ic_goods_bg);
                mLyImg.addView(imageView);
            }
        }
        for (int i = 0; i < imgList.size(); i++) {
            String imageUrl = ApiHttpClient.IMG_SERVICE_URL + imgList.get(i).getImg();

            final ImageView imageView = (ImageView) mLyImg.getChildAt(i);
            Glide.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.ic_goods_bg).error(R.drawable.ic_goods_bg).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    int width = resource.getIntrinsicWidth();
                    int height = resource.getIntrinsicHeight();
                    int nWidth = gridWidth;
                    int nHeight = (int) (2 * nWidth);
                    float scale = (float) height / width;
                    if (scale < 2) {
                        nHeight = (int) (scale * nWidth);
                    }
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, nHeight);
                    layoutParams.setMargins(0, 0, 0, 15);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageDrawable(resource);
                }
            });
        }
    }

    /**
     * 获取店铺所有标签
     */
    private void getStoreTag(List<ModelServiceDetail.InsInfoBean.CateListBean> cateListBean) {
        StoreCatedata.clear();
        if (cateListBean != null && cateListBean.size() > 0) {
            final LayoutInflater mInflater = LayoutInflater.from(mContext);
            for (int i = 0; i < cateListBean.size(); i++) {
                StoreCatedata.add(cateListBean.get(i).getCategory_cn());
            }
            TagAdapter adapter = new TagAdapter<String>(StoreCatedata) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.service_store_tag_item,
                            mFlowlayout, false);
                    tv.setText(StoreCatedata.get(position));
                    return tv;
                }
            };
            //adapter.setSelectedList(list_selected_position);
            mFlowlayout.setAdapter(adapter);
        }
    }

    /**
     * 该店铺其他服务项
     *
     * @param serviceListBeans
     */
    private void getStoreServiceView(final List<ModelServiceDetail.InsInfoBean.ServiceListBean> serviceListBeans) {
        mLyStoreAddview.removeAllViews();
        if (serviceListBeans != null && serviceListBeans.size() > 0) {
            mLyStoreAddview.setVisibility(View.VISIBLE);

            for (int i = 0; i < serviceListBeans.size(); i++) {
                if (i > 4) {
                    break;
                }
                View view = View.inflate(this, R.layout.service_detail_view, null);
                SimpleDraweeView iv_cardView_popular = view.findViewById(R.id.iv_cardView_popular);
                TextView tv_titleName = view.findViewById(R.id.tv_titleName);
                TextView tv_titleprice = view.findViewById(R.id.tv_titleprice);

                tv_titleName.setText(serviceListBeans.get(i).getTitle());
                tv_titleprice.setText("¥" + serviceListBeans.get(i).getPrice());
                //GlideUtils.getInstance().glideLoad(this, ApiHttpClient.IMG_SERVICE_URL + serviceListBeans.get(i).getTitle_img(), iv_cardView_popular, R.drawable.icon_girdview);
                FrescoUtils.getInstance().setImageUri(iv_cardView_popular, ApiHttpClient.IMG_SERVICE_URL + serviceListBeans.get(i).getTitle_img());
                //获取布局参数
              /*  ViewGroup.LayoutParams lp = iv_cardView_popular.getLayoutParams();
                lp.width = width / 3 * 2;
                iv_cardView_popular.setLayoutParams(lp);*/

                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ServiceDetailActivity.class);
                        intent.putExtra("service_id", serviceListBeans.get(finalI).getId());
                        startActivity(intent);

                    }
                });
                mLyStoreAddview.addView(view);
            }
        } else {
            mLyStoreAddview.setVisibility(View.GONE);
        }
    }

}
