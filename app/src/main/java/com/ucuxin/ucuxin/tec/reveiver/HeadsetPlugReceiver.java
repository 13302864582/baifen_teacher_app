package com.ucuxin.ucuxin.tec.reveiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.ucuxin.ucuxin.tec.TecApplication;

public class HeadsetPlugReceiver extends BroadcastReceiver {
	
	private static final String TAG = HeadsetPlugReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		  if (intent.hasExtra("state")){
			   if (intent.getIntExtra("state", 0) == 0){	
//				   Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show();
				   setSpeakerphoneOn(true);
			   }
			   else if (intent.getIntExtra("state", 0) == 1){
//				   Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show();
				   setSpeakerphoneOn(false);
			   }
		  }		
	}

	//system setting API	
	 private void setSpeakerphoneOn(boolean on) {
		 if(on) {
			 TecApplication.audioManager.setSpeakerphoneOn(true);
			 TecApplication.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, TecApplication.audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.USE_DEFAULT_STREAM_TYPE);//FLAG_PLAY_SOUND);
			 TecApplication.audioManager.setMode(AudioManager.STREAM_MUSIC);			 
		 } else {
			 TecApplication.audioManager.setSpeakerphoneOn(false);
			 TecApplication.audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,TecApplication.audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),AudioManager.USE_DEFAULT_STREAM_TYPE);
//			 WApplication.audioManager.adjustStreamVolume (AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI); 
			 TecApplication.audioManager.setMode(AudioManager.MODE_IN_CALL);
		 }
	 }
	 
}
