package com.ucuxin.ucuxin.tec.function.teccourse.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.function.teccourse.model.EduPartnerModel;

public class EduPartnerHolder extends BaseHolder<EduPartnerModel>{

	private TextView headerText;
	private NetworkImageView contactImage;
	private TextView contactName;
	private View relationType2; //关注
	private View viptag; //VIP
	private View relationType3; //SVIP
	private TextView extraInfo;
	private TextView techSchoolInfo;
	private int avatarSize;

	@Override
	public View initView() {
		avatarSize = TecApplication.getContext().getResources().getDimensionPixelSize(R.dimen.group_contacts_list_avatar_size);
		View view = View.inflate(TecApplication.getContext(), R.layout.group_contacts_list_row, null);
		headerText = (TextView) view.findViewById(R.id.headerText);
		contactImage = (NetworkImageView) view.findViewById(R.id.contactImage);
		contactName = (TextView) view.findViewById(R.id.contactName);
		viptag = view.findViewById(R.id.relationType);
		relationType2 = view.findViewById(R.id.relationType2);
		relationType3 = view.findViewById(R.id.relationType3);
		extraInfo = (TextView) view.findViewById(R.id.extraInfo);
		techSchoolInfo = (TextView) view.findViewById(R.id.techSchoolInfo);
		return view;
	}

	@Override
	public void refreshView() {
		EduPartnerModel data = getData();
		
		//分组字母显示 
		showHeaderText(data.getNamepinyin(), isParamBool());
		ImageLoader.getInstance().loadImage(data.getAvatar(), contactImage,
				R.drawable.ic_default_avatar, avatarSize, avatarSize / 10);
		
		contactName.setText(data.getName());
		
		setRelationType(data.getRelationtype());
		
		extraInfo.setText(data.getGrade()+"\t\t"+data.getSchools());
	}
	
	private void setRelationType(int type){
		//1关注  2会员
		if(type == 1){
			relationType2.setVisibility(View.VISIBLE);
			relationType3.setVisibility(View.GONE);
		}else{
			relationType3.setVisibility(View.VISIBLE);
			relationType2.setVisibility(View.GONE);
		}
	}
	
	private void showHeaderText(String text, boolean isShow){
		if(!TextUtils.isEmpty(text) && isShow){
			headerText.setText(text);
			headerText.setVisibility(View.VISIBLE);
		}else{
			headerText.setVisibility(View.GONE);
		}
	}
}
