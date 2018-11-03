package com.huacheng.huiservers.dialog;


import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.utils.AppUpdate;

public class SignOnDialog implements OnClickListener {
    private Activity mcontext;
    private Dialog mDialog;
    private Button bt_next;
    private PhotoView iv_photo2;
    View v;


    public SignOnDialog( Activity context,final String url,final String name) {
        this.mcontext = context;
        v=(LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_is_wifi, null);
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCanceledOnTouchOutside(true);
        bt_next = (Button) v.findViewById(R.id.bt_next);
        bt_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
                intent.putExtra("file_name",name);
                intent.putExtra("download_src",url);
                intent.setClass(mcontext,DownLoadDialog.class);
                mcontext.startActivity(intent);
				
			}
		});

    }

    public void dismiss(){
        mDialog.dismiss();
    }

    public void show(){
    	 mDialog.show();
         mDialog.getWindow().setContentView(v);
         WindowManager windowManager = mcontext.getWindowManager();
         Display display = windowManager.getDefaultDisplay();
         WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
         lp.width = (int)(display.getWidth()-330); //设置宽度
         mDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
