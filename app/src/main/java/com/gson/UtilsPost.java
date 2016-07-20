package com.gson;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Handler;
import android.util.Log;

public class UtilsPost {
	static Boolean result = false;
	static String receiveFail;
	static int receiveSuccess;
	public static Boolean doPost(String url, RequestParams params, final Handler handler) {

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("MyLog", "发送失败");
				Log.i("MyLog", arg1);
				receiveFail = arg1;
				result = false;
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				// TODO Auto-generated method stub
				String data = info.result;//这里是返回值

				if(info.statusCode == 200){
					Log.i("MyLog", "发送成功");
				}
				Log.i("请求结果", String.valueOf(info.statusCode));

				receiveSuccess = info.statusCode;
				result = true;
				//Message message = new Message();
				//message.obj = data;
				//handler.sendMessage(message);
			}
		});
		return result;
	}

	public String getReceiveFail()
	{
		return receiveFail;
	}

	public int getReceiveSuccess()
	{
		return receiveSuccess;
	}
}

