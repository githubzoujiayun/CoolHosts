package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class GetHostsVersion extends AsyncTask<String, Void, String>{
	CoolHosts caller;
	public GetHostsVersion(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String doInBackground(String... params) {
		try {
			URL url=new URL(Lib.HOSTS_VERSION_URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			return reader.readLine();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		Lib.REMOTE_VERSION=result;
		caller.appendOnConsole(caller.getConsole(),false,caller.getString(R.string.local_version)+Lib.getlocalversion());
		caller.appendOnConsole(caller.getConsole(),true,caller.getString(R.string.remote_version)+Lib.getRemoteVersion());
		caller.setNetState(true);
		caller.doNextTask();
	}

}
