package com.huacheng.huiservers.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;


/**
 * Created by hwh on 2018/3/16.
 */

public class ChooseFabuDialog extends Dialog implements View.OnClickListener {

    private SelectDialogListener mListener;
    private int i;
    Activity activity;
    List<BannerBean> bean;
    ShopProtocol protocol = new ShopProtocol();
    private TagFlowLayout mFlowLayout;
    private Dialog WaitDialog;

    public interface SelectDialogListener {
        public void onItemClick(View view, int position, String s, String id);
    }

    /**
     * 取消事件监听接口
     */
    private SelectDialogCancelListener mCancelListener;

    public interface SelectDialogCancelListener {
        public void onCancelClick(View v);
    }

    public ChooseFabuDialog(Activity activity, int i, SelectDialogListener listener) {
        super(activity, R.style.Dialog_down);
        this.activity = activity;
        this.i = i;
        mListener = listener;
        setCanceledOnTouchOutside(true);
    }

    LinearLayout lin_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.dialog_choose_fabu,
                null);
        setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        lin_container = (LinearLayout) view.findViewById(R.id.lin_container);
        Window window = getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        onWindowAttributesChanged(wl);

        getdata();

    }

    private void getdata() {//列表
        //RequestParams params = new RequestParams();
        String url;
        Url_info info = new Url_info(activity);
        url = info.getSocialCategorys+"/sign/1";

        HttpHelper hh = new HttpHelper(url, activity) {

            @Override
            protected void setData(String json) {
                WaitDialog = WaitDIalog.createLoadingDialog(activity, "正在加载...");
                bean = protocol.getSocialCategorys(json);
                if (bean.size() != 0) {
                    String[] mVals = new String[bean.size()];
                    for (int i = 0; i < bean.size(); i++) {
                        mVals[i] = bean.get(i).getC_name();
                }
                    getTagFlowLayout(mVals);
                    lin_container.setVisibility(View.VISIBLE);
                    WaitDIalog.closeDialog(WaitDialog);
                } else {
                    lin_container.setVisibility(View.GONE);
                    WaitDIalog.closeDialog(WaitDialog);
                }

            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private void getTagFlowLayout(final String[] mVals) {
        final LayoutInflater mInflater = LayoutInflater.from(activity);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        TagAdapter adapter = new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };

        adapter.setSelectedList(i);


        mFlowLayout.setAdapter(adapter);

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mListener.onItemClick(view, position, mVals[position], bean.get(position).getId());
                dismiss();
                //view.setVisibility(View.GONE);
                return true;
            }
        });


        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
