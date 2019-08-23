package com.ucuxin.ucuxin.tec.function.homework.adapter;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;*/
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ucuxin.ucuxin.tec.function.homework.TecHomeWorkCheckDetailItemFragment;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * 作业检查的详细activity Adapter
 * 
 * @author: sky
 */
public class TecHomeWorkDetailAdapter extends FragmentStatePagerAdapter {

	private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;
	private ArrayList<String> list;
	

	private final SparseArray<TecHomeWorkCheckDetailItemFragment> mFragmentList = new SparseArray<TecHomeWorkCheckDetailItemFragment>();
	private boolean isAllDadian= true;
	
	

	

	public TecHomeWorkDetailAdapter(FragmentManager fm, ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList,
									ArrayList<String> list, boolean isAllDadian) {
		super(fm);
		this.mHomeWorkPageModelList = homeWorkPageModelList;
		this.list = list;
		this.isAllDadian=isAllDadian;
	}

	

	public void setAllPageData(ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList, int state, int subjectid) {
		this.mHomeWorkPageModelList = homeWorkPageModelList;
		for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
			TecHomeWorkCheckDetailItemFragment fragment = getFragment(i);
			if (fragment != null) {
				fragment.showPageData(homeWorkPageModelList.get(i).getCheckpointlist(), state, subjectid, list, i);
			}
		}
	}

	public void setPageData(int position, StuPublishHomeWorkPageModel pageModel, int state, int subjectid) {
		mHomeWorkPageModelList.set(position, pageModel);
		TecHomeWorkCheckDetailItemFragment fragment = getFragment(position);

		if (fragment != null) {

			fragment.showPageData(pageModel.getCheckpointlist(), state, subjectid, list, position);
		}
	}

	@Override
	public Fragment getItem(int position) {
		TecHomeWorkCheckDetailItemFragment fragment = TecHomeWorkCheckDetailItemFragment
				.newInstance(mHomeWorkPageModelList.get(position),isAllDadian);
		mFragmentList.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		mFragmentList.remove(position);
	}

	public TecHomeWorkCheckDetailItemFragment getFragment(int pos) {
		TecHomeWorkCheckDetailItemFragment fragment = null;
		try {
			fragment = mFragmentList.get(pos);

		} catch (Exception e) {
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return mHomeWorkPageModelList == null ? 0 : mHomeWorkPageModelList.size();
	}

	public void removeFrameView(int currentPosition) {
		TecHomeWorkCheckDetailItemFragment tecHomeWorkCheckDetailItemFragment = mFragmentList.get(currentPosition);
		if (tecHomeWorkCheckDetailItemFragment != null) {
			tecHomeWorkCheckDetailItemFragment.removeFrameView();
		}
	}

	public void replaceFrameView(int currentPosition) {
		TecHomeWorkCheckDetailItemFragment tecHomeWorkCheckDetailItemFragment = mFragmentList.get(currentPosition);
		if (tecHomeWorkCheckDetailItemFragment != null) {
			tecHomeWorkCheckDetailItemFragment.replaceFrameView();
		}
	}

	public void removeAllRightWrongPoint() {
		for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
			TecHomeWorkCheckDetailItemFragment fragment = getFragment(i);
			if (fragment != null) {
				fragment.removeAllRightWrongPoint();
			}
		}
	}

	public void removeIndexRightWrongPoint(int currentPosition, HomeWorkCheckPointModel checkPointModel) {
		TecHomeWorkCheckDetailItemFragment fragment = getFragment(currentPosition);
		if (fragment != null) {
			fragment.removeIndexRightWrongPoint(checkPointModel);
		}

	}

	// public void setData( int position) {
	// getFragment(position).changeData();
	//
	// }

}

// public class TecHomeWorkDetailAdapter extends PagerAdapter {
// private ArrayList<HomeWorkPageModel> mHomeWorkPageModelList;
// private SingleFragmentActivity mActivity;
// private final SparseArray<AddPointCommonView> mFragmentRef = new
// SparseArray<AddPointCommonView>();
//
// public TecHomeWorkDetailAdapter(ArrayList<HomeWorkPageModel>
// mHomeWorkPageModelList, SingleFragmentActivity context) {
// super();
// this.mActivity = context;
// this.mHomeWorkPageModelList = mHomeWorkPageModelList;
// }
//
// public void setData(ArrayList<HomeWorkPageModel> homeWorkPageModelList) {
// this.mHomeWorkPageModelList = homeWorkPageModelList;
// notifyDataSetChanged();
// }
//
// @Override
// public int getCount() {
// return mHomeWorkPageModelList.size();
// }
//
// @Override
// public boolean isViewFromObject(View arg0, Object arg1) {
// return arg0 == arg1;
// }
//
// @Override
// public void destroyItem(ViewGroup container, int position, Object object) {
// super.destroyItem(container, position, object);
// }
//
// @Override
// public Object instantiateItem(ViewGroup container, int position) {

// return super.instantiateItem(container, position);
// }

// }