package com.ucuxin.ucuxin.tec.adapter;

import java.util.ArrayList;

import android.content.Context;
/*import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;*/

import com.viewpagerindicator.IconPagerAdapter;
import com.ucuxin.ucuxin.tec.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	private String[] mTitle;
	private int[] ICONS = new int[] { R.drawable.icon_stu_home_menu_gasstation_btn_selector,
			R.drawable.icon_stu_home_menu_talk_btn_selector, R.drawable.icon_stu_home_menu_friends_btn_selector };
	private ArrayList<Fragment> mFragmentList;

	public TabPageIndicatorAdapter(Context context, FragmentManager fm, ArrayList<Fragment> mFragmentList) {
		super(fm);
		this.mTitle = context.getResources().getStringArray(R.array.title_name_array);
		this.mFragmentList = mFragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitle[position % mTitle.length];
	}

	@Override
	public int getCount() {
		return mTitle.length;
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index];
	}
}
