package com.ucuxin.ucuxin.tec.controller;

import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

public class ChatMsgController extends BaseController {

	public ChatMsgController(Model model, INetWorkListener listner) {
		super(model, listner, ChatMsgController.class.getSimpleName());
	}
}
