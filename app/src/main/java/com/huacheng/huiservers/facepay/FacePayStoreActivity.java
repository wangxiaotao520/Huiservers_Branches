package com.huacheng.huiservers.facepay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huacheng.huiservers.HomeActivity;
import com.huacheng.huiservers.BaseUI;
import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.adapter.XiaoquAdapter;
import com.huacheng.huiservers.center.bean.CityBean;
import com.huacheng.huiservers.geren.bean.GroupMemberBean;
import com.huacheng.huiservers.http.HttpHelper;
import com.huacheng.huiservers.protocol.CenterProtocol;
import com.huacheng.huiservers.utils.TextPinyinUtil;
import com.huacheng.huiservers.utils.UIUtils;
import com.huacheng.huiservers.utils.Url_info;
import com.huacheng.huiservers.view.ClearEditText;
import com.huacheng.huiservers.view.Cn2Spell;
import com.huacheng.huiservers.view.PinyinComparator;
import com.huacheng.huiservers.view.SideBar;
import com.lidroid.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * 类：
 * 时间：2018/5/22 17:54
 * 功能描述:Huiservers
 */
public class FacePayStoreActivity extends BaseUI implements View.OnClickListener {
    private TextView title_name, txt_dialog;
    private ListView list_center;
    private LinearLayout lin_left;
    private RelativeLayout title_rel;
    private XiaoquAdapter adapter;
    private String s_id;
    private HashMap<Integer, Boolean> sss;
    CenterProtocol protocol = new CenterProtocol();
    List<CityBean> beans = new ArrayList<CityBean>();
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    //private CharacterParser characterParser;
    private List<GroupMemberBean> SourceDateList;
    private SideBar sideBar;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    /**
     * 搜索框
     */
    private ClearEditText mClearEditText;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.other_xiaoqu_pinyin_list);
  //      SetTransStatus.GetStatus(this);//系统栏默认为黑色
        /*s_id=this.getIntent().getExtras().getString("s_id");
        id=this.getIntent().getExtras().getString("id");
		name=this.getIntent().getExtras().getString("name");*/
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("选择商铺");
        lin_left = (LinearLayout) findViewById(R.id.lin_left);
        lin_left.setOnClickListener(this);
        // 实例化汉字转拼音类
        //characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        txt_dialog = (TextView) findViewById(R.id.txt_dialog);
        sideBar.setTextView(txt_dialog);

        list_center = (ListView) findViewById(R.id.list_center);
        listonclick();
    }

    @Override
    protected void initData() {
        super.initData();
        getSH();

    }

    private void getSH() {//获取商户列表
        showDialog(smallDialog);
        Url_info info = new Url_info(this);
        RequestParams params = new RequestParams();
        //params.addBodyParameter("city", s_id);
        HttpHelper hh = new HttpHelper(info.merchant_list, params, FacePayStoreActivity.this) {

            @Override
            protected void setData(String json) {
                hideDialog(smallDialog);
                beans = protocol.getSH(json);
                SourceDateList = filledData(beans);
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new XiaoquAdapter(FacePayStoreActivity.this, SourceDateList);
                sss = new HashMap<Integer, Boolean>();
                if (sss.size() == 0) {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        sss.put(i, false);
                    }
                }
                adapter.setIsSelected(sss);
                list_center.setAdapter(adapter);
            }

            @Override
            protected void requestFailure(Exception error, String msg) {
                hideDialog(smallDialog);
                UIUtils.showToastSafe("网络异常，请检查网络设置");
            }
        };
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.lin_left:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("sh_name","");
                bundle.putString("sh_id", "");
                bundle.putString("sh_uid", "");
                intent.putExtras(bundle);
                setResult(1111, intent);
                finish();
                break;

            default:
                break;
        }
    }


    private void listonclick() {
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    list_center.setSelection(position);
                }

            }
        });

        list_center.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (i == arg2) {
                        sss.put(i, true);
                    } else {
                        sss.put(i, false);
                    }
                }
                adapter.setIsSelected(sss);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("sh_name", SourceDateList.get(arg2).getName());
                bundle.putString("sh_id", SourceDateList.get(arg2).getId());
                bundle.putString("sh_uid", SourceDateList.get(arg2).getUid());
                intent.putExtras(bundle);
                setResult(200, intent);
                finish();

            }
        });
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                //titleLayout.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                //filterData(s.toString());
                //getXiaoqu();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void startCommunityActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    //================================================

    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    String pinyin, str;

    private List<GroupMemberBean> filledData(List<CityBean> datas) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

        for (int i = 0; i < datas.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            sortModel.setName(datas.get(i).getMerchant_name());
            sortModel.setId(datas.get(i).getId());
            sortModel.setUid(datas.get(i).getUid());
            //sortModel.setParentid(datas.get(i).getParentid());*/
            // 处理2级字体 “晟”
            String sub = datas.get(i).getMerchant_name().substring(0, 1);
            if (sub.equals("晟")) {
                pinyin = "C";
            } else {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    pinyin = TextPinyinUtil.getInstance().getPinyin(datas.get(i).getMerchant_name());
                } else {
                    pinyin = Cn2Spell.getPinYin(datas.get(i).getMerchant_name());
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

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (GroupMemberBean sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || Cn2Spell.getPinYin(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    //=====================================
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("sh_name","");
            bundle.putString("sh_id", "");
            bundle.putString("sh_uid", "");
            intent.putExtras(bundle);
            setResult(1111, intent);
            FacePayStoreActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
