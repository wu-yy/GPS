# -*- coding: utf-8 -*-
import sys
from com.android.monkeyrunner import MonkeyRunner
from com.android.monkeyrunner import MonkeyDevice
from com.android.monkeyrunner import MonkeyImage

#连接设备
device =MonkeyRunner.waitForConnection()

if not device:
    print >> sys.stderr,"fail"
    sys.exit(1)

#定义要启动的Activity
componentSetMockLocation='com.example.flocation/com.example.flocation.SetMockLocation'

def Run():
	num=1  #迭代数目
        mLongitude=113.568611111  #经度
	mLatitude=34.8113888889  #维度
	while (num < 10):
		#对天下游的操作
                device.touch(650,950,MonkeyDevice.DOWN_AND_UP)#打开app
		#print "打开App"
		MonkeyRunner.sleep(1)  #等待
		device.touch(1000,100,MonkeyDevice.DOWN_AND_UP)  #找到输入框
		MonkeyRunner.sleep(0.1)  #等待
		device.touch(1000,500,MonkeyDevice.DOWN_AND_UP)   #找到手动输入
		MonkeyRunner.sleep(0.1)  #等待
		device.press('KEYCODE_DPAD_UP',MonkeyDevice.DOWN_AND_UP)#向上移动光标
		MonkeyRunner.sleep(0.1)  #等待
		#text=device.getText()
		for i in range(0,16):
			device.press('KEYCODE_DEL', MonkeyDevice.DOWN_AND_UP)
		device.type(str(mLatitude))   #输入维度
		MonkeyRunner.sleep(0.1)  #等待
		device.press('KEYCODE_DPAD_DOWN',MonkeyDevice.DOWN_AND_UP) #向下移动光标
		MonkeyRunner.sleep(0.1)  #等待
		for i in range(0,16):
			device.press('KEYCODE_DEL', MonkeyDevice.DOWN_AND_UP)
		device.type(str(mLongitude)) #输入经度
		MonkeyRunner.sleep(0.4)  #等待
		device.touch(750,1150,MonkeyDevice.DOWN_AND_UP) # 确定输入
		MonkeyRunner.sleep(0.4)  #等待
		device.press('KEYCODE_HOME',MonkeyDevice.DOWN_AND_UP)  #返回Home
		#微信的操作
		MonkeyRunner.sleep(1.5)  #等待1.5s
		device.touch(850,950,MonkeyDevice.DOWN_AND_UP) #打开App
		MonkeyRunner.sleep(2)  #等待2s
		device.touch(600,1900,MonkeyDevice.DOWN_AND_UP) #发现
		MonkeyRunner.sleep(0.1)  #等待
		device.touch(600,800,MonkeyDevice.DOWN_AND_UP)  #打开附近的人
		MonkeyRunner.sleep(9)  #等待3 系统返回附近的人
		result=device.takeSnapshot()
		result.writeToFile('./pictures/'+str(num)+'.png','png') #save to file 保存到文件
		MonkeyRunner.sleep(0.1)  #等待
		device.press('KEYCODE_BACK',MonkeyDevice.DOWN_AND_UP)  #返回上一层
		MonkeyRunner.sleep(1)  #等待
		device.press('KEYCODE_HOME',MonkeyDevice.DOWN_AND_UP)  #返回主菜单
		MonkeyRunner.sleep(0.1)  #等待
		num=num+1
		#mLongitude=mLongitude+0.01  #经度 +1
		mLatitude=mLatitude+0.000555556    #维度  
        
if __name__ == '__main__':
    Run()
        
        
        
        
