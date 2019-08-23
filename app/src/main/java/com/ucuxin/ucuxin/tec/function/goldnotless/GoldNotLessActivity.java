package com.ucuxin.ucuxin.tec.function.goldnotless;
//package com.ucuxin.goldnotless;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.ucuxin.base.view.SingleFragmentActivity;
//import com.ucuxin.manager.IntentManager;
//import com.ucuxin.util.GoldToStringUtil;
//import com.ucuxin.util.WeLearnSpUtil;
//import com.ucuxin.ucuxin.tec.R;
//
//public class GoldNotLessActivity extends SingleFragmentActivity implements OnClickListener {
//
//	private LinearLayout mSignIn;
//	private LinearLayout mFriend;
//	private LinearLayout mPay;
//	private TextView mGold;
//	private ImageView unsignIntodayPointIv;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.fragment_gold_not_less);
//		
//		findViewById(R.id.back_layout).setOnClickListener(this);
//		
//		mSignIn = (LinearLayout) findViewById(R.id.dayday_signin_container);
//		mFriend = (LinearLayout) findViewById(R.id.friend_gold_container);
//		mPay = (LinearLayout) findViewById(R.id.pay_gold_container);
//		mGold = (TextView) findViewById(R.id.current_gold_num);
//		unsignIntodayPointIv = (ImageView) findViewById(R.id.unsign_intoday_point_iv_mygold);
//
//		setWelearnTitle(R.string.text_gold_selction);
//
//		mSignIn.setOnClickListener(this);
//		mFriend.setOnClickListener(this);
//		mPay.setOnClickListener(this);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		float gold = WeLearnSpUtil.getInstance().getUserGold();
//		String goldStr = GoldToStringUtil.GoldToString(gold);
//		mGold.setText(goldStr);
//		
//		int todaySignIn = WeLearnSpUtil.getInstance().isTodaySignIn();
//		if (todaySignIn != 0) {//今天已经签到
//			unsignIntodayPointIv.setVisibility(View.GONE);
//		}else {
//			unsignIntodayPointIv.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back_layout:
//			finish();
//			break;
//		case R.id.dayday_signin_container:
//			IntentManager.goSignInActivity(this);
//			break;
//		case R.id.friend_gold_container:
//			IntentManager.gotoFriendGoldActivity(this);
//			break;
//		case R.id.pay_gold_container:
//			IntentManager.goPayActivity(this);
//			break;
//		default:
//			break;
//		}
//	}
//
//}
