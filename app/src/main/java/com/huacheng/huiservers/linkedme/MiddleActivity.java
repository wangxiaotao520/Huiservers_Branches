package com.huacheng.huiservers.linkedme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.huacheng.huiservers.servicenew.ui.ServiceDetailActivity;
import com.huacheng.huiservers.servicenew.ui.shop.ServiceStoreActivity;
import com.huacheng.huiservers.shop.ShopDetailActivity;
import com.microquation.linkedme.android.LinkedME;
import com.microquation.linkedme.android.indexing.LMUniversalObject;
import com.microquation.linkedme.android.util.LinkProperties;

import java.util.HashMap;

/**
 * <p>中转页面</p>
 *
 * Created by LinkedME06 on 16/11/17.
 */

public class MiddleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LinkedME.TAG, "onCreate: MiddleActivity is called.");
      //  Toast.makeText(this, "MiddleActivity 被调用了", Toast.LENGTH_SHORT).show();
        //获取与深度链接相关的值
        LinkProperties linkProperties = getIntent().getParcelableExtra(LinkedME.LM_LINKPROPERTIES);
        LMUniversalObject lmUniversalObject = getIntent().getParcelableExtra(LinkedME.LM_UNIVERSALOBJECT);
        //LinkedME SDK初始化成功，获取跳转参数，具体跳转参数在LinkProperties中，和创建深度链接时设置的参数相同；
        if (linkProperties != null) {
            Log.i("LinkedME-Demo", "Channel " + linkProperties.getChannel());
            Log.i("LinkedME-Demo", "control params " + linkProperties.getControlParams());
            Log.i("LinkedME-Demo", "link(深度链接) " + linkProperties.getLMLink());
            //获取自定义参数封装成的HashMap对象
            HashMap<String, String> hashMap = linkProperties.getControlParams();

            //获取传入的参数
            String type = hashMap.get("type");
            String title = "";
            String shareContent = "";
            String url_path = "";
            if (type!=null){
                if (type.equals("goods_details")){//商品详情
                    String shop_id = hashMap.get("id");
                    Intent intent = new Intent(this, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("shop_id", shop_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else  if (type.equals("service_shop")){
                    String store_id = hashMap.get("id");
                    Intent intent = new Intent(this, ServiceStoreActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("store_id", store_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else  if (type.equals("service_detail")){
                    String service_id = hashMap.get("id");
                    Intent intent = new Intent(this, ServiceDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("service_id", service_id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
            //清除跳转数据，该方法理论上不需要调用，因Android集成方式各种这样，若出现重复跳转的情况，可在跳转成功后调用该方法清除参数
            //LinkedME.getInstance().clearSessionParams();
        }

        if (lmUniversalObject != null) {
            Log.i("LinkedME-Demo", "title " + lmUniversalObject.getTitle());
            Log.i("LinkedME-Demo", "control " + linkProperties.getControlParams());
            Log.i("ContentMetaData", "metadata " + lmUniversalObject.getMetadata());
        }
        finish();
    }
    
}
