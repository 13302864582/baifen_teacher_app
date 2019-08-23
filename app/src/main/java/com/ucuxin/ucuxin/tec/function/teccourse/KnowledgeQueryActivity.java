package com.ucuxin.ucuxin.tec.function.teccourse;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.view.KnowledgeGroupView;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.utils.ThreadPoolUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeQueryActivity extends BaseActivity implements TextWatcher, OnItemClickListener,
		OnClickListener, OnKeyListener {
	private static final String REGULAR_EXPRESSION = "、";
	private EditText et_input;
	private KnowledgeGroupView group;
	private ListView lv;
	private KnowledgeAdapter adapter;
	private List<String> data = new ArrayList<String>();
	private int gradeid = -1;
	private int subjectid = -1;
	private String knowledge;

	private List<String> searchResult = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.knowledge_query_activity);
		setWelearnTitle(R.string.knowledge_query);

		findViewById(R.id.back_layout).setOnClickListener(this);
		findViewById(R.id.next_setp_layout).setOnClickListener(this);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.save_text);

		
		et_input = (EditText) findViewById(R.id.knowledge_et_input);
		
		group = (KnowledgeGroupView) findViewById(R.id.knowledge_group);
		lv = (ListView) findViewById(R.id.knowledge_lv);

		adapter = new KnowledgeAdapter();
		lv.setAdapter(adapter);

		et_input.addTextChangedListener(this);
		et_input.setOnKeyListener(this);
		lv.setOnItemClickListener(this);

		Intent intent = getIntent();
		if (intent != null) {
			knowledge = intent.getStringExtra(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME);
			if (!TextUtils.isEmpty(knowledge)) {
				String[] split = knowledge.split(REGULAR_EXPRESSION);
				for (String str : split) {
					addSearchView(str);
				}
			}
			gradeid = intent.getIntExtra("gradeid", -1);
			subjectid = intent.getIntExtra("subjectid", -1);
		}
		
		downLoadKnowledge();
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
			
			String textStr = et_input.getText().toString().trim();
			if (!TextUtils.isEmpty(textStr)) {
				addSearchView(textStr);
				
				// 置空 查询数据
				et_input.setText("");
				data = new ArrayList<String>();
				adapter.notifyDataSetInvalidated();
			}
			return false;
		}
		return false;
	}
	private void downLoadKnowledge() {
		boolean knowledgeExis = WLDBHelper.getInstance().getWeLearnDB().isKnowledgeExis();
		if (knowledgeExis) {

		} else {
			showDialog("正在加载知识点列表，请稍后");
			OkHttpHelper.post(this, "system", "getallkpoint", null, new HttpListener() {

				@Override
				public void onSuccess(int code, final String dataJson, String errMsg) {
					if (code == 0) {
						ThreadPoolUtil.execute(new Runnable() {

							@Override
							public void run() {
								ArrayList<CatalogModel> catalogModels = null;
								try {
									catalogModels = new Gson().fromJson(dataJson,
											new TypeToken<ArrayList<CatalogModel>>() {
											}.getType());
								} catch (Exception e) {
								}
								if (catalogModels != null) {
									WLDBHelper.getInstance().getWeLearnDB().insertKnowledge(catalogModels);
								}
							}
						});
						closeDialog();
					} else {
						closeDialog();
						ToastUtils.show(errMsg);
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {
					closeDialog();
					ToastUtils.show("网络错误：" + HttpCode);
				}
			});
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (null != s) {
			String strText = s.toString();
			if (!TextUtils.isEmpty(strText)) {
				searchKnowledge(strText);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String textStr = data.get(position);

		addSearchView(textStr);

		// 置空 查询数据
		et_input.setText("");
		data = new ArrayList<String>();
		adapter.notifyDataSetInvalidated();
	}

	/**
	 * 添加搜索结果
	 * 
	 * @param textStr 搜索结果
	 */
	private void addSearchView(String textStr) {
		View group_item = View.inflate(getApplication(), R.layout.knowledge_group_item, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, DisplayUtils.dip2px(getApplication(), 42));
		params.topMargin = DisplayUtils.dip2px(getApplication(), 4);
		params.leftMargin = DisplayUtils.dip2px(getApplication(), 6);
		group_item.setLayoutParams(params);

		TextView tv = (TextView) group_item.findViewById(R.id.knowledge_group_item_tv);
		tv.setText(textStr);

		ImageView iv = (ImageView) group_item.findViewById(R.id.knowledge_group_item_iv);
		iv.setTag(group_item);
		iv.setOnClickListener(this);

		// 存储搜索结果
		group_item.setTag(textStr);
		searchResult.add(textStr);

		group.addView(group_item);
	}

	/**
	 * 获取选择结果
	 * @return 选择结果
	 */
	private String getSearchResult() {
		StringBuilder sb = new StringBuilder();
		for (String result : searchResult) {
			sb.append(result).append(REGULAR_EXPRESSION);
		}
		String kpoint = et_input.getText().toString().trim();
		String searchResult = "";
		if (sb.length() > 0 ) {
			if (kpoint.length() > 0) {
				searchResult = sb.toString() + kpoint;
			}else {
				searchResult = sb.substring(0, sb.length() - 1);
			}
		}else if (kpoint.length() > 0){
			searchResult = kpoint;
		}
		return searchResult;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.knowledge_group_item_iv:
			// 从ViewGroup中删除自己
			View parentView = (View) v.getTag();
			String searchText = (String) parentView.getTag();
			searchResult.remove(searchText);
			group.removeView(parentView);
			break;
		case R.id.next_setp_layout:// 保存
			Intent intent = new Intent();
			String kpoint = getSearchResult();
			intent.putExtra(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME, kpoint);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.back_layout:
			finish();
			break;
		}
	}

	private class KnowledgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView item_tv;
			if (null == convertView) {
				convertView = View.inflate(getApplication(), R.layout.knowledge_lv_item, null);
				item_tv = (TextView) convertView.findViewById(R.id.item_tv);
				convertView.setTag(item_tv);
			} else {
				item_tv = (TextView) convertView.getTag();
			}
			item_tv.setText(data.get(position));
			return convertView;
		}
	}

	private void searchKnowledge(String key) {
		// 查询, 刷新ListView
		if (gradeid == -1 || subjectid == -1) {
			data = WLDBHelper.getInstance().getWeLearnDB().queryKnowledgeByName(key);
		} else {
			String groupid = "0";
			switch (gradeid) {
			case 1:
			case 2:
			case 3:
				groupid = "1";
				break;
			case 4:
			case 5:
			case 6:
				groupid = "2";
				break;
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				groupid = "3";
				break;
			default:
				break;
			}
			data = WLDBHelper.getInstance().getWeLearnDB().queryKnowledgeByName(key, groupid, subjectid + "");
		}

		adapter.notifyDataSetInvalidated();
	}
}
