package com.huacheng.huiservers.house.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crazysunj.cardslideview.CardHandler;
import com.crazysunj.cardslideview.CardViewPager;
import com.crazysunj.cardslideview.ElasticCardView;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.house.HouseBean;
import com.huacheng.huiservers.house.HouseFamilyBillActivity;
import com.huacheng.huiservers.house.HouseInviteActivity;
import com.huacheng.huiservers.house.HousePersonInfoActivity;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.openDoor.OpenLanActivity;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import static com.huacheng.huiservers.R.id.cardview;
import static com.huacheng.huiservers.utils.UIUtils.startActivity;

/**
 * description
 * <p>
 * Created by sunjian on 2017/6/24.
 */

public class MySelectCardHandler implements CardHandler<HouseBean> {
    String wuye_type;

    public MySelectCardHandler(String wuye_type) {
        this.wuye_type = wuye_type;

    }

    @Override
    public View onBind(final Context context, final HouseBean data, final int position, @CardViewPager.TransformerMode int mode) {
        View view = View.inflate(context, R.layout.house_select_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView txt_xiaoqu_name = (TextView) view.findViewById(R.id.txt_xiaoqu_name);
        LinearLayout lin_onclick = (LinearLayout) view.findViewById(R.id.lin_onclick);
        TextView tv_house_select_address = (TextView) view.findViewById(R.id.tv_house_select_address);
        TextView tv_house_select_num = (TextView) view.findViewById(R.id.tv_house_select_num);
        ElasticCardView cardView = (ElasticCardView) view.findViewById(cardview);
        //txt_xiaoqu_name.setText(toMultiLine(data.getCommunity_name(),4));
        //txt_xiaoqu_name.setText(toMultiLine("华晟悦府", 4));
        txt_xiaoqu_name.setText(data.getCommunity_name());
        //}
        tv_house_select_address.setText(data.getAddress());
        tv_house_select_num.setText(data.getPer_count() + "人已绑定房屋");
        final boolean isCard = mode == CardViewPager.MODE_CARD;
        cardView.setPreventCornerOverlap(isCard);
        cardView.setUseCompatPadding(isCard);

        /*final String img = data.getImg();
        Glide.with(context).load(img).into(imageView);*/

        lin_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wuye_type.equals("family")) {//跳转到家庭账单

                    Intent intent2 = new Intent(context, HouseFamilyBillActivity.class);
                    //intent2.putExtra("roomInfo", mHouseBean.getRoom_info());
                    intent2.putExtra("room_id", data.getRoom_id());
                    startActivity(intent2);

                } else if (wuye_type.equals("fw_info")) {//跳转到房屋信息界面
                    Intent intent1 = new Intent(context, HousePersonInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("room_id", data.getRoom_id());
                    System.out.println("room_id***********" + data.getRoom_id());
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                } else if (wuye_type.equals("fw_invite")) {//跳转到访客邀请
                    getResult(context,data.getRoom_id(),"11");

                } else if (wuye_type.equals("fw_lanya")) {//跳转到蓝牙
                    getResult(context,data.getRoom_id(),"22");

                }
                //Toast.makeText(context, "我点到了", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "data:" + data + "position:" + position+"点到了", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void getResult(Context context,final String room_id,final String is_hh) {//判断邀请功能时候开启
        Url_info info = new Url_info(context);
        RequestParams params = new RequestParams();
        params.addBodyParameter("room_id", room_id);
        HttpHelper hh = new HttpHelper(info.checkIsAjb, params, context) {


            @Override
            protected void setData(String json) {
                JSONObject jsonObject, jsonData;
                try {
                    jsonObject = new JSONObject(json);
                    String data = jsonObject.getString("data");
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        jsonData = new JSONObject(data);
                        String mobile = jsonData.getString("mobile");
                        String community = jsonData.getString("community");
                        String building = jsonData.getString("building");
                        String room_code = jsonData.getString("room_code");
                        if (is_hh.equals("11")){
                            Intent intent1 = new Intent(context, HouseInviteActivity.class);
                            intent1.putExtra("room_id", room_id);
                            startActivity(intent1);
                        }else {
                            Intent intent1 = new Intent(context, OpenLanActivity.class);
                            intent1.putExtra("room_id", room_id);
                            startActivity(intent1);
                        }

                    } else {
                        XToast.makeText(context, jsonObject.getString("msg"), XToast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    private String toMultiLine(String str, int len) {
        char[] chs = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, sum = 0; i < chs.length; i++) {
            sum += chs[i] < 0xff ? 1 : 1;
            sb.append(chs[i]);
            if (sum >= len) {
                sum = 0;
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
