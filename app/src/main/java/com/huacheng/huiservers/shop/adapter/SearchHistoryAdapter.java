package com.huacheng.huiservers.shop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.shop.SearchShopActivity;
import com.huacheng.huiservers.utils.StringUtils;
import com.huacheng.huiservers.utils.XToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/02/03.
 */

public class SearchHistoryAdapter extends BaseAdapter {

    private List<String> mHistoryKeywords;
    private Context mContext;

    public SearchHistoryAdapter(List<String> historyKeywords, Context context) {
        this.mHistoryKeywords = historyKeywords;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mHistoryKeywords.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    SharedPreferences mPref;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_history, null);
            holder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
            holder.iv_clear = (ImageView) convertView.findViewById(R.id.iv_clear);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contentTextView.setText(mHistoryKeywords.get(position));

        holder.iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPref = mContext.getSharedPreferences("input", Activity.MODE_PRIVATE);
                String oldText = mPref.getString(SearchShopActivity.KEY_SEARCH_HISTORY_KEYWORD, "");
                String delName = mHistoryKeywords.get(position);

                String[] strold = oldText.split(",");

                StringBuilder strBuilder = new StringBuilder();
                ArrayList list = new ArrayList();
                for (int i = 0; i < strold.length; i++) {
                    if (!delName.contains(strold[i])) {
                        list.add(strold[i]);
                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        strBuilder.append(list.get(i));
                    } else {
                        strBuilder.append(list.get(i) + ",");
                    }
                }

                String str = strBuilder.toString();

                SharedPreferences.Editor editor;
                editor = mPref.edit();
                editor.putString(SearchShopActivity.KEY_SEARCH_HISTORY_KEYWORD, str);
                editor.commit();
                mHistoryKeywords.remove(position);
                SearchHistoryAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView contentTextView;
        ImageView iv_clear;
    }
}
