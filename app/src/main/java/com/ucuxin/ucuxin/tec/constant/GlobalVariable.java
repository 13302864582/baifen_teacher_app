
package com.ucuxin.ucuxin.tec.constant;

import java.util.ArrayList;
import java.util.List;

import com.ucuxin.ucuxin.tec.base.BaseFragment;
import com.ucuxin.ucuxin.tec.function.communicate.ChatMsgViewActivity;
import com.ucuxin.ucuxin.tec.function.question.OneQuestionMoreAnswersDetailActivity;
import com.ucuxin.ucuxin.tec.view.popwindow.AnswertextPopupWindow;
import com.ucuxin.ucuxin.tec.view.viewpager.MyViewPager;

import android.app.Activity;

/**
 * 对象定义
 * @author Administrator
 *
 */
public class GlobalVariable {
    public static Activity mainActivity;

    public static Activity myQPadActivity;

    public static Activity QAHallActivity;

    public static Activity centerActivity;

    public static Activity loginActivity;



    public static Activity oneQueMoreAnswActivity;

    public static BaseFragment loginFragment;

    public static Activity getBackPswActivity;

    public static boolean firstOpen;

    public static boolean doingGoldDB;

    public static MyViewPager mViewPager;
    // public static ChatMsgViewFragment mChatMsgViewFragment;

    public static ChatMsgViewActivity mChatMsgViewActivity;



    public static OneQuestionMoreAnswersDetailActivity mOneQuestionMoreAnswersDetailActivity;
    public static  AnswertextPopupWindow answertextPopupWindow;

    // public static HomeWorkModel mHomeWorkModel;
    
    
    public static List<Activity> tempActivityList=new ArrayList<Activity>();
}
