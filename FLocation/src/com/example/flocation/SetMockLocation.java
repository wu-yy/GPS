package com.example.flocation;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataOutputStream;

import org.w3c.dom.Text;
import android.annotation.SuppressLint;  

public class SetMockLocation extends Activity {  
    private static final String TAG = "SetLocation";  
    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)
    private LocationManager mLocationManager;  
    private Context mContext;  
    private String mMockProviderName = LocationManager.GPS_PROVIDER;  // 获取probider
   // private String mMockProviderName2 = LocationManager.NETWORK_PROVIDER;  
    private Thread thread;//需要一个线程一直刷新
    private boolean RUN=true;
    boolean isFirstLoc=true;//是否是初次定位
    private double mLongitude = 113.5686111111;   //默认郑州州
    private double mLatitude = 34.8113888888;  
    TextView t1;
    TextView t2;
    Button b1;
    Button distButton;
    EditText ALongtitude;
    EditText ALatitude;
    EditText BLongtitude;
    EditText BLatitude;
    TextView DistAB;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        //获取root 权限
        upgradeRootPermission(getPackageCodePath());  
        
        t1= (TextView)findViewById(R.id.editText1);
        t2=(TextView)findViewById(R.id.editText2);
        t1.setText(String.valueOf(mLongitude));
        t2.setText(String.valueOf(mLatitude));
        b1=(Button)findViewById(R.id.button1);
        
        distButton=(Button)findViewById(R.id.distButton);
        ALongtitude=(EditText)findViewById(R.id.ALongtitude);
         ALatitude=(EditText)findViewById(R.id.BLatitude);
         BLongtitude=(EditText)findViewById(R.id.BLongtitude);
       BLatitude=(EditText)findViewById(R.id.BLatitude);
       DistAB=(TextView)findViewById(R.id.DistAB);
        
        mContext=this;  
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);  
        enableTestProvider();  
     
        
     // 开启线程，一直修改GPS坐标  
        
        thread = new Thread(new Runnable() {  
  
            @Override  
            public void run() {  
                while (RUN) {  
                    try {  
                        Thread.sleep(50);  
                        Log.d("slepp 1s", "sleepsleep sleep");
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                    setLocation2(mLongitude, mLatitude);  
                }  
            }  
        });  
        thread.start();  
       
        b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mLongitude=Double.valueOf(t1.getText().toString());
				mLatitude = Double.valueOf(t2.getText().toString());
				//t1.setText(""); //清空输入
				//t2.setText(""); //清空输入
			}
        	
        
        });
        
        b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mLongitude=Double.valueOf(t1.getText().toString());
				mLatitude = Double.valueOf(t2.getText().toString());
				 
			}
        	
        	
        });
       // setLocation();//设置模拟位置信息，也可以建一个thread不断设置虚拟位置信息。  
        distButton.setOnClickListener(new OnClickListener(){
        	
        	@Override
			public void onClick(View v) {
        		 double lon1=0,lat1=0,lon2=0,lat2=0;
        		 lon1=Double.valueOf(ALongtitude.getText().toString());
        		 lat1=Double.valueOf(ALatitude.getText().toString());
        		 lon2=Double.valueOf(BLongtitude.getText().toString());
        		 lat2=Double.valueOf(BLatitude.getText().toString());
        		 double result=LantitudeLongitudeDist(lon1,lat1,lon2,lat2);
   				 DistAB.setText(String.valueOf(result));
			}
        	
        	
        });
    }   
    public void setLocation2(double longitude,double latitude)
    {
    	  
    	Location loc = new Location(mMockProviderName);//这里是模拟的gps位置信息，当然也可以设置network位置信息了。  
          loc.setAccuracy(Criteria.ACCURACY_FINE);  
    	  loc.setAltitude(110.4f);  
          loc.setTime(System.currentTimeMillis());//设置当前时间  
          loc.setBearing(0F);//add
          loc.setLatitude(mLatitude);           //设置纬度  
          loc.setLongitude(mLongitude);           //设置经度  
          loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());  
          mLocationManager.setTestProviderLocation(mMockProviderName, loc);   
        
    }
    @Override  
    public void onStart() {  
        super.onStart();  
    }  
      
    public void enableTestProvider(){  
    	  ContentResolver res = mContext.getContentResolver();  
          //获取gps的状态，false为关闭，true为开启。  
          boolean gps_enable = Settings.Secure.isLocationProviderEnabled(  
                            res, android.location.LocationManager.GPS_PROVIDER);  
          if(gps_enable){  
              //关闭gps  
              Settings.Secure.setLocationProviderEnabled(res,LocationManager.GPS_PROVIDER, false);  
          }     
          //获取“允许模拟地点”的状态，0为不允许，1为允许。  
          int mock_enable = Settings.Secure.getInt(  
                  res, Settings.Secure.ALLOW_MOCK_LOCATION, 0);    
          if(mock_enable == 0){  
              try {  
                  //开启 允许模拟地点  
                  Settings.Secure.putInt(res, Settings.Secure.ALLOW_MOCK_LOCATION, 1);    
              } catch     (Exception e) {  
                  Log.e(TAG, "write error", e);  
              }  
          }  
          mLocationManager.addTestProvider(mMockProviderName,  
                           "requiresNetwork" == "", "requiresSatellite" == "",  
                           "requiresCell" == "", "hasMonetaryCost" == "",  
                           "supportsAltitude" == "", "supportsSpeed" == "",  
                           "supportsBearing" == "supportsBearing",  
                           Criteria.POWER_LOW,  
                           Criteria.ACCURACY_FINE);  
          mLocationManager.setTestProviderEnabled(mMockProviderName, true);  
          mLocationManager.setTestProviderStatus
          (
             LocationManager.GPS_PROVIDER,
             LocationProvider.AVAILABLE,
             null,
             System.currentTimeMillis()
          );  
        }  
      
    public void unenableTestProvider(){  
        int mock_enable = Settings.Secure.getInt(  
                       mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0);    
        if(mock_enable == 0) return;  
        try {  
            mLocationManager.clearTestProviderEnabled(mMockProviderName);  
            mLocationManager.removeTestProvider(mMockProviderName);  
        } catch (Exception e) {  
            Log.e(TAG, "", e);  
        }  
        try {  
            //关闭 允许模拟地点  
            Settings.Secure.putInt(mContext.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0);    
        } catch (Exception e) {  
            Log.e(TAG, "write error", e);  
        }  
        }  
      
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
        //unenableTestProvider();  
    }  
    
    /** 
     * 转化为弧度(rad) 
     * */  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
      
    /** 
     * 基于余弦定理求两经纬度距离 
     * @param lon1 第一点的精度 
     * @param lat1 第一点的纬度 
     * @param lon2 第二点的精度 
     * @param lat3 第二点的纬度 
     * @return 返回的距离，单位km 
     * */  
    public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
  
        double radLon1 = rad(lon1);  
        double radLon2 = rad(lon2);  
  
        if (radLat1 < 0)  
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south  
        if (radLat1 > 0)  
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north  
        if (radLon1 < 0)  
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west  
        if (radLat2 < 0)  
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south  
        if (radLat2 > 0)  
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north  
        if (radLon2 < 0)  
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west  
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);  
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);  
        double z1 = EARTH_RADIUS * Math.cos(radLat1);  
  
        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);  
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);  
        double z2 = EARTH_RADIUS * Math.cos(radLat2);  
  
        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));  
        //余弦定理求夹角  
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));  
        double dist = theta * EARTH_RADIUS;  
        return dist;  
    }
    
    /** 
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限) 
     *  
     * @return 应用程序是/否获取Root权限 
     */  
    public static boolean upgradeRootPermission(String pkgCodePath) {  
        Process process = null;  
        DataOutputStream os = null;  
        try {  
            String cmd="chmod 777 " + pkgCodePath;  
            process = Runtime.getRuntime().exec("su"); //切换到root帐号  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(cmd + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
        } catch (Exception e) {  
            return false;  
        } finally {  
            try {  
                if (os != null) {  
                    os.close();  
                }  
                process.destroy();  
            } catch (Exception e) {  
            }  
        }  
        return true;  
    }  
}