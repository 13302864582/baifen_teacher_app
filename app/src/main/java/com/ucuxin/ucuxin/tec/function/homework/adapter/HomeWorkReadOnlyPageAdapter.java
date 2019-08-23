package com.ucuxin.ucuxin.tec.function.homework.adapter;

import java.util.List;

import com.ucuxin.ucuxin.tec.function.homework.HomeWorkReadOnlyDetailItemFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;*/

public class HomeWorkReadOnlyPageAdapter extends FragmentPagerAdapter {
	
	private List<HomeWorkReadOnlyDetailItemFragment> itemList;

	public HomeWorkReadOnlyPageAdapter(FragmentManager fm, List<HomeWorkReadOnlyDetailItemFragment> itemList) {
		super(fm);
		this.itemList = itemList;
	}

	@Override
	public int getCount() {
		if (itemList != null) {
			return itemList.size();
		}
		return 0;
	}

	@Override
	public Fragment getItem(int arg0) {
		return itemList.get(arg0);
	}


}
