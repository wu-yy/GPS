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
    private static final  double EARTH_RADIUS = 6378137;//����뾶(��λm)
    private LocationManager mLocationManager;  
    private Context mContext;  
    private String mMockProviderName = LocationManager.GPS_PROVIDER;  // ��ȡprobider
   // private String mMockProviderName2 = LocationManager.NETWORK_PROVIDER;  
    private Thread thread;//��Ҫһ���߳�һֱˢ��
    private boolean RUN=true;
    boolean isFirstLoc=true;//�Ƿ��ǳ��ζ�λ
    private double mLongitude = 113.5686111111;   //Ĭ��֣����
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
        //��ȡroot Ȩ��
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
     
        
     // �����̣߳�һֱ�޸�GPS����  
        
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
				//t1.setText(""); //�������
				//t2.setText(""); //�������
			}
        	
        
        });
        
        b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mLongitude=Double.valueOf(t1.getText().toString());
				mLatitude = Double.valueOf(t2.getText().toString());
				 
			}
        	
        	
        });
       // setLocation();//����ģ��λ����Ϣ��Ҳ���Խ�һ��thread������������λ����Ϣ��  
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
    	  
    	Location loc = new Location(mMockProviderName);//������ģ���gpsλ����Ϣ����ȻҲ��������networkλ����Ϣ�ˡ�  
          loc.setAccuracy(Criteria.ACCURACY_FINE);  
    	  loc.setAltitude(110.4f);  
          loc.setTime(System.currentTimeMillis());//���õ�ǰʱ��  
          loc.setBearing(0F);//add
          loc.setLatitude(mLatitude);           //����γ��  
          loc.setLongitude(mLongitude);           //���þ���  
          loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());  
          mLocationManager.setTestProviderLocation(mMockProviderName, loc);   
        
    }
    @Override  
    public void onStart() {  
        super.onStart();  
    }  
      
    public void enableTestProvider(){  
    	  ContentResolver res = mContext.getContentResolver();  
          //��ȡgps��״̬��falseΪ�رգ�trueΪ������  
          boolean gps_enable = Settings.Secure.isLocationProviderEnabled(  
                            res, android.location.LocationManager.GPS_PROVIDER);  
          if(gps_enable){  
              //�ر�gps  
              Settings.Secure.setLocationProviderEnabled(res,LocationManager.GPS_PROVIDER, false);  
          }     
          //��ȡ������ģ��ص㡱��״̬��0Ϊ������1Ϊ����  
          int mock_enable = Settings.Secure.getInt(  
                  res, Settings.Secure.ALLOW_MOCK_LOCATION, 0);    
          if(mock_enable == 0){  
              try {  
                  //���� ����ģ��ص�  
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
            //�ر� ����ģ��ص�  
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
     * ת��Ϊ����(rad) 
     * */  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
      
    /** 
     * �������Ҷ���������γ�Ⱦ��� 
     * @param lon1 ��һ��ľ��� 
     * @param lat1 ��һ���γ�� 
     * @param lon2 �ڶ���ľ��� 
     * @param lat3 �ڶ����γ�� 
     * @return ���صľ��룬��λkm 
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
        //���Ҷ�����н�  
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));  
        double dist = theta * EARTH_RADIUS;  
        return dist;  
    }
    
    /** 
     * Ӧ�ó������������ȡ RootȨ�ޣ��豸�������ƽ�(���ROOTȨ��) 
     *  
     * @return Ӧ�ó�����/���ȡRootȨ�� 
     */  
    public static boolean upgradeRootPermission(String pkgCodePath) {  
        Process process = null;  
        DataOutputStream os = null;  
        try {  
            String cmd="chmod 777 " + pkgCodePath;  
            process = Runtime.getRuntime().exec("su"); //�л���root�ʺ�  
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