package com.example.layout;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.location.NetworkService;

/**
 * 主Application
 */
public class LocationApplication extends Application {
	public LocationClient mLocationClient;	
	public MyLocationListener mMyLocationListener;
	public TextView mLocationResult;
	public BDLocation lastLocation = new BDLocation();
	public static int syn = 1;
	public static int identify; 
	public static String userId;
	//public TextView trigger, exit;
	//public Vibrator mVibrator;

	@Override
	public void onCreate() {
		super.onCreate();
		
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());    
		
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		/*mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);*/
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			//Send Location
			sendLocation(location);
			lastLocation = location;
			
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			logMsg(sb.toString());
			Log.i("BaiduLocationApiDem", sb.toString());
		}
/*
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			logMsg(sb.toString());
		}
*/
	}

	/**
	 * 显示请求字符串
	 * 
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送位置信息
	 * @param location
	 */
	public void sendLocation(BDLocation location) {
		LatLng p1 = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());    //LatLng是百度地图SDK中的GeoPoint
		//GeoPoint p1 = new GeoPoint(lastLocation.getLongitude(), 0);
		LatLng p2 = new LatLng(location.getLatitude(), location.getLongitude());		
		if( DistanceUtil.getDistance(p1, p2) > 5 && location.getRadius() < 150) {
			PositionPostAsyncTask PositionPost = new PositionPostAsyncTask(this, location);  //this是Application的上下文
			PositionPost.execute("");
		}
	}
	
	//位置坐标传送
    static class PositionPostAsyncTask extends AsyncTask<String, Integer, String> {
 
    	private Context myContext;
        private String resultData;
        private BDLocation location;
        
        /**
         * 
         * @param context  Application的上下文
         * @param location  BDLocation的位置信息
         */
        public PositionPostAsyncTask(Context context, BDLocation location) {
            myContext = context;
            this.location = location;
        }
 
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                resultData = InitData();
                Thread.sleep(1000);
            } catch (Exception e) {
             
            }
            return resultData;
        }
        @Override
        protected void onPreExecute() {
        }
        
       protected String InitData() {     	   
    	    //SharedPreferences share = this.getSharedPreferences(Login.SHARE_LOGIN_TAG, 0);
            String str ="";
            String url = "addAlarmInfoAction.action";
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("userId",userId));
            paramList.add(new BasicNameValuePair("identify", String.valueOf(identify)));
            paramList.add(new BasicNameValuePair("syn", String.valueOf(syn++)));
            paramList.add(new BasicNameValuePair("longitude",String.valueOf(location.getLongitude())));
            paramList.add(new BasicNameValuePair("latitude",String.valueOf(location.getLatitude())));
            paramList.add(new BasicNameValuePair("radius",String.valueOf(location.getRadius())));
            paramList.add(new BasicNameValuePair("address",location.getAddrStr()));
            paramList.add(new BasicNameValuePair("type","1"));
            paramList.add(new BasicNameValuePair("state","0"));
            paramList.add(new BasicNameValuePair("time", location.getTime()));
            paramList.add(new BasicNameValuePair("remark", ""));
            Log.i("sendTime",location.getTime());
            str = NetworkService.getPostResult(url, paramList);
            Log.i("msg", str);
            return str;
        }
     
       /**  
        * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）  
        * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置  
        */
        protected void onPostExecute(String result) {
        	Log.i("result", result);
            try {
            JSONTokener jsonParser = new JSONTokener(result);
            JSONObject responseobj = (JSONObject) jsonParser.nextValue();
            if("position".equals(responseobj.getString("headTage")))
            {
                JSONArray neworderlist = responseobj.getJSONArray("response");
                int length = neworderlist.length();
                if (length > 0) {
                    for(int i = 0; i < length; i++){//遍历JSONArray
                        JSONObject jo = neworderlist.getJSONObject(i);
                        //解析为坐标信息
                        float x = Float.parseFloat(jo.getString("x"));
                        float y = Float.parseFloat(jo.getString("y"));
                        float z = Float.parseFloat(jo.getString("z"));
                    }
                }
                else
                {
                }
            }
            else {
        }
        } catch (Exception e) {
        }
     
    }  
    }

}
