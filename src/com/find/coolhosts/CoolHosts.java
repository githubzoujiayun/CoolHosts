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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;  

public class CoolHosts extends Activity {  
  
	private boolean root;
	private TextView console;
	private Button oneKey;
	private Button help;
	private Button about;
	private static final String TAG=CoolHosts.class.getSimpleName();
	private static final String MY_AD_UNIT_ID="ca-app-pub-8527554614606787/7985243150";
	private AdView adView;
	private boolean netState=false;
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        downloadHostsTask.execute(Lib.SOURCE);
         
        about=(Button)findViewById(R.id.about);
        oneKey=(Button)findViewById(R.id.onekey);
        help=(Button)findViewById(R.id.help);
        console=(TextView)findViewById(R.id.console);
        
        adView = new AdView(this);
        adView.setAdUnitId(MY_AD_UNIT_ID);
        adView.setAdSize(AdSize.SMART_BANNER);
        LinearLayout layout = (LinearLayout)findViewById(R.id.adLayout);
        layout.addView(adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // 模拟器
//        .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4") // 我的Galaxy Nexus测试手机
        .build();
        adView.loadAd(adRequest);
//        String vv = Lib.getlocalversion(getExternalCacheDir().toString());
//        if (Lib.setVersion(getExternalCacheDir().toString(),vv)) {
//			System.out.println("ok");
//		}
    }  
    public void onResume (){
    	super.onResume();
    	root=RootChecker.hasRoot();
    	  adView.resume();
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
				intent.setData(Uri.parse("http://www.findspace.name"));
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
						appendOnConsole(R.string.copyingnewhosts);
						new FileCopier(CoolHosts.this).execute(getExternalCacheDir() + "/hosts", "/system/etc/hosts");
						oneKey.setBackgroundResource(R.drawable.pressed);
					}else{
						Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
    }
    /**Update the console textview*/
    public void appendOnConsole(final int ...id ){
    	for(int i:id){
    		console.append(getString(i)+"\n");
    	}
    }
    public void appendOnConsole(final String ...strs){
    	for(String tempstr:strs)
    		console.append(tempstr+"\n");
    }
    
    
    
    /**DownLoad hosts file*/
    AsyncTask<String, Void, File> downloadHostsTask = new AsyncTask<String, Void, File>() {
        @Override
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
                System.out.println(getExternalCacheDir());
                f = new File(getExternalCacheDir(), "hosts");
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
                    Lib.setRemoteVersion(f);
                    console.setText("");
                    appendOnConsole(getString(R.string.local_version)+Lib.getlocalversion());
                    appendOnConsole(getString(R.string.remote_version)+Lib.getRemoteVersion());
                    Log.d(TAG, "download success");
                    netState=true;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    };
    
    @Override
    public void onPause() {
      adView.pause();
      super.onPause();
    }



    @Override
    public void onDestroy() {
      adView.destroy();
      super.onDestroy();
    }
    
}  









