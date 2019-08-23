
package com.ucuxin.ucuxin.tec.adapter;

import java.util.LinkedList;
import java.util.List;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.base.ImageLoader;
import com.ucuxin.ucuxin.tec.utils.DisplayUtils;
import com.ucuxin.ucuxin.tec.view.imageview.DragImageView;


import android.app.Activity;
//import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;


public class LearningAdapter extends PagerAdapter {
    private Activity activity;

    private List<String> viewPagerList;
    private int height;

    public LearningAdapter(Activity activity, List<String> viewPagerList) {
        super();
        this.activity = activity;
        this.viewPagerList = viewPagerList;
        Display display = this.activity.getWindowManager().getDefaultDisplay();
		 height = display.getHeight()-DisplayUtils.dip2px(activity, 150);
    }
	
    // 当前viewPager里面有多少个条目
    LinkedList<View> convertView = new LinkedList<View>();

    // ArrayList
    @Override
    public int getCount() {
        return viewPagerList==null?0:viewPagerList.size();
    }

    /* 判断返回的对象和 加载view对象的关系 */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	View view = (View)object;
        convertView.add(view);// 把移除的对象 添加到缓存集合中
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	 View view=null;
        if (viewPagerList!=null&&viewPagerList.size()>0) {
            final int index = position % viewPagerList.size();
          
            if (convertView.size() > 0) {
                view = convertView.remove(0);
            } else {

            	 view = LayoutInflater.from(activity).inflate(R.layout.add_point_common_view, null);
            }
            DragImageView  mPicIv = (DragImageView) view.findViewById(R.id.pic_iv_add_point);
            mPicIv.setScreenSize(DisplayUtils.getScreenWidth() , height);
            ImageLoader.getInstance().loadImage(viewPagerList.get(index), mPicIv, R.drawable.loading,null);
            container.addView(view); // 加载的view对象
        }
       
        return view; // 返回的对象
    }

}
