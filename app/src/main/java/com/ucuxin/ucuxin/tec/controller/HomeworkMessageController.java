package com.ucuxin.ucuxin.tec.controller;

import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

/**
 * 抢作业消息控制器
 * @author Administrator
 *
 */
public class HomeworkMessageController extends BaseController {

	public HomeworkMessageController(Model model, INetWorkListener listner) {
		super(model, listner, HomeworkMessageController.class.getSimpleName());
	}
}
