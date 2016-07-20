package com.login;

import com.dhl.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText userName, password;
	private CheckBox rem_pw, auto_login;
	private Button btn_login,btn_set;
	private ImageButton btnQuit;
	private String userNameValue,passwordValue;
	private LinearLayout login_dialog_progress_line;
	private SharedPreferences sp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		//获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		userName = (EditText) findViewById(R.id.et_zh);
		password = (EditText) findViewById(R.id.et_mima);
		rem_pw = (CheckBox) findViewById(R.id.cb_mima);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_set = (Button) findViewById(R.id.btn_set);
		btnQuit = (ImageButton)findViewById(R.id.img_btn);
		login_dialog_progress_line = (LinearLayout) findViewById(R.id.login_dialog_progress_line);
		login_dialog_progress_line.setVisibility(View.GONE);

		//判断记住密码多选框的状态
		if(sp.getBoolean("ISCHECK", false))
		{
			//设置默认是记录密码状态
			rem_pw.setChecked(true);
			userName.setText(sp.getString("USER_NAME", ""));    //获取保存的信息
			password.setText(sp.getString("PASSWORD", ""));
			//判断自动登陆多选框状态
			if(sp.getBoolean("AUTO_ISCHECK", false))
			{
				//设置默认是自动登录状态
				auto_login.setChecked(true);
				//跳转界面
				Intent intent = new Intent(LoginActivity.this,LogoActivity.class);
				LoginActivity.this.startActivity(intent);

			}
		}

		btn_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				final EditText et = new EditText(LoginActivity.this);
				et.setInputType(InputType.TYPE_CLASS_NUMBER);

				new AlertDialog.Builder(LoginActivity.this).setTitle("管理员密码")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(et)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								String input = et.getText().toString();
								if (input.equals("")) {
									Toast.makeText(getApplicationContext(), "内容不能为空！" + input, Toast.LENGTH_LONG).show();
								}
								else{
									if(input.equals("1234"))
									{
										//跳转界面
										Intent intent = new Intent(LoginActivity.this,Setting.class);
										LoginActivity.this.startActivity(intent);
									}else {
										Toast.makeText(getApplicationContext(), "验证失败！" + input, Toast.LENGTH_SHORT).show();
										//finish();
									}
								}
							}
						})
						.setNegativeButton("取消", null)
						.show();


			}
		});

		// 登录监听事件  现在默认为用户名为：123 密码：1234
		btn_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userNameValue = userName.getText().toString();
				passwordValue = password.getText().toString();

				if(userNameValue.equals("test")&&passwordValue.equals("123"))
				{
					Toast.makeText(LoginActivity.this,"登录成功", Toast.LENGTH_SHORT).show();
					//登录成功和记住密码框为选中状态才保存用户信息
					if(rem_pw.isChecked())
					{
						//记住用户名、密码、
						Editor editor = sp.edit();
						editor.putString("USER_NAME", userNameValue);
						editor.putString("PASSWORD",passwordValue);
						editor.commit();
					}
					//跳转界面
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					LoginActivity.this.startActivity(intent);
					//finish();

				}else{

					Toast.makeText(LoginActivity.this,"用户名或密码错误，请重新登录", Toast.LENGTH_LONG).show();
				}

			}
		});

		//监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (rem_pw.isChecked()) {

					System.out.println("记住密码已选中");
					sp.edit().putBoolean("ISCHECK", true).commit();

				}else {

					System.out.println("记住密码没有选中");
					sp.edit().putBoolean("ISCHECK", false).commit();

				}

			}
		});

		//监听自动登录多选框事件
		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (auto_login.isChecked()) {
					System.out.println("自动登录已选中");
					sp.edit().putBoolean("AUTO_ISCHECK", true).commit();

				} else {
					System.out.println("自动登录没有选中");
					sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
				}
			}
		});

		btnQuit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	protected void onNewIntent(Intent intent)
	{

	}
}