package com.ucuxin.ucuxin.tec.adapter;

import org.json.JSONArray;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;*/
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ucuxin.ucuxin.tec.function.question.OneQuestionMoreAnswersDetailItemFragment;
import com.ucuxin.ucuxin.tec.utils.JsonUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class AnswerDetailAdapter extends FragmentStatePagerAdapter {

	private String mJsonStr;
	private Boolean mIsQpad;
	private final SparseArray<OneQuestionMoreAnswersDetailItemFragment> mFragmentRef = new SparseArray<OneQuestionMoreAnswersDetailItemFragment>();

	public void setData(String jsonStr, boolean isQpad) {
		this.mJsonStr = jsonStr;
		this.mIsQpad = isQpad;
		notifyDataSetChanged();
	}

	public AnswerDetailAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		OneQuestionMoreAnswersDetailItemFragment fragment = OneQuestionMoreAnswersDetailItemFragment.newInstance(
				position, mJsonStr, mIsQpad);
		mFragmentRef.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentRef.remove(position);
	}

	public OneQuestionMoreAnswersDetailItemFragment getFragment(int pos) {
		OneQuestionMoreAnswersDetailItemFragment fragment = null;
		fragment = mFragmentRef.get(pos);
		return fragment;
	}

	@Override
	public int getCount() {
		JSONArray answerArray = JsonUtils.getJSONArray(mJsonStr, "answer", new JSONArray());
		return answerArray.length() + 1;
	}
}
