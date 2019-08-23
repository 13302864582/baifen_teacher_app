package com.ucuxin.ucuxin.tec;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.ucuxin.ucuxin.tec.api.WeLearnApi;
import com.ucuxin.ucuxin.tec.base.BaseActivity;
import com.ucuxin.ucuxin.tec.config.AppConfig;
import com.ucuxin.ucuxin.tec.constant.UmengEventConstant;
import com.ucuxin.ucuxin.tec.db.WLDBHelper;
import com.ucuxin.ucuxin.tec.manager.IntentManager;
import com.ucuxin.ucuxin.tec.model.UserInfoModel;
import com.ucuxin.ucuxin.tec.utils.MyFileUtil;
import com.ucuxin.ucuxin.tec.utils.SharePerfenceUtil;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 此类的描述： 闪屏页面
 * 
 * @author: Sky @最后修改人： Sky
 * @最后修改日期:2015-7-20 下午7:04:37
 * @version: 2.0
 */
public class SplashActivity extends BaseActivity {
	protected static final String TAG = "SplashFragment";
	private ImageView welcomeImageView;
	
	
	public static final int HANDLER_SPLASH_CODE = 246;
	
	private MyHandler mhandler=new MyHandler(this);
	
	

//	@SuppressLint("HandlerLeak")
//	private Handler mhandler = new Handler() {
//		// private String openId;
//		public void handleMessage(android.os.Message msg) {
//			
//		};
//	};
	
	class MyHandler extends Handler{
		
		private WeakReference<SplashActivity> mSplashActivity;

		public MyHandler(SplashActivity mSplashActivity) {
			super();
			this.mSplashActivity = new WeakReference<SplashActivity>(mSplashActivity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SplashActivity activity=mSplashActivity.get();
			if (activity!=null) {
				switch (msg.what) {
				case HANDLER_SPLASH_CODE:
					boolean isLogin = false;

					UserInfoModel uInfo = WLDBHelper.getInstance().getWeLearnDB().queryCurrentUserInfo();
					if (null != uInfo) {
						isLogin = true;
					}

					int type = SharePerfenceUtil.getInstance().getGoLoginType();
					if (type != SharePerfenceUtil.LOGIN_TYPE_PHONE && type != SharePerfenceUtil.LOGIN_TYPE_QQ) {
						isLogin = false;
					}

					if (isLogin) {
						WeLearnApi.sendSessionToServer();
						IntentManager.goToMainView(SplashActivity.this);
					} else {
						IntentManager.goToLogInView(SplashActivity.this);
					}
					break;
				}			
			}			
		}		
		
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// MobclickAgent.updateOnlineConfig(WApplication.getContext());
		// mActionBar.hide();
		// 全屏并隐藏标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.fragment_splash);
		initView();
		initListener();

	}

	@Override
	public void initView() {
		super.initView();
//		if (AppConfig.IS_DEBUG) {
//			RefWatcher refWatcher = TecApplication.getRefWatcher(this);
//			refWatcher.watch(this);
//		}
		welcomeImageView = (ImageView) findViewById(R.id.welcome_img);
		
		String filePath = MyFileUtil.getWelcomeImagePath();
		File file = new File(filePath);
		if (file.exists()) {
			Bitmap bm = null;
			int latestVersion = SharePerfenceUtil.getInstance().getInt("versionCode", -1);
			if (latestVersion == -1 || latestVersion < TecApplication.versionCode) {
				file.delete();
				if (AppConfig.IS_DEBUG) {
					Toast.makeText(this, "删除文件", Toast.LENGTH_SHORT).show();
				}
			} else {
				Options op = new Options();
				op.outHeight = 1920;
				op.outWidth = 1080;
				bm = BitmapFactory.decodeFile(file.getAbsolutePath(), op);
			}
			if (null != bm) {
				welcomeImageView.setImageBitmap(bm);
			} else {
				if (AppConfig.IS_DEBUG) {
					Toast.makeText(this, "文件存在，bitmap为null", Toast.LENGTH_SHORT).show();
				}
				SharePerfenceUtil.getInstance().setWelcomeImageTime(0L);
			}
		} else {
			SharePerfenceUtil.getInstance().setWelcomeImageTime(0L);
		}
		SharePerfenceUtil.getInstance().putInt("versionCode", TecApplication.versionCode);
		boolean knowledgeExis = WLDBHelper.getInstance().getWeLearnDB().isKnowledgeExis();
		if (knowledgeExis) {
			Message msg = mhandler.obtainMessage();
			msg.what = HANDLER_SPLASH_CODE;
			int delayed = 1500;
			boolean isNewUser = SharePerfenceUtil.getInstance().IsNewUser();
			if (isNewUser) {
				delayed = 3000;
			}
			if (AppConfig.IS_DEBUG) {
				delayed = 300;
			}
			mhandler.sendMessageDelayed(msg, delayed);
		} else {
			mhandler.sendEmptyMessageDelayed(HANDLER_SPLASH_CODE, 100);
			// HttpHelper.post(this, "guest", "getallkpoint", null, new
			// HttpListener() {
			// @Override
			// public void onSuccess(int code, final String dataJson, String
			// errMsg) {
			// mhandler.sendEmptyMessageDelayed(GlobalContant.SPLASH, 100);
			// if (code == 0 && !TextUtils.isEmpty(dataJson)) {
			// ThreadPoolUtil.execute(new Runnable() {
			//
			// @Override
			// public void run() {
			// List<CatalogModel> catalogModels = null;
			// try {
			// catalogModels = JSON.parseArray(dataJson, CatalogModel.class);
			// } catch (Exception e) {
			// }
			// if (catalogModels != null) {
			// WLDBHelper.getInstance().getWeLearnDB().insertKnowledge(catalogModels);
			// }
			// }
			// });
			//
			// }
			//
			// }
			//
			// @Override
			// public void onFail(int HttpCode) {
			// mhandler.sendEmptyMessageDelayed(GlobalContant.SPLASH, 100);
			//
			// }
			// });

		}
	}

	@Override
	public void initListener() {
		super.initListener();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onEventBegin(this, UmengEventConstant.CUSTOM_EVENT_SPLASH);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onEventEnd(this, UmengEventConstant.CUSTOM_EVENT_SPLASH);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MobclickAgent.onEventEnd(this, UmengEventConstant.CUSTOM_EVENT_SPLASH);
		mhandler.removeCallbacksAndMessages(null);
	}

}
