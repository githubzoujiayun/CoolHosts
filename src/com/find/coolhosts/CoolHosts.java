package com.find.coolhosts;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.os.Bundle;  
import android.os.Environment;
import android.app.Activity;  
import android.widget.Toast;  
  
public class CoolHosts extends Activity {  
  
	String version; 
	
	
	
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        String apkRoot="chmod 777 "+getPackageCodePath();  
        System.out.println(getPackageCodePath());
//        boolean root = RootCheck.RootCommand(apkRoot);  
        boolean root=RootChecker.hasRoot();
        Toast.makeText(this, "root: "+root, Toast.LENGTH_SHORT).show();  
//        downloadHostsTask.execute(Lib.SOURCE);
//        String vv = Lib.getlocalversion(getExternalCacheDir().toString());
//        System.out.println(vv);
//        if (Lib.setVersion(getExternalCacheDir().toString(),vv)) {
//			System.out.println("ok");
//		}
    }  
    public void onResume (){
    	super.onResume();
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
//                try {
//                    setVersion(f);
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                }
            }
        }
    };
    
    
    
}  









