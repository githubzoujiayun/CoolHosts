package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public final class Lib {
	static final String SOURCE="http://www.findspace.name/adds/hosts2";
	static final String REMOTEVERSION="";
	static final String SOURCE_CH="http://googler.sinaapp.com/coolhostsversion";
	private static final String TAG=Lib.class.getSimpleName();
	static final String NOT_EXIST="Don't Exist.";
	static final String READ_ERROR="Read Error.";
	static final String TIMEMARK_HEAD="#+UPDATE_TIME";
	static final String LOG_NAME="CoolHosts";
	static final String HOSTSPATH="/system/etc/hosts";
	static String REMOTE_VERSION="";
	static String LOCALCHVERSION="";
	static String REMOTECHVERSION="";
	
	//存在本地文件名
	static final String HOSTSINCACHE="hosts";
	
	
	
	
    /**Get The local version mark
     * getExternalCacheDir
     * */
    public static String getlocalversion(){
    	String versionText="";
    	File f=new File("/system/etc/hosts");
    	try {
    		FileReader freader2=new FileReader(f);
    		BufferedReader breader2=new BufferedReader(freader2);
			int i=0;
			String tempstr;
			boolean getit=false;
			while(i<5){
				tempstr=breader2.readLine();
				if(tempstr!=null&&tempstr.contains(Lib.TIMEMARK_HEAD)){
					versionText=tempstr.substring(Lib.TIMEMARK_HEAD.length()+1);
					getit=true;
					break;
				};
				i++;
			}
			Log.d(TAG, "Got local hosts");
			if (!getit) {
				versionText=Lib.NOT_EXIST;
			}
			breader2.close();
		} catch (FileNotFoundException e) {
			versionText=Lib.NOT_EXIST;
			e.printStackTrace();
		} catch (IOException e) {
			versionText=Lib.READ_ERROR;
			e.printStackTrace();
		}
    	return versionText;
    }
	public static void setRemoteVersion(File f) throws IOException{
		try {
			BufferedReader breader=new BufferedReader(new FileReader(f));
			String line;
			while((line=breader.readLine())!=null){
				if(line.startsWith(Lib.TIMEMARK_HEAD)){
					REMOTE_VERSION=line.substring(Lib.TIMEMARK_HEAD.length()+1);
					break;
				}
			}
			breader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
	}
	public static String getRemoteVersion(){
		return REMOTE_VERSION;
	}
	
	
}
