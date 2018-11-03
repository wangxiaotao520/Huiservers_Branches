
package com.huacheng.huiservers.center;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.ShopProtocol;
import com.huacheng.huiservers.utils.MyCookieStore;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.XToast;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;

public class OrderPingjiaActivity extends BaseUI implements OnClickListener{
	private LinearLayout lin_left; 
	private TextView title_name,txt_name,txt_price,txt_num,txt_btn;
	private ImageView img_title;
	float rating1,rating2,rating3;
	BitmapUtils bitmapUtils;
	private EditText edt_content;
	private RatingBar room_ratingbar1,room_ratingbar2,room_ratingbar3;
	ShopProtocol protocol=new ShopProtocol();
	//ShopOTOrderBean info;
	private String o_id,p_id,p_info_id,price,numer,img,p_title;
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		setContentView(R.layout.shouhuo_pingjia);
//		SetTransStatus.GetStatus(this);//系统栏默认为黑色
		bitmapUtils=new BitmapUtils(this);
		//info=(ShopOTOrderBean) this.getIntent().getSerializableExtra("list_info");
		o_id=this.getIntent().getExtras().getString("o_id");
		p_id=this.getIntent().getExtras().getString("p_id");
		p_info_id=this.getIntent().getExtras().getString("p_info_id");
		price=this.getIntent().getExtras().getString("price");
		numer=this.getIntent().getExtras().getString("numer");
		img=this.getIntent().getExtras().getString("img");
		p_title=this.getIntent().getExtras().getString("p_title");
		/*bundle.putString("o_id",beans.get(arg0).getOid());
		bundle.putString("p_id",beans.get(arg0).getP_id());
		bundle.putString("p_info_id",beans.get(arg0).getId());
		bundle.putString("price",beans.get(arg0).getPrice());
		bundle.putString("numer",beans.get(arg0).getNumber());
		bundle.putString("img",beans.get(arg0).getP_title_img());
		bundle.putString("p_title",beans.get(arg0).getP_title());*/
		lin_left=(LinearLayout) findViewById(R.id.lin_left);
		lin_left.setOnClickListener(this);
		title_name=(TextView) findViewById(R.id.title_name);
		title_name.setText("订单评价");
		txt_btn=(TextView) findViewById(R.id.txt_btn);
		edt_content=(EditText) findViewById(R.id.edt_content);
		room_ratingbar1=(RatingBar) findViewById(R.id.room_ratingbar1);
		room_ratingbar2=(RatingBar) findViewById(R.id.room_ratingbar2);
		room_ratingbar3=(RatingBar) findViewById(R.id.room_ratingbar3);
		img_title=(ImageView) findViewById(R.id.img_title);
		txt_name= (TextView) findViewById(R.id.txt_name);
		txt_num= (TextView) findViewById(R.id.txt_num);
		txt_price= (TextView) findViewById(R.id.txt_price);
		bitmapUtils.display(img_title, img);
		txt_name.setText(p_title);
		txt_num.setText(numer);
		txt_price.setText("¥"+price);
		txt_btn.setOnClickListener(this);
		onchecked();
	}

	private void onchecked() {
		room_ratingbar1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				System.out.println("-----");
				rating1=room_ratingbar1.getRating();
			}
		});
		room_ratingbar2.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				rating2=room_ratingbar2.getRating();
			}
		});
		room_ratingbar3.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				rating3=room_ratingbar3.getRating();
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.lin_left:
			finish();
			break;
		case R.id.txt_btn://提交评价
			if (String.valueOf(rating1).equals("0")) {
				XToast.makeText(this, "描述评价星级不能为空", XToast.LENGTH_SHORT).show();
			}else if (String.valueOf(rating2).equals("0")) {
				XToast.makeText(this,"宝贝质量星级不能为空", XToast.LENGTH_SHORT).show();
			}else if (String.valueOf(rating3).equals("0")) {
				XToast.makeText(this,"送货速度星级不能为空", XToast.LENGTH_SHORT).show();
			}else if(edt_content.getText().toString().equals("")){
				XToast.makeText(this,"商品描述不能为空", XToast.LENGTH_SHORT).show();
			}else{
				getsubmint();
			}
			break;
		default:
			break;
		}

	}
	private void getsubmint() {//提交评价
		showDialog(smallDialog);
		RequestParams params=new RequestParams();
		params.addBodyParameter("oid",o_id);//订单id
		params.addBodyParameter("p_id",p_id);//商品id
		params.addBodyParameter("order_info_id",p_info_id);//商品信息id
		params.addBodyParameter("appearance",String.valueOf(rating1));
		params.addBodyParameter("quality",String.valueOf(rating2));
		params.addBodyParameter("speed",String.valueOf(rating3));
		params.addBodyParameter("description",edt_content.getText().toString());
		HttpHelper hh=new HttpHelper(MyCookieStore.SERVERADDRESS+"userCenter/shopping_order_score/",
				params,OrderPingjiaActivity.this) {

			@Override
			protected void setData(String json) {
				hideDialog(smallDialog);
				str=protocol.setShop(json);
				if (str.equals("1")) {
					finish();
					XToast.makeText(OrderPingjiaActivity.this,"评价成功", XToast.LENGTH_SHORT).show();
				}else {
					XToast.makeText(OrderPingjiaActivity.this,str, XToast.LENGTH_SHORT).show();
				}
			}

			@Override
			protected void requestFailure(Exception error, String msg) {
				hideDialog(smallDialog);
				UIUtils.showToastSafe("网络异常，请检查网络设置");
			}
		};

	}
}
