package com.ucuxin.ucuxin.tec.utils;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class AudioUtil {
	private Context context;

	private MediaRecorder recorder;
	private String path;
	
	public static final String TAG = AudioUtil.class.getSimpleName();

	public AudioUtil(String path, Context context) {
		this.path = sanitizePath(path);
		this.context = context.getApplicationContext();
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".amr";
		}
		return MyFileUtil.VOICE_PATH + path;
	}

	public boolean start() {	
		try {
			if (!MyFileUtil.sdCardIsAvailable()) {
				ToastUtils.show("SD卡不存在，请插入SD卡!");
				//throw new IOException("SD Card is not mounted,It is  " + Environment.getExternalStorageState() + ".");
			} 	
			if (!MyFileUtil.sdCardHasEnoughSpace()) {
				ToastUtils.show("SD卡空间按不足!");
				return false;
			}
			File directory = new File(path).getParentFile();
			if (!directory.exists() && !directory.mkdirs()) { 
				ToastUtils.show("录音文件路径不存在");
				//throw new IOException("Path to file could not be created"); 
			}
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			recorder.setOutputFile(path);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOnInfoListener(null);
			recorder.setOnErrorListener(null);
			recorder.prepare();
			recorder.start();
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;			
		} catch (IOException e) {
			e.printStackTrace();
			LogUtils.e(TAG, "prepare failed");
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
	}   

	public void stop(double voiceValue) throws IOException {
		voiceValue = 0.0;
		if (recorder != null) {
			recorder.stop();
			recorder.reset();
			recorder.release();
			recorder = null;
		}
	}
	
	
//	当我不对着麦克风说任何话的时候，测试获得的mMediaRecorder.getMaxAmplitude()值即为基准值  分贝
	public double getAmplitude() {		
		if (recorder != null) {		
			try {
				return recorder.getMaxAmplitude();
			} catch (Exception e) {
				
				return 0;		
			}
					
		} else	{
			return 0;
		}
			
	}
	
	public void nullContext(){
		this.context=null;
	}
	
}