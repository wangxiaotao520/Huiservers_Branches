package com.huacheng.huiservers.servicenew.ui.shop;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.servicenew.inter.OnRefreshAndLoadMoreListener;
import com.huacheng.huiservers.servicenew.model.CategoryBean;
import com.huacheng.huiservers.servicenew.model.ModelStore;
import com.huacheng.huiservers.servicenew.ui.MerchantServiceListActivity;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.sharesdk.PopupWindowShare;
import com.huacheng.libraryservice.utils.AppConstant;
import com.huacheng.libraryservice.utils.DeviceUtils;
import com.huacheng.libraryservice.utils.TDevice;
import com.huacheng.libraryservice.utils.fresco.FrescoUtils;
import com.huacheng.libraryservice.utils.json.JsonUtil;
import com.huacheng.libraryservice.utils.linkme.LinkedMeUtils;
import com.lzy.widget.HeaderViewPager;
import com.microquation.linkedme.android.referral.LMError;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Description: 服务店铺Activity
 * created by wangxiaotao
 * 2018/8/30 0030 下午 4:24
 */
public class ServiceStoreActivity extends BaseActivity implements View.OnClickListener {

    private View status_bar;
    private RelativeLayout rl_title;
    private LinearLayout lin_left;
    private TextView title_name;
    private TextView tv_store_phone;
    private TextView tv_service_num;
    private ImageView iv_right;
    public SmartRefreshLayout refreshLayout;
    private HeaderViewPager scrollableLayout;
    private SimpleDraweeView iv_head;
    private RelativeLayout rl_head;

    protected SystemBarTintManager tintManager;
    private View titleBg;
    private StoreServiceFragment storeServiceFragment;
    private StoreCommentFragment storeCommentFragment;
    private TextView tv_service_tab;
    private TextView tv_comment_tab;
    private SimpleDraweeView sdv_head;
    private RelativeLayout rl_more_service;
    private RelativeLayout rl_communication;
    private String store_id="";
    private ModelStore modelStore;
    private String share_url;
    private String share_title;
    private String share_desc;
    private String share_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(android.R.color.transparent);  //设置上方状态栏透明
        super.onCreate(savedInstanceState);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    protected void initView() {
        rl_head = findViewById(R.id.rl_head);
        rl_title = findViewById(R.id.rl_title);
        status_bar=findViewById(R.id.status_bar);
        status_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TDevice.getStatuBarHeight(this)));
        status_bar.setAlpha(0);
        lin_left = findViewById(R.id.lin_left);
        title_name = findViewById(R.id.title_name);
        tv_store_phone = findViewById(R.id.tv_store_phone);
        tv_service_num = findViewById(R.id.tv_service_num);
        iv_right = findViewById(R.id.iv_right);
        iv_right.setBackgroundResource(R.mipmap.ic_share);
        iv_right.setVisibility(View.VISIBLE);
        titleBg = findViewById(R.id.v_bg);
        titleBg.setAlpha(0);
        refreshLayout = findViewById(R.id.refreshLayout);
        // 一开始先不要让加载更多,防止网络错误时，加载更多报错
        refreshLayout.setEnableLoadMore(false);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        //设置偏移量，只能在这里设置
        scrollableLayout.setTopOffset(DeviceUtils.dip2px(this,48)+ TDevice.getStatuBarHeight(this));
        iv_head = findViewById(R.id.iv_head);

        tv_service_tab = findViewById(R.id.tv_service_tab);
        tv_comment_tab = findViewById(R.id.tv_comment_tab);
        sdv_head = findViewById(R.id.sdv_head);
        rl_more_service = findViewById(R.id.rl_more_service);
        rl_communication = findViewById(R.id.rl_communication);
    }

    @Override
    protected void initData() {
        // 请求数据
        showDialog(smallDialog);
        HashMap<String, String> params = new HashMap<>();
        params.put("id",store_id);
        MyOkHttp.get().post(ApiHttpClient.GET_SHOP_DETAIL, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)){
                    ModelStore modelStore = (ModelStore) JsonUtil.getInstance().parseJsonFromResponse(response, "data", ModelStore.class);
                    inflateContent(modelStore);
                }else {
                    String msg = JsonUtil.getInstance().getMsgFromResponse(response,"获取数据失败");
                    XToast.makeText(mContext, msg, XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(mContext, "网络异常，请检查网络设置", XToast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 填充数据
     * @param modelStore
     */
    private void inflateContent(ModelStore modelStore) {
       if (modelStore!=null){
           this.modelStore=modelStore;
           title_name.setText(modelStore.getName()+"");
           tv_store_phone.setText(modelStore.getTelphone()+"");
           tv_service_num.setText("共"+modelStore.getServiceCount()+"个服务 >");
           FrescoUtils.getInstance().setImageUri(iv_head,ApiHttpClient.IMG_SERVICE_URL+modelStore.getIndex_img());//头图
           FrescoUtils.getInstance().setImageUri(sdv_head,ApiHttpClient.IMG_SERVICE_URL+modelStore.getLogo());//logo
           List<CategoryBean> category = modelStore.getCategory();
           //
          ((StoreServiceFragment)currentFragment).setCategoryBeanList(category);
          ((StoreServiceFragment)currentFragment).setmDatas(modelStore.getService());
          tv_comment_tab.setText("评论 "+modelStore.getCommentsCount());
           // 我将这三个方法放在这里，在Fragment初始化的，结果不能滑动了，分析是必须在headerviewpager必须在oncreate中调用一次
//           switchFragmentNoBack(storeServiceFragment);
//           //此方法
//           scrollableLayout.setCurrentScrollableContainer(storeServiceFragment);
//           setButtomUI(tv_service_tab);
       }
    }

    @Override
    protected void initListener() {
        lin_left.setOnClickListener(this);
        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                rl_head.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                refreshLayout.setEnableRefresh(alpha>0?false:true);
                //
//                if (currentFragment!=null&&((OnRefreshAndLoadMoreListener)currentFragment).canLoadMore()){
//                    refreshLayout.setEnableLoadMore(alpha>=1?true:false);
//                }else {
//                    refreshLayout.setEnableLoadMore(false);
//                }
                titleBg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                status_bar.setAlpha(alpha);
            }
        });
        tv_service_tab.setOnClickListener(this);
        tv_comment_tab.setOnClickListener(this);
        rl_more_service.setOnClickListener(this);
        rl_communication.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (currentFragment!=null){
                    ((OnRefreshAndLoadMoreListener)currentFragment).onRefresh(refreshLayout);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (currentFragment!=null){
                    ((OnRefreshAndLoadMoreListener)currentFragment).onLoadMore(refreshLayout);
                }
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
        //不知道为啥写在这里不起作用，但是demo就是这么写的起作用，真乃格兰了
       scrollableLayout.setTopOffset(rl_title.getHeight()+status_bar.getHeight());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_store;
    }

    @Override
    protected void initIntentData() {
        store_id=getIntent().getStringExtra("store_id");
        if (store_id==null){
            store_id="";
        }
    }

    @Override
    protected int getFragmentCotainerId() {
        return R.id.fl_bottom_container;
    }

    @Override
    protected void initFragment() {
        storeServiceFragment = new StoreServiceFragment();
        storeCommentFragment = new StoreCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_id",store_id);
        storeServiceFragment.setArguments(bundle);
        Bundle bundle2 = new Bundle();
        bundle2.putString("store_id",store_id);
        storeCommentFragment.setArguments(bundle2);
        currentFragment=storeServiceFragment;
        switchFragmentNoBack(storeServiceFragment);
        //此方法
        scrollableLayout.setCurrentScrollableContainer(storeServiceFragment);
        setButtomUI(tv_service_tab);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lin_left) {
            finish();
        }  else if (v.getId() == R.id.tv_service_tab) {
            setButtomUI(tv_service_tab);
            switchFragmentNoBack(storeServiceFragment);
            scrollableLayout.setCurrentScrollableContainer(storeServiceFragment);
            // 进行Fragment判断 ，是否可加载 ，就一页或者最后一页就不能加载了
//            if (((OnRefreshAndLoadMoreListener)currentFragment).canLoadMore()){
//                refreshLayout.setEnableLoadMore(true);
//            }else {
//                refreshLayout.setEnableLoadMore(false);
//            }
        } else if (v.getId() == R.id.tv_comment_tab) {
            setButtomUI(tv_comment_tab);
            switchFragmentNoBack(storeCommentFragment);
            scrollableLayout.setCurrentScrollableContainer(storeCommentFragment);
            // 进行Fragment判断 ，是否可加载 ，就一页或者最后一页就不能加载了
//            if (((OnRefreshAndLoadMoreListener)currentFragment).canLoadMore()){
//                refreshLayout.setEnableLoadMore(true);
//            }else {
//                refreshLayout.setEnableLoadMore(false);
//            }
        }else if (v.getId() == R.id.rl_communication){
            if (modelStore==null){
                return;
            }
            // 联系商家
            new CommomDialog(mContext, R.style.my_dialog_DimEnabled, "确认拨打电话？", new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"
                                + modelStore.getTelphone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                }
            }).show();
        }else if (v.getId() == R.id.rl_more_service){
            // 更多服务
            Intent intent = new Intent(this, MerchantServiceListActivity.class);
            intent.putExtra("tabType","service");
            intent.putExtra("store_id",store_id+"");
            startActivity(intent);
        }else if (v.getId() == R.id.iv_right){
            if (modelStore == null) {
                return;
            }
            share_title = modelStore.getName() + "";
            share_desc =  "我在社区慧生活发现了一个好店铺,快过来看看吧";
            share_icon = ApiHttpClient.IMG_SERVICE_URL + modelStore.getLogo();
            // 分享路径
            share_url = ApiHttpClient.API_URL_SHARE+"home/service/ins_details/id/" + modelStore.getId();
            HashMap<String, String> params = new HashMap<>();
            params.put("type", "service_shop");
            params.put("id", modelStore.getId());
            showDialog(smallDialog);
            LinkedMeUtils.getInstance().getLinkedUrl(this, share_url, share_title, params, new LinkedMeUtils.OnGetLinkedmeUrlListener() {
                @Override
                public void onGetUrl(String url, LMError error) {
                    hideDialog(smallDialog);
                    if (error == null) {
                        String share_url_new = share_url + "?linkedme=" + url;
                        showSharePop(share_title, share_desc, share_icon, share_url_new);
                    } else {
                     //   XToast.makeText(ServiceStoreActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        String share_url_new = share_url + "?linkedme=" + "";
                        showSharePop(share_title, share_desc, share_icon, share_url_new);
                    }
                }
            });
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
        popup.showBottom(iv_right);
    }
    public void setButtomUI(TextView selected) {
        TextView[] txt_buttoms = {tv_service_tab, tv_comment_tab};

        for (int i = 0; i < txt_buttoms.length; i++) {// 否则遍历底部按钮，把被选中的id对应的按钮修改掉，再把其他的修改成非选择状态
            if (txt_buttoms[i].getId() != selected.getId()) {

                txt_buttoms[i].setTextColor(this.getResources().getColor(R.color.grey96));
            } else {
                txt_buttoms[i].setTextColor(this.getResources().getColor(R.color.orange));
            }
            continue;
        }
    }
}
