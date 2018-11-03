package com.huacheng.huiservers.property;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.XiaoquAdapter;
import com.huacheng.huiservers.geren.bean.GroupMemberBean;
import com.huacheng.huiservers.utils.SharePrefrenceUtil;
import com.huacheng.huiservers.utils.TextPinyinUtil;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.utils.XToast;
import com.huacheng.huiservers.view.Cn2Spell;
import com.huacheng.huiservers.view.PinyinComparator;
import com.huacheng.huiservers.view.SideBar;
import com.huacheng.huiservers.wuye.bean.WuYeBean;
import com.huacheng.libraryservice.base.BaseActivity;
import com.huacheng.libraryservice.http.MyOkHttp;
import com.huacheng.libraryservice.http.response.JsonResponseHandler;
import com.huacheng.libraryservice.utils.json.JsonUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 选择小区Activity
 * created by wangxiaotao
 * 2018/8/23 0023 下午 5:34
 */
public class SelectCommunityActivity extends BaseActivity implements View.OnClickListener {
    private TextView txt_dialog, txt_search, text_city;
    private ListView list_center;
    private ImageView search_back;
    private XiaoquAdapter adapter;
    private RelativeLayout rel_no_data;
    private LinearLayout ly_tag;
    private HashMap<Integer, Boolean> sss;
    /**
     * 汉字转换成拼音的类
     */
    //private CharacterParser characterParser;
    private List<GroupMemberBean> SourceDateList = new ArrayList<>();
    private SideBar sideBar;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private String id, name, type, region_city;



    @Override
    protected void initView() {

        pinyinComparator = new PinyinComparator();
        findTitleViews();
        titleName.setText("选择小区");
        sideBar = findViewById(R.id.sidrbar);
        txt_dialog = findViewById(R.id.txt_dialog);
        sideBar.setTextView(txt_dialog);
        list_center = findViewById(R.id.list_center);
    }

    @Override
    protected void initData() {
        showDialog(smallDialog);
        getloactionCommunity();
    }

    private void getloactionCommunity() {
        SharePrefrenceUtil prefrenceUtil = new SharePrefrenceUtil(this);
        HashMap<String, String> params = new HashMap<>();
        params.put("community_id",prefrenceUtil.getXiaoQuId());
        MyOkHttp.get().post(Url_info.get_pro_com, params, new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                hideDialog(smallDialog);
                if (JsonUtil.getInstance().isSuccess(response)) {
                    List<WuYeBean> cityBeanList = (List<WuYeBean>) JsonUtil.getInstance().getDataArrayByName(response, "data", WuYeBean.class);


                    if (cityBeanList != null) {
                        if (cityBeanList.size() > 0) {
                            //      rel_no_data.setVisibility(View.GONE);
                            sideBar.setVisibility(View.VISIBLE);
                            List<GroupMemberBean> SourceDateList_new = filledData(cityBeanList);
                            SourceDateList.clear();
                            SourceDateList.addAll(SourceDateList_new);
                            // 根据a-z进行排序源数据
                            Collections.sort(SourceDateList, pinyinComparator);
                            if (adapter == null) {
                                adapter = new XiaoquAdapter(SelectCommunityActivity.this, SourceDateList);
                                list_center.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                            }
                            //      mFirstIn_region_id = cityBean.getCommunity_list().get(0).getRegion_id();
                        } else {
                            rel_no_data.setVisibility(View.VISIBLE);
                            sideBar.setVisibility(View.GONE);
                            txt_dialog.setVisibility(View.GONE);
                            SourceDateList.clear();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            //        mFirstIn_region_id = 0 + "";
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                hideDialog(smallDialog);
                XToast.makeText(SelectCommunityActivity.this, "网络错误，请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<GroupMemberBean> filledData(List<WuYeBean> datas) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

        for (int i = 0; i < datas.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setName(datas.get(i).getName()+"");
            sortModel.setId(datas.get(i).getId()+"");
            sortModel.setIs_ym(datas.get(i).getIs_ym()+"");
            sortModel.setCompany_id(datas.get(i).getCompany_id()+"");
            sortModel.setCompany_name(datas.get(i).getCompany_name()+"");
            sortModel.setDepartment_id(datas.get(i).getDepartment_id()+"");
            sortModel.setDepartment_name(datas.get(i).getDepartment_name()+"");
            // 处理2级字体 “晟”
            String sub = datas.get(i).getName().substring(0, 1);
            String pinyin = "";
            if (sub.equals("晟")) {
                pinyin = "C";
            } else {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    pinyin = TextPinyinUtil.getInstance().getPinyin(datas.get(i).getName());
                } else {
                    pinyin = Cn2Spell.getPinYin(datas.get(i).getName());
                }
            }
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            mSortList.add(sortModel);
        }
        return mSortList;

    }

    @Override
    protected void initListener() {
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if (SourceDateList == null || adapter == null) {
                    return;
                }
                if (SourceDateList.size() == 0) {
                    return;
                }
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    list_center.setSelection(position);
                }
            }
        });

        list_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                    long arg3) {
//                String name = SourceDateList.get(position).getName();
//                ToastUtils.showShort(SelectCommunityActivity.this,name);
                Intent intent = new Intent();
                intent.putExtra("community", SourceDateList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        list_center.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_community;
    }

    @Override
    protected void initIntentData() {
        Intent intent = getIntent();

    }

    @Override
    protected int getFragmentCotainerId() {
        return 0;
    }

    @Override
    protected void initFragment() {

    }

    @Override
    public void onClick(View v) {

    }
}