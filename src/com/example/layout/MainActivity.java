package com.example.layout;


import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;


public class MainActivity extends Activity {
	private LocationClient mLocationClient;
	private TextView LocationResult;
	private Button startLocation;
	
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="bd09ll";   //bd09ll(百度加密经纬度坐标)
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
		
		mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
		LocationApplication.syn = 1;  //每一次启动程序，将syn置为1；
		Random random = new Random();
		LocationApplication.identify = random.nextInt(100000); //随机生成一个随机数，标识号每次都不同
		
		LocationResult = (TextView)findViewById(R.id.showinfo);
		
		((LocationApplication)getApplication()).mLocationResult = LocationResult;		
		
		 
		startLocation = (Button)findViewById(R.id.sendmessage);
		startLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InitLocation();
				
				if(startLocation.getText().equals(getString(R.string.startlocation))){
					mLocationClient.start();
					
					startLocation.setText(getString(R.string.stoplocation));
				}else{
					mLocationClient.stop();
					startLocation.setText(getString(R.string.startlocation));
				}
				
				
			}
		});

		
		
		Button button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("personInfo");
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onStop();
	}

	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值bd09ll
		int span=2000;		
		option.setScanSpan(span);//设置发起定位请求的间隔时间为2000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	

}
