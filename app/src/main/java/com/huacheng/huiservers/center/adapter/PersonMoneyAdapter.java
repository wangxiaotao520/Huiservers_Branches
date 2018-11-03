package com.huacheng.huiservers.center.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huacheng.huiservers.R;
import com.huacheng.huiservers.center.bean.ListBean;
import com.huacheng.huiservers.utils.StringUtils;

import java.util.List;

public class PersonMoneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
	// 普通布局
	private final int TYPE_ITEM = 1;
	// 脚布局
	private final int TYPE_FOOTER = 2;
	// 当前加载状态，默认为加载完成
	private int loadState = 2;
	// 正在加载
	public final int LOADING = 1;
	// 加载完成
	public final int LOADING_COMPLETE = 2;
	// 加载到底
	public final int LOADING_END = 3;

	private List<ListBean> bean;
	private Context context;

	public PersonMoneyAdapter(Context context,List<ListBean> bean){
		this.context=context;
		this.bean=bean;
	}


	@Override
	public int getItemViewType(int position) {
		// 最后一个item设置为FooterView
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;

		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 通过判断显示类型，来创建不同的View
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.person_my_money_item, parent, false);
			return new RecyclerViewHolder(view);

		} else if (viewType == TYPE_FOOTER) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.layout_refresh_footer, parent, false);
			return new FootViewHolder(view);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof RecyclerViewHolder) {
			RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
			recyclerViewHolder.txt_jiadian.setText(bean.get(position).getLog_title());
			if (bean.get(position).getAtype().equals("1")) {
				recyclerViewHolder.txt_ordermoney.setText("+¥"+bean.get(position).getAmount());
				recyclerViewHolder.txt_ordermoney.setTextColor(context.getResources().getColor(R.color.green));
			}else {
				recyclerViewHolder.txt_ordermoney.setText("-¥"+bean.get(position).getAmount());
				recyclerViewHolder.txt_ordermoney.setTextColor(context.getResources().getColor(R.color.colorPrimary));
			}
			recyclerViewHolder.txt_time.setText(StringUtils.getDateToString(bean.get(position).getPaytime(), "1"));
			recyclerViewHolder.txt_data.setText(bean.get(position).getLog_description());
			if (bean.get(position).getPaytype().equals("alipay")) {
				recyclerViewHolder.txt_fukuan.setText("付款方式：支付宝");
			}else if (bean.get(position).getPaytype().equals("wxpay")) {
				recyclerViewHolder.txt_fukuan.setText("付款方式：微信");
			}else if (bean.get(position).getPaytype().equals("hcpay")) {
				recyclerViewHolder.txt_fukuan.setText("付款方式：一卡通支付");
			}else if (bean.get(position).getPaytype().equals("bestpay")) {
				recyclerViewHolder.txt_fukuan.setText("付款方式：翼支付");
			}
			
		}else if (holder instanceof FootViewHolder) {
			FootViewHolder footViewHolder = (FootViewHolder) holder;
			switch (loadState) {
				case LOADING: // 正在加载
					footViewHolder.pbLoading.setVisibility(View.VISIBLE);
					footViewHolder.tvLoading.setVisibility(View.VISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					break;

				case LOADING_COMPLETE: // 加载完成
					footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
					footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
					footViewHolder.llEnd.setVisibility(View.GONE);
					break;

				case LOADING_END: // 加载到底
					footViewHolder.pbLoading.setVisibility(View.GONE);
					footViewHolder.tvLoading.setVisibility(View.GONE);
					footViewHolder.llEnd.setVisibility(View.VISIBLE);
					break;

				default:
					break;
			}
		}
	}


	@Override
	public int getItemCount() {
		return bean.size() + 1;
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager) {
			final GridLayoutManager gridManager = ((GridLayoutManager) manager);
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
				@Override
				public int getSpanSize(int position) {
					// 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
					return getItemViewType(position) == TYPE_FOOTER ? gridManager.getSpanCount() : 1;

				}
			});
		}
	}

	private class RecyclerViewHolder extends RecyclerView.ViewHolder {
		TextView txt_jiadian,txt_ordermoney,txt_data,txt_time,txt_fukuan;

		public RecyclerViewHolder(View itemView) {
			super(itemView);
			txt_jiadian=(TextView) itemView.findViewById(R.id.txt_jiadian);
			txt_ordermoney=(TextView) itemView.findViewById(R.id.txt_ordermoney);
			txt_data=(TextView) itemView.findViewById(R.id.txt_data);
			txt_time=(TextView) itemView.findViewById(R.id.txt_time);
			txt_fukuan=(TextView) itemView.findViewById(R.id.txt_fukuan);
		}
	}

	private class FootViewHolder extends RecyclerView.ViewHolder {

		ProgressBar pbLoading;
		TextView tvLoading;
		LinearLayout llEnd;

		FootViewHolder(View itemView) {
			super(itemView);
			pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
			tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
			llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
		}
	}

	/**
	 * 设置上拉加载状态
	 *
	 * @param loadState 0.正在加载 1.加载完成 2.加载到底
	 */
	public void setLoadState(int loadState) {
		this.loadState = loadState;
		notifyDataSetChanged();
	}

}
