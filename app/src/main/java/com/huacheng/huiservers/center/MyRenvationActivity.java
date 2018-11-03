package com.huacheng.huiservers.center;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.ZhuangXiuItemActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.lidroid.xutils.http.RequestParams;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.huacheng.huiservers.R.id.webview;

/**
 * Created by hwh on 2018/3/22.
 */

public class MyRenvationActivity extends BaseUI implements View.OnClickListener {

    private LinearLayout lin_left;
    private RelativeLayout ry, rel_ti;
    private TextView title_name;
    private ListView list;
    List<BannerBean> bean = new ArrayList<BannerBean>();
    ShopProtocol protocol = new ShopProtocol();
    private ListAdapter adapter;
    SharePrefrenceUtil prefrenceUtil;
    private Jump jump;
    List<BannerBean> beans = new ArrayList<BannerBean>();//广告数据

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_my_renvation);
  //      SetTransStatus.GetStatus(this);
        prefrenceUtil = new SharePrefrenceUtil(this);
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        ry = (RelativeLayout) findViewById(R.id.ry);
        rel_ti = (RelativeLayout) findViewById(R.id.rel_ti);
        title_name = (TextView) findViewById(R.id.title_name);
        list = (ListView) findViewById(R.id.list);

        title_name.setText("我的装修");
        getdata();

        lin_left.setOnClickListener(this);

    }

    private void getdata() {//列表
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        HttpHelper hh = new HttpHelper(info.my_renvation, this) {
            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                bean = protocol.getMyRenvation(json);
                if (bean.size() > 0) {
                    ry.setVisibility(View.GONE);
                    list.setVisibility(View.VISIBLE);
                    adapter = new ListAdapter(MyRenvationActivity.this);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    ry.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    getbanner();

                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };

    }

    private void getbanner() {//获取商城首页顶部广告信息
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        params.addBodyParameter("community_id", prefrenceUtil.getXiaoQuId());
        params.addBodyParameter("c_name", "hc_my_decoration");
        HttpHelper hh = new HttpHelper(info.get_Advertising, params, this) {

            @Override
            protected void setData(String json) {
                beans = protocol.bannerInfo(json);
                if (beans != null) {
                    rel_ti.setOnClickListener(MyRenvationActivity.this);
                    rel_ti.setVisibility(View.VISIBLE);
                } else {
                    rel_ti.setVisibility(View.GONE);
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_left:
                finish();
                break;
            case R.id.rel_ti://
                if (TextUtils.isEmpty(beans.get(0).getUrl())) {
                    if (beans.get(0).getUrl_type().equals("0") || TextUtils.isEmpty(beans.get(0).getUrl_type())) {
                        jump = new Jump(this, beans.get(0).getType_name(), beans.get(0).getAdv_inside_url());
                    } else {
                        jump = new Jump(this, beans.get(0).getUrl_type(), beans.get(0).getType_name(), "", beans.get(0).getUrl_type_cn());
                    }
                } else {//URL不为空时外链
                    jump = new Jump(this, beans.get(0).getUrl());

                }
                break;
            default:
                break;
        }
    }


    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        StringUtils stringUtils;
        ViewHolder holder = null;
        Bitmap bitmap;

        public ListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            if (bean == null) {
                return 0;
            }

            return bean.size();
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
            if (convertView == null) {

                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_renvation, null);
                holder = new ViewHolder();
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mWebview = (WebView) convertView.findViewById(webview);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_share = (ImageView) convertView.findViewById(R.id.iv_share);
            holder.linear = (LinearLayout) convertView.findViewById(R.id.linear);
            holder.tv_time.setText(stringUtils.getDateToString(bean.get(position).getUptime(), "2"));

            //能够的调用JavaScript代码
            holder.mWebview.getSettings().setJavaScriptEnabled(true);
            // 设置允许JS弹窗
            //        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            // 设置可以支持缩放
            holder.mWebview.getSettings().setSupportZoom(true);
            holder.mWebview.getSettings().setBuiltInZoomControls(true);
            // 设置隐藏缩放工具控制条
            holder.mWebview.getSettings().setDisplayZoomControls(false);
            //扩大比例的缩放
            holder.mWebview.getSettings().setUseWideViewPort(false);
            //自适应屏幕
            holder.mWebview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            holder.mWebview.getSettings().setLoadWithOverviewMode(true);

            //设置字体大小
            holder.mWebview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

//            byte[] bytes = Base64.decode(bean.get(position).getContent(), Base64.DEFAULT);
            //加载HTML字符串进行显示
            //holder.mWebview.loadDataWithBaseURL(null, getNewContent(bean.get(position).getContent()), "text/html", "utf-8", null);
            String content = bean.get(position).getContent();
            if (!"".equals(content)) {
                String css = "<style type=\"text/css\"> " +
                        "img {" +
                        "max-width: 100% !important;" +//限定图片宽度填充屏幕
                        "height:auto !important;" +//限定图片高度自动
                        "}" +
                        "</style>";
                String content1 = "<head>" + css + "</head><body>" + content + "</body></html>";
                holder.mWebview.loadDataWithBaseURL(null, content1, "text/html", "utf-8", null);
            }
            holder.iv_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*  holder.linear.setDrawingCacheEnabled(true);
                    holder.linear.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.linear.layout(0, 0, holder.linear.getMeasuredWidth(), holder.linear.getMeasuredHeight());
                    bitmap = Bitmap.createBitmap(holder.linear.getDrawingCache());
                    holder.linear.setDrawingCacheEnabled(false);*/

                    Intent intent = new Intent();
                    intent.setClass(MyRenvationActivity.this, ZhuangXiuItemActivity.class);
                    intent.putExtra("webCon", bean.get(position).getContent());
                    startActivity(intent);

                }
            });

            return convertView;
        }

        //webview   图片适配
        private String getNewContent(String htmltext) {

            Document doc = Jsoup.parse(htmltext);
            Elements elements = doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width", "100%").attr("height", "auto");
            }

            return doc.toString();
        }

        public class ViewHolder {
            private TextView tv_time;
            private ImageView iv_share;
            private WebView mWebview;
            private LinearLayout linear;


        }
    }
}
