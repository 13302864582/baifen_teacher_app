
package com.ucuxin.ucuxin.tec.function.homework.adapter;

import java.util.ArrayList;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;*/
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ucuxin.ucuxin.tec.function.homework.fragment.ShowHwCheckDetailItemFragment;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * 显示作业检查的结果Adapter
 * 
 * @author: sky
 */
public class ShowHomeworkAdapter extends FragmentStatePagerAdapter {

    private ArrayList<StuPublishHomeWorkPageModel> mHomeWorkPageModelList;

    private final SparseArray<ShowHwCheckDetailItemFragment> mFragmentList = new SparseArray<ShowHwCheckDetailItemFragment>();

    public ShowHomeworkAdapter(FragmentManager fm,
                               ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList) {
        super(fm);
        this.mHomeWorkPageModelList = homeWorkPageModelList;
    }

    public void setAllPageData(ArrayList<StuPublishHomeWorkPageModel> homeWorkPageModelList,int state,int subjectid) {
        this.mHomeWorkPageModelList = homeWorkPageModelList;
        for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
            ShowHwCheckDetailItemFragment fragment = getFragment(i);
            if (fragment != null) {
                fragment.showPageData(homeWorkPageModelList.get(i).getCheckpointlist(),state,subjectid);
            }
        }
    }

    public void setPageData(int position, StuPublishHomeWorkPageModel pageModel,int state,int subjectid) {
        mHomeWorkPageModelList.set(position, pageModel);
        ShowHwCheckDetailItemFragment fragment = getFragment(position);
        if (fragment != null) {
            fragment.showPageData(pageModel.getCheckpointlist(),state,subjectid);
        }
    }

    @Override
    public Fragment getItem(int position) {
        ShowHwCheckDetailItemFragment fragment = ShowHwCheckDetailItemFragment
                .newInstance(mHomeWorkPageModelList.get(position));
        mFragmentList.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mFragmentList.remove(position);
    }

    public ShowHwCheckDetailItemFragment getFragment(int pos) {
        ShowHwCheckDetailItemFragment fragment = null;
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
        ShowHwCheckDetailItemFragment ShowHwCheckDetailItemFragment = mFragmentList
                .get(currentPosition);
        if (ShowHwCheckDetailItemFragment != null) {
            ShowHwCheckDetailItemFragment.removeFrameView();
        }
    }

    public void replaceFrameView(int currentPosition) {
        ShowHwCheckDetailItemFragment ShowHwCheckDetailItemFragment = mFragmentList
                .get(currentPosition);
        if (ShowHwCheckDetailItemFragment != null) {
            ShowHwCheckDetailItemFragment.replaceFrameView();
        }
    }

    public void removeAllRightWrongPoint() {
        for (int i = 0; i < mHomeWorkPageModelList.size(); i++) {
            ShowHwCheckDetailItemFragment fragment = getFragment(i);
            if (fragment != null) {
                fragment.removeAllRightWrongPoint();
            }
        }
    }

    

}
