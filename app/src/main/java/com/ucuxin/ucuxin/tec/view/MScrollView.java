package com.ucuxin.ucuxin.tec.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MScrollView extends ScrollView {

	public MScrollView(Context context) {
		super(context);

	}
	
	
	public MScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}
	
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
