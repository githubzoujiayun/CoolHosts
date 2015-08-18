package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**发送get请求，getversion=0 || 1 <br>0：只是获取推送页面的网址<br>1:获取最新的版本号<br>返回值：要显示的网页网址以及最新的程序版本*/
public class SendGetApplication extends AsyncTask<Integer, Void, String> {
	CoolHosts caller;
	int isGetVersion=0;
	public SendGetApplication(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String doInBackground(Integer... url) {
		/**url[0]只允许AndroidClientVersion=1；
		 * 返回的是最新的版本号* */
		HttpClient httpclient=null;
		String getUrl="http://www.findspace.name/adds/coolhosts.php";
		HttpGet get=null;
		StringBuilder builder = new StringBuilder();
		try{
			httpclient=new DefaultHttpClient();
			if(url!=null){
				isGetVersion=url[0];
			}
			get=new HttpGet(getUrl);
			HttpResponse response=httpclient.execute(get);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line+"\n");
				}
				Log.v("Getter", "Your data: " + builder.toString()); //response data
			} else {
				Log.e("Getter", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
            e.printStackTrace();
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
		return builder.toString();
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result==null)Log.e(CoolHosts.TAG, "没有信息");
		else{
			String[] ans=result.split("\n");
			Log.d(CoolHosts.TAG, ans[0]);
			Log.d(CoolHosts.TAG, ans[1]);
			if(isGetVersion==0)
				caller.setWebview(ans[0]);
			else{
				Lib.REMOTECHVERSION=ans[1];
				if(!Lib.REMOTECHVERSION.equals(Lib.LOCALCHVERSION))
					caller.showVersion();
				else{
					Toast.makeText(caller, R.string.nonewversion, Toast.LENGTH_SHORT).show();
				}
			}
			caller.doNextTask();
		}
	}
}
