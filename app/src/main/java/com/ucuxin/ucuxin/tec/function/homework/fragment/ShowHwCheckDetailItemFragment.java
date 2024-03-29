package com.ucuxin.ucuxin.tec.function.homework.fragment;

import java.util.ArrayList;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.function.homework.model.HomeWorkCheckPointModel;
import com.ucuxin.ucuxin.tec.function.homework.model.StuPublishHomeWorkPageModel;
import com.ucuxin.ucuxin.tec.function.homework.view.AddPointCommonView;

import androidx.fragment.app.Fragment;

/**
 * 展示作业检查的详细Fragment
 * @author:  sky
 */
public class ShowHwCheckDetailItemFragment extends Fragment {
	private AddPointCommonView mAddPointView;
	private StuPublishHomeWorkPageModel pageModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.homework_check_detail_pager_item, null);
		Bundle arguments = getArguments();
		pageModel = (StuPublishHomeWorkPageModel) arguments.getSerializable(StuPublishHomeWorkPageModel.TAG);

		mAddPointView = (AddPointCommonView) view.findViewById(R.id.add_point_common_tec_detail);
		if (pageModel != null) {
			String imgpath = pageModel.getImgpath();
			if (imgpath != null) {
				mAddPointView.setPagePic(imgpath, false);
			}
		}
		return view;
	}

	public void showPageData(ArrayList<HomeWorkCheckPointModel> checkpointlist,int state,int subjectid) {
		
		pageModel.setCheckpointlist(checkpointlist);
		mAddPointView.showRightOrWrongPoint(checkpointlist,state,subjectid,null,0);
	}

	public static ShowHwCheckDetailItemFragment newInstance(StuPublishHomeWorkPageModel pageModel) {
		ShowHwCheckDetailItemFragment fragment = new ShowHwCheckDetailItemFragment();
		Bundle data = new Bundle();
		data.putSerializable(StuPublishHomeWorkPageModel.TAG, pageModel);
		fragment.setArguments(data);
		return fragment;
	}

	public void removeFrameView() {
		if (mAddPointView != null) {
			mAddPointView.removeFrameDelView();
		}
	}
	
	public void replaceFrameView() {
        if (mAddPointView != null) {
            mAddPointView.replaceFrameDelView();
        }
    }
	
	public void removeAllRightWrongPoint() {
		if (mAddPointView != null) {
			mAddPointView.removeAllRightWrongPoint();
		}
	}
}
