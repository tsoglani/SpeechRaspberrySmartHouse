**1st way </br>
on command line enter</br>
-nano ~/.config/lxsession/LXDE-pi/autostart</br>
and enter at the end of showing list</br>
-@lxterminal -e /home/pi/Desktop/SmartHouseServer.sh</br>



-enter sudo nano /home/pi/Desktop/SmartHouseServer.sh
     coppy-paste [SmartHouseServer.sh ](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/SmartHouseServer.sh)
-- or copy and paste on Desktop   [SmartHouseServer.sh ](https://github.com/tsoglani/SpeechRaspberrySmartHouse/blob/master/Raspberry_2B-3/SmartHouseServer.sh)  

-sudo chmod 777 /home/pi/Desktop/SmartHouseServer.sh



**2nd way !!
- you  need to change permission on storage file, for example in cmd (command line), you have to change the permission status on /etc/rc.local by writting: 
 "sudo chmod -R 777 /etc/rc.local" </br>

As soon as you do that you will be able to apply the "write and read" permission which will enable you to modify the " /etc/rc.local" file and write the following script.
</br>
Create script:</br>
My example for /etc/rc.local file:  </br>
" 

sudo java -jar /home/pi/Desktop/SmartHouseServer/SmartHouseServer.jar</br>

</br>
exit 0</br>
"


if rc.local doesn't run enter on command line "chmod +x /etc/rc.local"

if this doesnt work clear </br>
sudo java -jar /home/pi/Desktop/SmartHouseServer/SmartHouseServer.jar</br>
from /etc/rc.local</br>



