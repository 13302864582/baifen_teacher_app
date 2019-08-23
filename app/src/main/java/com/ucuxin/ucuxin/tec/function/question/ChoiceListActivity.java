package com.ucuxin.ucuxin.tec.function.question;

import java.util.ArrayList;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Chapter;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Point;
import com.ucuxin.ucuxin.tec.function.homework.model.CatalogModel.Subject;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChoiceListActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	private ListView listView;
	private int type;

	@SuppressWarnings("unchecked")
	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.choice_list_activity);
		findViewById(R.id.back_layout).setOnClickListener(this);
		listView = (ListView) findViewById(R.id.list_lv_choice);
		ChoiceListAdapter adapter;

		ArrayList<Subject> subjects = null;
		ArrayList<Chapter> chapters = null;
		ArrayList<Point> points = null;
		Intent intent = getIntent();
		if (intent != null) {
			type = intent.getIntExtra(FiterKnowledgeActivity.CHOICE_TYPE, 0);
			adapter = new ChoiceListAdapter();
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
	}

	class ChoiceListAdapter extends BaseAdapter {
		private ArrayList<CatalogModel> catalogModels;
		private int size;

		public ChoiceListAdapter() {

			switch (type) {
			case 1:
				catalogModels = new ArrayList<CatalogModel>();
				catalogModels.add(new CatalogModel(1, getString(R.string.grade_text1)));
				catalogModels.add(new CatalogModel(2, getString(R.string.grade_text2)));
				catalogModels.add(new CatalogModel(3, getString(R.string.grade_text3)));
				catalogModels.add(new CatalogModel(4, getString(R.string.grade_text4)));
				catalogModels.add(new CatalogModel(5, getString(R.string.grade_text5)));
				catalogModels.add(new CatalogModel(6, getString(R.string.grade_text6)));

				size = 6;
				break;
			case 2:
				size = FiterKnowledgeActivity.subjects.size();
				break;
			case 3:
				size = FiterKnowledgeActivity.chapters.size();
				break;
			case 4:
				size = FiterKnowledgeActivity.points.size();
				break;

			default:
				break;
			}

		}

		@Override
		public int getCount() {
			return size + 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			TextView view = null;
			if (convertView == null) {
				view = (TextView) View.inflate(ChoiceListActivity.this, R.layout.fiter_choice_know_item, null);
			} else {
				view = (TextView) convertView;
			}
			String name = "";
			int id = 0;
			if (position == 0) {

				name = "全部";
			} else if (position > 0) {
				position--;
				switch (type) {
				case 1:
					name = catalogModels.get(position).getGroupname();
					id = catalogModels.get(position).getGroupid();
					break;
				case 2:
					name = FiterKnowledgeActivity.subjects.get(position).getSubjectname();
					id = FiterKnowledgeActivity.subjects.get(position).getSubjectid();
					break;
				case 3:
					name = FiterKnowledgeActivity.chapters.get(position).getChaptername();
					id = FiterKnowledgeActivity.chapters.get(position).getChapterid();
					break;
				case 4:
					name = FiterKnowledgeActivity.points.get(position).getName();
					id = FiterKnowledgeActivity.points.get(position).getId();
					break;
				default:
					break;
				}
			}
			view.setText(name);
			view.setTag(id);
			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Integer id = (Integer) view.getTag();
		int gradeGroupId = SharePerfenceUtil.getInstance().getGradeGroupId();
		int subjectGroupId = SharePerfenceUtil.getInstance().getSubjectGroupId();
		int chapterGroupId = SharePerfenceUtil.getInstance().getChapterGroupId();
		int knowPointGroupId = SharePerfenceUtil.getInstance().getKnowPointGroupId();
		boolean flag = true;
		switch (type) {
		case 1:
			SharePerfenceUtil.getInstance().setGradeGroupId(id);
			if (id != gradeGroupId) {
				SharePerfenceUtil.getInstance().setSubjectGroupId(0);
				SharePerfenceUtil.getInstance().setChapterGroupId(0);
				SharePerfenceUtil.getInstance().setKnowPointGroupId(0);
			}
			break;
		case 2:
			SharePerfenceUtil.getInstance().setSubjectGroupId(id);
			if (id != subjectGroupId) {
				SharePerfenceUtil.getInstance().setChapterGroupId(0);
				SharePerfenceUtil.getInstance().setKnowPointGroupId(0);
			}
			break;
		case 3:
			if (id != chapterGroupId) {
				SharePerfenceUtil.getInstance().setKnowPointGroupId(0);
			}
			SharePerfenceUtil.getInstance().setChapterGroupId(id);
			break;
		case 4:
			if (id == knowPointGroupId) {
				flag = false;
			}
			SharePerfenceUtil.getInstance().setKnowPointGroupId(id);
			break;

		default:
			break;
		}
		if (flag) {
			setResult(RESULT_OK);
		}
		finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_layout:
			finish();
			break;
		}
	}
}
