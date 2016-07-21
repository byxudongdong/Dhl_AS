package com.dhl;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gson.FilesOpt;
import com.gson.Root;
import com.lidroid.xutils.util.LogUtils;
import com.login.DatabaseHelper;
import com.login.HttpUser;
import com.login.JavaBean;
import com.login.R;
import com.others.Wait_pandian;
import com.timeout.Timeout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import gettime.GetThisDay;
import gettime.Gettime;

public class Main_menu extends Activity{
	String strFilePath = FilesOpt.getSdCardPath() + "/locidString.txt";
	DatabaseHelper helper;
	SQLiteDatabase db;
	public Button fahuo_main;
	public Button qita_main;
	public Button sync_main;
	public Button back_main;
	private GridLayout main_menu_grid;
	private LinearLayout login_dialog_progress_line;
	private SharedPreferences sp;
	int newref_id;
	String newtime = null;
	String newtimeYesterday = null;
	Thread newThread = null; //声明一个子线程    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		fahuo_main = (Button) findViewById(R.id.fahuo_main);
		qita_main = (Button) findViewById(R.id.qita_main);
		sync_main = (Button) findViewById(R.id.sync_main);
		back_main = (Button) findViewById(R.id.back_main);
		
		main_menu_grid = (GridLayout) findViewById(R.id.main_menu_grid);
        login_dialog_progress_line = (LinearLayout) findViewById(R.id.login_dialog_progress_line);
		
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

//		Editor editor = sp.edit();  //testFunc
//		editor.putString("locidFlag",  "true" );
//		editor.commit();

		if(!sp.getString("locidFlag", "").equals("true")) {
			login_dialog_progress_line.setVisibility(View.VISIBLE);

			fahuo_main.setClickable(false);
			qita_main.setClickable(false);
			sync_main.setClickable(false);
			back_main.setClickable(false);
		}
		else
		{
			login_dialog_progress_line.setVisibility(View.GONE);
		}

		newThread = new Thread(new Runnable() {
		    @Override
	            public void run() {

	            	//newtime = Gettime.main(null);//这里写入子线程需要做的工作
	        		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  
	        		t.setToNow(); // 取得系统时间。  	            		            		
	        		
	        		int year = t.year;  
	        		int month = t.month + 1;  
	        		int date = t.monthDay; 
	        		int hour = t.hour; // 0-23  
	        		int minute = t.minute;  
	        		int second = t.second;
	        		
	        		newtime = String.valueOf(year)
	        				+"-"+String.format("%02d",month)
		            		+"-"+String.format("%02d",date)
		            		+"-"+String.format("%02d",hour)
		            		+"-"+String.format("%02d",minute)
		            		+"-"+String.format("%02d",second);
					//记住同步时间
	        		
	            	if(newtime != null)
	            	{
						Editor editor = sp.edit();
						editor.putString("NEW_TIME", newtime);
						//editor.putInt("ref_id", 1);
						editor.commit();
						//创建一个SQLiteHelper对象
				        helper = new DatabaseHelper(Main_menu.this, newtime.substring(0,10) + ".db");
				        //使用getWritableDatabase()或getReadableDatabase()方法获得SQLiteDatabase对象
				        db = helper.getWritableDatabase();
				        
				        //创建一个表
				        db.execSQL("create table if not exists locid (" +
				                    "_id integer primary key,"				                    
				                    +"loc_id text not null," 
				                    +"String text not null)");
				        
				        db.execSQL("insert into locid (loc_id,String) "
				        		+ "values ('loc_id','0001')");
				        
				        db.execSQL("create table if not exists ptsdata "
				        		+"("			                    				                    
			                    +"ref_id integer primary key," 
			                    +"user_id text not null,"
			                    +"task_time timestamp not null default (datetime('now','localtime')),"
			                    +"task_name text not null,"
			                    +"task_event text,"
			                    +"doc_id text,"
			                    +"task_id text,"
			                    +"loc_id text,"
			                    +"box_id text,"
			                    +"sku text,"
			                    +"qty integer,"
			                    +"last_opt_id integer,"
			                    +"pushstate integer not null"
			                    + ")"
			                    );
				        
				        if(sp.getString("LocIdUpDate", "1970_01_01").substring(0, 10).equals(newtime.substring(0,10)) )
			        	{
				        	Log.i("货架已经更新", "无需更新");
				        }
				        else{
					        String Url = sp.getString("locidservice", "http://aux.dhl.com/pts/interface/getLocIdList");
					        //String Url = "http://www.kuaidi100.com/query?type=shentong&postid=3307313264542";
					        String getdata = HttpUser.getJsonContent(Url);  //请求数据地址
					        //Log.i("网络数据","json-lib，JSON转对象:"+getdata);
					        //String s1 = "{\"loc_id\":[\"001\",\"002\",\"003\",\"004\",\"005\",\"006\",\"007\",\"008\",\"009\",\"010\"]}";
					        //System.out.println("Json转为简单Bean===" + s1); 
					    	//JSON对象 转 JSONModel对象
					    	Root result = JavaBean.getPerson(getdata, com.gson.Root.class);
					    	if(result == null)		//网络请求异常，没有有效数据
					    	{
								String locidTxt = null;
								try {
									locidTxt= FilesOpt.readTxt(strFilePath);
								} catch (Exception e) {
									Log.i("TXT文件读取异常", "无法完成读取");
									e.printStackTrace();
								}
								result = JavaBean.getPerson(locidTxt, com.gson.Root.class);
								if(result != null)
								{
									LogUtils.i("开始插入备份数据*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));

									db.beginTransaction();
									String sql = "insert into locid (loc_id,String) values(?,?)";
									for (int i=0;i<result.getLoc_id().size();i++) {
										SQLiteStatement stat = db.compileStatement(sql);
										stat.bindString(1, "loc_id");
										stat.bindString(2, result.getLoc_id().get(i));
										stat.executeInsert();
									}
									db.setTransactionSuccessful();
									db.endTransaction();

									LogUtils.i("插入备份数据完毕*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));

									Editor editor1 = sp.edit();
									editor1.putString("LocIdUpDate", newtime);
									//editor.putInt("ref_id", 1);
									editor1.commit();
								}else {
									Log.i("TXT文件解析异常", "无法完成解析");
									handler.sendEmptyMessage(0x002);
								}

					    	}
							else		//网络获取数据成功
							{

								FilesOpt.WriteTxtFile( getdata, strFilePath);
						    	//转成String 方便输出
						    	//Log.i("货架列表","json-lib，JSON转对象:"+result.toString());
						    	
						        //if(db.rawQuery("select * from locid", null).moveToNext() == false)
						        {
						        	Boolean opStyle = false;
						        	//插入同步货架记录
						        	if(opStyle)
						        	{
						        		LogUtils.i("开始插入数据*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
							        	for(int i=0;i<result.getLoc_id().size();i++)
							    		{
									        db.execSQL("insert into locid (loc_id,String) "
									        		+ "values ('loc_id','"+result.getLoc_id().get(i)+"')");
							    		}
							        	LogUtils.i("插入数据完毕*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
							        	
						        	}else{	
							            LogUtils.i("开始插入数据*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
							            
							            db.beginTransaction();
							            String sql = "insert into locid (loc_id,String) values(?,?)";
							        	for (int i=0;i<result.getLoc_id().size();i++) {
							        		SQLiteStatement stat = db.compileStatement(sql);
							        		stat.bindString(1, "loc_id");
							        		stat.bindString(2, result.getLoc_id().get(i));
							        		stat.executeInsert();
							        	}
							        	db.setTransactionSuccessful();
							        	db.endTransaction();
							        	
							            LogUtils.i("插入数据完毕*****************"+ new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date()));
							            
										Editor editor1 = sp.edit();
										editor1.putString("LocIdUpDate", newtime);
										//editor.putInt("ref_id", 1);
										editor1.commit();
						        	}
						        	
						        }
					    	}
				        }

				        if(db.isOpen())
				        {
				        	db.close();
				        }
	            	}

	            	handler.sendEmptyMessage(0x001);
	            }
        	},"gettime");
		newThread.start(); //启动线程
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				login_dialog_progress_line.setVisibility(View.GONE);
				fahuo_main.setClickable(true);
				qita_main.setClickable(true);
				sync_main.setClickable(true);
				back_main.setClickable(true);
				Editor editor = sp.edit();
				editor.putString("locidFlag",  "true" ); //货架更新成功
				editor.apply();
			}
			else if(msg.what == 0x002)
			{
	    		Toast.makeText(getApplicationContext(), "更新货架失败！", Toast.LENGTH_LONG).show();
	    		//finish();
//				Editor editor = sp.edit();
//				editor.putString("locidFlag",  "false" ); //货架更新失败
//				editor.apply();
				finish();
			}
			else if(msg.what == 0x003){
				login_dialog_progress_line.setVisibility(View.GONE);
				fahuo_main.setClickable(true);
				qita_main.setClickable(true);
				sync_main.setClickable(true);
				back_main.setClickable(true);
				Log.i("跳过更新阻塞","跳过更新阻塞");
			}
		};
	};
	
	public void fahuo_main(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.dhl.Fahuo_menu.class));
	}
	
	public void other(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.others.Other_func.class));
	}
	
	public void sync(View v)
	{
		startActivity( new Intent( Main_menu.this,
              com.others.Sync.class));
	}

	public void back(View v)
	{
		dialog();
		//finish();
	}
	
	@Override
    protected void onPause() {
        super.onPause();

    }
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
        //关闭数据库

        db.close();

	}
	
	
	protected void dialog() { 
		AlertDialog.Builder builder = new Builder(Main_menu.this); 
		builder.setMessage("建议先同步数据，确定要退出业务操作吗?"); 
		builder.setTitle("提示"); 
		builder.setPositiveButton("确认", 
			new android.content.DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) 
				{ 
					dialog.dismiss(); 
					
					Main_menu.this.finish(); 
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
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
			//dialog(); 
			dialog();
			return false; 
		} 
		return super.onKeyDown(keyCode, event);
	}
}
