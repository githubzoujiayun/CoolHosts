package com.find.coolhosts;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**检查app版本*/
public class CheckCoolHostsVersion{

	Context context;
	public CheckCoolHostsVersion(Context context) {
		this.context=context;
	}
	public String getLocalVersion() throws NameNotFoundException{  
	        // 获取packagemanager的实例  
	        PackageManager packageManager = context.getPackageManager();  
	        // getPackageName()是你当前类的包名，0代表是获取版本信息  
	        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);  
	        String version = packInfo.versionName;  
	        Lib.LOCALCHVERSION=version;
	        return version;  
	}  
	public String getRemoteVersion() throws IOException{
		return Lib.REMOTECHVERSION;
	}
}
