package com.others;

import com.login.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Other_func extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		
	}
	
	public void ss(View v)
	{
		startActivity( new Intent( Other_func.this,
              com.others.Wait.class));
	}
	
	public void pandian(View v)
	{
		startActivity( new Intent( Other_func.this,
              com.others.Wait_pandian.class));
	}
	
	public void back(View v)
	{
		finish();
	}

}
