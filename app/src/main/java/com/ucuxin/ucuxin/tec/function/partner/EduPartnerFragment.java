package com.ucuxin.ucuxin.tec.function.partner;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.partner.SideBar.OnTouchingLetterChangedListener;
import com.ucuxin.ucuxin.tec.function.teccourse.adapter.BaseHolder;
import com.ucuxin.ucuxin.tec.function.teccourse.adapter.EduPartnerHolder;
import com.ucuxin.ucuxin.tec.function.teccourse.adapter.WBaseAdapter;
import com.ucuxin.ucuxin.tec.function.teccourse.model.EduPartnerModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;

public class EduPartnerFragment extends Fragment implements OnItemClickListener, HttpListener{
	
	private static final String TAG = EduPartnerFragment.class.getSimpleName();
	/** 字母 - 位置*/
	private HashMap<Character, Integer> mLetterIndex = new HashMap<Character, Integer>();
	/** 位置 - 字母*/
	private SparseArray<Character> mIntegerIndex = new SparseArray<Character>();
	private List<EduPartnerModel> mEduData = new ArrayList<EduPartnerModel>();
	private ListView mListView;
	private EduPartnerAdapter mAdapter;

	public EduPartnerFragment(){}

	public EduPartnerFragment(List<EduPartnerModel> data) {
		mEduData.addAll(data);
		initIndex();
	}
	
	/**设置ListView要展示的数据, 会清空执之前的数据*/
	public void setData(List<EduPartnerModel> data){
		mEduData.clear();
		mEduData.addAll(data);
		initIndexRestart(); //重新初始化索引
		mAdapter.notifyDataSetChanged();
	}
	
	public void refreshData(){
		UserInfoModel userInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
		if(userInfo != null){
			JSONObject json = new JSONObject();
			try {
				json.put("orgid", userInfo.getOrgid());
			} catch (JSONException e) {
				LogUtils.e(TAG, "Json： ", e);
			}
			OkHttpHelper.post(getActivity(),"org", "orgstudents",json, this);
		}
	}
	

	
	
	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (!TextUtils.isEmpty(dataJson)) {
			ArrayList<EduPartnerModel> listData =  null;
			try {
				listData = new Gson().fromJson(dataJson, new TypeToken<ArrayList<EduPartnerModel>>() {}.getType());
			} catch (Exception e) {
				LogUtils.e(TAG, "数据请求失败！", e);
			}
			
			if (listData != null && listData.size() > 0) {
				setData(listData);
			}
		}
	
		
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_group, null);
		mListView = (ListView) view.findViewById(R.id.contactsList);
		TextView mDialog = (TextView) view.findViewById(R.id.dialog);
		SideBar mSideBar = (SideBar) view.findViewById(R.id.sidrbar);
		
		mSideBar.setTextView(mDialog);
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {

				int position = getPositionForChar(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});
		mAdapter = new EduPartnerAdapter(mEduData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return view;
	}
	
	private class EduPartnerAdapter extends WBaseAdapter<EduPartnerModel>{

		public EduPartnerAdapter(List<EduPartnerModel> data) {
			super(data);
		}

		@Override
		public BaseHolder<EduPartnerModel> createHolder() {
			return new EduPartnerHolder();
		}
		
		@Override
		public boolean getBoolParam(int position) {
			initIndex();
			
			Character character = mIntegerIndex.get(position);
			return character != null;
		}
	}
	
	private synchronized void initIndexRestart(){
		mLetterIndex.clear();
		mIntegerIndex.clear();
		initIndex();
	}
	
	private synchronized void initIndex() {
		if (mLetterIndex.size() <= 0 || mIntegerIndex.size() <= 0) {
			String temp = "";
			int index = 0;
			for (EduPartnerModel data : mEduData) {
				String sortStr = data.getNamepinyin();
				if (sortStr != null) {
					if (!sortStr.matches("[A-Z]")) {
						sortStr = "#";
					}
				}
				if(!TextUtils.isEmpty(sortStr) && !sortStr.equals(temp)){ // 出现一个新的字母
					mLetterIndex.put(sortStr.charAt(0), index); //存入索引
					mIntegerIndex.put(index, sortStr.charAt(0)); //存入索引
					temp = sortStr;
				}
				index++;
			}
		}
	}

	/** 找到第一个 (字母) 出现的位置*/
	private int getPositionForChar(char c) {
		initIndex();

		Integer integer = mLetterIndex.get(c);
		if (null != integer) {
			return integer;
		}
		return -1;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		EduPartnerModel model = mEduData.get(position);
		
		Bundle data = new Bundle();
		data.putInt("userid", model.getUserid());
		data.putInt("roleid", GlobalContant.ROLE_ID_STUDENT);
		IntentManager.goToStudentInfoView(getActivity(), data);
	}
	
	
	
	


}
