package com.ucuxin.ucuxin.tec.controller;

import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

/**
 * 主页面消息控制器
 * @author Administrator
 *
 */
public class MainMessageController extends BaseController {

	public MainMessageController(Model model, INetWorkListener listner) {
		super(model, listner, MainMessageController.class.getSimpleName());
	}
}
