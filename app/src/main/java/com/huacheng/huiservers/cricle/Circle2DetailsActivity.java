package com.huacheng.huiservers.cricle;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.cricle.adapter.Circle2detailListAdapter;
import com.huacheng.huiservers.cricle.bean.CircleDetail4Bean;
import com.huacheng.huiservers.dialog.WaitDIalog;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.login.LoginVerifyCode1Activity;
import com.huacheng.huiservers.protocol.CircleProtocol;
import com.huacheng.huiservers.protocol.CommonProtocol;
import com.huacheng.huiservers.utils.LogUtils;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.CircularImage;
import com.huacheng.huiservers.view.MyListView;
import com.huacheng.libraryservice.dialog.CommomDialog;
import com.huacheng.libraryservice.http.ApiHttpClient;
import com.huacheng.libraryservice.utils.glide.GlideUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.huacheng.huiservers.R.id.et_input;
import static com.huacheng.huiservers.R.id.tv_send;


/**
 * 类：
 * 时间：2018/3/16 15:43
 * 功能描述:Huiservers
 */
public class Circle2DetailsActivity extends BaseUI {

    CircleProtocol mCircleProtocol = new CircleProtocol();
    CircleDetail4Bean mCirclebean = new CircleDetail4Bean();
    Circle2detailListAdapter cricle2detailListAdapter;
    BitmapUtils bitmapUtils;
    @BindView(R.id.lin_pinglun)
    LinearLayout mLinPinglun;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    @BindView(R.id.lin_top_all)
    LinearLayout mLinTopAll;
    @BindView(R.id.lin_comment)
    LinearLayout linComment;

    private SharedPreferences preferencesLogin;
    private String login_type;
    private Dialog WaitDialog;

    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.lin_nodata)
    LinearLayout mLinNodata;
    @BindView(R.id.lin_yescontent)
    LinearLayout mLinYescontent;

    String circle_id, circle_comment;
    @BindView(R.id.left)
    ImageView mLeft;
    @BindView(R.id.lin_left)
    LinearLayout mLinLeft;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.iv_photo)
    CircularImage mIvPhoto;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.lin_guan)
    LinearLayout mLinGuan;
    @BindView(R.id.tv_user_content)
    TextView mTvUserContent;

    @BindView(R.id.lin_user)
    LinearLayout mLinUser;
    @BindView(R.id.list_reply)
    MyListView mListReply;
    @BindView(et_input)
    EditText mEtInput;
    @BindView(tv_send)
    TextView mTvSend;
    @BindView(R.id.iv_top)
    ImageView mIvTop;
    @BindView(R.id.lin_img)
    LinearLayout mLinImg;
    @BindView(R.id.iv_photo_bootom)
    CircularImage mIvPhotoBootom;


    private String SCROLLtag = "0";
    private String commentSuccess = "0";

    private int isPro = 0;//是否是物业公告
    private Handler handler = new Handler();

    /**
     * 滑动到指定位置
     */
    private void scrollToPosition() {

        /*handler.post(new Runnable() {

            @Override
            public void run() {
                mScrollView.scrollTo(0, mLinPinglun.getTop() - mLinTopAll.getTop());
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //显示dialog
                mScrollView.scrollTo(0, mLinPinglun.getTop() - mLinTopAll.getTop());
            }
        }, 500);   //0.5秒
    }

    boolean isRefresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cricle2_detail);
        ButterKnife.bind(this);
        //       SetTransStatus.GetStatus(this);
        MyCookieStore.My_notify = 1;
        circle_id = this.getIntent().getExtras().getString("id");
        isPro = this.getIntent().getExtras().getInt("mPro");


        circle_comment = this.getIntent().getExtras().getString("circle_comment");
//        isRefresh = this.getIntent().getExtras().getBoolean("refresh");

        preferencesLogin = this.getSharedPreferences("login", 0);
        login_type = preferencesLogin.getString("login_type", "");
        bitmapUtils = new BitmapUtils(this);
        mTitleName.setText("邻里详情");
        showDialog(smallDialog);
        getdata();
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (StringUtils.isEmpty(mEtInput.getText().toString())) {
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_gray2_corners));
                    mTvSend.setTextColor(getResources().getColor(R.color.gray3));
                    mTvSend.setEnabled(false);
                } else {
                    mTvSend.setBackground(getResources().getDrawable(R.drawable.bg_primary2_corners));
                    mTvSend.setTextColor(getResources().getColor(R.color.white));
                    mTvSend.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //setEditTextInputSpace(mEtInput);

            }
        });

    }

    //获取邻里详情
    private void getdata() {
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", circle_id);
        params.addBodyParameter("is_pro", isPro + "");
        HttpHelper hh = new HttpHelper(info.get_social, params, Circle2DetailsActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                mCirclebean = mCircleProtocol.getSocialDetail(json);
                //顶部头像
//                Glide.with(context).load(StringUtils.getImgUrl(mCirclebean.getAvatars())).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(mIvPhoto);
                GlideUtils.getInstance().glideLoad(context,StringUtils.getImgUrl(mCirclebean.getAvatars()),mIvPhoto,R.drawable.facehead1);
                System.out.println("@@@@@@@@@@@@@@@@@^" + mCirclebean.getAvatars());
                //底部头像
               /* Glide.with(context).load(MyCookieStore.URL + mCirclebean.getAvatars()).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.facehead1).error(R.drawable.facehead1).into(mIvPhotoBootom);*/

                mTvName.setText(mCirclebean.getNickname());
                mTvTime.setText(mCirclebean.getAddtime());
                //判断内容是官方发布还是用户发布admin_id为0标识为用户发布
                if (mCirclebean.getAdmin_id().equals("0")) {//用户发布
                    mTvName.setTextColor(context.getResources().getColor(R.color.black_jain_87));
                    mLinGuan.setVisibility(View.GONE);
                    mLinUser.setVisibility(View.VISIBLE);
                    //System.out.println("NNNNNN+++++++++" + mCirclebean.getContent());
                    byte[] bytes = Base64.decode(mCirclebean.getContent(), Base64.DEFAULT);
                    mTvUserContent.setText(new String(bytes));
                    //System.out.println("^^^^^^^^^+++++++++" + new String(bytes));
                    // mTvUserContent.setText("     " + mCirclebean.getContent());
                    //动态添加用户发表图片
                    getAddview();


                } else {///官方图文混排

                    mTvName.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                    if (isPro == 1) {
                        mLinGuan.setVisibility(View.VISIBLE);
                        mLinUser.setVisibility(View.VISIBLE);
                        getAddview();
                    } else {
                        mLinGuan.setVisibility(View.VISIBLE);
                        mLinUser.setVisibility(View.GONE);
                    }

                    //能够的调用JavaScript代码
                    mWebview.getSettings().setJavaScriptEnabled(true);
                    // 设置允许JS弹窗
                    //        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    // 设置可以支持缩放
                    mWebview.getSettings().setSupportZoom(true);
                    mWebview.getSettings().setBuiltInZoomControls(true);
                    // 设置隐藏缩放工具控制条
                    mWebview.getSettings().setDisplayZoomControls(false);
                    //扩大比例的缩放
                    mWebview.getSettings().setUseWideViewPort(false);
                    //自适应屏幕
                    mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    mWebview.getSettings().setLoadWithOverviewMode(true);

                    //设置字体大小
                    mWebview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    byte[] bytes = Base64.decode(mCirclebean.getContent(), Base64.DEFAULT);
                    //加载HTML字符串进行显示
                    // mWebview.loadDataWithBaseURL(null, getNewContent(new String(bytes)), "text/html", "utf-8", null);
                    String content = new String(bytes);
                    if (!"".equals(content)) {
                        String css = "<style type=\"text/css\"> " +
                                "img {" +
                                "max-width: 100% !important;" +//限定图片宽度填充屏幕
                                "height:auto !important;" +//限定图片高度自动
                                "}" +
                                "</style>";
                        String content1 = "<head>" + css + "</head><body>" + content + "</body></html>";
                        mWebview.loadDataWithBaseURL(null, content1, "text/html", "utf-8", null);
                        LogUtils.d("[content1]" + content1);
                    }


                }
                if (mCirclebean.getReply_list() != null) {
                    mLinYescontent.setVisibility(View.VISIBLE);
                    mLinNodata.setVisibility(View.GONE);
                    cricle2detailListAdapter = new Circle2detailListAdapter(Circle2DetailsActivity.this, mCirclebean.getReply_list());
                    mListReply.setAdapter(cricle2detailListAdapter);

                    cricle2detailListAdapter.setmOnItemDeleteListener(new Circle2detailListAdapter.onItemDeleteListener() {
                        @Override
                        public void onDeleteClick(final int i) {

                            new CommomDialog(Circle2DetailsActivity.this, R.style.my_dialog_DimEnabled, "删除此信息吗？", new CommomDialog.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm) {
                                        if (mCirclebean.getReply_list() != null) {
                                            socialReplyDel(mCirclebean.getReply_list().get(i).getId(), mCirclebean.getReply_list().get(i).getSocial_id());
                                        }
                                        dialog.dismiss();
                                    }
                                }
                            }).show();//.setTitle("提示")

                        }
                    });
                } else {
                    mLinYescontent.setVisibility(View.GONE);
                    mLinNodata.setVisibility(View.VISIBLE);
                    //显示无评论图
                }

//                mCirclebean.getIs_observe()

                if ("1".equals(mCirclebean.getIs_observe())) {
                    linComment.setVisibility(View.VISIBLE);
                } else {
                    linComment.setVisibility(View.GONE);
                }

//                linComment.setVisibility(View.GONE);

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    /**
     * 删除邻里回复
     *
     * @param socialID
     */
    private void socialReplyDel(String id, String socialID) {
        showDialog(smallDialog);
        Url_info urlInfo = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("social_reply_id", id);
        params.addBodyParameter("social_id", socialID);
        params.addBodyParameter("is_pro", isPro + "");
        new HttpHelper(urlInfo.social_reply_del, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                String result = new CommonProtocol().getResult(json);
                if (result.equals("1")) {
                    mLinImg.removeAllViews();
                    SCROLLtag = "1";
                    getdata();

                    scrollToPosition();
                    mCirclebean.setType(1);
                    EventBus.getDefault().post(mCirclebean);
                } else {
                    XToast.makeText(Circle2DetailsActivity.this, "删除失败", XToast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    //评论
    private void pinglun(String social_id, String content) {
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("social_id", social_id);
        params.addBodyParameter("content", content);
        params.addBodyParameter("is_pro", isPro + "");

        HttpHelper hh = new HttpHelper(info.social_reply, params, this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    if (StringUtils.isEquals(status, "1")) {
                        WaitDIalog.closeDialog(WaitDialog);
//                        MyCookieStore.Circle_refresh = 1;
                        XToast.makeText(context, "评论成功", XToast.LENGTH_SHORT).show();
                        mLinImg.removeAllViews();
                        SCROLLtag = "1";
                        getdata();

                        scrollToPosition();
                        mCirclebean.setType(0);
                        EventBus.getDefault().post(mCirclebean);
                    } else {
                        WaitDIalog.closeDialog(WaitDialog);
                        XToast.makeText(Circle2DetailsActivity.this, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    protected void getAddview() {
        mLinImg.removeAllViews();
        if (mCirclebean.getImg_list() != null) {
            for (int i = 0; i < mCirclebean.getImg_list().size(); i++) {
                View view = LinearLayout.inflate(Circle2DetailsActivity.this, R.layout.cricle_detail_imgdesc_item, null);
                ImageView img = (ImageView) view.findViewById(R.id.img);

                //
                bitmapUtils.display(img, MyCookieStore.URL + mCirclebean.getImg_list().get(i).getImg());//"http://property.hui-shenghuo.cn/"
                mLinImg.addView(view);
            }
            if (SCROLLtag.equals("1")) {//值为1 的时候  代表评论成功执行这段话
                //scrollToPosition();

            }
//            mCirclebean.getImg_list().clear();
        }
    }


    @OnClick({R.id.lin_left, et_input, tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_left:
                UIUtils.closeInputMethod(this, mEtInput);

             /*   if (!StringUtils.isEmpty(circle_comment)) {
                    if (circle_comment.equals("1")) {
                        setResult(11);
                    }
                }
*/
               /* if (isRefresh) {
                    if (commentSuccess.equals("1")) {

                    }
                }*/
                finish();
                break;
            case et_input:
                break;
            case tv_send:
                preferencesLogin = getSharedPreferences("login", 0);
                login_type = preferencesLogin.getString("login_type", "");
                if (login_type.equals("")|| ApiHttpClient.TOKEN==null||ApiHttpClient.TOKEN_SECRET==null) {
                    startActivity(new Intent(Circle2DetailsActivity.this, LoginVerifyCode1Activity.class));
                } else {

                    if (TextUtils.isEmpty(mEtInput.getText().toString().trim())) {
                        XToast.makeText(Circle2DetailsActivity.this, "请输入您要说的话", Toast.LENGTH_SHORT).show();
                    } else {

                        String str_count = Base64.encodeToString(getStringNoBlank(mEtInput.getText().toString().trim()).getBytes(), Base64.DEFAULT);
                        pinglun(circle_id, str_count);
                        mEtInput.setText("");
                    }
                }
                break;
        }
    }

    public static String getStringNoBlank(String str) {//去除首尾空格 换行符
        if (str != null && !"".equals(str)) {
            str.replaceAll("\n", "");
            Pattern p = Pattern.compile("(^\\s*)|(\\s*$)");
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return "";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCirclebean != null) {
            if (!"0".equals(mCirclebean.getId())) {
                mCirclebean.setType(2);
                EventBus.getDefault().post(mCirclebean);
            }
        }
    }
}
