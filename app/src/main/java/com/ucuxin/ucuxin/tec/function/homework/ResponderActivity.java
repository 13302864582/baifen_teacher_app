
package com.ucuxin.ucuxin.tec.function.homework;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;*/
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.function.question.PayAnswerFragment;
import com.ucuxin.ucuxin.tec.utils.ToastUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 抢答activity
 * 
 * @author: sky
 */
public class ResponderActivity extends BaseActivity implements OnCheckedChangeListener {

	@Override
	public void report(int reasonid,String reason,String tipTxt) {
		homeworkCheckFragment.report(reasonid,reason,tipTxt);

	}

	private FragmentManager fm;

	private RadioGroup radioGroup;

	private RadioButton radio_question;

	private RadioButton radio_homework;

	private RelativeLayout back_layout;

	private PayAnswerFragment questionFragment;

	private HomeworkCheckFragment homeworkCheckFragment;
	private String curFragment = "1";
	private int index = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.responder_activity);
		initView();
		initListener();
		fm = getSupportFragmentManager();
		//onCheckedChanged(radioGroup, radio_question.getId());
		onCheckedChanged(radioGroup, radio_homework.getId());

	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String goTag = intent.getStringExtra("go_tag");
		if ("finish_homework".equals(goTag)) {
			onCheckedChanged(radioGroup, radio_homework.getId());
		}
	}

	@Override
	public void initView() {
		super.initView();
		back_layout = (RelativeLayout) findViewById(R.id.back_layout);
		this.radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		radio_question = (RadioButton) findViewById(R.id.radio_question);
		radio_homework = (RadioButton) findViewById(R.id.radio_homework);
		setWelearnTitle(R.string.title_qingda);

	}

	@Override
	public void initListener() {
		super.initListener();
		radioGroup.setOnCheckedChangeListener(this);
		back_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:// 返回
			finish();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		index = radio_question.getId() == checkedId ? 0 : 1;
		setTabSelection(index);
	}

	public void setTabSelection(int index) {
		// 每次先清除上次的选中状态
		clearSelection();

		switch (index) {
		case 0:// 问题tab
				// 当点击了消息tab时，改变控件的图片和文字颜色

			setWelearnTitle(R.string.text_ask_title);
			radio_question.setBackgroundResource(R.drawable.bline);
			radio_homework.setBackgroundColor(Color.parseColor("#FFFFFF"));
			radio_question.setTextColor(getResources().getColor(R.color.responder_tab_text_checked));
			radio_homework.setTextColor(getResources().getColor(R.color.responder_tab_text_normal));
			if (questionFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				questionFragment = new PayAnswerFragment();
			}
			switchContent("hw", questionFragment, "qu");

			break;

		case 1:// 作业tab
		default:
			// 当点击了作业tab时，改变控件的图片和文字颜色
			setWelearnTitle(R.string.homework_checks_title_text);
			radio_homework.setBackgroundResource(R.drawable.bline);
			radio_question.setBackgroundColor(Color.parseColor("#FFFFFF"));
			radio_homework.setTextColor(getResources().getColor(R.color.responder_tab_text_checked));
			radio_question.setTextColor(getResources().getColor(R.color.responder_tab_text_normal));
			if (homeworkCheckFragment == null) {
				// 如果ContactsFragment为空，则创建一个并添加到界面上
				homeworkCheckFragment = new HomeworkCheckFragment();
			}

			switchContent("qu", homeworkCheckFragment, "hw");

			break;

		}

	}

	private void switchContent(String tag1, Fragment to, String tag) {

		if (!curFragment.equals(tag)) {
			FragmentTransaction transaction = fm.beginTransaction();
			fm.popBackStack();
			if ("1".equals(curFragment)) {

				transaction.add(R.id.layout_container, to, tag).commit();
			} else {
				Fragment findFragmentByTag = fm.findFragmentByTag(tag1);
				if (findFragmentByTag == null) {
					ToastUtils.show("kongkomg");
				}
				if (!to.isAdded()) { // 先判断是否被add过
					transaction.hide(findFragmentByTag).add(R.id.layout_container, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
				} else {
					transaction.hide(findFragmentByTag).show(to).commit(); // 隐藏当前的fragment，显示下一个
				}
			}

			curFragment = tag;

		}
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		radio_question.setTextColor(getResources().getColor(R.color.tabbar_normal));
		radio_homework.setTextColor(getResources().getColor(R.color.tabbar_pressed));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1002) {
				if (questionFragment != null) {
					questionFragment.changeQuestion();
				}
			}
		} else {
			if (homeworkCheckFragment != null) {
				homeworkCheckFragment.changeHomeWork();
			}
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (questionFragment != null) {
			questionFragment.executeQuestionBack();
		}
		if (homeworkCheckFragment != null) {
			homeworkCheckFragment.executeHwBack();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (questionFragment != null) {
			questionFragment.executeQuestionBack();
		}
		if (homeworkCheckFragment != null) {
			homeworkCheckFragment.executeHwBack();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		// super.onSaveInstanceState(outState);
		// //将这一行注释掉，阻止activity保存fragment的状态
	}
}
