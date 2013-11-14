package com.emergentbit.krispysgamecontrollersamples;

import android.app.Application;

public class SampleApplication extends Application {
	
	private static SampleApplication sInstance;
	
	public static SampleApplication getInstance() {
		return sInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}

}
