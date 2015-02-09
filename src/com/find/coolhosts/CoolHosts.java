package com.find.coolhosts;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import com.google.android.gms.ads.*;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;  
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;  

public class CoolHosts extends Activity {  
  
	private boolean root;
	private TextView console;
	private TextView chversion;
	private Button oneKey;
	private Button help;
	private Button about;
	private static final String TAG=CoolHosts.class.getSimpleName();
	private boolean netState=false;
	private CheckCoolHostsVersion getVersion;
	public static String CACHEDIR;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        CACHEDIR=getFilesDir().toString();
        Log.v(TAG, CACHEDIR);
        downloadHostsTask.execute(Lib.SOURCE,Lib.HOSTSINCACHE);
        about=(Button)findViewById(R.id.about);
        oneKey=(Button)findViewById(R.id.onekey);
        help=(Button)findViewById(R.id.help);
        console=(TextView)findViewById(R.id.console);
        chversion=(TextView)findViewById(R.id.chversion);
		getVersion=new CheckCoolHostsVersion(this);
		checkCHVersion();
    }  
    public void onResume (){
    	super.onResume();
    	root=RootChecker.hasRoot();
    	if(!root)
        	Toast.makeText(this, R.string.unrooted, Toast.LENGTH_SHORT).show();
    	about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builderAbout = new AlertDialog.Builder(CoolHosts.this);
				builderAbout.setMessage(R.string.dialog_about);
				builderAbout.setTitle(R.string.about);
				builderAbout.setCancelable(true);
				builderAbout.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					@Override
					public void onClick (DialogInterface dialog, int which){dialog.cancel();}});
				AlertDialog alertAbout = builderAbout.create();
				alertAbout.show();
			}
		});
    	help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setData(Uri.parse("http://www.findspace.name/easycoding/503"));
				intent.setAction(Intent.ACTION_VIEW);
				CoolHosts.this.startActivity(intent);
			}
		});
    	oneKey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!root){
					Toast.makeText(CoolHosts.this, R.string.unrooted, Toast.LENGTH_SHORT).show();
				}else{
					if(netState){
						new FileDeleter(CoolHosts.this, R.string.deleteoldhosts);
						appendOnConsole(console,R.string.copyingnewhosts);
						new FileCopier(CoolHosts.this).execute(CACHEDIR + "/hosts", "/system/etc/hosts");
						oneKey.setBackgroundResource(R.drawable.pressed);
					}else{
						Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
    }
    /**Update the console textview*/
    public void appendOnConsole(TextView textview,final int ...id ){
    	for(int i:id){
    		console.append(getString(i)+"\n");
    	}
    }
    public void appendOnConsole(TextView textview,final String ...strs){
    	for(String tempstr:strs)
    		console.append(tempstr+"\n");
    }
    
    
    
    /**DownLoad hosts file*/
    AsyncTask<String, Void, File> downloadHostsTask = new AsyncTask<String, Void, File>() {
    	boolean downhosts=false;
        protected File doInBackground(String... params) {
            File f = null;
            try {
                URL url = new URL(params[0]);
                URLConnection ucon = url.openConnection();
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                f = new File(CACHEDIR, params[1]);
                if(params[1].equals(Lib.HOSTSINCACHE)){
                	downhosts=true;
                }
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(baf.toByteArray());
                fos.close();
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return f;
        }

        @Override
        public void onPostExecute(File f) {
            if (f != null) {
            		try {
            			if(downhosts){
            				Lib.setRemoteVersion(f);
            				console.setText("");
            				appendOnConsole(console,getString(R.string.local_version)+Lib.getlocalversion());
            				appendOnConsole(console,getString(R.string.remote_version)+Lib.getRemoteVersion());
            			}else{
            				chversion.setText("");
            				Lib.REMOTECHVERSION=getVersion.getRemoteVersion();
            				appendOnConsole(chversion, "应用程序本地版本："+Lib.LOCALCHVERSION+"\n最新版本："+Lib.REMOTECHVERSION);
            				if(!Lib.REMOTECHVERSION.equals(Lib.LOCALCHVERSION)){
            					AlertDialog.Builder builderAbout = new AlertDialog.Builder(CoolHosts.this);
            					builderAbout.setMessage(R.string.NeedUpdate);
            					builderAbout.setTitle(R.string.about);
            					AlertDialog alertAbout = builderAbout.create();
            					alertAbout.show();
            				}
            			}
            			Log.d(TAG, "download success");
            			netState=true;
            		} catch (IOException ioe) {
            			ioe.printStackTrace();
            		}
            }
        }
    };
    /**检查app的版本
     * @throws NameNotFoundException */
    
	public void checkCHVersion(){
	    		//获得本地app的版本
			try {
				Lib.LOCALCHVERSION=getVersion.getVersionName();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			Log.v(TAG, "Local"+Lib.LOCALCHVERSION);
			downloadHostsTask.execute(Lib.SOURCE_CH,Lib.CHVERSIONINCACHE);
			
	}
	public TextView getConsole(){return console;}
//	AsyncTask<String, Integer, String>
}









