package com.ucuxin.ucuxin.tec.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/*import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;*/
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class GuidePageAdapter extends PagerAdapter implements OnClickListener {

	public static final int GUIDE_TYPE_LOGIN = 0x1;
	public static final int GUIDE_TYPE_ASK = 0x2;
	public static final int GUIDE_TYPE_PUBLISH_HOMEWORK = 0x3;
	private Context context;
	private List<View> views;
	private OnViewClickListener mOnViewClickListener;
	private int guideType;

	public GuidePageAdapter(Context context, List<View> views, int guideType, OnViewClickListener mOnViewClickListener) {
		this.views = views;
		this.guideType = guideType;
		this.mOnViewClickListener = mOnViewClickListener;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(views.get(arg1));
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return (arg0 == arg1);
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1), 0);
		if (arg1 == views.size() - 1) {
			switch (guideType) {
			case GUIDE_TYPE_LOGIN:
				arg0.findViewById(R.id.btns_layout).setVisibility(View.VISIBLE);
				//arg0.findViewById(R.id.login_info_layout).setVisibility(View.VISIBLE);
				TextView infoTV1 = (TextView) arg0.findViewById(R.id.login_info_tv1);
				TextView tv_wangzhi = (TextView) arg0.findViewById(R.id.tv_wangzhi);
				
				final String address = TecApplication.getContext().getResources().getString(R.string.login_guide_info_address);
				//String info = TecApplication.getContext().getResources().getString(R.string.login_guide_info1, address);
				SpannableStringBuilder builder = new SpannableStringBuilder(address);
				ForegroundColorSpan redSpan = new ForegroundColorSpan(TecApplication.getContext().getResources()
						.getColor(R.color.welearn_blue));
				builder.setSpan(redSpan, 0, address.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv_wangzhi.setText("网址:"+builder);

				infoTV1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							Uri uri = Uri.parse("http://" + address);
							Intent it = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(it);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				Button phoneLogin = (Button) arg0.findViewById(R.id.phone_loginorreg_bt);
				Button qqLogin = (Button) arg0.findViewById(R.id.login_bt);
				phoneLogin.setOnClickListener(this);
				//注册h5加载
				//Button phoneRegtea = (Button) arg0.findViewById(R.id.phone_userreg_bt);
				//phoneRegtea.setOnClickListener(this);

				qqLogin.setOnClickListener(this);
				break;
			case GUIDE_TYPE_ASK:
				LinearLayout startToDoAskLayout = (LinearLayout) arg0.findViewById(R.id.start_todo_layout);
				startToDoAskLayout.setVisibility(View.VISIBLE);
				Button todoAskBtn = (Button) arg0.findViewById(R.id.start_todo_btn);
				todoAskBtn.setBackgroundResource(R.drawable.bg_start_ask_btn_selector);
				todoAskBtn.setOnClickListener(this);
				break;
			case GUIDE_TYPE_PUBLISH_HOMEWORK:
				LinearLayout startToDoHomeworkLayout = (LinearLayout) arg0.findViewById(R.id.start_todo_layout);
				startToDoHomeworkLayout.setVisibility(View.VISIBLE);
				Button todoHomeworkBtn = (Button) arg0.findViewById(R.id.start_todo_btn);
				todoHomeworkBtn.setBackgroundResource(R.drawable.bg_start_publish_homework_btn_selector);
				todoHomeworkBtn.setOnClickListener(this);
				break;
			}
		}
		return views.get(arg1);
	}

	@Override
	public void onClick(View v) {
		if (null != mOnViewClickListener) {
			mOnViewClickListener.onSubViewClick(v);
		}
	}

	public interface OnViewClickListener {
		void onSubViewClick(View v);
	}

}
