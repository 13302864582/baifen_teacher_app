package com.ucuxin.ucuxin.tec.manager;


public interface INetWorkListener {

	void onPre();
	
	void onException();

	void onAfter(String jsonStr, int msgDef);
	
	void onDisConnect();
}
