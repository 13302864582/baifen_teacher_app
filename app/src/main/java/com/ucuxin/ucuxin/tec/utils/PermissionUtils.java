package com.ucuxin.ucuxin.tec.utils;

import com.ucuxin.ucuxin.tec.TecApplication;

import android.content.pm.PackageManager;

/**
 * 权限工具类
 * 
 * @author Administrator
 *
 */
public class PermissionUtils {

	/**
	 * 判断应用是否具有某个权限
	 * @param permissionStr
	 * @param packageName
	 * @return
	 */
	public static boolean checkPermissionIsExist(String permissionStr, String packageName) {
		PackageManager pm = TecApplication.getContext().getPackageManager();
		boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(permissionStr, packageName));
		// showToast("有这个权限");
// showToast("木有这个权限");
		return permission;

	}

}
