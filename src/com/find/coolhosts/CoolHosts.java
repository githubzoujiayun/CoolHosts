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

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;  
import android.annotation.SuppressLint;
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;  

public class CoolHosts extends Activity {  
  
	private boolean root;
	private TextView console;
	private Button oneKey;
	public static final String TAG=CoolHosts.class.getSimpleName();
	private boolean netState=false;
	public static String CACHEDIR;
	private WebView webView;
	private WebDownloader downloadHostsTask;
	@SuppressLint("SetJavaScriptEnabled") 
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        CACHEDIR=getFilesDir().toString();
        Log.v(TAG, CACHEDIR);
        oneKey=(Button)findViewById(R.id.onekey);
        console=(TextView)findViewById(R.id.console);
        webView = (WebView) findViewById(R.id.webView1);  
        
        downloadHostsTask=new WebDownloader(CoolHosts.this);
        downloadHostsTask.execute(Lib.SOURCE,Lib.HOSTSINCACHE);
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本  
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放  
        webView.loadUrl("http://www.findspace.name");  
        webView.setWebViewClient(new WebViewClient(){  
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
                view.loadUrl(url);// 使用当前WebView处理跳转  
                return true;//true表示此事件在此处被处理，不需要再广播  
            }  
            @Override   //转向错误时的处理  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
                Toast.makeText(CoolHosts.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
            }  
        });  
    }  
    public void onResume (){
    	super.onResume();
    	root=RootChecker.hasRoot();
    	if(!root)
        	Toast.makeText(this, R.string.unrooted, Toast.LENGTH_SHORT).show();
    	oneKey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!root){
					Toast.makeText(CoolHosts.this, R.string.unrooted, Toast.LENGTH_SHORT).show();
				}else{
					if(getNetState()){
						new FileDeleter(CoolHosts.this, R.string.deleteoldhosts);
						appendOnConsole(console,false,R.string.copyingnewhosts);
						new FileCopier(CoolHosts.this).execute(CACHEDIR + "/hosts", "/system/etc/hosts");
					}else{
						Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
    }
    /**Update the console textview*/
    public void appendOnConsole(TextView textview,boolean isAppend,final int ...id ){
    	if(!isAppend)console.setText("");
    	for(int i:id){
    		console.append(getString(i)+"\n");
    	}
    }
    public void appendOnConsole(TextView textview,boolean isAppend,final String ...strs){
    	if(!isAppend)console.setText("");
    	for(String tempstr:strs)
    		console.append(tempstr+"\n");
    }
    
    /**显示网页*/
    public void setWebview(String url){
    	webView.loadUrl(url);
    }
    
	public TextView getConsole(){return console;}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "帮助").setIcon(R.drawable.help);
		menu.add(0, 2, 1, "关于").setIcon(R.drawable.about);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {//得到被点击的item的itemId
		case 1://对应的ID就是在add方法中所设定的Id
			setWebview("http://www.findspace.name/easycoding/503");
			break;
		case 2:
			AlertDialog.Builder builderAbout = new AlertDialog.Builder(CoolHosts.this);
			builderAbout.setMessage(R.string.dialog_about);
			builderAbout.setTitle(R.string.about);
			builderAbout.setCancelable(true);
			builderAbout.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				@Override
				public void onClick (DialogInterface dialog, int which){dialog.cancel();}});
			AlertDialog alertAbout = builderAbout.create();
			alertAbout.show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override   
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        // TODO Auto-generated method stub  
        if (keyCode == KeyEvent.KEYCODE_BACK||keyCode==KeyEvent.KEYCODE_HOME) {  
            finish();
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
	public boolean getNetState() {
		return netState;
	}
	public void setNetState(boolean netState) {
		this.netState = netState;
	}  
}









