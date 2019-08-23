package com.ucuxin.ucuxin.tec.controller;

import com.ucuxin.ucuxin.tec.manager.INetWorkListener;
import com.ucuxin.ucuxin.tec.model.Model;

/**
 * 抢问题消息控制器
 * @author Administrator
 *
 */
public class QuestionMessageController extends BaseController {

	public QuestionMessageController(Model model, INetWorkListener listner) {
		super(model, listner, QuestionMessageController.class.getSimpleName());
	}
}
