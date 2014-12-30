package com.find.coolhosts;

import android.os.Bundle;  
import android.app.Activity;  
import android.widget.Toast;  
  
public class MainAct extends Activity {  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        String apkRoot="chmod 777 "+getPackageCodePath();  
        System.out.println(getPackageCodePath());
//        boolean root = RootCheck.RootCommand(apkRoot);  
        boolean root=RootChecker.hasRoot();
        Toast.makeText(this, "root: "+root, Toast.LENGTH_SHORT).show();  
        
    }  
   
}  
