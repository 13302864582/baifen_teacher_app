package com.ucuxin.ucuxin.tec.adapter;

import java.util.LinkedHashSet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ucuxin.ucuxin.tec.function.QAHallListItemView;
import com.ucuxin.ucuxin.tec.model.AnswerListItemGson;

public class QAHallListAdapter extends BaseAdapter {

	private Context mContext;
	private LinkedHashSet<AnswerListItemGson> mDataObj;

	private boolean isScrolling;

	public void setScrolling(boolean isScrolling) {
		this.isScrolling = isScrolling;
	}

	public void setData(LinkedHashSet<AnswerListItemGson> dataObj) {
		this.mDataObj = dataObj;
		notifyDataSetChanged();
	}

	public QAHallListAdapter(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mDataObj == null ? 0 : mDataObj.size();
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
		QAHallListItemView itemView = null;
		if (convertView == null) {
			itemView = new QAHallListItemView(mContext);
		} else {
			itemView = (QAHallListItemView) convertView;
		}
		Object[] array = mDataObj.toArray();
		if (array.length > postion) {
			Object obj = array[postion];
			if (obj instanceof AnswerListItemGson) {
				itemView.showData((AnswerListItemGson) obj, isScrolling);
			}
		}
		return itemView;
	}
}
