package com.others;

import com.login.DatabaseHelper;
import com.login.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class Fahuo_Other extends Activity {
	DatabaseHelper helper;
	SQLiteDatabase db;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程
	private SharedPreferences sp;

	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fahuo_other);

		editText = (EditText) findViewById(R.id.editText1);

		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);

		newThread = new Thread(new Runnable() {
			@Override
			public void run() {

				Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
				t.setToNow(); // 取得系统时间。
				int year = t.year;
				int month = t.month + 1;
				int date = t.monthDay;
//			            		int hour = t.hour; // 0-23
//			            		int minute = t.minute;
//			            		int second = t.second;

				newtime = String.valueOf(year)
						+"-"+String.format("%02d",month)
						+"-"+String.format("%02d",date);

				Editor editor = sp.edit();
				editor.putString("NEW_TIME", newtime);
				editor.commit();
				//创建一个SQLiteHelper对象
				helper = new DatabaseHelper(Fahuo_Other.this, newtime.substring(0,10) + ".db");
				//使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
				db = helper.getWritableDatabase();

				//创建一个表
				db.execSQL("create table if not exists ptsdata "
						+"("
						+"ref_id integer primary key,"
						+"user_id text not null,"
						+"task_time timestamp not null default (datetime('now','localtime')),"
						+"task_name text not null,"
						+"task_event text,"
						+"doc_id integer,"
						+"task_id integer,"
						+"loc_id text,"
						+"box_id text,"
						+"sku text,"
						+"qty integer,"
						+"last_opt_id integer,"
						+"pushstate integer not null"
						+ ")"
				);
				start_record();
			}
		},"fahuo_other");
		newThread.start();
	}

	public void back(View v)
	{
		dialog();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
			dialog();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
		Log.i("TimeOut", "进入打包操作");
	}
	@Override
	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be "paused").
		//关闭数据库
		db.close();
		Log.i("TimeOut", "退出&销毁打包操作");
	}

	private void start_record()
	{
		db = helper.getWritableDatabase();

		db.execSQL("insert into ptsdata (user_id,task_name,"
				+ "task_event,last_opt_id,"
				+ "pushstate) "
				+ "values ("
				+ "'"+sp.getString("user_id", "")+"'"+","
				+ "'出货信息核验','开始',"
				//+  sp.getInt("doc_id", 0)+","
				+ "0,0)");

	}

	private void end_record()
	{
		db = helper.getWritableDatabase();

		db.execSQL("insert into ptsdata (user_id,task_name,"
				+ "task_event,qty,last_opt_id,"
				+ "pushstate) "
				+ "values ("
				+ "'"+sp.getString("user_id", "")+"'"+","
				+ "'出货信息核验','结束',"
				+  Integer.parseInt(editText.getText().toString())+","
				+ "0,0)");

	}

	private void end_record_1()
	{
		db = helper.getWritableDatabase();

		db.execSQL("insert into ptsdata (user_id,task_name,"
				+ "task_event,qty,last_opt_id,"
				+ "pushstate) "
				+ "values ("
				+ "'"+sp.getString("user_id", "")+"'"+","
				+ "'出货信息核验','结束',"
				+"0,"
				+ "0,0)");

	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(Fahuo_Other.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which)
					{
						if(!TextUtils.isEmpty(editText.getText().toString()))
						{
							dialog.dismiss();
							end_record();
							Fahuo_Other.this.finish();
						}else{
							dialog.dismiss();
							end_record_1();
							Fahuo_Other.this.finish();

						}
					}
				}
		);
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}
		);
		builder.create().show();
	}
}
