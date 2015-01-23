package com.example.mycoolweather.service;

import com.example.mycoolweather.receiver.AutoUpdateReceiver;
import com.example.mycoolweather.util.HttpCallbackListener;
import com.example.mycoolweather.util.HttpUtil;
import com.example.mycoolweather.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent,int flags,int startId){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
			
		}).start();
		
		AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
		int interval=8*60*60*1000;//8小时的毫秒数
		long triggerAtTime=SystemClock.elapsedRealtime()+interval;
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, i, 0);
		alarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public void updateWeather(){
		SharedPreferences spf=PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode=spf.getString("weather_code", null);
		String address = "http://www.weather.com.cn/data/cityinfo/" +
				weatherCode + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
			
		});
	}

}
