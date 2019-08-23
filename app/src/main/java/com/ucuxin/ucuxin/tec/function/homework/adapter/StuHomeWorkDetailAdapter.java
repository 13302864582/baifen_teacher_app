package com.ucuxin.ucuxin.tec.function.homework.adapter;

import java.util.ArrayList;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;*/
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.student.StuHomeWorkCheckDetailItemFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class StuHomeWorkDetailAdapter extends FragmentStatePagerAdapter {

	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
	private final SparseArray<StuHomeWorkCheckDetailItemFragment> mFragmentRef = new SparseArray<StuHomeWorkCheckDetailItemFragment>();

	public void setAllPageData(ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList,int state,int subjectid) {
		this.mHomeWorkPageModelList = homeWorkPageModelList;
		for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
			StuHomeWorkCheckDetailItemFragment fragment = getFragment(i);
			if (fragment != null) {
				fragment.showPageData(homeWorkPageModelList.get(i).getCheckpointlist(),state,subjectid);
			}
		}
	}

	public void setPageData(int position, StuPublishHomeWorkPageModel pageModel,int state,int subjectid) {
		mHomeWorkPageModelList.set(position, pageModel);
		StuHomeWorkCheckDetailItemFragment fragment = getFragment(position);
		if (fragment != null) {
			fragment.showPageData(pageModel.getCheckpointlist(),state,subjectid);
		}
	}

	public StuHomeWorkDetailAdapter(FragmentManager fm, ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList) {
		super(fm);
		this.mHomeWorkPageModelList = homeWorkPageModelList;
	}

	@Override
	public Fragment getItem(int position) {
		StuHomeWorkCheckDetailItemFragment fragment = StuHomeWorkCheckDetailItemFragment
				.newInstance(mHomeWorkPageModelList.get(position));
		mFragmentRef.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentRef.remove(position);
	}

	public StuHomeWorkCheckDetailItemFragment getFragment(int pos) {
		StuHomeWorkCheckDetailItemFragment fragment = null;
		try {
			fragment = mFragmentRef.get(pos);
		} catch (Exception e) {
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mHomeWorkPageModelList == null ? 0 : mHomeWorkPageModelList.size();
	}

}

// public class StuHomeWorkDetailAdapter extends PagerAdapter {
// private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
// private SingleFragmentActivity mActivity;
// private AddPointCommonView mAddPointView;
//
// // private final SparseArray<AddPointCommonView> mFragmentRef = new
// // SparseArray<AddPointCommonView>();
//
// public StuHomeWorkDetailAdapter(ArrayList<StuPublishHomeWorkPageModel>
// mHomeWorkPageModelList,
// SingleFragmentActivity context) {
// super();
// this.mActivity = context;
// this.mHomeWorkPageModelList = mHomeWorkPageModelList;
// }
//
// public void setData(StuPublishHomeWorkPageModel pageModel, int position) {
// // this.mHomeWorkPageModelList.remove(position);
// // mHomeWorkPageModelList.add(position, pageModel);
// mHomeWorkPageModelList.set(position, pageModel);
// notifyDataSetChanged();
// }
//
// @Override
// public int getCount() {
// return mHomeWorkPageModelList == null ? 0 : mHomeWorkPageModelList.size();
// }
//
// @Override
// public boolean isViewFromObject(View arg0, Object arg1) {
// return arg0 == arg1;
// }
//
// @Override
// public void destroyItem(ViewGroup container, int position, Object object) {
// container.removeView(view)
// }
//
// @Override
// public View instantiateItem(ViewGroup container, int position) {
// View view = View.inflate(mActivity,
// R.layout.homework_check_detail_pager_item, null);
// mAddPointView = (AddPointCommonView)
// view.findViewById(R.id.add_point_common_tec_detail);
// if (position < mHomeWorkPageModelList.size()) {
// StuPublishHomeWorkPageModel pageModel = mHomeWorkPageModelList.get(position);
// mAddPointView.showData(pageModel, false, mActivity);
// }
// container.addView(view);
// return view;
// }
//
// }