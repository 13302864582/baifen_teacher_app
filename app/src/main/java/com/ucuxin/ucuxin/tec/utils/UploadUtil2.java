package com.ucuxin.ucuxin.tec.utils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ucuxin.ucuxin.tec.okhttp.OkHttpUtils;
import com.ucuxin.ucuxin.tec.okhttp.builder.PostFormBuilder;
import com.ucuxin.ucuxin.tec.okhttp.callback.StringCallback;

public class UploadUtil2 {

	public static final String TAG = UploadUtil2.class.getSimpleName();
	public static String UNKNOW_ERROR_MSG = "网络异常";

	public static void upload(String url, Map<String, String> params, Map<String, List<File>> files,
			StringCallback listener, boolean isMainThread, int index) {

		try {
			PostFormBuilder builder = OkHttpUtils.post();

			if (files != null && files.size() > 0) {
				Set<Entry<String, List<File>>> entries = files.entrySet();
				for (Entry<String, List<File>> entry : entries) {
					String key = entry.getKey();
					List<File> fList = entry.getValue();
					if (null != fList) {
						for (File f : fList) {
							if (!f.exists()) {
								ToastUtils.show("文件不存在，请修改文件路径");
								return;
							}
							builder.addFile(key, key, f);
						}
					}
				}
			}

			builder.url(url).params(params)//
					.build()//
					.connTimeOut(5000).writeTimeOut(30000).readTimeOut(30000).execute(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
