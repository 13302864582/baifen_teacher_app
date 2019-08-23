package com.ucuxin.ucuxin.tec.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ucuxin.ucuxin.tec.function.AnswerListItemView;
import com.ucuxin.ucuxin.tec.model.AnswerListItemGson;

public class AnswerListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<AnswerListItemGson> mList;
	private boolean isScrolling;

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public void setData(ArrayList<AnswerListItemGson> currentList) {
		this.mList = currentList;
		notifyDataSetChanged();
	}

	public AnswerListAdapter(Activity context) {
		super();
		this.mActivity = context;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int postion) {
		return postion;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup container) {
		AnswerListItemView itemView = null;
		if (convertView == null) {
			itemView = new AnswerListItemView(mActivity);
		} else {
			itemView = (AnswerListItemView) convertView;
		}
		if (mList.size() > postion) {
			AnswerListItemGson itemModel = mList.get(postion);
			itemView.showData(itemModel, isScrolling);
		}
		
		return itemView;
	}
}
