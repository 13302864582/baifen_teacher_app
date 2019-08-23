package com.ucuxin.ucuxin.tec.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.ContactListRowView;
import com.ucuxin.ucuxin.tec.model.ContactsModel;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;

public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

	public static final String TAG = ContactsListAdapter.class.getSimpleName();
	private ContactsModel mContactsModel = null;
	private Context mContext = null;
	private int avatarSize;

	public ContactsListAdapter(Context context) {
		avatarSize = context.getResources().getDimensionPixelSize(R.dimen.group_contacts_list_avatar_size);
		this.mContext = context;
	}

	public void setContactsModel(ContactsModel contactsModel) {
		this.mContactsModel = contactsModel;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mContactsModel != null ? mContactsModel.getContactsCount() : 0;
	}

	protected void bindSectionHeader(View view, int position, String secheader,
			boolean displaySectionHeader) {

		if (displaySectionHeader) {
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
			TextView SectionTitle = (TextView) view.findViewById(R.id.header)
					.findViewById(R.id.headerText);
			SectionTitle.setText(secheader);
		} else {
			view.findViewById(R.id.header).setVisibility(View.GONE);
		}
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewgroup) {
		
		
		int pos = position;
		int count = mContactsModel.getContactsCount();
		if (count <= position) {
			pos = count - 1;
		}
		ContactListRowView row = null;

		UserInfoModel contact = mContactsModel.getItem(pos);// item.getContactInfo();
		String secheader = contact.getNamepinyin();
		boolean flag = false;
		if (secheader != null) {
			if (!secheader.matches("[A-Z]")) {
				secheader = "#";
			}
			flag = mContactsModel.checkFirstOfSection(pos);
		}

		if (view == null) {
			row = new ContactListRowView(mContext, contact);
		} else {
			row = (ContactListRowView) view;
			row.setContactGson(contact);
		}

		if (secheader != null) {
			bindSectionHeader(row, pos, secheader, flag);
		}
		
		return row;
	}
	
	class ViewHolder {
		LinearLayout headerLayout;
		TextView header;
		ImageView viptag;
		TextView contactName;
		TextView extraInfo;
		TextView techSchoolInfo;
		NetworkImageView contactImage;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return mContactsModel.getItem(position);
	}

	@Override
	public int getPositionForSection(int section) {
		return mContactsModel.getContactsPositionForSection(section);
	}

	@Override
	public int getSectionForPosition(int position) {
		return mContactsModel.getContactsSectionForPosition(position);
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
