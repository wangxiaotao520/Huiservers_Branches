package com.huacheng.huiservers.dialog.tabGround;

import android.view.SoundEffectConstants;

import com.huacheng.huiservers.geren.bean.GerenBean;

import java.util.List;

import static com.huacheng.huiservers.dialog.tabGround.TagContainerLayout.ViewColor;

/**
 * Author: mrj
 * github:https://github.com/jj3341332
 * blog:http://blog.csdn.net/jj3341332
 * Create Date: 2017/3/8 9:36
 */
public class OneTagLabel1 extends   TagFactory<TagView>  {
    private final List<GerenBean> mTagBean;
    private final List<TagView> mChildViews;
    private final ViewColor mBanViewColor;
    private final ViewColor mDefaultViewColor;
    private final ViewColor mClickViewColor;

    public OneTagLabel1(List<GerenBean> tagBean, List<TagView> allChildViews, ViewColor banViewColor,ViewColor defaultViewColor,ViewColor clickViewColor){

        this.mChildViews=allChildViews;
        this.mBanViewColor=banViewColor;
        this.mDefaultViewColor=defaultViewColor;
        this.mClickViewColor=clickViewColor;
        this.mTagBean =tagBean;
        initTags();
    }

    private void initTags() {
        for (int i=0;i<mChildViews.size();i++){
         /*   if (mTagBean.get(i).getAmount()==0){
                mChildViews.get(i).setTagViewColor(mBanViewColor);
                mChildViews.get(i).setEnabled(false);
            }else{

            }*/
            mChildViews.get(i).setTagViewColor(mDefaultViewColor);
            mChildViews.get(i).setText(mTagBean.get(i).getC_name());
            mChildViews.get(i).postInvalidate();
        }

    }

    @Override
    public  ClickStatus onColorTagClick(int position) {
        TagView view= mChildViews.get(position);
        if (view.getEnabled()==true){
            view.playSoundEffect(SoundEffectConstants.CLICK);
            if (!view.getIsClick()){
                for (TagView tagView : mChildViews) {
                    if (tagView.getEnabled()==true) {
                        tagView.setTagViewColor(mDefaultViewColor);
                        tagView.setIsClick(false);

                    }
                }
                view.setTagViewColor(mClickViewColor);
                view.setIsClick(true);

                return ClickStatus.CLICK;
            }else{
                view.setTagViewColor(mDefaultViewColor);
                view.setIsClick(false);
                return ClickStatus.UNCLICK;
            }
        }
        return ClickStatus.BAN;
    }

    @Override
    public ClickStatus onSizeTagClick(int position) {
       return null;
    }

    @Override
    public  TagView getClickObject() {
        return  null;
    }
}
