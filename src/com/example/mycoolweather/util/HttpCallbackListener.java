package com.example.mycoolweather.util;

public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
