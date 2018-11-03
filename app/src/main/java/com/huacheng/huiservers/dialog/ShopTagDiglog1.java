package com.huacheng.huiservers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.dialog.tabGround.OneTagLabel1;
import com.huacheng.huiservers.dialog.tabGround.TagContainerLayout;
import com.huacheng.huiservers.dialog.tabGround.TagContainerLayout.ViewColor;
import com.huacheng.huiservers.dialog.tabGround.TagFactory;
import com.huacheng.huiservers.dialog.tabGround.TagView;
import com.huacheng.huiservers.geren.bean.GerenBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: mrj
 * github:https://github.com/jj3341332
 * blog:http://blog.csdn.net/jj3341332
 * Create Date: 2017/3/7 9:14
 */

public class ShopTagDiglog1 extends Dialog {

    private ViewColor mBanViewColor = new ViewColor();
    private ViewColor mDefaultViewColor = new ViewColor();
    private ViewColor mClickViewColor = new ViewColor();
    private List<GerenBean> mTagBean = null;

    private TagFactory tagFactory;
    private TagContainerLayout colorTagContainer;

    private int colorPosition = -1;

    //****************

    private Builder.IShopTagDialogEventListener mShopTagDialogEventListener;

    private Context mContext;

    //****************
    private ShopTagDiglog1(Context context) {
        super(context, R.style.ShopTabDialog);
    }

    protected ShopTagDiglog1(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShopTagDiglog1(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {
        initDialog();
        if (mTagBean == null) {
            throw new RuntimeException("NullPointer exception!");
        }

        if (mTagBean.size()>0) {
            initOneTag();
        }


    }

    private void initOneTag() {
        List<String> titles = new ArrayList<String>();
        for (GerenBean tagBean : mTagBean) {
            titles.add(tagBean.getC_name());
        }
        colorTagContainer.setTitles(titles);
        tagFactory = new OneTagLabel1( mTagBean, colorTagContainer.getAllChildViews(), mBanViewColor, mDefaultViewColor, mClickViewColor);
        colorTagContainer.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView view, int position, String text) {
                TagFactory.ClickStatus clickStatus = tagFactory.onColorTagClick(position);
                if (clickStatus == TagFactory.ClickStatus.CLICK) {
                    mShopTagDialogEventListener.shopTagDialogEvent(mTagBean.get(position).getC_name(), mTagBean.get(position).getId());
                    dismiss();
                } else if (clickStatus == TagFactory.ClickStatus.UNCLICK) {
//                    Toast.makeText(getContext(), "请选择 颜色分类", Toast.LENGTH_SHORT).show();
                }
//                EventBus.getDefault().post(chooseTextView.getText());
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });
    }


    private void initDialog() {
        setContentView(R.layout.shop_tab_dialog);
        //设置返回键可撤销
        setCancelable(true);
        //设置点击非Dialog区可撤销
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        colorTagContainer = (TagContainerLayout) findViewById(R.id.color_tag_container);

    }

    public static class Builder {


        private ShopTagDiglog1 shopTagDiglog;

        public Builder(Context context) {
            shopTagDiglog = new ShopTagDiglog1(context);
        }

        //******************************************************将值传出来
        // 利用interface来构造一个回调函数
        public interface IShopTagDialogEventListener {
            public void shopTagDialogEvent(String valueYouWantToSendBackToTheActivity, String typeId);
        }

        // 在构造函数中，设置进去回调函数
        public Builder(Context context,
                       IShopTagDialogEventListener onShopTagDialogEventListener) {
            shopTagDiglog = new ShopTagDiglog1(context);
            shopTagDiglog.mShopTagDialogEventListener = onShopTagDialogEventListener;

        }
        //******************************************************将值传出来

        public Builder setBanViewColor(ViewColor viewColor) {
            shopTagDiglog.mBanViewColor = viewColor;
            return this;
        }

        public Builder setDefaultViewColor(ViewColor viewColor) {
            shopTagDiglog.mDefaultViewColor = viewColor;
            return this;
        }

        public Builder setClickViewColor(ViewColor viewColor) {
            shopTagDiglog.mClickViewColor = viewColor;
            return this;
        }

        public Builder setTagBean(List<GerenBean> tagBean) {
            shopTagDiglog.mTagBean = tagBean;
            return this;
        }

        public Builder setColorTagContainer(TagContainerLayout colorTagContainer) {
            shopTagDiglog.colorTagContainer = colorTagContainer;
            return this;
        }

        public ShopTagDiglog1 create() {
            shopTagDiglog.init();
            return shopTagDiglog;
        }

    }


}
