package com.ucuxin.ucuxin.tec.function.weike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.function.teccourse.model.PurchaseStudentModel;
import com.ucuxin.ucuxin.tec.function.teccourse.model.PurchaseStudentModel.Charpter;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PurchaseStudentActivity extends BaseActivity implements OnClickListener {
	private ListView studentListLv;
	private ArrayList<PurchaseStudentModel> mstudentModels;
	private PurchaseStudentAdapter mAdapter;
	private int courseid;
	private int avatarSize;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.purchase_student_activity);
		setWelearnTitle(R.string.purchase_student_title_text);
		findViewById(R.id.back_layout).setOnClickListener(this);
		avatarSize = getResources().getDimensionPixelSize(R.dimen.purchase_student_list_avatar_size);

		studentListLv = (ListView) findViewById(R.id.student_list_lv_purchase);
		mAdapter = new PurchaseStudentAdapter();
		studentListLv.setAdapter(mAdapter);
		Intent intent = getIntent();
		if (intent != null) {
			courseid = intent.getIntExtra("courseid", 0);
		}
		loadData();
	}

	private void loadData() {
		if (courseid != 0) {

			JSONObject data = new JSONObject();
			try {
				data.put("courseid", courseid);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			OkHttpHelper.post(this, "course","studentofcourse", data, new HttpListener() {				
				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (!TextUtils.isEmpty(dataJson)) {
						try {
							mstudentModels = new Gson().fromJson(dataJson,
									new TypeToken<ArrayList<PurchaseStudentModel>>() {
									}.getType());
						} catch (Exception e) {
						}
						if (mstudentModels != null && !mstudentModels.isEmpty()) {
							mAdapter.notifyDataSetChanged();
					
						}
					}				
					
				}
				
				@Override
				public void onFail(int HttpCode,String errMsg) {
					
					
				}
			});
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			goBack();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		goBack();
	}
	private void goBack(){
		setResult(RESULT_OK);
		finish();
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	class PurchaseStudentAdapter extends BaseAdapter implements OnClickListener {
		PurchaseHolder visibleHolder;
		private int visiblePosition = -1;
		@Override
		public int getCount() {
			return mstudentModels == null ? 0 : mstudentModels.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PurchaseHolder holder;
			if (convertView == null) {
				holder = new PurchaseHolder();
				convertView = View.inflate(PurchaseStudentActivity.this, R.layout.purchase_student_list_item, null);
				convertView.setTag(holder);

				holder.avatarIv = (NetworkImageView) convertView.findViewById(R.id.avatar_iv_purchase_item);
				holder.avatarIv.setOnClickListener(this);
				holder.studentNameTv = (TextView) convertView.findViewById(R.id.name_tv_purchase_item);
				holder.sexTv = (TextView) convertView.findViewById(R.id.sex_tv_purchase_item);
				holder.saleTimeTv = (TextView) convertView.findViewById(R.id.sale_time_tv_purchase_item);
				holder.askTimeTv = (TextView) convertView.findViewById(R.id.ask_time_tv_purchase_item);

				holder.remindView = convertView.findViewById(R.id.ask_container_purchase_item);
				holder.catalogIcon = convertView.findViewById(R.id.catalog_icon_purchase_item);
				holder.catalogContainer = convertView.findViewById(R.id.catalog_container_purchase_item);
				holder.bodyContainer = convertView.findViewById(R.id.body_container_purchase_item);
				holder.bodyContainer.setOnClickListener(this);
				holder.bodyContainer.setTag(holder);
				
				holder.catalogLv = (ListView) convertView.findViewById(R.id.catalog_lv_purchase_item);
				CatalogListAdapter itemAdapter = new CatalogListAdapter();
				holder.catalogLv.setAdapter(itemAdapter);
				holder.catalogLv.setOnItemClickListener(itemAdapter);
			} else {
				holder = (PurchaseHolder) convertView.getTag();
			}
			holder.position = position;
			PurchaseStudentModel model = mstudentModels.get(position);
			if (model != null) {
				holder.model = model;
				String avatar = model.getAvatar();
				String name = model.getName();
				int sex = model.getSex();

				long datatime = model.getDatatime();
				long lasttime = model.getLasttime();
				float price = model.getPrice();
				int process = model.getProcess();
				int todo = model.getTodo();
				int userid = model.getUserid();
				holder.avatarIv.setTag(R.id.tag_first, userid);
				SimpleDateFormat format = new SimpleDateFormat("y.M.d");
				String saleTime = format.format(new Date(datatime));

				ImageLoader.getInstance().loadImage(avatar, holder.avatarIv, R.drawable.ic_default_avatar, avatarSize,
						avatarSize / 10);
				holder.studentNameTv.setText(name + "");
				int sexResId = R.string.sextype_unknown;
				switch (sex) {
				case GlobalContant.SEX_TYPE_MAN:
					sexResId = R.string.sextype_man;
					break;
				case GlobalContant.SEX_TYPE_WOMEN:
					sexResId = R.string.sextype_women;
					break;
				}
				holder.sexTv.setText(sexResId);
				holder.saleTimeTv.setText(saleTime);

				String askTime = format.format(new Date(lasttime));
				holder.askTimeTv.setText(askTime + "");
				CatalogListAdapter catalogAdapter = null;
				ListAdapter adapter = holder.catalogLv.getAdapter();
				if (adapter != null && adapter instanceof CatalogListAdapter) {
					catalogAdapter = (CatalogListAdapter) adapter;
				}
				if (todo == 1) {// 有追问
//					setCatalogGone(holder);
					holder.bodyContainer.setClickable(true);
					catalogAdapter.setData(model);
					if (visiblePosition == position) {
						setCatalogVisible(holder);
					}else {
						setCatalogGone(holder);
					}
				} else {
					catalogAdapter.setData(null);
					holder.bodyContainer.setClickable(false);
					holder.remindView.setVisibility(View.GONE);
					holder.catalogContainer.setVisibility(View.GONE);
					holder.catalogIcon.setVisibility(View.GONE);
				}
				setListViewHeightBasedOnChildren(holder.catalogLv);
		
				
			}
			return convertView;
		}

		void setCatalogVisible(PurchaseHolder holder) {
			holder.catalogContainer.setVisibility(View.VISIBLE);
			holder.catalogIcon.setVisibility(View.VISIBLE);
			holder.remindView.setVisibility(View.GONE);	

			visibleHolder = holder;
			visiblePosition = holder.position;
		}

		void setCatalogGone(PurchaseHolder holder) {
			holder.catalogContainer.setVisibility(View.GONE);
			holder.catalogIcon.setVisibility(View.GONE);
			holder.remindView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.body_container_purchase_item:
				PurchaseHolder holder = (PurchaseHolder) view.getTag();
				if (visiblePosition == -1) {
					setCatalogVisible(holder);
				}else  {
					setCatalogGone(visibleHolder);
					if (visiblePosition == holder.position){
						visiblePosition = -1;
						visibleHolder = null;
					}else {
						setCatalogVisible(holder);
					}
				}
//				PurchaseHolder holder = (PurchaseHolder) view.getTag();
//				if (visibleHolder == null) {
//					setCatalogVisible(holder);
//				} else {
//					setCatalogGone(visibleHolder);
//					if (visibleHolder == holder) {
//						visibleHolder = null;
//					} else {
//						setCatalogVisible(holder);
//					}
//				}
				break;
			case R.id.avatar_iv_purchase_item:
				Integer userid = (Integer) view.getTag(R.id.tag_first);
				if (userid != 0) {
					IntentManager.gotoPersonalPage(PurchaseStudentActivity.this, userid, GlobalContant.ROLE_ID_STUDENT);
				}
				break;


			default:
				break;
			}
		}
	}

	class CatalogListAdapter extends BaseAdapter implements OnItemClickListener {
		PurchaseStudentModel model;
		ArrayList<Charpter> charpters;

		public void setData (PurchaseStudentModel model){
			this.model = model;
			if (model == null) {
				this.charpters = null;
			} else {
				this.charpters = model.getCharpter();
			}
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			int count = 0;
			if (charpters != null) {
				count = charpters.size();
			}
			return count;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(PurchaseStudentActivity.this, R.layout.course_ask_item, null);
			}
			TextView chapterIndexTv = (TextView) convertView.findViewById(R.id.chapter_index_askitem);
			TextView pageIndexTv = (TextView) convertView.findViewById(R.id.page_index_askitem);
			if (charpters != null) {
				Charpter charpter = charpters.get(position);
				int charpterindex = charpter.getCharpterindex();
				int pageindex = charpter.getPageindex();
				chapterIndexTv.setText("" + charpterindex);
				pageIndexTv.setText("" + pageindex);
			}
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (model != null && charpters != null) {
				Charpter charpter = charpters.get(position);
				if (charpter != null) {
					int pageid = charpter.getPageid();
					String imgurl = charpter.getImgurl();
					String charptername = charpter.getCharptername();
					int studentid = model.getUserid();
					String avatar = model.getAvatar();
					String name = model.getName();
					IntentManager.goToSingleStudentQAActivity(PurchaseStudentActivity.this, pageid, imgurl,
							charptername, studentid, avatar, name);
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				loadData();
				
				break;
			case 1003:
				
				break;

			default:
				break;
			}
		}
	}

	static class PurchaseHolder {
		int position;
		ListView catalogLv;
		View catalogContainer;
		View bodyContainer;
		TextView studentNameTv;
		TextView saleTimeTv;
		TextView askTimeTv;
		TextView sexTv;
		NetworkImageView avatarIv;
		View remindView;
		View catalogIcon;
		PurchaseStudentModel model;
	}
}
