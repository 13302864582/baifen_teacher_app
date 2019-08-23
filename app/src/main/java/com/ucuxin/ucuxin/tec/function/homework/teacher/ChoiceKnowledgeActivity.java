package com.ucuxin.ucuxin.tec.function.homework.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.utils.ThreadPoolUtil;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ChoiceKnowledgeActivity extends BaseActivity implements OnItemClickListener, OnClickListener,
		TextWatcher, HttpListener {

	private EditText choice_knowledge_edittext;
	private TextView search_result_text;
	private ListView choice_knowledge_listview;
	private ImageView clearIV;
	private List<String> list;
	private SearchAdapter searchAdapter;
	// private boolean isSearch;
	private String knowledgeName = "";
	private RelativeLayout nextStepLayout;
	private int gradeid = -1;
	private int subjectid = -1;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_knowledge_activity);

		setWelearnTitle(R.string.choice_knowledge_text);

		findViewById(R.id.back_layout).setOnClickListener(this);
		nextStepLayout = (RelativeLayout) findViewById(R.id.next_setp_layout);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.text_done);
		nextStepLayout.setOnClickListener(this);

		clearIV = (ImageView) findViewById(R.id.choice_knowledge_delete_bt);
		clearIV.setOnClickListener(this);

		choice_knowledge_edittext = (EditText) findViewById(R.id.choice_knowledge_edittext);
		Intent intent = getIntent();
		knowledgeName = intent.getStringExtra(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME);
		if (!TextUtils.isEmpty(knowledgeName)) {
			choice_knowledge_edittext.setText(knowledgeName);
		}
		gradeid = intent.getIntExtra("gradeid", -1);
		subjectid = intent.getIntExtra("subjectid", -1);
		// getGreamSchool();
		choice_knowledge_edittext.addTextChangedListener(this);
		search_result_text = (TextView) findViewById(R.id.search_result_text);
		choice_knowledge_listview = (ListView) findViewById(R.id.choice_knowledge_listview);
		search_result_text.setVisibility(View.INVISIBLE);
		choice_knowledge_listview.setVisibility(View.INVISIBLE);
		searchAdapter = new SearchAdapter(list);
		choice_knowledge_listview.setAdapter(searchAdapter);
		choice_knowledge_listview.setOnItemClickListener(this);

		boolean knowledgeExis = WLDBHelper.getInstance().getWeLearnDB().isKnowledgeExis();
		if (knowledgeExis) {

		} else {
			showDialog("请稍后");
			OkHttpHelper.post(this, "system", "getallkpoint", null, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		knowledgeName = list.get(position);
		choice_knowledge_edittext.setText(knowledgeName);
		search_result_text.setVisibility(View.GONE);
		choice_knowledge_listview.setVisibility(View.GONE);
		clearIV.setVisibility(View.GONE);
	}

	private class SearchAdapter extends BaseAdapter {
		private List<String> mList;

		public void setmList(List<String> mList) {
			this.mList = mList;
			if (this.mList == null) {
				this.mList = new ArrayList<String>();
			}
			notifyDataSetChanged();
		}

		public SearchAdapter(List<String> mList) {
			this.mList = mList;
			if (this.mList == null) {
				this.mList = new ArrayList<String>();
			}
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView search_school_item_textview;
			if (convertView == null) {
				convertView = View.inflate(ChoiceKnowledgeActivity.this, R.layout.search_school_list_item, null);
				search_school_item_textview = (TextView) convertView.findViewById(R.id.search_school_item_textview);
				convertView.setTag(search_school_item_textview);
			} else {
				search_school_item_textview = (TextView) convertView.getTag();
			}

			/*
			 * if (position == mList.size()) {
			 * search_school_item_textview.setText("其他学校"); } else {
			 */
			search_school_item_textview.setText(mList.get(position));
			// }
			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_layout:
			finish();
			break;
		case R.id.next_setp_layout:
			knowledgeName = choice_knowledge_edittext.getText().toString().trim();
			if (TextUtils.isEmpty(knowledgeName)) {
				ToastUtils.show("请输入该错题所属的知识点");
				return;
			}
			Intent data = new Intent();
			data.putExtra(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME, knowledgeName);
			setResult(RESULT_OK, data);
			finish();
			break;
		case R.id.search_school_delete_bt:
			choice_knowledge_edittext.setText("");
			break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		textChange(s);
	}

	@Override
	public void afterTextChanged(Editable s) {
		textChange(s);
	}

	private void textChange(CharSequence s) {
		if (s.toString().length() > 0) {
			search_result_text.setVisibility(View.VISIBLE);
			choice_knowledge_listview.setVisibility(View.VISIBLE);
			clearIV.setVisibility(View.VISIBLE);
			if (gradeid == -1 || subjectid == -1) {
				list = WLDBHelper.getInstance().getWeLearnDB().queryKnowledgeByName(s.toString());
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
				list = WLDBHelper.getInstance().getWeLearnDB()
						.queryKnowledgeByName(s.toString(), groupid, subjectid + "");
			}
			searchAdapter.setmList(list);
		} else {
			search_result_text.setVisibility(View.GONE);
			choice_knowledge_listview.setVisibility(View.GONE);
			clearIV.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSuccess(int code, final String dataJson, String errMsg) {
		if (code == 0) {
			ThreadPoolUtil.execute(new Runnable() {

				@Override
				public void run() {
					ArrayList<CatalogModel> catalogModels = null;
					try {
						catalogModels = new Gson().fromJson(dataJson, new TypeToken<ArrayList<CatalogModel>>() {
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
}
