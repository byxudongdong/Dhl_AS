package com.dhl;

import com.login.DatabaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import android.util.Log;

public class Mythread extends Thread {
	Context mContext;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;

	public Mythread(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public void run() {
		//获得实例对象
		sp = this.mContext.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);
		newtime = sp.getString("NEW_TIME", null);//
		//获取同步时间
		if(newtime != null)
		{
			Editor editor = sp.edit();
			editor.putString("NEW_TIME", newtime);
			editor.commit();
			//创建一个SQLiteHelper对象
			DatabaseHelper helper = new DatabaseHelper(this.mContext, newtime.substring(0,10) + ".db");
			//使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
			SQLiteDatabase db = helper.getWritableDatabase();

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

			db.execSQL("insert into ptsdata (user_id,task_name,"
					+ "task_event,doc_id,"+"task_id,"+"box_id,"+"sku,"+"qty,"
					+ "last_opt_id,"
					+ "pushstate) "
					+ "values ("
					+ "'"+sp.getString("user_id", "")+"'"+","
					+ "'包装',"
					+ "'"
					+ "扫描SKU"+"-"+sp.getString("task_event", "")
					+ "',"
					+ sp.getInt("doc_id", 0)+","
					+ sp.getInt("task_id",0)+","
					+ "'"+sp.getString("box_id", "")+"'"+","
					+ "'"+sp.getString("sku", "")+"'"+","
					+ sp.getInt("qty", 0)+","
					+ "0,0)");

			//获取游标对象
			Cursor queryResult = db.rawQuery("select * from ptsdata", null);
			if (queryResult.getColumnCount() != 0) {
				//打印记录
				while (queryResult.moveToNext()) {
					Log.i("info", "user_id: " + queryResult.getString(queryResult.getColumnIndex("user_id"))
							+ " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
							+ " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
					);
				}
			}
			//关闭游标对象
			queryResult.close();
			//关闭数据库
			db.close();
		}
		else{
			Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
			t.setToNow(); // 取得系统时间。
			int year = t.year;
			int month = t.month + 1;
			int date = t.monthDay;

			newtime = mContext.getString(year)+"-"+mContext.getString(month)+"-"+mContext.getString(date);
			Editor editor = sp.edit();
			editor.putString("NEW_TIME", newtime);
			editor.commit();
			//创建一个SQLiteHelper对象
			DatabaseHelper helper = new DatabaseHelper(this.mContext, newtime.substring(0,10) + ".db");
			//使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
			SQLiteDatabase db = helper.getWritableDatabase();

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

			db.execSQL("insert into ptsdata (user_id,task_name,"
					+ "task_event,doc_id,"+"task_id,"+"box_id,"+"sku,"+"qty,"
					+ "last_opt_id,"
					+ "pushstate) "
					+ "values ("
					+ "'"+sp.getString("user_id", "")+"'"+","
					+ "'包装',"
					+ "'扫描SKU',"
					+ sp.getInt("doc_id", 0)+","
					+ sp.getInt("task_id",0)+","
					+ "'"+sp.getString("box_id", "")+"'"+","
					+ "'"+sp.getString("sku", "")+"'"+","
					+ sp.getInt("qty", 0)+","
					+ "0,0)");

			//关闭数据库
			db.close();
		}
	}
}

