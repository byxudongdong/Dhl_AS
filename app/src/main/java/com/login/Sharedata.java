package com.login;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Sharedata {
	
	public void editdata( SharedPreferences sp, String key, String string)
	{
		Editor editor = sp.edit();
		editor.putString(key, string);
		editor.commit();
	}
	
	public void creatdb()
	{
		
		
	}
	

}
