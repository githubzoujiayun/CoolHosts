package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class CatHosts extends Activity {
	
	EditText showHosts;
	static final String TAG="CatHosts";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cathosts);
		
		showHosts=(EditText)findViewById(R.id.showHosts);
		showHosts.setText("Loading...");
		showHosts.setKeyListener(null);
		readHosts.execute();
		
	}

	// read hosts from /system/etc/hosts
	AsyncTask<Integer, Void, String> readHosts = new AsyncTask<Integer, Void, String>() {

		@Override
		protected String doInBackground(Integer... params) {
			try {
				File file=new File(Lib.HOSTSPATH);
				FileInputStream fis=new FileInputStream(file);
				byte[]buffer=new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				String res=EncodingUtils.getString(buffer, "UTF-8");
				Log.d(TAG, res);
				return res;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(TAG, "read error");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onPostExecute(String contents) {
			showHosts.setText(contents);
		}
	};
}
