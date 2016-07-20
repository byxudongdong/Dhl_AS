package com.dhl;

import com.login.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Fahuo_menu extends Activity{
	public Button jianhuo_main;
	public Button baozhuang_main;
	public Button fenjian_main;
	public Button back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fahuo_menu);
		jianhuo_main = (Button) findViewById(R.id.jianhuo_main);
		baozhuang_main = (Button) findViewById(R.id.baozhuang_main);
		fenjian_main = (Button) findViewById(R.id.fenjian_main);
		
		
	}

	public void jianhuo_main(View v)
	{
		startActivity( new Intent( Fahuo_menu.this,
              com.opration.Jianhuo_Doc.class));
	}
	
	public void fenjian_menu(View v)
	{
		startActivity( new Intent( Fahuo_menu.this,
              com.opration.Fenjian_Doc.class));
	}
	
	public void baozhuang_menu(View v)
	{
		startActivity( new Intent( Fahuo_menu.this,
              com.baozhuang.Doc_baozhuang.class));
	}
	
	public void fenjian_other(View v)
	{
		startActivity( new Intent( Fahuo_menu.this,
	              com.others.Fahuo_Other.class));
		
	}
	
	public void fahuo_menu_back(View v)
	{
		finish();
	}
}
