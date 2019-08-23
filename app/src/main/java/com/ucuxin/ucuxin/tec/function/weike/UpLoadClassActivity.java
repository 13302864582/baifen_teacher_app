package com.ucuxin.ucuxin.tec.function.weike;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.homework.SelectPicPopupWindow;
import com.ucuxin.ucuxin.tec.function.homework.teacher.TecHomeWorkSingleCheckActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerImageGridActivity;
import com.ucuxin.ucuxin.tec.function.teccourse.adapter.UpLoadClassAdapter;
import com.ucuxin.ucuxin.tec.function.teccourse.adapter.UpLoadClassAdapter.OnImageDeleteClickListener;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CharpterModel;
import com.ucuxin.ucuxin.tec.function.teccourse.model.CoursePageModel;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper;
import com.ucuxin.ucuxin.tec.http.OkHttpHelper.HttpListener;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;
import com.ucuxin.ucuxin.tec.view.dialog.WelearnDialogBuilder;
import com.ucuxin.ucuxin.tec.view.gridview.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpLoadClassActivity extends BaseActivity
		implements OnClickListener, OnItemClickListener, OnImageDeleteClickListener {
	protected static final int MAX_IMAGE_SIZE = 8;
	private static final int REQUEST_CODE_GET_IMAGE_FROM_SYS = 0x1;
	// private static final int REQUEST_CODE_GET_IMAGE_FROM_CROP = 0x2;
	private EditText nameEt;
	private WelearnDialogBuilder mDialog;
	private TextView knowledgeTv;
	private MyGridView pageGridView;
	private Button deleteBtn;
	private UpLoadClassAdapter mAdapter;
	private List<CoursePageModel> mPageList = new ArrayList<CoursePageModel>();
	private CoursePageModel addPageModel;
	private int charpterid;
	private int courseid;
	private int gradeid = -1;
	private int subjectid = -1;
	private View tipsContainer;

	@Override
	@SuppressLint("InlinedApi")
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.upload_class_activity);
		setWelearnTitle(R.string.upload_class_text);

		findViewById(R.id.back_layout).setOnClickListener(this);
		findViewById(R.id.next_setp_layout).setOnClickListener(this);
		TextView nextStepTV = (TextView) findViewById(R.id.next_step_btn);
		nextStepTV.setBackgroundResource(R.drawable.publish_btn_selector);
		nextStepTV.setVisibility(View.VISIBLE);
		nextStepTV.setText(R.string.save_text);

		nameEt = (EditText) findViewById(R.id.classname_et_uploadclass);
		knowledgeTv = (TextView) findViewById(R.id.knowledge_tv_uploadclass);
		knowledgeTv.setOnClickListener(this);

		pageGridView = (MyGridView) findViewById(R.id.page_gridview_uploadclass);
		addPageModel = getAddModel();
		mPageList.add(addPageModel);
		mAdapter = new UpLoadClassAdapter(this, mPageList, this);
		pageGridView.setAdapter(mAdapter);
		pageGridView.setOnItemClickListener(this);

		deleteBtn = (Button) findViewById(R.id.delete_class_btn_uploadclass);
		deleteBtn.setOnClickListener(this);

		tipsContainer = findViewById(R.id.tips_container_uploadclass);

		Intent intent = getIntent();
		if (intent != null) {
			charpterid = intent.getIntExtra("charpterid", 0);
			courseid = intent.getIntExtra("courseid", 0);
			gradeid = intent.getIntExtra("gradeid", -1);
			subjectid = intent.getIntExtra("subjectid", -1);
			if (charpterid != 0) {
				tipsContainer.setVisibility(View.GONE);
				deleteBtn.setVisibility(View.VISIBLE);
				setWelearnTitle(R.string.edit_class_text);
			}
			String charptername = intent.getStringExtra("charptername");
			String kpoint = intent.getStringExtra("kpoint");
			if (!TextUtils.isEmpty(charptername)) {
				nameEt.setText(charptername);
			}
			if (!TextUtils.isEmpty(kpoint)) {
				knowledgeTv.setText(kpoint);
			}
			loadData(true);
		}
	}

	private void loadData(final boolean isRefreshName) {
		if (charpterid != 0) {
			JSONObject data = new JSONObject();
			try {
				data.put("charpterid", charpterid);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			OkHttpHelper.post(this, "course", "charpterdetail", data, new HttpListener() {

				@Override
				public void onSuccess(int code, String dataJson, String errMsg) {
					if (!TextUtils.isEmpty(dataJson)) {
						CharpterModel classHourModel = null;
						ArrayList<CoursePageModel> pagelist = null;
						try {
							classHourModel = new Gson().fromJson(dataJson, CharpterModel.class);
						} catch (Exception e) {
						}
						if (classHourModel != null) {
							if (isRefreshName) {
								String charptername = classHourModel.getCharptername();
								String kpoint = classHourModel.getKpoint();
								if (!TextUtils.isEmpty(charptername)) {
									nameEt.setText(charptername);
								}
								if (!TextUtils.isEmpty(kpoint)) {
									knowledgeTv.setText(kpoint);
								}
							}
							pagelist = classHourModel.getPage();
							if (pagelist != null) {
								int size = pagelist.size();
								if (size < MAX_IMAGE_SIZE) {
									pagelist.add(addPageModel);
								}
								mPageList = pagelist;
								mAdapter.setData(mPageList);
							}
						}
					}

				}

				@Override
				public void onFail(int HttpCode,String errMsg) {

				}
			});
		}
	}

	private CoursePageModel getAddModel() {
		CoursePageModel newModel = new CoursePageModel();
		newModel.setThumbnail(UpLoadClassAdapter.ADD_IMAGE_TAG);
		return newModel;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.next_setp_layout:// 保存
			updateCharpter();
			break;
		case R.id.back_layout:
			finish();
			break;
		case R.id.knowledge_tv_uploadclass:
			String kpoint = knowledgeTv.getText().toString().trim();
			if (kpoint.equals(getString(R.string.upload_course_hint_text2))) {
				kpoint = "";
			}
			IntentManager.goToKnowledgeQueryActivity(this, kpoint, gradeid, subjectid);
			break;
		case R.id.delete_class_btn_uploadclass:
			deleteClass();
			break;

		default:
			break;
		}
	}

	private void deleteClass() {
		if (null == mDialog) {
			mDialog = WelearnDialogBuilder.getDialog(this);
		}
		mDialog.withMessage(R.string.delete_chapter_confirm_text).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (charpterid != 0) {

					JSONObject data = new JSONObject();
					try {
						data.put("charpterid", charpterid);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					OkHttpHelper.post(UpLoadClassActivity.this, "course", "delcharpter", data, new HttpListener() {

						@Override
						public void onSuccess(int code, String dataJson, String errMsg) {
							setResult(RESULT_OK);
							finish();

						}

						@Override
						public void onFail(int HttpCode,String errMsg) {

						}
					});
				}
			}
		});
		mDialog.show();

	}

	private void updateCharpter() {
		JSONObject data = new JSONObject();
		String charptername = nameEt.getText().toString().trim();
		String kpoint = knowledgeTv.getText().toString().trim();
		if (kpoint.equals(getString(R.string.upload_course_hint_text2))) {
			kpoint = "";
		}
		if (TextUtils.isEmpty(charptername) || TextUtils.isEmpty(kpoint)) {
			ToastUtils.show("请先输入课时名称及知识点");
			return;
		}
		if (charpterid == 0) {
			ToastUtils.show("请先添加讲义");
			return;
		}
		try {
			data.put("charpterid", charpterid);
			data.put("charptername", charptername);
			data.put("kpoint", kpoint);
			JSONArray page = new JSONArray(new Gson().toJson(mPageList));
			data.put("page", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		OkHttpHelper.post(this, "course", "updatecharpter", data, new HttpListener() {

			@Override
			public void onSuccess(int code, String dataJson, String errMsg) {
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFail(int HttpCode,String errMsg) {

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1002:
				loadData(false);
				break;
			case 1003:// 知识点
				if (data != null) {
					String kpoint = data.getStringExtra(TecHomeWorkSingleCheckActivity.KNOWLEDGE_NAME);
					if (TextUtils.isEmpty(kpoint)) {
						kpoint = getString(R.string.upload_course_hint_text2);
					}
					knowledgeTv.setText(kpoint);
				}
				break;
			case REQUEST_CODE_GET_IMAGE_FROM_SYS://
				String path = data.getStringExtra("path");
				boolean isFromPhotoList = data.getBooleanExtra("isFromPhotoList", false);
				// path =
				// TecHomeWorkCheckDetailActivity.autoFixOrientation(path,
				// isFromPhotoList , this , null);
				// if (!TextUtils.isEmpty(path)) {
				// IntentManager.goToAddHandoutActivity(this, charpterid, path);
				// }
				IntentManager.goToCropImageActivity(this, path, isFromPhotoList);
				break;
			case 1202:
				path = data.getStringExtra(PayAnswerImageGridActivity.IMAGE_PATH);
				if (!TextUtils.isEmpty(path)) {
					IntentManager.goToAddHandoutActivity(this, charpterid, path);
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onDeleteClick(final int pos) {
		if (null == mDialog) {
			mDialog = WelearnDialogBuilder.getDialog(this);
		}
		mDialog.withMessage(R.string.delete_course_page_confirm_text).setOkButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					mDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				int size = mPageList.size();
				if (pos >= 0 && pos < size) {
					mPageList.remove(pos);
					size--;
				}

				if (size < MAX_IMAGE_SIZE) {
					CoursePageModel pageModel = mPageList.get(size - 1);
					if (!UpLoadClassAdapter.ADD_IMAGE_TAG.equals(pageModel.getThumbnail())) {
						mPageList.add(addPageModel);
					}
				}
				JSONObject data = new JSONObject();
				String charptername = nameEt.getText().toString().trim();
				String kpoint = knowledgeTv.getText().toString().trim();
				if (kpoint.equals(getString(R.string.upload_course_hint_text2))) {
					kpoint = "";
				}
				try {
					data.put("charpterid", charpterid);
					data.put("charptername", charptername);
					data.put("kpoint", kpoint);
					JSONArray page = new JSONArray(new Gson().toJson(mPageList));
					data.put("page", page);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				OkHttpHelper.post(UpLoadClassActivity.this, "course", "updatecharpter", data, new HttpListener() {

					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						mAdapter.setData(mPageList);
					}

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});

			}
		});
		mDialog.show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		CoursePageModel pageModel = mPageList.get(pos);
		String path = pageModel.getThumbnail();
		if (UpLoadClassAdapter.ADD_IMAGE_TAG.equals(path)) {
			if (charpterid == 0) {
				JSONObject data = new JSONObject();
				String charptername = nameEt.getText().toString().trim();
				String kpoint = knowledgeTv.getText().toString().trim();
				if (kpoint.equals(getString(R.string.upload_course_hint_text2))) {
					kpoint = "";
				}
				if (TextUtils.isEmpty(charptername) || TextUtils.isEmpty(kpoint)) {
					ToastUtils.show("请先输入课时名称及知识点");
					return;
				}
				try {
					data.put("courseid", courseid);
					data.put("charptername", charptername);
					data.put("kpoint", kpoint);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				OkHttpHelper.post(this, "course", "addcharpter", data, new HttpListener() {
					@Override
					public void onSuccess(int code, String dataJson, String errMsg) {
						charpterid = JsonUtils.getInt(dataJson, "charpterid", 0);
						if (charpterid != 0) {
							// deleteBtn.setVisibility(View.VISIBLE);
							// setWelearnTitle(R.string.edit_class_text);
							startActivityForResult(new Intent(UpLoadClassActivity.this, SelectPicPopupWindow.class),
									REQUEST_CODE_GET_IMAGE_FROM_SYS);
						}

					}

					@Override
					public void onFail(int HttpCode,String errMsg) {

					}
				});
			} else {
				startActivityForResult(new Intent(this, SelectPicPopupWindow.class), REQUEST_CODE_GET_IMAGE_FROM_SYS);
			}
		} else {
			int pageid = pageModel.getPageid();
			String imgurl = pageModel.getImgurl();
			IntentManager.goToAddHandoutActivity(this, imgurl, pageid);
		}

	}

}
