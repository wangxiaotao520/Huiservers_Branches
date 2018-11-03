package com.huacheng.huiservers.fragment.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.Jump;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.fragment.adapter.HomeCardAdapter;
import com.huacheng.huiservers.shop.bean.BannerBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;


public class ShopCardFragment extends Fragment {

    private CardView mCardView;
    List<BannerBean> mAct;
    private int mPosition;
    Jump jump;

    public ShopCardFragment() {
    }

    public ShopCardFragment(List<BannerBean> acts, int i) {
        this.mAct = acts;
        this.mPosition = i;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_card_img_item, container, false);
        mCardView = (CardView) view.findViewById(R.id.cardView);

        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * HomeCardAdapter.MAX_ELEVATION_FACTOR);


        ImageView iv_card = (ImageView) view.findViewById(R.id.iv_card);
        //获取图片的宽高--start
      /*  iv_card.getLayoutParams().width = ToolUtils.getScreenWidth(getActivity()) - 50;
        Double d = Double.valueOf(ToolUtils.getScreenWidth(getActivity()) - 50) /
                (Double.valueOf(mAct.get(mPosition).getImg_size()));
        iv_card.getLayoutParams().height = (new Double(d)).intValue();*/
        //获取图片的宽高--ends

        String picture = mAct.get(mPosition).getImg();
        if (!StringUtils.isEmpty(picture)) {
            Glide.with(this)
                    .load(picture)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_card);
          /*  TextView tv_commodity = (TextView) view.findViewById(R.id.tv_viewpager_name);
            tv_commodity.setText(mAct.get(mPosition).getTitle());*/
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mAct.get(mPosition).getUrl())) {
                    if (mAct.get(mPosition).getUrl_type().equals("0") || TextUtils.isEmpty(mAct.get(mPosition).getUrl_type())) {
                        jump = new Jump(getActivity(), mAct.get(mPosition).getType_name(), mAct.get(mPosition).getAdv_inside_url());
                    } else {
                        jump = new Jump(getActivity(), mAct.get(mPosition).getUrl_type(), mAct.get(mPosition).getType_name(), "", mAct.get(mPosition).getUrl_type_cn());
                    }
                } else {//URL不为空时外链
                    jump = new Jump(getActivity(), mAct.get(mPosition).getUrl());

                }
               /* Intent intent = new Intent(getActivity(), EducationActivity2.class);
                intent.putExtra("id", mAct.get(mPosition).getId());
                intent.putExtra("cid", mAct.get(mPosition).getC_id());
                intent.putExtra("name", "活动");
                startActivity(intent);*/

            }
        });
        return view;
    }

    public CardView getCardView() {
        return mCardView;
    }
}
