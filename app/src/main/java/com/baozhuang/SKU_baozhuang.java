/**
 *
 */
package com.baozhuang;

import java.util.HashMap;

import com.dhl.broadrec;
import com.login.DatabaseHelper;
import com.login.R;
import com.opration.Fenjian_Task;
import com.opration.Jianhuo_Doc;
import com.opration.PlayBeepSound;
import com.rules.LocIdRules;
import com.timeout.Timeout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author
 *
 */
public class SKU_baozhuang extends Activity {
	private static final int SHOW_ANOTHER_ACTIVITY = 0;
	private SharedPreferences sp;
	private String newdate;
	String newtime = null;
	Thread newThread = null; //声明一个子线程

	private SoundPool mSoundPool = null;
	HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();
	private EditText sku_id_data;
	private EditText count_data;

	DatabaseHelper helper;
	SQLiteDatabase db;
	IntentFilter mFilter =null;
	public String bt_data;

	BroadcastReceiver mreceiver = new  BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			bt_data = intent.getStringExtra(broadrec.EXTRA_DATA);

			Editor editor = sp.edit();
			if(sku_id_data.hasFocus())
			{
				sku_id_data.setText(bt_data);
				sku_id_data.setSelection(bt_data.length());
				editor.putString("sku", bt_data);

				mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
				Log.i("user_data", sku_id_data.getText().toString());
				count_data.requestFocus();//获取焦点
			}else if (count_data.hasFocus()) {
				if(LocIdRules.isNumeric(bt_data) && bt_data.length()<8)
				{
					count_data.setText(bt_data);
					count_data.setSelection(bt_data.length());
					editor.putInt("qty", Integer.valueOf(bt_data).intValue());
				}
				Log.i("user_data", count_data.getText().toString());
			}
			editor.commit();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fenjian_sku);
		sku_id_data = (EditText)findViewById(R.id.sku_id_data);
		count_data = (EditText)findViewById(R.id.count_data);

		mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		soundMap.put(1, mSoundPool.load(this, R.raw.test_2k_8820_200ms, 1));
		soundMap.put(2, mSoundPool.load(this, R.raw.error, 1));

		mFilter = new IntentFilter();
		mFilter.addAction(broadrec.ACTION_DATA_AVAILABLE);
		registerReceiver(mreceiver,mFilter);

		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		newdate = sp.getString("NEWTIME", "");
		Log.i("NEWDATE", newdate);

		resetTime();

		newThread = new Thread(new Runnable() {
			@Override
			public void run() {

				newtime = sp.getString("NEW_TIME", null);//
				//获取同步时间
				if(newtime != null)
				{
					Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//创建一个SQLiteHelper对象
					helper = new DatabaseHelper(SKU_baozhuang.this, newtime.substring(0,10) + ".db");
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

					//获取游标对象
					Cursor queryResult = db.rawQuery("select * from ptsdata", null);
					if (queryResult.getColumnCount() != 0) {
						//打印记录
						if (queryResult.moveToLast()) {
							Log.i("info", "user_id: " + queryResult.getString(queryResult.getColumnIndex("user_id"))
									+ " timastamp: " + queryResult.getString(queryResult.getColumnIndex("task_time"))
									+ " String: " + queryResult.getString(queryResult.getColumnIndex("sku"))
							);
						}
					}
					//关闭游标对象
					queryResult.close();
					//关闭数据库
					//db.close();
				}
				else{
					Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
					t.setToNow(); // 取得系统时间。
					int year = t.year;
					int month = t.month + 1;
					int date = t.monthDay;

					newtime = String.valueOf(year)
							+"-"+String.format("%02d",month)
							+"-"+String.format("%02d",date);

					Editor editor = sp.edit();
					editor.putString("NEW_TIME", newtime);
					editor.commit();
					//创建一个SQLiteHelper对象
					helper = new DatabaseHelper(SKU_baozhuang.this, newtime.substring(0,10) + ".db");
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

				}

			}
		},"baozhuang_sku");
		newThread.start();
	}

	public void sku_tijiao(View v)
	{
		if(!TextUtils.isEmpty(sku_id_data.getText().toString()) && !TextUtils.isEmpty(count_data.getText().toString() ))
		{
			Editor editor = sp.edit();
			editor.putString("task_event", "pack");
			editor.putString("sku",  sku_id_data.getText().toString() ); //Integer.parseInt()
			editor.putInt("qty", Integer.parseInt( count_data.getText().toString() ));
			editor.commit();

			record();

			Intent intent = new Intent();
			intent.setClass(this, Box_baozhuang.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	public void sku_back(View v)
	{
		finish();
	}

	private void record()
	{
		db = helper.getWritableDatabase();
		db.execSQL("insert into ptsdata (user_id,task_name,"
				+ "task_event,doc_id,"+"box_id,"+"sku,"+"qty,"
				+ "last_opt_id,"
				+ "pushstate) "
				+ "values ("
				+ "'"+sp.getString("user_id", "")+"'"+","
				+ "'包装',"
				+ "'"
				+ "扫描SKU"+"-"+sp.getString("task_event", "")
				+ "',"
				+  "'"+sp.getString("doc_id", "")+"'"+","
				//+ sp.getInt("task_id",0)+","
				+ "'"+sp.getString("box_id", "")+"'"+","
				+ "'"+sp.getString("sku", "")+"'"+","
				+ sp.getInt("qty", 0)+","
				+ "0,0)");
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
		//resetTime();
		registerReceiver(mreceiver,mFilter);
	}
	@Override
	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be "paused").
		unregisterReceiver(mreceiver);
		//mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除
//        //关闭数据库
//        if(db.isOpen())
//        {
//        	db.close();
//        }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("销毁", "销毁");
		mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除

		//关闭数据库
		if(db.isOpen())
		{
			db.close();
		}

	};


	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//Log.i("TAG", "操作ing");
		//resetTime();
		return super.dispatchTouchEvent(ev);
	}

	private void resetTime() {
		// TODO Auto-generated method stub
		mHandler.removeMessages(SHOW_ANOTHER_ACTIVITY);//從消息隊列中移除
		Message msg = mHandler.obtainMessage(SHOW_ANOTHER_ACTIVITY);
		mHandler.sendMessageDelayed(msg, 1000*60* sp.getInt("timeout", 10) );//無操作?分钟后進入屏保
	}

	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==SHOW_ANOTHER_ACTIVITY)
			{
				//跳到activity
//               Log.i(TAG, "跳到activity");
				Intent intent=new Intent(SKU_baozhuang.this,Timeout.class);
				startActivity(intent);
			}
		}
	};

}
