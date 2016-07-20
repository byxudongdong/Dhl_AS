package com.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Setting extends Activity{
	private Button set_timeout,set_service,set_back,locidservice;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

		set_timeout = (Button)findViewById(R.id.set_timeout);
		set_service = (Button)findViewById(R.id.set_service);
		set_back = (Button)findViewById(R.id.set_back);
		locidservice = (Button)findViewById(R.id.locidservice);

		set_timeout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				final EditText et = new EditText(Setting.this);
				et.setInputType(InputType.TYPE_CLASS_NUMBER);
				et.setHint(String.valueOf(sp.getInt("timeout", 10)));

				new AlertDialog.Builder(Setting.this).setTitle("设置超时（分钟）")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								String input = et.getText().toString();
								if (input.equals("")) {
									Toast.makeText(getApplicationContext(), "内容不能为空！" + input, Toast.LENGTH_LONG).show();
								}
								else {
									Editor editor = sp.edit();
									editor.putInt("timeout", Integer.parseInt(input));
									editor.commit();
									Toast.makeText(getApplicationContext(), "成功设置超时时间为：" + input +"分钟", Toast.LENGTH_LONG).show();

								}
							}
						})
						.setNegativeButton("取消", null)
						.show();

			}
		});

		set_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				final EditText et = new EditText(Setting.this);
				et.setHint(sp.getString("service", "http://aux.dhl.com/pts/interface/pushTask"));

				new AlertDialog.Builder(Setting.this).setTitle("设置服务器地址")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								String input = et.getText().toString();
								if (input.equals("")) {
									Toast.makeText(getApplicationContext(), "内容不能为空！" + input, Toast.LENGTH_LONG).show();
								}
								else {
									Editor editor = sp.edit();
									editor.putString("service", input);
									editor.commit();
									Toast.makeText(getApplicationContext(), "成功设置服务器地址为：" + input, Toast.LENGTH_LONG).show();

								}
							}
						})
						.setNegativeButton("取消", null)
						.show();

			}
		});

		locidservice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				final EditText et = new EditText(Setting.this);
				et.setHint(sp.getString("locidservice", "http://aux.dhl.com/pts/interface/getLocIdList"));

				new AlertDialog.Builder(Setting.this).setTitle("设置货架接口")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								String input = et.getText().toString();
								if (input.equals("")) {
									Toast.makeText(getApplicationContext(), "内容不能为空！" + input, Toast.LENGTH_LONG).show();
								}
								else {
									Editor editor = sp.edit();
									editor.putString("locidservice", input);
									editor.commit();
									Toast.makeText(getApplicationContext(), "成功设置货架接口为：" + input, Toast.LENGTH_LONG).show();

								}
							}
						})
						.setNegativeButton("取消", null)
						.show();

			}
		});

		set_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				finish();
			}
		});
	}

}
