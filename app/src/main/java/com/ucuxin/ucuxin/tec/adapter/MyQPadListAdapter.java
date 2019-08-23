package com.ucuxin.ucuxin.tec.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ucuxin.ucuxin.tec.function.question.MyQPadListItemView;
import com.ucuxin.ucuxin.tec.function.question.model.AnswerListItemModel;

public class MyQPadListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<AnswerListItemModel> mList;
	private boolean isScrolling;
	private boolean isadd;
	
	public boolean isIsadd() {
		return isadd;
	}

	public void setIsadd(boolean isadd) {
		this.isadd = isadd;
	}

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}
	
	public void setData(ArrayList<AnswerListItemModel> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public MyQPadListAdapter(Activity context) {
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
		MyQPadListItemView itemView = null;
		if (convertView == null) {
			itemView = new MyQPadListItemView(mActivity,isadd);
		} else {
			itemView = (MyQPadListItemView) convertView;
		}
		if (mList.size() > postion) {
			AnswerListItemModel itemModel = mList.get(postion);
			itemView.showData(itemModel, isScrolling);
		}
		return itemView;
	}
}
