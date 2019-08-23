package com.ucuxin.ucuxin.tec.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;

public class ContactListRowView extends FrameLayout {
	private UserInfoModel mContactInfo = null;
	private View mView = null;
	private int avatarSize;
	private Context context;

	public ContactListRowView(Context context, AttributeSet attrs, UserInfoModel contact) {
		super(context, attrs);
		this.context = context;
		mContactInfo = contact;
		setupView(context);
	}

	public ContactListRowView(Context context, UserInfoModel contact) {
		super(context);
		this.context = context;
		mContactInfo = contact;
		setupView(context);
	}

	public void setContactGson(UserInfoModel contact) {
		mContactInfo = contact;
		setContactInfo(mView);
	}

	private void setContactInfo(View view) {
		ViewHolder holder = (ViewHolder) view.getTag();

		ImageLoader.getInstance().loadImage(mContactInfo.getAvatar_100(), holder.contactImage,
				R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);

		holder.contactName.setText(mContactInfo.getName());
		int sexId = R.string.sextype_unknown;
		switch(mContactInfo.getSex()){
		case GlobalContant.SEX_TYPE_MAN:
			sexId = R.string.sextype_man;
			break;
		case GlobalContant.SEX_TYPE_WOMEN:
			sexId = R.string.sextype_women;
			break;
		}
		
		if (mContactInfo.getRoleid() == GlobalContant.ROLE_ID_STUDENT) {// 学生
			String grade = mContactInfo.getGrade() == null ? "" : mContactInfo.getGrade();
			holder.extraInfo.setText(context.getString(R.string.contact_list_extraInfo, context.getString(sexId),
					mContactInfo.getSchools(), grade));
			holder.techSchoolInfo.setText("");
		} else if (mContactInfo.getRoleid() == GlobalContant.ROLE_ID_COLLEAGE) {// 老师
			String major = mContactInfo.getMajor() == null ? "" : mContactInfo.getMajor();
			holder.extraInfo.setText(context.getString(R.string.contact_list_extraInfo, context.getString(sexId),
					mContactInfo.getSchools(), major));
			holder.techSchoolInfo.setText("");
		}
		
		int relationtype = mContactInfo.getRelationtype();
		holder.viptag.setVisibility(View.GONE);
		holder.relationType2.setVisibility(View.GONE);
		holder.relationType3.setVisibility(View.GONE);
		switch (relationtype) {
		case 1:
			holder.relationType2.setVisibility(View.VISIBLE);
			break;
		case 2:
			holder.relationType3.setVisibility(View.VISIBLE);
			break;

		default:
			int supervip = mContactInfo.getSupervip();
			if (supervip > 0) {
				holder.viptag.setVisibility(View.VISIBLE);
			}
			break;
		}
		

	}

	final static class ViewHolder {
		View viptag;
		TextView contactName;
		View relationType2;
		View relationType3;
		TextView extraInfo;
		TextView techSchoolInfo;
		NetworkImageView contactImage;
	}

	@SuppressLint("InflateParams")
	private void setupView(Context context) {
		mView = LayoutInflater.from(context).inflate(R.layout.group_contacts_list_row, null);
		ViewHolder holder = new ViewHolder();
		avatarSize = getResources().getDimensionPixelSize(R.dimen.group_contacts_list_avatar_size);
		holder.contactImage = (NetworkImageView) mView.findViewById(R.id.contactImage);
		holder.contactName = (TextView) mView.findViewById(R.id.contactName);
		holder.viptag =  mView.findViewById(R.id.relationType);
		holder.relationType2 =  mView.findViewById(R.id.relationType2);
		holder.relationType3 =  mView.findViewById(R.id.relationType3);
		holder.extraInfo = (TextView) mView.findViewById(R.id.extraInfo);
		holder.techSchoolInfo = (TextView) mView.findViewById(R.id.techSchoolInfo);
		addView(mView);
		mView.setTag(holder);
		setContactInfo(mView);
	}

}
