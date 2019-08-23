package com.ucuxin.ucuxin.tec.function.partner;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.adapter.ContactsListAdapter;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.partner.SideBar.OnTouchingLetterChangedListener;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ContactsModel;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.MyAsyncTask;
import com.ucuxin.ucuxin.tec.utils.ThreadPoolUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class GroupFragment extends Fragment implements HttpListener, OnItemClickListener {

	private static final int REFLASH_TIME = 2000;
	private ListView mContactsListView;
	private ContactsListAdapter mContactsListAdapter;
	private View mView;
	private Activity mActivity;

	public static final String TAG = GroupFragment.class.getSimpleName();
	private ContactsModel mContactsModel;
	private static long reflashTime;
	private boolean isClearn;
	private SideBar sideBar;
	private TextView dialog;
	
	private boolean isThisFragmentSelected = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	private void createContactListModel() {
		if (mContactsModel == null) {
			mContactsModel = new ContactsModel();
		}

		new MyAsyncTask() {
			List<UserInfoModel> infos;

			@Override
			public void preTask() {

			}

			@Override
			public void postTask() {
				if (infos != null && infos.size() > 0) {
					if (mContactsModel != null && mContactsModel.getContactsCount() > 0) {
						mContactsModel.clearnContactsList();
					}
					mContactsModel.setContactList(infos);
				}

				if (mContactsListView.getAdapter() == null) {
					mContactsListView.setAdapter(mContactsListAdapter);
				}
				mContactsListAdapter.setContactsModel(mContactsModel);

				if (System.currentTimeMillis() - reflashTime >= 5000) {
					WeLearnApi.getContactsList(GroupFragment.this.getActivity(), GroupFragment.this);
					reflashTime = System.currentTimeMillis();
				}
			}

			@Override
			public void doInBack() {
				infos = WLDBHelper.getInstance().getWeLearnDB().queryAllContactInfo();
			}
		}.excute();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_group, container, false);

		mContactsListView = (ListView) mView.findViewById(R.id.contactsList);

		if (mContactsModel == null) {
			mContactsModel = new ContactsModel();
		}
		mContactsListAdapter = new ContactsListAdapter(mActivity);

		sideBar = (SideBar) mView.findViewById(R.id.sidrbar);
		dialog = (TextView) mView.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {

				int position = mContactsListAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mContactsListView.setSelection(position);
				}
			}
		});
		createContactListModel();

		mContactsListView.setOnItemClickListener(this);

		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mContactsListView.setVisibility(View.VISIBLE);
		if (System.currentTimeMillis() - reflashTime >= REFLASH_TIME && isThisFragmentSelected) {
//			if (Config.DEBUG) {
//				ToastUtils.show("Get Contact List");
//			}
			WeLearnApi.getContactsList(GroupFragment.this.getActivity(), GroupFragment.this);
			reflashTime = System.currentTimeMillis();
		}
		MobclickAgent.onEventBegin(mActivity, UmengEventConstant.CUSTOM_EVENT_GROUP);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(mActivity, UmengEventConstant.CUSTOM_EVENT_GROUP);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserInfoModel contact = mContactsModel.getItem(position);
		if (contact == null) {
			return;
		}
		Bundle data = new Bundle();
		data.putInt("userid", contact.getUserid());
		data.putInt("roleid", contact.getRoleid());
		if (contact.getRoleid() == 1|contact.getRoleid() == 3) {
			IntentManager.goToStudentInfoView(mActivity, data);
		} else if (contact.getRoleid() == 2) {
			IntentManager.goToTeacherInfoView(mActivity, data);
		}
	}

	@Override
	public void onSuccess(int code, String dataJson, String errMsg) {
		if (code == 0) {

			JSONArray contactsJsonArray = null;
			try {
				contactsJsonArray = new JSONArray(dataJson);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (contactsJsonArray == null) {
				if (mContactsListView.getAdapter() == null) {
					mContactsListView.setAdapter(mContactsListAdapter);
				}

				mContactsListAdapter.setContactsModel(mContactsModel);
				return;
			}
			isClearn = false;
			if (mContactsModel != null && mContactsModel.getContactsCount() > 0) {
				isClearn = true;
				mContactsModel.clearnContactsList();
			}

			final List<UserInfoModel> records = new Gson().fromJson(contactsJsonArray.toString(),
					new TypeToken<ArrayList<UserInfoModel>>() {
					}.getType());
			List<UserInfoModel> contactlist = new ArrayList<UserInfoModel>(records);
			mContactsModel.setContactList(contactlist);

			ThreadPoolUtil.execute(new Runnable() {
				@Override
				public void run() {
					if (isClearn) {
						WLDBHelper.getInstance().getWeLearnDB().clearContactUserInfo();
					}
					if (records.size() > 0) {
						for (UserInfoModel u : records) {
							WLDBHelper.getInstance().getWeLearnDB().insertOrUpdatetContactInfo(u);
						}
					}
				}
			});

			if (mContactsListView.getAdapter() == null) {
				mContactsListView.setAdapter(mContactsListAdapter);
			}
			mContactsListAdapter.setContactsModel(mContactsModel);
		} else {
			if (!TextUtils.isEmpty(errMsg)) {
				ToastUtils.show(errMsg);
			}
		}
	}

	@Override
	public void onFail(int HttpCode,String errMsg) {

	}

	public void onSelected(boolean isSelected) {
		isThisFragmentSelected = isSelected;
	}
	
}