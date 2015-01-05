package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public final class Lib {
	static final String SOURCE="http://www.findspace.name/adds/hosts";
	private static final String TAG=Lib.class.getSimpleName();
	static final String NOT_EXIST="Don't Exist.";
	static final String READ_ERROR="Read Error.";
	static final String TIMEMARK_HEAD="#+UPDATE_TIME";
	static final String LOG_NAME="CoolHosts";
	static final String HOSTSPATH="/system/etc/hosts";
	static String REMOTE_VERSION="";
	/**Set the local version mark.
	 * 
	 * getExternalCacheDir()
     * */
    public static boolean setVersion(String dir,String versionText) {
    	File f=new File(dir,"version");
    	try {
			FileOutputStream fileos=new FileOutputStream(f);
			fileos.write(versionText.getBytes());
			fileos.close();
			return true;
		} catch (FileNotFoundException e) {
			try {
				f.createNewFile();
				setVersion(dir,versionText);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return false;
	}
    
    
    /**Get The local version mark
     * getExternalCacheDir
     * */
    public static String getlocalversion(String dir){
    	String versionText="";
    	File f=new File(dir,"version");
    	try {
			FileReader freader=new FileReader(f);
			BufferedReader breader=new BufferedReader(freader);
			versionText=breader.readLine();
			breader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	f=new File("/system/etc/hosts");
    	try {
			FileReader freader2=new FileReader(f);
			BufferedReader breader2=new BufferedReader(freader2);
			int i=0;
			String tempstr;
			boolean getit=false;
			while(i<4){
				tempstr=breader2.readLine();
				if(tempstr.contains(Lib.TIMEMARK_HEAD)){
					versionText=tempstr.substring(Lib.TIMEMARK_HEAD.length()+1);
					getit=true;
					Log.d(TAG, "Got local hosts");
					break;
				};
			}
			if (!getit) {
				versionText=Lib.NOT_EXIST;
			}
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
