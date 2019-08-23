package com.ucuxin.ucuxin.tec.function.partner;

import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;*/
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.teccourse.model.EduPartnerModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


/**
 * 学伴activity
 * @author:  sky
 */
public class PartnerFragment extends Fragment implements OnClickListener, HttpListener{
	
	private static final String TAG = PartnerFragment.class.getSimpleName();
	public static final String FRAGEMT_1 = "GroupFragment";
	public static final String FRAGEMT_2 = "EduPartnerFragment";
	
	private String CurrentPage = FRAGEMT_1;
	private TextView tv_edu;
	private TextView tv_friend;
	
	private GroupFragment groupFragment;
	private EduPartnerFragment eduPartnerFragment;
	private List<EduPartnerModel> mEduData = new ArrayList<EduPartnerModel>();
	private LinearLayout partner_ll;
	private View view;
	private boolean isOnSelecteError;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		initView();
		if(isOnSelecteError){
			isOnSelecteError = false;
			onSelected(true);
		}
		return view;
	}

	private synchronized void initView() {
		if(view == null){
			view = View.inflate(getActivity(), R.layout.fragment_partner, null);
			partner_ll = (LinearLayout) view.findViewById(R.id.partner_ll);
			tv_friend = (TextView) view.findViewById(R.id.partner_tv_friend);
			tv_edu = (TextView) view.findViewById(R.id.partner_tv_edu);
			
			tv_friend.setOnClickListener(this);
			tv_edu.setOnClickListener(this);
			
			tv_friend.performClick();
		}
	}

	@Override
	public void onClick(View v) {
		FragmentTransaction ft = null;
		switch (v.getId()) {
		case R.id.partner_tv_friend: // 好友
			if(groupFragment == null){
				groupFragment = new GroupFragment();
			}
			CurrentPage = FRAGEMT_1;
			tv_friend.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
			tv_edu.setTextColor(getResources().getColor(R.color.master_tab_losefocus));

			ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.partner_fl_content, groupFragment, FRAGEMT_1);
			ft.commit();
			break;
		case R.id.partner_tv_edu: // 机构
			if(eduPartnerFragment == null){
				eduPartnerFragment = new EduPartnerFragment(mEduData);
			}else{
				eduPartnerFragment.refreshData();
			}
			CurrentPage = FRAGEMT_2;
			tv_edu.setTextColor(getResources().getColor(R.color.master_tab_gotfocus));
			tv_friend.setTextColor(getResources().getColor(R.color.master_tab_losefocus));

			ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.partner_fl_content, eduPartnerFragment, FRAGEMT_2);
			ft.commit();
			break;
		}
	}
	
	public void onSelected(boolean isSelected) {
		if(isSelected){ //当用户选中学伴的时候请求机构数据
			if (getActivity() == null) {
				isOnSelecteError = true;
				return;
			}
			initView();
			UserInfoModel userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
			if(userInfo != null){
				JSONObject json = new JSONObject();
				try {
					json.put("orgid", userInfo.getOrgid());
				} catch (JSONException e) {
					LogUtils.e(TAG, "Json： ", e);
				}
				OkHttpHelper.post(getActivity(), "org","orgstudents",json,this);
			}
		}
		if(CurrentPage.equals(FRAGEMT_1) && groupFragment != null){
			groupFragment.onSelected(isSelected);
		}
	}

	
	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		//(OK)机构页面数据请求
		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<EduPartnerModel> listData =  null;
			try {
				listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<EduPartnerModel>>() {}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			
			if (listData != null && listData.size() > 0) {
				mEduData.clear();
				mEduData.addAll(listData);
				if (partner_ll != null) {
					partner_ll.setVisibility(View.VISIBLE);
				}
			}else{
				//冒得数据, 干掉分组
				if (partner_ll != null) {
					partner_ll.setVisibility(View.GONE);
				}
				tv_friend.performClick();
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

		
	}

	
}
