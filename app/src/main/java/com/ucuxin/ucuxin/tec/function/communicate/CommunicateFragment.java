package com.ucuxin.ucuxin.tec.function.communicate;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.controller.CommunicateController;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.function.communicate.adapter.MessageListAdapter;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.ChatInfo;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.LogUtils;
import com.ucuxin.ucuxin.tec.utils.MyAsyncTask;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * @author parsonswang
 */
public class CommunicateFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener,
		INetWorkListener {
	private ListView mListView;
	private CommunicateController mCommunicateController;
	private MessageListAdapter mAdapter;
	private List<ChatInfo> infos;
	private Activity mActivity;
	private int currentIndex;
	private static boolean isDoInDB;
	private static long reflashTime;
	private WelearnDialogBuilder mWelearnDialogBuilder;

	private static final String TAG = CommunicateFragment.class.getSimpleName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private void showMessageList() {
		new MyAsyncTask() {

			@Override
			public void preTask() {
			}

			@Override
			public void postTask() {
				/*if (MainActivity2.unReadMsgPointIv != null) {
					if (MainActivity2.isShowPoint) {
						MainActivity2.unReadMsgPointIv.setVisibility(View.VISIBLE);
					} else {
						MainActivity2.unReadMsgPointIv.setVisibility(View.INVISIBLE);
					}
				}*/
				if (infos != null && infos.size() > 0) {
					for (int i = 0; i < infos.size(); i++) {
						final ChatInfo chat = infos.get(i);
						boolean successed = queryAndSetUserData(chat);
						if (!successed) {
							currentIndex = i;
							break;
						}

					}
				}
				mAdapter.setData(infos);
			}

			@Override
			public void doInBack() {
				if (!isDoInDB) {
					isDoInDB = true;
					MainActivity.isShowPoint = false;
					infos = WLDBHelper.getInstance().getWeLearnDB().queryMessageList();
					isDoInDB = false;
				}
			}
		}.excute();
	}

	private boolean queryAndSetUserData(ChatInfo chat) {
		final int fromuser = chat.getFromuser();
		final int fromroleid = chat.getFromroleid();
		UserInfoModel user = WLDBHelper.getInstance().getWeLearnDB().queryByUserId(fromuser, true);

		boolean flag = true;
		if (user != null) {
			chat.setUser(user);
			// mAdapter.setData(infos);
			flag = true;
		} else {
			flag = false;

			WeLearnApi.getContactInfo(getActivity(), fromuser, new HttpListener() {
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (code == 0) {
						UserInfoModel user = new Gson().fromJson(dataJson, UserInfoModel.class);
						String name = "";
						String avatar_100 = "";
						if (user == null) {
							user = new UserInfoModel();
							user.setUserid(fromuser);
							user.setRoleid(fromroleid);
							user.setName("未知");
							user.setAvatar_100("");
						} 
						name = user.getName();
						avatar_100 = user.getAvatar_100();
						WLDBHelper.getInstance().getWeLearnDB().insertorUpdate(fromuser, fromroleid, name, avatar_100);

						if (currentIndex < infos.size()) {
							LogUtils.i(TAG, user.toString());
							infos.get(currentIndex).setUser(user);
						}
						setUser();
						mAdapter.setData(infos);
					} else {
						// ToastUtils.show(mActivity, errMsg +"----是我弹的");
					}
				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
				}
			});
		}
		return flag;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mCommunicateController == null) {
			mCommunicateController = new CommunicateController(null, CommunicateFragment.this);
		}

		if (System.currentTimeMillis() - reflashTime >= 1000) {
			showMessageList();
			reflashTime = System.currentTimeMillis();
		}
		MobclickAgent.onEventBegin(mActivity, UmengEventConstant.CUSTOM_EVENT_COMMUNACATE);

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(mActivity, UmengEventConstant.CUSTOM_EVENT_COMMUNACATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_communicate, container, false);
		mListView = (ListView) view.findViewById(R.id.msg_list);

		mAdapter = new MessageListAdapter(mActivity);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Bundle data = new Bundle();
		int userid = infos.get(position).getFromuser();
		data.putInt("userid", userid);
		IntentManager.goToChatListView(mActivity, data, false);
	}

	@Override
	public void onPre() {
	}

	@Override
	public void onException() {
	}

	@Override
	public void onAfter(String jsonStr, int msgDef) {
		// int code = JSONUtils.getInt(jsonStr, "code", -1);
		// String errMsg = JSONUtils.getString(jsonStr, "errmsg", "");
		switch (msgDef) {
		case MsgConstant.MSG_DEF_MSGS:
			// if (System.currentTimeMillis() - reflashTime >= 1000) {
			showMessageList();
			reflashTime = System.currentTimeMillis();
			// }
			break;
		}
	}

	private void setUser() {
		currentIndex++;
		if (currentIndex < infos.size()) {
			ChatInfo chat = infos.get(currentIndex);
			boolean successed = queryAndSetUserData(chat);
			if (successed) {
				setUser();
			}
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mCommunicateController != null) {
			mCommunicateController.removeMsgInQueue();
		}
		WelearnHandler.getInstance().removeMessage(MsgConstant.MSG_DEF_MSGS);
	}

	@Override
	public void onDisConnect() {
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
		final int userid = infos.get(position).getFromuser();
		UserInfoModel user = infos.get(position).getUser();
		String name = "";
		if (user != null) {
			name = user.getName();
		}
		if (name == null) {
			name = "";
		}
		if (null == mWelearnDialogBuilder) {
			mWelearnDialogBuilder = WelearnDialogBuilder.getDialog(getActivity());
		}

		mWelearnDialogBuilder.withMessage(getActivity().getString(R.string.text_del_msg_with_name, name))
				.setOkButtonClick(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							mWelearnDialogBuilder.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}

						WLDBHelper.getInstance().getWeLearnDB().deleteMsg(userid);
						showMessageList();
					}
				});
		mWelearnDialogBuilder.show();
		return true;
	}
}