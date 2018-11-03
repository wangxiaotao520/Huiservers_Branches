package com.huacheng.huiservers.property;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.property.adapter.SelectCommonAdapter;
import com.huacheng.huiservers.property.bean.ModelSelectCommon;
import com.huacheng.huiservers.property.inter.SelectCommonInterface;
import com.huacheng.huiservers.property.presenter.SelectCommonPresenter;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.libraryservice.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 通用选择Activity（选择楼号 选择单元号...）
 * created by wangxiaotao
 * 2018/8/27 0027 下午 6:55
 */
public class SelectCommonActivity extends BaseActivity implements SelectCommonPresenter.OnGetDataListener {
    String name="";
    int type=0;
    private String community_id;
    private String houses_type;
    private String buildsing_id;
    private String unit;
    private ListView listview;
    List<SelectCommonInterface> mDatas=new ArrayList();
    private SelectCommonAdapter<SelectCommonInterface> adapter;
    private SelectCommonPresenter selectCommonPresenter;
    private RelativeLayout rel_no_data;
    @Override
    protected void initIntentData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        type = intent.getIntExtra("type",0);
        if (type==2){
            //楼号
            community_id=intent.getStringExtra("community_id");
            houses_type=intent.getStringExtra("houses_type");
        }else if (type==3){
            //单元号
            community_id=intent.getStringExtra("community_id");
            houses_type=intent.getStringExtra("houses_type");
            buildsing_id=intent.getStringExtra("buildsing_id");
        }else if (type==4){
            //房间号
            community_id=intent.getStringExtra("community_id");
            houses_type=intent.getStringExtra("houses_type");
            buildsing_id=intent.getStringExtra("buildsing_id");
            unit=intent.getStringExtra("unit");
        }else if (type==5){
            //商铺号
            community_id=intent.getStringExtra("community_id");
            houses_type=intent.getStringExtra("houses_type");
        }else if (type==6){
            //抄表费项
            community_id=intent.getStringExtra("community_id");
        }
    }
    @Override
    protected void initView() {
        findTitleViews();
        titleName.setText(name);
        listview = findViewById(R.id.listview);
        adapter = new SelectCommonAdapter<>(this, mDatas,type);
        listview.setAdapter(adapter);
        selectCommonPresenter = new SelectCommonPresenter(this, this);
        rel_no_data=findViewById(R.id.rel_no_data);
    }

    @Override
    protected void initData() {
        showDialog(smallDialog);
        if (type==0){
            selectCommonPresenter.getHouseType();
        }else if (type==1){
            selectCommonPresenter.getChaobiaoType();
        }else if (type==2) {
            selectCommonPresenter.getFloor(community_id,houses_type);
        }else if (type==3){
            selectCommonPresenter.getUnit(community_id,houses_type,buildsing_id);
        }else if (type==4){
            selectCommonPresenter.getRoom(community_id,houses_type,buildsing_id,unit);
        }else if (type==5){
            selectCommonPresenter.getShops(community_id,houses_type);
        }else if (type==6){
            selectCommonPresenter.getCBType(community_id);
        }
    }

    @Override
    protected void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id==-1){
                    return;
                }
                ModelSelectCommon modelSelectCommon = (ModelSelectCommon) mDatas.get((int) id);
                    Intent intent = new Intent();
                    intent.putExtra("modelselectcommon",modelSelectCommon);
                    setResult(RESULT_OK,intent);
                    finish();

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_common;
    }



    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    /**
     * 获取数据
     * @param status
     * @param datas
     * @param msg
     */
    @Override
    public void onGetData(int status, List<ModelSelectCommon> datas, String msg) {
        hideDialog(smallDialog);
        if (status==1){
            if (datas!=null){
                mDatas.clear();
                mDatas.addAll(datas);
//                if (type==2){
//                    //楼号
//                    ModelSelectCommon modelSelectCommon = new ModelSelectCommon();
//                    modelSelectCommon.setBuildsing_id("-1");
//                    mDatas.add(0,modelSelectCommon);
//                }else if (type==3){
//                    //单元号
//                    ModelSelectCommon modelSelectCommon = new ModelSelectCommon();
//                    modelSelectCommon.setUnit("-1");
//                    mDatas.add(0,modelSelectCommon);
//                }else if (type==4){
//                    //房间号
//                    ModelSelectCommon modelSelectCommon = new ModelSelectCommon();
//                    modelSelectCommon.setCode("-1");
//                    mDatas.add(0,modelSelectCommon);
//                }else if (type==5){
//                    //商铺号
//                    ModelSelectCommon modelSelectCommon = new ModelSelectCommon();
//                    modelSelectCommon.setCode("-1");
//                    mDatas.add(0,modelSelectCommon);
//                }
                if (mDatas.size()==0){
                    rel_no_data.setVisibility(View.VISIBLE);
                }else {
                    adapter.notifyDataSetChanged();
                    rel_no_data.setVisibility(View.GONE);
                }
            }
        }else {
            XToast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        }
    }
}
