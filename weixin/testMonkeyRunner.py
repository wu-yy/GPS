# -*- coding: cp936 -*-
#����������Ҫ�õ��İ����ಢ�������  
import sys  
from com.android.monkeyrunner import MonkeyRunner as mr  
from com.android.monkeyrunner import MonkeyDevice as md  
from com.android.monkeyrunner import MonkeyImage as mi  
   
#connect device �����豸  
#��һ������Ϊ�ȴ������豸ʱ��  
#�ڶ�������Ϊ�������ӵ��豸  
device = mr.waitForConnection(1.0,'e0d98451')  
if not device:  
    print >> sys.stderr,"fail"  
    sys.exit(1)  
#����Ҫ������Activity  
componentName='com.example.simulate/.ShellActivity'  
#�����ض���Activity  
device.startActivity(component=componentName)  
mr.sleep(3.0)  
#do someting �������ǵĲ���  
#���� helloworld  
device.type('helloworld')  
#����س�  
device.press('KEYCODE_ENTER')  
#return keyboard  
#device.press('KEYCODE_BACK')  
#------  
#takeSnapshot��ͼ  
mr.sleep(3.0)  
result = device.takeSnapshot()  
   
#save to file ���浽�ļ�  
result.writeToFile('./shot1.png','png');  
