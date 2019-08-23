package com.ucuxin.ucuxin.tec.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import com.ucuxin.ucuxin.tec.constant.GlobalContant;
import com.ucuxin.ucuxin.tec.constant.MsgConstant;
import com.ucuxin.ucuxin.tec.dispatch.ImMsgDispatch;
import com.ucuxin.ucuxin.tec.dispatch.WelearnHandler;
import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

/**
 * 用作控制器
 * 
 * @author parsonswang
 * 
 */
public class BaseController {

	private HandlerThread mHandlerThread;
	private Handler mHandler;
	private WeakReference<ImMsgDispatch> dispatcherWeakReference=new WeakReference<ImMsgDispatch>(new ImMsgDispatch() {

		@Override
		public Bundle handleImMsg(Message msg) {
			handleResponseMessage(msg);
			return null;
		}
	});

	protected Model mModel;
	protected INetWorkListener mListner;
	private String mMsgQueueName;

	protected void handleResponseMessage(Message msg) {
		if ((MsgConstant.MSG_DEF_CONN_TIMEOUT == msg.what || MsgConstant.MSG_DEF_SVR_ERROR == msg.what) && mListner != null) {
			mListner.onDisConnect();
			if (MsgConstant.MSG_DEF_CONN_TIMEOUT == msg.what) {
			}
		} else {
			if (msg.obj instanceof String) {
				String jsonStr = (String) msg.obj;
				if (!TextUtils.isEmpty(jsonStr) && mListner != null) {
					mListner.onAfter(jsonStr, msg.what);
				}
			}
		}
	}

	public void removeMsgInQueue() {
		if (mHandlerThread != null) {
			Looper looper = mHandlerThread.getLooper();
			if (looper != null) {
				looper.quit();
			}
		}
		if (mMsgQueueName == null || dispatcherWeakReference.get() == null) {
			return;
		}
		WelearnHandler.getInstance().unRegistDispatcher(dispatcherWeakReference.get(),
				mMsgQueueName);
		mListner=null;
	}

	protected void handleEventMessage(Message msg) {
	}

	private BaseController() {
		mHandlerThread = new HandlerThread(GlobalContant.HANDLER_THREAD_NAME);
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				BaseController.this.handleEventMessage(msg);
			}
		};
	}

	public BaseController(Model model) {
		this();
		this.mModel = model;
		WelearnHandler.getInstance()
				.registDispatcher(dispatcherWeakReference.get(), mMsgQueueName);
	}

	public BaseController(Model model, INetWorkListener listner,
			String msgQueueName) {
		this();
		if (model != null) {
			this.mModel = model;
		}
		this.mListner = listner;
		this.mMsgQueueName = msgQueueName;
		WelearnHandler.getInstance()
				.registDispatcher(dispatcherWeakReference.get(), mMsgQueueName);
	}

	public void setModel(Model model) {
		this.mModel = model;
	}

	public Handler getHandler() {
		return mHandler;
	}
}
