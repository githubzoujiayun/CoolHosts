package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import android.os.AsyncTask;

/**发送get请求，返回值：要显示的网页网址以及最新的程序版本*/
public class SendGetApplication extends AsyncTask<String, Void, String[]> {
	CoolHosts caller;
	public SendGetApplication(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String[] doInBackground(String... url) {
		/**url[0]只允许AndroidClientVersion=1；
		 * 返回的是最新的版本号* */
		String ans[]=new String[5];
		CloseableHttpClient httpclient=null;
		String getUrl="http://www.findspace.name/adds/coolhosts.php";
		HttpGet get=null;
		try{
			httpclient=HttpClients.createDefault();
			if(url!=null){
				getUrl+="?&"+url[0];
			}
			get=new HttpGet(getUrl);
			HttpResponse response=httpclient.execute(get);
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				InputStream is=entity.getContent();
				BufferedReader in=new BufferedReader(new InputStreamReader(is));
				String line="";
				int i=0;
				while((line=in.readLine())!=null)
					ans[i++]=line;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		return ans;
	}
	@Override
	protected void onPostExecute(String result[]) {
		super.onPostExecute(result);
		caller.setWebview(result[0]);
	}
}
