package com.ucuxin.ucuxin.tec.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.model.CatalogModel;

public class CatalogListAdapter extends BaseExpandableListAdapter{

    public static final String TAG = CatalogListAdapter.class.getSimpleName();
    
//    private LayoutInflater inflater;
	private CatalogModel mCatalogModel = null;
	private Context mContext;
    
	public void setCatalogModel(CatalogModel catalogModel) {
		this.mCatalogModel = catalogModel;

		//update the view
		notifyDataSetChanged();
	}
    
	public CatalogListAdapter(Context context) {
		this.mContext = context;
	}
    
	@Override
	public int getGroupCount() {

		if(mCatalogModel != null){
			return mCatalogModel.getGroupCount();
		}
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(mCatalogModel != null){
			return mCatalogModel.getDetailCount(groupPosition);
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {

		if(mCatalogModel != null){
			return mCatalogModel.getGroupWithPos(groupPosition);
		}
		return null;
	}
	
	public Object getGroupState(int groupPosition){
		if(mCatalogModel != null){
			return mCatalogModel.getGroupStateWithPos(groupPosition);
		}
		return null;		
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(mCatalogModel != null){
			return mCatalogModel.getDetailWithPos(groupPosition,childPosition);
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
        return childPosition;				
	}

	@Override
	public boolean hasStableIds() {

		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

        TextView title;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_catalog_list_row, parent, false);
        }
        if(mCatalogModel != null){         
	        title = (TextView) convertView.findViewById(R.id.catalog_item_title);
	        String t = "        " + getGroup(groupPosition);
	        title.setText(t);
	        TextView state = (TextView) convertView.findViewById(R.id.filter_selected_name);
	        state.setText(String.valueOf(getGroupState(groupPosition)));
        }
        return convertView;				
	}

	//public TextView[] stateViews = new TextView[4];
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
        TextView title;

        if(convertView == null){
//            convertView = inflater.inflate(R.layout.fragment_catalog_detail_row, parent, false);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_catalog_detail_row, parent, false);
        }
        if(mCatalogModel != null){        
        	title = (TextView) convertView.findViewById(R.id.catalog_detail_name);
        	String name = "        "+getChild(groupPosition,childPosition);
        	title.setText(name);
        }
        return convertView;				
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
}
