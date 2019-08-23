
package com.ucuxin.ucuxin.tec.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ucuxin.ucuxin.tec.R;
import com.ucuxin.ucuxin.tec.TecApplication;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * MediaUtil 工具类
 * 
 * @author: sky
 */
public class MediaUtil {

	public AudioUtil audioUtil;

	private boolean isPlaying;

	public static int RECORD_NO = 0;

	private static int RECORD_ING = 1;

	private static int RECODE_ED = 2;

	public static int RECODE_STATE = 0;

	private static int MIX_TIME = 1;

	private static boolean mIsMsgChat;

	private boolean isCancel;

	private double voiceValue;
	RecordCallback mcallback;
	private float recodeTime;
	private Handler mHandle;
	private Runnable mtask;
	private ArrayList<String> arraylist = new ArrayList<String>();

	public enum MaxRecordTime {
		MAX_OF_QA, MAX_OF_HOMEWORK, MAX_OF_COURSE
	}

	private static class WeLearnMediaUtilHolder {
		private static final MediaUtil INSANCE = new MediaUtil();
	}

	public static MediaUtil getInstance(boolean isMsgChat) {
		mIsMsgChat = isMsgChat;
		return WeLearnMediaUtilHolder.INSANCE;
	}

	public void setIsCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	public void record(String fileName, RecordCallback callback, Activity context) {
		record(fileName, callback, context, null, MaxRecordTime.MAX_OF_QA);
	}

	/**
	 * listener不为空的话, 可以点击dialog停止录音
	 * 
	 * @param fileName
	 * @param callback
	 * @param context
	 * @param listener
	 */
	public void record(String fileName, RecordCallback callback, Activity context, OnClickListener listener,
			MaxRecordTime recordTime) {
		if (RECODE_STATE != RECORD_ING) {
			// MyFileUtil.deleteVoiceFile();
			RECODE_STATE = RECORD_ING;
			if (listener != null) {
				WeLearnUiUtil.getInstance().showCourseVoiceDialog(context, listener);
			} else if (mIsMsgChat) {
				// {
				WeLearnUiUtil.getInstance().showChatVoiceDialog(context);
				// } else {
				// WeLearnUiUtil.getInstance().showVoiceDialog(context);
				// }
			}
			audioUtil = new AudioUtil(fileName, context);
			try {
				audioUtil.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ThreadPoolUtil.execute(new ImgThread(callback, recordTime));
		}
	}

	public void record(String fileName, final RecordCallback callback, Activity context,
			final MaxRecordTime recordTime) {
		if (RECODE_STATE != RECORD_ING) {
			// MyFileUtil.deleteVoiceFile();
			RECODE_STATE = RECORD_ING;
			audioUtil = new AudioUtil(fileName, context);
			arraylist.add(MyFileUtil.VOICE_PATH + fileName + ".amr");
			try {
				audioUtil.start();

			} catch (Exception e) {
				e.printStackTrace();
				RECODE_STATE = RECODE_ED;
				callback.onAfterRecord(-1);
				return;
			}
			mtask = new Runnable() {
				@Override
				public void run() {
					if (arraylist.size() > 1) {

						stopRecord(voiceValue, callback, true);
					} else {
						if (mHandle != null && mtask != null) {

							mHandle.removeCallbacks(mtask);
						}
						voiceValue = audioUtil.getAmplitude();
						arraylist.clear();
						if (audioUtil != null) {
							try {
								audioUtil.stop(voiceValue);
							} catch (Exception e) {
								e.printStackTrace();
								RECODE_STATE = RECODE_ED;
								callback.onAfterRecord(-1);
								return;
							}

						}

						if (callback != null) {
							callback.onAfterRecord(2);
						}
						RECODE_STATE = RECODE_ED;

					}

				}

			};
			mHandle = new Handler();
			mHandle.postDelayed(mtask, 2 * 60 * 1000);
		}
	}

	public void stopRecord(final double voiceValue, final RecordCallback callback, final boolean falg) {
		int delayTime = 1000;
		if (isCancel) {
			delayTime = 0;
		}
		if (!falg) {
			delayTime = 0;
		}
		if (mHandle != null && mtask != null) {

			mHandle.removeCallbacks(mtask);
		}
		mtask = null;
		mHandle = null;
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				WeLearnUiUtil.getInstance().closeDialog();

				try {

					audioUtil.stop(voiceValue);
				} catch (Exception e) {
					e.printStackTrace();
					RECODE_STATE = RECODE_ED;
					arraylist.clear();
					callback.onAfterRecord(-1);
					return;
				}
				if (falg) {
					if (arraylist.size() > 1) {

						getInputCollection(arraylist);
					}
					arraylist.clear();

				}
				callback.onAfterRecord(recodeTime);
				RECODE_STATE = RECODE_ED;
				// if (recodeTime < MIX_TIME) {
				// //WeLearnUiUtil.getInstance().showWarnDialogWhenRecordVoice();
				// RECODE_STATE = RECORD_NO;
				// } else {
				// callback.onAfterRecord(recodeTime);
				// }
			}

		}, delayTime);
	}

	public void getInputCollection(ArrayList<String> list) {

		String string = list.get(0);
		String mMinute = MyFileUtil.VOICE_PATH + System.currentTimeMillis() + ".amr";
		// 创建音频文件,合并的文件放这里
		File file1 = new File(mMinute);
		FileOutputStream fileOutputStream = null;

		if (!file1.exists()) {
			try {
				file1.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(file1);

		} catch (IOException e) {

			e.printStackTrace();
		}
		// list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
		for (int i = 0; i < list.size(); i++) {
			File file = new File(list.get(i));
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] myByte = new byte[fileInputStream.available()];
				// 文件长度
				int length = myByte.length;

				// 头文件
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				}

				// 之后的文件，去掉头文件就可以了
				else {
					while (fileInputStream.read(myByte) != -1) {

						fileOutputStream.write(myByte, 6, length - 6);
					}
				}

				fileOutputStream.flush();
				fileInputStream.close();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		// 结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			File file = new File(list.get(i));
			if (file != null) {
				file.delete();
			}
		}
		boolean boo = file1.renameTo(new File(string));

	}

	private class ImgThread implements Runnable {
		private int MAX_TIME;

		private RecordCallback callback;

		private MaxRecordTime recordTime;

		Handler imgHandle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						WeLearnUiUtil.getInstance().closeDialog();
						try {
							if (audioUtil != null) {
								audioUtil.stop(voiceValue);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (recodeTime < MIX_TIME) {
							WeLearnUiUtil.getInstance().showWarnDialogWhenRecordVoice();
							RECODE_STATE = RECORD_NO;
						} else {
							if (callback != null) {
								callback.onAfterRecord(recodeTime);
							}
						}
					}
					break;
				case 1:
					if (mIsMsgChat) {
						if (!isCancel) {
							WeLearnUiUtil.getInstance().setMsgDialogImage(voiceValue);
						}
					} else {
						WeLearnUiUtil.getInstance().setDialogImage(voiceValue);
					}
					break;
				default:
					break;
				}
			}
		};

		public ImgThread(RecordCallback callback, MaxRecordTime recordTime) {
			this.callback = callback;
			this.recordTime = recordTime;
			switch (this.recordTime) {
			case MAX_OF_QA:
				MAX_TIME = 60;
				break;
			case MAX_OF_HOMEWORK:
				MAX_TIME = 60;
				break;
			case MAX_OF_COURSE:
				MAX_TIME = 120;
				break;

			default:
				break;
			}
		}

		@Override
		public void run() {
			recodeTime = 0.0f;
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					imgHandle.sendEmptyMessage(0);
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					recodeTime += 0.2;
					if (RECODE_STATE == RECORD_ING) {
						voiceValue = audioUtil.getAmplitude();
						imgHandle.sendEmptyMessage(1);
					}
				}
			}
		}

	}

	public void stopVoice(AnimationDrawable animationDrawable) {
		if (isPlaying) {
			isPlaying = false;
		}
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
		if (currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
			currentMediaPlayer.setOnErrorListener(null);
			currentMediaPlayer.stop();
			currentMediaPlayer.release();
			currentMediaPlayer = null;
		}
	}

	public void pauseVoice(AnimationDrawable animationDrawable) {
		if (currentMediaPlayer != null) {
			currentMediaPlayer.pause();
		}
		if (animationDrawable != null && animationDrawable.isRunning()) {
			animationDrawable.stop();
		}
	}

	private boolean isPause;

	private MediaPlayer currentMediaPlayer;

	// private MediaPlayer stopPlayer;

	private String currentPath = "";
	
	
	

	public MediaPlayer getCurrentMediaPlayer() {
		return currentMediaPlayer;
	}

	public void setCurrentMediaPlayer(MediaPlayer currentMediaPlayer) {
		this.currentMediaPlayer = currentMediaPlayer;
	}

	/**
	 * 播放声音
	 * 
	 * @param audioPath
	 * @param animationDrawable
	 * @param callback
	 */
	public void playVoice(final boolean isLocal, final String audioPath, final AnimationDrawable animationDrawable,
			final ResetImageSourceCallback callback, final ProgressBar bar) {
		if (!audioPath.equals(currentPath)) {
			stopPlay();
			isPause = false;
		}

		currentPath = audioPath;

		final ProgressBarRunnable pbr = new ProgressBarRunnable(bar);
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MsgConstant.END_OF_PLAY:
					if (null != currentMediaPlayer) {
						currentMediaPlayer.setVolume(1.0f, 1.0f);
						currentMediaPlayer.start();
						isPlaying = true;
						if (callback != null) {
							callback.playAnimation();
						}
						if (bar != null) {
							bar.setProgress(0);
							bar.setMax(currentMediaPlayer.getDuration());
							ThreadPoolUtil.execute(pbr);
						}
						currentMediaPlayer.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								stopVoice(animationDrawable);
								if (callback != null) {
									callback.reset();
								}
								if (bar != null) {
									bar.setVisibility(View.GONE);
								}
							}
						});
					}
					break;
				}
			}
		};

		if (animationDrawable != null && !animationDrawable.isRunning()) {
			animationDrawable.start();
		}
		if (isPause && currentMediaPlayer != null) {// 如果点击的时候已经暂停了，则恢复播放
			currentMediaPlayer.start();
			isPlaying = true;
			isPause = false;
			ThreadPoolUtil.execute(pbr);
		} else {
			if (!isPlaying) {
				if (callback != null) {
					callback.beforePlay();
				}
				currentMediaPlayer = new MediaPlayer();
				currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
				ThreadPoolUtil.execute(new Runnable() {

					@Override
					public void run() {
						FileInputStream input = null;
						try {
							if (isLocal) {
								File file = new File(audioPath);
								if (file.exists()) {
									input = new FileInputStream(file);
									currentMediaPlayer.setDataSource(input.getFD());
								}
							} else {
								currentMediaPlayer.setDataSource(TecApplication.getContext(), Uri.parse(audioPath));
							}
							isPlaying = true;
							currentMediaPlayer.prepare();
							handler.sendEmptyMessage(MsgConstant.END_OF_PLAY);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (input != null) {
								try {
									input.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
			} else {// 暂停播放
				pauseVoice(animationDrawable);
				isPlaying = false;
				isPause = true;
				if (callback != null) {
					callback.reset();
				}
			}
		}
	}

	public void playLocalAudio(Context context, MediaPlayer player) {
		// stopPlayer = player;
		// if (!stopPlayer.isPlaying()) {
		// try {
		// stopPlayer.prepare();
		// } catch (IllegalStateException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// stopPlayer.start();
		// }

		currentMediaPlayer = player;
		if (!currentMediaPlayer.isPlaying()) {
			try {
				currentMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			currentMediaPlayer.start();
		}

	}

	public void stopLocalAudio() {
		// if (stopPlayer != null) {
		// stopPlayer.setOnErrorListener(null);
		// stopPlayer.stop();
		// stopPlayer.release();
		// stopPlayer = null;
		// }
		if (currentMediaPlayer != null) {
			currentMediaPlayer.setOnErrorListener(null);
			currentMediaPlayer.stop();
			currentMediaPlayer.release();
			currentMediaPlayer = null;
		}
	}

	/**
	 * 停止其他播放
	 */
	public void stopPlay() {
		for (AnimationDrawable animation : TecApplication.animationDrawables) {
			stopVoice(animation);
		}
	}

	/**
	 * 重置播放动画
	 * 
	 * @param iconVoiceView
	 */
	public void resetAnimationPlay(ImageView iconVoiceView) {
		for (ImageView currentView : TecApplication.anmimationPlayViews) {
			if (currentView != iconVoiceView) {
				currentView.setImageResource(R.drawable.ic_play2);
			}
		}
	}

	/**
	 * 重置播放动画
	 * 
	 * @param iconVoiceView
	 */
	public void resetAnimationPlayAtHomeWork(ImageView iconVoiceView) {
		for (ImageView currentView : TecApplication.anmimationPlayViews) {
			if (currentView != iconVoiceView) {
				int resID = 0;
				Object tag = currentView.getTag();
				if (tag != null && (tag instanceof Integer)) {
					resID = (Integer) tag;
				}
				if (resID == 0) {
					currentView.setImageResource(R.drawable.ic_play2);
				} else {
					currentView.setImageResource(resID);
				}
			}
		}
	}

	private class ProgressBarRunnable implements Runnable {
		private ProgressBar bar;

		public ProgressBarRunnable(ProgressBar bar) {
			this.bar = bar;
		}

		@Override
		public void run() {
			synchronized (this) {
				// bar.setProgress(0);
				if (currentMediaPlayer != null) {
					int currentPos = 0;
					int total = currentMediaPlayer.getDuration();
					while (currentMediaPlayer != null && currentPos < total && !isPause) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (currentMediaPlayer != null) {
							try {
								currentPos = currentMediaPlayer.getCurrentPosition();
							} catch (Exception e) {
								e.printStackTrace();
							}
							// bar.setProgress(currentPos);
						}
					}
				}
			}
		}
	}

	/**
	 * 播放过程中的回调
	 * 
	 * @author 汪春
	 */
	public interface RecordCallback {
		void onAfterRecord(float voiceTime);// 录制后处理
	}

	public interface ResetImageSourceCallback {
		void beforePlay();

		void reset();

		void playAnimation();
	}

}
