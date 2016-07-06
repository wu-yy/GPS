# -*- coding: utf-8 -*- 
from math import*
def Distance1(Lat_A,Lng_A,Lat_B,Lng_B): #第一种计算方法
    ra=6378.140 #赤道半径
    rb=6356.755 #极半径 （km）
    flatten=(ra-rb)/ra  #地球偏率
    rad_lat_A=radians(Lat_A)
    rad_lng_A=radians(Lng_A)
    rad_lat_B=radians(Lat_B)
    rad_lng_B=radians(Lng_B)
    pA=atan(rb/ra*tan(rad_lat_A))
    pB=atan(rb/ra*tan(rad_lat_B))
    xx=acos(sin(pA)*sin(pB)+cos(pA)*cos(pB)*cos(rad_lng_A-rad_lng_B))
    c1=(sin(xx)-xx)*(sin(pA)+sin(pB))**2/cos(xx/2)**2
    c2=(sin(xx)+xx)*(sin(pA)-sin(pB))**2/sin(xx/2)**2
    dr=flatten/8*(c1-c2)
    distance=ra*(xx+dr)
    return distance
def Distance2(lat1,lng1,lat2,lng2):# 第二种计算方法
    radlat1=radians(lat1)  
    radlat2=radians(lat2)  
    a=radlat1-radlat2  
    b=radians(lng1)-radians(lng2)  
    s=2*asin(sqrt(pow(sin(a/2),2)+cos(radlat1)*cos(radlat2)*pow(sin(b/2),2)))  
    earth_radius=6378.137  
    s=s*earth_radius  
    if s<0:  
        return -s  
    else:  
        return s
lat1=32.060255; lng1=118.796877 # 南京
lat2=39.904211; lng2=116.407395 # 北京

#print('Distance1={0:.3f} km'.format(Distance1(lat1,lng1,lat2,lng2)))
#print('Distance2={0:.3f} km'.format(Distance2(lat1,lng1,lat2,lng2)))
def Change(jw):
    a=jw.split('.')
    change=float(a[0])+float(a[1])/60.0+float(a[2])/3600.0
    print change
    return change

exitint=0
while exitint !=1:
    print ("输入第一个点纬度：格式为：度.分.秒")
    jw=raw_input()
    lat1=Change(jw)
    print ("输入第一个点经度：格式为：度.分.秒")
    jw=raw_input()
    lng1=Change(jw)
    print ("输入第二个点纬度：格式为：度.分.秒")
    jw=raw_input()
    lat2=Change(jw)
    print ("输入第二个点经度：格式为：度.分.秒")
    jw=raw_input()
    lng2=Change(jw)
    print('Distance1={0:.3f} km'.format(Distance1(lat1,lng1,lat2,lng2)))
    print('Distance2={0:.3f} km'.format(Distance2(lat1,lng1,lat2,lng2)))
    print ("if input is 1 ,the program is ended.")
    jw=raw_input()
    exitint=int(jw)
    








