package com.ucuxin.ucuxin.tec.controller;

import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

/**
 * 消息列表页的控制器
 * @author parsonswang
 *
 */
public class CommunicateController extends BaseController {

	public CommunicateController(Model model, INetWorkListener listner) {
		super(model, listner, CommunicateController.class.getSimpleName());
	}
}
