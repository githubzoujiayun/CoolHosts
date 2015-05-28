package com.find.coolhosts;


import java.util.LinkedList;
import java.util.Queue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	public CheckCoolHostsVersion getVersion;
	
	private enum TASK
	{
		DOWNHOSTS,COPYNEWHOSTS,DELETEOLDHOSTS,GETURL,GETCLVERSION
	}
	private Queue <TASK> taskQueue=null;
	
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
        taskQueue = new LinkedList<TASK>();
        downloadHostsTask=new WebDownloader(CoolHosts.this);
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本  
        webView.getSettings().setBuiltInZoomControls(false);//设置使支持缩放  
        webView.loadUrl("http://www.findspace.name");  
//        if(Build.VERSION.SDK_INT >= 19) {
//	        webView.getSettings().setLoadsImagesAutomatically(true);
//	    } else {
//	        webView.getSettings().setLoadsImagesAutomatically(false);
//	    }
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
        try {
        	getVersion=new CheckCoolHostsVersion(CoolHosts.this);
			getVersion.getLocalVersion();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        taskQueue.add(TASK.DOWNHOSTS);
        taskQueue.add(TASK.GETURL);
        doNextTask();
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
						taskQueue.add(TASK.DELETEOLDHOSTS);
						taskQueue.add(TASK.COPYNEWHOSTS);
						doNextTask();
					}else{
						Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
					}
					
				}
			}
		});
//    	oneKey.setOnClickListener(new OnClickListener() {
//    		@Override
//    		public void onClick(View v) {
//    			if(!root){
//    				Toast.makeText(CoolHosts.this, R.string.unrooted, Toast.LENGTH_SHORT).show();
//    			}else{
//    				if(getNetState()){
//    					
//    				}else{
//    					Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
//    				}
//    				
//    			}
//    		}
//    	});
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
		menu.add(0, 1, 0,R.string.help ).setIcon(R.drawable.help);
		menu.add(0, 2, 1, R.string.about).setIcon(R.drawable.about);
		menu.add(0,3,2,R.string.updatechversion);
		menu.add(0,4,3,R.string.emptyhosts);
		menu.add(0,5,4,R.string.customhostsaddress);
		menu.add(0,6,5,R.string.catHosts);
		return super.onCreateOptionsMenu(menu);
	}
	//
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {//得到被点击的item的itemId
		case 1:
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
		case 3:
			taskQueue.add(TASK.GETCLVERSION);
			doNextTask();
			break;
		case 4:
			taskQueue.add(TASK.DELETEOLDHOSTS);
			doNextTask();
			break;
		case 5:
			Toast.makeText(this, R.string.note, Toast.LENGTH_SHORT).show();
			break;
		case 6:
			catHosts();
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
	public void showVersion(){
		AlertDialog.Builder builderAbout = new AlertDialog.Builder(CoolHosts.this);
		builderAbout.setMessage(getString(R.string.local_version)+Lib.LOCALCHVERSION+"\n"+getString(R.string.remote_version)+Lib.REMOTECHVERSION+"\n"+getString(R.string.updatechnote));
		builderAbout.setTitle(R.string.updatechversion);
		builderAbout.setCancelable(true);
		builderAbout.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick (DialogInterface dialog, int which){dialog.cancel();}});
		AlertDialog alertAbout = builderAbout.create();
		alertAbout.show();
	}
	public void doNextTask(){
		if(taskQueue!=null && taskQueue.peek()!=null){
			switch(taskQueue.remove()){
			case COPYNEWHOSTS:
				new FileCopier(CoolHosts.this).execute(CACHEDIR + "/hosts", "/system/etc/hosts");
				break;
			case DELETEOLDHOSTS:
				new FileCopier(CoolHosts.this).execute(null, "/system/etc/hosts");
				break;
			case DOWNHOSTS:
				downloadHostsTask.execute(Lib.SOURCE,Lib.HOSTSINCACHE);
				break;
			case GETURL:
		        new SendGetApplication(CoolHosts.this).execute(0);
				break;
			case GETCLVERSION:
				new SendGetApplication(CoolHosts.this).execute(1);
				break;
			default:
				break;
			
			}
		}
	}
	/**cat hosts' content*/
	public void catHosts(){
		Intent catIntent=new Intent(CoolHosts.this,CatHosts.class);
		CoolHosts.this.startActivity(catIntent);
	}
	
}









