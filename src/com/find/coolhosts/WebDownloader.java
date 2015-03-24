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

import android.os.AsyncTask;
import android.util.Log;

public class WebDownloader extends AsyncTask<String, Void, File>{
	CoolHosts caller;
	public WebDownloader(CoolHosts caller) {
		this.caller=caller;
	}
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
            f = new File(CoolHosts.CACHEDIR, params[1]);
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
    				caller.appendOnConsole(caller.getConsole(),false,caller.getString(R.string.local_version)+Lib.getlocalversion());
    				caller.appendOnConsole(caller.getConsole(),true,caller.getString(R.string.remote_version)+Lib.getRemoteVersion());
        			Log.d(CoolHosts.TAG, "download success");
        			caller.setNetState(true);
        			caller.doNextTask();
        		} catch (IOException ioe) {
        			ioe.printStackTrace();
        		}
        }
    }

}
